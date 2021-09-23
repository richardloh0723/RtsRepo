package com.rts.lab.labFour;

import java.util.List;

/**
 * Customer is responsible to take (consume) bun
 * from shelf, and leave.
 * Producer: Worker (assigned with shelf)
 * Consumer: Customer (his job is to eat.)
 */
public class Customer implements Runnable {
    private Bakery bakery;
    private Object shelfLock;
    private String customerName;
    private List<Customer> customerList;
    @Override
    public void run() {
        System.out.println("Customer " + customerName
                + " is queueing!");
        try {
            takeBun();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Customer(Bakery bakery, List<Customer> customers) {
        this.bakery = bakery;
        this.shelfLock = bakery.getShelfLock();
        this.customerName = Thread.currentThread().getName();
        customerList = customers;
    }

    public void takeBun() throws InterruptedException {
        synchronized(shelfLock) {
            while(bakery.getShelfBunAmount() == 0) {
                System.out.println("Customer: shelf no bun. Wait.");
                shelfLock.wait();
            }
        }
        System.out.println("Customer: Took bun. Leaving.");
        bakery.setShelfBunAmount(bakery.getShelfBunAmount()-1);
        System.out.println("Customer " + customerName + " is leaving!");
        customerList.remove(0);
    }
}
