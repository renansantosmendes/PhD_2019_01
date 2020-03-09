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
import static Algorithms.Methods.readProblemUsingExcelData;
import InstanceReader.ScriptGenerator;
import java.io.IOException;

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
        Integer numberOfVehicles = 250;

        Integer populationSize = 100;
        Integer maximumNumberOfGenerations = 1000;
        Integer maximumNumberOfExecutions = 1;
        double probabilityOfMutation = 0.02;
        double probabilityOfCrossover = 0.7;
        double neighborhoodSelectionProbability = 0.8;

        int numberOfEvaluations = 1000;
        int neighborSize = 10;
        int maximumNumberOfReplacedSolutions = 30;
        int fileSize = populationSize;
        FunctionType functionType = FunctionType.AGG;

        List<Double> parameters = new ArrayList<>();//0.0273, 0.5208, 0.0161, 0.3619, 0.0739
        List<Double> nadirPoint = new ArrayList<>();

//        new DataUpdaterUsingGoogleMapsApi(directionsApiKey, new NodeDAO(nodesData).getListOfNodes(),
//                adjacenciesData).updateAdjacenciesData();
//        
        new ScriptGenerator(instanceName, instanceSize, vehicleCapacity)
                .generate("2d", "medium");

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
        //parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);// r n delta_t
        parameters.add((double) numberOfRequests * numberOfNodes);//r n
        parameters.add((double) numberOfRequests);//r
        parameters.add((double) numberOfNodes);//n
        parameters.add(1.0);//1
        parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);//

        nadirPoint.add(10000000.0);
        nadirPoint.add(10000000.0);
        System.out.println("Nadir Point = " + nadirPoint);
        System.out.println("Instance Name = " + instanceName);

        int reducedDimension = 2;

        onMOEAD(instanceName, neighborSize, numberOfEvaluations, maximumNumberOfReplacedSolutions, reducedDimension, parameters,
                nadirPoint, populationSize, maximumNumberOfGenerations, functionType, maximumNumberOfExecutions,
                neighborhoodSelectionProbability, probabilityOfMutation, probabilityOfCrossover, requests,
                requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows,
                currentTime, lastNode);
       

    }

}
