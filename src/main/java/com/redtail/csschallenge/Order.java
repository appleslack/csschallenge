package com.redtail.csschallenge;

import java.util.Date;
import com.redtail.csschallenge.model.*;

/**
 * Order
 */
public class Order {

    private MenuItem    item;
    private Date        orderDate;
    private Date        orderPreparedDate;
    OrderStatus         status;
    private long        orderNumber;
    private int         normalizedDecay = -1;

    public Order(MenuItem item) {
        this.item = item;
        this.orderDate = new Date();
        this.status = OrderStatus.ORDER_CREATED;
    }

    public MenuItem getItem() {
        return item;
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

    public void setOrderNumber( long number ) {
        this.orderNumber = number;
    }

    public long getOrderNumber() {
        return orderNumber;
    }
    public void setFullfilled() {
        setOrderStatus( OrderStatus.PREPARED );
        this.orderPreparedDate = new Date();
    }
    
    public Date getOrderPreparedDate() {
        return this.orderPreparedDate;
    }

    public void setOrderStatus( OrderStatus status ) {
        this.status = status;
        System.out.println(this + ": Order status changed to " + status);
    }

    public OrderStatus getOrderStatus() {
        return this.status;
    }
    
    public void setNormalizedDecay(int normalizedDecay) {
        this.normalizedDecay = normalizedDecay;
    }
    
    public int getNormalizedDecay() {
        return normalizedDecay;
    }

    @Override
    public String toString() {
        return "Order number " + orderNumber + " : " + item.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.getName().hashCode());
        result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
        result = prime * result + (int)orderNumber;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.getName().equals(other.item.getName()))
            return false;
        if (orderDate == null) {
            if (other.orderDate != null)
                return false;
        } else if (!orderDate.equals(other.orderDate))
            return false;
        return true;
    }

}

