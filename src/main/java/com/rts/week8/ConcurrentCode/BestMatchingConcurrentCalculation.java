/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week8.ConcurrentCode;

import com.rts.week8.Distance.LevenshteinDistance;
import com.rts.week8.Shared.BestMatchingData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 * @author emran
 */
public class BestMatchingConcurrentCalculation {
	/** problem statement: how do I determine the start and end index??
	 *
	 */
	public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) throws InterruptedException, ExecutionException {
//		// how to we introduce concurrency?
//		List<String> results=new ArrayList<String>();
	int minDistance=Integer.MAX_VALUE;
//		int distance;
//			for (String str: dictionary) {
//				// my guess is to distribute the task into 2 threads
//				// 1. calculate the distance with LevenshteinDistance
//				distance=LevenshteinDistance.calculate(word,str);
//				// 2. if and else if statements
//				if (distance<minDistance) {
//					results.clear();
//					minDistance=distance;
//					results.add(str);
//				} else if (distance==minDistance) {
//					results.add(str);
//				}
//			}
//			// create a class called task that will implement callable. (call method will put all these into process.)
//			BestMatchingData result=new BestMatchingData();
//			result.setWords(results);
//			result.setDistance(minDistance);
//			return result;

	// 4 cores (i5 7200u)
	int numOfCores = Runtime.getRuntime().availableProcessors();
	int inc = (dictionary.size())/numOfCores;
	int startIndex;
	int endIndex;

	ExecutorService executors = Executors.newFixedThreadPool(8);
	List<Future<BestMatchingData>> returnedFutures = new ArrayList<>();

	for (int i=0;i<numOfCores; i++) {
		// ****logic here (determining the index start and end)
		startIndex = i*inc;
		if(i == numOfCores-1) {
			endIndex = dictionary.size();
		} else {
			// end index is going to be
			endIndex = (i+1) * inc;
		}
		Task task = new Task(word, dictionary, startIndex, endIndex);
		Future<BestMatchingData> future = executors.submit(task);
		returnedFutures.add(future);
	}
		executors.shutdown();
		// all the results put into this.list
		List<String> results = new ArrayList<>();
		//retrieve all the results from all the lists of futures.
		for(Future<BestMatchingData> returnedFuture : returnedFutures) {
			BestMatchingData data = returnedFuture.get();
			if (data.getDistance() < minDistance) {
				results.clear();
				minDistance=data.getDistance();
				results.addAll(data.getWords());
			} else if (data.getDistance() == minDistance) {
				results.addAll(data.getWords());
			}
		}
		BestMatchingData result=new BestMatchingData();
		result.setWords(results);
		result.setDistance(minDistance);
		return result;
	}
}

class Task implements Callable<BestMatchingData> {
	/**
	 * Provide start index and end index, focusing on specific indexes
	 * in different threads using callables using for range loop
	 */
	private List<String> dictionary;
	private String word;
	private int startIndex;
	private int endIndex;

	@Override
	public BestMatchingData call() throws Exception {
		List<String> results=new ArrayList<String>();
		int minDistance=Integer.MAX_VALUE;
		int distance;
		//for (String str: dictionary) {
		for (int i = startIndex; i < endIndex; i++) {
			// my guess is to distribute the task into 2 threads
			// 1. calculate the distance with LevenshteinDistance
			distance=LevenshteinDistance.calculate(word,dictionary.get(i));
			// 2. if and else if statements
			if (distance<minDistance) {
				results.clear();
				minDistance=distance;
				results.add(dictionary.get(i));
			} else if (distance==minDistance) {
				results.add(dictionary.get(i));
			}
		}
		BestMatchingData result=new BestMatchingData();
		result.setWords(results);
		result.setDistance(minDistance);
		return result;
	}
	public Task(String word, List<String> dictionary, int startIndex, int endIndex) {
		this.word = word;
		this.dictionary = dictionary;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
}