package com.rts.caveprogramming.tutorial.producerconsumer;

import java.util.Scanner;

public class Processor {
    /**
     * 1. Two different methods will be run by different threads.
     * @throws InterruptedException
     */
    public void produce() throws InterruptedException {
        // use intrinsic lock of "this" processor object (Processor class)
        synchronized (this) {
            System.out.println("Producer thread running");
            /**
             * 2. Every object in Java has the wait object.
             * Because it is the method from Object class from Java.
             * P.S. Object class is the parent class of all classes in java
             * by default.
             * wait(); wait() to not consume resources in the system
             * Important: Can be only called inside synchronized block,
             * hands over the control of this synchronized block
             * wait(timeout);
             */
            wait();
            /**
             * Threads will not resume until two things happened
             * 1) Possible for this thread to regain lock
             */
            System.out.println("Resumed.");
        }
    }

    public void consume() throws InterruptedException {
        // thread sleep 2000ms to let producer runs first.
        Thread.sleep(2000);
        // get input
        Scanner scanner = new Scanner(System.in);
        synchronized (this) {
            /**
             * This synchronized code block will include
             * notify() method.
             * Also, notify() can only be called inside
             * synchronized keyword.
             */
            System.out.println("Waiting for return key.");
            scanner.nextLine();
            System.out.println("Return key pressed.");

            /**
             * notify here will notify one of the thread.
             * Important to know: notify WILL NOT hand over the control of lock
             * like wait(); but instead it will hand over the lock
             * iff the compiler jump out the synchronized block.
             */
            notify();
        } // <- this (sync block) will relinquish the lock, not notify() itself.
    }


}
