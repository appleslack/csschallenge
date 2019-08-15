package com.redtail.csschallenge;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * OrderSimulator
 * Simulate orders coming in at a rate given by a Poisson Distrubution.  The
 * default lambda is 3.5 orders/sec - I could easily make this rate dynamic
 * but that's outside the scope of the work - but I'll make it easy to do so
 * while constructing the simulator...
 */
public class PeriodicTask {
    private final Random r = new Random();
    private double poissonLambdaValue;
    // private Thread periodicWakeThread;
    private ScheduledExecutorService periodicExecutor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> sFuture = null;
    private final PeriodicTaskCallback callback;

    public PeriodicTask(double lambda, PeriodicTaskCallback cb) {
        this.setLambda(lambda);
        callback = cb;

        // periodicWakeThread = new Thread();
    }

    // See: https://en.wikipedia.org/wiki/Poisson_distribution#Generating_Poisson-distributed_random_variables
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

    public void setLambda( double lambda ) {
        poissonLambdaValue = lambda;
        System.out.println( "Using lambda value for poisson distribution: " + lambda);
    }

    // Given more time, I'd abstract out the rate at which the periodic task is run (using a lambda function) to
    // give this class much more flexibility.  For now - the rate uses getPoissonRandom()...
    public void start() {
        Runnable orderTask = () -> {
            int nextRate = this.getPoissonRandom();
            System.out.println( "New poisson rate = " + nextRate );

            // For now - just call callback
            if( this.callback != null ) {
                callback.runTask();
            }
        };

        sFuture = periodicExecutor.scheduleAtFixedRate(orderTask, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        sFuture.cancel(true);
        periodicExecutor.shutdown();
    }
    // class PeriodicWakeTask extends TimerTask {
    //     public void run() {
            
    //         System.out.println( "")
    //     }
    // }
}