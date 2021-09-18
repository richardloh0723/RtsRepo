package com.rts.lecture.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPool {
    /**
     * Same with fixed thread pool
     * with additional parameters
     * equivalent with timertask.
     * delay..
     */
    public static void main(String[] args) {
        //Executors create object for us. Factory method
        //requesting executor service: responsible to thread tool
        //that contains specific number of threads
        /**
         * Cached threadpool: extends thread on demand, or delete threads
         * when not in use (120 seconds).
         */
        ScheduledExecutorService manager = Executors.newScheduledThreadPool(2);
        for (int i=0;i<50;i++) {
            manager.scheduleAtFixedRate(new ScheduleLogic(1),0,1, TimeUnit.SECONDS);
        }
    }
}

class ScheduleLogic implements Runnable {
    int no;
    public ScheduleLogic(int no) {
        this.no = no;
    }
    @Override
    public void run() {
        System.out.println("hello from logic " + no);
    }
}
