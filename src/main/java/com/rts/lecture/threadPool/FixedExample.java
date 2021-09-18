package com.rts.lecture.threadPool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
executors: assignment is used
 */

public class FixedExample {
    /**
     Explain different types of executors
     */
    public static void main(String[] args) {
        //Executors create object for us. Factory method
        //requesting executor service: responsible to thread tool
        //that contains specific number of threads
        int cores = Runtime.getRuntime().availableProcessors();
        /**
         * 1. If threads match the number of core of our CPU
         * will increase the likelihood of one to one
         * potential parallelism.
         * Second way: parallel stream (will talk bout that later)
         */
        ExecutorService manager = Executors.newFixedThreadPool(cores);
        System.out.println(cores);
        /*
        Reducing the cost of utilising thread
        Threadpool is still there, alive to wait another logic to pass in
         */
        manager.submit(new FixedLogic(1));
        manager.submit(new FixedLogic(2));
        manager.submit(new FixedLogic(3));
        manager.submit(new FixedLogic(4));
        manager.submit(new FixedLogic(5));
        manager.shutdown();
        // we can also control the amount of thread
        /*
        Green threads end up sharing OS Threads.
        fixed thread pool will increase the likelihood
        to get one to one -> green thread 1 to 1 with os thread.
         */
    }
}

class FixedLogic implements Runnable {
    int no;
    public FixedLogic(int no) {
        this.no = no;
    }
    @Override
    public void run() {
        for (int i=0;i<10;i++) {
            System.out.println("Aloha from logic " + no + " Executing in thread" + Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            }catch(InterruptedException e) {

            }
        }
    }
}

