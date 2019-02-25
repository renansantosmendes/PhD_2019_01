/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import static Algorithms.Algorithms.*;
import static Algorithms.EvolutionaryAlgorithms.*;
import static Algorithms.Methods.*;
import AlgorithmsResults.ResultsGraphicsForParetoCombinedSet;
import ProblemRepresentation.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author renansantos
 */
public class AlgorithmsCalibration {
    private static List<Double> nadirPoint = new ArrayList<>();
    
    public AlgorithmsCalibration(){
        nadirPoint.add(1000000.0);
        nadirPoint.add(1000000.0);
    }

    public static void NSGAII_Calibration(String instanceName, int reducedDimension, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> listOfRequests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {

        //List<Double> parameters = new ArrayList<>();
        double hypervolume = 0;
        Map<Double, List<Double>> hypervolumesMap = new HashMap<>();
        //Start the algorithm here - generating random weigths
        for (int i = 0; i < 2; i++) {
            List<Double> parameters = new ArrayList<>();
            parameters.addAll(new ArrayList<>(generateLambdas()));

            //executing the NSGA-II algorithm
            hypervolume = NSGAII(instanceName, reducedDimension, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                    listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                    listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                    timeWindows, currentTime, lastNode);

            System.out.println("Lambdas = " + parameters);
            hypervolumesMap.put(hypervolume, parameters);
            //parameters.clear();
        }

        System.out.println(hypervolumesMap);
    }

    public static List<Double> generateLambdas() {
        Random rnd = new Random();
        double x, y, z, w, t;
        do {
            x = rnd.nextDouble();
            y = rnd.nextDouble();
            z = rnd.nextDouble();
            w = rnd.nextDouble();
            t = 1 - x - y - z - w;
        } while (x + y + z + w > 1);

        List<Double> parameters = new ArrayList<>();
        parameters.add(x);
        parameters.add(y);
        parameters.add(z);
        parameters.add(w);
        parameters.add(t);

        return parameters;
    }

    public static double vnd(String instanceName, int reducedDimension, List<Double> parameters, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> listOfRequests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {

        double oldHypervolume = NSGAII(instanceName,reducedDimension, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                timeWindows, currentTime, lastNode);
        int k = 0;
        int r = 8;
        List<Integer> neighborhoodList = new ArrayList<>();
        
        for (int i = 0; i < r; i++) {
            neighborhoodList.add(i + 1);
        }
        
        double newHypervolume;
        while (k < r) {
            Collections.shuffle(neighborhoodList);
            //encontrar melhor vizinho s' em N_k(s)
            //se f(s') > f(s) -> s = s' e k = 1
            //entao -> k++
            List<Double> newParameters = new ArrayList<>(parameters);
            System.out.println("Neighborhood = " + neighborhoodList.get(k));
            newHypervolume = firstImprovement(instanceName, reducedDimension, neighborhoodList.get(k), newParameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                    listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                    listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                    timeWindows, currentTime, lastNode);

            if (newHypervolume > oldHypervolume) {
                parameters.clear();
                parameters.addAll(newParameters);
                oldHypervolume = newHypervolume;
                k = 1;
            } else {
                k++;
            }
        }

        //System.out.println("Best Hypervolume = " + oldHypervolume);
        //System.out.println("Best Lambdas = " + parameters);
        return oldHypervolume;
    }

    public static double firstImprovement(String instanceName,  int reducedDimension, int neighborhoodNumber, List<Double> parameters, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> listOfRequests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {
        double delta;
        double hypervolume = 0;
        switch (neighborhoodNumber) {
            case 1:
                delta = 0.01;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 2:
                delta = 0.02;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 3:
                delta = 0.03;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 4:
                delta = 0.1;
                hypervolume = addVariation(instanceName,  reducedDimension,delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 5:
                delta = -0.01;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 6:
                delta = -0.02;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 7:
                delta = -0.03;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            case 8:
                delta = -0.1;
                hypervolume = addVariation(instanceName, reducedDimension, delta, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);
                break;
            default:
                break;
        }
        return hypervolume;
    }

    private static double addVariation(String instanceName, int reducedDimension, double delta, List<Double> parameters, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> listOfRequests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {

        double newHypervolume = 0;
        double oldHypervolume = 0;

        for (int i = 0; i < parameters.size(); i++) {
            for (int j = i + 1; j < parameters.size(); j++) {

                double x = parameters.get(i) + delta;
                double y = parameters.get(j) - delta;

                DecimalFormat formatator = new DecimalFormat("0.0000");

                x = Double.parseDouble(formatator.format(x).replace(",", "."));
                y = Double.parseDouble(formatator.format(y).replace(",", "."));

                List<Double> newParameters = new ArrayList<>(parameters);
                if (x >= 0 && y >= 0) {
                    newParameters.set(i, x);
                    newParameters.set(j, y);
                }

                oldHypervolume = NSGAII(instanceName,reducedDimension, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);

                newHypervolume = NSGAII(instanceName,reducedDimension, newParameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                        listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                        timeWindows, currentTime, lastNode);

                if (newHypervolume > oldHypervolume) {
                    parameters.clear();
                    parameters.addAll(newParameters);
                    return newHypervolume;
                }
            }
        }

        return oldHypervolume;
    }

    public static double perturbation(String instanceName, int reducedDimension, List<Double> parameters, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> listOfRequests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {
        double perturbationDelta = 0.01;
        Random rnd = new Random();
        int expoente = rnd.nextInt(2);
        perturbationDelta = (double) Math.pow(-1, expoente) * perturbationDelta * rnd.nextDouble();
        int i, j;

        do {
            i = rnd.nextInt(parameters.size());
            j = rnd.nextInt(parameters.size());
        } while (i == j);

        double x = parameters.get(i) + perturbationDelta;
        double y = parameters.get(j) - perturbationDelta;

        DecimalFormat formatator = new DecimalFormat("0.0000");

        x = Double.parseDouble(formatator.format(x).replace(",", "."));
        y = Double.parseDouble(formatator.format(y).replace(",", "."));
        if (x >= 0 && y >= 0) {
            parameters.set(i, x);
            parameters.set(j, y);
        }
        double hypervolume = NSGAII(instanceName, reducedDimension, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                timeWindows, currentTime, lastNode);

        return hypervolume;
    }

    public static void ils(String instanceName, int reducedDimension, List<Request> listOfRequests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Set<Integer> setOfVehicles,
            List<Request> listOfNonAttendedRequests, List<Request> requestList, List<Integer> loadIndexList,
            List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes, Long timeWindows, Long currentTime,
            Integer lastNode) throws IOException {

        Integer numberOfVehicles = 50;
        Integer vehicleCapacity = 4;
        Integer populationSize = 100;
        Integer maximumNumberOfGenerations = 50;
        Integer maximumNumberOfExecutions = 1;
        double probabilityOfMutation = 0.02;
        double probabilityOfCrossover = 0.7;

        List<Double> parameters = new ArrayList<>();
        double oldHypervolume, newHypervolume;
        int maxNumberOfIterations = 10, currentIteration = 0;
        parameters.add(0.20);
        parameters.add(0.20);
        parameters.add(0.20);
        parameters.add(0.20);
        parameters.add(0.20);

        System.out.println("***************************************************************");
        System.out.println("*                     Lambdas Calibration                     *");
        System.out.println("***************************************************************");

        oldHypervolume = vnd(instanceName, reducedDimension, parameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                timeWindows, currentTime, lastNode);

        while (currentIteration < maxNumberOfIterations) {
            List<Double> newParameters = new ArrayList<>(parameters);
            newHypervolume = perturbation(instanceName, reducedDimension, newParameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                    listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                    listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                    timeWindows, currentTime, lastNode);

            newHypervolume = vnd(instanceName,reducedDimension, newParameters, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                    listOfRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                    listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                    timeWindows, currentTime, lastNode);

            if (newHypervolume > oldHypervolume) {
                oldHypervolume = newHypervolume;
                parameters.clear();
                parameters.addAll(newParameters);
                System.out.println("New Lambdas increased the hypervolume!!!");
                System.out.println("Hypervolume = " + newHypervolume);
                System.out.println("Lambdas = " + parameters);
            }
            currentIteration++;
        }
        System.out.println("Best Hypervolume = " + oldHypervolume);
        System.out.println("Best Lambdas = " + parameters);
    }

}
