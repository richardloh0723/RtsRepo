package com.rts.richard.selflearning;

public class sample {
    public static void main(String[] args) {
        for(int i=0;i<10;i++) {
            int product = 0;
            try {
                product = (i + 10) / i;
                System.out.println(product);
            } catch (ArithmeticException aex) {
                System.out.println(aex);
            }
        }
    }
}
