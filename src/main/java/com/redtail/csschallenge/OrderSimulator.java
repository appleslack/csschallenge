package com.redtail.csschallenge;

import java.util.Random;

/**
 * OrderSimulator
 */
public class OrderSimulator {
    private final Random r = new Random();
    private final double poissonLambdaValue;

    public OrderSimulator(double lambda) {
        poissonLambdaValue = lambda;
    }

    private int getPoissonRandom() {
        double L = Math.exp(-poissonLambdaValue);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }

    
    
}