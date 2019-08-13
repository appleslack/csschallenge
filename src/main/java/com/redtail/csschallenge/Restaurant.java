package com.redtail.csschallenge;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redtail.csschallenge.model.MenuItem;

/**
 * Restaurant
 */
public class Restaurant {

    private static final Restaurant THE_INSTANCE = new Restaurant();
    
    private String name;
    private DeliveryManager dm;
    private Boolean restaurantOpen = false;
    private List<MenuItem> theMenu;

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
        File jsonSource = new File("./resoures/static/MenuItems.json");
        List<MenuItem> items = mapper.readValue(jsonSource, List.class);
        System.out.println("The Items Are" + items );
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // This starts the whole thing off by telling the order manager that it's
    // OK to take orders (if we're closed, then orders will be rejected).
    public void startTakingOrders() {

        this.restaurantOpen = true;
    }

    // Stop taking any new individual orders (via the Restful API) and tell the
    // test order scheduler to stop also (so we can see all orders become complete)
    public void stopTakingOrders() {

    }

    // Start the automatic order processor 
    public void startAutoOrderProcessor() {
        // AutoOrderProcessor.sharedInstance().startSchedulingOrders();
    }

    public void stopAutoOrderProcessor() {
        // AutoOrderProcessor.sharedInstance().stopSchedulingOrders();
    }
}