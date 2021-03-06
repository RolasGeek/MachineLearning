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
 private String lastGuess;
 public static Teacher getInstance() {
	 if(instance == null) {
		 instance = new Teacher();
		 instance.execute();
		 }
	 return instance;
 }
 
 public void execute() {
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
		lastGuess = owner;
		return owner;
	}
 //Paskaičiota blogai, sumazinti zodziu koeficiantus
public void learn(String text, Integer method) {
	Double koef = 0.0;
	if(method == 0) {
		koef = 0.9;
	} else {
		koef = 1.05;
	}
	HashMap<String, Object> data = classifier.getValues();
	String words[] = text.split("[^a-zA-Z0-9\'“”’\"$]");
	for (String string : words) {
		//Patikslinti koeficiantus
		DataClass obj = (DataClass) data.get(string);
		Integer index = obj.getByName(lastGuess); //Blogai atspetas
		User user = obj.getTypes().get(index);
		System.out.println("Zodžio -" + string + "koef: " + user.getKoef());
		user.setKoef(user.getKoef()*koef);
		System.out.println("naujas: " + user.getKoef());
		obj.getTypes().set(index, user);
		data.replace(string, obj);
	}
	classifier.setValues(data);
}

 
}
