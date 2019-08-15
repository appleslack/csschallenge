package com.redtail.csschallenge.rest;

import com.redtail.csschallenge.Restaurant;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private static final String PERIODIC_ORDERING_COULD_NOT_BE_STARTED = "Periodic ordering could not be started";
    private static final String STARTED_PERIODIC_ORDERING = "Started periodic ordering...";
    private static final String STOPPED_PERIODIC_ORDERING = "Stopped periodic ordering...";
    private static final String PERIODIC_ORDERING_COULD_NOT_BE_STOPPED = "Periodic ordering could not be stopped (not started?)";

    @RequestMapping("/orderRandomItem")
    public String orderRandomItem() {
        String itemName;
        try {
            itemName = Restaurant.sharedInstance().orderRandomItem();
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception caught while ordering";
        }

        return "Ordered " + itemName;
    }

    @RequestMapping("/startPeriodicOrdering")
    public String startPeriodicOrdering() {
        boolean success = Restaurant.sharedInstance().startAutoOrderProcessor();
        if( success ) {
            return STARTED_PERIODIC_ORDERING;
        }
        else {
            return PERIODIC_ORDERING_COULD_NOT_BE_STARTED;
        }
    }

    @RequestMapping("/stopPeriodicOrdering")
    public String stopPeriodicOrdering() {
        boolean success = Restaurant.sharedInstance().stopAutoOrderProcessor();
        if( success ) {
            return STOPPED_PERIODIC_ORDERING;
        }
        else {
            return PERIODIC_ORDERING_COULD_NOT_BE_STOPPED;
        }
    }

}
