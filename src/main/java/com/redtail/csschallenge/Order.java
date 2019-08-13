package com.redtail.csschallenge;

import java.util.Date;
import com.redtail.csschallenge.model.*;

/**
 * Order
 */
public class Order {

    private MenuItem    item;
    private Date        orderDate;

    public Order(MenuItem item) {
        this.item = item;
        this.orderDate = new Date();
    }

    public MenuItem getItemName() {
        return item.getItemName();
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    
}

