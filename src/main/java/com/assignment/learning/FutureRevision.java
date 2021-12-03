package com.assignment.learning;
/*
in this file, we are going to do a future revision
to implement solutions to return something using callable.
Problem: the runnable interface does not return something, it only has single variable
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Therefore, we can implement callable interface to mention which kind of var
 * that you're going to return.
 * Therefore, we can do something like this
 */
public class FutureRevision {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // placeholder that holds the value that arrives some time in the future
        // based on how long your call operation takes.
        // Future<Integer> future = executor.submit(new Task());

        /*
        By the time your executorservice is running the threads
        you can perform separate other operations here,
        and then those other operations are finished
        you can use future.get() to get the results from that
        callable tasks
         */

        /*
        When the future is not ready
        get() operation will block (it is considered as a blocking operation
        until the future is returning something (finished something)
         */

        /*
        you can also extend the submission for a multiple tasks
        have a for loop to submit multiple tasks
        Process:
        after submitting the individual callable task and assigning
        the future to the future object, we need to add those future
        objects into an arraylist or linkedlist.
         */
        List<Future<Integer>> allFutures = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            Future<Integer> future = executor.submit(new Task());
            allFutures.add(future);
        }

        /*
        Therefore, in the future, we are allowed to do a for loop
        to retrieve all the futures from the ArrayList after an amount
        of time.
         */
        for(int i=0; i<100; i++) {
            Future<Integer> returnedFuture = allFutures.get(i);
            try {
                /*
                the get() method of the future is a blocking method
                therefore we need to take the overhead into consideration
                 */
                Integer returnedResult = returnedFuture.get();
                System.out.println("Result of future: " + returnedResult);
            } catch (Exception e) {

            }
        }
        executor.shutdown();
    }
}

class Task implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(50);
        // perform some operations bla bla
        return new Random().nextInt();
    }
}
