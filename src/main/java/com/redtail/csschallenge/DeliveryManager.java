package com.redtail.csschallenge;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DeliveryManager
 * The delivery manager is responsible for scheduling a delivery for each order and once
 * the order has moved through the Kitchen, it owns the Order until completed (whether delivered
 * or not).
 * After the Kitchen has finished with the Order, it's placed on the appropriate DeliveryShelf.
 * 
 * The DeliverManager is also responsible for keeping track (updating) the decay for each order
 * one each shelf and trashing the order if the delay has reached zero.
 */
public final class DeliveryManager {
    private static final DeliveryManager THE_INSTANCE = new DeliveryManager();

    private final DeliveryShelf   hotShelf      = new DeliveryShelf(DeliveryShelfType.HOT);
    private final DeliveryShelf   coldShelf     = new DeliveryShelf(DeliveryShelfType.COLD);
    private final DeliveryShelf   frozenShelf   = new DeliveryShelf(DeliveryShelfType.FROZEN);
    private final DeliveryShelf   overflowShelf = new DeliveryShelf(DeliveryShelfType.OVERFLOW);
    private final List<Order>     trashedOrders = new ArrayList<>();
    
    private ExecutorService driverThreadPool = null;

    private DeliveryManager() {
        Runnable decayUpdater = () -> {
            while(true) {
                // Don't let shelves be modified while doing this operation
                synchronized(this) {
                    decayOrdersOnShelf(hotShelf);
                    decayOrdersOnShelf(coldShelf);
                    decayOrdersOnShelf(frozenShelf);
                    decayOrdersOnShelf(overflowShelf);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(decayUpdater).start();
    }
    /*
    * Iterate through orders on the given shelf and update their decay value.  Return
    * orders that have a decay indicating they should be trashed (<=0)
    * decayValue = ([shelf life] - [order age]) - ([decay rate] * [order age])
    */
    private void decayOrdersOnShelf( DeliveryShelf shelf ) {
        List<Order> orders = shelf.getOrdersOnShelf();
        List<Order> trashOrders = null;

        Date now = new Date();
        for (Order order : orders ) {
            int shelfLife = order.getItem().getShelfLife();
            double decayRate = order.getItem().getDecayRate();
            // Decay rate doubles for items on overflow shelf
            if( shelf.shelfType() == DeliveryShelfType.OVERFLOW ) {
                decayRate *= 2.0;
            }
            Date prepDate = order.getOrderPreparedDate();
            int orderAge = (int)(prepDate.getTime() - now.getTime()) / 1000;
            int decay = (shelfLife - orderAge) - (int)(decayRate * orderAge);
            int normalizedDecay = decay/ shelfLife;

            if( decay <= 0 ) {
                order.setOrderStatus(OrderStatus.TRASHED);
                normalizedDecay = 0;
                if( trashOrders == null ) {
                    trashOrders = new ArrayList<>();
                }
            }

            // This is used to display the status of orders on the shelf.
            order.setNormalizedDecay(normalizedDecay);
        }

        if( trashOrders != null ) {
            for (Order orderToTrash : trashOrders) {
                shelf.removeOrderFromShelf(orderToTrash);
                trashOrders.add(orderToTrash);
                System.out.println( orderToTrash + " placed in trash!");
            }
        }
    }

    // When the kitchen is open we should start scheduling drivers
    public void startSchedulingDeliveries() {
        driverThreadPool = Executors.newCachedThreadPool();
    }
    public void stopSchedulingDeliveries() {
        if( driverThreadPool != null ) {
            driverThreadPool.shutdown();
        }
    }
    public void scheduleDriverForOrder( Order order ) {
        // Driver driver = DriverFactory.newDriverForOrder(order);
        // driverThreadPool.submit(driver);

    }
    public static DeliveryManager sharedInstance() {
        return THE_INSTANCE;
    }

    public Boolean placeOrderOnShelfForDelivery( Order order ) {
        Boolean successful = false;
        DeliveryShelf shelf = null;

        DeliveryShelfType shelfType = order.getItem().getDeliveryShelf();
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

        // Attempt to place on the proper shelf.  If not successful, place on overflow.
        // If overflow is full, notify Restaurant Manager (sounds good right?) that this
        // occurrent and have the order placed in the trash?  Or how about we make another
        // shelf for the homeless and we place it there to take out themselves.
        if( shelf != null ) {
            // For now, we'll synchronize on the DeliveryManager but we could also
            // do the same on each shelf.  We'll see...
            synchronized( this ) {
                Boolean success = shelf.addOrderToShelf(order);
                if( !success ) {
                    // We coudn't add it to the shelf we wanted.  Try adding to the overflow shelf instead.
                    success = overflowShelf.addOrderToShelf(order);
                    if( !success ) {
                        System.out.println( "Could not add order to overflow shelf - it's FULL!");
                        // Trash the order?
    
                    }
                }
            }
        }
        return successful;
    }
    // This is called when the order has finished being delivered.  It's also called after a delivery
    // was attempted but the order was trashed.  In that case, the order will be found in the trash
    // can and can be removed from there.
    public void handleOrderFinished( Order order ) {
        // Don't allow the order to be moved or modified in another thread here
        synchronized(order) {
            if( order.getOrderStatus() == OrderStatus.TRASHED ) {
                // Remove from list of items in the trash to clean up.

            }
            // Remove from the shelf 
            
        }
    }
    /*
    * shelfStatusJSON
    * Return a json formatted version the shelf status information and relevant order
    * information for orders on each shelf:
    *
    * 
    */
    public String shelfStatusJSON() {

        return "";
    }
}