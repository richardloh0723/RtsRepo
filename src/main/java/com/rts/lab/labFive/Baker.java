package com.rts.lab.labFive;

import java.util.List;

public class Baker implements Runnable{
    // baker logic - create buns object 12 every 5 seconds
    // buns shared across separate tasks
    // create buns - 5 seconds
    // add to this list
    private List<Bun> coolingRackBuns;
    @Override
    public void run() {
        createBun();
    }

    public void createBun() {
        while(true) {
            try {
                Thread.sleep(5000);
                System.out.println("Baker: Buns has been baked.");
                for (int i=0;i<12;i++) {
                    Bun bun = new Bun();
                    coolingRackBuns.add(bun);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public Baker(List<Bun> coolingRackBuns) {
        this.coolingRackBuns = coolingRackBuns;
    }

}
