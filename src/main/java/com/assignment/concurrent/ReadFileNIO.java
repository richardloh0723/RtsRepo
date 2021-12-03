package com.assignment.concurrent;

import com.assignment.DatFileConversion;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ReadFileNIO implements DatFileConversion{
//    @Benchmark
//// Change benchmark test to throughput
//    @BenchmarkMode(Mode.Throughput)
//// Specifies the number of iteration
//    @Measurement(iterations = 1)
//    @Fork(1)
//    @Timeout(time = 15)
//    @OutputTimeUnit(TimeUnit.SECONDS)
    private String filePath;
    private int totalNumOfDistinctItems;
    private int totalNumOfTransactions;

    public List<int[]> convertFile() throws Exception {
    //public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        List<int[]> itemSets = convertDatFileToTransactionsList(new ArrayList<>(),"mushroom.dat");
        long end = System.currentTimeMillis();

        totalNumOfTransactions = itemSets.size();
        for(int[] candidate : itemSets) {
            for(int element : candidate) {
                if (element + 1 > totalNumOfDistinctItems) {
                    totalNumOfDistinctItems = element + 1;
                }
            }
        }
        return itemSets;
    }

    public static List<int[]> convertDatFileToTransactionsList(List<int[]> frequentSets, String transactionFileName)
            throws IOException {
        String data = new String(Files.readAllBytes(Paths.get("mushroom.dat")));
        String[] content = data.split("\n");
        int[] newRay = null;
		/*
		has two jobs, one is to grab the data from .dat file
		to ArrayList for faster I/O
		2. to calculate the distinct frequent sets
		 */
        for (String line : content) {
            String[] str = line.split("\\s+");
            newRay = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                newRay[i] = Integer.valueOf(str[i]);
            }
            frequentSets.add(newRay);
        }
        return frequentSets;
    }

    public ReadFileNIO(String filePath) {
        this.filePath = filePath;
    }

    public int getTotalNumOfDistinctItems() {
        return totalNumOfDistinctItems;
    }

    public void setTotalNumOfDistinctItems(int totalNumOfDistinctItems) {
        this.totalNumOfDistinctItems = totalNumOfDistinctItems;
    }

    public int getTotalNumOfTransactions() {
        return totalNumOfTransactions;
    }

    public void setTotalNumOfTransactions(int totalNumOfTransactions) {
        this.totalNumOfTransactions = totalNumOfTransactions;
    }
}
