package com.redtail.csschallenge;

import java.util.Date;
import com.redtail.csschallenge.model.*;

/**
 * Order
 */
public class Order {

    private MenuItem    item;
    private Date        orderDate;
    private Boolean     orderFullfilled; // Has is been made yet (same as being on a shelf since it's considered instantaneous)    
    private Date        orderFullfillmentDate;

    public Order(MenuItem item) {
        this.item = item;
        this.orderDate = new Date();
    }

    public String getItemName() {
        return item.getName();
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setFullfilled() {
        this.orderFullfilled = true;
        this.orderFullfillmentDate = new Date();
    }
    
    public Date getFullfillmentDate() {
        return this.orderFullfillmentDate;
    }
    
    @Override
    public String toString() {
        return "Order [item=" + item + ", orderDate=" + orderDate + ", orderFullfilled=" + orderFullfilled
                + ", orderFullfillmentDate=" + orderFullfillmentDate + "]";
    }

    
}

