package com.assignment.concurrent;

import com.assignment.DatFileConversion;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Math.toIntExact;

public class ApAlgorithm {

	private List<int[]> listOfCurrentItemSets;
	private String filePath;
	private int totalNumOfDistinctItems;
	private int totalNumOfTransactions;
	private double minimumSupport;
	private double minimumConfidence;
	private List<String> frequentItemSetsList = new ArrayList<String>();

	// modified
	private List<int[]> datFileCandidates;
	long start;
	private final int numOfCores = Runtime.getRuntime().availableProcessors();

	/**
	 * generates the apriori itemsets from a file
	 * @throws Exception
	 */
	public ApAlgorithm() throws Exception {
		start = System.currentTimeMillis();
		configure();
		go();
	}

	private void go() throws Exception {

		/*
		First, we are required to generate K=1 frequent itemsets candidate
		in the first initialization to find K+1 itemsets. Furthermore, (e.g.)
		for the second iteration, we need to generate K+1 candidates to generate
		K+2 itemsets, so on and so forth.
		 */
		listOfCurrentItemSets = generateOneFrequentItemsetCandidates();
		int itemSetsTotal = 1;
		int frequentSetsTotal = 0;

		while (listOfCurrentItemSets.size() > 0) {
			// potential: method to start frequent itemSets calculation
			calculateFrequentItemsets();
			frequentSetsTotal += listOfCurrentItemSets.size();
			log("Found " + listOfCurrentItemSets.size() + " frequent itemsets of size " + itemSetsTotal + " (with support "
					+ (minimumSupport * 100) + "%)");
			createNewItemsetsFromPreviousOnes();
			itemSetsTotal++;
		}

		// display the execution time
		long end = System.currentTimeMillis();
		log("Execution time is: " + ((double) (end - start) / 1000) + " seconds.");
		log("Found " + frequentSetsTotal + " frequents sets for support " + (minimumSupport * 100) + "% (absolute "
				+ Math.round(totalNumOfTransactions * minimumSupport) + ")");
		log("Done");
		/*
		method to calculate minimum confidence
		 */
		//calculateMinimumConfidence();
	}

	/** triggers actions if a frequent item set has been found */
	private void foundFrequentItemSet(int[] itemset, int support) {
		String New, New1;

		New = Arrays.toString(itemset);
		New1 = New.substring(0, New.length() - 1) + ", " + support + "]";
		frequentItemSetsList.add(New1);
		System.out.println(New + "  (" + ((support / (double) totalNumOfTransactions)) + " " + support + ")");

	}

	/** outputs a message in Sys.err if not used as library */
	private void log(String message) {
		System.err.println(message);
	}

	/**
	 * This method initialize the number of items in a dataset,
	 * number of transactions in a dataset
	 * and the minimum support and confidence.
	 * @throws Exception
	 */
	private void configure() throws Exception {
		filePath = "mushroom.dat";
		minimumSupport = 0.6;
		minimumConfidence = .8;
		// initialize total number of distinct items and
		totalNumOfDistinctItems = 0;
		// total number of transactions
		totalNumOfTransactions = 0;
		ReadFileNIO readFileUsingNIO = new ReadFileNIO(filePath);

		datFileCandidates = readFileUsingNIO.convertFile();
		// encapsulation
		totalNumOfTransactions = readFileUsingNIO.getTotalNumOfTransactions();
		totalNumOfDistinctItems = readFileUsingNIO.getTotalNumOfDistinctItems();
//		BufferedReader data_in = new BufferedReader(new FileReader(transactionFileName));
//		while (data_in.ready()) {
//			String line = data_in.readLine();
//			if (line.matches("\\s*"))
//				continue; // ignore when its empty lines
//			totalNumOfTransactions++;
//			StringTokenizer t = new StringTokenizer(line, " ");
//		}

		outputConfig();

	}

	/**
	 * outputs the current configuration
	 */
	private void outputConfig() {
		// output config info to the user
		log("Input configuration: " + totalNumOfDistinctItems + " items, " + totalNumOfTransactions + " transactions, ");
		log("minsup = " + minimumSupport + "%");
	}

	/**
	 * instantiate an ArrayList, and insert all the K=1 itemset
	 * into the Arraylist {@link int[] listOfCurrentItemSets}.
	 * In this case, all the distinct items will be considered as
	 * an element in int[] array with one values (in int)
	 */
	public ArrayList<int[]> generateOneFrequentItemsetCandidates() {
		List<int[]> listOfOneFreqItemSetCandidates = new ArrayList<>();
		for (int i = 0; i < totalNumOfDistinctItems; i++) {
			int[] candidates = { i };
			listOfOneFreqItemSetCandidates.add(candidates);
		}
		return (ArrayList<int[]>) listOfOneFreqItemSetCandidates;
	}

