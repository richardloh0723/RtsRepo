/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rts.week8.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emran
 */
public class WordsLoader {

	public static List<String> load(String path) {
		Path file = Paths.get(path);
		List<String> data = new ArrayList<String>();
		try (InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				data.add(line);
			}
		} catch (IOException x) {
			x.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
}