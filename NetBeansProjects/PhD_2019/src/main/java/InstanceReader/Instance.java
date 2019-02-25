/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

/**
 *
 * @author renansantos
 */
public class Instance {

    private int numberOfRequests;
    private int requestTimeWindows;
    private Integer vehicleCapacity;
    private String instanceSize;
    private int numberOfNodes;
    private String nodesData;
    private String adjacenciesData;
    String instanceName;
    private int numberOfVehicles = numberOfRequests;

    public Instance() {
        
    }
    
    public Instance(String instanceName) {
        this.instanceName = instanceName;
        removeInformationFromInstanceName();
    }
    
    public Instance setNumberOfRequests(int numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
        return this;
    }

    public Instance setRequestTimeWindows(int requestTimeWindows) {
        this.requestTimeWindows = requestTimeWindows;
        return this;
    }

    public Instance setVehicleCapacity(Integer vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
        return this;
    }

    public Instance setInstanceSize(String instanceSize) {
        this.instanceSize = instanceSize;
        return this;
    }

    public Instance setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        return this;
    }

    public Instance setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
        return this;
    }

    public void setNodesData(String nodesData) {
        this.nodesData = nodesData;
    }

    public void setAdjacenciesData(String adjacenciesData) {
        this.adjacenciesData = adjacenciesData;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public int getRequestTimeWindows() {
        return requestTimeWindows;
    }

    public Integer getVehicleCapacity() {
        return vehicleCapacity;
    }

    public String getInstanceSize() {
        return instanceSize;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public String getNodesData() {
        return nodesData;
    }

    public String getAdjacenciesData() {
        return adjacenciesData;
    }

    public String getInstanceName() {
        this.instanceName = this.buildInstaceNames();
        return instanceName;
    }
    
    public String getFullInstanceName(){
        this.build();
        return instanceName + "k" + this.vehicleCapacity + this.instanceSize;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public String buildInstaceNames() {
        String instanceName;
        if (numberOfRequests < 100) {

            if (numberOfRequests < 10) {
                if (requestTimeWindows < 10) {
                    instanceName = "r00" + numberOfRequests + "n" + numberOfNodes + "tw0" + requestTimeWindows;
                } else {
                    instanceName = "r00" + numberOfRequests + "n" + numberOfNodes + "tw" + requestTimeWindows;
                }
            } else if (requestTimeWindows < 10) {
                instanceName = "r0" + numberOfRequests + "n" + numberOfNodes + "tw0" + requestTimeWindows;
            } else {
                instanceName = "r0" + numberOfRequests + "n" + numberOfNodes + "tw" + requestTimeWindows;
            }

        } else if (requestTimeWindows < 10) {
            instanceName = "r" + numberOfRequests + "n" + numberOfNodes + "tw0" + requestTimeWindows;
        } else {
            instanceName = "r" + numberOfRequests + "n" + numberOfNodes + "tw" + requestTimeWindows;
        }

        nodesData = "bh_n" + numberOfNodes + instanceSize;
        adjacenciesData = "bh_adj_n" + numberOfNodes + instanceSize;

        return instanceName;
    }

    
    public Instance build(){
        this.instanceName = this.buildInstaceNames();
        return this;
    }
    
    public String toString() {
        return this.instanceName;
    }
    
    private void removeInformationFromInstanceName(){
        String numberOfRequestsString = this.instanceName.substring(1, 4);
        String numberOfNodesString = this.instanceName.substring(5, 7);
        String requestTimeWindowsString  = this.instanceName.substring(9, 11);
        this.numberOfRequests = Integer.parseInt(numberOfRequestsString);
        this.numberOfNodes = Integer.parseInt(numberOfNodesString);
        this.requestTimeWindows = Integer.parseInt(requestTimeWindowsString);
        if(this.instanceName.length() > 11){
            this.vehicleCapacity = Integer.parseInt(this.instanceName.substring(12,13));
            this.instanceSize = this.instanceName.substring(13,14);
            this.instanceName = buildInstaceNames();
            int i = 0;
        }
    }
}
