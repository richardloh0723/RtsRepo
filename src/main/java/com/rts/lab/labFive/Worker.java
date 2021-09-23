package com.rts.lab.labFive;

import java.util.List;

public class Worker implements Runnable{
    // removing buns from this list (cooling rack) 4 every seconds
    // put shelf
    private List<Bun> coolingRackBuns;
    private List<Bun> shelfBuns;
    @Override
    public void run() {
        removeBun();
    }
    public Worker(List<Bun> coolingRackBuns, List<Bun> shelfBuns) {
        this.coolingRackBuns = coolingRackBuns;
        this.shelfBuns = shelfBuns;
    }

    public void removeBun() {
        try {
            while(true) {
                while(!coolingRackBuns.isEmpty()) {
                    Thread.sleep(4000);
                    for (int i = 0; i < 12; i++) {
                        Bun temp = coolingRackBuns.get(0);
                        shelfBuns.add(temp);
                        coolingRackBuns.remove(temp);
                    }
                }
                // how to wait?
                // how to implement constraints
            }
        } catch (InterruptedException e) {

        }
    }


}
