/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

import Algorithms.Methods;
import InstanceReader.AdjacenciesDAO;
import InstanceReader.NodeDAO;
import InstanceReader.RequestDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class InstanceData {
    final Long timeWindows = (long) 3;
    private List<Request> listOfRequests = new ArrayList<>();
    private List<List<Integer>> listOfAdjacencies = new LinkedList<>();
    private List<List<Long>> distanceBetweenNodes = new LinkedList<>();
    private List<List<Long>> timeBetweenNodes = new LinkedList<>();
    private Set<Integer> setOfOrigins = new HashSet<>();
    private Set<Integer> setOfDestinations = new HashSet<>();
    private Set<Integer> setOfNodes = new HashSet<>();
    private int numberOfNodes;
    private Map<Integer, List<Request>> requestsWichBoardsInNode = new HashMap<>();
    private Map<Integer, List<Request>> requestsWichLeavesInNode = new HashMap<>();
    private List<Integer> loadIndexList = new LinkedList<>();
    private Set<Integer> setOfVehicles = new HashSet<>();
    private List<Request> listOfNonAttendedRequests = new ArrayList<>();
    private List<Request> requestList = new ArrayList<>();
    static Long currentTime;
    static Integer lastNode;
    private String instanceName;
    private String nodesData;
    private String adjacenciesData;
    private int numberOfVehicles = 500;
    private int vehicleCapacity = 1;

    public InstanceData(String instanceName, String nodesData, String adjacenciesData) {
        this.instanceName = instanceName;
        this.nodesData = nodesData;
        this.adjacenciesData = adjacenciesData;
        Methods.initializeFleetOfVehicles(setOfVehicles, numberOfVehicles);
    }

    public List<Request> getListOfRequests() {
        return listOfRequests;
    }

    public void setListOfRequests(List<Request> listOfRequests) {
        this.listOfRequests = listOfRequests;
    }

    public List<List<Integer>> getListOfAdjacencies() {
        return listOfAdjacencies;
    }

    public void setListOfAdjacencies(List<List<Integer>> listOfAdjacencies) {
        this.listOfAdjacencies = listOfAdjacencies;
    }

    public List<List<Long>> getDistanceBetweenNodes() {
        return distanceBetweenNodes;
    }

    public void setDistanceBetweenNodes(List<List<Long>> distanceBetweenNodes) {
        this.distanceBetweenNodes = distanceBetweenNodes;
    }

    public List<List<Long>> getTimeBetweenNodes() {
        return timeBetweenNodes;
    }

    public void setTimeBetweenNodes(List<List<Long>> timeBetweenNodes) {
        this.timeBetweenNodes = timeBetweenNodes;
    }

    public Set<Integer> getSetOfOrigins() {
        return setOfOrigins;
    }

    public void setSetOfOrigins(Set<Integer> setOfOrigins) {
        this.setOfOrigins = setOfOrigins;
    }

    public Set<Integer> getSetOfDestinations() {
        return setOfDestinations;
    }

    public void setSetOfDestinations(Set<Integer> setOfDestinations) {
        this.setOfDestinations = setOfDestinations;
    }

    public Set<Integer> getSetOfNodes() {
        return setOfNodes;
    }

    public void setSetOfNodes(Set<Integer> setOfNodes) {
        this.setOfNodes = setOfNodes;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public Map<Integer, List<Request>> getRequestsWichBoardsInNode() {
        return requestsWichBoardsInNode;
    }

    public void setRequestsWichBoardsInNode(Map<Integer, List<Request>> requestsWichBoardsInNode) {
        this.requestsWichBoardsInNode = requestsWichBoardsInNode;
    }

    public Map<Integer, List<Request>> getRequestsWichLeavesInNode() {
        return requestsWichLeavesInNode;
    }

    public void setRequestsWichLeavesInNode(Map<Integer, List<Request>> requestsWichLeavesInNode) {
        this.requestsWichLeavesInNode = requestsWichLeavesInNode;
    }

    public List<Integer> getLoadIndexList() {
        return loadIndexList;
    }

    public void setLoadIndexList(List<Integer> loadIndexList) {
        this.loadIndexList = loadIndexList;
    }

    public Set<Integer> getSetOfVehicles() {
        return setOfVehicles;
    }

    public void setSetOfVehicles(Set<Integer> setOfVehicles) {
        this.setOfVehicles = setOfVehicles;
    }

    public List<Request> getListOfNonAttendedRequests() {
        return listOfNonAttendedRequests;
    }

    public void setListOfNonAttendedRequests(List<Request> listOfNonAttendedRequests) {
        this.listOfNonAttendedRequests = listOfNonAttendedRequests;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getNodesData() {
        return nodesData;
    }

    public void setNodesData(String nodesData) {
        this.nodesData = nodesData;
    }

    public String getAdjacenciesData() {
        return adjacenciesData;
    }

    public void setAdjacenciesData(String adjacenciesData) {
        this.adjacenciesData = adjacenciesData;
    }    

    public static Long getCurrentTime() {
        return currentTime;
    }

    public static void setCurrentTime(Long currentTime) {
        InstanceData.currentTime = currentTime;
    }

    public static Integer getLastNode() {
        return lastNode;
    }

    public static void setLastNode(Integer lastNode) {
        InstanceData.lastNode = lastNode;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }
    
    
    
    public Integer readProblemData() {
        listOfRequests.addAll(new RequestDAO(instanceName).getListOfRequest());
        List<Request> requests = new ArrayList<>();
        requests.addAll(listOfRequests);
        listOfRequests.clear();
        listOfRequests.addAll(requests.subList(0, 8));
        setOfNodes.addAll(new NodeDAO(nodesData).getSetOfNodes());
        distanceBetweenNodes.addAll(new AdjacenciesDAO(adjacenciesData, nodesData).getAdjacenciesListOfDistances());
        timeBetweenNodes.addAll(new AdjacenciesDAO(adjacenciesData, nodesData).getAdjacenciesListOfTimes());
        numberOfNodes = new AdjacenciesDAO(adjacenciesData, nodesData).getNumberOfNodes();

        setOfOrigins.addAll(listOfRequests.stream()
                .map(Request::getOrigin)
                .collect(Collectors.toCollection(HashSet::new)));
        setOfDestinations.addAll(listOfRequests.stream()
                .map(Request::getDestination)
                .collect(Collectors.toCollection(HashSet::new)));

        requestsWichBoardsInNode.putAll(listOfRequests.stream()
                .collect(Collectors.groupingBy(Request::getOrigin)));
        requestsWichLeavesInNode.putAll(listOfRequests.stream()
                .collect(Collectors.groupingBy(Request::getDestination)));

        for (int i = 0; i < numberOfNodes; i++) {
            if (requestsWichBoardsInNode.get(i) != null && requestsWichLeavesInNode.get(i) != null) {
                loadIndexList.add(requestsWichBoardsInNode.get(i).size() - requestsWichLeavesInNode.get(i).size());
            } else {
                loadIndexList.add(0);
            }
        }
        return numberOfNodes;
    }
}
