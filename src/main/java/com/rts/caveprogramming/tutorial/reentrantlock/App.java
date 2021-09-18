package com.rts.caveprogramming.tutorial.reentrantlock;
/**
 * This is the multi-threading part 10 - reentrant locks
 * referred by Cave of Programming @ YouTube.
 * In this tutorial, we are going to look on reentrant lock
 * which is the alternatives of synchronized methods/blocks
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    runner.firstThread();
                }catch(InterruptedException e){}
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    runner.secondThread();
                }catch(InterruptedException e){}
            }
        });
        t1.start(); t2.start(); t1.join(); t2.join();
        runner.finished();
    }


}
