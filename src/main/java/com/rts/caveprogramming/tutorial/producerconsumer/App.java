package com.rts.caveprogramming.tutorial.producerconsumer;

/**
 * In this tutorial/demo, we are going to look at the low level
 * synchronization of producer consumer pattern in concurrency
 * and the problems behind it.
 *
 * Methods used will be wait() and notify().
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        final Processor processor = new Processor();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processor.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
           public void run() {
               try {
                   processor.consume();
               }catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
