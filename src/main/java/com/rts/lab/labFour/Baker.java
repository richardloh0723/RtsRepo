package com.rts.lab.labFour;
/**
 * Baker is responsible to take (consume) bun
 * from the oven, and let it rest in cooling rack.
 * Producer: Oven
 * Consumer: Baker (assigned with cooling rack)
 */
public class Baker implements Runnable {
    private final Bakery bakery;

    private static final int COOLING_RACK_BUN_LIMIT = 18;

    private final Object ovenLock;

    private final Object coolingRackLock;

    private boolean alive = true;

    @Override
    public void run() {
        try{
            programOven();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Baker produces buns at a rate of 12 every 5 seconds.
     * Baker task: to program the oven to bake buns at
     * a rate of 12 every 5 seconds.
     */
    public void programOven() throws InterruptedException {
        System.out.println("Baker: Baking bun!");
        while(alive) {
            synchronized(ovenLock) {
                while(bakery.getOvenBunAmount() == 0) {
                    // baker wait for the oven to bake the dough
                    System.out.println("Baker: Waiting for oven..");
                    ovenLock.wait();
                }
                System.out.println("Baker: Take from oven..");
                bakery.setOvenBunAmount(bakery.getOvenBunAmount()-12);
                // baker reprogram the oven after taking fresh buns
                // from the oven
                System.out.println("Baker: Reprogram the oven.");
                ovenLock.notify();
            }
            // baker will become producer of cooling rack bun.
            // in this case, baker and worker will obtain
            // coolingRackLock.
            synchronized(coolingRackLock) {
                while(bakery.getCoolingRackBunAmount() == COOLING_RACK_BUN_LIMIT) {
                    // baker will wait for the cooling rack when rack
                    // is full of buns (18 buns)
                    System.out.println("Baker: Cooling rack is full. Waiting.");
                    coolingRackLock.wait();
                }
                bakery.setCoolingRackBunAmount(bakery.getCoolingRackBunAmount()+12);
                // baker to notify worker when the bun is ready
                // iff bun > 0.
                // else this statement will not do anything. (useless)
                System.out.println("Baker: Cooling rack is filled!");
                coolingRackLock.notify();

            }
        }
    }

    public Baker(Bakery bakery) {
        this.bakery = bakery;
        ovenLock = bakery.getOvenLock();
        coolingRackLock = bakery.getCoolingRackLock();
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
