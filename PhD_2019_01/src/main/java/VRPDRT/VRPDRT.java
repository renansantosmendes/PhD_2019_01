/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRT;

import InstanceReader.*;
import ProblemRepresentation.*;
import java.io.IOException;
import java.util.*;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class VRPDRT {

    private final Long timeWindows = (long) 3;
    private List<Request> requests = new ArrayList<>();
    private List<List<Integer>> listOfAdjacencies = new LinkedList<>();
    private List<List<Long>> distanceBetweenNodes = new LinkedList<>();
    private List<List<Long>> timeBetweenNodes = new LinkedList<>();
    private Set<Integer> setOfNodes = new HashSet<>();
    private int numberOfNodes;
    private Map<Integer, List<Request>> requestsWhichBoardsInNode = new HashMap<>();
    private Map<Integer, List<Request>> requestsWhichLeavesInNode = new HashMap<>();
    private List<Integer> loadIndexList = new LinkedList<>();
    private Set<Integer> setOfVehicles = new HashSet<>();
    private List<Request> nonAttendedRequests = new ArrayList<>();
    private List<Request> requestList = new ArrayList<>();
    private ProblemData data;
    private Instance instance;
    private String instanceName;
    private String nodesInstanceName;
    private String adjacenciesInstanceName;
    private Integer numberOfVehicles;
    private Integer vehicleCapacity;
    private String excelDataFilesPath;
    private ProblemSolution solution = new ProblemSolution();
    private Long currentTime;
    private Integer lastNode;
    private List<Long> earliestTime = new ArrayList<>();
    private List<Integer> loadIndex;
    private boolean feasibleNodeIsFound;
    private boolean feasibleRequestIsFound;
    private Set<Integer> feasibleNodes;
    private Route currentRoute;
    private double max, min;
    private RankedList rankedList;
    private Iterator<Integer> vehicleIterator;
    private int currentVehicle;
    private String log;
    private Parameters parameters;
    private static int seed = 256344;

    public VRPDRT(Instance instance) {
        this.loadIndexList = new LinkedList<>();
        this.instance = instance;
        this.instanceName = instance.getInstanceName();
        this.nodesInstanceName = instance.getNodesData();
        this.adjacenciesInstanceName = instance.getAdjacenciesData();
        this.numberOfVehicles = instance.getNumberOfVehicles();
        this.vehicleCapacity = instance.getVehicleCapacity();
        rankedList = new RankedList(numberOfNodes);
        this.parameters = new Parameters(instance);
        //this.readInstance();
    }

    public VRPDRT(Instance instance, String excelDataFilesPath) {
        this.loadIndexList = new LinkedList<>();
        this.instance = instance;
        this.instanceName = instance.getInstanceName();
        this.nodesInstanceName = instance.getNodesData();
        this.adjacenciesInstanceName = instance.getAdjacenciesData();
        this.numberOfVehicles = instance.getNumberOfVehicles();
        this.vehicleCapacity = instance.getVehicleCapacity();
        this.excelDataFilesPath = excelDataFilesPath;
        this.readExcelInstance();
        rankedList = new RankedList(numberOfNodes);
        this.parameters = new Parameters(instance);
        //initializeData();
    }

    public VRPDRT(Instance instance, String excelDataFilesPath, RankedList rankedList) {
        this.loadIndexList = new LinkedList<>();
        this.instance = instance;
        this.instanceName = instance.getInstanceName();
        this.nodesInstanceName = instance.getNodesData();
        this.adjacenciesInstanceName = instance.getAdjacenciesData();
        this.numberOfVehicles = instance.getNumberOfVehicles();
        this.vehicleCapacity = instance.getVehicleCapacity();
        this.excelDataFilesPath = excelDataFilesPath;
        this.readExcelInstance();
        this.rankedList = rankedList;
        this.parameters = new Parameters(instance);
        // initializeData();
    }

    public void readExcelInstance() {
        try {
            data = new ProblemData(instance, this.excelDataFilesPath);
            requests.addAll(data.getRequests());
            numberOfNodes = data.getNumberOfNodes();
            timeBetweenNodes.addAll(data.getTimeBetweenNodes());
            distanceBetweenNodes.addAll(data.getDistanceBetweenNodes());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
    }

    public ProblemData getData() {
        return data;
    }

    public void setData(ProblemData data) {
        this.data = data;
    }

    public ProblemSolution getSolution() {
        return solution;
    }

    public void setSolution(ProblemSolution solution) {
        this.solution.setSolution(solution);
    }

    public ProblemSolution buildGreedySolution() {
        initializeData();
        solution = new ProblemSolution();
        log = "";

        rankedList.initialize();
        vehicleIterator = setOfVehicles.iterator();
        nonAttendedRequests.clear();
        while (hasRequestToAttend() && hasAvaibleVehicle()) {

            separateOriginFromDestination();
            currentRoute = new Route();
            currentVehicle = vehicleIterator.next();
            log += "\tGROute " + (currentVehicle + 1) + " ";

            currentRoute.addVisitedNodes(0);
            currentTime = (long) 0;

            lastNode = currentRoute.getLastNode();

            while (hasRequestToAttend()) {
                feasibleNodeIsFound = false;
                calculateLoadIndex();

                feasibleNodes = new HashSet<>();
                earliestTime = new ArrayList<>();

                findFeasibleNodes();

                if (hasFeasibleNode()) {
                    rankedList.calculateCRL(feasibleNodes, distanceBetweenNodes, lastNode)
                            .calculateNRL(feasibleNodes, loadIndex, lastNode)
                            .calculateDRL(feasibleNodes, requestsWhichLeavesInNode, lastNode, timeBetweenNodes, earliestTime)
                            .calculateTRL(feasibleNodes, requestsWhichBoardsInNode, lastNode, timeBetweenNodes, earliestTime);
                } else {
                    rankedList.calculateListWithoutFeasibleNodes(feasibleNodes);
                }

                rankedList.calculateNRF(feasibleNodes);
                max = Collections.max(rankedList.getValuesOfNRF());
                addBestNode();

                landPassengers();
                boardPassengers();
                boardPassengersWithRelaxationTime();
                removesUnfeasibleRequests();
                findRequestToAttend();
                removeNonAttendeRequests();
                finalizeRoute();
            }
            nonAttendedRequestListFeasibilityAvaliation();
        }
        solution.setNonAttendedRequestsList(nonAttendedRequests);
        solution.evaluate(distanceBetweenNodes, vehicleCapacity, parameters);
        solution.setLogger(log);
        solution.linkTheRoutes();
        return solution;
    }

    private boolean hasFeasibleNode() {
        return feasibleNodes.size() > 1;
    }

    private void calculateLoadIndex() {
        loadIndex.clear();
        for (int i = 0; i < numberOfNodes; i++) {
            loadIndex.add(requestsWhichBoardsInNode.get(i).size() - requestsWhichLeavesInNode.get(i).size());
        }
    }

    private void initializeData() {
        requestList.clear();
        nonAttendedRequests.clear();
        requestList.addAll(requests);
        loadIndex = new ArrayList<>();
        initializeFleetOfVehicles();
    }

    private boolean hasAvaibleVehicle() {
        return vehicleIterator.hasNext();
    }

    private boolean hasRequestToAttend() {
        return !requestList.isEmpty();
    }

    public void initializeFleetOfVehicles() {
        for (int i = 0; i < numberOfVehicles; i++) {
            setOfVehicles.add(i);
        }
    }

    public void separateOriginFromDestination() {

        nonAttendedRequests.clear();
        requestsWhichBoardsInNode.clear();
        requestsWhichLeavesInNode.clear();
        List<Request> origin = new LinkedList<Request>();
        List<Request> destination = new LinkedList<Request>();

        for (int j = 0; j < numberOfNodes; j++) {
            for (Request request : requestList) {
                if ((request.getOrigin() == j || request.getDestination() == j)) {
                    if (request.getOrigin() == j) {
                        origin.add((Request) request.clone());
                    } else {
                        destination.add((Request) request.clone());
                    }
                }
            }
            requestsWhichBoardsInNode.put(j, new LinkedList<Request>(origin));
            requestsWhichLeavesInNode.put(j, new LinkedList<Request>(destination));
            origin.clear();
            destination.clear();
        }
    }

    public void findFeasibleNodes() {
        for (int i = 1; i < numberOfNodes; i++) {
            evaluateFeasibilityForNode(i);
        }
    }

    public void evaluateFeasibilityForNode(Integer currentNode) {
        if (!Objects.equals(currentNode, lastNode)) {
            feasibleNodeIsFound = false;
            if (currentRoute.getActualOccupation() < vehicleCapacity) {
                for (Request request : requestsWhichBoardsInNode.get(currentNode)) {
                    if (lastNode == 0 && timeBetweenNodes.get(lastNode).get(currentNode) <= request.getPickupTimeWindowUpper()) {
                        feasibleNodes.add(currentNode);
                        feasibleNodeIsFound = true;
                        break;
                    }

                    if (!feasibleNodeIsFound && currentTime + timeBetweenNodes.get(lastNode).get(currentNode) >= request.getPickupTimeWindowLower() - timeWindows
                            && currentTime + timeBetweenNodes.get(lastNode).get(currentNode) <= request.getPickupTimeWindowUpper()) {
                        feasibleNodes.add(currentNode);
                        feasibleNodeIsFound = true;
                        break;
                    }
                }
            }

            if (!feasibleNodeIsFound && currentRoute.getActualOccupation() > 0) {
                for (Request request : requestsWhichLeavesInNode.get(currentNode)) {
                    if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                        feasibleNodes.add(currentNode);
                        break;
                    }
                }
            }
        }
    }

    public void addBestNode() {
        for (Map.Entry<Integer, Double> nrf : rankedList.getNodeRankingFunction().entrySet()) {
            Integer newNode = nrf.getKey();
            Double value = nrf.getValue();

            if (Objects.equals(max, value)) {
                if (lastNode == 0) {
                    for (Request request : requestsWhichBoardsInNode.get(newNode)) {
                        if (timeBetweenNodes.get(lastNode).get(newNode) <= request.getPickupTimeWindowUpper()) {
                            earliestTime.add(request.getPickupTimeWindowLower());
                        }
                    }
                    currentTime = Math.max(Collections.min(earliestTime) - timeBetweenNodes.get(lastNode).get(newNode), 0);
                    currentRoute.setDepartureTimeFromDepot(currentTime);
                    earliestTime.clear();
                }

                currentTime += timeBetweenNodes.get(lastNode).get(newNode);

                currentRoute.addVisitedNodes(newNode);
                lastNode = currentRoute.getLastNode();
                break;
            }
        }
        rankedList.clear();
        lastNode = currentRoute.getLastNode();
        //return currentTime;
    }

    public void landPassengers() {
        List<Request> listRequestAux = new LinkedList<>();
        listRequestAux.addAll(requestsWhichLeavesInNode.get(lastNode));

        for (Request request : listRequestAux) {

            if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                requestsWhichLeavesInNode.get(lastNode).remove((Request) request.clone());
                requestList.remove((Request) request.clone());
                log += "ENTREGA: " + currentTime + ": " + (Request) request.clone() + " ";
                try {
                    currentRoute.leavePassenger((Request) request.clone(), currentTime);
                } catch (Exception e) {
                    //System.out.print("solucao vigente: " + S + " R problema\n");
                    System.out.println("L Atend (" + currentRoute.getRequestAttendanceList().size() + ") " + currentRoute.getRequestAttendanceList());
                    System.out.println("L Visit (" + currentRoute.getNodesVisitationList().size() + ") " + currentRoute.getNodesVisitationList());
                    System.out.println("Qik (" + currentRoute.getVehicleOccupationWhenLeavesNode().size() + ") " + currentRoute.getVehicleOccupationWhenLeavesNode());
                    System.out.println("Tempoik (" + currentRoute.getTimeListTheVehicleLeavesTheNode().size() + ") " + currentRoute.getTimeListTheVehicleLeavesTheNode());
                    System.exit(-1);
                }
                //EXTRA
                log += "Q=" + currentRoute.getActualOccupation() + " ";
            }
        }
        listRequestAux.clear();
    }

    public void boardPassengers() {
        List<Request> listRequestAux = new LinkedList<>();
        listRequestAux.addAll(requestsWhichBoardsInNode.get(lastNode));

        for (Request request : listRequestAux) {
            if (currentRoute.getActualOccupation() < vehicleCapacity && currentTime >= request.getPickupTimeWindowLower() && currentTime <= request.getPickupTimeWindowUpper()) {
                requestsWhichBoardsInNode.get(lastNode).remove((Request) request.clone());
                log += "COLETA: " + currentTime + ": " + (Request) request.clone() + " ";
                currentRoute.boardPassenger((Request) request.clone(), currentTime);
                //EXTRA
                log += "Q =" + currentRoute.getActualOccupation() + " ";
            }
        }

        listRequestAux.clear();
    }

    public void boardPassengersWithRelaxationTime() {

        List<Request> listRequestAux = new LinkedList<>();
        listRequestAux.addAll(requestsWhichBoardsInNode.get(lastNode));

        long waitTime = timeWindows;
        long aux;

        for (Request request : listRequestAux) {
            if (currentRoute.getActualOccupation() < vehicleCapacity && currentTime + waitTime >= request.getPickupTimeWindowLower() && currentTime + waitTime <= request.getPickupTimeWindowUpper()) {
                aux = currentTime + waitTime - request.getPickupTimeWindowLower();
                currentTime = Math.min(currentTime + waitTime, request.getPickupTimeWindowLower());
                waitTime = aux;
                requestsWhichBoardsInNode.get(lastNode).remove((Request) request.clone());
                log += "COLETAw: " + currentTime + ": " + (Request) request.clone() + " ";
                currentRoute.boardPassenger((Request) request.clone(), currentTime);
                log += "Q=" + currentRoute.getActualOccupation() + " ";
            }
        }
    }

    public void removesUnfeasibleRequests() {
        List<Request> listRequestAux = new LinkedList<>();
        listRequestAux.clear();
        for (Integer key : requestsWhichBoardsInNode.keySet()) {
            listRequestAux.addAll(requestsWhichBoardsInNode.get(key));
            Integer i;
            Integer n2;
            for (i = 0, n2 = listRequestAux.size(); i < n2; i++) {
                Request request = listRequestAux.get(i);
                if (currentTime > request.getPickupTimeWindowUpper()) {
                    nonAttendedRequests.add((Request) request.clone());
                    requestList.remove((Request) request.clone());
                    requestsWhichBoardsInNode.get(key).remove((Request) request.clone());
                    requestsWhichLeavesInNode.get(request.getDestination()).remove((Request) request.clone());
                }
            }
            listRequestAux.clear();
        }
    }

    public void findRequestToAttend() {
        feasibleRequestIsFound = false;
        for (int i = 1; !feasibleRequestIsFound && i < numberOfNodes; i++) {//varre todas as solicitações para encontrar se tem alguma viável
            if (i != lastNode) {

                //Procura solicitação para embarcar
                if (currentRoute.getActualOccupation() < vehicleCapacity) {//se tiver lugar, ele tenta embarcar alguem no veículo
                    for (Request request : requestsWhichBoardsInNode.get(i)) {//percorre todos os nós menos o nó que acabou de ser adicionado (por causa da restrição)
                        if (currentTime + timeBetweenNodes.get(lastNode).get(i) >= request.getPickupTimeWindowLower() - timeWindows
                                && currentTime + timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper()) {
                            feasibleRequestIsFound = true;
                            break;
                        }
                    }
                }
                if (!feasibleRequestIsFound && currentRoute.getActualOccupation() > 0) {
                    for (Request request : requestsWhichLeavesInNode.get(i)) {
                        if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                            feasibleRequestIsFound = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void removeNonAttendeRequests() {
        List<Request> listRequestAux = new LinkedList<>();
        if (!feasibleRequestIsFound) {
            for (Integer key : requestsWhichBoardsInNode.keySet()) {//bloco de comando que coloca as solicitações que nn embarcaram no conjunto de inviáveis (U)
                listRequestAux.addAll(requestsWhichBoardsInNode.get(key));
                Integer i, n2;
                for (i = 0, n2 = listRequestAux.size(); i < n2; i++) {
                    Request request = listRequestAux.get(i);
                    nonAttendedRequests.add((Request) request.clone());
                    requestList.remove((Request) request.clone());
                    requestsWhichBoardsInNode.get(key).remove((Request) request.clone());
                    requestsWhichLeavesInNode.get(request.getDestination()).remove((Request) request.clone());
                }
                listRequestAux.clear();
            }
        }
    }

    public void finalizeRoute() {
        //-------------------------------------------------------------------------------------------------------------------------------------- 
        if (requestList.isEmpty()) {
            currentRoute.addVisitedNodes(0);
            currentTime += timeBetweenNodes.get(lastNode).get(0);
            solution.getSetOfRoutes().add(currentRoute);
        }
    }

    public void nonAttendedRequestListFeasibilityAvaliation() {
        if (!nonAttendedRequests.isEmpty() && hasAvaibleVehicle()) {
            boolean feasibleRequestFoundInNonAttendedList = false;
            List<Request> vechicleNonAttendedRequests = new ArrayList<>(nonAttendedRequests);
            for (Request request : vechicleNonAttendedRequests) {
                if (timeBetweenNodes.get(0).get(request.getOrigin()) <= request.getPickupTimeWindowUpper()) {
                    feasibleRequestFoundInNonAttendedList = true;
                }
            }
            if (feasibleRequestFoundInNonAttendedList) {
                requestList.addAll(nonAttendedRequests);
            }
        }
    }

    public ProblemSolution rebuildSolution(List<Integer> neighborhood, List<Request> requestList) {
//        requestList.clear();
//        requestList.addAll(listRequests);

        solution = new ProblemSolution();
        solution.setLinkedRouteList(neighborhood);
        String log = "";
        initializeFleetOfVehicles();
        vehicleIterator = setOfVehicles.iterator();
        nonAttendedRequests.clear();

        List<Request> auxP = new LinkedList<>(requestList);
        for (Request request : auxP) {
            if (!neighborhood.contains(request.getOrigin()) || !neighborhood.contains(request.getDestination()) || !(timeBetweenNodes.get(0).get(request.getOrigin()) <= request.getPickupTimeWindowUpper())) {
                nonAttendedRequests.add((Request) request.clone());
                requestList.remove((Request) request.clone());
            }
        }

//        System.out.println("conditions = " + !hasRequestToAttend() + "\t" + hasAvaibleVehicle() + "\t" + !neighborhood.isEmpty());
        while (!hasRequestToAttend() && hasAvaibleVehicle() && !neighborhood.isEmpty()) {

            requestsWhichBoardsInNode.clear();
            requestsWhichLeavesInNode.clear();
            List<Request> origem = new LinkedList<Request>();
            List<Request> destino = new LinkedList<Request>();
            for (int j = 0; j < numberOfNodes; j++) {

                for (Request request : requestList) {
                    if (request.getOrigin() == j) {
                        origem.add((Request) request.clone());
                    }
                    if (request.getDestination() == j) {
                        destino.add((Request) request.clone());
                    }
                }

                requestsWhichBoardsInNode.put(j, new LinkedList<Request>(origem));
                requestsWhichLeavesInNode.put(j, new LinkedList<Request>(destino));

                origem.clear();
                destino.clear();
            }

            currentRoute = new Route();
            currentVehicle = vehicleIterator.next();
            log += "\tROTA " + (currentVehicle + 1) + " ";

            currentRoute.addVisitedNodes(0);
            long currentTime = 0;

            Integer lastNode = currentRoute.getLastNode();
            Integer newNode;
            boolean encontrado;

            while (!requestList.isEmpty()) {

//				lastNode = R.getLastNode();
                newNode = -1;
                encontrado = false;

                //List<Integer> vizinhoCopia = new ArrayList<Integer>(vizinho);
                for (int k = 0; !encontrado && k < neighborhood.size(); k++) {
                    int i = neighborhood.get(k);

                    if (i != lastNode) {
                        if (currentRoute.getActualOccupation() < vehicleCapacity) {
                            for (Request request : requestsWhichBoardsInNode.get(i)) {
                                if (lastNode == 0 && timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                                    newNode = neighborhood.remove(k);
                                    encontrado = true;
                                    break;
                                }
                                //if( (currentTime + d.get(lastNode).get(i)) <= request.getPickupTimeWindowUpper()){
                                if (currentTime + timeBetweenNodes.get(lastNode).get(i) >= request.getPickupTimeWindowLower() - timeWindows
                                        && currentTime + timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                                    newNode = neighborhood.remove(k);
                                    encontrado = true;
                                    break;
                                }
                            }
                        }

                        /**
                         * E OS N�S DE ENTREGA? DEVEM SER VI�VEIS TAMB�M?*
                         */
                        if (!encontrado && currentRoute.getActualOccupation() > 0) {
                            for (Request request : requestsWhichLeavesInNode.get(i)) {
                                if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                                    newNode = neighborhood.remove(k);
                                    encontrado = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (newNode == -1) {
                    System.out.println("newNode Invalido");
                }
                //-------------------------------------------------------------------------------------------------
                //Step 6
                List<Long> EarliestTime = new ArrayList<>();

                if (lastNode == 0) {
                    //System.out.println("VIZINHO PROBLEMATICO "+vizinho);
                    for (Request request : requestsWhichBoardsInNode.get(newNode)) {
                        if (timeBetweenNodes.get(lastNode).get(newNode) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                            EarliestTime.add(request.getPickupTimeWindowLower());
                        }
                    }

                    currentTime = Math.max(Collections.min(EarliestTime) - timeBetweenNodes.get(lastNode).get(newNode), 0);
                    currentRoute.setDepartureTimeFromDepot(currentTime);

                    EarliestTime.clear();
                }

                currentTime += timeBetweenNodes.get(lastNode).get(newNode);

                currentRoute.addVisitedNodes(newNode);
                lastNode = currentRoute.getLastNode();

                List<Request> listRequestAux = new LinkedList<>(requestsWhichLeavesInNode.get(lastNode));

                for (Request request : listRequestAux) {

                    if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                        requestsWhichLeavesInNode.get(lastNode).remove((Request) request.clone());
                        requestList.remove((Request) request.clone());

                        //if(currentK == 1){
                        log += "ENTREGA: " + currentTime + ": " + (Request) request.clone() + " ";
                        //}

                        currentRoute.leavePassenger((Request) request.clone(), currentTime);

                        //EXTRA
                        log += "Q=" + currentRoute.getActualOccupation() + " ";
                    }
                }
                listRequestAux.clear();

                listRequestAux.addAll(requestsWhichBoardsInNode.get(lastNode));

                for (Request request : listRequestAux) {
                    if (currentRoute.getActualOccupation() < vehicleCapacity && currentTime >= request.getPickupTimeWindowLower() && currentTime <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                        requestsWhichBoardsInNode.get(lastNode).remove((Request) request.clone());
                        log += "COLETA: " + currentTime + ": " + (Request) request.clone() + " ";
                        currentRoute.boardPassenger((Request) request.clone(), currentTime);
                        log += "Q=" + currentRoute.getActualOccupation() + " ";
                    }
                }

                listRequestAux.clear();

                listRequestAux.addAll(requestsWhichBoardsInNode.get(lastNode));

                long waitTime = timeWindows;
                long aux;

                for (Request request : listRequestAux) {
                    if (currentRoute.getActualOccupation() < vehicleCapacity && currentTime + waitTime >= request.getPickupTimeWindowLower() && currentTime + waitTime <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {

                        aux = currentTime + waitTime - request.getPickupTimeWindowLower();
                        currentTime = Math.min(currentTime + waitTime, request.getPickupTimeWindowLower());
                        waitTime = aux;
                        requestsWhichBoardsInNode.get(lastNode).remove((Request) request.clone());
                        log += "COLETAw: " + currentTime + ": " + (Request) request.clone() + " ";
                        currentRoute.boardPassenger((Request) request.clone(), currentTime);
                        log += "Q=" + currentRoute.getActualOccupation() + " ";
                    }
                }

                listRequestAux.clear();

                for (Integer key : requestsWhichBoardsInNode.keySet()) {
                    listRequestAux.addAll(requestsWhichBoardsInNode.get(key));
                    for (Integer i = 0, k = listRequestAux.size(); i < k; i++) {
                        Request request = listRequestAux.get(i);
                        if (currentTime > request.getPickupTimeWindowUpper() || !neighborhood.contains(request.getOrigin()) || !neighborhood.contains(request.getDestination())) {
                            nonAttendedRequests.add((Request) request.clone());
                            requestList.remove((Request) request.clone());
                            requestsWhichBoardsInNode.get(key).remove((Request) request.clone());
                            requestsWhichLeavesInNode.get(request.getDestination()).remove((Request) request.clone());
                        }
                    }
                    listRequestAux.clear();

                }

                encontrado = false;
                for (int k = 0; !encontrado && k < neighborhood.size(); k++) {
                    int i = neighborhood.get(k);

                    if (i != lastNode) {

                        if (currentRoute.getActualOccupation() < vehicleCapacity) {
                            for (Request request : requestsWhichBoardsInNode.get(i)) {
                                if (currentTime + timeBetweenNodes.get(lastNode).get(i) >= request.getPickupTimeWindowLower() - timeWindows
                                        && currentTime + timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                                    encontrado = true;
                                    break;
                                }
                            }
                        }

                        if (!encontrado && currentRoute.getActualOccupation() > 0) {
                            for (Request request : requestsWhichLeavesInNode.get(i)) {
                                if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                                    encontrado = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!encontrado) {
                    for (Integer key : requestsWhichBoardsInNode.keySet()) {

                        listRequestAux.addAll(requestsWhichBoardsInNode.get(key));

                        for (Integer i = 0, k = listRequestAux.size(); i < k; i++) {
                            Request request = listRequestAux.get(i);

                            nonAttendedRequests.add((Request) request.clone());
                            requestList.remove((Request) request.clone());
                            requestsWhichBoardsInNode.get(key).remove((Request) request.clone());
                            requestsWhichLeavesInNode.get(request.getDestination()).remove((Request) request.clone());

                        }
                        listRequestAux.clear();

                    }
                }

                //Step 8
                if (requestList.isEmpty()) {
                    currentRoute.addVisitedNodes(0);
                    currentTime += timeBetweenNodes.get(lastNode).get(0);
                    solution.getSetOfRoutes().add(currentRoute);
                }

            }

            //Step 9
            if (!nonAttendedRequests.isEmpty() && vehicleIterator.hasNext()) {
                List<Request> auxU = new LinkedList<>(nonAttendedRequests);
                for (Request request : auxU) {
                    if (neighborhood.contains(request.getOrigin()) && neighborhood.contains(request.getDestination()) && timeBetweenNodes.get(0).get(request.getOrigin()) <= request.getPickupTimeWindowUpper()) {
                        requestList.add((Request) request.clone());
                        nonAttendedRequests.remove((Request) request.clone());
                    }
                }
            }
        }
        solution.setNonAttendedRequestsList(nonAttendedRequests);
        solution.evaluate(distanceBetweenNodes, vehicleCapacity, parameters);
        //evaluateAggregatedObjectiveFunctions(parameters, solution);
        solution.setLogger(log);

        return solution;
    }

    public ProblemSolution rebuildSolutionRefactoring(List<Integer> neighborhood, List<Request> requestList) {
        requestList.clear();
        requestList.addAll(requests);

        solution = new ProblemSolution();
        solution.setLinkedRouteList(neighborhood);
        String log = "";

        vehicleIterator = setOfVehicles.iterator();
        nonAttendedRequests.clear();

        List<Request> auxP = new LinkedList<>(requestList);
        for (Request request : auxP) {
            if (!neighborhood.contains(request.getOrigin()) || !neighborhood.contains(request.getDestination()) || !(timeBetweenNodes.get(0).get(request.getOrigin()) <= request.getPickupTimeWindowUpper())) {
                nonAttendedRequests.add((Request) request.clone());
                requestList.remove((Request) request.clone());
            }
        }

        while (!hasRequestToAttend() && hasAvaibleVehicle() && !neighborhood.isEmpty()) {
            requestsWhichBoardsInNode.clear();
            requestsWhichLeavesInNode.clear();
            List<Request> origem = new LinkedList<Request>();
            List<Request> destino = new LinkedList<Request>();
            for (int j = 0; j < numberOfNodes; j++) {

                for (Request request : requestList) {
                    if (request.getOrigin() == j) {
                        origem.add((Request) request.clone());
                    }
                    if (request.getDestination() == j) {
                        destino.add((Request) request.clone());
                    }
                }

                requestsWhichBoardsInNode.put(j, new LinkedList<Request>(origem));
                requestsWhichLeavesInNode.put(j, new LinkedList<Request>(destino));

                origem.clear();
                destino.clear();
            }

            currentRoute = new Route();
            currentVehicle = vehicleIterator.next();
            log += "\tROTA " + (currentVehicle + 1) + " ";

            currentRoute.addVisitedNodes(0);
            long currentTime = 0;

            Integer lastNode = currentRoute.getLastNode();
            Integer newNode;
            boolean encontrado;

            while (!requestList.isEmpty()) {

//				lastNode = R.getLastNode();
                newNode = -1;
                encontrado = false;

                //List<Integer> vizinhoCopia = new ArrayList<Integer>(vizinho);
                for (int k = 0; !encontrado && k < neighborhood.size(); k++) {
                    int i = neighborhood.get(k);

                    if (i != lastNode) {
                        if (currentRoute.getActualOccupation() < vehicleCapacity) {
                            for (Request request : requestsWhichBoardsInNode.get(i)) {
                                if (lastNode == 0 && timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                                    newNode = neighborhood.remove(k);
                                    encontrado = true;
                                    break;
                                }
                                //if( (currentTime + d.get(lastNode).get(i)) <= request.getPickupTimeWindowUpper()){
                                if (currentTime + timeBetweenNodes.get(lastNode).get(i) >= request.getPickupTimeWindowLower() - timeWindows
                                        && currentTime + timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                                    newNode = neighborhood.remove(k);
                                    encontrado = true;
                                    break;
                                }
                            }
                        }

                        /**
                         * E OS N�S DE ENTREGA? DEVEM SER VI�VEIS TAMB�M?*
                         */
                        if (!encontrado && currentRoute.getActualOccupation() > 0) {
                            for (Request request : requestsWhichLeavesInNode.get(i)) {
                                if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                                    newNode = neighborhood.remove(k);
                                    encontrado = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (newNode == -1) {
                    System.out.println("newNode Invalido");
                }
                //-------------------------------------------------------------------------------------------------
                //Step 6
                List<Long> EarliestTime = new ArrayList<>();

                if (lastNode == 0) {
                    //System.out.println("VIZINHO PROBLEMATICO "+vizinho);
                    for (Request request : requestsWhichBoardsInNode.get(newNode)) {
                        if (timeBetweenNodes.get(lastNode).get(newNode) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                            EarliestTime.add(request.getPickupTimeWindowLower());
                        }
                    }

                    currentTime = Math.max(Collections.min(EarliestTime) - timeBetweenNodes.get(lastNode).get(newNode), 0);
                    currentRoute.setDepartureTimeFromDepot(currentTime);

                    EarliestTime.clear();
                }

                currentTime += timeBetweenNodes.get(lastNode).get(newNode);

                currentRoute.addVisitedNodes(newNode);
                lastNode = currentRoute.getLastNode();

                List<Request> listRequestAux = new LinkedList<>(requestsWhichLeavesInNode.get(lastNode));

                for (Request request : listRequestAux) {

                    if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                        requestsWhichLeavesInNode.get(lastNode).remove((Request) request.clone());
                        requestList.remove((Request) request.clone());

                        //if(currentK == 1){
                        log += "ENTREGA: " + currentTime + ": " + (Request) request.clone() + " ";
                        //}

                        currentRoute.leavePassenger((Request) request.clone(), currentTime);

                        //EXTRA
                        log += "Q=" + currentRoute.getActualOccupation() + " ";
                    }
                }
                listRequestAux.clear();

                listRequestAux.addAll(requestsWhichBoardsInNode.get(lastNode));

                for (Request request : listRequestAux) {
                    if (currentRoute.getActualOccupation() < vehicleCapacity && currentTime >= request.getPickupTimeWindowLower() && currentTime <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                        requestsWhichBoardsInNode.get(lastNode).remove((Request) request.clone());
                        log += "COLETA: " + currentTime + ": " + (Request) request.clone() + " ";
                        currentRoute.boardPassenger((Request) request.clone(), currentTime);
                        log += "Q=" + currentRoute.getActualOccupation() + " ";
                    }
                }

                listRequestAux.clear();

                listRequestAux.addAll(requestsWhichBoardsInNode.get(lastNode));

                long waitTime = timeWindows;
                long aux;

                for (Request request : listRequestAux) {
                    if (currentRoute.getActualOccupation() < vehicleCapacity && currentTime + waitTime >= request.getPickupTimeWindowLower() && currentTime + waitTime <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {

                        aux = currentTime + waitTime - request.getPickupTimeWindowLower();
                        currentTime = Math.min(currentTime + waitTime, request.getPickupTimeWindowLower());
                        waitTime = aux;
                        requestsWhichBoardsInNode.get(lastNode).remove((Request) request.clone());
                        log += "COLETAw: " + currentTime + ": " + (Request) request.clone() + " ";
                        currentRoute.boardPassenger((Request) request.clone(), currentTime);
                        log += "Q=" + currentRoute.getActualOccupation() + " ";
                    }
                }

                listRequestAux.clear();

                for (Integer key : requestsWhichBoardsInNode.keySet()) {
                    listRequestAux.addAll(requestsWhichBoardsInNode.get(key));
                    for (Integer i = 0, k = listRequestAux.size(); i < k; i++) {
                        Request request = listRequestAux.get(i);
                        if (currentTime > request.getPickupTimeWindowUpper() || !neighborhood.contains(request.getOrigin()) || !neighborhood.contains(request.getDestination())) {
                            nonAttendedRequests.add((Request) request.clone());
                            requestList.remove((Request) request.clone());
                            requestsWhichBoardsInNode.get(key).remove((Request) request.clone());
                            requestsWhichLeavesInNode.get(request.getDestination()).remove((Request) request.clone());
                        }
                    }
                    listRequestAux.clear();

                }

                encontrado = false;
                for (int k = 0; !encontrado && k < neighborhood.size(); k++) {
                    int i = neighborhood.get(k);

                    if (i != lastNode) {

                        if (currentRoute.getActualOccupation() < vehicleCapacity) {
                            for (Request request : requestsWhichBoardsInNode.get(i)) {
                                if (currentTime + timeBetweenNodes.get(lastNode).get(i) >= request.getPickupTimeWindowLower() - timeWindows
                                        && currentTime + timeBetweenNodes.get(lastNode).get(i) <= request.getPickupTimeWindowUpper() && neighborhood.contains(request.getDestination())) {
                                    encontrado = true;
                                    break;
                                }
                            }
                        }

                        if (!encontrado && currentRoute.getActualOccupation() > 0) {
                            for (Request request : requestsWhichLeavesInNode.get(i)) {
                                if (!requestsWhichBoardsInNode.get(request.getOrigin()).contains(request)) {
                                    encontrado = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!encontrado) {
                    for (Integer key : requestsWhichBoardsInNode.keySet()) {

                        listRequestAux.addAll(requestsWhichBoardsInNode.get(key));

                        for (Integer i = 0, k = listRequestAux.size(); i < k; i++) {
                            Request request = listRequestAux.get(i);

                            nonAttendedRequests.add((Request) request.clone());
                            requestList.remove((Request) request.clone());
                            requestsWhichBoardsInNode.get(key).remove((Request) request.clone());
                            requestsWhichLeavesInNode.get(request.getDestination()).remove((Request) request.clone());

                        }
                        listRequestAux.clear();

                    }
                }

                //Step 8
                if (requestList.isEmpty()) {
                    currentRoute.addVisitedNodes(0);
                    currentTime += timeBetweenNodes.get(lastNode).get(0);
                    solution.getSetOfRoutes().add(currentRoute);
                }

            }

            //Step 9
            if (!nonAttendedRequests.isEmpty() && vehicleIterator.hasNext()) {
                List<Request> auxU = new LinkedList<>(nonAttendedRequests);
                for (Request request : auxU) {
                    if (neighborhood.contains(request.getOrigin()) && neighborhood.contains(request.getDestination()) && timeBetweenNodes.get(0).get(request.getOrigin()) <= request.getPickupTimeWindowUpper()) {
                        requestList.add((Request) request.clone());
                        nonAttendedRequests.remove((Request) request.clone());
                    }
                }
            }
        }
        solution.setNonAttendedRequestsList(nonAttendedRequests);
        solution.evaluate(distanceBetweenNodes, vehicleCapacity);
        //evaluateAggregatedObjectiveFunctions(parameters, solution);
        solution.setLogger(log);

        return solution;
    }

    public void startSeed(){
         seed = 256344;
    }
    
    public ProblemSolution buildRandomSolution() {
        
       
        solution = new ProblemSolution();
        solution = buildGreedySolution();

        List<Integer> sequence = new ArrayList<>();
        sequence.addAll(solution.getLinkedRouteList());
        for (int i = 0; i < 3; i++) {
            seed++;
            Random rnd = new Random(i + 1);
            Random p1 = new Random(i + seed);
            Random p2 = new Random(seed * i);
            int index1, index2;
            index1 = p1.nextInt(solution.getLinkedRouteList().size());
            do {
                index2 = p2.nextInt(solution.getLinkedRouteList().size());
            } while (Objects.equals(sequence.get(index1), sequence.get(index2)));

            Collections.swap(sequence, index1, index2);
        }
        //solution = rebuildSolution(sequence, requests);
        solution = rebuildSolution(sequence, getRequestListCopy());

        return solution;
    }

    public List<Request> getRequestListCopy() {
        List<Request> requestList = new ArrayList<>();
        for (Request request : requests) {
            requestList.add((Request) request.clone());
        }
        return requestList;
    }
}
