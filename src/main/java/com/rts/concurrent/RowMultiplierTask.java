package com.rts.concurrent;

public class RowMultiplierTask implements Runnable {
    private final double[][] matrix1;
    private final double[][] matrix2;
    private final double[][] result;
    private final int i;

    public RowMultiplierTask(double[][] matrix1, double[][] matrix2, double[][] result, int i) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.i = i;

    }

    @Override
    public void run() {
        for (int j = 0; j < matrix2[0].length; j++) {
            result[i][j] = 0;
            for (int k = 0; k < matrix1[0].length; k++) {
                result[i][j] += matrix1[i][k] * matrix2[k][j];
            }
        }

    }
}
