/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.ExecutorsVersion;

import com.rts.week10.SharedClasses.DistanceMeasurer;
import com.rts.week10.SharedClasses.DocumentCluster;
import com.rts.week10.SharedClasses.Documents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author emran
 */

public class ExecutorKMeans {



	public static DocumentCluster[] calculate(Documents[] documents,
											  int clusterCount, int vocSize, int seed) throws ExecutionException, InterruptedException {
		DocumentCluster[] clusters = new DocumentCluster[clusterCount];

		Random random = new Random(seed);
		for (int i = 0; i < clusterCount; i++) {
			clusters[i] = new DocumentCluster(vocSize, new ArrayList<>());
			clusters[i].initialize(random);
		}

		boolean change = true;

		int numSteps = 0;
		while (change) {
			change = assignment(clusters, documents);
			update(clusters);
			numSteps++;
		}
		System.out.println("Number of steps: "+numSteps);
		return clusters;
	}
	// the assignment static method can be implemented ExecutorService
	// by distributing different documents based on the cores that we
	// defined.

	// therefore, we can create a class that calculate the clusters
	private static boolean assignment(DocumentCluster[] clusters,
			Documents[] documents) throws ExecutionException, InterruptedException {
		int numOfCores = Runtime.getRuntime().availableProcessors();
		ExecutorService executors = Executors.newFixedThreadPool(numOfCores);
		List<Future<Boolean>> returnedResults = new ArrayList<>();
		int startIndex;
		int endIndex;
		int documentsPerThread = (documents.length)/numOfCores;
		// int numChanges = 0;
		// numChanges should be changed into something safe
		// because it is being assessed by multiple threads.
		AtomicInteger numChanges = new AtomicInteger();

		for (DocumentCluster cluster : clusters) {
			cluster.clearClusters();
		}

		// determining the index start to end
		for (int i=0; i < numOfCores; i++) {
			System.out.println("number of cores: " + numOfCores + "documents per thread: " + documentsPerThread);
			startIndex = i * documentsPerThread;
			endIndex = (i+1) * documentsPerThread - 1;

			ResultTask task = new ResultTask(startIndex,endIndex,clusters,documents);
			Future<Boolean> futureResult = executors.submit(task);
			returnedResults.add(futureResult);
		}
		executors.shutdown();
		//retrieve all the results from all the futures
		for(Future<Boolean> returnedResult : returnedResults) {
			Boolean result = returnedResult.get();
			if(result) {
				numChanges.incrementAndGet();
			}
		}
		// based on the size of the documents, we can distribute the tasks
		// evenly to threads that we predefined (via newfixedthreadpool).
		// e.g. our document is 1000 in length, therefore each thread will have 250
		// documents to work on (0 - 249, 250 - 499, 500 - 749, 750 - 999)

//		for (Documents document : documents) {
//			double distance = Double.MAX_VALUE;
//			DocumentCluster selectedCluster = null;
//			for (DocumentCluster cluster : clusters) {
//				double curDistance = DistanceMeasurer.euclideanDistance(
//						document.getData(), cluster.getCentroid());
//				if (curDistance < distance) {
//					distance = curDistance;
//					selectedCluster = cluster;
//				}
//			}
//			selectedCluster.addDocument(document);
//			boolean result = document.setCluster(selectedCluster);
//		}
		System.out.println("Number of Changes: " + numChanges);
		return numChanges.get() > 0;
	}

	private static void update(DocumentCluster[] clusters) {
		for (DocumentCluster cluster : clusters) {
			cluster.calculateCentroid();
		}

	}

}

class ResultTask implements Callable<Boolean> {

	private int startIndex;
	private int endIndex;
	private DocumentCluster[] clusters;
	private Documents[] documents;
	private Boolean result;

	ResultTask(int startIndex, int endIndex, DocumentCluster[] clusters, Documents[] documents) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.clusters = clusters;
		this.documents = documents;
	}

	@Override
	public Boolean call() throws Exception {
		for (int i = startIndex; i < endIndex; i++) {
			double distance = Double.MAX_VALUE;
			DocumentCluster selectedCluster = null;
			for (DocumentCluster cluster : clusters) {
				double curDistance = DistanceMeasurer.euclideanDistance(
						documents[i].getData(), cluster.getCentroid());
				if (curDistance < distance) {
					distance = curDistance;
					selectedCluster = cluster;
				}
			}
			selectedCluster.addDocument(documents[i]);
			result = documents[i].setCluster(selectedCluster);
		}
		return result;
	}
}
