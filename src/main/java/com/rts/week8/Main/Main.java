/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week8.Main;

import com.rts.week8.Concurrent.BestMatchingSeqCalculation;
import com.rts.week8.Data.WordsLoader;
import com.rts.week8.Sequential.BestMatchingConcurrentCalculation;
import com.rts.week8.Shared.BestMatchingData;
import org.openjdk.jmh.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author emran
 */
public class Main {
//	// Benchmark annotation
//	@Benchmark
//// Change benchmark test to throughput
//	@BenchmarkMode(Mode.AverageTime)
//// Specifies the number of iteration
//	@Measurement(iterations = 1)
//	@Fork(1)
//	@Timeout(time = 30)
//	@OutputTimeUnit(TimeUnit.SECONDS)
//	public void test() throws ExecutionException, InterruptedException {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Date startTime, endTime;
		List<String> dictionary= WordsLoader.load("UKAdvancedCrypticsDictionary.txt");

		System.out.println("Dictionary Size: "+dictionary.size());

		startTime=new Date();

		String word="gro";

		BestMatchingData result = BestMatchingConcurrentCalculation.getBestMatchingWords(word, dictionary);
		List<String> results=result.getWords();
		endTime=new Date();
		System.out.println("Word: "+word);
		System.out.println("Minimum distance: "+result.getDistance());
		System.out.println("List of best matching words: "+results.size());
		results.forEach(System.out::println);
		System.out.println("Execution Time: "+(endTime.getTime()-startTime.getTime()));
	}

}