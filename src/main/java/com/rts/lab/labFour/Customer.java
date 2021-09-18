package com.rts.lab.labFour;
/**
 * Customer is responsible to take (consume) bun
 * from shelf, and leave.
 * Producer: Worker (assigned with shelf)
 * Consumer: Customer (his job is to eat.)
 */
public class Customer implements Runnable {
    private Object shelfLock;
    @Override
    public void run() {
        //take the bun
        //leave
    }
}
