package com.assignment.concurrent;

import com.assignment.DatFileConversion;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ReadFileConventional {
//    @Benchmark
//// Change benchmark test to throughput
//    @BenchmarkMode(Mode.Throughput)
//// Specifies the number of iteration
//    @Measurement(iterations = 1)
//    @Fork(1)
//    @Timeout(time = 30)
//    @OutputTimeUnit(TimeUnit.SECONDS)
    //public List<int[]> convertFile() throws Exception {
    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Scanner file = new Scanner(new File("mushroom.dat"));
        int[] newRay = null;
        List<int[]> frequentSets = new ArrayList<>();
		/*
		has two jobs, one is to grab the data from .dat file
		to ArrayList for faster I/O
		2. to calculate the distinct frequent sets
		 */
        while (file.hasNext()) {
            String line = file.nextLine();
            String[] str = line.split("\\s+");
            newRay = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                newRay[i] = Integer.valueOf(str[i]);
            }
            frequentSets.add(newRay);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(" " + frequentSets.size());
    }
}
