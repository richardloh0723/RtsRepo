/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.knn.loader;

import java.io.*;
import java.nio.*;
import java.util.*;
import com.rts.knn.data.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *
 * @author emran
 */
public class BankMarketingLoader {

	/**
	 * Method that loads the examples of the Bank Marketing data set from a file
	 * @param dataPath Path to the file where the data items are stored
	 * @return List of BankMarketing examples
	 */
	public List<BankMarketing> load (String dataPath) {
		Path file=Paths.get(dataPath);
		List<BankMarketing> dataSet=new ArrayList<>();
		try (InputStream in = Files.newInputStream(file);
			    BufferedReader reader =
			      new BufferedReader(new InputStreamReader(in))) {
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	String data[]=line.split(";");
			    	BankMarketing dataObject=new BankMarketing();
			    	dataObject.setData(data);
			    	dataSet.add(dataObject);
			    }
			} catch (IOException x) {
			  x.printStackTrace();
			} catch (Exception e) {
			  e.printStackTrace();
			}
		return dataSet;
	}
}
