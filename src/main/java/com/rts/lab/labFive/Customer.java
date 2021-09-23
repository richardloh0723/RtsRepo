package com.rts.lab.labFive;

import java.util.List;

public class Customer implements Runnable {
    private List<Bun> shelfBuns;
    //removes 1 from the shelf list.
    @Override
    public void run() {
        takeBun();
    }

    public void takeBun() {
        while(!shelfBuns.isEmpty()) {
            shelfBuns.remove(0);
        }
    }

    public Customer(List<Bun> shelfBuns) {
        this.shelfBuns = shelfBuns;
    }
}
