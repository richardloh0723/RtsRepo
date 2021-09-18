package com.rts.caveprogramming.tutorial.lowlevelproducerconsumer;

import java.util.LinkedList;
import java.util.Random;

/**
 * It is very important to know the concept of intrinsic lock in Java object.
 * Intrinsic lock (monitor lock) is an implicit internal entity associated
 * with each instance of objects.
 */
public class Processor {
    /**
     * Create a linked list that has Integer objects
     * And set the LIMIT as 10.
     */
    private LinkedList<Integer> list = new LinkedList<Integer>();

    private static final int LIMIT = 10;
    /**
     * In this case, we could use intrinsic lock to lock processor obj by itself,
     * but we don't do that here because we're going to emphasize that
     * we need to call wait() and notify() on the object we're locking on
     * so, I make it explicit this time.
     */
    private Object lock = new Object();

    public void produce() throws InterruptedException {
        int value = 0;
        // run indefinitely, not recommended for real programs
        while(true) {
            // synchronized on this lock object
            synchronized (lock) {
                /**
                 * Also, I can only add items when there's below limit
                 * or InOtherWords, still got space la
                 * Therefore, I use while loop each and every time
                 * the linked list size eq LIMIT.
                 */
                while(list.size() == LIMIT) {
                    // emphasizing that we're going to use this obj
                    // NEED TO CALL WAIT FROM THE OBJECT THAT WE'RE
                    // LOCKING
                    lock.wait();
                }
                // add value as fast as i can
                /**
                 * Java will autoboxing the type for me
                 * int -> Integer
                 */
                list.add(value++);
                lock.notify(); //tell consume thread: got resource edy!!
            }
        }
    }

    public void consume() throws InterruptedException {

        Random random = new Random();

        while(true) {
            // synchronized on this lock object
            synchronized (lock) {
                //length of the list
                while(list.size() == 0) {
                    /**
                     * This thread will go to sleep if the list
                     * is full.
                     */
                    lock.wait();
                }

                System.out.print("List size is: " + list.size());
                /**
                 * removeFirst means remove the first element
                 * of linked list. To achieve first-in-first-out
                 * in queue
                 */
                int value = list.removeFirst();
                System.out.println("; value is " + value);
                /**
                 * This thread will wake up the producer
                 * when the thread is not full.
                 * i.o.w: when it consumed one thread
                 */
                lock.notify();
            }

            Thread.sleep(random.nextInt(1000));
        }
    }
}
