package com.rts.week10.ConcurrentVersion;

import com.rts.week10.SharedClasses.DistanceMeasurer;
import com.rts.week10.SharedClasses.DocumentCluster;
import com.rts.week10.SharedClasses.Documents;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class AssignmentTask extends RecursiveAction {

    private DocumentCluster[] clusters;

    private Documents[] documents;

    private int start, end;

    private AtomicInteger numChanges;

    private int maxSize;

    public AssignmentTask(DocumentCluster[] clusters, Documents[] documents,
                          int start, int end, AtomicInteger numChanges, int maxSize) {
        this.clusters = clusters;
        this.documents = documents;
        this.start = start;
        this.end = end;
        this.numChanges = numChanges;
        this.maxSize = maxSize;
    }

    @Override
    protected void compute() {
        if (end - start <= maxSize) {
            for (int i = start; i < end; i++) {
                Documents document = documents[i];
                double distance = Double.MAX_VALUE;
                DocumentCluster selectedCluster = null;
                for (DocumentCluster cluster : clusters) {
                    double curDistance = DistanceMeasurer.euclideanDistance(
                            document.getData(), cluster.getCentroid());
                    if (curDistance < distance) {
                        distance = curDistance;
                        selectedCluster = cluster;
                    }
                }
                selectedCluster.addDocument(document);
                boolean result = document.setCluster(selectedCluster);
                if (result) {
                    numChanges.incrementAndGet();
                }

            }
        } else {
            int mid = (start + end)/2;
            AssignmentTask task1 = new AssignmentTask(clusters, documents, start, mid, numChanges,maxSize);
            AssignmentTask task2 = new AssignmentTask(clusters, documents, mid, end, numChanges,maxSize);
            invokeAll(task1, task2);
        }
    }

    public DocumentCluster[] getClusters() {
        return clusters;
    }

    public Documents[] getDocuments() {
        return documents;
    }

    public void setClusters(DocumentCluster[] clusters) {
        this.clusters = clusters;
    }

    public void setDocuments(Documents[] documents) {
        this.documents = documents;
    }
}
