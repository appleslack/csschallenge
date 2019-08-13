package com.redtail.csschallenge;

import java.util.List;

import com.redtail.csschallenge.model.MenuItem;

/**
 * OrderManager
 */
public class OrderManager {
    public static final OrderManager THE_INSTANCE = new OrderManager();
    
    private List<Order> orders;
    
    private OrderManager() {

    }

    public static OrderManager sharedInstance() {
        return THE_INSTANCE;
    }
    
    public Order addOrderForItem( MenuItem item ) {

        Order order = new Order( item );
        orders.add(order);

        return order;
    }
    
}