package com.studies.classifiers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.classifiers.lazy.IBk;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class KNearest {
	 public static BufferedReader readDataFile(String filename) {
			BufferedReader inputReader = null;
	 
			try {
				inputReader = new BufferedReader(new FileReader(filename));
			} catch (FileNotFoundException ex) {
				System.err.println("File not found: " + filename);
			}
	 
			return inputReader;
		}
	 
		public static double execute(Integer mistakes, Integer length, Integer user) throws Exception {
			BufferedReader datafile = readDataFile("C:\\Temp\\doubleChecking.txt");
	 
			Instances data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
	 
			//do not use first and second
			Instance inst = new DenseInstance(3); 
			inst.setValue(0, mistakes);
			inst.setValue(1, length);
			inst.setValue(2, user);
			inst.setDataset(data);
	 
			IBk ibk = new IBk();		
			ibk.buildClassifier(data);
	 
			double class1 = ibk.classifyInstance(inst);
			System.out.println("belongs to: " + class1);
			return class1;
	 
			
		}
}
