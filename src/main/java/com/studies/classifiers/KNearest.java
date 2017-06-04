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
	 
		public static void execute() throws Exception {
			BufferedReader datafile = readDataFile("C:\\Temp\\doubleChecking.txt");
	 
			Instances data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
	 
			//do not use first and second
			Instance first = data.instance(0);
			Instance second = data.instance(1);
			Instance inst = new DenseInstance(3); 
			inst.setValue(0, 2);
			inst.setValue(1, 24);
			inst.setValue(2, 0);
			inst.setDataset(data);
			
			data.delete(0);
			data.delete(1);
	 
			IBk ibk = new IBk();		
			ibk.buildClassifier(data);
	 
			double class1 = ibk.classifyInstance(first);
			double class2 = ibk.classifyInstance(inst);
			
	 
			System.out.println("first: " + class1 + "\nsecond: " + class2);
		}
}
