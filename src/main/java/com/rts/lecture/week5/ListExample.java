package com.rts.lecture.week5;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class ListExample {
    public static void main(String[] args) {
        LinkedBlockingQueue<String> channel = new LinkedBlockingQueue<>(10);

        new Thread(new producer(channel)).start();
        new Thread(new consumer(channel)).start();
    }
}

class producer implements Runnable {
    // LinkedList<String> channel;
    LinkedBlockingQueue<String> channel;
    @Override
    public void run() {
        while(true) {
            try {
                channel.put("hello");
                Thread.sleep(5000);
            }catch(InterruptedException e){}
        }

    }
    public producer(LinkedBlockingQueue<String> channel) {
        this.channel = channel;
    }
}

class consumer implements Runnable {
    LinkedBlockingQueue<String> channel;

    /**
     * using link blocking queue
     * @param channel
     */
    public consumer(LinkedBlockingQueue<String> channel) {
        this.channel = channel;
    }
    @Override
    public void run() {
        while(true) {

            try {
                // safety aspects is guaranteed.
                //
                String msg = channel.take();
                System.out.println("consumer message " + msg);
                Thread.sleep(500);
            }
            catch(InterruptedException e) {

            }
        }
    }
}