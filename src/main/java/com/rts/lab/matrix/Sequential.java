/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.lab.matrix;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;


/**
 * @author emran
 */
public class Sequential {
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

        SequentialMultiplier.multiply(matrix1, matrix2, resultSerial);


    }

}

class SequentialMultiplier {

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {


        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;

        int rows2 = matrix2.length;
        int columns2 = matrix2[0].length;

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                result[i][j] = 0;
                for (int k = 0; k < columns1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

    }

}
