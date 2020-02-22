/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Request;
import java.io.IOException;
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
public class ProblemData {

    private int numberOfNodes;
    private List<Integer> nodes;
    private List<Request> requests;
    private List<Request> instanceRequests = new ArrayList<>();
    private List<List<Integer>> listOfAdjacencies = new LinkedList<>();
    private List<List<Long>> distanceBetweenNodes = new LinkedList<>();
    private List<List<Long>> timeBetweenNodes = new LinkedList<>();
    private long[][] distance;
    private String instanceName;
    private String nodesInstanceName;
    private String adjacenciesInstanceName;

    private int numberOfVehicles;
    private int vehicleCapacity;
    private Instance instance;
    private String excelDataFilesPath;

    public ProblemData(String instanceName, String nodesInstanceName, String adjacenciesInstanceName,
            int numberOfVehicles, int vehicleCapacity) {
        this.instanceName = instanceName;
        this.nodesInstanceName = nodesInstanceName;
        this.adjacenciesInstanceName = adjacenciesInstanceName;
        this.numberOfVehicles = numberOfVehicles;
        this.vehicleCapacity = vehicleCapacity;
        //this.readInstance();
        //this.startVehiclesData();
    }

    public ProblemData(Instance instance, String excelDataFilesPath) throws IOException, BiffException {
        this.instance = instance;
        this.instanceName = instance.getInstanceName();
        this.nodesInstanceName = instance.getNodesData();
        this.adjacenciesInstanceName = instance.getAdjacenciesData();
        this.numberOfVehicles = instance.getNumberOfVehicles();
        this.vehicleCapacity = instance.getVehicleCapacity();
        this.excelDataFilesPath = excelDataFilesPath;
        this.readExcelInstance();
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public void setNodes(List<Integer> nodes) {
        this.nodes = nodes;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Request> getInstanceRequests() {
        return instanceRequests;
    }

    public void setInstanceRequests(List<Request> instanceRequests) {
        this.instanceRequests = instanceRequests;
    }

   
    public long[][] getDistance() {
        return distance;
    }

    public void setDistance(long[][] distance) {
        this.distance = distance;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getNodesInstanceName() {
        return nodesInstanceName;
    }

    public void setNodesInstanceName(String nodesInstanceName) {
        this.nodesInstanceName = nodesInstanceName;
    }

    public String getAdjacenciesInstanceName() {
        return adjacenciesInstanceName;
    }

    public void setAdjacenciesInstanceName(String adjacenciesInstanceName) {
        this.adjacenciesInstanceName = adjacenciesInstanceName;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

//    public void readInstance() {
//        this.numberOfNodes = new NumberOfNodesDAO().getNumberOfNodes(this.nodesInstanceName);
//        this.nodes = new NodeDAO(this.nodesInstanceName).getListOfNodes();
//        this.requests = new RequestDAO(this.instanceName).getListOfRequestUsingNodesList(nodes);
//        this.duration = new AdjacenciesDAO(this.adjacenciesInstanceName, this.nodesInstanceName).getDurationBetweenNodes(this.numberOfNodes);
//        this.distance = new AdjacenciesDAO(this.adjacenciesInstanceName, this.nodesInstanceName).getDistanceBetweenNodes(this.numberOfNodes);
//        this.instanceRequests.addAll(this.requests);
//    }

    public void readExcelInstance()  {
        ReadDataInExcelFile reader = new ReadDataInExcelFile(this.excelDataFilesPath, instance);
        this.numberOfNodes = reader.getNumberOfNodes();//<-- erro está aqui
//        this.nodes = reader.getListOfNodes();
        this.requests = reader.getRequests();
        this.timeBetweenNodes = reader.getAdjacenciesListOfTimes();
        this.distanceBetweenNodes = reader.getAdjacenciesListOfDistances();
        this.instanceRequests.addAll(this.requests);
    }

    public List<List<Long>> getDistanceBetweenNodes() {
        return distanceBetweenNodes;
    }

    public List<List<Long>> getTimeBetweenNodes() {
        return timeBetweenNodes;
    }

    


}
