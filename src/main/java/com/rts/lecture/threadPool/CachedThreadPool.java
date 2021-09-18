package com.rts.lecture.threadPool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
executors: assignment is used
 */

public class CachedThreadPool {
    /**
     Explain different types of executors
     */
    public static void main(String[] args) {
        //Executors create object for us. Factory method
        //requesting executor service: responsible to thread tool
        //that contains specific number of threads
        /**
         * Cached threadpool: extends thread on demand, or delete threads
         * when not in use (120 seconds).
         */
        ExecutorService manager = Executors.newCachedThreadPool();
        for (int i=0;i<50;i++) {
            manager.submit(new Logic(i));
            try {
                Thread.sleep(50);
            }catch(InterruptedException e) {

            }
        }
        // for about 180 seconds, the threads will stay alive
        // maintains numbers of threads in demand

    }
}