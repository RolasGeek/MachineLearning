package com.studies.classifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/** šūdas*/
public class Neuron
{

    private File file = new File("spellChecking.txt");

    public Neuron() {
    }

    public  ArrayList<ArrayList<Double>> readFromFile(File file) {

        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> mas1 = new ArrayList<>();
        ArrayList<Double> mas2 = new ArrayList<>();
        ArrayList<Double> mas3 = new ArrayList<>();
        ArrayList<Double> mas4 = new ArrayList<>();

        try {

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String s = sc.next();
                double i = sc.nextDouble();
                if (s.equals("Laima")) {
                    mas1.add(i);
                    matrix.add(0,mas1);
                } else if (s.equals("Eivydas")) {
                    mas2.add(i);
                    matrix.add(1,mas2);
                } else if (s.equals("Rolandas")) {
                    mas3.add(i);
                    matrix.add(2,mas3);
                } else if (s.equals("Jonas")) {
                    mas4.add(i);
                    matrix.add(3,mas4);
                }
                System.out.println(i);

            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return matrix;
    }



}
