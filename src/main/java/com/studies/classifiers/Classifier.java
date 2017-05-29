package com.studies.classifiers;

import java.util.HashMap;
import java.util.List;

public class Classifier {
	private HashMap<String, Object> values;

	public HashMap<String, Object> getValues() {
		return values;
	}

	public void setValues(HashMap<String, Object> values) {
		this.values = values;
	}
	
	public DataClass getByKey(String key) {
		return (DataClass) values.get(key);
	}
}
