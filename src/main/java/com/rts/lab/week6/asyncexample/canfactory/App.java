package com.rts.lab.week6.asyncexample.canfactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Can generatedCan;
        Can filledCan;
        Can sealedCan;
        Can labeledCan;
        ExecutorService ex = Executors.newCachedThreadPool();

        Future<Can> canGenerator = ex.submit(new CanGenerator());

        while(!canGenerator.isDone()) {
            System.out.println("Waiting for can");
            Thread.sleep(1000);
        }
        try {
            generatedCan = canGenerator.get();
            Future<Can> canFiller = ex.submit(new filling(generatedCan));
            while(!canFiller.isDone()) {
                System.out.println("Waiting for filled can!");
                Thread.sleep(1000);
            }
            try {
                filledCan = canFiller.get();
                Future<Can> canSealer = ex.submit(new sealing(filledCan));
                while(!canSealer.isDone()) {
                    System.out.println("Waiting for sealed can!");
                    Thread.sleep(1000);
                }
                try {
                    sealedCan = canSealer.get();
                    Future<Can> canLabeler = ex.submit(new labeling(sealedCan));
                    while(!canLabeler.isDone()) {
                        System.out.println("Waiting for labeled can!");
                        Thread.sleep(1000);
                    }
                    // ends here, fresh cans here
                    try {
                        labeledCan = canLabeler.get();
                        System.out.println("The can has been done. " + labeledCan);
                    } catch(Exception e) {

                    }
                } catch (Exception e) {}
            } catch (Exception e) {}
        } catch (Exception e) {}
    }
}

class Can {
    public Can() {
        System.out.println("Unprocessed can: I'm here.");
    }
}

class CanGenerator implements Callable<Can> {
    Can can;
    @Override
    public Can call() {
        try {
            Thread.sleep(2000);
            can = new Can();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return can;
    }

}

class filling implements Callable<Can> {
    private Can generatedCan;
    //some miscellaneous process...
    private Can filledCan;
    @Override
    public Can call() throws Exception {
        Thread.sleep(2000);
        //some miscellaneous process... bla bla
        filledCan = generatedCan;
        return filledCan;
    }

    public filling(Can generatedCan) {
        this.generatedCan = generatedCan;
    }
}

class sealing implements Callable<Can> {
    private Can filledCan;
    //some miscellaneous process...
    private Can sealedCan;

    @Override
    public Can call() throws Exception {
        Thread.sleep(2000);
        //some miscellaneous process... bla bla
        sealedCan = filledCan;
        return sealedCan;
    }
    public sealing(Can filledCan) {
        this.filledCan = filledCan;
    }
}

class labeling implements Callable<Can> {
    private Can SealedCan;
    private Can LabeledCan;

    @Override
    public Can call() throws Exception {
        Thread.sleep(2000);
        //some miscellaneous process... bla bla
        LabeledCan = SealedCan;
        return LabeledCan;
    }
    public labeling(Can SealedCan) {
        this.SealedCan = SealedCan;
    }
}
