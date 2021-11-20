/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week8.SequentialCode;

import com.rts.week8.Distance.LevenshteinDistance;
import com.rts.week8.Shared.BestMatchingData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author emran
 */
public class BestMatchingSeqCalculation {
    
public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) throws InterruptedException, ExecutionException {
	
    
    List<String> results=new ArrayList<String>();
		int minDistance=Integer.MAX_VALUE;
		int distance;
		for (String str: dictionary) {
			distance=LevenshteinDistance.calculate(word,str);
			if (distance<minDistance) {
				results.clear();
				minDistance=distance;
				results.add(str);
			} else if (distance==minDistance) {
				results.add(str);
			}
		}

		BestMatchingData result=new BestMatchingData();
		result.setWords(results);
		result.setDistance(minDistance);
		return result;
	}

}