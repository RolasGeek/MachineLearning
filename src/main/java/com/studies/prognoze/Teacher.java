package com.studies.prognoze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.studies.classifiers.Classifier;
import com.studies.classifiers.DataClass;
import com.studies.classifiers.User;
import com.studies.classifiers.prepareClassifiers;

public class Teacher {
 private Classifier classifier = new Classifier();
 private static Teacher instance;
 private static Teacher getInstance() {
	 if(instance == null) {
		 instance = new Teacher();
		 instance.execute();
		 }
	 return instance;
 }
 
 private void execute() {
	 classifier = prepareClassifiers.prepareWordCount();
 }
 
 public String calculute(String text) {
		
		HashMap<String, Object> data = classifier.getValues();
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
