package com.rts.caveprogramming.tutorial;

import java.util.Scanner;

// threads being cached
// threads interleaving
public class VolatileDemo {
    public static void main(String[] args) {
        Processor p1 = new Processor();
        p1.start();

        System.out.println("Press return to stop");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        // when java tries to optimize code,
        // thread does not expect another thread to modify
        // the code.
        p1.shutdown();
    }
}

/**
 * This thread might decide to cache the value in start,
 * so it never sees the change of value of it.
 */
class Processor extends Thread {
    //now, this code is guarantee to work in all systems
    private volatile boolean running = true;
    @Override
    public void run() {
        while(running) {
            System.out.println("Hi");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void shutdown() {
        running = false;
    }

}
