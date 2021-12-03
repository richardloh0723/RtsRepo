package com.assignment.learning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class WriteFile {
    public static void main(String[] args) throws FileNotFoundException {
        // create a file output stream objects
        // add another parameter (append: true) to enable appending on file.
        FileOutputStream fos = new FileOutputStream("myFile.txt");
        // create a print writer object
        PrintWriter pw = new PrintWriter(fos);

        // write some stuffs onto the file
        pw.println("I love Java");
        pw.println(56);
        pw.close();

        System.out.println("File myFile.txt was written to.");
    }
}
