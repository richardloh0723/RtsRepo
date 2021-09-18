package com.rts.concurrent;

import com.rts.lab.matrix.Generator;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *     Classic concurrent benchmarking! (sequential vs. concurrent)? This is a textbook example showing the
 * benchmarking of concurrency. Spoiler alert: concurrency ( as a traditional approach ) does not work well
 * compared with sequential.
 *
 * Matrix Multiplication: Row * Column
 * we can think about delegating tasks into
 * different threads
 * 1. Delegate each row into separate threads (1000 threads, T1,T2,T3,T4,...,T1000)
 * (Increments from 10^1,2,3)
 * c in benchmarking
 * apply other concepts, benchmark it and discuss (handout homework)
 *
 * activities for next week: executor services
 *      k-means neighbouring, categorisation scenario, thread pools (different behaviours)
 *      Next2 week: Executor services, concurrent data structures linked block queue, callables and futures
 *      Phases of threads (starting, stopping, bla)
 */
public class Concurrent1 {
    // Benchmark annotation
    @Benchmark
    // Change benchmark test to throughput
    // Change the size of the matrices == 1000*1000 , 2000*2000
    @BenchmarkMode(Mode.Throughput)
    // Specifies the number of iteration
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Fork(1)
    @Timeout(time = 30, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test() {
        //public static void main(String[] args) {

        double matrix1[][] = Generator.generate(2000, 2000);
        double matrix2[][] = Generator.generate(2000, 2000);
        double resultSerial[][] = new double[matrix1.length][matrix2[0].length];

        ConcurrentMultiplier.multiply(matrix1, matrix2, resultSerial);


    }

}

class ConcurrentMultiplier {

    // Multiple threads: Ensure threads coordination to avoid problems
    private static ArrayList<Thread> threads = new ArrayList<>(); // 10, 100, 1000, 1000

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;

        int rows2 = matrix2.length;
        int columns2 = matrix2[0].length;

        for (int i = 0; i < rows1; i++) {
//            for (int j = 0; j < columns2; j++) {
//                result[i][j] = 0;
//                for (int k = 0; k < columns1; k++) {
//                    result[i][j] += matrix1[i][k] * matrix2[k][j];
//                }
//            }
            RowMultiplierTask task = new RowMultiplierTask(matrix1, matrix2, result, i);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);

            if(threads.size() % 10 == 0) {
                waitForThreads();
            }
        }
    }

    public static void waitForThreads() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch(Exception ex) {}
        }
    }

}
