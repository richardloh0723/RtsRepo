package com.rts.lab.labFive;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bakery {
    private static List<Bun> coolingRackBuns = new LinkedList<>();
    private static List<Bun> shelfBuns = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException{
        ExecutorService manager = Executors.newCachedThreadPool();
        manager.submit(new Baker(coolingRackBuns));
        manager.submit(new Worker(coolingRackBuns, shelfBuns));
        while(true) {
            // 2 customers walk into bakery
            Thread.sleep(1000);
            manager.submit(new Customer(shelfBuns));
            manager.submit(new Customer(shelfBuns));
        }
    }
    // executor services
    // scheduled executor services
    // list

}
