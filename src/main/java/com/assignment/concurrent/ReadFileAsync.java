package com.assignment.concurrent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

/**
 * The program is retrieved from StackOverflow
 * however, we can improve the code by converting the code
 * into Callable, in which it will return the read String
 * and merge them into an array list of integer array.
 */
public class ReadFileAsync implements Callable<String> {
    private FileChannel fileChannel;
    private long startLoc;
    private int fileSize;

    //Object that gets returned
    private String stringOfFileChunk;

    public ReadFileAsync(FileChannel fileChannel,
                         long startLoc, int fileSize,
                         int sequenceNumber) {
        this.fileChannel = fileChannel;
        this.startLoc = startLoc;
        this.fileSize = fileSize;
    }

    @Override
    public String call() {
        try {
            // allocate memory for size of the file chunk
            ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
            // read the chunk of file to the RAM
            fileChannel.read(byteBuffer,startLoc);
            // convert file chunk to readable string as unicode
            stringOfFileChunk = new String(byteBuffer.array(),StandardCharsets.UTF_8);
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        return stringOfFileChunk;
    }
}
