package com.rts.lab.labFour;


public class App {
    public static void main(String[] args) {
        Bakery bakery = new Bakery();
        Baker baker = new Baker(bakery);
        Worker worker = new Worker(bakery);
        Oven oven = new Oven(bakery);

        CustomerFactory customerFactory = new CustomerFactory(bakery);

        Thread t1 = new Thread(baker);
        Thread t2 = new Thread(worker);
        Thread t3 = new Thread(oven);
        Thread t4 = new Thread(customerFactory);
        Thread timer = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               baker.setAlive(false);
               worker.setAlive(false);
               oven.setAlive(false);
               customerFactory.setAlive(false);
           }
        });

        timer.start();t1.start(); t2.start(); t3.start(); t4.start();

    }
}
