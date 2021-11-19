package com.rts.week12.labexercise;

import java.util.concurrent.*;

public class LecturerExample {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(5);

        ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(new Callable() {
            public String call() throws Exception {
                System.out.println("Executed!!");
                Thread.sleep(50000);
                return "Called!";
            }
        },5, TimeUnit.SECONDS);
        while(!scheduledFuture.isDone()) {
            Thread.sleep(10000);
            scheduledFuture.cancel(true);
            System.out.println("interrupted");
        }
    }
}

