package com.assignment.stackoverflow.concurrentreadfile;

import java.io.*;
import static java.lang.Math.toIntExact;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileRead implements Callable<String>
{

    private FileChannel _channel;
    private long _startLocation;
    private int _size;
    int _sequence_number;

    private static final String FILE_PATH = "mushroom.dat";
    private static final int NUM_OF_CORES = 1;

    public FileRead(long loc, int size, FileChannel chnl, int sequence)
    {
        _startLocation = loc;
        _size = size;
        _channel = chnl;
        _sequence_number = sequence;
    }

    @Override
    public String call()
    {
        String string_chunk = null;
        try
        {

            //allocate memory
            ByteBuffer buff = ByteBuffer.allocate(_size);

            //Read file chunk to RAM
            _channel.read(buff, _startLocation);

            //chunk to String
            string_chunk = new String(buff.array(), Charset.forName("UTF-8"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return string_chunk;
    }

    //args[0] is path to read file
//args[1] is the size of thread pool; Need to try different values to fing sweet spot
    public static void main(String[] args) throws Exception
    {
        long start = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
        FileChannel channel = fileInputStream.getChannel();
        Future<String> returnedString;
        // declare reference to store list of future
        List<Future<String>> returnedStringList = new ArrayList<>();
        //get the total number of bytes in the file
        long remaining_size = channel.size();
        //file_size/threads
        //for each thread, distribute the large chunks of data into several smaller chunks
        //to process
        long chunk_size = remaining_size / NUM_OF_CORES;
        //thread pool
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_CORES);

        long start_loc = 0;//file pointer
        int i = 0; //loop counter
        while (remaining_size >= chunk_size)
        {
            //launches a new thread
            returnedString = executor.submit(new FileRead(start_loc, toIntExact(chunk_size), channel, i));
            remaining_size = remaining_size - chunk_size;
            start_loc = start_loc + chunk_size;
            i++;
            returnedStringList.add(returnedString);
        }

        //load the last remaining piece
        returnedString = executor.submit(new FileRead(start_loc, toIntExact(remaining_size), channel, i));
        returnedStringList.add(returnedString);

        //Tear Down
        executor.shutdown();

        //Wait for all threads to finish
        while (!executor.isTerminated())
        {
            //wait for infinity time
        }
        fileInputStream.close();
        String data_set = null;
        // assess the returned futures
        for(Future<String> returnedFuture : returnedStringList) {
            data_set = data_set + returnedFuture.get();
        }
        System.out.println(data_set);
        // print number of lines
        long end = System.currentTimeMillis();
        System.out.print(end-start);
        System.out.println(" " + countLines(data_set));
        // convert raw data set into array list of integer

    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
