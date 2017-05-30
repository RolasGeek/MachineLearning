package com.studies.prognoze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.Data;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import com.studies.SpellingChecker.LanguageToolInstance;
import com.studies.classifiers.Classifier;
import com.studies.classifiers.DataClass;
import com.studies.classifiers.User;
import com.studies.classifiers.prepareClassifiers;
import com.sun.jna.IntegerType;

public class Prognoze {
	private static Prognoze instance;
	private Classifier classifier1 = null; // Zodziu pasikartojimo
	private Classifier classifier2 = null; // Spell checkinimo
	private Classifier classifier3 = null; // Word length
	public static Prognoze getInstance() {
		if(instance == null) {
			instance = new Prognoze();
			instance.executeClassifiers();
		}
		return instance;
	}
	public void executeClassifiers() {
		System.out.println("Skaiciuojamos tikimybes");
		classifier1 = prepareClassifiers.prepareWordCount();
		classifier2 = prepareClassifiers.prepareSpellingCheck();
		classifier3 = prepareClassifiers.prepareMessageLenght();
		System.out.println("Baigta");
	}
	
	public List<User> Calculate(String text) {
		
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
		//Sudedami koef balsavimui
		List<User> resultdata = new ArrayList<>();
		for (String key : result.keySet()) {
			Double value = (Double) result.get(key);
			Double db = Math.abs(value/biggest);
			resultdata.add(new User(key, db , 0));
		}
		Collections.sort(resultdata, (o1,o2)-> Double.compare(o2.getKoef(), o1.getKoef()));
		return resultdata;
	}
	
	public List<User> Calculate2(String text) {
		
		HashMap<String, Object> result = classifier2.getValues();
		
		//JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
		List<RuleMatch> matches;
		try {
			matches = LanguageToolInstance.getInstance().getLangTool().check(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String words[] = text.split("[^a-zA-Z0-9\'“”’\"$]");
		double koef = matches.size()/(double)words.length;
		
		Double smallest = Double.MAX_VALUE, temp=0.0;
		String owner = "";
		for (String key : result.keySet()) {
			temp = Math.abs((double)result.get(key)-koef);
			System.out.println(key+" - "+result.get(key));
			if(temp < smallest) {
				smallest = temp;
				owner = key;
			}
		}
		//Sudedami koef balsavimui
		List<User> resultdata = new ArrayList<>();
		for (String key : result.keySet()) {
			Double value = (Double) result.get(key);
			Double db = Math.abs(value-(double)result.get(owner));
			resultdata.add(new User(key, 1 - db , 0));
		}
		Collections.sort(resultdata, (o1,o2)-> Double.compare(o2.getKoef(), o1.getKoef()));
		System.out.println("message koef: "+koef+". arciausias koef: "+result.get(owner));
		return resultdata;
	}
	
	public List<User> Calculate3(String text) {
		Integer lenght = text.length() / 10;
		DataClass result = classifier3.getByKey(Integer.toString(lenght));
		if(result != null) {
			List<User> types = result.getTypes();
			for (User u : types) {
				Integer index = types.indexOf(u);
				Double koef = types.get(0).getAmount() != 0.0 ? u.getAmount()/types.get(0).getAmount() : 0.0;
				u.setKoef(koef); //Balsaviko koef
				types.set(index, u);
				System.out.println("Name: " + u.getName() + " Amount: " + u.getAmount());
			}
		return types;
		} else {
			return null;
		}
		
	}
	public String executeAll(String text) {
		List<User> list1 = Calculate(text);
		sortByName(list1);
		List<User> list2 = Calculate2(text);
		sortByName(list2);
		List<User> list3 = Calculate3(text);
		sortByName(list3);
		List<User> result = new DataClass().getTypes();
		int i = 0;
		for (User user : result) {
			user.setKoef(list1.get(i).getKoef()*0.45+list2.get(i).getKoef()*0.45+list3.get(i).getKoef()*0.1);
			result.set(i,user);
			i++;
		}
		Collections.sort(result,(o1, o2)->Double.compare(o2.getKoef(), o1.getKoef()));
		return result.get(0).getName();
	}
	
	public List<User> sortByName(List<User> list) {
		Collections.sort(list, (o1,o2)->o1.getName().compareTo(o2.getName()));
		return list;
	}
 
}
