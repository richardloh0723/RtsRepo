/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.knn.main;

import com.rts.knn.data.BankMarketing;
import com.rts.knn.loader.BankMarketingLoader;
import com.rts.knn.sequential.KnnClassifier;
import org.openjdk.jmh.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author emran
 */
public class Main {
// Benchmark annotation
@Benchmark
// Change benchmark test to throughput
@BenchmarkMode(Mode.AverageTime)
// Specifies the number of iteration
@Measurement(iterations = 1)
@Fork(1)
@Timeout(time = 30)
@OutputTimeUnit(TimeUnit.SECONDS)
public void test() {
	//public static void main(String[] args) {

	BankMarketingLoader loader = new BankMarketingLoader();
	List<BankMarketing> train = loader.load("bank.data");
	System.out.println("Train: " + train.size());
	List<BankMarketing> test = loader.load("bank.test");
	System.out.println("Test: " + test.size());
	double currentTime = 0d;
	int success = 0, mistakes = 0;

	int k = 10;
//	if (args.length == 1) {
//		k = Integer.parseInt(args[0]);
//	}

	success = 0;
	mistakes = 0;
	// 4 8 16 32 64
	int numofThreads = 4;
	KnnClassifier classifier = new KnnClassifier(train, k, numofThreads);
	try {
		Date start, end;
		start = new Date();
		for (BankMarketing example : test) {
			String tag = classifier.classify(example);
			if (tag.equals(example.getTag())) {
				success++;
			} else {
				mistakes++;
			}
		}
		end = new Date();

		currentTime = end.getTime() - start.getTime();
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println("******************************************");
	System.out.println("Serial Classifier - K: " + k);
	System.out.println("Success: " + success);
	System.out.println("Mistakes: " + mistakes);
	System.out.println("Execution Time: " + (currentTime / 1000)
			+ " seconds.");
	System.out.println("******************************************");

	//}
}
}
