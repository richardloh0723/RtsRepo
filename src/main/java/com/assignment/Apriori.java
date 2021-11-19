package com.assignment;


import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;

/**
 * The class encapsulates an implementation of the Apriori algorithm to compute
 * frequent itemsets.
 *
 * Added Association Rules By Tomrock D'souza :
 * https://gist.github.com/tomrockdsouza/34bdc63161befad19ce33564a473fc58
 *
 * Datasets contains integers (>=0) separated by spaces, one transaction by
 * line, e.g. 1 2 5 2 4 2 3 1 2 4 1 3 1 3 1 3 2 5 1 3 1 2 3
 * 
 * Usage with the command line : $ java mining.Apriori fileName support
 * confidence $ java mining.Apriori chass.dat 0.22 0.7
 *
 * Usage as library: see {@link ExampleOfClientCodeOfApriori}
 * 
 * @author Martin Monperrus, University of Darmstadt, 2010
 * @author Nathan Magnus and Su Yibin, under the supervision of Howard Hamilton,
 *         University of Regina, June 2009.
 * @contributor Tomrock D'souza, St. Francis Institute Of Technology, University
 *              of Mumbai, 2017 Forked from:
 *              https://gist.github.com/monperrus/7157717
 * @copyright GNU General Public License v3 No reproduction in whole or part
 *            without maintaining this copyright notice and imposing this
 *            condition on any subsequent users.
 */
public class Apriori {
	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@Measurement(iterations = 3)
	@Fork(1)
	@Timeout(time = 30)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void testMethod() throws Exception {
		// public static void main(String[] args) throws Exception {
		ApAlgorithm apriori = new ApAlgorithm();
	}
}