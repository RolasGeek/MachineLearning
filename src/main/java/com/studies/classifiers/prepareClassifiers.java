package com.studies.classifiers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.SpellingChecker.SpellCheckResponse;
import com.studies.SpellingChecker.SpellingCheckClass;
import com.studies.model.Messages;
import com.studies.service.MessageService;

public class prepareClassifiers {
	


	public static Classifier prepareSpellingCheck() throws IOException {
		Classifier classifier = new Classifier();
		DataClass users = new DataClass();
		List<Messages> all = MessageService.getInstance().listAll(0, 10000);
		
		for (Messages messages : all) {
			//Klaidų kiekis
			//Neeed spelling checker
			//Istrink ta kur is rest ima jeigu rasi veikianti localu gera
			System.out.println("mess");
		}
		return null;
	}
	private static HashMap<String, Object> countWords() {
		HashMap<String, Object> data = new HashMap<>();
		List<Messages> all = MessageService.getInstance().listAll(0, 10000);
		for (Messages messages : all) {
			String words[] = messages.getMessage().split("[^a-zA-Z0-9\'“”’\"$]");
			for (String string : words) {
				if(string != "") {
				DataClass exist = (DataClass) data.get(string);
				if(exist != null) {
					Integer index = exist.getByName(messages.getName());
					User u = exist.getTypes().get(index);
					u.setAmount(u.getAmount()+1);
					exist.getTypes().set(index, u);
					data.replace(string, exist);
				} else {
					DataClass empty = new DataClass();
					Integer index = empty.getByName(messages.getName());
					empty.getTypes().set(index, new User(messages.getName(), 0.0, 1));
					data.put(string, empty);
				}
				}
			}
		}
		return data;
	}
	
	public static Classifier prepareWordCount() {
		Classifier classifier = new Classifier();
		HashMap<String, Object> data = countWords();
		for (String key : data.keySet()) {
			DataClass value = (DataClass) data.get(key);
			List<User> list = value.getTypes();
			for (User user : list) {
				if(user.getAmount() > 0) {
				Integer index = list.indexOf(user);
				Double d = (double)user.getAmount() / differetWords(user.getName(), data);
				user.setKoef(d);
				list.set(index, user);
				}
			}
			System.out.println("done");
			
		}
		classifier.setValues(data);
		return classifier;
	}
	
	private static Integer differetWords(String name, HashMap<String, Object> data) {
		Integer count = 0;
		for (String key : data.keySet()) {
			DataClass value = (DataClass) data.get(key);
			User u = value.getTypes().get(value.getByName(name));
			if(u.getAmount() > 0) {
				count++;
			}
		}
		return count;
	}
	
	

}
