/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.lecture.week10.forkjoin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author emran
 */
public class SerialMetaData {
    
	public static void main(String[] args) {
		Path path = Paths.get("data", "amazon-meta.txt");

		for (int j = 0; j < 10; j++) {
			AmazonMetaData[] data = AmazonMetaDataLoader.load(path);
			AmazonMetaData[] data2 = data.clone();

			Date start, end;

			start = new Date();
			Arrays.sort(data);
			end = new Date();
			System.out.println("Execution Time Java Arrays.sort(): " + (end.getTime() - start.getTime()));

			System.out.println(data[0].getTitle());
			System.out.println(data2[0].getTitle());
			SerialMergeSort mySorter = new SerialMergeSort();
			start = new Date();
			mySorter.mergeSort(data2, 0, data2.length);
			end = new Date();

			System.out.println("Execution Time Java SerialMergeSort: " + (end.getTime() - start.getTime()));

			for (int i = 0; i < data.length; i++) {
				if (data[i].compareTo(data2[i]) != 0) {
					System.err.println("There's a difference is position " + i);
					System.exit(-1);
				}
			}
			System.out.println("Both arrays are equal");
			System.out.println(data2[0].getTitle() + ": " + data2[0].getSalesrank());
		}

	}
}
