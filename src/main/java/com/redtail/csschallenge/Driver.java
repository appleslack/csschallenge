
package com.redtail.csschallenge;

/**
 * Driver
 */
public class Driver implements Runnable {
    private int trafficDelay;
    private Order orderForDelivery;

    public Driver(Order order, int delay ) {
        trafficDelay = delay;
        orderForDelivery = order;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(trafficDelay*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if( orderForDelivery.getOrderStatus() != OrderStatus.TRASHED) {
            orderForDelivery.setOrderStatus(OrderStatus.DELIVERED);
        }
        DeliveryManager.sharedInstance().handleOrderFinished(orderForDelivery);
    }

    
    
}