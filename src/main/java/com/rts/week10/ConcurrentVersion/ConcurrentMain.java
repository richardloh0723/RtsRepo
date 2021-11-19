/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.ConcurrentVersion;

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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author emran
 */
public class ConcurrentMain {
//	@Benchmark
//// Change benchmark test to throughput
//	@BenchmarkMode(Mode.AverageTime)
//// Specifies the number of iteration
//	@Measurement(iterations = 1)
//	@Fork(1)
//	@Timeout(time = 30)
//	@OutputTimeUnit(TimeUnit.SECONDS)
	public void test() throws IOException {
	//public static void main(String[] args) throws IOException {

		Path pathVoc = Paths.get("data", "movies.words");
		Map<String, Integer> vocIndex = VocabularyLoader.load(pathVoc);
		System.out.println("Voc Size: " + vocIndex.size());

		Path pathDocs = Paths.get("data", "movies.data");
		Documents[] documents = DocumentLoader.load(pathDocs, vocIndex);
		System.out.println("Document Size: " + documents.length);



		int K = 10;
		int SEED = 13;
		int MAX_SIZE = 400;

		Date start, end;
		start = new Date();
		DocumentCluster[] clusters = ConcurrentKMeans.calculate(documents, K, vocIndex.size(), SEED, MAX_SIZE);
		end = new Date();
		System.out.println("K: " + K + "; SEED: " + SEED + "; MAX_SIZE: " + MAX_SIZE);
		System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

		System.out.println(
				Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
						.map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));

	}

}
