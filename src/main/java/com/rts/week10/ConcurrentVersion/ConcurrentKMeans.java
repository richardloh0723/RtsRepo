/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.ConcurrentVersion;

import com.rts.week10.SharedClasses.DistanceMeasurer;
import com.rts.week10.SharedClasses.DocumentCluster;
import com.rts.week10.SharedClasses.Documents;
import org.openjdk.jmh.annotations.Fork;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author emran
 */

public class ConcurrentKMeans {

	public static DocumentCluster[] calculate(Documents[] documents,
											  int numberClusters, int vocSize, int seed,
											  int maxSize) {
		DocumentCluster[] clusters = new DocumentCluster[numberClusters];

		Random random = new Random(seed);
		for (int i = 0; i < numberClusters; i++) {
			//clusters[i] = new DocumentCluster(vocSize, new ArrayList<>());
			clusters[i] = new DocumentCluster(vocSize, new ConcurrentLinkedQueue<>());
			clusters[i].initialize(random);
		}

		boolean change = true;

		// introduce ForkJoinPool
		ForkJoinPool pool = new ForkJoinPool();

		int numSteps = 0;
		while (change) {
			change = assignment(clusters, documents, maxSize, pool);
			update(clusters, maxSize, pool);
			numSteps++;
		}
		pool.shutdown();
		System.out.println("Number of steps: "+numSteps);
		return clusters;
	}

	private static boolean assignment(DocumentCluster[] clusters,
			Documents[] documents, int maxSize, ForkJoinPool pool) {


		for (DocumentCluster cluster : clusters) {
			cluster.clearClusters();
		}

		AtomicInteger numChanges = new AtomicInteger(0);
		//for (Documents document : documents) {
		AssignmentTask task = new AssignmentTask(clusters,documents,0,
									documents.length,numChanges,maxSize);
		pool.execute(task);
		task.join();
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
//			if (result) {
//				numChanges++;
//			}
		//}
		System.out.println("Number of Changes: " + numChanges);
		return numChanges.get() > 0;
	}

	private static void update(DocumentCluster[] clusters, int maxSize, ForkJoinPool pool) {
		UpdateTask task = new UpdateTask(clusters, 0, clusters.length, maxSize);
//		for (DocumentCluster cluster : clusters) {
//			cluster.calculateCentroid();
//		}
		pool.execute(task);
		task.join();

	}

}
