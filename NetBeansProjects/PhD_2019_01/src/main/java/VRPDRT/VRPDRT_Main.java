/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRT;

import Algorithms.*;
import static Algorithms.Algorithms.*;
import java.util.*;
import ProblemRepresentation.*;
import com.google.maps.errors.ApiException;
import java.io.IOException;
import static Algorithms.EvolutionaryAlgorithms.*;
import static Algorithms.Methods.inicializeRandomPopulation;
import static Algorithms.Methods.readProblemUsingExcelData;
import InstanceReader.*;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class VRPDRT_Main {

    final static Long timeWindows = (long) 3;
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

    public static void main(String[] args) throws ApiException, InterruptedException, IOException, BiffException {
        String directionsApiKey = "AIzaSyD9W0em7H723uVOMD6QFe_1Mns71XAi5JU";
        String filePath = "/home/renansantos/Área de Trabalho/Excel Instances/";
        //filePath = "/home/rmendes/VRPDRT/";

        int numberOfRequests = 50;
        int requestTimeWindows = 10;
        final Integer vehicleCapacity = 11;
        String instanceSize = "s";

        int numberOfNodes = 12;
        String nodesData = "bh_n" + numberOfNodes + instanceSize;
        String adjacenciesData = "bh_adj_n" + numberOfNodes + instanceSize;
        String instanceName = buildInstaceName(nodesData, adjacenciesData, numberOfRequests, numberOfNodes,
                requestTimeWindows, instanceSize);
        final Integer numberOfVehicles = 250;

        Integer populationSize = 20;
        Integer maximumNumberOfGenerations = 1000;
        Integer maximumNumberOfExecutions = 30;
        double probabilityOfMutation = 0.02;
        double probabilityOfCrossover = 0.7;
        double neighborhoodSelectionProbability = 0.7;
        int numberOfEvaluations = 300000;
        int neighborSize = 100;
        int maximumNumberOfReplacedSolutions = 20;
        int fileSize = populationSize;
        EvolutionaryAlgorithms.FunctionType functionType = EvolutionaryAlgorithms.FunctionType.AGG;

        List<Double> parameters = new ArrayList<>();//0.0273, 0.5208, 0.0161, 0.3619, 0.0739
        List<Double> nadirPoint = new ArrayList<>();

//        new DataUpdaterUsingGoogleMapsApi(directionsApiKey, new NodeDAO(nodesData).getListOfNodes(),
//                adjacenciesData).updateAdjacenciesData();
//        
        new ScriptGenerator(instanceName, instanceSize, vehicleCapacity).generate("2d", "medium");

//        numberOfNodes = readProblemData(instanceName, nodesData, adjacenciesData, requests, distanceBetweenNodes,
//                timeBetweenNodes, Pmais, Pmenos, requestsWhichBoardsInNode, requestsWhichLeavesInNode, setOfNodes,
//                numberOfNodes, loadIndexList);
        numberOfNodes = readProblemUsingExcelData(filePath, instanceName, nodesData, adjacenciesData, requests, distanceBetweenNodes,
                timeBetweenNodes, Pmais, Pmenos, requestsWhichBoardsInNode, requestsWhichLeavesInNode, setOfNodes,
                numberOfNodes, loadIndexList);

        Algorithms.printProblemInformations(requests, numberOfVehicles, vehicleCapacity, instanceName, adjacenciesData, nodesData);
        Methods.initializeFleetOfVehicles(setOfVehicles, numberOfVehicles);

        parameters.add(0.10);//1
        parameters.add((double) requestTimeWindows);//delta_t
        parameters.add((double) numberOfNodes);//n
        parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);// r n delta_t
        parameters.add((double) numberOfRequests * numberOfNodes);//r n
        parameters.add((double) numberOfRequests);//r
        parameters.add((double) numberOfNodes);//n
        parameters.add(1.0);//1
        parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);//

        nadirPoint.add(10000000.0);
        nadirPoint.add(10000000.0);
        System.out.println("Nadir Point = " + nadirPoint);
        System.out.println("Instance Name = " + instanceName);

        int reducedDimension = 9;

        for (int i = 0; i < 20; i++) {
            Random rnd = new Random();
            double x, y, z, w;

            do {
                x = rnd.nextDouble();
                y = rnd.nextDouble();
                z = rnd.nextDouble();
                w = 1 - x - y - z;
            } while (x + y + z + w != 1);

            ProblemSolution solution = greedyConstructive(x, y, z, w,
                    requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity,
                    setOfVehicles, listOfNonAttendedRequests, requestList,
                    loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                    timeWindows, currentTime, lastNode);
            System.out.println(solution);
        }
        
        
        List<ProblemSolution> population = new ArrayList<>();
            inicializeRandomPopulation(parameters, reducedDimension, population, populationSize, requests,
                    requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                    requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
        System.out.println();
        population.forEach(u -> System.out.println(u));
//        MOEAD(instanceName, neighborSize, numberOfEvaluations,maximumNumberOfReplacedSolutions, reducedDimension, parameters,
//                nadirPoint, populationSize, maximumNumberOfGenerations,functionType, maximumNumberOfExecutions,
//                neighborhoodSelectionProbability, probabilityOfMutation, probabilityOfCrossover, requests,
//                requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, 
//                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows,
//                currentTime, lastNode);
//        
//        NSGAII(instanceName, reducedDimension, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
//                requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
//                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
//                timeWindows, currentTime, lastNode);

//        SPEA2(instanceName, reducedDimension, parameters, nadirPoint, populationSize, fileSize, maximumNumberOfGenerations, maximumNumberOfExecutions,
//                probabilityOfMutation, probabilityOfCrossover, requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode,
//                numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, requestList, loadIndexList,
//                timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
        //new GoogleStaticMap(new NodeDAO(nodesData).getListOfNodes(), adjacenciesData, nodesData).getStaticMapForInstance();
        //new SolutionGeneratorForAggregationTree().generateSolutionsForAggregationTree(reducedDimension,parameters);
    }

}
