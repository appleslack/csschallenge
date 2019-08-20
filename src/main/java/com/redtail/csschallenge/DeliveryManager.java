package com.redtail.csschallenge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DeliveryManager The delivery manager is responsible for scheduling a delivery
 * for each order and once the order has moved through the Kitchen, it owns the
 * Order until completed (whether delivered or not). After the Kitchen has
 * finished with the Order, it's placed on the appropriate DeliveryShelf.
 * 
 * The DeliverManager is also responsible for keeping track (updating) the decay
 * for each order one each shelf and trashing the order if the delay has reached
 * zero.
 */
public final class DeliveryManager {
    private static final DeliveryManager THE_INSTANCE = new DeliveryManager();

    private final DeliveryShelf hotShelf = new DeliveryShelf(DeliveryShelfType.HOT);
    private final DeliveryShelf coldShelf = new DeliveryShelf(DeliveryShelfType.COLD);
    private final DeliveryShelf frozenShelf = new DeliveryShelf(DeliveryShelfType.FROZEN);
    private final DeliveryShelf overflowShelf = new DeliveryShelf(DeliveryShelfType.OVERFLOW);

    private ExecutorService driverThreadPool = null;

    private DeliveryManager() {
        Runnable decayUpdater = () -> {
            while (true) {
                try {
                    // Don't let shelves be modified while doing this operation
                    synchronized (hotShelf) {
                        decayOrdersOnShelf(hotShelf);
                    }
                    synchronized (coldShelf) {
                        decayOrdersOnShelf(coldShelf);
                    }
                    synchronized (frozenShelf) {
                        decayOrdersOnShelf(frozenShelf);
                    }
                    synchronized (overflowShelf) {
                        decayOrdersOnShelf(overflowShelf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception caught while decaying orders: " + e);
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(decayUpdater).start();
    }

    /*
     * getShelfStatusInfo
     * 
     * This is called by a client (via REST API) to get the current order status of all the
     * shelves and sends back a json formatted string of each item on a shelf, indexed by
     * the shelf name. 
     * 
     */
    public String getShelfStatusInfo() {
        HashMap<String, List<Order>> shelfInfoMap = new HashMap<>();
        shelfInfoMap.put("hot", hotShelf.getOrdersOnShelf());
        shelfInfoMap.put("cold", coldShelf.getOrdersOnShelf());
        shelfInfoMap.put("frozen", frozenShelf.getOrdersOnShelf());
        shelfInfoMap.put("overflow", overflowShelf.getOrdersOnShelf());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(shelfInfoMap);
        } catch (Exception e) {
            System.out.println("Exception while writing JSON String: " + e);
        }

        return jsonString;
    }

    public boolean anyOrdersOnShelves() {
        boolean ordersOnShelves = false;
        synchronized (hotShelf) {
            if( hotShelf.hasOrdersOnShelf() ) {
                return true;
            }
            else if( coldShelf.hasOrdersOnShelf()) {
                return true;
            }
            else if( frozenShelf.hasOrdersOnShelf()) {
                return true;
            }
            else if( overflowShelf.hasOrdersOnShelf()) {
                return true;
            }
        }
        return ordersOnShelves;
    }
    /*
     * Iterate through orders on the given shelf and update their decay value.
     * Return orders that have a decay indicating they should be trashed (<=0)
     * decayValue = ([shelf life] - [order age]) - ([decay rate] * [order age])
     */
    private void decayOrdersOnShelf(DeliveryShelf shelf) {
        List<Order> orders = shelf.getOrdersOnShelf();
        List<Order> trashOrders = null;

        Date now = new Date();
        for (Order order : orders) {
            Date prepDate = order.getOrderPreparedDate();

            // For orders not prepared or orders already in the trash, no further processing
            // needed.
            if (prepDate == null || order.getOrderStatus() == OrderStatus.TRASHED) {
                continue;
            }
            int shelfLife = order.getItem().getShelfLife();
            double decayRate = order.getItem().getDecayRate();
            // Decay rate doubles for items on overflow shelf
            if (shelf.shelfType() == DeliveryShelfType.OVERFLOW) {
                decayRate *= 2.0;
            }
            int orderAge = (int) (now.getTime() - prepDate.getTime()) / 1000;
            int decay = (shelfLife - orderAge) - (int) (decayRate * orderAge);
            double normalizedDecay = (double) decay / (double) shelfLife;

            if (decay <= 0) {
                order.setOrderStatus(OrderStatus.TRASHED);
                normalizedDecay = 0;
                if (trashOrders == null) {
                    trashOrders = new ArrayList<>();
                }
                trashOrders.add(order);
            }

            // This is used to display the status of orders on the shelf.
            order.setNormalizedDecay(normalizedDecay);
        }

        if (trashOrders != null) {
            for (Order orderToTrash : trashOrders) {
                handleOrderFinished(orderToTrash);
            }
        }

    }

    // When the kitchen is open we should start scheduling drivers
    public void startSchedulingDeliveries() {
        if( driverThreadPool == null ) {
            driverThreadPool = Executors.newCachedThreadPool();
        }
    }

    public void stopSchedulingDeliveries() {
        if (driverThreadPool != null) {
            driverThreadPool.shutdown();
            driverThreadPool = null;
        }
    }

    public void scheduleDriverForOrder(Order order) {
        if( driverThreadPool != null ) {
            Driver driver = DriverFactory.driverForOrder(order);
            driverThreadPool.submit(driver);
        }
    }

    public static DeliveryManager sharedInstance() {
        return THE_INSTANCE;
    }

    public Boolean placeOrderOnShelfForDelivery(Order order) {
        Boolean successful = false;
        DeliveryShelf shelf = null;

        DeliveryShelfType shelfType = order.getItem().getDefaultShelf();
        switch (shelfType) {
        case COLD:
            shelf = coldShelf;
            break;
        case HOT:
            shelf = hotShelf;
            break;
        case FROZEN:
            shelf = frozenShelf;
            break;
        default:
            break;
        }

        // Attempt to place on the proper shelf. If not successful, place on overflow.
        // If overflow is full, notify Restaurant Manager (sounds good right?) that this
        // occurrent and have the order placed in the trash? Or how about we make
        // another
        // shelf for the homeless and we place it there to take out themselves.
        if (shelf != null) {
            // For now, we'll synchronize on the DeliveryManager but we could also
            // do the same on each shelf. We'll see...
            synchronized (this) {
                Boolean success = shelf.addOrderToShelf(order);
                if( success ) {
                    // We added order to its default shelf.  Note this in order:
                    order.setDeliveryShelf(shelf.shelfType());
                }
                else {
                    // We coudn't add it to the shelf we wanted. Try adding to the overflow shelf
                    // instead.
                    success = overflowShelf.addOrderToShelf(order);
                    if( success ) {
                        // Set the delivery shelf to the overflow shelf
                        order.setDeliveryShelf(overflowShelf.shelfType());
                    }
                    if (!success) {
                        System.out.println("Could not add order to overflow shelf - it's FULL!");
                        // TODO: Trash the order!
                        
                    }
                }
            }
        }
        // Now that the kitchen has prepared the order, schedule a driver to deliver it.
        // NOTE:  This is NOT per specification:  "Drivers should be dispatched to pick up each order as it is placed."
        //        I'm waiting for the kitchen to prepare the order instead as I have a limited number
        //        of chefs and I have a delay in the system for orders while being prepared (modeling a
        //        more real-world reality).
        scheduleDriverForOrder(order);

        return successful;
    }

    // This is called when the order has finished being delivered. It's also called
    // after a delivery
    // was attempted but the order was trashed. In that case, the order will be
    // found in the trash
    // can and can be removed from there.
    public void handleOrderFinished(Order order) {
        // Don't allow the order to be moved or modified in another thread here
        synchronized (order) {
            if (order.getOrderStatus() == OrderStatus.TRASHED ||
                order.getOrderStatus() == OrderStatus.DELIVERED ) {
                // Remove from list of items in the trash to clean up.
                DeliveryShelf shelf = null;
                switch (order.getDeliveryShelf()) {
                case COLD:
                    shelf = coldShelf;
                    break;
                case HOT:
                    shelf = hotShelf;
                    break;
                case FROZEN:
                    shelf = frozenShelf;
                    break;
                default:
                    shelf = overflowShelf;
                    break;
                }
                if (shelf != null) {
                    synchronized (shelf) {
                        shelf.removeOrderFromShelf(order);
                    }
                }
            }

        }
    }
}