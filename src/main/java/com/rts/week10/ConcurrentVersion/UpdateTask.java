package com.rts.week10.ConcurrentVersion;

import com.rts.week10.SharedClasses.DocumentCluster;

import java.util.concurrent.RecursiveAction;

public class UpdateTask extends RecursiveAction {
    private DocumentCluster[] clusters;

    private int start, end;

    private int maxSize;

    public UpdateTask(DocumentCluster[] clusters, int start, int end, int maxSize) {
        this.clusters = clusters;
        this.start = start;
        this.end = end;
        this.maxSize = maxSize;
    }

    @Override
    protected void compute() {
//        for (DocumentCluster cluster : clusters) {
//            cluster.calculateCentroid();
//        } into

        if (end - start <= maxSize) {
            for (int i = start; i < end; i++) {
                DocumentCluster cluster = clusters[i];
                cluster.calculateCentroid();
            }
        } else {
            int mid = (start + end) / 2;
            UpdateTask task1 = new UpdateTask(clusters,start,mid,maxSize);
            UpdateTask task2 = new UpdateTask(clusters,mid,end,maxSize);
            invokeAll(task1, task2);
        }
    }
}
