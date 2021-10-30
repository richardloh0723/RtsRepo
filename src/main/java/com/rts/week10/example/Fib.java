package com.rts.week10.example;

import java.util.concurrent.RecursiveTask;

public class Fib extends RecursiveTask<Integer> {
    final int n;
    Fib(int n) {this.n = n;}
    @Override
    protected Integer compute() {
        if (n<=1)
            return n;
        Fib f1 = new Fib(n-1);
        f1.fork();
        Fib f2 = new Fib(n-2);
        f2.fork();

        return f2.join() + f1.join();

    }
}
