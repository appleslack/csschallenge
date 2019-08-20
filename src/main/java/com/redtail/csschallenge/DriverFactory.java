package com.redtail.csschallenge;

import java.util.Random;

/**
 * DriverFactory
 */
public class DriverFactory {
    // Note:  With this driver delay as specified, the orders are never making it to the 
    // overflow shelf as the drivers (an infinite number of them) are always picking up the 
    // orders off the delivery shelves before the overflow occurs.  Adjust these parameters
    // below and you'll see different behavior.  For example, use these:
    private static final int MIN_TRAFFIC_DELAY = 20;
    private static final int MAX_TRAFFIC_DELAY = 100;
    // private static final int MIN_TRAFFIC_DELAY = 2;
    // private static final int MAX_TRAFFIC_DELAY = 10;

    private static final Random r = new Random();

    public static Driver driverForOrder( Order order ) {
        int trafficDelayForDriver = r.nextInt((MAX_TRAFFIC_DELAY - MIN_TRAFFIC_DELAY) + 1) + MIN_TRAFFIC_DELAY;

        System.out.println( "Created driver with traffic delay " + trafficDelayForDriver);
        return new Driver( order, trafficDelayForDriver);
    }

}