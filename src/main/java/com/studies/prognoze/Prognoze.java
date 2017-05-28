package com.studies.prognoze;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.Data;

import com.studies.classifiers.Classifier;
import com.studies.classifiers.DataClass;
import com.studies.classifiers.User;
import com.studies.classifiers.prepareClassifiers;

public class Prognoze {
	private static Prognoze instance;
	private Classifier classifier1 = null; // Zodziu pasikartojimo
	
	public static Prognoze getInstance() {
		if(instance == null) {
			instance = new Prognoze();
		}
		return instance;
	}
	
	public String Calculate(String text) {
		classifier1 = prepareClassifiers.prepareWordCount();
		HashMap<String, Object> data = classifier1.getValues();
		String words[] = text.split("[^a-zA-Z0-9\'“”’\"$]");
		HashMap<String, Double> result = new HashMap<>();
		List<User> list = new DataClass().getTypes();
		//Result sarasas
		for (User user : list) {
			result.put(user.getName(), 0.0);
		}
		for (String string : words) {
			DataClass obj = (DataClass) data.get(string);
			if(obj != null) {
				List<User> values = obj.getTypes();
				Collections.sort(values, (u1, u2) ->Double.compare(u2.getKoef(), u1.getKoef()));
				Double score = 1.0;
				for (User user : values) {
					if(user.getKoef() > 0) {
						Double d = result.get(user.getName());
						d += score;
						result.replace(user.getName(), d);
					}
					score -= 0.25;
				}
			}
		}
		Double biggest = 0.0;
		String owner = "";
		for (String key : result.keySet()) {
			if(result.get(key) > biggest) {
				biggest = result.get(key);
				owner = key;
			}
		}
		return owner;
	}
 
}
