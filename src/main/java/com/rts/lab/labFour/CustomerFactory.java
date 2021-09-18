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
    private List<Customer> customers = new ArrayList<>();
    private final String[] customerNames = {"Alan","Lisa","Josh","Katie","Jeff","Alfie","Roy","Jones","Jenny","Richard","Steve",
    "Caleb","Rubini","Justin","Bieber","Axelsen","Chen Long"};
    @Override
    public void run() {
        generateCustomer();
    }

    public void generateCustomer() {
        while(true) {
            for (String customerName:customerNames) {
                Customer customer = new Customer();
                Thread customerThread = new Thread(customer, customerName);
                //18/9/2021: until here.
                customers.add(customer);
            }
        }
    }

}
