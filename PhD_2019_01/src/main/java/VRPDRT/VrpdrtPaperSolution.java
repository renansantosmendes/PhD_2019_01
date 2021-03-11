/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRT;

import Algorithms.Algorithms;
import static Algorithms.Algorithms.buildInstaceName;
import static Algorithms.Algorithms.rebuildSolutionForOnlineAlgorithms;
import Algorithms.EvolutionaryAlgorithms;
import static Algorithms.EvolutionaryAlgorithms.getMinMaxForObjectives;
import static Algorithms.EvolutionaryAlgorithms.initializeMaxValues;
import static Algorithms.EvolutionaryAlgorithms.initializeMinValues;
import static Algorithms.EvolutionaryAlgorithms.onMOEAD;
import static Algorithms.EvolutionaryAlgorithms.populationGeneratorForWeights;
import Algorithms.Methods;
import static Algorithms.Methods.readProblemUsingExcelData;
import InstanceReader.ReadDataInExcelFile;
import InstanceReader.ScriptGenerator;
import ProblemRepresentation.Node;
import ProblemRepresentation.ProblemSolution;
import ProblemRepresentation.Request;
import ReductionTechniques.CorrelationType;
import com.google.maps.errors.ApiException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jxl.read.biff.BiffException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author renan
 */
public class VrpdrtPaperSolution {

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
        String directionsApiKey = "AIzaSyDnbydYYYtvGROBcUXDiiOaxafkmJ0vyos";
        String filePath = "C:\\Doutorado - Renan\\Excel Instances\\";
        //filePath = "/home/rmendes/VRPDRT/";

        int numberOfRequests = 4;
        int requestTimeWindows = 10;
        final Integer vehicleCapacity = 4;
        String instanceSize = "s";

        int numberOfNodes = 12;
        String nodesData = "bh_n" + numberOfNodes + instanceSize;
        String adjacenciesData = "bh_adj_n" + numberOfNodes + instanceSize;
        String instanceName = buildInstaceName(nodesData, adjacenciesData, numberOfRequests, numberOfNodes,
                requestTimeWindows, instanceSize);
        Integer numberOfVehicles = 250;
        instanceName = "r004n12tw10";
        System.out.println(instanceName);

        Integer populationSize = 100;
        Integer maximumNumberOfGenerations = 1000;
        Integer maximumNumberOfExecutions = 21;//21
        double probabilityOfMutation = 0.8;//0.9//0.02
        double probabilityOfCrossover = 0.7;
        double neighborhoodSelectionProbability = 0.02;//0.02//0.5

        int numberOfEvaluations = 60000;
        int intervalOfAggregations = 5;
        int neighborSize = 10;//10//3
        int maximumNumberOfReplacedSolutions = 1;//10//3//1//5
        int fileSize = populationSize;
        EvolutionaryAlgorithms.FunctionType functionType = EvolutionaryAlgorithms.FunctionType.PBI;//PBI

        List<Double> parameters = new ArrayList<>();
        List<List<Double>> nadirPoint = new ArrayList<>();
        List<List<Integer>> transformationList = new ArrayList<>();
        if (numberOfRequests >= 250) {
            new ScriptGenerator(instanceName, instanceSize, vehicleCapacity)
                    .generate("30d", "lamho-0");
        } else {
            new ScriptGenerator(instanceName, instanceSize, vehicleCapacity)
                    .generate("7d", "lamho-0");
        }

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

        parameters.clear();

        parameters.add(1.0);
        parameters.add(1.0);
        parameters.add(1.0);
        parameters.add(1.0);
        parameters.add(1.0);
        parameters.add(1.0);
        parameters.add(1.0);
        parameters.add(1.0);

        List<Double> mins = initializeMinValues();
        List<Double> maxs = initializeMaxValues();
        nadirPoint.add(mins);
        nadirPoint.add(maxs);

        System.out.println("Nadir Point = " + nadirPoint);
        System.out.println("Instance Name = " + instanceName);
        int reducedDimension = 2;

        ProblemSolution s = new ProblemSolution();
        s.setSolution(Algorithms.greedyConstructive(0.4, 0.2, 0.2, 0.2, nadirPoint, 0, requests, requestsWhichBoardsInNode,
                requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, requestList,
                loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode));

        System.out.println(s.getSetOfRoutes());
        System.out.println("Attendance Request List");
        s.getSetOfRoutes().forEach(route -> System.out.println(route.getRequestAttendanceList()));

        System.out.println("Solution");
        System.out.println(s);
        System.out.println(s.getStringWithOriginalObjectivesForCsvFile());

        List<Node> nodes = new ReadDataInExcelFile(filePath, instanceName, nodesData, adjacenciesData).getListOfNodes();
        System.out.println(nodes);

        s.getStaticMapWithAllRoutes(nodes, "bh_adj_n12s", nodesData);
    }
}
