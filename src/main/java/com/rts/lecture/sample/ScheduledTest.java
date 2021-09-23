package com.rts.lecture.sample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTest {
    public static void main(String[] args) {
        // returns schedule executor service
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        MyTask myTask = new MyTask();

        scheduledExecutorService.schedule(myTask, 5, TimeUnit.SECONDS);

        scheduledExecutorService.shutdown();
    }


}
class MyTask implements Runnable {
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("My task started");
        System.out.println("My task end..");
    }
}
