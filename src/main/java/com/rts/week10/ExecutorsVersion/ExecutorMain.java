/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.ExecutorsVersion;

import com.rts.week10.SharedClasses.DocumentCluster;
import com.rts.week10.SharedClasses.DocumentLoader;
import com.rts.week10.SharedClasses.Documents;
import com.rts.week10.SharedClasses.VocabularyLoader;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author emran
 */

/**
 * the main method is comprised on three procedures
 * 1. read the data
 * 2. init clusters
 * 3. call k-means method
 *
 *
 *
 * @author Richard Loh
 */
public class ExecutorMain {
//	@Benchmark
//// Change benchmark test to throughput
//	@BenchmarkMode(Mode.AverageTime)
//// Specifies the number of iteration
//	@Measurement(iterations = 1)
//	@Fork(1)
//	@Timeout(time = 30)
//	@OutputTimeUnit(TimeUnit.SECONDS)
	//public void test() throws IOException {
	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

		Path pathVoc = Paths.get("data", "movies.words");
		Map<String, Integer> vocIndex = VocabularyLoader.load(pathVoc);
		System.out.println("Voc Size: " + vocIndex.size());

		Path pathDocs = Paths.get("data", "movies.data");
		Documents[] documents = DocumentLoader.load(pathDocs, vocIndex);
		System.out.println("Document Size: " + documents.length);



		int K = 10;
		int SEED = 13;

		Date start, end;
		start = new Date();
		DocumentCluster[] clusters = ExecutorKMeans.calculate(documents, K, vocIndex.size(), SEED);
		end = new Date();
		System.out.println("K: " + K + "; SEED: " + SEED);
		System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

		System.out.println(
				Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
						.map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));

	}

}
