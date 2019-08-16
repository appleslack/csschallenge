package com.redtail.csschallenge.rest;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redtail.csschallenge.Restaurant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTaurantController {

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

    @RequestMapping("/shelves/status")
    public String retrieveShelfStatusInfo() {
        return "";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(
      value = "/restaurant/menu", 
      method = RequestMethod.GET, 
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object getRestaurantMenu() {
        // Get the menu that read in @ start of application
        // Resource resource = this.getClass().getResource("/static/MenuItems.json");
        Resource resource = new ClassPathResource("/static/MenuItems.json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
