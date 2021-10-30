/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.lecture.week10.forkjoin;

import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author emran
 */
public class ConcurrentMergeSort {
    

	public void mergeSort (Comparable data[], int start, int end) {
		
		MergeSortTask task=new MergeSortTask(data, start, end,null);
		ForkJoinPool.commonPool().invoke(task);

	}
}
