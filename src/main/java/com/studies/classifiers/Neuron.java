package com.studies.classifiers;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.quick.QuickPropagation;

import java.io.File;
import java.util.*;


public class Neuron {
	
    public Neuron() {
    	
    }

    // unused
    private double[][] convertToDoubleArray(ArrayList<ArrayList<Double>> arrayList) {

        Double[][] array = new Double[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<Double> row = arrayList.get(i);
            array[i] = row.toArray(new Double[row.size()]);
        }

        double[][] d = new double[arrayList.size()][];

        for (int i = 0; i < arrayList.size(); i++) {
            double[] temp = new double[arrayList.get(i).size()];
            for (int j = 0; j < arrayList.get(i).size(); j++) {
                temp[j] = arrayList.get(i).get(j);
            }
            d[i] = temp;
        }

        return d;
    }

	public BasicNetwork createNeuron(File f) {
        Map<String, ArrayList<Double>> data = readFromFile(f);

		double[][] array = new double[countValuesInMap(data)][1];
        double[][] ideal = new double[array.length][1];
        int i = 0, j = 0;
		for(String s : data.keySet()) {
            ArrayList<Double> als = data.get(s);
            for(Double d : als) {
            	array[j][0] = d;
            	ideal[j][0] = i;
            	j++;
            }
            i++;
        }
        
        return network(array, ideal);
    }

    private Map<String, ArrayList<Double>> readFromFile(File file) {
        Map<String, ArrayList<Double>> koefMap = new LinkedHashMap<>();

        try {
            Scanner sc = new Scanner(file);
            sc.useLocale(Locale.US);

            while (sc.hasNext()){
                String s = sc.next();
                double i = sc.nextDouble();
                if (koefMap.containsKey(s)){
                    ArrayList<Double> mas = koefMap.get(s);
                    mas.add(i);
                    koefMap.put(s, mas);
                }
                else{
                    ArrayList<Double> mas = new ArrayList<>();
                    mas.add(i);
                    koefMap.put(s, mas);
                }
            }
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return koefMap;
    }

    public BasicNetwork network(double[][] data, double[][] ideal) {
    	for(int i = 0; i < data.length; i++) {
    		for(int j = 0; j < data[i].length; j++) {
    		//	System.out.format("data: %.3f | ideal: %.3f\n", data[i][j], ideal[i][j]);
    		}
    	}
    	
        MLDataSet trainingSet = new BasicNeuralDataSet(data, ideal);
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
        network.addLayer(new BasicLayer(new ActivationLinear(), true, 4));
        network.addLayer(new BasicLayer(new ActivationLinear(), true, 1));
        network.getStructure().finalizeStructure();
        network.reset();
    	
        final Train train = new QuickPropagation(network, trainingSet);

        StopTrainingStrategy stop = new StopTrainingStrategy();
        train.addStrategy(new Greedy());
        train.addStrategy(stop);
        
        int epoch = 0;
        while (!stop.shouldStop()) {
        	train.iteration();
			//System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		}
        System.out.println("Error: "+train.getError());
        
        System.out.println("epochs: "+epoch);
        System.out.println("Neural Network Results:");

        for(MLDataPair pair: trainingSet ) {
            final MLData output = network.compute(pair.getInput());
            System.out.print("data=");
            for(double d : pair.getInput().getData()) {
            	System.out.format("%.3f, ", d);
            }
            System.out.print("actual=");
            for(double d : output.getData()) {
            	System.out.format("%.3f, ", d);
            }
            System.out.print("ideal=");
            for(double d : pair.getIdeal().getData()) {
            	System.out.format("%.3f, ", d);
            }
            System.out.println("");
        }

        /*
        Just for outs
         */
        Map<String, ArrayList<Double>> map = readFromFile(new File("C:/Temp/spellChecking.txt"));
        int i = 0;
        System.out.println("NEURON name list:");
        for (String s : map.keySet()){
            System.out.println("UserName: " + s + " index: " + i);
            i++;
        }

        return network;
    }
    
    private int countValuesInMap(Map<String, ArrayList<Double>> data) {
    	int num = 0;
    	for(String s : data.keySet()) {
            ArrayList<Double> als = data.get(s);
            for(Double d : als) {
            	num++;
            }
        }
    	return num;
    }
    
    /*
    public ArrayList<Double> getMapValueByIndex(int index){
        return usersMap.get((usersMap.keySet().toArray())[index]);
    }

    public String getMapKeyByIndex(int index){
        return (String) usersMap.keySet().toArray()[index];
    }*/
}
