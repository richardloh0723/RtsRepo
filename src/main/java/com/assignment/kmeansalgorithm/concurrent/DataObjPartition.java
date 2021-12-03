package com.assignment.kmeansalgorithm.concurrent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * the aim of creating this class is to distribute
 * the determined centroids and the data objects that they are
 * required to process. (Iris dataset) using Hash Map.
 * distance calculation algorithm : Euclidean distance algorithm
 */

public class DataObjPartition implements Callable<Map<double[], Integer>> {
    private Map<Integer, double[]> centroids;

    private List<double[]> dataObject;

    private int k;
    /*
    Start index of the features
    End index of the features
     */
    private int startIndex;

    private int endIndex;

    int k1 = 0;

    double dist=0.0;

    @Override
    public Map<double[], Integer> call() throws Exception {
        // instantiate hashmap to put double[] data object as key
        // Integer as its value (cluster that its positioned at)
        Map<double[], Integer> clusters = new HashMap<>();
        // loop through the features based on the start end index
        for(int i = startIndex; i < endIndex; i++) {
            double min = 999999.0;
            for(int j = 0; j < k; j++) {
                dist = Distance.eucledianDistance(centroids.get(j), dataObject.get(i));
                if (dist < min) {
                    min = dist;
                    k1 = j;
                }
                System.out.println("hi: " + Arrays.toString(dataObject.get(i)));
                clusters.put(dataObject.get(i), k1);
            }
        }
        return clusters;
    }

    public Map<Integer, double[]> getCentroids() {
        return centroids;
    }

    public void setCentroids(Map<Integer, double[]> centroids) {
        this.centroids = centroids;
    }

    public List<double[]> getDataObject() {
        return dataObject;
    }

    public void setDataObject(List<double[]> dataObject) {
        this.dataObject = dataObject;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }


}