	/**
	 * if m is the size of the current itemsets, generate all possible itemsets of
	 * size n+1 from pairs of current itemsets replaces the itemsets of itemsets by
	 * the new ones
	 */
	private void createNewItemsetsFromPreviousOnes() {
		// by construction, all existing itemsets have the same size
		int currentSizeOfItemsets = listOfCurrentItemSets.get(0).length;
		log("Creating itemsets of size " + (currentSizeOfItemsets + 1) + " based on " + listOfCurrentItemSets.size()
				+ " itemsets of size " + currentSizeOfItemsets);

		HashMap<String, int[]> tempCandidates = new HashMap<String, int[]>(); // temporary candidates

		// compare each pair of itemsets of size n-1
		for (int i = 0; i < listOfCurrentItemSets.size(); i++) {
			for (int j = i + 1; j < listOfCurrentItemSets.size(); j++) {
				int[] X = listOfCurrentItemSets.get(i);
				int[] Y = listOfCurrentItemSets.get(j);

				assert (X.length == Y.length);

				// make a string of the first n-2 tokens of the strings
				int[] newCand = new int[currentSizeOfItemsets + 1];
				for (int s = 0; s < newCand.length - 1; s++) {
					newCand[s] = X[s];
				}

				int ndifferent = 0;
				// then we find the missing value
				for (int s1 = 0; s1 < Y.length; s1++) {
					boolean found = false;
					// is Y[s1] in X?
					for (int s2 = 0; s2 < X.length; s2++) {
						if (X[s2] == Y[s1]) {
							found = true;
							break;
						}
					}
					if (!found) { // Y[s1] is not in X
						ndifferent++;
						// we put the missing value at the end of newCand
						newCand[newCand.length - 1] = Y[s1];
					}

				}

				// we have to find at least 1 different, otherwise it means that we have two
				// times the same set in the existing candidates
				assert (ndifferent > 0);

				if (ndifferent == 1) {
					// HashMap does not have the correct "equals" for int[] :-(
					// I have to create the hash myself using a String :-(
					// I use Arrays.toString to reuse equals and hashcode of String
					Arrays.sort(newCand);
					tempCandidates.put(Arrays.toString(newCand), newCand);
				}
			}
		}

		// set the new itemsets
		listOfCurrentItemSets = new ArrayList<int[]>(tempCandidates.values());
		log("Created " + listOfCurrentItemSets.size() + " unique itemsets of size " + (currentSizeOfItemsets + 1));

	}

	private boolean[] identifyIsItemExist(int[] transaction, boolean[] boolListIsItemExist) {
		Arrays.fill(boolListIsItemExist,false);
		for(int element : transaction) {
			boolListIsItemExist[element] = true;
		}
		return boolListIsItemExist;
	}

	/**
	 * passes through the data to measure the frequency of sets in listOfCurrentItemSets
	 * then filter those that are under the minimum support (minSup)
	 */
	private void calculateFrequentItemsets() throws Exception {
		/*
			first part: scan through all the transactions to see if it is containing
			the desired candidates
		 */
		log("Passing through the data to compute the frequency of " + listOfCurrentItemSets.size() + " itemsets of size "
				+ listOfCurrentItemSets.get(0).length);

		// Instantiate an ArrayList to put found frequent candidates to the list<int[]>
		List<int[]> frequentCandidates = new ArrayList<>();

		// whether the transaction has all the items in the itemSet
		boolean transactionIsMatchedWithCandidate;

		// the number of successful matches, initialized by zeros
		int[] count = new int[listOfCurrentItemSets.size()];

		boolean[] boolListIsItemExist = new boolean[totalNumOfDistinctItems];

		// for each transaction
		/*
		modification 1: the current process of continuously reading the file is inefficient.
		Therefore, each line could be stored inside an integer array (heap) called
		datFileCandidates
		 */
		/*
		idea: split the iteration of totalNumOfTransactions to different threads to process
		 */
		for (int i = 0; i < totalNumOfTransactions; i++) {
			// identify whether the item exists in that transaction, true or false.
			boolListIsItemExist = identifyIsItemExist(datFileCandidates.get(i), boolListIsItemExist);

			// check each candidate
			for (int j = 0; j < listOfCurrentItemSets.size(); j++) {
				transactionIsMatchedWithCandidate = true; // reset match to true

				// tokenize the candidate so that we know what items need to be
				// present for a match
				int[] candidate = listOfCurrentItemSets.get(j);

				// check each item in the itemSet to see if it is present in the
				// transaction

				// if it is not a match, filter the candidate
				for (int item : candidate) {
					if (boolListIsItemExist[item] == false) {
						transactionIsMatchedWithCandidate = false;
						break;
					}
				}
				// if it is a match, increase the count
				if (transactionIsMatchedWithCandidate) {
					count[j]++;
				}
			}

		}
		/*
			Second part: see if the living candidates could satisfy the determined
			minimum support. (default: 60%)
		 */
		for (int i = 0; i < listOfCurrentItemSets.size(); i++) {
			// if the count% is larger than the minSup%, add to the candidate to
			// the frequent candidates
			if ((count[i] / (double) (totalNumOfTransactions)) >= minimumSupport) {
				foundFrequentItemSet(listOfCurrentItemSets.get(i), count[i]);
				frequentCandidates.add(listOfCurrentItemSets.get(i));
			}
		}

		// new candidates are only the frequent candidates
		// that has passed the filtration/pruning process.
		listOfCurrentItemSets = frequentCandidates;
	}

