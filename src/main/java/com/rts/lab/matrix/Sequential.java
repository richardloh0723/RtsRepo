/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.lab.matrix;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @author emran
 */
public class Sequential {
    // Benchmark annotation
    @Benchmark
    // Change benchmark test to throughput
    // Change the size of the matrices == 1000*1000 , 2000*2000
    @BenchmarkMode(Mode.AverageTime)
    // Specifies the number of iteration
    @Measurement(iterations = 1)
    @Fork(1)
    @Timeout(time = 30)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void test() {
        //public static void main(String[] args) {

        double matrix1[][] = Generator.generate(50, 50);
        double matrix2[][] = Generator.generate(50, 50);
        double resultSerial[][] = new double[matrix1.length][matrix2[0].length];

        SequentialMultiplier.multiply(matrix1, matrix2, resultSerial);


    }

}

class SequentialMultiplier {

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService manager = Executors.newCachedThreadPool();

        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;

        int rows2 = matrix2.length;
        int columns2 = matrix2[0].length;
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < rows1; i++) {
            RowMultiplierTask task = new RowMultiplierTask(result,matrix1,matrix2,i);
            manager.submit(task);
        }
    }
    static void waitForThreads(ArrayList<Thread> threads) {
        for(Thread thread: threads) {
            try{
                thread.join();
            } catch(InterruptedException e) {}
        }
    }

}

class RowMultiplierTask implements Runnable {
    final double[][] result;
    final double[][] matrix1;
    final double[][] matrix2;

    final int row;

    public void run() {
        for (int j = 0; j < matrix2[0].length; j++) {
            result[row][j] = 0;
            for (int k = 0; k < matrix1[0].length; k++) {
                result[row][j] += matrix1[row][k] * matrix2[k][j];
            }
        }
    }

    public RowMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int row) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
    }
}
