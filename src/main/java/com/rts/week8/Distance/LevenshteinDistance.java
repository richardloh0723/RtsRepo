/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week8.Distance;

import java.util.concurrent.Callable;

/**
 *
 * @author emran
 */
public class LevenshteinDistance implements Callable {

	private String string1;
	private String string2;

	public LevenshteinDistance(String string1, String string2) {
		this.string1 = string1;
		this.string2 = string2;
	}

	public static int calculate (String string1, String string2) {
		int[][] distances=new int[string1.length()+1][string2.length()+1];
		
		for (int i=1; i<=string1.length();i++) {
			distances[i][0]=i;
		}
		
		for (int j=1; j<=string2.length(); j++) {
			distances[0][j]=j;
		}
		
		for(int i=1; i<=string1.length(); i++) {
			for (int j=1; j<=string2.length(); j++) {
				if (string1.charAt(i-1)==string2.charAt(j-1)) {
					distances[i][j]=distances[i-1][j-1];
				} else {
					distances[i][j]=minimum(distances[i-1][j],distances[i][j-1],distances[i-1][j-1])+1;
				}
			}
		}
		
		return distances[string1.length()][string2.length()];
	}

	private static int minimum(int i, int j, int k) {
		return Math.min(i,Math.min(j, k));
	}

	@Override
	public Integer call() throws Exception {
		int[][] distances=new int[string1.length()+1][string2.length()+1];

		for (int i=1; i<=string1.length();i++) {
			distances[i][0]=i;
		}

		for (int j=1; j<=string2.length(); j++) {
			distances[0][j]=j;
		}

		for(int i=1; i<=string1.length(); i++) {
			for (int j=1; j<=string2.length(); j++) {
				if (string1.charAt(i-1)==string2.charAt(j-1)) {
					distances[i][j]=distances[i-1][j-1];
				} else {
					distances[i][j]=minimum(distances[i-1][j],distances[i][j-1],distances[i-1][j-1])+1;
				}
			}
		}
		return Integer.valueOf(distances[string1.length()][string2.length()]);
	}
}
