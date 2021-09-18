/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.lab.matrix;

import java.util.Random;

/**
 *
 * @author emran
 */
public class Generator {
    public static double[][] generate (int rows, int columns) {
		double[][] ret=new double[rows][columns];
		Random random=new Random();
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				ret[i][j]=random.nextDouble()*10;
			}
		}
		return ret;
	}
}
