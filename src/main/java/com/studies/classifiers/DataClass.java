package com.studies.classifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.studies.model.Messages;
import com.studies.service.MessageService;

public class DataClass {
	private List<User> types;

	public DataClass() {
		List<User> temp = new ArrayList<>();
		List<String> m = MessageService.getInstance().findUniquenames();
		for (String messages : m) {
			temp.add(new User(messages, 0.0 ,0)); 
		}
		this.types = temp;
	}

	public List<User> getTypes() {
		return types;
	}

	public void setTypes(List<User> types) {
		this.types = types;
	}
	
	public Integer getByName(String name) {
		for (User user : types) {
			if(user.getName().equals(name))
			return types.indexOf(user);
		}
		return null;
	}
	
	public double getKoefSum(String name){
		Double sum = 0.0;
		for (User user : types) {
			if(!user.getName().equals(name)){
				sum += user.getKoef();
			}
		}
		return sum;
	}
	
	public void sortByKoef() {
		Collections.sort(types, (o1,o2) -> Double.compare(o2.getKoef(), o1.getKoef()));
	}
	
}
