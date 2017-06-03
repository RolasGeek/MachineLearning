package com.studies.classifiers;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import java.io.File;
import java.util.*;


public class Neuron {

    private LinkedHashMap<String, ArrayList<Double>> usersMap = new LinkedHashMap<>();

    public Neuron() {
    }

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

    public void createNeuron(File f) {

        ArrayList<ArrayList<Double>> data = readFromFile(f);

        double[][] array = convertToDoubleArray(data);
        double[][] ideal = new double[array.length][1];
        for (int i = 0; i < array.length; i++){
            ideal[i][0] = i;
        }

        network(array, ideal);
    }

    private ArrayList<ArrayList<Double>> readFromFile(File file) {
        usersMap = new LinkedHashMap<>();

        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();

        try {
            Scanner sc = new Scanner(file);
            sc.useLocale(Locale.US);

            while (sc.hasNext()){
                String s = sc.next();
                double i = sc.nextDouble();
                if (usersMap.containsKey(s)){
                    ArrayList<Double> mas = usersMap.get(s);
                    mas.add(i);
                    usersMap.put(s, mas);
                }
                else{
                    ArrayList<Double> mas = new ArrayList<>();
                    mas.add(i);
                    usersMap.put(s, mas);
                }
            }
            sc.close();
            for (ArrayList<Double> d : usersMap.values()){
                matrix.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public void network(double[][] data, double[][] ideal) {
        NeuralDataSet trainingSet = new BasicNeuralDataSet(data, ideal);
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(new ActivationLinear(), true, 2));
        network.addLayer(new BasicLayer(new ActivationLinear(), true, 4));
        network.addLayer(new BasicLayer(new ActivationLinear(), true, 1));
        network.getStructure().finalizeStructure();
        network.reset();

        final Train train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;
        double oldEpochError = Double.MAX_VALUE;
        int oldErrorEpoch = 0;
        do {
            train.iteration();
            if(oldEpochError + 1000 <= epoch) {
                break;
            }
            if(train.getError() - oldEpochError <= 1) {
                oldEpochError = train.getError();
                oldErrorEpoch = epoch;
            }
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while(train.getError() > 0.01);

        System.out.println("Neural Network Results:");

        for(MLDataPair pair: trainingSet ) {
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) +
                    "," + pair.getInput().getData(1) +
                    ", actual=" + output.getData(0) +
                    ",ideal=" + pair.getIdeal().getData(0));
        }
    }

    public ArrayList<Double> getMapValueByIndex(int index){
        return usersMap.get((usersMap.keySet().toArray())[index]);
    }

    public String getMapKeyByIndex(int index){
        return (String) usersMap.keySet().toArray()[index];
    }
}
