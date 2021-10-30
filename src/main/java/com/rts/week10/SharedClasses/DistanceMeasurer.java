/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.SharedClasses;

/**
 *
 * @author emran
 */
public class DistanceMeasurer {

	public static double euclideanDistance(Word[] words, double[] centroid) {
		double distance = 0;

		int wordIndex = 0;
		for (int i = 0; i < centroid.length; i++) {
			if ((wordIndex < words.length)
					&& (words[wordIndex].getIndex() == i)) {
				distance += Math.pow(
						(words[wordIndex].getTfidf() - centroid[i]), 2);
				wordIndex++;
			} else {
				distance += centroid[i] * centroid[i];
			}
		}

		return Math.sqrt(distance);
	}

}

