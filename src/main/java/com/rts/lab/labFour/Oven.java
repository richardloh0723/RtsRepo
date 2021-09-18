package com.rts.lab.labFour;

/**
 * Oven is responsible to bake bun
 * Producer: Oven
 * Consumer: Baker
 */
public class Oven implements Runnable {

    private Object ovenLock;

    private Bakery bakery;

    // "when oven finish baking"
    private static final int MAX_BUN_AMOUNT = 12;
    // oven baking speed in milliseconds (considered 12 buns)
    private static final int BAKE_DURATION = 5000;



    @Override
    public void run() {
        ovenLock = bakery.getOvenLock();
        try {
            bakeBun();
        } catch (InterruptedException e) {}
    }

    public Oven(Bakery bakery) {
        this.bakery = bakery;
    }

    public void bakeBun() throws InterruptedException {
        while(true) {
            synchronized(ovenLock) {
                while(bakery.getOvenBunAmount() == MAX_BUN_AMOUNT) {
                    // Oven will stop baking
                    // until baker takes buns from oven
                    ovenLock.wait();
                }
                //sleep method: sleep during bake
                Thread.sleep(BAKE_DURATION);
                bakery.setOvenBunAmount(bakery.getOvenBunAmount()+12);
                //Oven ping baker to collect fresh buns
                ovenLock.notify();
            }
        }
    }
}
