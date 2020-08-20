/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RandomNumberGenerator;

import java.util.*;

/**
 *
 * @author renansantos
 */
public class UniformRandomGenerator {

    private int numberOfObjectives;
    private int populationSize;
    double[][] lambda;
    private List<Double> listOfRandomNumbers;

    public UniformRandomGenerator(int numberOfObjectives, int populationSize) {
        this.numberOfObjectives = numberOfObjectives;
        this.populationSize = populationSize;
        this.listOfRandomNumbers = new ArrayList<>();
    }

    public List<Double> generateUniformRandomNumbers() {
        Random r = new Random();
        listOfRandomNumbers.clear();

        for (int i = 0; i < this.numberOfObjectives; i++) {
            listOfRandomNumbers.add(r.nextDouble());
        }

        List<Double> newListOfRandomNumbers = new ArrayList<>();
        double totalSum = listOfRandomNumbers.stream().mapToDouble(Double::doubleValue).sum();

        for (double number : listOfRandomNumbers) {
            newListOfRandomNumbers.add(number / totalSum);
        }

        listOfRandomNumbers.clear();
        listOfRandomNumbers.addAll(newListOfRandomNumbers);

        return newListOfRandomNumbers;
    }
    
    public List<Double> generateUniformRandomNumbers(int seed) {
        Random r = new Random(seed);
        listOfRandomNumbers.clear();

        for (int i = 0; i < this.numberOfObjectives; i++) {
            listOfRandomNumbers.add(r.nextDouble());
        }

        List<Double> newListOfRandomNumbers = new ArrayList<>();
        double totalSum = listOfRandomNumbers.stream().mapToDouble(Double::doubleValue).sum();

        for (double number : listOfRandomNumbers) {
            newListOfRandomNumbers.add(number / totalSum);
        }

        listOfRandomNumbers.clear();
        listOfRandomNumbers.addAll(newListOfRandomNumbers);

        return newListOfRandomNumbers;
    }

    public double[][] generateUniformRandomNumbersInMatrix() {
        int i = 0;

        lambda = new double[populationSize][numberOfObjectives];
        while (i < this.populationSize) {
            if (i < this.numberOfObjectives) {
                List<Double> listOfRandomNumbers = generateIdentityMatrix(i, this.numberOfObjectives);

                for (int j = 0; j < listOfRandomNumbers.size(); j++) {
                    lambda[i][j] = listOfRandomNumbers.get(j);
                }
            } else if (i >= this.numberOfObjectives) {
                List<Double> listOfRandomNumbers = generateUniformRandomNumbers();

                for (int j = 0; j < listOfRandomNumbers.size(); j++) {
                    lambda[i][j] = listOfRandomNumbers.get(j);
                }
            }
            i++;
        }
        return lambda;
    }
    
    public double[][] generateUniformRandomNumbersInMatrix(int seed) {
        int i = 0;

        lambda = new double[populationSize][numberOfObjectives];
        while (i < this.populationSize) {
            if (i < this.numberOfObjectives) {
                List<Double> listOfRandomNumbers = generateIdentityMatrix(i, this.numberOfObjectives);

                for (int j = 0; j < listOfRandomNumbers.size(); j++) {
                    lambda[i][j] = listOfRandomNumbers.get(j);
                }
            } else if (i >= this.numberOfObjectives) {
                List<Double> listOfRandomNumbers = generateUniformRandomNumbers(seed);

                for (int j = 0; j < listOfRandomNumbers.size(); j++) {
                    lambda[i][j] = listOfRandomNumbers.get(j);
                }
            }
            i++;
        }
        return lambda;
    }

    private List<Double> generateIdentityMatrix(int solutionNumber, int numberOfObjectives) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < numberOfObjectives; i++) {
            if (i == solutionNumber) {
                list.add(1.0);
            } else {
                list.add(0.0);
            }
        }
        return list;
    }
}
