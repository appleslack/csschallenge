package com.redtail.csschallenge;

public enum OrderStatus {
    ORDER_CREATED,
    AWAITING_PREP,      // Waiting for kitchen to start preparing
    PREPARING,          // Actively preparing in kitchen
    PREPARED,           // The kitchen has finished preparing
    ON_DELIVERY_SHELF,  // Waiting to be pickup up for delivery.  Decay rate started.
    FULLFILLED          // Delivery successful (archived order)          
}
