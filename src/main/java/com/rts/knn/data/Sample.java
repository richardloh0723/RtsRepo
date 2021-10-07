/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.knn.data;

/**
 *
 * @author emran
 */
public abstract class Sample {

	/**
	 * Method that returns the tag or class of the example
	 * @return The tag or class of the examples
	 */
	public abstract String getTag();
	
	/**
	 * Method that return the values of the attributes of the example as an array of doubles
	 * @return The values of the attributes of the example
	 */
	public abstract double[] getExample();
}
