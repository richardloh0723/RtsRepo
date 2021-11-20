package com.assignment.concurrent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

public class ApAlgorithm {
	private List<int[]> listOfCurrentItemSets;
	private String transactionFileName;
	private int totalNumOfDistinctItems;
	private int totalNumOfTransactions;
	private double minimumSupport;
	private double minimumConfidence;
	private List<String> frequentItemSetsList = new ArrayList<String>();

	/**
	 * generates the apriori itemsets from a file
	 * @throws Exception
	 */
	public ApAlgorithm() throws Exception {
		// configuring the settings
		configure();
		go();
	}

	private void go() throws Exception {
		// initialize timer
		long start = System.currentTimeMillis();

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
			if (listOfCurrentItemSets.size() != 0) {
				frequentSetsTotal += listOfCurrentItemSets.size();
				log("Found " + listOfCurrentItemSets.size() + " frequent itemsets of size " + itemSetsTotal + " (with support "
						+ (minimumSupport * 100) + "%)");
				createNewItemsetsFromPreviousOnes();
			}
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
		transactionFileName = "mushroom.dat";
		minimumSupport = 0.6;
		minimumConfidence = .8;
		// initialize total number of distinct items and
		totalNumOfDistinctItems = 0;
		// total number of transactions
		totalNumOfTransactions = 0;
		BufferedReader data_in = new BufferedReader(new FileReader(transactionFileName));
		while (data_in.ready()) {
			String line = data_in.readLine();
			if (line.matches("\\s*"))
				continue; // ignore when its empty lines
			totalNumOfTransactions++;
			StringTokenizer t = new StringTokenizer(line, " ");
			while (t.hasMoreTokens()) {
				int x = Integer.parseInt(t.nextToken());
				if (x + 1 > totalNumOfDistinctItems)
					totalNumOfDistinctItems = x + 1;
			}
		}

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

	/** put "true" in trans[i] if the integer i is in line */
	private void line2booleanArray(String line, boolean[] trans) {
		Arrays.fill(trans, false);
		StringTokenizer stFile = new StringTokenizer(line, " "); // read a line from the file to the tokenizer
		// put the contents of that line into the transaction array
		while (stFile.hasMoreTokens()) {

			int parsedVal = Integer.parseInt(stFile.nextToken());
			trans[parsedVal] = true; // if it is not a 0, assign the value to true
		}
	}

	/**
	 * passes through the data to measure the frequency of sets in listOfCurrentItemSets
	 * then filters thoses who are under the minimum support (minSup)
	 */
	private void calculateFrequentItemsets() throws Exception {

		log("Passing through the data to compute the frequency of " + listOfCurrentItemSets.size() + " itemsets of size "
				+ listOfCurrentItemSets.get(0).length);

		List<int[]> frequentCandidates = new ArrayList<int[]>(); // the frequent candidates for the current itemset

		boolean match; // whether the transaction has all the items in an itemset
		int count[] = new int[listOfCurrentItemSets.size()]; // the number of successful matches, initialized by zeros

		// load the transaction file
		BufferedReader data_in = new BufferedReader(new InputStreamReader(new FileInputStream(transactionFileName)));

		boolean[] trans = new boolean[totalNumOfDistinctItems];

		// for each transaction
		for (int i = 0; i < totalNumOfTransactions; i++) {

			// boolean[] trans = extractEncoding1(data_in.readLine());
			String line = data_in.readLine();
			line2booleanArray(line, trans);

			// check each candidate
			for (int c = 0; c < listOfCurrentItemSets.size(); c++) {
				match = true; // reset match to false
				// tokenize the candidate so that we know what items need to be
				// present for a match
				int[] cand = listOfCurrentItemSets.get(c);
				// int[] cand = candidatesOptimized[c];
				// check each item in the itemset to see if it is present in the
				// transaction
				for (int xx : cand) {
					if (trans[xx] == false) {
						match = false;
						break;
					}
				}
				if (match) { // if at this point it is a match, increase the count
					count[c]++;
					// log(Arrays.toString(cand)+" is contained in trans "+i+" ("+line+")");
				}
			}

		}

		data_in.close();

		for (int i = 0; i < listOfCurrentItemSets.size(); i++) {
			// if the count% is larger than the minSup%, add to the candidate to
			// the frequent candidates
			if ((count[i] / (double) (totalNumOfTransactions)) >= minimumSupport) {
				foundFrequentItemSet(listOfCurrentItemSets.get(i), count[i]);
				frequentCandidates.add(listOfCurrentItemSets.get(i));
			}
			// else log("-- Remove candidate: "+ Arrays.toString(candidates.get(i)) + " is:
			// "+ ((count[i] / (double) numTransactions)));
		}

		// new candidates are only the frequent candidates
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
}
