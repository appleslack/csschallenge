package com.redtail.csschallenge;

import java.util.ArrayList;
import java.util.List;

import com.redtail.csschallenge.model.MenuItem;

/**
 * OrderManager
 */
public final class OrderManager {
    public static final OrderManager THE_INSTANCE = new OrderManager();
    
    private List<Order> activeOrders = new ArrayList<>();
    
    private OrderManager() {

    }

    public static OrderManager sharedInstance() {
        return THE_INSTANCE;
    }
    
    public Order addOrderForItem( MenuItem item ) {
        Order order = new Order( item );
        synchronized( this.activeOrders ) {
            activeOrders.add(order);
            Kitchen.sharedInstance().newOrderArrived(order);
        }

        return order;
    }
    
    
    public void orderCompleted( Order order ) {

    }

}