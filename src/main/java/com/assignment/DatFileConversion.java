package com.assignment;

import java.util.List;

public interface DatFileConversion {
    public List<int[]> convertFile() throws Exception;

    public int getTotalNumOfDistinctItems();

    public void setTotalNumOfDistinctItems(int totalNumOfDistinctItems);

    public int getTotalNumOfTransactions();

    public void setTotalNumOfTransactions(int totalNumOfTransactions);
}