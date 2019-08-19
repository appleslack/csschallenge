package com.redtail.csschallenge;

import java.util.ArrayList;
import java.util.List;

/**
 * DeliveryShelf
 */
public class DeliveryShelf {

    private int                 capacity = 0;
    private List<Order>         ordersOnShelf = null;
    private final String        shelfName;
    private DeliveryShelfType   shelfType;

    public DeliveryShelf( DeliveryShelfType type ) {
        shelfType = type;

        switch (type) {
            case HOT:
                capacity = 15;
                shelfName = "hot";
                break;
            case COLD:
                capacity = 15;
                shelfName = "cold";
                break;
            case FROZEN:
                capacity = 15;
                shelfName = "frozen";
                break;
            case OVERFLOW:
                capacity = 20;
                shelfName = "overflow";
                break;

            default:
                shelfName = "unknown";
                break;
        }
        ordersOnShelf = new ArrayList<>(capacity);
    }

    public boolean hasOrdersOnShelf() {
        return (ordersOnShelf.size() > 0 );
    }
    
    public Boolean addOrderToShelf( Order order ) {
        Boolean successful = false;
        synchronized( this.ordersOnShelf ) {
            if( this.ordersOnShelf.size() < this.capacity ) {
                this.ordersOnShelf.add(order);
                successful = true;
                System.out.println(order + ": Added " + order + " to " + shelfName() + " shelf" );
            }
        }
        if( !successful ) {
            System.out.println(order + ": Cannot add " + order + " to " + shelfName() + " shelf - over capacity" );
        }

        return successful;
    }

    public boolean removeOrderFromShelf( Order order ) {
        boolean removed = false;

        synchronized( this.ordersOnShelf ) {
            removed = this.ordersOnShelf.remove(order);
        }
        if( removed ) {
            System.out.println(order.getItemName() + ": Removed " + order.getItemName() + " from " + shelfName() + " shelf" );
        }
        return removed;
    }

    public DeliveryShelfType shelfType() {
        return shelfType;
    }
    
    public List<Order> getOrdersOnShelf() {
        return this.ordersOnShelf;
    }

    public String shelfName() {
        return shelfName;
    }

}