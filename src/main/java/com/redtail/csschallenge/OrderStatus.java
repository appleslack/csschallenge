package com.redtail.csschallenge;

public enum OrderStatus {
    ORDER_CREATED,
    AWAITING_PREP,      // Waiting for kitchen to start preparing
    PREPARING,          // Actively preparing in kitchen
    PREPARED,           // The kitchen has finished preparing
    ON_DELIVERY_SHELF,  // Waiting to be pickup up for delivery.  Decay rate started.
    DELIVERED,          // Delivery successful (archived order)          
    TRASHED            // The order has been trashed while waiting for delivery (or there's no more room on overflow shelf)
}
