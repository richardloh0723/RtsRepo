package com.rts.lab.labFour;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomerFactory is responsible to generate customer,
 * with fairness condition: First in first out.
 * Thus, were going to use ArrayBlockingQueue to ensure
 * fairness
 */
public class CustomerFactory implements Runnable {
    private Bakery bakery;
    private List<Customer> customers = new ArrayList<>();
    private boolean alive;
    private final String[] customerNames = {"Alan","Lisa","Josh","Katie","Jeff","Alfie","Roy","Jones","Jenny","Richard","Steve",
    "Caleb","Rubini","Justin","Bieber","Axelsen","Chen Long"};
    @Override
    public void run() {
        generateCustomer();
    }

    public void generateCustomer() {
        while(alive) {
            try {
                Thread.sleep(500);
                for (String customerName : customerNames) {
                    Customer customer = new Customer(bakery, customers);
                    Thread customerThread = new Thread(customer, customerName);
                    //18/9/2021: until here.
                    customers.add(customer);
                    customerThread.start();
                }
            }catch(InterruptedException e) {}
        }
    }

    public CustomerFactory(Bakery bakery) {
        this.bakery = bakery;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
