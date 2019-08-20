package com.redtail.csschallenge.rest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redtail.csschallenge.DeliveryManager;
import com.redtail.csschallenge.Restaurant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/*
 * RESTaurantController
 * Note: This is just a quick-n-easy implmentation of a RESTful API for the
 * frontend and for quick testing (Use curl to start & stop auto ordering for example) which
 * may or may not be wired into the frontend.  We'll see...
 * 
 */

@RestController
public class RESTaurantController {

    private static final String PERIODIC_ORDERING_COULD_NOT_BE_STARTED = "Periodic ordering could not be started";
    private static final String STARTED_PERIODIC_ORDERING = "Started";
    private static final String STOPPED_PERIODIC_ORDERING = "Stopped";
    private static final String PERIODIC_ORDERING_COULD_NOT_BE_STOPPED = "Periodic ordering could not be stopped (not started?)";

    /*
    * orderRandomItem
    * 
    * Note: Use this from a Terminal to easily order a single, random item from the menu:
    *
    * Terminal:  curl http://localhost:8000/restaurant/orderRandomItem
    */
    @RequestMapping("/restaurant/orderRandomItem")
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

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(
        value="/restaurant/orderItem",
        method = RequestMethod.GET
    )
    public String orderItem( @RequestParam String itemName ) {
        System.out.println( "Ordering item: " + itemName );
        try {
            Restaurant.sharedInstance().orderItemWithName(itemName);
        } catch (Exception e) {
            return "Failed";
        }
        return "Success";
    }
    /*
    * startPeriodicOrdering
    * 
    * Start an auto-ordering thread that will pick random orders from the menu start the ordering
    * process for each one.  Quickly fills up the order shelves and starts scheduling drivers to
    * pick up the orders.  
    *
    * NOTE:  This is the best way to test the system for concurrency and compliance to the
    *        application specification.
    *
    * API Endpoint:  /restarant/startPeriodicOrdering
    * Returns:  "Success", if auto ordering started successfully.
    *           Error String, otherwise
    */
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/restaurant/startPeriodicOrdering")
    public String startPeriodicOrdering() {
        boolean success = Restaurant.sharedInstance().startAutoOrderProcessor();
        if( success ) {
            return STARTED_PERIODIC_ORDERING;
        }
        else {
            return PERIODIC_ORDERING_COULD_NOT_BE_STARTED;
        }
    }
    
    /*
    * stopPeriodicOrdering
    * 
    * Stop the auto-ordering thread if currently started.
    *
    *
    * API Endpoint:  /restarant/stopPeriodicOrdering
    *
    * Returns:  "Success", if auto ordering stoppped successfully.
    *           Error String, otherwise
    */

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/restaurant/stopPeriodicOrdering")
    public String stopPeriodicOrdering() {
        boolean success = Restaurant.sharedInstance().stopAutoOrderProcessor();
        if( success ) {
            return STOPPED_PERIODIC_ORDERING;
        }
        else {
            return PERIODIC_ORDERING_COULD_NOT_BE_STOPPED;
        }
    }

    /*
    * retrieveShelfStatusInfo
    *
    * Get the current state of all the shelves (orders on the shelves).  Useful for updating
    * the UI one time - for example when populating a client when loading.  Use the event-based
    * mechanism (SSE) below to be send periodic status updates.
    *
    * API Endpoint:  /restarant/shelfStats
    */

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(
        value="/restaurant/shelfStats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String retrieveShelfStatusInfo() {
        return DeliveryManager.sharedInstance().getShelfStatusInfo();
    }

    /*
    * periodicShelfStatusInfo
    *
    * Support Server-Side Events (SSE) so we can periodically update the 
    * shelf status info when there's items on one or more shelves.  If there's
    * no items, then we don't want to continuously send out updates...
    *
    *  API Endpoint:  /restaurant/sse/shelfStats
    */
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(
        value="/restaurant/sse/shelfStats",
        method = RequestMethod.GET
    )
    public SseEmitter periodicShelfStatusInfo() {
        final SseEmitter emitter = new SseEmitter();
        // ExecutorService service = Executors.newSingleThreadExecutor();
        // service.execute(() -> {
        //     try {
        //         if( DeliveryManager.sharedInstance().anyOrdersOnShelves() == true) {
        //             String jsonStats = DeliveryManager.sharedInstance().getShelfStatusInfo();
        //             emitter.send(jsonStats);
        //         }
        //         Thread.sleep(2);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //         emitter.completeWithError(e);
        //         return;
        //     }
        // });
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if( DeliveryManager.sharedInstance().anyOrdersOnShelves() == true ) {
                        String jsonStats = DeliveryManager.sharedInstance().getShelfStatusInfo();
                        emitter.send(jsonStats);
                    }
                } catch (Exception e) {
                }
            }
        } , 2000, 2000, TimeUnit.MILLISECONDS);
    
        return emitter;
    }

    /*
    *  getRestaurantMenu
    *
    *  Retrieve the restaurant's Menu from the server.
    *
    *  API Endpoint:  /restaurant/menu
    */
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
