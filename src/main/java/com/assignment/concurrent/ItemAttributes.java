package com.assignment.concurrent;

/**
 * A class that stores total number of transactions
 * and total number of distinct items.
 */
public class ItemAttributes {
    private int totalNumOfTransactions;
    private int totalNumOfDistinctItems;

    public int getTotalNumOfTransactions() {
        return totalNumOfTransactions;
    }

    public void setTotalNumOfTransactions(int totalNumOfTransactions) {
        this.totalNumOfTransactions = totalNumOfTransactions;
    }

    public int getTotalNumOfDistinctItems() {
        return totalNumOfDistinctItems;
    }

    public void setTotalNumOfDistinctItems(int totalNumOfDistinctItems) {
        this.totalNumOfDistinctItems = totalNumOfDistinctItems;
    }
}
