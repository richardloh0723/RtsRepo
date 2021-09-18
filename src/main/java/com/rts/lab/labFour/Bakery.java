package com.rts.lab.labFour;

/**
 * In this program, we will assume the bakery is the middleman of all threads
 * that are doing producer consumer jobs
 */
public class Bakery{
    private final Object coolingRackLock; //explicit lock for cooling lock
    private final Object shelfLock; //explicit lock for shelf lock
    private final Object ovenLock; //explicit lock for oven lock

    // create an explicit variable for oven and baker
    // for the bun amount (when 12, baker takes the bun out of oven)
    // when 0, oven start to bake (programmed by baker)
    private int ovenBunAmount = 0;
    private int coolingRackBunAmount = 0;
    private int shelfBunAmount = 0;

    private boolean isClosed = false;

    public Bakery() {
        // Creates explicit locks during instantiation
        coolingRackLock = new Object();
        shelfLock = new Object();
        ovenLock = new Object();
    }

    public Object getCoolingRackLock() {
        return coolingRackLock;
    }

    public Object getShelfLock() {
        return shelfLock;
    }

    public Object getOvenLock() {
        return ovenLock;
    }

    public int getOvenBunAmount() {
        return ovenBunAmount;
    }

    public void setOvenBunAmount(int ovenBunAmount) {
        this.ovenBunAmount = ovenBunAmount;
    }

    public int getCoolingRackBunAmount() {
        return coolingRackBunAmount;
    }

    public void setCoolingRackBunAmount(int coolingRackBunAmount) {
        this.coolingRackBunAmount = coolingRackBunAmount;
    }

    public int getShelfBunAmount() {
        return shelfBunAmount;
    }

    public void setShelfBunAmount(int shelfBunAmount) {
        this.shelfBunAmount = shelfBunAmount;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }


}
