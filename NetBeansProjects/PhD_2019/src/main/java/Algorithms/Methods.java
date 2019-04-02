/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import static Algorithms.Algorithms.*;
import InstanceReader.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import ProblemRepresentation.*;
import static java.util.stream.Collectors.toSet;
import static Algorithms.EvolutionaryAlgorithms.initializePopulation;
import static Algorithms.Algorithms.*;
import InstanceReader.ReadDataInExcelFile;
import java.io.IOException;
import jxl.read.biff.BiffException;

/**
 *
 * @author Renan
 */
public class Methods {

    public static Integer readProblemData(String instanceName, String nodesTable, String adjacenciesTable, List<Request> listOfRequests,
            List<List<Long>> distanceBetweenNodes, List<List<Long>> timeBetweenNodes, Set<Integer> setOfOrigins,
            Set<Integer> setOfDestinations, Map<Integer, List<Request>> requestsWichBoardsInNode, Map<Integer, List<Request>> requestsWichLeavesInNode,
            Set<Integer> setOfNodes, Integer numberOfNodes, List<Integer> loadIndex) {

        listOfRequests.addAll(new RequestDAO(instanceName).getListOfRequest());
        setOfNodes.addAll(new NodeDAO(nodesTable).getSetOfNodes());
        distanceBetweenNodes.addAll(new AdjacenciesDAO(adjacenciesTable, nodesTable).getAdjacenciesListOfDistances());
        timeBetweenNodes.addAll(new AdjacenciesDAO(adjacenciesTable, nodesTable).getAdjacenciesListOfTimes());
        numberOfNodes = new AdjacenciesDAO(adjacenciesTable, nodesTable).getNumberOfNodes();

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
                loadIndex.add(requestsWichBoardsInNode.get(i).size() - requestsWichLeavesInNode.get(i).size());
            } else {
                loadIndex.add(0);
            }
        }

        // timeBetweenNodes.forEach(System.out::println);
        return numberOfNodes;
    }

    public static Integer readProblemUsingExcelData(String filePath, String instanceName, String nodesTable, String adjacenciesTable, List<Request> listOfRequests,
            List<List<Long>> distanceBetweenNodes, List<List<Long>> timeBetweenNodes, Set<Integer> setOfOrigins,
            Set<Integer> setOfDestinations, Map<Integer, List<Request>> requestsWichBoardsInNode, Map<Integer, List<Request>> requestsWichLeavesInNode,
            Set<Integer> setOfNodes, Integer numberOfNodes, List<Integer> loadIndex) throws IOException, BiffException {

        listOfRequests.addAll(new ReadDataInExcelFile(filePath, instanceName, nodesTable, adjacenciesTable)
                .getRequests());
        setOfNodes.addAll(new ReadDataInExcelFile(filePath, instanceName, nodesTable, adjacenciesTable)
                .getSetOfNodes());
        distanceBetweenNodes.addAll(new ReadDataInExcelFile(filePath, instanceName, nodesTable, adjacenciesTable)
                .getAdjacenciesListOfDistances());
        timeBetweenNodes.addAll(new ReadDataInExcelFile(filePath, instanceName, nodesTable, adjacenciesTable)
                .getAdjacenciesListOfTimes());
        numberOfNodes = new ReadDataInExcelFile(filePath, instanceName, nodesTable, adjacenciesTable).getNumberOfNodes();

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
                loadIndex.add(requestsWichBoardsInNode.get(i).size() - requestsWichLeavesInNode.get(i).size());
            } else {
                loadIndex.add(0);
            }
        }

        return numberOfNodes;
    }

    public static void separateOriginFromDestination(List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, Integer n, List<Request> P) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        U.clear();
        Pin.clear();
        Pout.clear();

        List<Request> origem = new LinkedList<Request>();
        List<Request> destino = new LinkedList<Request>();

        for (int j = 0; j < n; j++) {
            for (Request request : P) {
                if ((request.getOrigin() == j || request.getDestination() == j)) {
                    if (request.getOrigin() == j) {
                        origem.add((Request) request.clone());
                    } else {
                        destino.add((Request) request.clone());
                    }
                }
            }

            Pin.put(j, new LinkedList<Request>(origem));
            Pout.put(j, new LinkedList<Request>(destino));

            origem.clear();
            destino.clear();
        }
    }

    public static void findFeasibleNodes(Integer numberOfNodes, Integer lastNode, boolean feasibleNodeIsFound,
            Integer vehicleCapacity, Route route, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Set<Integer> feasibleNode, List<List<Long>> timeBetweenNodes,
            Long currentTime, Long timeWindows) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        for (int i = 1; i < numberOfNodes; i++) {
            feasibilityConstraints(i, lastNode, feasibleNodeIsFound, route, vehicleCapacity, requestsWhichBoardsInNode,
                    requestsWhichLeavesInNode, feasibleNode, timeBetweenNodes, currentTime, timeWindows);
        }
    }

    public static void CalculaCRL(Set<Integer> FeasibleNode, Map<Integer, Double> CRL, List<List<Long>> c, Integer lastNode) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        Double min, max;
        for (Integer i : FeasibleNode) {
            CRL.put(i, (double) c.get(lastNode).get(i));
        }

        //Normalizacao
        min = Collections.min(CRL.values());
        max = Collections.max(CRL.values());

        if (min != max) {
            for (Integer i : FeasibleNode) {
                CRL.put(i, ((max - CRL.get(i)) / (max - min)));
            }
        } else {
            for (Integer i : FeasibleNode) {
                CRL.put(i, 1.0);
            }
        }

    }

    public static void CalculaNRL(Set<Integer> FeasibleNode, Map<Integer, Double> NRL, List<Integer> m, Integer lastNode) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        Double min, max;
        for (Integer i : FeasibleNode) {
            NRL.put(i, (double) m.get(i));
        }

        //Normalizacao
        min = Collections.min(NRL.values());
        max = Collections.max(NRL.values());

        if (min != max) {
            for (Integer i : FeasibleNode) {
                NRL.put(i, ((NRL.get(i) - min) / (max - min)));
            }
        } else {
            for (Integer i : FeasibleNode) {
                NRL.put(i, 1.0);
            }
        }
    }

    public static void CalculaDRL(Set<Integer> FeasibleNode, Map<Integer, Double> DRL, Map<Integer, List<Request>> Pout,
            Integer lastNode, List<List<Long>> d, List<Long> EarliestTime) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        Double min, max;
        for (Integer i : FeasibleNode) {
            if (Pout.get(i).size() > 0) {
                for (Request request : Pout.get(i)) {
                    EarliestTime.add(request.getDeliveryTimeWindowLower());
                }
                DRL.put(i, (double) (Collections.min(EarliestTime) + d.get(lastNode).get(i)));
                EarliestTime.clear();
            }
        }

        //Normalizacao
        if (DRL.size() > 0) {
            min = Collections.min(DRL.values());
            max = Collections.max(DRL.values());

            if (min != max) {
                for (Integer i : DRL.keySet()) {
                    DRL.put(i, ((max - DRL.get(i)) / (max - min)));
                }
            } else {
                for (Integer i : DRL.keySet()) {
                    DRL.put(i, 1.0);
                }
            }
        }

        for (Integer i : FeasibleNode) {
            if (!DRL.containsKey(i)) {
                DRL.put(i, 0.0);
            }
        }
    }

    public static void CalculaTRL(Set<Integer> FeasibleNode, Map<Integer, Double> TRL, Map<Integer, List<Request>> Pin,
            Integer lastNode, List<List<Long>> d, List<Long> EarliestTime) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        double min, max;
        for (Integer i : FeasibleNode) {
            if (Pin.get(i).size() > 0) {
                for (Request request : Pin.get(i)) {
                    EarliestTime.add(request.getPickupTimeWIndowLower());
                }

                TRL.put(i, (double) (Collections.min(EarliestTime) + d.get(lastNode).get(i)));

                EarliestTime.clear();
            }
        }

        //Normalizacao
        if (TRL.size() > 0) {
            min = Collections.min(TRL.values());
            max = Collections.max(TRL.values());

            if (min != max) {
                for (Integer i : TRL.keySet()) {
                    TRL.put(i, ((max - TRL.get(i)) / (max - min)));
                }
            } else {
                for (Integer i : TRL.keySet()) {
                    TRL.put(i, 1.0);
                }
            }
        }

        for (Integer i : FeasibleNode) {
            if (!TRL.containsKey(i)) {
                TRL.put(i, 0.0);
            }
        }
    }

    public static void CalculaListaSemNosViaveis(Map<Integer, Double> lista, Set<Integer> FeasibleNode) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        for (Integer i : FeasibleNode) {
            lista.put(i, 1.0);
        }
    }

    public static void CalculaNRF(Map<Integer, Double> NRF, Map<Integer, Double> CRL, Map<Integer, Double> NRL,
            Map<Integer, Double> DRL, Map<Integer, Double> TRL, Double alphaD, Double alphaP,
            Double alphaV, Double alphaT, Set<Integer> FeasibleNode) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        for (Integer i : FeasibleNode) {
            NRF.put(i, (alphaD * CRL.get(i) + alphaP * NRL.get(i))
                    + (alphaV * DRL.get(i) + alphaT * TRL.get(i)));
        }
    }

    public static long AdicionaNo(Map<Integer, Double> NRF, Map<Integer, Double> CRL, Map<Integer, Double> NRL, Map<Integer, Double> DRL,
            Map<Integer, Double> TRL, Double max, Integer lastNode, Map<Integer, List<Request>> Pin,
            List<List<Long>> d, List<Long> EarliestTime, Long currentTime, Route R) {
        //-------------------------------------------------------------------------------------------------------------------------------------- 
        for (Map.Entry<Integer, Double> e : NRF.entrySet()) {
            Integer newNode = e.getKey();
            Double value = e.getValue();

            if (Objects.equals(max, value)) {
                if (lastNode == 0) {
                    for (Request request : Pin.get(newNode)) {
                        if (d.get(lastNode).get(newNode) <= request.getPickupTimeWindowUpper()) {
                            EarliestTime.add(request.getPickupTimeWIndowLower());
                        }
                    }
                    currentTime = Math.max(Collections.min(EarliestTime) - d.get(lastNode).get(newNode), 0);
                    R.setDepartureTimeFromDepot(currentTime);
                    EarliestTime.clear();
                }

                currentTime += d.get(lastNode).get(newNode);

                R.addVisitedNodes(newNode);
                lastNode = R.getLastNode();
                break;
            }
        }
        CRL.clear();
        NRL.clear();
        DRL.clear();
        TRL.clear();
        NRF.clear();
        return currentTime;
    }

    public static void Desembarca(Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, Integer lastNode, Long currentTime,
            List<Request> P, List<Request> listRequestAux, Route R, String log) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        listRequestAux.addAll(Pout.get(lastNode));//percorre todas as solicitações que desembarcam no último nó

        for (Request request : listRequestAux) {

            if (!Pin.get(request.getOrigin()).contains(request)) {
                Pout.get(lastNode).remove((Request) request.clone());
                P.remove((Request) request.clone());
                log += "ENTREGA: " + currentTime + ": " + (Request) request.clone() + " ";
                try {
                    R.leavePassenger((Request) request.clone(), currentTime);
                } catch (Exception e) {
                    //System.out.print("solucao vigente: " + S + " R problema\n");
                    System.out.println("L Atend (" + R.getRequestAttendanceList().size() + ") " + R.getRequestAttendanceList());
                    System.out.println("L Visit (" + R.getNodesVisitationList().size() + ") " + R.getNodesVisitationList());
                    System.out.println("Qik (" + R.getVehicleOccupationWhenLeavesNode().size() + ") " + R.getVehicleOccupationWhenLeavesNode());
                    System.out.println("Tempoik (" + R.getTimeListTheVehicleLeavesTheNode().size() + ") " + R.getTimeListTheVehicleLeavesTheNode());
                    System.exit(-1);
                }
                //EXTRA
                log += "Q=" + R.getActualOccupation() + " ";
            }
        }
        listRequestAux.clear();
    }

    public static void Embarca(Map<Integer, List<Request>> Pin, Integer lastNode, Long currentTime,
            List<Request> P, List<Request> listRequestAux, Route R, String log, Integer Qmax) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        listRequestAux.addAll(Pin.get(lastNode));

        for (Request request : listRequestAux) {
            if (R.getActualOccupation() < Qmax && currentTime >= request.getPickupTimeWIndowLower() && currentTime <= request.getPickupTimeWindowUpper()) {
                Pin.get(lastNode).remove((Request) request.clone());
                log += "COLETA: " + currentTime + ": " + (Request) request.clone() + " ";
                R.boardPassenger((Request) request.clone(), currentTime);
                //EXTRA
                log += "Q =" + R.getActualOccupation() + " ";
            }
        }

        listRequestAux.clear();
    }

    public static long EmbarcaRelaxacao(Map<Integer, List<Request>> Pin, Integer lastNode, Long currentTime, List<Request> P,
            List<Request> listRequestAux, Route R, String log, Integer Qmax, Long TimeWindows) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        listRequestAux.addAll(Pin.get(lastNode));

        long waitTime = TimeWindows;
        long aux;

        for (Request request : listRequestAux) {
            if (R.getActualOccupation() < Qmax && currentTime + waitTime >= request.getPickupTimeWIndowLower() && currentTime + waitTime <= request.getPickupTimeWindowUpper()) {

                aux = currentTime + waitTime - request.getPickupTimeWIndowLower();
                currentTime = Math.min(currentTime + waitTime, request.getPickupTimeWIndowLower());
                waitTime = aux;

                Pin.get(lastNode).remove((Request) request.clone());

                log += "COLETAw: " + currentTime + ": " + (Request) request.clone() + " ";

                R.boardPassenger((Request) request.clone(), currentTime);

                //EXTRA
                log += "Q=" + R.getActualOccupation() + " ";
            }
        }
        return currentTime;
    }

    public static void RetiraSolicitacoesInviaveis(Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<Request> listRequestAux,
            Long currentTime, List<Request> P, List<Request> U) {
        //--------------------------------------------------------------------------------------------------------------------------------------
        listRequestAux.clear();
        for (Integer key : Pin.keySet()) {
            listRequestAux.addAll(Pin.get(key));
            Integer i;
            Integer n2;
            for (i = 0, n2 = listRequestAux.size(); i < n2; i++) {//percorre todas as requisições que embarcariam em lastNode
                Request request = listRequestAux.get(i);
                if (currentTime > request.getPickupTimeWindowUpper()) {
                    U.add((Request) request.clone());
                    P.remove((Request) request.clone());
                    Pin.get(key).remove((Request) request.clone());
                    Pout.get(request.getDestination()).remove((Request) request.clone());
                }
            }
            listRequestAux.clear();
        }
    }

    public static boolean ProcuraSolicitacaoParaAtender(Route R, Integer Qmax, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            Long currentTime, Integer n, List<List<Long>> d, Integer lastNode,
            Long TimeWindows, boolean encontrado) {
        //-------------------------------------------------------------------------------------------------------------------------------------- 
        encontrado = false;
        for (int i = 1; !encontrado && i < n; i++) {//varre todas as solicitações para encontrar se tem alguma viável
            if (i != lastNode) {

                //Procura solicitação para embarcar
                if (R.getActualOccupation() < Qmax) {//se tiver lugar, ele tenta embarcar alguem no veículo
                    for (Request request : Pin.get(i)) {//percorre todos os nós menos o nó que acabou de ser adicionado (por causa da restrição)
                        if (currentTime + d.get(lastNode).get(i) >= request.getPickupTimeWIndowLower() - TimeWindows
                                && currentTime + d.get(lastNode).get(i) <= request.getPickupTimeWindowUpper()) {
                            encontrado = true;
                            break;
                        }
                    }
                }
                //Procura solicitação para desembarcar
                if (!encontrado && R.getActualOccupation() > 0) {
                    for (Request request : Pout.get(i)) {
                        if (!Pin.get(request.getOrigin()).contains(request)) {
                            encontrado = true;
                            break;
                        }
                    }
                }
            }
        }
        return encontrado;
    }

    public static void RetiraSolicitacaoNaoSeraAtendida(boolean encontrado, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<Request> listRequestAux,
            Long currentTime, List<Request> P, List<Request> U) {
        //--------------------------------------------------------------------------------------------------------------------------------------         
        if (!encontrado) {
            for (Integer key : Pin.keySet()) {//bloco de comando que coloca as solicitações que nn embarcaram no conjunto de inviáveis (U)
                listRequestAux.addAll(Pin.get(key));
                Integer i, n2;
                for (i = 0, n2 = listRequestAux.size(); i < n2; i++) {
                    Request request = listRequestAux.get(i);
                    U.add((Request) request.clone());
                    P.remove((Request) request.clone());
                    Pin.get(key).remove((Request) request.clone());
                    Pout.get(request.getDestination()).remove((Request) request.clone());
                }
                listRequestAux.clear();
            }
        }
    }

    public static long FinalizaRota(List<Request> P, Route R, Long currentTime, Integer lastNode, List<List<Long>> d, ProblemSolution S) {
        //-------------------------------------------------------------------------------------------------------------------------------------- 
        if (P.isEmpty()) {
            R.addVisitedNodes(0);
            currentTime += d.get(lastNode).get(0);
            S.getSetOfRoutes().add(R);
        }
        return currentTime;
    }

    public static void AnaliseSolicitacoesViaveisEmU(List<Request> U, List<Request> P, Iterator itK, List<List<Long>> d) {
        //-------------------------------------------------------------------------------------------------------------------------------------- 
        if (!U.isEmpty() && itK.hasNext()) {//analise se há solicitações que possam ser atendidas com um novo veículo começando na origem
            boolean encontrado = false;
            List<Request> auxU = new ArrayList<Request>(U);
            for (Request request : auxU) {
                if (d.get(0).get(request.getOrigin()) <= request.getPickupTimeWindowUpper()) {
                    encontrado = true;
                }
            }
            if (encontrado) {
                P.addAll(U);
            }
        }
    }

    public static void feasibilityConstraints(Integer i, Integer lastNode, boolean encontrado, Route R, Integer Qmax, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Set<Integer> FeasibleNode, List<List<Long>> d, Long currentTime, Long timeWindows) {
        if (i != lastNode) {
            encontrado = false;
            if (R.getActualOccupation() < Qmax) {
                for (Request request : Pin.get(i)) {//retorna uma lista com as requisições que embarcam em i
                    if (lastNode == 0 && d.get(lastNode).get(i) <= request.getPickupTimeWindowUpper()) { //d.get(lastNode).get(i) � o tempo de chegar de lastNode ate o no i?
                        FeasibleNode.add(i);
                        encontrado = true;
                        break;
                    }
                    //para lastNode que não seja a origem - faz cair dentro da janela de tempo de pelo menos uma requisição
                    if (!encontrado && currentTime + d.get(lastNode).get(i) >= request.getPickupTimeWIndowLower() - timeWindows
                            && currentTime + d.get(lastNode).get(i) <= request.getPickupTimeWindowUpper()) {
                        FeasibleNode.add(i);
                        encontrado = true;
                        break;
                    }
                }
            }

            /**
             * E OS NÓS DE ENTREGA? DEVEM SER VIÁVEIS TAMBÉM?*
             */
            if (!encontrado && R.getActualOccupation() > 0) {//ou seja, se não encontrou um nó viavel e há pessoas dentro do veículo
                for (Request request : Pout.get(i)) {//retorna uma lista com as requisições que desembarcam em i
                    if (!Pin.get(request.getOrigin()).contains(request)) {
                        FeasibleNode.add(i);
                        break;
                    }
                }
            }
        }
    }

    public static void inicializePopulation(int reducedDimension, List<ProblemSolution> Pop, Integer TamPop, List<Request> listRequests,
            Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K,
            List<Request> U, List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
            Long TimeWindows, Long currentTime, Integer lastNode) {

        for (int i = 0; i < TamPop; i++) {
            ProblemSolution S = new ProblemSolution();
            S.setSolution(GeraSolucaoAleatoria(reducedDimension, Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
            //S.setSolution(PerturbacaoSemente(parameters, S, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
            Pop.add(S);
        }

        for (int i = 0; i < TamPop; i++) {
            ProblemSolution solucao = new ProblemSolution(Pop.get(i));
        }
        //Coloquei a linha de baixo, que estava no codigo principal dos algoritmos multi
        Inicializa(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode);
    }

    public static void inicializeRandomPopulation(List<Double> parameters, int reducedDimension, List<ProblemSolution> Pop, Integer TamPop, List<Request> listRequests,
            Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K,
            List<Request> U, List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
            Long TimeWindows, Long currentTime, Integer lastNode) {

        ProblemSolution S = new ProblemSolution(reducedDimension);
        S.setSolution(GeraSolucaoAleatoria(reducedDimension, Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
        for (int i = 0; i < TamPop; i++) {
            ProblemSolution S2 = new ProblemSolution(reducedDimension);
            S2.setSolution(PerturbacaoSemente(i, reducedDimension, parameters, S, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
            Pop.add(S2);
        }
//        for (int i = 0; i < TamPop; i++) {
//            ProblemSolution S = new ProblemSolution();
//            S.setSolution(GeraSolucaoAleatoria(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
//            ProblemSolution S2 = new ProblemSolution();
//            S2.setSolution(PerturbacaoSemente(i, parameters, S, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
//            Pop.add(S2);
//        }
//        for (int i = 0; i < TamPop; i++) {
//            ProblemSolution solucao = new ProblemSolution(Pop.get(i));
//        }
//        //Coloquei a linha de baixo, que estava no codigo principal dos algoritmos multi
//        Inicializa(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode);
    }

    public static void Inicializa(List<ProblemSolution> Pop, int TamPop, List<Request> listRequests, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P, List<Integer> m, List<List<Long>> d,
            List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        ProblemSolution s0 = new ProblemSolution();
        for (int i = 0; i < TamPop; i++) {
            s0.setSolution(geraPesos(i, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
            //Pop.add(s0);
            Pop.get(i).setSolution(s0);
            //System.out.println("s0 = " + s0);

        }
    }

//    public static void initializePopulation(List<Solution> Pop, Integer TamPop, List<Request> listRequests, Map<Integer, List<Request>> Pin,
//            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U,
//            List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
//            Long TimeWindows, Long currentTime, Integer lastNode) {
//
//        for (int i = 0; i < TamPop; i++) {
//            ProblemSolution S = new ProblemSolution();
//            ProblemSolution S_linha = new ProblemSolution();            
//            S.setSolution(GeraSolucaoAleatoria(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
//            S_linha.setSolution(perturbation(S, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
//            Pop.add(S);
//        }
//
//        for (int i = 0; i < TamPop; i++) {
//            ProblemSolution solucao = new ProblemSolution(Pop.get(i));
//        }
//        //Coloquei a linha de baixo, que estava no codigo principal dos algoritmos multi
//        //initializePopulation(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode);
//    }
    public static void InicializaPopulacaoPerturbacao(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop, Integer TamPop, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U,
            List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
            Long TimeWindows, Long currentTime, Integer lastNode) {

        for (int i = 0; i < TamPop; i++) {
            ProblemSolution S = new ProblemSolution();
            ProblemSolution S_linha = new ProblemSolution();
            S.setSolution(GeraSolucaoAleatoria(reducedDimension, Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
            //S_linha.setSolution(perturbation(S, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
            S_linha.setSolution(PerturbacaoSemente(i, reducedDimension, parameters, S, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
            Pop.add(S_linha);
        }
    }

    public static void InicializaPopulacaoGulosa(List<ProblemSolution> Pop, Integer TamPop, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U,
            List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
            Long TimeWindows, Long currentTime, Integer lastNode) {

        for (int i = 0; i < TamPop; i++) {
            ProblemSolution S = new ProblemSolution();
            Random rnd = new Random();
            double x, y, z, w;
            do {
                x = rnd.nextDouble();
                y = rnd.nextDouble();
                z = rnd.nextDouble();
                w = 1 - x - y - z;
            } while (x + y + z > 1);
            //S.setSolution(greedyConstructive(x, y, z, w, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
            //System.out.println("SolucaoGulosaAleatoria = "+ S);
            //S.setSolution(GeraSolucaoAleatoria(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode));
            //Pop.add(S);

        }
        //for (int i = 0; i < TamPop; i++) {
        //  ProblemSolution solucao = new ProblemSolution(Pop.get(i));
        //}
    }

    public static ProblemSolution GeraSolucaoAleatoria(int reducedDimension, List<ProblemSolution> Pop, Integer TamPop, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U,
            List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
            Long TimeWindows, Long currentTime, Integer lastNode) {

        P.clear();
        U.clear();
        P.addAll(listRequests);

        //Step 1
        ProblemSolution solution = new ProblemSolution();
        String log = "";

        int currentK;
        Map<Integer, Double> CRL = new HashMap<>(n), // Cost Rank List
                NRL = new HashMap<>(n), // Number of Passengers Rank List
                DRL = new HashMap<>(n), // Delivery time-window Rank List
                TRL = new HashMap<>(n), // Time-window Rank List
                NRF = new HashMap<>(n);	// Time-window Rank List

        Iterator<Integer> itK = K.iterator();
        U.clear();
        while (!P.isEmpty() && itK.hasNext()) {

            separateOriginFromDestination(U, Pin, Pout, n, P);

            //Step 2
            Route R = new Route();
            currentK = itK.next();
            log += "\tGROTA " + (currentK + 1) + " ";

            //Step 3
            R.addVisitedNodes(0);
            currentTime = (long) 0;
            double max, min;
            lastNode = R.getLastNode();

            boolean encontrado;

            while (!P.isEmpty()) {
                encontrado = false;
                m.clear();
                for (int i = 0; i < n; i++) {
                    m.add(Pin.get(i).size() - Pout.get(i).size());
                }

                //Step 4
                Set<Integer> FeasibleNode = new HashSet<>();
                List<Long> EarliestTime = new ArrayList<>();

                findFeasibleNodes(n, lastNode, encontrado, Qmax, R, Pin, Pout, FeasibleNode, d, currentTime, TimeWindows);

                //System.out.println("FEASIBLE NODES = "+ FeasibleNode);			
                if (FeasibleNode.size() > 1) {
                    //Step 4.1
                    CalculaCRL(FeasibleNode, CRL, c, lastNode);
                    //Step 4.2
                    CalculaNRL(FeasibleNode, NRL, m, lastNode);
                    //Step 4.3
                    CalculaDRL(FeasibleNode, DRL, Pout, lastNode, d, EarliestTime);
                    //Step 4.4
                    CalculaTRL(FeasibleNode, TRL, Pin, lastNode, d, EarliestTime);
                } else {
                    //Step 4.1
                    CalculaListaSemNosViaveis(CRL, FeasibleNode);
                    //Step 4.2
                    CalculaListaSemNosViaveis(NRL, FeasibleNode);
                    //Step 4.3
                    CalculaListaSemNosViaveis(DRL, FeasibleNode);
                    //Step 4.4
                    CalculaListaSemNosViaveis(TRL, FeasibleNode);
                }

                //Step 5 
                //CalculaNRF(NRF,CRL,NRL,DRL,TRL,alphaD,alphaP,alphaV,alphaT,FeasibleNode);
                Random gerador = new Random(19700621);
                for (Integer i : FeasibleNode) {
                    NRF.put(i, gerador.nextDouble());
                }
                //Step 6              
                //System.out.println("Tamanho da NRF = " + NRF.size());              
                max = Collections.max(NRF.values());

                currentTime = AdicionaNo(NRF, CRL, NRL, DRL, TRL, max, lastNode, Pin, d, EarliestTime, currentTime, R);
                lastNode = R.getLastNode();

                //Step 7
                //RETIRAR A LINHA DE BAIXO DEPOIS - inicialização de listRequestAux
                List<Request> listRequestAux = new LinkedList<>();
                //Desembarca as solicitações no nó 
                Desembarca(Pin, Pout, lastNode, currentTime, P, listRequestAux, R, log);
                //Embarca as solicitações sem tempo de espera
                Embarca(Pin, lastNode, currentTime, P, listRequestAux, R, log, Qmax);
                //Embarca agora as solicitações onde o veículo precisar esperar e guarda atualiza o tempo (currentTime)                               
                currentTime = EmbarcaRelaxacao(Pin, lastNode, currentTime, P, listRequestAux, R, log, Qmax, TimeWindows);

                //---------- Trata as solicitações inviáveis -----------
                RetiraSolicitacoesInviaveis(Pin, Pout, listRequestAux, currentTime, P, U);
                encontrado = ProcuraSolicitacaoParaAtender(R, Qmax, Pin, Pout, currentTime, n, d, lastNode, TimeWindows, encontrado);
                RetiraSolicitacaoNaoSeraAtendida(encontrado, Pin, Pout, listRequestAux, currentTime, P, U);

                //Step 8
                currentTime = FinalizaRota(P, R, currentTime, lastNode, d, solution);
            }

            //Step 9
            AnaliseSolicitacoesViaveisEmU(U, P, itK, d);
        }

//        S.setNonAttendedRequestsList(U);
//        //S.setfObjetivo1(FOp(S, c));
//        S.setLogger(log);
//        S.linkTheRoutes();
        //S.setFuncaoObjetivo(FuncaoObjetivo(S,c));
        solution.setNonAttendedRequestsList(U);
        solution.setTotalDistance(FO1(solution, c));
        solution.setTotalDeliveryDelay(FO2(solution));
        solution.setTotalRouteTimeChargeBanlance(FO3(solution));
        solution.setNumberOfNonAttendedRequests(FO4(solution));
        solution.setNumberOfVehicles(FO5(solution));
        solution.setTotalTravelTime(FO6(solution));
        solution.setTotalWaintingTime(FO7(solution));
        solution.setDeliveryTimeWindowAntecipation(FO8(solution));
        solution.setTotalOccupationRate(FO9(solution, Qmax));
        solution.setObjectivesList();
        //evaluateAggregatedObjectiveFunctions(solution, 1, 1, 1, 1, 1);

        solution.setObjectiveFunction(FuncaoDeAvaliacao(solution, listRequests, c));
        solution.setLogger(log);
        solution.linkTheRoutes();
        //S.setfObjetivo1((int) FuncaoObjetivo(S, c));
        solution.setObjectiveFunction(FuncaoDeAvaliacao(solution, listRequests, c));
        solution.setAggregatedObjectives(new double[reducedDimension]);
        return solution;
    }

    public static void mutationSwap(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop, double Pm, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P,
            List<Integer> m, List<List<Long>> d, List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        Random rnd = new Random();
        Random p1 = new Random();
        Random p2 = new Random();
        int posicao1, posicao2;
        double prob;

        for (int i = 0; i < Pop.size(); i++) {
            prob = rnd.nextFloat();

            if (prob < Pm) {
                List<Integer> individuo = new ArrayList<>(Pop.get(i).getLinkedRouteList());
                posicao1 = p1.nextInt(individuo.size());

                do {
                    posicao2 = p2.nextInt(individuo.size());
                } while (Objects.equals(individuo.get(posicao1), individuo.get(posicao2)));

                Collections.swap(individuo, posicao1, posicao2);

                ProblemSolution S = new ProblemSolution();
                S.setSolution(rebuildSolution(reducedDimension, parameters, individuo, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                Pop.get(i).setSolution(S);
            }
        }
    }

    public static void mutacaoShuffle(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop, double Pm, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P,
            List<Integer> m, List<List<Long>> d, List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        Random rnd = new Random();
        Random p1 = new Random();
        Random p2 = new Random();
        int posicao1, posicao2;
        double prob;

        for (int i = 0; i < Pop.size(); i++) {
            prob = rnd.nextFloat();

            if (prob < Pm) {
                List<Integer> individuo = new ArrayList<>(Pop.get(i).getLinkedRouteList());
                Collections.shuffle(individuo);
                ProblemSolution S = new ProblemSolution();
                S.setSolution(rebuildSolution(reducedDimension, parameters, individuo, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                Pop.get(i).setSolution(S);
            }
        }
    }

    public static void mutation2Opt(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop, double Pm, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P,
            List<Integer> m, List<List<Long>> d, List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        Random rnd = new Random();
        Random p1 = new Random();
        Random p2 = new Random();
        int posicao1, posicao2;
        double prob;

        for (int i = 0; i < Pop.size(); i++) {
            prob = rnd.nextFloat();

            if (prob < Pm) {
                List<Integer> individuo = new ArrayList<>(Pop.get(i).getLinkedRouteList());

                int index1, index2;

                do {
                    index1 = rnd.nextInt(individuo.size());
                    index2 = rnd.nextInt(individuo.size());
                } while (index1 == index2);

                List<Integer> indices = new ArrayList<>();
                indices.add(index1);
                indices.add(index2);

                int min = Collections.min(indices);
                int max = Collections.max(indices);

                List<Integer> aux = new ArrayList<>(individuo.subList(min, max));

                Collections.reverse(aux);

                individuo.subList(min, max).clear();
                individuo.addAll(min, aux);

                ProblemSolution S = new ProblemSolution();
                S.setSolution(rebuildSolution(reducedDimension, parameters, individuo, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                Pop.get(i).setSolution(S);
            }
        }
    }

    public static void mutation2Shuffle(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop, double Pm, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P,
            List<Integer> m, List<List<Long>> d, List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        Random rnd = new Random();
        Random p1 = new Random();
        Random p2 = new Random();
        int posicao1, posicao2;
        double prob;

        for (int i = 0; i < Pop.size(); i++) {
            prob = rnd.nextFloat();

            if (prob < Pm) {
                List<Integer> individuo = new ArrayList<>(Pop.get(i).getLinkedRouteList());

                int index1, index2;

                do {
                    index1 = rnd.nextInt(individuo.size());
                    index2 = rnd.nextInt(individuo.size());
                } while (index1 == index2);

                List<Integer> indices = new ArrayList<>();
                indices.add(index1);
                indices.add(index2);

                int min = Collections.min(indices);
                int max = Collections.max(indices);

                List<Integer> aux = new ArrayList<>(individuo.subList(min, max));

                Collections.shuffle(aux);

                individuo.subList(min, max).clear();
                individuo.addAll(min, aux);

                ProblemSolution S = new ProblemSolution();
                S.setSolution(rebuildSolution(reducedDimension, parameters, individuo, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                Pop.get(i).setSolution(S);
            }
        }
    }

    public static void mutation2ShuffleForMOEAD(int reducedDimension, List<Double> parameters, ProblemSolution Pop, double Pm, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P,
            List<Integer> m, List<List<Long>> d, List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        Random rnd = new Random();
        Random p1 = new Random();
        Random p2 = new Random();
        int posicao1, posicao2;
        double prob;

        prob = rnd.nextFloat();

        if (prob < Pm) {
            List<Integer> individuo = new ArrayList<>(Pop.getLinkedRouteList());

            int index1, index2;

            do {
                index1 = rnd.nextInt(individuo.size());
                index2 = rnd.nextInt(individuo.size());
            } while (index1 == index2);

            List<Integer> indices = new ArrayList<>();
            indices.add(index1);
            indices.add(index2);

            int min = Collections.min(indices);
            int max = Collections.max(indices);

            List<Integer> aux = new ArrayList<>(individuo.subList(min, max));

            Collections.shuffle(aux);

            individuo.subList(min, max).clear();
            individuo.addAll(min, aux);

            ProblemSolution S = new ProblemSolution();
            S.setSolution(rebuildSolution(reducedDimension, parameters, individuo, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
            Pop.setSolution(S);

        }
    }

    public static void MutacaoILS(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop, double Pm, List<Request> listRequests, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P, List<Integer> m, List<List<Long>> d, List<List<Long>> c,
            Long TimeWindows, Long currentTime, Integer lastNode) {
        Random rnd = new Random();
        Random p1 = new Random();
        Random p2 = new Random();
        int posicao1, posicao2;
        double prob;

        for (int i = 0; i < Pop.size(); i++) {
            prob = rnd.nextFloat();
            //System.out.println("Prob gerada = " + prob);
            if (prob < Pm) {

                ProblemSolution S = new ProblemSolution();
                //S.setSolution(IteratedLocalSearch(Pop.get(i), listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
                //S.setSolution(VariableNeighborhoodDescend(Pop.get(i), listRequests,  P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                S.setSolution(firstImprovementAlgorithm(reducedDimension, parameters, Pop.get(i), 2, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                Pop.get(i).setSolution(S);
            }
        }
    }

    public static void Fitness(List<ProblemSolution> Pop) {
        int soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            soma += Pop.get(i).getObjectiveFunction();
        }
        List<Double> fit = new ArrayList<>();

        for (int i = 0; i < Pop.size(); i++) {
            fit.add((double) Pop.get(i).getObjectiveFunction() / soma);
        }
        Collections.sort(fit);
        Collections.reverse(fit);

        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(fit.get(i));
        }
    }

    public static void NewFitness(List<ProblemSolution> population) {
        double max = population.stream().mapToDouble(ProblemSolution::getObjectiveFunction).max().getAsDouble();
        double min = population.stream().mapToDouble(ProblemSolution::getObjectiveFunction).min().getAsDouble();
        population.forEach(u -> u.setFitness((max - u.getObjectiveFunction()) / (max - min)));

        double sum = population.stream().mapToDouble(ProblemSolution::getFitness).sum();
        population.forEach(u -> u.setFitness(u.getFitness() / sum));
    }

    public static void populationSorting(List<ProblemSolution> Pop) {
        Collections.sort(Pop);
    }

    public static void printPopulation(List<ProblemSolution> Pop) {
        for (int i = 0; i < Pop.size(); i++) {
            //System.out.println("Pop(" + i + ") = " + Pop.get(i));
            System.out.println(Pop.get(i));
            //System.out.println(Pop.get(i).getLinkedRoute());
        }
    }

    public static void rouletteWheelSelectionAlgorithm(List<Integer> pais, List<ProblemSolution> Pop, Integer TamMax) {
        Random rnd = new Random();
        double valor;
        double soma;
        int pos;
        pais.clear();
        for (int i = 0; i < TamMax; i++) {
            soma = 0;
            pos = -1;
            //valor = rnd.nextFloat() / 10;
            valor = rnd.nextDouble() / 10;
            //System.out.println(valor);
            //ImprimePopulacao(Pop);
            for (int j = 0; j < Pop.size(); j++) {
                soma += Pop.get(j).getFitness();
                if (valor <= soma) {
                    pos = j;
                    pais.add(pos);
                    break;
                }
            }
            if (pos == -1) {
                pos = rnd.nextInt(Pop.size());
                pais.add(pos);
                //System.out.println("Precisou");
            }
        }
    }

    public static void onePointCrossover(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop_nova, List<ProblemSolution> Pop, Integer TamMax, double Pc,
            List<Integer> pais, List<Request> listRequests, List<Request> P, Set<Integer> K, List<Request> U,
            Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<List<Long>> d, List<List<Long>> c,
            Integer n, Integer Qmax, Long TimeWindows) {
        int pai;
        int mae;
        int pontoCorte;
        int menorTamanho;
        double valor;
        Random rnd = new Random();
        List<ProblemSolution> NewPop = new ArrayList<>();
        List<Integer> filho1 = new ArrayList<>();
        List<Integer> filho2 = new ArrayList<>();
        NewPop.clear();
        for (int i = 0; i < (pais.size() - 1); i = i + 2) {

            valor = rnd.nextFloat();

            pai = pais.get(i);
            mae = pais.get(i + 1);
            ProblemSolution s1 = new ProblemSolution();
            ProblemSolution s2 = new ProblemSolution();
            if (valor < Pc) {

                menorTamanho = Math.min(Pop.get(pai).getLinkedRouteList().size(), Pop.get(mae).getLinkedRouteList().size());

                pontoCorte = rnd.nextInt(menorTamanho);

                filho1.addAll(Pop.get(pai).getLinkedRouteList().subList(0, pontoCorte));
                filho1.addAll((Pop.get(mae).getLinkedRouteList().subList(pontoCorte, Pop.get(mae).getLinkedRouteList().size())));
                filho2.addAll(Pop.get(mae).getLinkedRouteList().subList(0, pontoCorte));
                filho2.addAll((Pop.get(pai).getLinkedRouteList().subList(pontoCorte, Pop.get(pai).getLinkedRouteList().size())));

                s1.setSolution(rebuildSolution(reducedDimension, parameters, filho1, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                s2.setSolution(rebuildSolution(reducedDimension, parameters, filho2, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

            } else {
                s1.setSolution(Pop.get(mae));
                s2.setSolution(Pop.get(pai));
            }

            NewPop.add(s1);
            NewPop.add(s2);
            //s1.resetSolution(-1);
            //System.out.println("Pai = "+ pai +" Mae = " + mae);
            filho1.clear();
            filho2.clear();
        }
        //Pop.clear();
        //Pop.addAll(NewPop);
        //NewPop.clear();
        //OrdenaPopulacao(Pop);
        //Fitness(Pop);
        Pop_nova.clear();
        Pop_nova.addAll(NewPop);
        NewPop.clear();
        populationSorting(Pop_nova);
    }

    public static void twoPointsCrossover(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop_nova, List<ProblemSolution> Pop, Integer TamMax, double Pc, List<Integer> pais, List<Request> listRequests,
            List<Request> P, Set<Integer> K, List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            List<List<Long>> d, List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        int pai;
        int mae;
        int pontoCorte;
        int menorTamanho;
        double valor;
        Random rnd = new Random();
        List<ProblemSolution> NewPop = new ArrayList<>();
        List<Integer> filho1 = new ArrayList<>();
        List<Integer> filho2 = new ArrayList<>();
        NewPop.clear();
        try {
            for (int i = 0; i < (TamMax - 1); i = i + 2) {

                valor = rnd.nextFloat();

                pai = pais.get(i);
                mae = pais.get(i + 1);
                ProblemSolution s1 = new ProblemSolution();
                ProblemSolution s2 = new ProblemSolution();
                if (valor < Pc) {

                    int index1, index2;
                    menorTamanho = Math.min(Pop.get(pai).getLinkedRouteList().size(), Pop.get(mae).getLinkedRouteList().size());

                    filho1.addAll(Pop.get(pai).getLinkedRouteList());
                    filho2.addAll(Pop.get(mae).getLinkedRouteList());

                    do {
                        index1 = rnd.nextInt(menorTamanho);
                        index2 = rnd.nextInt(menorTamanho);
                    } while (index1 == index2);

                    List<Integer> indices = new ArrayList<>();
                    indices.add(index1);
                    indices.add(index2);

                    int min = Collections.min(indices);
                    int max = Collections.max(indices);
                    //System.out.println("Indices = " + indices);
                    //System.out.println("Min = " + min + " Max = " + max);

                    List<Integer> parte1 = new ArrayList<>(filho1.subList(min, max));
                    List<Integer> parte2 = new ArrayList<>(filho2.subList(min, max));

                    filho1.subList(min, max).clear();
                    filho2.subList(min, max).clear();

                    //System.out.println(filho1);
                    //System.out.println(filho2);
                    //System.out.println("Cruzou!!!");
                    filho1.addAll(min, parte2);
                    filho2.addAll(min, parte1);

                    s1.setSolution(rebuildSolution(reducedDimension, parameters, filho1, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    s2.setSolution(rebuildSolution(reducedDimension, parameters, filho2, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                } else {
                    s1.setSolution(Pop.get(mae));
                    s2.setSolution(Pop.get(pai));
                }

                NewPop.add(s1);
                NewPop.add(s2);
                //s1.resetSolution(-1);
                //System.out.println("Pai = "+ pai +" Mae = " + mae);
                filho1.clear();
                filho2.clear();
            }
            //Pop.clear();
            Pop_nova.clear();
            Pop_nova.addAll(NewPop);
            NewPop.clear();
            //OrdenaPopulacao(Pop_nova);
            //Fitness(Pop);
            //FitnessSPEA2(Pop_nova);
        } catch (IllegalArgumentException e) {
            printPopulation(Pop);
        }
    }

    public static void twoPointsCrossoverForMOEAD(int reducedDimension, List<Double> parameters, List<ProblemSolution> Pop_nova, List<ProblemSolution> Pop, Integer TamMax, double Pc,
            List<ProblemSolution> pais, List<Request> listRequests,
            List<Request> P, Set<Integer> K, List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            List<List<Long>> d, List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        ProblemSolution pai;
        ProblemSolution mae;
        int pontoCorte;
        int menorTamanho;
        double valor;
        Random rnd = new Random();
        List<ProblemSolution> NewPop = new ArrayList<>();
        List<Integer> filho1 = new ArrayList<>();
        List<Integer> filho2 = new ArrayList<>();
        NewPop.clear();
        try {
            for (int i = 0; i < (TamMax - 1); i = i + 2) {

                valor = rnd.nextFloat();

                pai = pais.get(i);
                mae = pais.get(i + 1);
                ProblemSolution s1 = new ProblemSolution();
                ProblemSolution s2 = new ProblemSolution();
                if (valor < Pc) {

                    int index1, index2;
                    menorTamanho = Math.min(pai.getLinkedRouteList().size(), mae.getLinkedRouteList().size());

                    filho1.addAll(pai.getLinkedRouteList());
                    filho2.addAll(mae.getLinkedRouteList());

                    do {
                        index1 = rnd.nextInt(menorTamanho);
                        index2 = rnd.nextInt(menorTamanho);
                    } while (index1 == index2);

                    List<Integer> indices = new ArrayList<>();
                    indices.add(index1);
                    indices.add(index2);

                    int min = Collections.min(indices);
                    int max = Collections.max(indices);
                    //System.out.println("Indices = " + indices);
                    //System.out.println("Min = " + min + " Max = " + max);

                    List<Integer> parte1 = new ArrayList<>(filho1.subList(min, max));
                    List<Integer> parte2 = new ArrayList<>(filho2.subList(min, max));

                    filho1.subList(min, max).clear();
                    filho2.subList(min, max).clear();

                    //System.out.println(filho1);
                    //System.out.println(filho2);
                    //System.out.println("Cruzou!!!");
                    filho1.addAll(min, parte2);
                    filho2.addAll(min, parte1);

                    s1.setSolution(rebuildSolution(reducedDimension, parameters, filho1, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    s2.setSolution(rebuildSolution(reducedDimension, parameters, filho2, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                } else {
                    s1.setSolution(mae);
                    s2.setSolution(pai);
                }

                NewPop.add(s1);
                NewPop.add(s2);
                //s1.resetSolution(-1);
                //System.out.println("Pai = "+ pai +" Mae = " + mae);
                filho1.clear();
                filho2.clear();
            }
            //Pop.clear();
            Pop_nova.clear();
            Pop_nova.addAll(NewPop);
            NewPop.clear();
            //OrdenaPopulacao(Pop_nova);
            //Fitness(Pop);
            //FitnessSPEA2(Pop_nova);
        } catch (IllegalArgumentException e) {
            printPopulation(Pop);
        }
    }

    public static ProblemSolution copyBestSolution(List<ProblemSolution> population, ProblemSolution bestSolution) {
        for (ProblemSolution solution : population) {
            if (solution.getObjectiveFunction() < bestSolution.getObjectiveFunction()) {
                bestSolution.setSolution(solution);
            }
        }
        return bestSolution;
    }

    public static void insertBestIndividualInPopulation(List<ProblemSolution> population, ProblemSolution bestSolution) {
        population.get(population.size() - 1).setSolution(bestSolution);
    }

    public static void savePopulation(List<ProblemSolution> population, Integer generationNumber) {
        String folder, dataFileName;
        try {
            folder = "\\home\\renan";
            dataFileName = "Solution";
            boolean success = (new File(folder)).mkdirs();
            if (!success) {
                System.out.println("Folders already exists!");
            }
            PrintStream printStream;
            printStream = new PrintStream(folder + "\\GA-MESTRADO" + dataFileName + ".txt");

            printStream.print("\tGeneration = " + generationNumber + "\n");
            for (ProblemSolution solution : population) {
                printStream.print("\t" + solution + "\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ProblemSolution firstImprovementAlgorithm(int reducedDimension, List<Double> parameters, ProblemSolution s, int movementType, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            List<List<Long>> d, List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        ProblemSolution melhor = new ProblemSolution(s);

        ProblemSolution aux = new ProblemSolution();

        List<Integer> original = new ArrayList<Integer>(s.getLinkedRouteList());

        List<Integer> vizinho = new ArrayList<Integer>();

        /**
         * Tipo Estrategia: 1 - melhorVizinho, 2 - primeiroMelhorVizinho Tipo
         * Movimento: 1 - Troca, 2 - Substituicao, 3 - Deslocamento, 4 -
         * Aleatoria
         *
         */
        switch (movementType) {
            case 1: // troca						

                for (int i = 0; i < original.size() - 1; i++) {
                    for (int j = i + 1; j < original.size(); j++) {
                        vizinho.addAll(original);

                        if (vizinho.get(i) != vizinho.get(j)) {
                            Collections.swap(vizinho, i, j);

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI TROCA-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);
                                return melhor;
                            }
                        }
                        vizinho.clear();
                    }
                }
                break;

            case 2: // substituicao

                for (int i = 0; i < original.size(); i++) {
                    for (int j = 1; j < n; j++) {
                        vizinho.addAll(original);

                        if (vizinho.get(i) != j) {
                            vizinho.set(i, j);

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI INSERCAO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);
                                return melhor;
                            }
                        }

                        vizinho.clear();
                    }
                }
                break;

            case 3: // deslocamento

                for (int i = 0; i < original.size(); i++) {
                    for (int j = 0; j < original.size(); j++) {
                        if (i != j) {
                            vizinho.addAll(original);
                            vizinho.remove(i);
                            vizinho.add(j, original.get(i));

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);
                                return melhor;
                            }
                        }
                        vizinho.clear();
                    }
                }

                break;

            case 4: // aleatoria

                Random r1 = new Random();

                int escolhaVizinho;

                Random r2 = new Random(System.nanoTime());

                int qtd = (int) (0.1 * (original.size() * original.size()));

                int elemento,
                 posicao,
                 posicao1,
                 posicao2;

                for (int i = 0; i < qtd; i++) {//???QUANTAS VEZES S�O NECESS�RIAS...

                    vizinho.addAll(original);

                    escolhaVizinho = r1.nextInt(120);

                    if (escolhaVizinho < 20 || escolhaVizinho >= 60 && escolhaVizinho < 80) {
                        //Troca

                        posicao1 = r1.nextInt(original.size());

                        do {
                            posicao2 = r2.nextInt(original.size());
                        } while (vizinho.get(posicao1) == vizinho.get(posicao2));

                        Collections.swap(vizinho, posicao1, posicao2);

                    } else if (escolhaVizinho >= 20 && escolhaVizinho < 40 || escolhaVizinho >= 80 && escolhaVizinho < 100) {
                        //Insercao

                        posicao = r1.nextInt(original.size());

                        do {
                            elemento = r2.nextInt(n);
                        } while (elemento == 0 || elemento == vizinho.get(posicao));

                        vizinho.set(posicao, elemento);
                    } else {
                        //Movimento

                        posicao1 = r1.nextInt(original.size());

                        do {
                            posicao2 = r2.nextInt(original.size());
                        } while (posicao1 == posicao2);

                        vizinho.remove(posicao1);
                        vizinho.add(posicao2, original.get(posicao1));
                    }

                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                    if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                        //System.out.println("ACHEI ALEATORIA-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                        melhor.setSolution(aux);
                        return melhor;

                    }

                    vizinho.clear();
                }

                break;

            case 5:
                for (int i = 0; i < original.size() - 1; i++) {
                    int contador = 1;
                    for (int j = 0; j < original.size(); j++) {
                        if ((i != j) && (j != i + 1)) {

                            vizinho.addAll(original);
                            List<Integer> nosRetirados = new ArrayList<>(vizinho.subList(i, i + 2));

                            vizinho.subList(i, i + 2).clear();
                            vizinho.addAll(contador, nosRetirados);
                            contador++;

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);
                                return melhor;

                            }
                        }
                        vizinho.clear();
                    }
                }

                break;
            case 6:
                for (int i = 0; i < original.size() - 2; i++) {
                    int contador = 1;
                    for (int j = 0; j < original.size(); j++) {
                        if ((i != j) && (j != i + 1) && (j != i + 2)) {

                            vizinho.addAll(original);
                            List<Integer> nosRetirados = new ArrayList<>(vizinho.subList(i, i + 3));

                            vizinho.subList(i, i + 3).clear();
                            vizinho.addAll(contador, nosRetirados);
                            contador++;

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);
                                return melhor;

                            }
                        }
                        vizinho.clear();
                    }
                }
                break;
        }

        return melhor;
    }

    public static ProblemSolution bestImprovementAlgorithm(int reducedDimension, List<Double> parameters, ProblemSolution s, int tipoMovimento, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            List<List<Long>> d, List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        ProblemSolution melhor = new ProblemSolution(s);

        ProblemSolution aux = new ProblemSolution();

        List<Integer> original = new ArrayList<Integer>(s.getLinkedRouteList());

        List<Integer> vizinho = new ArrayList<Integer>();

        /**
         * Tipo Estrategia: 1 - melhorVizinho, 2 - primeiroMelhorVizinho Tipo
         * Movimento: 1 - Troca, 2 - Substituicao, 3 - Deslocamento, 4 -
         * Aleatoria
         *
         */
        switch (tipoMovimento) {
            case 1: // troca						

                for (int i = 0; i < original.size() - 1; i++) {
                    for (int j = i + 1; j < original.size(); j++) {
                        vizinho.addAll(original);

                        if (vizinho.get(i) != vizinho.get(j)) {
                            Collections.swap(vizinho, i, j);

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI TROCA-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);

                            }
                        }
                        vizinho.clear();
                    }
                }
                break;

            case 2: // substituicao

                for (int i = 0; i < original.size(); i++) {
                    for (int j = 1; j < n; j++) {
                        vizinho.addAll(original);

                        if (vizinho.get(i) != j) {
                            vizinho.set(i, j);

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI INSERCAO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);

                            }
                        }

                        vizinho.clear();
                    }
                }
                break;

            case 3: // deslocamento

                for (int i = 0; i < original.size(); i++) {
                    for (int j = 0; j < original.size(); j++) {
                        if (i != j) {
                            vizinho.addAll(original);
                            vizinho.remove(i);
                            vizinho.add(j, original.get(i));

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);

                            }
                        }
                        vizinho.clear();
                    }
                }

                break;

            case 4: // aleatoria

                Random r1 = new Random();

                int escolhaVizinho;

                Random r2 = new Random(System.nanoTime());

                int qtd = (int) (0.1 * (original.size() * original.size()));

                int elemento,
                 posicao,
                 posicao1,
                 posicao2;

                for (int i = 0; i < qtd; i++) {//???QUANTAS VEZES S�O NECESS�RIAS...

                    vizinho.addAll(original);

                    escolhaVizinho = r1.nextInt(120);

                    if (escolhaVizinho < 20 || escolhaVizinho >= 60 && escolhaVizinho < 80) {
                        //Troca

                        posicao1 = r1.nextInt(original.size());

                        do {
                            posicao2 = r2.nextInt(original.size());
                        } while (vizinho.get(posicao1) == vizinho.get(posicao2));

                        Collections.swap(vizinho, posicao1, posicao2);

                    } else if (escolhaVizinho >= 20 && escolhaVizinho < 40 || escolhaVizinho >= 80 && escolhaVizinho < 100) {
                        //Insercao

                        posicao = r1.nextInt(original.size());

                        do {
                            elemento = r2.nextInt(n);
                        } while (elemento == 0 || elemento == vizinho.get(posicao));

                        vizinho.set(posicao, elemento);
                    } else {
                        //Movimento

                        posicao1 = r1.nextInt(original.size());

                        do {
                            posicao2 = r2.nextInt(original.size());
                        } while (posicao1 == posicao2);

                        vizinho.remove(posicao1);
                        vizinho.add(posicao2, original.get(posicao1));
                    }

                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                    if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                        //System.out.println("ACHEI ALEATORIA-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                        melhor.setSolution(aux);

                    }

                    vizinho.clear();
                }

                break;

            case 5:
                for (int i = 0; i < original.size() - 1; i++) {
                    int contador = 1;
                    for (int j = 0; j < original.size(); j++) {
                        if ((i != j) && (j != i + 1)) {

                            vizinho.addAll(original);
                            List<Integer> nosRetirados = new ArrayList<>(vizinho.subList(i, i + 2));

                            vizinho.subList(i, i + 2).clear();
                            vizinho.addAll(contador, nosRetirados);
                            contador++;

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);

                            }
                        }
                        vizinho.clear();
                    }
                }

                break;
            case 6:
                for (int i = 0; i < original.size() - 2; i++) {
                    int contador = 1;
                    for (int j = 0; j < original.size(); j++) {
                        if ((i != j) && (j != i + 1) && (j != i + 2)) {

                            vizinho.addAll(original);
                            List<Integer> nosRetirados = new ArrayList<>(vizinho.subList(i, i + 3));

                            vizinho.subList(i, i + 3).clear();
                            vizinho.addAll(contador, nosRetirados);
                            contador++;

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getObjectiveFunction() < melhor.getObjectiveFunction()) {
                                //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                                melhor.setSolution(aux);

                            }
                        }
                        vizinho.clear();
                    }
                }
                break;
        }

        return melhor;
    }

    public static ProblemSolution primeiroMelhorVizinhoAleatorio(int reducedDimension, List<Double> parameters, ProblemSolution s, int tipoMovimento, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            List<List<Long>> d, List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        ProblemSolution melhor = new ProblemSolution(s);
        ProblemSolution aux = new ProblemSolution();
        List<Integer> original = new ArrayList<>(s.getLinkedRouteList());
        List<Integer> vizinho = new ArrayList<>();
        int qtd = (int) (0.1 * (original.size() * original.size()));
        qtd = 100;
        Random r1 = new Random();
        //System.out.println("original.size() = " + original.size() );
        //System.out.println("s.getLinkedRoute().size() = " + s.getLinkedRoute().size());
        int escolhaVizinho;
        int elemento;
        Random r2 = new Random(System.nanoTime());

        int posicao, posicao1, posicao2, contador;
        /**
         * Tipo Estrategia: 1 - melhorVizinho, 2 - primeiroMelhorVizinho Tipo
         * Movimento: 1 - Troca, 2 - Substituicao, 3 - Deslocamento, 4 -
         * Aleatoria
         *
         */
        //System.out.println("qtd = " + qtd);

        switch (tipoMovimento) {
            case 1: // troca						

                contador = 0;
                for (int i = 0; i < qtd; i++) {//???QUANTAS VEZES S�O NECESS�RIAS...
                    //System.out.println("Contador = " + contador);
                    contador++;
                    vizinho.addAll(original);

                    posicao1 = r1.nextInt(original.size());

                    do {
                        posicao2 = r2.nextInt(original.size());
                    } while (Objects.equals(vizinho.get(posicao1), vizinho.get(posicao2)));

                    Collections.swap(vizinho, posicao1, posicao2);
                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    //System.out.println("Posições da troca = " + posicao1 + "\t" + posicao2);
                    //System.out.println("ProblemSolution gerada = " + aux);
                    if (aux.getTotalDistance() < melhor.getTotalDistance()) {
                        melhor.setSolution(aux);
                        return melhor;
                    }
                }
                vizinho.clear();
                break;

            case 2: //substituicao
                contador = 0;
                for (int i = 0; i < qtd; i++) {
                    //System.out.println("Contador = " + contador);
                    contador++;
                    vizinho.addAll(original);
                    posicao = r1.nextInt(original.size());

                    do {
                        elemento = r2.nextInt(n);
                    } while (elemento == 0 || elemento == vizinho.get(posicao));

                    vizinho.set(posicao, elemento);
                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    //System.out.println("Posições da troca = " + posicao + "\t" + elemento);
                    //System.out.println("ProblemSolution gerada = " + aux);
                    if (aux.getTotalDistance() < melhor.getTotalDistance()) {
                        melhor.setSolution(aux);
                        return melhor;
                    }
                }
                vizinho.clear();
                break;

            case 3: // deslocamento
                contador = 0;
                for (int i = 0; i < qtd; i++) {
                    //System.out.println("Contador = " + contador);
                    contador++;
                    vizinho.addAll(original);
                    posicao1 = r1.nextInt(original.size());

                    do {
                        posicao2 = r2.nextInt(original.size());
                    } while (posicao1 == posicao2);

                    vizinho.remove(posicao1);
                    vizinho.add(posicao2, original.get(posicao1));

                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    if (aux.getTotalDistance() < melhor.getTotalDistance()) {
                        //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
                        melhor.setSolution(aux);
                        return melhor;
                    }
                }
                vizinho.clear();
                break;
        }
        return melhor;
    }

    public static ProblemSolution vizinhoAleatorio(int reducedDimension, List<Double> parameters, ProblemSolution s, int semente1, int semente2, int tipoMovimento, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            List<List<Long>> d, List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        ProblemSolution melhor = new ProblemSolution(s);
        ProblemSolution aux = new ProblemSolution();
        List<Integer> original = new ArrayList<>(s.getLinkedRouteList());
        List<Integer> vizinho = new ArrayList<>();
        int qtd = (int) (0.1 * (original.size() * original.size()));
        qtd = 100;
        Random r1 = new Random(semente1);
        //System.out.println("original.size() = " + original.size() );
        //System.out.println("s.getLinkedRoute().size() = " + s.getLinkedRoute().size());
        int escolhaVizinho;
        int elemento;
        Random r2 = new Random(semente2);

        int posicao, posicao1, posicao2, contador;
        /**
         * Tipo Estrategia: 1 - melhorVizinho, 2 - primeiroMelhorVizinho Tipo
         * Movimento: 1 - Troca, 2 - Substituicao, 3 - Deslocamento, 4 -
         * Aleatoria
         *
         */
        //System.out.println("qtd = " + qtd);

        switch (tipoMovimento) {
            case 1: // troca						

                contador = 0;
                for (int i = 0; i < qtd; i++) {//???QUANTAS VEZES S�O NECESS�RIAS...
                    //System.out.println("Contador = " + contador);
                    contador++;
                    vizinho.addAll(original);

                    posicao1 = r1.nextInt(original.size());

                    do {
                        posicao2 = r2.nextInt(original.size());
                    } while (Objects.equals(vizinho.get(posicao1), vizinho.get(posicao2)));

                    Collections.swap(vizinho, posicao1, posicao2);
                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    return aux;
                    //System.out.println("Posições da troca = " + posicao1 + "\t" + posicao2);
                    //System.out.println("ProblemSolution gerada = " + aux);
//                    if (aux.getfObjetivo() < melhor.getfObjetivo()) {
//                        melhor.setSolution(aux);
//                        return melhor;
//                    }
                }
                vizinho.clear();
                break;

            case 2: //substituicao
                contador = 0;
                for (int i = 0; i < qtd; i++) {
                    //System.out.println("Contador = " + contador);
                    contador++;
                    vizinho.addAll(original);
                    posicao = r1.nextInt(original.size());

                    do {
                        elemento = r2.nextInt(n);
                    } while (elemento == 0 || elemento == vizinho.get(posicao));

                    vizinho.set(posicao, elemento);
                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    //System.out.println("Posições da troca = " + posicao + "\t" + elemento);
                    //System.out.println("ProblemSolution gerada = " + aux);
                    return aux;
//                    if (aux.getfObjetivo() < melhor.getfObjetivo()) {
//                        melhor.setSolution(aux);
//                        return melhor;
//                    }
                }
                vizinho.clear();
                break;

            case 3: // deslocamento
                contador = 0;
                for (int i = 0; i < qtd; i++) {
                    //System.out.println("Contador = " + contador);
                    contador++;
                    vizinho.addAll(original);
                    posicao1 = r1.nextInt(original.size());

                    do {
                        posicao2 = r2.nextInt(original.size());
                    } while (posicao1 == posicao2);

                    vizinho.remove(posicao1);
                    vizinho.add(posicao2, original.get(posicao1));

                    aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    return aux;
//                    if (aux.getfObjetivo() < melhor.getfObjetivo()) {
//                        //System.out.println("ACHEI MOVIMENTO-> "+aux.getfObjetivo()+" "+ aux.getNonAttendedRequestsList().size());
//                        melhor.setSolution(aux);
//                        return melhor;
//                    }
                }
                vizinho.clear();
                break;
        }
        return melhor;
    }

    public static ProblemSolution buscaTabu(int reducedDimension, List<Double> parameters, ProblemSolution inicial, int tipoEstrategia, int tipoMovimento, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<List<Long>> d,
            List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        ProblemSolution estrela = new ProblemSolution();
        ProblemSolution s = new ProblemSolution(inicial);
        estrela.setSolution(s);

        int iteracao = 0, //contador do n�mero de itera��es
                melhorIteracao = 0, //itera��o mais recente que forneceu s*
                BTMAX = 10;			//numero m�ximo de itera��es sem melhora em s*
        /**
         *
         * Map<Map<Integer,Integer>,Integer> listaTabu = new
         * HashMap<Map<Integer,Integer>, Integer>();
         *
         *
         *
         * Map<Double,List<Double>> A = new TreeMap<Double, List<Double>>();
         * double doubleAlfa;
         *
         * for(int alfa = 30; alfa <= 70; alfa += 5){
         *
         * doubleAlfa = Double.parseDouble(Float.toString(new
         * Float(alfa*0.01))); A.put(doubleAlfa, new ArrayList<Double>(5));
         *
         * *
         */
        int[][] listaTabuTroca, listaTabuSubstituicao, listaTabuMovimento;

        listaTabuTroca = new int[s.getLinkedRouteList().size()][s.getLinkedRouteList().size()];

        listaTabuSubstituicao = new int[s.getLinkedRouteList().size()][n];

        listaTabuMovimento = new int[s.getLinkedRouteList().size()][s.getLinkedRouteList().size()];

        /**
         * Tipo Estrategia: 1 - melhorVizinho, 2 - primeiroMelhorVizinho Tipo
         * Movimento: 1 - Troca, 2 - Substituicao, 3 - Deslocamento, 4 -
         * Aleatoria
         *
         */
        while (iteracao - melhorIteracao <= BTMAX) {
            iteracao++;
            //System.out.println(iteracao - melhorIteracao);
            s.setSolution(melhorVizinhoBT(reducedDimension, parameters, s, estrela, tipoMovimento, listaTabuTroca, listaTabuSubstituicao, listaTabuMovimento, iteracao,
                    listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
            if (s.getObjectiveFunction() < estrela.getObjectiveFunction()) {
                estrela.setSolution(s);
                melhorIteracao = iteracao;
            }

        }

        return estrela;
    }

    public static ProblemSolution melhorVizinhoBT(int reducedDimension, List<Double> parameters, ProblemSolution s, ProblemSolution estrela, int tipoMovimento, int[][] listaTabuTroca, int[][] listaTabuSubstituicao,
            int[][] listaTabuMovimento, int iteracao, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<List<Long>> d,
            List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {

        ProblemSolution melhor = new ProblemSolution();
        melhor.setTotalDistance(999999);

        ProblemSolution aux = new ProblemSolution();

        List<Integer> original = new ArrayList<Integer>(s.getLinkedRouteList());

        List<Integer> vizinho = new ArrayList<Integer>();

        /**
         * Tipo Estrategia: 1 - melhorVizinho, 2 - primeiroMelhorVizinho Tipo
         * Movimento: 1 - Troca, 2 - Substituicao, 3 - Deslocamento, 4 -
         * Aleatoria
         *
         */
        // armazena a �ltima troca de posi��es realizada
        int pos1 = -1, pos2 = -1, pos = -1, elem = -1,
                duracaoTabu = 5;
        boolean atualizaListaTabu = false;

        switch (tipoMovimento) {
            case 1: // troca			

                for (int posicao1 = 0; posicao1 < original.size() - 1; posicao1++) {
                    for (int posicao2 = posicao1 + 1; posicao2 < original.size(); posicao2++) {
                        vizinho.addAll(original);

                        if (vizinho.get(posicao1) != vizinho.get(posicao2)) {
                            Collections.swap(vizinho, posicao1, posicao2);

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getTotalDistance() < melhor.getTotalDistance()
                                    && ((listaTabuTroca[posicao2][posicao1] <= iteracao && listaTabuTroca[posicao1][posicao2] <= iteracao)
                                    || aux.getTotalDistance() < estrela.getTotalDistance())) {

                                melhor.setSolution(aux);
                                pos1 = posicao1;
                                pos2 = posicao2;
                                atualizaListaTabu = true;

                            }
                        }
                        vizinho.clear();
                    }
                }

                if (atualizaListaTabu) {
                    listaTabuTroca[pos2][pos1] = iteracao + duracaoTabu;
                    listaTabuTroca[pos1][pos2] = iteracao + duracaoTabu;
                }

                break;

            case 2: // substituicao

                for (int posicao = 0; posicao < original.size(); posicao++) {
                    for (int elemento = 1; elemento < n; elemento++) {
                        vizinho.addAll(original);

                        if (vizinho.get(posicao) != elemento) {
                            vizinho.set(posicao, elemento);

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getTotalDistance() < melhor.getTotalDistance()
                                    && (listaTabuSubstituicao[posicao][elemento] <= iteracao || aux.getTotalDistance() < estrela.getTotalDistance())) {

                                melhor.setSolution(aux);
                                pos = posicao;
                                elem = original.get(posicao);
                                atualizaListaTabu = true;

                            }
                        }

                        vizinho.clear();
                    }
                }

                if (atualizaListaTabu) {
                    listaTabuSubstituicao[pos][elem] = iteracao + duracaoTabu;
                }

                break;

            case 3: // deslocamento

                for (int posicao1 = 0; posicao1 < original.size(); posicao1++) {
                    for (int posicao2 = 0; posicao2 < original.size(); posicao2++) {
                        if (posicao1 != posicao2) {
                            vizinho.addAll(original);
                            vizinho.remove(posicao1);
                            vizinho.add(posicao2, original.get(posicao1));

                            aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if (aux.getTotalDistance() < melhor.getTotalDistance()
                                    && (listaTabuMovimento[posicao1][posicao2] <= iteracao || aux.getTotalDistance() < estrela.getTotalDistance())) {

                                melhor.setSolution(aux);
                                pos1 = posicao1;
                                pos2 = posicao2;
                                atualizaListaTabu = true;

                            }
                        }
                        vizinho.clear();
                    }
                }

                if (atualizaListaTabu) {
                    listaTabuMovimento[pos2][pos1] = iteracao + duracaoTabu;
                }

                break;

            case 4: // aleatoria

                Random r1 = new Random();

                int escolhaVizinho;

                Random r2 = new Random(System.nanoTime());

                int qtd = (int) (0.1 * (original.size() * original.size()));

                int elemento,
                 posicao,
                 posicao1,
                 posicao2;

                for (int i = 0; i < qtd; i++) {//???QUANTAS VEZES S�O NECESS�RIAS...

                    vizinho.addAll(original);

                    escolhaVizinho = r1.nextInt(120);

                    if (escolhaVizinho < 20 || escolhaVizinho >= 60 && escolhaVizinho < 80) {
                        //Troca

                        posicao1 = r1.nextInt(original.size());

                        do {
                            posicao2 = r2.nextInt(original.size());
                        } while (vizinho.get(posicao1) == vizinho.get(posicao2));

                        Collections.swap(vizinho, posicao1, posicao2);

                        aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                        if (aux.getTotalDistance() < melhor.getTotalDistance()
                                && ((listaTabuTroca[posicao2][posicao1] <= iteracao && listaTabuTroca[posicao1][posicao2] <= iteracao)
                                || aux.getTotalDistance() < estrela.getTotalDistance())) {

                            melhor.setSolution(aux);
                            pos1 = posicao1;
                            pos2 = posicao2;

                            listaTabuTroca[pos2][pos1] = iteracao + duracaoTabu;
                            listaTabuTroca[pos1][pos2] = iteracao + duracaoTabu;
                        }
                    } else if (escolhaVizinho >= 20 && escolhaVizinho < 40 || escolhaVizinho >= 80 && escolhaVizinho < 100) {
                        //Substituicao

                        posicao = r1.nextInt(original.size());

                        do {
                            elemento = r2.nextInt(n);
                        } while (elemento == 0 || elemento == vizinho.get(posicao));

                        vizinho.set(posicao, elemento);

                        aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                        if (aux.getTotalDistance() < melhor.getTotalDistance()
                                && (listaTabuSubstituicao[posicao][elemento] <= iteracao || aux.getTotalDistance() < estrela.getTotalDistance())) {

                            melhor.setSolution(aux);
                            pos = posicao;
                            elem = original.get(posicao);

                            listaTabuSubstituicao[pos][elem] = iteracao + duracaoTabu;
                        }
                    } else {
                        //Movimento

                        posicao1 = r1.nextInt(original.size());

                        do {
                            posicao2 = r2.nextInt(original.size());
                        } while (posicao1 == posicao2);

                        vizinho.remove(posicao1);
                        vizinho.add(posicao2, original.get(posicao1));

                        aux.setSolution(rebuildSolution(reducedDimension, parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                        if (aux.getTotalDistance() < melhor.getTotalDistance()
                                && (listaTabuMovimento[posicao1][posicao2] <= iteracao || aux.getTotalDistance() < estrela.getTotalDistance())) {

                            melhor.setSolution(aux);
                            pos1 = posicao1;
                            pos2 = posicao2;

                            listaTabuMovimento[pos2][pos1] = iteracao + duracaoTabu;
                        }
                    }
                    vizinho.clear();
                }
                break;
        }
        return melhor;
    }

    public static void initializeFleetOfVehicles(Set<Integer> setOfVehicles, final Integer numberOfVehicles) {
        for (int i = 0; i < numberOfVehicles; i++) {
            setOfVehicles.add(i);
        }
    }
}
