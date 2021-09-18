package com.rts.lab.labFour;
/**
 * Worker is responsible to take (consume) bun
 * from cooling rack, and place them on shelf.
 * Producer: Baker (assigned with cooling rack)
 * Consumer: Worker (assigned with shelf)
 */
public class Worker implements Runnable {
    private Bakery bakery;
    private final Object coolingRackLock;
    private final Object shelfLock;
    private static final int SHELF_BUN_LIMIT = 10;
    private int shelfBunAmt = 0;
    @Override
    public void run() {
        try {
            execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Worker(Bakery bakery) {
        this.bakery = bakery;
        coolingRackLock = bakery.getCoolingRackLock();
        shelfLock = bakery.getShelfLock();
    }
    /**
     * execute() method created for simplicity
     * 1. worker takes the buns from the cooling rack
     * 2. worker places the buns to the gondola
     */
    public void execute() throws InterruptedException{
        while(true) {
            takeBunFromCoolingRack();
            placeBunToShelf();
        }
    }

    /**
     * takeBunFromCoolingRack() is a consumer method as the worker
     * in "consuming" bun from the cooling rack.
     */
    public void takeBunFromCoolingRack() throws InterruptedException {
        synchronized(coolingRackLock) {
            while(bakery.getCoolingRackBunAmount() == 0) {
                /**
                 * Worker will sleep if there is no bun on the
                 * cooling rack
                 */
                coolingRackLock.wait();
            }
            // Removes 4 buns per 1000ms from cooling rack
            // and place it to shelf
            Thread.sleep(1000);
            bakery.setCoolingRackBunAmount(bakery.getCoolingRackBunAmount()-4);
            // condition: when cooling rack reaches 18 buns and after worker took it,
            // notifying baker is needed to let him continue his job.
            coolingRackLock.notify();
        }
    }

    /**
     * placeBunToShelf() is a producer method as the worker
     * are "producing (placing)" bun to the shelf.
     */
    public void placeBunToShelf() throws InterruptedException {
        synchronized(shelfLock) {
            while(bakery.getShelfBunAmount() == 10) {
                // when the shelf reaches the maximum amount (10),
                // wait for customer to notify that the shelf
                // has got place.
                shelfLock.wait();
            }
            bakery.setShelfBunAmount(bakery.getShelfBunAmount()+4);
            // situations when shelf == 0, notify customer that
            // the shelf is filled
            shelfLock.notify();
        }
    }
}
