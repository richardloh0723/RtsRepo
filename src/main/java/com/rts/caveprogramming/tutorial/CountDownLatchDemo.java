package com.rts.caveprogramming.tutorial;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {
    public static void main(String[] args) {
        /**
         * Earlier on in this tutorial, we saw a lot of
         * terrible problems with thread synchronization
         * (accessing methods multiple threads at the same time)
         *
         * countdownlatch can access multiple methods without worry.
         */
        // lets one or more threads count down the latch
        CountDownLatch latch = new CountDownLatch(3);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i=0;i<3;i++) {
            executor.submit(new CDLProcessor(latch));
        }
        // waits until countdownlatch has counted down into zero
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Completed");
    }
}

class CDLProcessor implements Runnable {
    private CountDownLatch latch;
    @Override
    public void run() {
        System.out.println("Started.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    public CDLProcessor(CountDownLatch latch) {
        this.latch = latch;
    }

}
