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
    private double         normalizedDecay = 0.0;
    private DeliveryShelfType deliveryShelf;

    public Order(MenuItem item) {
        this.item = item;
        this.orderDate = new Date();
        this.status = OrderStatus.ORDER_CREATED;
        this.deliveryShelf = DeliveryShelfType.NOT_ASSIGNED;
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

    public Date getOrderPreparedDate() {
        return this.orderPreparedDate;
    }

    public void setOrderStatus( OrderStatus status ) {
        if( this.status == OrderStatus.DELIVERED ) {
            System.out.println( "This shouldn't happen!");
        }
        this.status = status;

        System.out.println(this + ": Order status changed to " + status);
        if( status == OrderStatus.PREPARED ) {
            this.orderPreparedDate = new Date();
        }
    }

    public OrderStatus getOrderStatus() {
        return this.status;
    }

    public double getNormalizedDecay() {
        return normalizedDecay;
    }
    
    public void setNormalizedDecay(double normalizedDecay) {
        this.normalizedDecay = normalizedDecay;
    }
    
    public DeliveryShelfType getDeliveryShelf() {
        return deliveryShelf;
    }

    public void setDeliveryShelf(DeliveryShelfType deliveryShelf) {
        this.deliveryShelf = deliveryShelf;
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

