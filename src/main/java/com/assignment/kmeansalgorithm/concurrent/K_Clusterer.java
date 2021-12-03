package com.assignment.kmeansalgorithm.concurrent;/*
 * Programmed by Shephalika Shekhar
 * Class for Kmeans Clustering implemetation
 */
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.*;

public class K_Clusterer {
//	    @Benchmark
//// Change benchmark test to throughput
//    @BenchmarkMode(Mode.Throughput)
//// Specifies the number of iteration
//    @Measurement(iterations = 1)
//    @Fork(1)
//    @Timeout(time = 30)
//    @OutputTimeUnit(TimeUnit.SECONDS)
	//public void test() throws Exception {
	public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {
		ReadDataset r1 = new ReadDataset();
		List<double[]> features = r1.getFeatures();
		features.clear();

		String file= "Iris.txt";
		r1.read(file); //load data

		int ex=1;
		int k = 5;
		int distance = 1;

		// Hashmap to store centroids with index (index, centroids)
		Map<Integer, double[]> centroids = new HashMap<>();
		// calculating initial centroids
		double[] x1;
		int r =0;
		/**
		 * Jing Hoong: The initialization of initial centroid of original program
		 * is the first few data objects (based on the determined K),
		 * which is not optimal.
		 */
		for (int i = 0; i < k; i++) {
			// increment r and get that value
			x1=features.get(r = r + 1);
			centroids.put(i, x1);
		}
		// Hashmap for finding cluster indexes
		Map<double[], Integer> clusters;
		Map<double[], Integer> clustersBeforeNextCalculation;
		clusters = kmeans(features, distance, centroids, k);

		double[] db;

		// reassigning to new clusters
		// potential of parallel k-means clustering
		// split tasks to different threads
		// (if convergence, break)
		boolean isConverged = false;
		int numOfCores = Runtime.getRuntime().availableProcessors();
		for(int it = 0; it < 1000; it++) {
			List<Future<Map<double[], Integer>>> returnedFutureAssignedClusters
					= new ArrayList<>();
			ExecutorService executors = Executors.newFixedThreadPool(numOfCores);
			for (int j = 0; j < k; j++) {
				List<double[]> list = new ArrayList<>();
				for (double[] key : clusters.keySet()) {
					if (clusters.get(key)==j) {
						list.add(key);
					}
				}
				db = centroidCalculator(list, r1);
				centroids.put(j, db);
			}
			clusters.clear();
			for (int i = 0; i < numOfCores; i++) {
				int[] startEndIndex = calculateStartEndIndex(numOfCores, i, features);
				DataObjPartition task = new DataObjPartition();
				task.setStartIndex(startEndIndex[0]);
				task.setEndIndex(startEndIndex[1]);
				task.setDataObject(features);
				task.setCentroids(centroids);
				Future<Map<double[], Integer>> future = executors.submit(task);
				returnedFutureAssignedClusters.add(future);
			}
			// merge those returned future into "clusters"
			executors.shutdown();
			for(Future<Map<double[],Integer>> returnedResult :
			returnedFutureAssignedClusters) {
				clusters.putAll(returnedResult.get());
			}
//			if(clustersBeforeNextCalculation.equals(clusters)) {
//				//break the loop
//				isConverged = true;
//			}
		}
		
		//final cluster print
		System.out.println("\nFinal Clustering of Data");
		System.out.println("Feature1\tFeature2\tFeature3\tFeature4\tCluster");
		for (double[] key : clusters.keySet()) {
			for (int i = 0; i < key.length; i++) {
				System.out.print(key[i] + "\t \t");
			}
			System.out.print(clusters.get(key) + "\n");
		}
		
		//Calculate WCSS
		double wcss=0;
		
		for(int i=0;i<k;i++){
			double sse=0;
			for (double[] key : clusters.keySet()) {
				if (clusters.get(key)==i) {
					sse+=Math.pow(Distance.eucledianDistance(key, centroids.get(i)),2);
				}
				}
			wcss+=sse;
		}
		System.out.println("\n*********Programmed by Shephalika Shekhar************\n*********Results************\n" +
				"Manhattan Distance");
		System.out.println("Number of Clusters: "+k);
		System.out.println("WCSS: "+wcss);
	}
	
	//method to calculate centroids
	public static double[] centroidCalculator(List<double[]> a, ReadDataset r1) {
		
		int count = 0;
		//double x[] = new double[ReadDataset.numberOfFeatures];
		double sum=0.0;
		double[] centroids = new double[r1.getNumberOfFeatures()];
		for (int i = 0; i < r1.getNumberOfFeatures(); i++) {
			sum=0.0;
			count = 0;
			for(double[] x:a){
				count++;
				sum = sum + x[i];
			}
			centroids[i] = sum / count;
		}
		return centroids;

	}

	// method for putting features to clusters and reassignment of clusters.
	public static Map<double[], Integer> kmeans(List<double[]> features,int distance, Map<Integer, double[]> centroids, int k) {
		Map<double[], Integer> clusters = new HashMap<>();
		int k1 = 0;
		double dist=0.0;
		for(double[] x:features) {
			double minimum = 999999.0;
			for (int j = 0; j < k; j++) {
				if(distance==1){
				 dist = Distance.eucledianDistance(centroids.get(j), x);
				}
				else if(distance==2){
					dist = Distance.manhattanDistance(centroids.get(j), x);
				}
				if (dist < minimum) {
					minimum = dist;
					k1 = j;
				}

			}
			clusters.put(x, k1);
		}
		
		return clusters;

	}

	public static int[] calculateStartEndIndex(int numOfCores, int corePosition,
										List<double[]> features) {
		// return int[0] start index, int[1] end index
		int startIndex;
		int endIndex;
		int featuresPerCore;
		int remainingFeatures = 0;
		// scope: the number of cores must be in even number
		featuresPerCore = features.size() / numOfCores;

		startIndex = corePosition * featuresPerCore;
		if(corePosition == numOfCores - 1) {
			endIndex = features.size() - 1;
		} else {
			endIndex = (corePosition + 1) * featuresPerCore - 1;
		}
		return new int[] {startIndex, endIndex};
	}

}
