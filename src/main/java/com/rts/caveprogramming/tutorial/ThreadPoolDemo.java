package com.rts.caveprogramming.tutorial;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        /**
         * Thread pool: like having workers on the factory
         * Executors.newFixedThreadPool: fixed workers
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for(int i=1;i<=5;i++) {
            executor.submit(new ThreadPoolProcessor(i));
        }

        executor.shutdown(); // wait for all the threads to die (complete)

        System.out.println("All task submitted.");

        try {
            //this will wait for one day until all my tasks finish.
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadPoolProcessor implements Runnable {
    private int id;
    @Override
    public void run() {
        System.out.println("Starting: " + id);
        //e.g. simulate like handling files..
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Completed: " + id);
    }
    public ThreadPoolProcessor(int id) {
        this.id = id;
    }

}

