package com.redtail.csschallenge;

import java.util.Random;

/**
 * DriverFactory
 */
public class DriverFactory {
    private static final int MIN_TRAFFIC_DELAY = 2;
    private static final int MAX_TRAFFIC_DELAY = 10;

    private static final Random r = new Random();

    public static Driver driverForOrder( Order order ) {
        int trafficDelayForDriver = r.nextInt((MAX_TRAFFIC_DELAY - MIN_TRAFFIC_DELAY) + 1) + MIN_TRAFFIC_DELAY;

        System.out.println( "Created driver with traffic delay " + trafficDelayForDriver);
        return new Driver( order, trafficDelayForDriver);
    }

}