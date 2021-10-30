/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.SharedClasses;

/**
 *
 * @author emran
 */
public class Documents {

	private Word[] data;
	private String name;
	private DocumentCluster cluster;

	public Documents(String name, int size) {
		this.name = name;
		data = new Word[size];
		cluster = null;
	}

	public Word[] getData() {
		return data;
	}

	public void setData(Word[] data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DocumentCluster getCluster() {
		return cluster;
	}

	public boolean setCluster(DocumentCluster cluster) {
		if (this.cluster == cluster) {
			return false;
		} else {
			this.cluster = cluster;
			return true;
		}
	}

}

