package com.rts.lab.labFour;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press return key to terminate the program.");
        scanner.nextLine();

        Bakery bakery = new Bakery();
        Baker baker = new Baker(bakery);
        Worker worker = new Worker(bakery);
        Oven oven = new Oven(bakery);

        CustomerFactory customerFactory = new CustomerFactory();

        Thread t1 = new Thread();
        Thread t2 = new Thread();
        Thread t3 = new Thread();
        Thread t4 = new Thread();

    }
}
