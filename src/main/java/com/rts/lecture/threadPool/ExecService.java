package com.rts.lecture.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
executors: assignment is used
 */

public class ExecService {
    /**
     Explain different types of executors
     */
    public static void main(String[] args) {
        //Executors create object for us. Factory method
        //requesting executor service: responsible to thread tool
        //that contains SINGLE THREAD (permanently one thread ONLY)
        ExecutorService manager = Executors.newSingleThreadExecutor();
        /*
        Reducing the cost of utilising thread
        Threadpool is still there, alive to wait another logic to pass in
         */
        manager.submit(new Logic(1));
        manager.submit(new Logic(2));
        manager.submit(new Logic(3));
        manager.submit(new Logic(4));
        manager.submit(new Logic(5));
        manager.shutdown();
    }
}

class Logic implements Runnable {
    int no;
    public Logic(int no) {
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
