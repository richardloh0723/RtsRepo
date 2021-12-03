package com.assignment.learning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("myfile.txt");
        Scanner in = new Scanner(fis);
        // while the file has something inside, havent finish printing
        while(in.hasNext()) {
            System.out.println(in.nextLine());
        }
    }
}
