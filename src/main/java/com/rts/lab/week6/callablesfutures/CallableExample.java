package com.rts.lab.week6.callablesfutures;

import java.util.concurrent.*;

public class CallableExample {
    /**
     * ScheduledFuture
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService ex = Executors.newCachedThreadPool();
        /*
         * After submitting, we will get Futures.
         * (a callable class will return something after we
         * submit it to the executorservice.
         */
        ex.submit(new logic());
        Future<String> returningMessage = ex.submit(new logic());
        /*
         * We can reduce latency.
         */
        while(!returningMessage.isDone()) {
            // if the thing is not returning something, we can do something here
        }
        try {
            String msg = returningMessage.get();
        } catch(Exception e){}
        //else we can do something else, such as retrieving object from the future
        //and doing something with that

    }
}

/**
 * Callable is a advanced runnable that it can return
 * something.
 */
class logic implements Callable<String> {

    @Override
    public String call() throws Exception {
        /*
         * We need to use something capable
         * to return something
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        return "This task has be done";
    }
}

