/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import static Algorithms.Algorithms.greedyConstructive;
import static Algorithms.Algorithms.perturbation;
import static Algorithms.Methods.readProblemUsingExcelData;
import ProblemRepresentation.Request;
import ProblemRepresentation.ProblemSolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class RandomSolutionGenerator {

    private String vehicleCapacities[] = {"4"};//"11""16""13" -> removed
    private String nodesDistance[] = {"s"};//m l
    private String numberOfRequests[] = {"250"};//{"050","100","150","200","250"};
    private String timeWindows[] = {"10"};//05 "03"-> removed
    private String numberOfNodes = "12";
    private int numberOfInstances;
    private int numberOfSolutionsPerInstance;
    private int idealNumberOfSolutions = 10000;

    public void generateSolutions(int reducedDimension, String filePath, List<Double> parameters) throws FileNotFoundException {
        this.numberOfInstances = this.vehicleCapacities.length * this.nodesDistance.length
                * this.numberOfRequests.length * this.timeWindows.length;
        this.numberOfSolutionsPerInstance = idealNumberOfSolutions / this.numberOfInstances + 1;

        String folder = "RandomSolutions";
        boolean success = (new File(folder)).mkdirs();
        String destinationFileForObjectives = folder + "/Random_Solutions_Objectives_"+idealNumberOfSolutions+".txt";
        String destinationFileForSolutions = folder + "/Random_Solutions_Solutions_"+idealNumberOfSolutions+".txt";

        PrintStream printStreamForObjectives = new PrintStream(destinationFileForObjectives);
        PrintStream printStreamForSolutions = new PrintStream(destinationFileForSolutions);

        System.out.println("Number of solutions per instance = " + this.numberOfSolutionsPerInstance);

        for (int i = 0; i < vehicleCapacities.length; i++) {
            for (int j = 0; j < nodesDistance.length; j++) {
                for (int k = 0; k < numberOfRequests.length; k++) {
                    for (int l = 0; l < timeWindows.length; l++) {
                        String requestsInstance = "r" + numberOfRequests[k] + "n" + numberOfNodes + "tw" + timeWindows[l];
                        int vehicleCapacityForInstance = Integer.valueOf(vehicleCapacities[i]);
                        String nodesInstance = "bh_n" + numberOfNodes + nodesDistance[j];
                        String adjacenciesInstance = "bh_adj_n" + numberOfNodes + nodesDistance[j];

                        System.out.println("Instance configuration = " + requestsInstance + "-" + vehicleCapacityForInstance
                                + "-" + nodesInstance + "-" + adjacenciesInstance);
                        generateSolution(reducedDimension, filePath, parameters, requestsInstance, vehicleCapacityForInstance, nodesInstance, adjacenciesInstance,
                                printStreamForObjectives, printStreamForSolutions);

                    }
                }
            }
        }
    }

    private void generateSolution(int reducedDimension, String dataFilePath, List<Double> parameters, String requestsInstance, int vehicleCapacityForInstance, String nodesInstance,
            String adjacenciesInstance, PrintStream printStreamForObjectives, PrintStream printStreamForSolutions)
            throws FileNotFoundException {
        final Long timeWindows = (long) 3;
        List<Request> listOfRequests = new ArrayList<>();
        List<List<Integer>> listOfAdjacencies = new LinkedList<>();
        List<List<Long>> distanceBetweenNodes = new LinkedList<>();
        List<List<Long>> timeBetweenNodes = new LinkedList<>();
        Set<Integer> Pmais = new HashSet<>();
        Set<Integer> Pmenos = new HashSet<>();
        Set<Integer> setOfNodes = new HashSet<>();
        int numberOfNodes = 0;
        Map<Integer, List<Request>> requestsWichBoardsInNode = new HashMap<>();
        Map<Integer, List<Request>> requestsWichLeavesInNode = new HashMap<>();
        List<Integer> loadIndexList = new LinkedList<>();
        Set<Integer> setOfVehicles = new HashSet<>();
        List<Request> listOfNonAttendedRequests = new ArrayList<>();
        List<Request> requestList = new ArrayList<>();
        Long currentTime = (long) 0;
        Integer lastNode = 0;

        String instanceName = requestsInstance;
        String nodesData = nodesInstance;
        String adjacenciesData = adjacenciesInstance;
        final Integer numberOfVehicles = 250;
        final Integer vehicleCapacity = vehicleCapacityForInstance;
        String filePath = dataFilePath;
//        numberOfNodes = readProblemData(instanceName, nodesData, adjacenciesData, listOfRequests, distanceBetweenNodes,
//                timeBetweenNodes, Pmais, Pmenos, requestsWichBoardsInNode, requestsWichLeavesInNode, setOfNodes,
//                numberOfNodes, loadIndexList);
        try {
            numberOfNodes = readProblemUsingExcelData(filePath, instanceName, nodesData, adjacenciesData, requestList, distanceBetweenNodes,
                    timeBetweenNodes, Pmais, Pmenos, requestsWichBoardsInNode, requestsWichLeavesInNode, setOfNodes,
                    numberOfNodes, loadIndexList);
        } catch (IOException ex) {
            Logger.getLogger(RandomSolutionGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(RandomSolutionGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        Methods.initializeFleetOfVehicles(setOfVehicles, numberOfVehicles);

//        ProblemSolution solution = greedyConstructive(0.2, 0.15, 0.55, 0.1,reducedDimension, requestList, requestsWichBoardsInNode,
//                requestsWichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, listOfRequests,
//                loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

        ProblemSolution solution1 = new ProblemSolution();

        for (int i = 0; i < numberOfSolutionsPerInstance; i++) {//numberOfSolutionsPerInstance
//            solution1.setSolution(perturbation(reducedDimension, parameters, solution,requestList , requestsWichBoardsInNode, requestsWichLeavesInNode,
//                    numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, listOfRequests, loadIndexList, timeBetweenNodes,
//                    distanceBetweenNodes, timeWindows));
//            //System.out.println(solution1);
//            printStreamForObjectives.print(solution1.getStringWithOriginalObjectivesForCsvFile() + "\n");
//            printStreamForSolutions.print(solution1 + "\n");
        }
    }
}
