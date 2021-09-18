package com.rts.caveprogramming.tutorial.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

    private int count = 0;
    /**
     * Once a thread has acquired this. lock,
     * another thread needs to wait for the lock
     * to be released.
     */
    private Lock lock = new ReentrantLock();
    /**
     * You are getting condition object from the lock
     * you are locking on.
     */
    private Condition lockCondition = lock.newCondition();

    private void increment() {
        for(int i =0;i<1000000;i++) {
            count++;
        }
    }

    /**
     * It is best practice to use try and finally
     * to catch exception. The thread needs to be unlocked
     * even it hits error during (increment()).
     * @throws InterruptedException
     */
    public void firstThread() throws InterruptedException {
        lock.lock();
        try {
            increment();
        } finally {
            lock.unlock();
        }
    }
    public void secondThread() throws InterruptedException {
        lock.lock();
        try {
            increment();
        } finally {
            lock.unlock();
        }
    }

    public void finished() {
        System.out.println("Count is: " + count);
    }
}
