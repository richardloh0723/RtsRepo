package com.rts.lecture.sample;

import java.util.concurrent.*;

public class SampleScheduledThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        //future is the placeholder that will arrive some time in the future
        //we can also extends submission in multiple tasks.
        Future<Integer> future = service.submit(new Task());
        service.submit(new Task());
        // perform some unrelated operations
        future.get(); // this will get the actual integer itself - 520
                      // and will block.
        System.out.println("Thread name: " + Thread.currentThread().getName());
    }

}

/**
 * Runnable interface has only single method call run()
 * and does not return anything.
 */
class Task implements Callable {
    public Integer call() throws InterruptedException {
        Thread.sleep(3000);
        return 520;
    }

}

class coolingRodsTask implements Callable<Boolean> {
    public Boolean call() throws Exception {
        return deployRods();
    }
    public Boolean deployRods() throws InterruptedException {
        System.out.println("cooling.");
        Thread.sleep(500);
        System.out.println("cooling complete.");
        return true;
    }
}





