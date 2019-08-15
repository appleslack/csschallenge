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

    public DeliveryShelf( DeliveryShelfType type ) {

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

    public Boolean addOrderToShelf( Order order ) {
        Boolean successful = false;
        synchronized( this.ordersOnShelf ) {
            if( this.ordersOnShelf.size() < this.capacity ) {
                this.ordersOnShelf.add(order);
                successful = true;
                System.out.println(order.getItemName() + ": Added " + order.getItemName() + " to " + shelfName() + " shelf" );
            }
        }
        if( !successful ) {
            System.out.println(order.getItemName() + ": Cannot add " + order.getItemName() + " to " + shelfName() + " shelf - over capacity" );
        }

        return successful;
    }

    public void removeOrderFromShelf( Order order ) {
        boolean removed = false;

        synchronized( this.ordersOnShelf ) {
            removed = this.ordersOnShelf.remove(order);
        }
        if( removed ) {
            System.out.println(order.getItemName() + ": Removed " + order.getItemName() + " from " + shelfName() + " shelf" );
        }

    }

    public String shelfName() {
        return shelfName;
    }

}