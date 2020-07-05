/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRT;

import Algorithms.*;
import Algorithms.Algorithms.*;
import static Algorithms.Algorithms.buildInstaceName;
import java.util.*;
import ProblemRepresentation.*;
import com.google.maps.errors.ApiException;
import Algorithms.EvolutionaryAlgorithms.*;
import static Algorithms.EvolutionaryAlgorithms.MOEAD;
import static Algorithms.EvolutionaryAlgorithms.onMOEAD;
import static Algorithms.Methods.readProblemData;
import static Algorithms.Methods.readProblemUsingExcelData;
import InstanceReader.ScriptGenerator;
import ReductionTechniques.CorrelationType;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class VRPDRT_Main {

    static final Long timeWindows = (long) 3;
    static List<Request> requests = new ArrayList<>();
    static List<List<Integer>> listOfAdjacencies = new LinkedList<>();
    static List<List<Long>> distanceBetweenNodes = new LinkedList<>();
    static List<List<Long>> timeBetweenNodes = new LinkedList<>();
    static Set<Integer> Pmais = new HashSet<>();
    static Set<Integer> Pmenos = new HashSet<>();
    static Set<Integer> setOfNodes = new HashSet<>();
    static int numberOfNodes;
    static Map<Integer, List<Request>> requestsWhichBoardsInNode = new HashMap<>();
    static Map<Integer, List<Request>> requestsWhichLeavesInNode = new HashMap<>();
    static List<Integer> loadIndexList = new LinkedList<>();
    static Set<Integer> setOfVehicles = new HashSet<>();
    static List<Request> listOfNonAttendedRequests = new ArrayList<>();
    static List<Request> requestList = new ArrayList<>();

    //-------------------Test--------------------------------
    static Long currentTime;
    static Integer lastNode;

    public static void main(String[] args) throws ApiException, Exception, IOException, BiffException {
        String directionsApiKey = "AIzaSyD9W0em7H723uVOMD6QFe_1Mns71XAi5JU";
        String filePath = "/home/renansantos/Área de Trabalho/Excel Instances/";
        filePath = "/home/rmendes/VRPDRT/";

        int numberOfRequests = 250;
        int requestTimeWindows = 10;
        final Integer vehicleCapacity = 4;
        String instanceSize = "s";

        int numberOfNodes = 12;
        String nodesData = "bh_n" + numberOfNodes + instanceSize;
        String adjacenciesData = "bh_adj_n" + numberOfNodes + instanceSize;
        String instanceName = buildInstaceName(nodesData, adjacenciesData, numberOfRequests, numberOfNodes,
                requestTimeWindows, instanceSize);
        Integer numberOfVehicles = 250;

        Integer populationSize = 100;
        Integer maximumNumberOfGenerations = 1000;
        Integer maximumNumberOfExecutions = 21;
        double probabilityOfMutation = 0.8;//0.9
        double probabilityOfCrossover = 0.7;
        double neighborhoodSelectionProbability = 0.02;//0.02

        int numberOfEvaluations = 60000;
        int neighborSize = 10;//10//3
        int maximumNumberOfReplacedSolutions = 1;
        int fileSize = populationSize;
        FunctionType functionType = FunctionType.PBI;//PBI

        List<Double> parameters = new ArrayList<>();
        List<Double> nadirPoint = new ArrayList<>();
        List<List<Integer>> transformationList = new ArrayList<>();
        if (numberOfRequests == 250){
            new ScriptGenerator(instanceName, instanceSize, vehicleCapacity)
                .generate("30d", "lamho-1");
        }else{
            new ScriptGenerator(instanceName, instanceSize, vehicleCapacity)
                .generate("7d", "lamho-1");
        }
        
//                .generate("30d", "lamho-1","MOEAD_R2", "/tmp/rmendes/Results/", "/home/rmendes/experiment_tmp");

        numberOfNodes = readProblemUsingExcelData(filePath, instanceName, nodesData, adjacenciesData, requests, distanceBetweenNodes,
                timeBetweenNodes, Pmais, Pmenos, requestsWhichBoardsInNode, requestsWhichLeavesInNode, setOfNodes,
                numberOfNodes, loadIndexList);

        Algorithms.printProblemInformations(requests, numberOfVehicles, vehicleCapacity, instanceName, adjacenciesData, nodesData);
        Methods.initializeFleetOfVehicles(setOfVehicles, numberOfVehicles);

        parameters.add(0.10);//1
        parameters.add((double) requestTimeWindows);//delta_t
        parameters.add((double) numberOfNodes);//n
        parameters.add((double) numberOfRequests * numberOfNodes);//r n
        parameters.add((double) numberOfRequests);//r
        parameters.add((double) numberOfNodes);//n
        parameters.add(1.0);//1
        parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);//

        nadirPoint.add(10000000.0);
        nadirPoint.add(10000000.0);
        
        System.out.println("Nadir Point = " + nadirPoint);
        System.out.println("Instance Name = " + instanceName);
        int reducedDimension;
                   
        reducedDimension = 8;
       
//        RandomSolutionGenerator solutionGenerator = new RandomSolutionGenerator();
//        solutionGenerator.generateSolutions(reducedDimension, filePath, parameters);
//        long start = System.currentTimeMillis();

//        reducedDimension = 2;
        
        MOEAD(instanceName, neighborSize, numberOfEvaluations, maximumNumberOfReplacedSolutions, reducedDimension, transformationList, parameters,
        nadirPoint, populationSize, maximumNumberOfGenerations, functionType, maximumNumberOfExecutions,
        neighborhoodSelectionProbability, probabilityOfMutation, probabilityOfCrossover, requests,
        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows,
        currentTime, lastNode);

//        onMOEAD(instanceName, neighborSize, numberOfEvaluations, maximumNumberOfReplacedSolutions, reducedDimension, CorrelationType.KENDALL,
//        transformationList, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, functionType, maximumNumberOfExecutions,
//        neighborhoodSelectionProbability, probabilityOfMutation, probabilityOfCrossover, requests,
//        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
//        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows,
//        currentTime, lastNode);
        
//        
//        onMOEAD(instanceName, neighborSize, numberOfEvaluations, maximumNumberOfReplacedSolutions, reducedDimension, CorrelationType.PEARSON,
//        transformationList, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, functionType, maximumNumberOfExecutions,
//        neighborhoodSelectionProbability, probabilityOfMutation, probabilityOfCrossover, requests,
//        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
//        listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows,
//        currentTime, lastNode);
    }

}
