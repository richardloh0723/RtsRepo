package com.assignment.concurrent;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class main {
    //args[0] is path to read file
//args[1] is the size of thread pool; Need to try different values to fing sweet spot
//    @Benchmark
//// Change benchmark test to throughput
//    @BenchmarkMode(Mode.Throughput)
//// Specifies the number of iteration
//    @Measurement(iterations = 1)
//    @Fork(1)
//    @Timeout(time = 30)
//    @OutputTimeUnit(TimeUnit.SECONDS)
	//public void test() throws Exception {
    public static void main(String[] args) throws Exception {
        ApAlgorithm ap = new ApAlgorithm();
    }
}
