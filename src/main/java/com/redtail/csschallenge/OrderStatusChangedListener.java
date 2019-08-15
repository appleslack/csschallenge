package com.redtail.csschallenge;

interface OrderStatusChangedListener {
    void orderStatusChanged( Order order, OrderStatus newStatus );

}
