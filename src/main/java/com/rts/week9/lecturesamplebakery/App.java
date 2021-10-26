package com.rts.week9.lecturesamplebakery;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws IOException, TimeoutException {
        // next week: go back to matrix multiplication benchmarking
        // update concurrent implementation
        // replace thread to executor service
        // cached thread pool
        // fixed thread pool
        // change size of thread pool 50, 100, cores of your machine
//        ScheduledExecutorService bakery = Executors.newScheduledThreadPool(3);
//        ExecutorService customer = Executors.newCachedThreadPool();
//        // do it continuously
//        LinkedBlockingQueue<Bun> coolingRack = new LinkedBlockingQueue<>(18);
//        LinkedBlockingQueue<Bun> shelf = new LinkedBlockingQueue<>(10);

//        bakery.scheduleAtFixedRate(new Baker(coolingRack), 0,1, TimeUnit.HOURS);
//        bakery.scheduleAtFixedRate(new Worker(coolingRack,shelf), 0,1, TimeUnit.SECONDS);
//        bakery.scheduleAtFixedRate(new CustomerGen(customer,shelf), 0,1, TimeUnit.SECONDS);
    }
}

class Bun {}

// 5
class CustomerGen implements Runnable {
    ExecutorService customer;
    LinkedBlockingQueue<Bun> shelf;

    public CustomerGen(ExecutorService customer,LinkedBlockingQueue<Bun> shelf) {
        this.customer = customer;
        this.shelf = shelf;
    }

    public void run() {
        customer.submit(new Customer(shelf));
        customer.submit(new Customer(shelf));
    }
}

class Baker implements Runnable {
    // concurrent - safe for concurrency
    //LinkedBlockingQueue<Bun> coolingRack;

   // public Baker(LinkedBlockingQueue<Bun> coolingRack) {
    //    this.coolingRack = coolingRack;
    //}
   ConnectionFactory factory = new ConnectionFactory();
   Connection conn;
   Channel chan;

    public Baker() throws IOException, TimeoutException {
        conn = factory.newConnection();
        chan= conn.createChannel();
    }

    public void run() {
        for (int i = 0; i < 12; i++) {
            //Bun b = new Bun();
            try {
//                coolingRack.put(b);
//                System.out.println("BAKER: adding a new bun to the cooling rack!");
//                Thread.sleep(100);
                sendMsg();
            } catch (Exception e){}
        }
    }

    public void sendMsg() throws IOException {
        String msg = "bun";
        chan.basicPublish("", "hello_world", false, null, msg.getBytes());
        System.out.println("message to worker sent");
    }
}

// 4
class Worker implements Runnable {
    // concurrent - safe for concurrency
    LinkedBlockingQueue<Bun> coolingRack;
    LinkedBlockingQueue<Bun> shelf;

    public Worker(LinkedBlockingQueue<Bun> coolingRack, LinkedBlockingQueue<Bun> shelf) {
        this.coolingRack = coolingRack;
        this.shelf = shelf;
    }

    public void run() {
        for (int i = 0; i < 4; i++) {
            try {// important
                //?
                Bun b = coolingRack.take();
                System.out.println("WORKER: removing a bun from the cooling rack");
                Thread.sleep(50);
                shelf.put(b);
                System.out.println("WORKER: adding a bun to the shelf.");
            } catch (InterruptedException e){}
        }
    }
}

// 2
class Customer implements Runnable {
    LinkedBlockingQueue<Bun> shelf;
    public void run() {
        try {
            System.out.println("CUSTOMER: has entered the bakery");
            Thread.sleep(100);
            shelf.take();
            System.out.println("CUSTOMER: Has taken a bun and is leaving the bakery");
        } catch (InterruptedException e) {}
    }

    public Customer(LinkedBlockingQueue<Bun> shelf) {
        this.shelf = shelf;
    }
}

