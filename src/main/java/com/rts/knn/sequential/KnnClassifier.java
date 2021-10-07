/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.knn.sequential;

import com.rts.knn.data.Distance;
import com.rts.knn.data.Sample;
import com.rts.knn.distance.EuclideanDistanceCalculator;
import java.util.Arrays;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author emran
 */
public class KnnClassifier {

	/**
	 * List of train data
	 */
	private final List<? extends Sample> dataSet;

	private ThreadPoolExecutor executor;
	
	/**
	 * K parameter
	 */
	private int k;
	
	/**
	 * Constructor of the class. Initialize the internal data
	 * @param dataSet Train data
	 * @param k K parameter
	 */
	public KnnClassifier(List<? extends Sample> dataSet, int k, int numOfThreads) {
		this.dataSet=dataSet;
		this.k=k;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
	}
	
	/**
	 * Method that classifies an example
	 * @param example Example to classify
	 * @return The tag or class of the example
	 * @throws Exception Exception if something goes wrong
	 */
	public String classify (Sample example) throws InterruptedException {
		
		Distance[] distances=new Distance[dataSet.size()];

		// used to block when the for loop is running, like semaphore
		// 1000 to 0
		CountDownLatch controller = new CountDownLatch(dataSet.size());
		
		int index=0;
		// main calculation - where our performance is losing
		// delegate the responsibility to different threads

		// ccp choice 1. individual tasks - 1000 obj will be individually processes, concurrently
		// ccp choice 2: Grouping
		for (Sample localExample : dataSet) { //5000 objects
			ConcurrentIndividualTask task = new ConcurrentIndividualTask(index, distances, example, localExample, controller);
			executor.execute(task);
			index++;
		}
		// need to wait for the loop to finish
		controller.await();
		// and then sort the distances using Array static method.
		// parallel sort to reduce latency and increase performance
		Arrays.parallelSort(distances);
		// Arrays.sort(distances);
		
		Map<String, Integer> results = new HashMap<>();
		for (int i = 0; i < k; i++) {
		  Sample localExample = dataSet.get(distances[i].getIndex());
		  String tag = localExample.getTag();
		  results.merge(tag, 1, (a, b) -> a+b);
		}
		
		return Collections.max(results.entrySet(),
			    Map.Entry.comparingByValue()).getKey();
	}
}

/**
 * Class that implements Runnable to run individual task
 */
class ConcurrentIndividualTask implements Runnable {
	/**
	 * List of train data
	 */
	private int index;
	private Sample localExample;
	private Distance[] distances;
	private Sample example;
	CountDownLatch controller;

	@Override
	public void run() {
		distances[index]=new Distance();
		distances[index].setIndex(index);
		distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
		controller.countDown();
	}

	public ConcurrentIndividualTask(int index, Distance[] distances, Sample localExample, Sample example
	, CountDownLatch controller) {
		this.index = index;
		this.distances = distances;
		this.localExample = localExample;
		this.example = example;
		this.controller = controller;
	}


}
