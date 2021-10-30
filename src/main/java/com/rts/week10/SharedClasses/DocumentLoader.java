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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author emran
 */
public class DocumentLoader {

	public static Documents[] load(Path path, Map<String, Integer> vocIndex) throws IOException {
		List<Documents> list = new ArrayList<Documents>();

		try(BufferedReader reader  = Files.newBufferedReader(path)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Documents item = processItem(line, vocIndex);
				list.add(item);
			}
		} 

		Documents[] ret = new Documents[list.size()];
		return list.toArray(ret);

	}

	private static Documents processItem(String line,
			Map<String, Integer> vocIndex) {

		String[] tokens = line.split(",");
		int size = tokens.length - 1;

		Documents document = new Documents(tokens[0], size);
		Word[] data = document.getData();

		for (int i = 1; i < tokens.length; i++) {
			String[] wordInfo = tokens[i].split(":");
			Word word = new Word();
			word.setIndex(vocIndex.get(wordInfo[0]));
			word.setTfidf(Double.parseDouble(wordInfo[1]));
			data[i - 1] = word;
		}
		Arrays.sort(data);

		return document;
	}
}

