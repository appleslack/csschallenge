package com.redtail.csschallenge;

import java.util.concurrent.atomic.AtomicLong;

import com.redtail.csschallenge.model.MenuItem;

/**
 * OrderFactory
 */
public class OrderFactory {
    private static final AtomicLong orderSequenceNumber = new AtomicLong(1);

    public static Order createOrderForItem( MenuItem item ) {
        Order order = new Order(item);
        long nextOrderSeq = orderSequenceNumber.getAndIncrement();
        order.setOrderNumber(nextOrderSeq);

        return order;
    }
}