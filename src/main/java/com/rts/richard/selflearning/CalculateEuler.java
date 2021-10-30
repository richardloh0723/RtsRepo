package com.rts.richard.selflearning;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.*;

/**
 * This is the sample code snippet that was retrieved from
 * java threads and concurrency book by Friesen, 2015.
 * page no. 88 (78 (88 of 208))
 */
public class CalculateEuler {
    final static int LASTITER = 17;

    public static void main(String[] args) {
        // the first thing that we need to do is to create a thread pool using Executors static method
        // newCachedThreadPool, newFixedThreadPool, newSingleThreadExecutor (same with newFixedThreadPool(1))
        ExecutorService executor = Executors.newFixedThreadPool(6);
        // then, a callable class will return something in the future
        Callable<BigDecimal> callable = new Callable<BigDecimal>(){
            @Override
            public BigDecimal call() {
                MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
                BigDecimal result = BigDecimal.ZERO;
                for (int i = 0; i <= LASTITER; i++) {
                    BigDecimal factorial = factorial(new BigDecimal(i));
                    BigDecimal res = BigDecimal.ONE.divide(factorial,mc);
                    result = result.add(res);
                }
                return result;
            }
            public BigDecimal factorial(BigDecimal n) {
                if(n.equals(BigDecimal.ZERO)) {
                    return BigDecimal.ONE;
                } else {
                    return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
                }
            }
        };
        // then we submit it to the executor (thread pool that we just created)
        // and create a futureTask reference of Future<V> that will receive
        // something in the future.
        Future<BigDecimal> futureTask = executor.submit(callable);
        try {
            //isDone method is a quite useful method to check whether the method is done or not
            while(!futureTask.isDone()) {
                System.out.println("I am waiting!");
            }
            // if it's done, we can retrieve the result using get() method, provided by Future<V> interface
            System.out.println(futureTask.get());
        } catch (Exception e) {}
        executor.shutdownNow();
    }
}
