/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week10.SharedClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author emran
 */
public class VocabularyLoader {
	
	public static Map<String, Integer> load (Path path) throws IOException {
		int index=0;
		HashMap<String, Integer> vocIndex=new HashMap<String, Integer>();
		try(BufferedReader reader = Files.newBufferedReader(path)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				vocIndex.put(line,index );
				index++;
			}
		} 
		
		return vocIndex;

	}

}