	public void calculateMinimumConfidence() {
		int b = frequentItemSetsList.size();
		if (b == 0) {
			System.exit(0);
		}
		int i, j, k = 0, m = 0;
		String newb = frequentItemSetsList.get(b - 1);
		int a = ((newb.substring(1, newb.length() - 1).split(", ")).length);
		int[][] lol = new int[b][a - 1];
		int[] lols = new int[b];
		for (i = 0; i < b; i++) {
			newb = frequentItemSetsList.get(i);
			String[] poop = newb.substring(1, newb.length() - 1).split(", ");
			for (j = 0; j < poop.length - 1; j++) {
				lol[i][j] = Integer.parseInt(poop[j]);
			}
			lols[i] = Integer.parseInt(poop[j]);
			if ((j + 1) == a && k == 0) {
				k = i;
			}
			poop = null;
		}
		System.out.println("\nAssociation Rules: When Minimum Confidence=" + minimumConfidence * 100 + "%");
		for (i = k; i < b; i++) {
			for (j = 0; j < k; j++) {
				m += assoc_print(lol[i], lol[j], lols[i], lols[j]);
			}
		}
		if (m == 0) {
			System.out.println("No association rules passed the minimum confidence of " + minimumConfidence * 100 + "%");
		}
	}

	public int assoc_print(int[] a, int[] b, int a1, int b1) {
		String win = "(", lose = "(";
		int i, j, k = 0;
		int[] loss = new int[a.length];
		for (i = 0; i < b.length && b[i] != 0; i++) {
			k = 1;
			win = win + b[i] + ",";
			for (j = 0; j < a.length; j++) {
				if (b[i] == a[j]) {
					k = 0;
					loss[j] = 1;
				}
			}
		}
		win = win.substring(0, win.length() - 1) + ")";
		for (i = 0; i < a.length; i++) {
			if (loss[i] == 0) {
				lose = lose + a[i] + ",";
			}
		}
		lose = lose.substring(0, lose.length() - 1) + ")";
		if (k == 0) {
			double Lol = (double) a1 / b1;
			if (Lol > minimumConfidence) {
				System.out.printf("%s ==> %s :	%.2f%c \n", win, lose, Lol * 100, 37);
				return 1;
			}
		}
		return 0;
	}
	/*
	the modified version will need us to read the file in parallel
	and store it into an array list with integer array format
	which holds a continuous sequence of itemSets
	in order to do a priori process.
	the thread pool type that is used is newFixedThreadPool,
	however the experiment could run in different types of thread pools
	Pool size: 1,2,4,8,16,32
	 */
	public void convertFileToTransactionsList() throws IOException, ExecutionException, InterruptedException {
		FileInputStream fileInputStream = new FileInputStream("mushroom.dat");
		FileChannel channel = fileInputStream.getChannel();
		long remainingSize = channel.size();
		long fileChunkSize = remainingSize / numOfCores;

		// create a thread pool based on the number of cores
		ExecutorService executor = Executors.newFixedThreadPool(numOfCores);

		// indicates the file pointer
		long startLoc = 0;
		List<Future<String>> returnedFutureList = new ArrayList<>();
		System.out.println("1. this section has been run!");
		while(remainingSize >= fileChunkSize) {
			// distribute task to a new thread
			ReadFileAsync processFileChunk = new ReadFileAsync(channel,startLoc,toIntExact(fileChunkSize),0);
			Future<String> returnedFuture = executor.submit(processFileChunk);
			returnedFutureList.add(returnedFuture);
			remainingSize = startLoc - fileChunkSize;
			startLoc = startLoc + fileChunkSize;
		}
		System.out.println("2. this section has been run!");
		// remaining file chunk
		Future <String> returnedFuture = executor.submit(new ReadFileAsync(channel, startLoc,toIntExact(remainingSize),0));
		returnedFutureList.add(returnedFuture);
//		// executor will wait all threads to shut down
//		executor.shutdown();
		// wait for all threads to finish their tasks
		System.out.println("3. this section has been run!");
		while(!executor.isTerminated()) {

		}
		System.out.println("4. this section has been run!");
		fileInputStream.close();
		StringBuilder processedString = new StringBuilder();
		for(Future<String> futureString : returnedFutureList) {
			System.out.println(futureString.get());
			Thread.sleep(1000);
		}
	}
}
