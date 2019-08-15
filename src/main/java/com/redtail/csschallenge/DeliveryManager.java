package com.redtail.csschallenge;

/**
 * DeliveryManager
 */
public final class DeliveryManager {
    private static final DeliveryManager THE_INSTANCE = new DeliveryManager();

    private final DeliveryShelf   hotShelf      = new DeliveryShelf(DeliveryShelfType.HOT);
    private final DeliveryShelf   coldShelf     = new DeliveryShelf(DeliveryShelfType.COLD);
    private final DeliveryShelf   frozenShelf   = new DeliveryShelf(DeliveryShelfType.FROZEN);
    private final DeliveryShelf   overflowShelf = new DeliveryShelf(DeliveryShelfType.OVERFLOW);

    private DeliveryManager() {
        
    }

    public static DeliveryManager sharedInstance() {
        return THE_INSTANCE;
    }

    public Boolean placeOrderOnShelfAndScheduleDelivery( Order order ) {
        Boolean successful = false;
        DeliveryShelf shelf = null;

        DeliveryShelfType shelfType = order.getItem().getDeliveryShelf();
        switch (shelfType) {
            case COLD:
                shelf = coldShelf;
                break;
            case HOT:
                shelf = hotShelf;
                break;
            case FROZEN:
                shelf = frozenShelf;
                break;
            default:
                break;
        }

        // Attempt to place on the proper shelf.  If not successful, place on overflow.
        // If overflow is full, notify Restaurant Manager (sounds good right?) that this
        // occurrent and have the order placed in the trash?  Or how about we make another
        // shelf for the homeless and we place it there to take out themselves.
        if( shelf != null ) {
            Boolean success = shelf.addOrderToShelf(order);
            if( !success ) {
                // We coudn't add it to the shelf we wanted.  Try adding to the overflow shelf instead.
                success = overflowShelf.addOrderToShelf(order);
                if( !success ) {
                    System.out.println( "Could not add order to overflow shelf - it's FULL!");
                    // Trash the order?

                }
            }

        }
        return successful;
    }

}