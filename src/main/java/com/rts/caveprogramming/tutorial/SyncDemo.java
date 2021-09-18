package com.rts.caveprogramming.tutorial;

// threads being cached
// threads interleaving
public class SyncDemo {
    private int count = 0;

    /**
     * volatile doesnt work in this scenario
     * because it does not guarantee mutual exclusion,
     * only guarantee VISIBILITY. (Can see the state of variables)
     */
    public static void main(String[] args) throws InterruptedException {
        /**
         * In this tutorial, we're going to start
         * introducing
         * 1) the concept of synchronization in Java. Why you need it
         * 2) Problems you will get on synchronized threads
         */
        SyncDemo syncDemo = new SyncDemo();
        syncDemo.doWork();
    }

    public void doWork() throws InterruptedException {
        /**
         * 3 STEPS: READ COUNT, + 1 AND MODIFY COUNT
         * Therefore, the count will not be accurate
         * as threads are doing this things at the same time.
         *
         * Therefore, we need to use synchronized keyword
         * on methods that are accessed by multiple threads.
         */
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<100000;i++){
                    incrementCount();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<100000;i++){
                    incrementCount();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join(); t2.join();
        System.out.println("Count is " + count);

        /**
         * In this case, the count is 0. This is because the count
         * gets printed out before the t1 and t2 get started.
         * Therefore, we're going to use t1.join() and t2.join() method.
         * Thus, compiler will wait two threads to die before
         * executing the sysout method.
         */

    }
    // were going to create a method which will be accessed by
    // multiple threads.

    /**
     * Or we can use AtomicInteger to increment/decrement a variable
     */
    public synchronized void incrementCount() {
        count++;
    }

}

