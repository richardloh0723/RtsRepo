package com.assignment.stackoverflow.concurrentreadfile;

import com.assignment.DatFileConversion;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ReadFileBlockingQueue {
    /**
        The main issue for the original code is that
        the algorithm requires redundant read and write operations
        that could be simplified into reading the file into one time
        and store it inside a String variable,
        and convert the String into array list of integer array.

        The main issue is the sequential method of converting String
        into ArrayList<int[]> requires 3.381 seconds, which can be improved
        using multithreading to split the conversion task into multiple
        subpart in order to let the threads do the job.
         */
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
    public List<int[]> convertFile() throws Exception {
    //public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        String content = new String(Files.readAllBytes(Paths.get("mushroom.dat")));
        String[] str = content.split("\r\n|\r|\n");

        LinkedBlockingQueue<int[]> transactionsList = new LinkedBlockingQueue<>();

        int[] newRay = null;
        // number of cores > 2^n
        int numOfCores = 4;
        int startIndex;
        int endIndex;
        int chunkPerThread = str.length / numOfCores;
        ExecutorService executors = Executors.newFixedThreadPool(numOfCores);
        CountDownLatch controller = new CountDownLatch(numOfCores);
        List<int[]> transactions = new ArrayList<>();
        for(int i = 0; i < numOfCores; i++) {
            // determining start index and end index
            startIndex = i * chunkPerThread;
            // determining if it is the last chunk
            // true: endIndex == length of the string (default: 503888)
            // false: the chunk size * i (where its located) - 1 (array starts from 0)
            // *only applicable to 2 power numbers
            if(i == numOfCores - 1) {
                endIndex = str.length - 1;
            } else {
                endIndex = (i+1) * chunkPerThread - 1;
            }
            executors.execute(new StringArrayConversionBlockingQ(startIndex,endIndex,
                                        transactionsList,str, controller));

        }
        // is shutdown thread safe?
        controller.await();
        executors.shutdown();
        // retrieve all the results from the future array list.
        long end = System.currentTimeMillis();
        System.out.println(end - start + " " + transactionsList.size());
        return transactions;
    }
}

class StringArrayConversionBlockingQ implements Runnable {
    /*
    concept: the conversion requires start index of the str, and
    end index of the str. Therefore, all the threads will only process
    that particular chunk of string array.
     */
    private int startIndex;
    private int endIndex;
    private LinkedBlockingQueue<int[]> transactionsList;
    private String[] str;
    private CountDownLatch controller;
    @Override
    public void run() {
        int newRay[] = null;
        for(int i = startIndex; i < endIndex + 1; i++) {
            // split str[i] into set of transactions
            // and convert the string into integers
            // using stream
            int[] itemsetArr = Stream.of(str[i].split("\\s"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            transactionsList.add(itemsetArr);
        }
        controller.countDown();
    }

    public StringArrayConversionBlockingQ(int startIndex, int endIndex,
                                          LinkedBlockingQueue<int[]> transactionsList,
                                          String[] str,
                                          CountDownLatch controller) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.transactionsList = transactionsList;
        this.str = str;
        this.controller = controller;
    }
}
