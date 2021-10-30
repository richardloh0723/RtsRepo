/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.SharedClasses;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author emran
 */

public class DocumentCluster {

	private double centroid[];

	private Collection<Documents> documents;

	public DocumentCluster(int size, Collection<Documents> documents) {
		this.documents = documents;
		centroid = new double[size];
	}

	public void addDocument(Documents document) {
		documents.add(document);
	}

	public void clearClusters() {
		documents.clear();
	}

	public void calculateCentroid() {

		Arrays.fill(centroid, 0);

		for (Documents document : documents) {
			Word vector[] = document.getData();

			for (Word word : vector) {
				centroid[word.getIndex()] += word.getTfidf();
			}
		}

		for (int i = 0; i < centroid.length; i++) {
			centroid[i] /= documents.size();
		}
	}

	public double[] getCentroid() {
		return centroid;
	}

	public Collection<Documents> getDocuments() {
		return documents;
	}

	public void initialize(Random random) {
		for (int i = 0; i < centroid.length; i++) {
			centroid[i] = random.nextDouble();
		}
	}

	public int getDocumentCount() {
		return documents.size();
	}

}

