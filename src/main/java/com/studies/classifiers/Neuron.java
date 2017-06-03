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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;


/**
 * šūdas
 */
public class Neuron {

    public Neuron() {
    }

    private double[][] convertToDoubleArray(ArrayList<ArrayList<Double>> arrayList) {

        Double[][] array = new Double[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<Double> row = arrayList.get(i);
            array[i] = row.toArray(new Double[row.size()]);
        }

        double[][] d = new double[arrayList.size()][];

        try {
            for (int i = 0; i < arrayList.size(); i++) {
                double[] temp = new double[arrayList.get(i).size()];
                for (int j = 0; j < arrayList.get(i).size(); j++) {
                    temp[j] = arrayList.get(i).get(j);
                    //d[i][j] = arrayList.get(i).get(j);
                }
                d[i] = temp;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return d;
    }

    public void createNeuron(File f) {

        ArrayList<ArrayList<Double>> data = readFromFile(f);

        double[][] array = convertToDoubleArray(data);
        double[][] ideal = {{0}, {1}, {2}, {3}};
        network(array, ideal);
    }

    private ArrayList<ArrayList<Double>> readFromFile(File file) {


        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> mas1 = new ArrayList<>();
        ArrayList<Double> mas2 = new ArrayList<>();
        ArrayList<Double> mas3 = new ArrayList<>();
        ArrayList<Double> mas4 = new ArrayList<>();

        try {
            Scanner sc = new Scanner(file);
            sc.useLocale(Locale.US);

            while (sc.hasNext()) {
                String s = sc.next();
                double i = sc.nextDouble();
                if (s.equals("Laima")) {
                    mas1.add(i);
                } else if (s.equals("Eivydas")) {
                    mas2.add(i);
                } else if (s.equals("Rolandas")) {
                    mas3.add(i);
                } else if (s.equals("Jonas")) {
                    mas4.add(i);
                }
                System.out.println(i);
            }
            sc.close();
            matrix.add(mas1);
            matrix.add(mas2);
            matrix.add(mas3);
            matrix.add(mas4);
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
}
