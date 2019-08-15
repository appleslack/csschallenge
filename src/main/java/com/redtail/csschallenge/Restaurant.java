package com.redtail.csschallenge;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redtail.csschallenge.model.MenuItem;

/**
 * Restaurant
 */
public class Restaurant {

    private static final Restaurant THE_INSTANCE = new Restaurant();
    
    private String name;
    // private DeliveryManager dm;
    private Boolean restaurantOpen = false;
    private HashMap<String, MenuItem> theMenu;
    private PeriodicTask orderSim = null;

    private Restaurant()  {
        this.readMenuItems();
    }

    public static Restaurant sharedInstance() {
        return THE_INSTANCE;
    }

    // Read the json file and create a list of possible MenuItems (the name is a unique value)
    // and create theMenu
    private void readMenuItems() {
        ObjectMapper mapper = new ObjectMapper();
        // File jsonSource = new File("./src/main/resources/static/MenuItems.json");
        String fn = this.getClass().getResource("/static/MenuItems.json").getFile();

        File jsonSource = new File(fn);

        try {
            List<HashMap> items = mapper.readValue(jsonSource, List.class);
            theMenu = new HashMap<>();
            for (HashMap item : items) {
                MenuItem mi = new MenuItem(
                        item.get("name").toString(), 
                        item.get("temp").toString(), 
                        Integer.parseInt(item.get("shelfLife").toString()), 
                        Double.parseDouble(item.get("decayRate").toString()));

                theMenu.put(mi.getName(), mi );
            }
            System.out.println("Read " + theMenu.size() + " items on the menu" );
        } catch (Exception e) {
            System.out.println( "Exception reading MenuItems - " + e );
        }
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    protected MenuItem getMenuItem( String itemName ) {
        return theMenu.get(itemName);
    }

    // This starts the whole thing off by telling the order manager that it's
    // OK to take orders (if we're closed, then orders will be rejected).
    public void startTakingOrders() {

        this.restaurantOpen = true;
        System.out.println(getName() + " is now taking orders!");
    }

    // Stop taking any new individual orders (via the Restful API) and tell the
    // test order scheduler to stop also (so we can see all orders become complete)
    public void stopTakingOrders() {
        this.restaurantOpen = false;
        System.out.println(getName() + " is no longer taking orders!");
    }

    // Start the automatic order processor 
    public boolean startAutoOrderProcessor() {
        boolean success = false;

        if( orderSim == null ) {
            PeriodicTaskCallback cb = () -> {
                try {
                    this.orderRandomItem();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            orderSim = new PeriodicTask(3.5, cb );
            orderSim.start();
            success = true;
        }
        else {
            System.out.println("Order simulator already started..." );
        }
        return success;
    }

    public boolean stopAutoOrderProcessor() {
        boolean success = false;
        if( orderSim != null ) {
            orderSim.stop();
            orderSim = null;
            success = true;
        }
        return success;
    }

    // Given an item, ask the order manager for a new order.  Once the
    // order has been put on one of the outgoing shelves (via the Order Manager),
    // the delivery can be scheduled (via DeliveryScheduler)
    public void orderItemWithName( String itemName ) throws Exception {

        if( this.restaurantOpen ) {
            MenuItem item = this.getMenuItem(itemName);
            if( item != null ) {
                OrderManager.sharedInstance().addOrderForItem(item);
            }
            else {
                throw new Exception("Menu Item Not Found: " + itemName );
            }
        }
        else {

            System.out.println("Order denied : Restaurant closed at this time");
        }

        return ;
    }

    public String orderRandomItem( ) throws Exception {

        // Get a random item from the Menu:
        int numItems = this.theMenu.size();
        int randomItem = ThreadLocalRandom.current().nextInt(0, numItems);

        Object[] keys = theMenu.keySet().toArray();
        String itemName = (String) keys[randomItem];
        this.orderItemWithName(itemName);

        return itemName;
    }
}