package com.redtail.csschallenge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Kitchen
 * 
 * The kitchen is responsible for making orders and when finished making each
 * order, putting on the shelf (via ShelveManager) then notifying the Order
 * Manager that the order has been placed on the shelf and ready for delivery
 * pickup.
 */
public final class  Kitchen {
    private static final Kitchen THE_INSTANCE = new Kitchen();
    private int numConcurrentOrders = 0;        // Assuming 1 chef default who can actively cook 4 orders at a time

    // The kitchen is only capable of making a finite number of orders at a time (totally arbitray number based on one
    // chef in this particular kitchen).  Orders will first be placed in the queue then removed as
    // resources allow for the next order to be made.
    private BlockingDeque<Order>  queuedOrders;
    private List<OrderStatusChangedListener> orderStatusChangedListeners;
    
    public static Kitchen sharedInstance() {
        return THE_INSTANCE;
    }
    
    private Kitchen() {
        this.queuedOrders = new LinkedBlockingDeque<>();
        this.orderStatusChangedListeners = new ArrayList<>();
        
        this.setNumChefsOnDuty(1);
        this.consumeOrders();
    }
 
    public void registerOrderChangedListener( OrderStatusChangedListener changedListener ) {
        this.orderStatusChangedListeners.add(changedListener);
    }

    public void setNumChefsOnDuty( int numChefs ) {
        // This number should be validated based on kitchen size and equipemnt, but 
        // outside this scope
        numConcurrentOrders = 4 * numChefs;
    }

    // We have a new incoming order - put on the queuedOrders for "later" fullfillment
    public void newOrderArrived(Order order ) {
        synchronized( this  ) {
            System.out.println("Kitchen:  New order arrived: " + order.getItemName());
            this.queuedOrders.add(order);
            order.setOrderStatus(OrderStatus.AWAITING_PREP);
        }
    }

    public void consumeOrders() {
        // Note:  We're assuming that the number of chefs in the kitchen does not change
        // over the life of the running process.  Making this dynamic would be a bit more
        // complicated but it's not in the scope.
        ExecutorService executor = Executors.newFixedThreadPool(this.numConcurrentOrders);

        // Create the consumer task which deques orders, makes them then
        // puts them on the shelf.
        Runnable kitchenOrderFullfiller = () -> {
            try {
                while( true ) {
                    Order order = null;
                    synchronized(this.queuedOrders) {
                        System.out.println( "Waiting for an order... ");
                        order = this.queuedOrders.take();
                    }
                    // Now Prepare (make) order
                    this.prepareOrder( order );
                }
            } catch (Exception e) {
                System.out.println("Exception while consuming an order: " + e );
            }
        };
        
        executor.execute(kitchenOrderFullfiller);
    }

    public void prepareOrder( Order order ) {
        order.setOrderStatus(OrderStatus.PREPARING);

        // Sleep the preparer thread to simulate that the kitchen takes at least _SOME_ time
        // to make an order.  Yes - I know - it's supposed to be instantaneous but that's not fun.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println( "No longer waiting - jeez: " + e);
        }

        order.setOrderStatus(OrderStatus.PREPARED);
        // Send notification that the order has changed.  I'm doing this in the kitchen
        // prep thread, so I don't mind sending like this here.
        // Perhaps I should always send notifications when the order status changes, but right now
        // I don't have a good reason to do this.  If this is necessary, move this to another method:
        sendOrderChangedNotification( order, OrderStatus.PREPARED);
    }

    public void sendOrderChangedNotification( Order order, OrderStatus status ) {
        for (OrderStatusChangedListener listener : this.orderStatusChangedListeners ) {
            listener.orderStatusChanged(order, status);
        }
    }
}