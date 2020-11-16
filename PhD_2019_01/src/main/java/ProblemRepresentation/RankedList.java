/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

import java.util.*;

/**
 *
 * @author renansantos
 */
public class RankedList {

    private Map<Integer, Double> costRankList;
    private Map<Integer, Double> numberOfPassengersRankList;
    private Map<Integer, Double> deliveryTimeWindowRankList;
    private Map<Integer, Double> timeWindowRankList;
    private Map<Integer, Double> nodeRankingFunction;
    private int numberOfNodes;
    private Double alphaD;
    private Double alphaP;
    private Double alphaV;
    private Double alphaT;

    public RankedList(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
    
    public void initialize() {
        costRankList = new HashMap<>(numberOfNodes);
        numberOfPassengersRankList = new HashMap<>(numberOfNodes);
        deliveryTimeWindowRankList = new HashMap<>(numberOfNodes);
        timeWindowRankList = new HashMap<>(numberOfNodes);
        nodeRankingFunction = new HashMap<>(numberOfNodes);
    }

    public void clear() {
        costRankList.clear();
        numberOfPassengersRankList.clear();
        deliveryTimeWindowRankList.clear();
        timeWindowRankList.clear();
        nodeRankingFunction.clear();
    }

    public RankedList setAlphaD(Double alphaD) {
        this.alphaD = alphaD;
        return this;
    }

    public RankedList setAlphaP(Double alphaP) {
        this.alphaP = alphaP;
        return this;
    }

    public RankedList setAlphaV(Double alphaV) {
        this.alphaV = alphaV;
        return this;
    }

    public RankedList setAlphaT(Double alphaT) {
        this.alphaT = alphaT;
        return this;
    }

    public RankedList calculateCRL(Set<Integer> FeasibleNode, List<List<Long>> distanceBetweenNodes, Integer lastNode) {
        Double min, max;
        for (Integer i : FeasibleNode) {
            costRankList.put(i, (double) distanceBetweenNodes.get(lastNode).get(i));
        }

        min = Collections.min(costRankList.values());
        max = Collections.max(costRankList.values());

        if (min != max) {
            for (Integer i : FeasibleNode) {
                costRankList.put(i, ((max - costRankList.get(i)) / (max - min)));
            }
        } else {
            for (Integer i : FeasibleNode) {
                costRankList.put(i, 1.0);
            }
        }

        return this;
    }

    public RankedList calculateNRL(Set<Integer> FeasibleNode, List<Integer> loadIndex, Integer lastNode) {
        Double min, max;
        for (Integer i : FeasibleNode) {
            numberOfPassengersRankList.put(i, (double) loadIndex.get(i));
        }

        min = Collections.min(numberOfPassengersRankList.values());
        max = Collections.max(numberOfPassengersRankList.values());

        if (min != max) {
            for (Integer i : FeasibleNode) {
                numberOfPassengersRankList.put(i, ((numberOfPassengersRankList.get(i) - min) / (max - min)));
            }
        } else {
            for (Integer i : FeasibleNode) {
                numberOfPassengersRankList.put(i, 1.0);
            }
        }
        return this;
    }

    public RankedList calculateDRL(Set<Integer> feasibleNodes, Map<Integer, List<Request>> requestsWhichLeavesInNode,
            Integer lastNode, List<List<Long>> timeBetweenNodes, List<Long> EarliestTime) {
        Double min, max;
        for (Integer i : feasibleNodes) {
            if (requestsWhichLeavesInNode.get(i).size() > 0) {
                for (Request request : requestsWhichLeavesInNode.get(i)) {
                    EarliestTime.add(request.getDeliveryTimeWindowLower());
                }
                deliveryTimeWindowRankList.put(i, (double) (Collections.min(EarliestTime) + timeBetweenNodes.get(lastNode).get(i)));
                EarliestTime.clear();
            }
        }

        if (deliveryTimeWindowRankList.size() > 0) {
            min = Collections.min(deliveryTimeWindowRankList.values());
            max = Collections.max(deliveryTimeWindowRankList.values());

            if (min != max) {
                for (Integer i : deliveryTimeWindowRankList.keySet()) {
                    deliveryTimeWindowRankList.put(i, ((max - deliveryTimeWindowRankList.get(i)) / (max - min)));
                }
            } else {
                for (Integer i : deliveryTimeWindowRankList.keySet()) {
                    deliveryTimeWindowRankList.put(i, 1.0);
                }
            }
        }

        for (Integer i : feasibleNodes) {
            if (!deliveryTimeWindowRankList.containsKey(i)) {
                deliveryTimeWindowRankList.put(i, 0.0);
            }
        }
        return this;
    }

    public RankedList calculateTRL(Set<Integer> feasibleNodes, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Integer lastNode, List<List<Long>> timeBetweenNodes, List<Long> EarliestTime) {
        double min, max;
        for (Integer i : feasibleNodes) {
            if (requestsWhichBoardsInNode.get(i).size() > 0) {
                for (Request request : requestsWhichBoardsInNode.get(i)) {
                    EarliestTime.add(request.getPickupTimeWindowLower());
                }
                timeWindowRankList.put(i, (double) (Collections.min(EarliestTime) + timeBetweenNodes.get(lastNode).get(i)));
                EarliestTime.clear();
            }
        }

        if (timeWindowRankList.size() > 0) {
            min = Collections.min(timeWindowRankList.values());
            max = Collections.max(timeWindowRankList.values());

            if (min != max) {
                for (Integer i : timeWindowRankList.keySet()) {
                    timeWindowRankList.put(i, ((max - timeWindowRankList.get(i)) / (max - min)));
                }
            } else {
                for (Integer i : timeWindowRankList.keySet()) {
                    timeWindowRankList.put(i, 1.0);
                }
            }
        }

        for (Integer i : feasibleNodes) {
            if (!timeWindowRankList.containsKey(i)) {
                timeWindowRankList.put(i, 0.0);
            }
        }
        return this;
    }

    public void calculateListWithoutFeasibleNodes(Set<Integer> feasibleNodes) {
        for (Integer i : feasibleNodes) {
            costRankList.put(i, 1.0);
            numberOfPassengersRankList.put(i, 1.0);
            deliveryTimeWindowRankList.put(i, 1.0);
            timeWindowRankList.put(i, 1.0);
        }
    }
    
    public void calculateNRF(Set<Integer> FeasibleNode) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        for (Integer i : FeasibleNode) {
            nodeRankingFunction.put(i, (alphaD * costRankList.get(i) + alphaP * numberOfPassengersRankList.get(i))
                    + (alphaV * deliveryTimeWindowRankList.get(i) + alphaT * timeWindowRankList.get(i)));
        }
    }

    public Collection<Double> getValuesOfNRF(){
        return nodeRankingFunction.values();
    }

    public Map<Integer, Double> getCostRankList() {
        return costRankList;
    }

    public Map<Integer, Double> getNumberOfPassengersRankList() {
        return numberOfPassengersRankList;
    }

    public Map<Integer, Double> getDeliveryTimeWindowRankList() {
        return deliveryTimeWindowRankList;
    }

    public Map<Integer, Double> getTimeWindowRankList() {
        return timeWindowRankList;
    }

    public Map<Integer, Double> getNodeRankingFunction() {
        return nodeRankingFunction;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }
    
    
    
    
}
