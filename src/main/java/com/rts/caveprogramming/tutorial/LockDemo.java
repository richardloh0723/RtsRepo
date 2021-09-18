package com.rts.caveprogramming.tutorial;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * In this tutorial, we are going to focus on the use
 * of locks to achieve synchronization
 */
public class LockDemo {

}

class Worker {
    private static Random random = new Random();
    private static List<Integer> list1 = new ArrayList<Integer>();
    private static List<Integer> list2 = new ArrayList<Integer>();
    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    /**
     * Takeaways: synchronized method will take twice as long.
     * The reason is synchronized method will acquire intrinsic lock
     * of Worker object. If one thread runs one method, second thread
     * will WAIT.
     * However, there is only one intrinsic lock for worker object.
     * it is not efficient that we are only have 1 lock only.
     * Hence, we only need to let the system know:
     * "Two threads cannot run the same method at the same time."
     */

    /**
     * Therefore, we can use separate locks, synchronizing locks separately.
     */
    public static void stageOne() {
        // do something with list one
        /**
         * Do some calculations bla
         * demo nia
         */
        synchronized (lock1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list1.add(random.nextInt(100));
    }

    public static void stageTwo() {
        // do something with list one
        /**
         * Do some calculations bla
         * demo nia
         */
        synchronized (lock2) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        list2.add(random.nextInt(100));
    }

    public static void process() {
        for(int i=0;i<1000;i++) {
            stageOne();
            stageTwo();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting .. ");
        long start = System.currentTimeMillis();
        /**
         * Minimalist way to run a thread.
         */
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        });
        t1.start();t2.start();
        t1.join();t2.join();
        long end = System.currentTimeMillis();

        System.out.println("Time take: " + (end-start));
        System.out.println("List 1: " + list1.size() + "\nList 2: " + list2.size());
    }
}

