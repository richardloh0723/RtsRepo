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

public class ReadFileAsynchronous {
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
//    @Benchmark
//// Change benchmark test to throughput
//    @BenchmarkMode(Mode.Throughput)
//// Specifies the number of iteration
//    @Measurement(iterations = 1)
//    @Fork(1)
//    @Timeout(time = 30)
//    @OutputTimeUnit(TimeUnit.SECONDS)
    //public List<int[]> convertFile() throws Exception {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        String content = new String(Files.readAllBytes(Paths.get("mushroom.dat")));
        String[] str = content.split("\r\n|\r|\n");

        int[] newRay = null;
        // number of cores > 2^n
        int numOfCores = 4;
        int startIndex;
        int endIndex;
        int chunkPerThread = str.length / numOfCores;
        ExecutorService executors = Executors.newFixedThreadPool(numOfCores);
        List<Future<List<int[]>>> returnedFutures = new ArrayList<>();
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
            Future<List<int[]>> returnedTransactions = executors.submit(
                    new StringArrayConversion(startIndex,endIndex,chunkPerThread,str));
            returnedFutures.add(returnedTransactions);
        }
        // is shutdown thread safe?
        executors.shutdown();
        // retrieve all the results from the future array list.
        for(Future<List<int[]>> returnedTransactionChunks : returnedFutures) {
            transactions.addAll(returnedTransactionChunks.get());
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start + " " + transactions.size());
    }
}

class StringArrayConversion implements Callable<List<int[]>> {
    /*
    concept: the conversion requires start index of the str, and
    end index of the str. Therefore, all the threads will only process
    that particular chunk of string array.
     */
    private int startIndex;
    private int endIndex;
    private int chunkPerThread;
    private String[] str;
    // return frequentSets
    List<int[]> frequentSets;
    @Override
    public List<int[]> call() {
        int newRay[] = null;
        frequentSets = new ArrayList<>();
        for(int i = startIndex; i < endIndex + 1; i++) {
            // split str[i] into set of transactions
            // and convert the string into integers
            // using stream
            int[] itemsetArr = Stream.of(str[i].split("\\s"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            frequentSets.add(itemsetArr);
        }
        return frequentSets;
    }

    public StringArrayConversion(int startIndex, int endIndex,
                                 int chunkPerThread, String[] str) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.chunkPerThread = chunkPerThread;
        this.str = str;
    }
}
