/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import static Algorithms.Algorithms.*;
import static Algorithms.Methods.*;
import java.io.*;
import java.util.*;
import ProblemRepresentation.*;
import static Algorithms.Algorithms.*;
import static Algorithms.Methods.*;
import AlgorithmsResults.ResultsGraphicsForParetoCombinedSet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import static Algorithms.Algorithms.*;
import AlgorithmsResults.ResultsGraphicsForConvergence;
import ReductionTechniques.CorrelationType;
import ReductionTechniques.HierarchicalCluster;
import java.text.DecimalFormat;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Renan
 */
public class EvolutionaryAlgorithms {

    public static void MOGA(List<Double> parameters, List<ProblemSolution> Pop, Integer TamPop, Integer MaxGer, double Pm, double Pc, List<Request> listRequests, Map<Integer, List<Request>> Pin,
            Map<Integer, List<Request>> Pout, Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P, List<Integer> m,
            List<List<Long>> d, List<List<Long>> c, Long TimeWindows, Long currentTime, Integer lastNode) {
        String diretorio, nomeArquivo;
        try {
            List<Integer> pais = new ArrayList<>();

            double tempoInicio, tempoFim;
            //SBest = copyBestSolution(Pop, SBest);
            //System.out.println("Melhor Individuo = " + SBest);
            int somaTotal;
            double media, desvio;
            diretorio = "\\home\\renanMulti";
            nomeArquivo = "MOGA";
            boolean success = (new File(diretorio)).mkdirs();
            if (!success) {
                System.out.println("Diretórios ja existem!");
            }
            PrintStream saida;
            saida = new PrintStream(diretorio + "\\" + nomeArquivo + ".txt");
            //----------------------------------------------------------------------------------------------------
            for (int cont = 0; cont < 1; cont++) {
                //--------------- initializePopulation com a mesma população inicial ------------------
                List<ProblemSolution> naoDominados = new ArrayList();
                List<ProblemSolution> arquivo = new ArrayList();
                double dist[][] = new double[TamPop][TamPop];
                double sigmaSH = Math.sqrt(2) / 60;// 0.05;//deixado para o algoritmo calcular o raio do nicho
                double sigma = 1;
                double alfa = 1;
                int TamMax = 60;
                initializePopulation(Pop, TamPop, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode);
                for (int i = 0; i < TamPop; i++) {
                    ProblemSolution s = new ProblemSolution();
                    s.setSolution(vizinhoAleatorio(parameters, Pop.get(i), i, i + 1, 1, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
                    Pop.get(i).setSolution(s);
                }

                dominanceAlgorithm(Pop, naoDominados);
                System.out.println("Primeira Fronteira");
                printPopulation(naoDominados);
                System.out.println("\n\n\n");
                fitnessEvaluationForMultiObjectiveOptimization(Pop);
                normalizeObjectiveFunctionsValues(Pop);
                evaluateDistanceBetweenSolutions(Pop, dist);
                printPopulation(Pop);

                System.out.println("Execução = " + cont);
                int GerAtual = 0;
                while (GerAtual < MaxGer) {
                    updateSolutionsFile(Pop, arquivo, TamMax);
                    populationSorting(Pop);
                    mutation2Shuffle(parameters, Pop, Pm, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows, currentTime, lastNode);
                    dominanceAlgorithm(Pop, naoDominados);
                    updateSolutionsFile(Pop, arquivo, TamMax);
                    fitnessEvaluationForMultiObjectiveOptimization(Pop);
                    normalizeObjectiveFunctionsValues(Pop);
                    evaluateDistanceBetweenSolutions(Pop, dist);
                    System.out.println("Geração = " + GerAtual);
                    GerAtual++;
                }
                List<ProblemSolution> melhores = new ArrayList<>();
                dominanceAlgorithm(arquivo, melhores);
                normalizeObjectiveFunctionsValues(melhores);
                //melhores.addAll(naoDominados);
                System.out.println("Pareto final");
                printPopulation(melhores);
                //ImprimePopulacao(arquivo);
                Pop.clear();

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static double NSGAII(String instanceName, List<Double> parameters, List<Double> nadirPoint, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> requests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {

        List<ProblemSolution> offspring = new ArrayList<>();
        List<ProblemSolution> population = new ArrayList<>();
        List<ProblemSolution> finalPareto = new ArrayList<>();
        List<ProblemSolution> nonDominatedSolutions = new ArrayList();
        List<ProblemSolution> fileWithSolutions = new ArrayList();
        List<Integer> parents = new ArrayList<>();
        List<ProblemSolution> parentsAndOffspring = new ArrayList();
        List<List<ProblemSolution>> nonDominatedFronts = new ArrayList<>();
        List<List<Double>> hypervolumes = new ArrayList<>();
        double hypervolume = 0;
        String folderName, fileName;

        LocalDateTime time = LocalDateTime.now();
        folderName = "AlgorithmsResults//9FO//NSGA-II//" + instanceName + "k" + vehicleCapacity + "_" + time.getYear() + "_" + time.getMonthValue() + "_" + time.getDayOfMonth();
        fileName = "NSGAII";

        boolean success = (new File(folderName)).mkdirs();
        if (!success) {
            System.out.println("Folder already exists!");
        }
        try {
            List<ProblemSolution> combinedPareto = new ArrayList<>();
            PrintStream printStreamForCombinedPareto = new PrintStream(folderName + "/" + fileName + "-Pareto_Combinado.txt");
            PrintStream printStreamForObjectiveFunctionOfCombinedPareto = new PrintStream(folderName + "/" + fileName + "-Pareto_Combinado_Funcoes_Objetivo.txt");
            PrintStream printStreamForAllObjectives = new PrintStream(folderName + "/nsga_pareto_9fo.csv");
            PrintStream printStreamForAllObjectives2 = new PrintStream(folderName + "/nsga_pareto_reduced.txt");
            for (int executionCounter = 0; executionCounter < maximumNumberOfExecutions; executionCounter++) {
                String executionNumber;
                List<Double> listOfHypervolumes = new ArrayList<>();
                executionNumber = Integer.toString(executionCounter);
                PrintStream saida1 = new PrintStream(folderName + "/" + fileName + "-Execucao-" + executionNumber + ".txt");
                PrintStream saida2 = new PrintStream(folderName + "/" + fileName + "-tamanho_arquivo-" + executionNumber + ".txt");
                PrintStream saida3 = new PrintStream(folderName + "/" + fileName + "-teste-" + executionNumber + ".csv");
                PrintStream saida4 = new PrintStream(folderName + "/" + fileName + "-Pareto-" + executionNumber + ".csv");
                int maximumSize;

//                inicializePopulation(population, populationSize, requests,
//                        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
//                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
                inicializeRandomPopulation(parameters, population, populationSize, requests,
                        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

                int numberOfClusters = 2;
                //normalizeObjectiveFunctionsValues(population);
                //normalizeObjectiveFunctions(population);
                normalizeObjectiveFunctionsForSolutions(population);
                evaluateAggregatedObjectiveFunctions(parameters, population);

                //printPopulation(population);
                dominanceAlgorithm(population, nonDominatedSolutions);//possivel local do erro
                maximumSize = population.size();
                offspring.addAll(population);
                nonDominatedFrontiersSortingAlgorithm(offspring, nonDominatedFronts);
                fitnessEvaluationForMultiObjectiveOptimization(offspring);

                rouletteWheelSelectionAlgorithm(parents, offspring, maximumSize);

                twoPointsCrossover(parameters, offspring, population, maximumSize, probabilityOfCrossover, parents, requests,
                        requestList, setOfVehicles, listOfNonAttendedRequests, requestsWhichBoardsInNode,
                        requestsWhichLeavesInNode, timeBetweenNodes, distanceBetweenNodes, numberOfNodes,
                        vehicleCapacity, timeWindows);

                mutation2Shuffle(parameters, offspring, probabilityOfMutation, requests, requestsWhichBoardsInNode,
                        requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

                //normalizeObjectiveFunctionsValues(fileWithSolutions);
                normalizeObjectiveFunctionsForSolutions(fileWithSolutions);
                evaluateAggregatedObjectiveFunctions(parameters, fileWithSolutions);

                normalizeObjectiveFunctionsForSolutions(offspring);
                evaluateAggregatedObjectiveFunctions(parameters, offspring);
                //normalizeObjectiveFunctions(fileWithSolutions);
                //normalizeObjectiveFunctions(fileWithSolutions);
                for (ProblemSolution s : fileWithSolutions) {
                    saida1.print("\t" + s.getAggregatedObjective1() + "\t" + s.getAggregatedObjective2() + "\n");
                    saida3.print("\t" + s.getAggregatedObjective1Normalized() + "\t" + s.getAggregatedObjective2Normalized() + "\n");
                }
                saida1.print("\n\n");
                saida2.print(fileWithSolutions.size() + "\n");
                saida3.print("\n\n");

                //dominanceAlgorithm(offspring, nonDominatedSolutions);
                //fileWithSolutions.addAll(nonDominatedSolutions);
                System.out.println("Execution = " + executionCounter);
                int actualGeneration = 0;
                while (actualGeneration < maximumNumberOfGenerations) {
                    //saveCurrentPopulation(population, actualGeneration, folderName, fileName);
                    dominanceAlgorithm(offspring, nonDominatedSolutions);
                    fileWithSolutions.addAll(nonDominatedSolutions);
                    nonDominatedFrontiersSortingAlgorithm(offspring, nonDominatedFronts);
                    fitnessEvaluationForMultiObjectiveOptimization(offspring);
                    parentsAndOffspring.clear();

                    parentsAndOffspring.addAll(population);
                    parentsAndOffspring.addAll(offspring);

                    nonDominatedFrontiersSortingAlgorithm(parentsAndOffspring, nonDominatedFronts);
                    fitnessEvaluationForMultiObjectiveOptimization(parentsAndOffspring);
                    dominanceAlgorithm(parentsAndOffspring, nonDominatedSolutions);

                    updateNSGASolutionsFileTest(parentsAndOffspring, fileWithSolutions, maximumSize);
                    //normalizeObjectiveFunctionsValues(fileWithSolutions);
                    normalizeObjectiveFunctionsForSolutions(fileWithSolutions);
                    evaluateAggregatedObjectiveFunctions(parameters, fileWithSolutions);
                    //normalizeObjectiveFunctions(fileWithSolutions);
                    reducePopulation(population, nonDominatedFronts, maximumSize);
                    offspring.clear();

                    offspring.addAll(population);
                    rouletteWheelSelectionAlgorithm(parents, offspring, maximumSize);

                    twoPointsCrossover(parameters, offspring, population, maximumSize, probabilityOfCrossover, parents, requests, requestList, setOfVehicles, listOfNonAttendedRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, timeBetweenNodes, distanceBetweenNodes, numberOfNodes, vehicleCapacity, timeWindows);
                    mutation2Shuffle(parameters, offspring, probabilityOfMutation, requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
                    //normalizeObjectiveFunctionsValues(offspring);
                    normalizeAggregatedObjectiveFunctions(offspring);

                    normalizeObjectiveFunctionsForSolutions(offspring);
                    evaluateAggregatedObjectiveFunctions(parameters, offspring);

                    System.out.println("Generation = " + actualGeneration + "\t" + fileWithSolutions.size());

                    listOfHypervolumes.add(smetric(fileWithSolutions, nadirPoint));

                    for (ProblemSolution s : fileWithSolutions) {
                        saida1.print("\t" + s.getAggregatedObjective1() + "\t" + s.getAggregatedObjective2() + "\n");
                        //saida3.print("\t" + s.getAggregatedObjective1Normalized() + "\t" + s.getAggregatedObjective2Normalized() + "\n");
                        saida3.print("\t" + s.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
                    }
                    saida1.print("\n\n");
                    saida2.print(fileWithSolutions.size() + "\n");
                    saida3.print("\n\n");
                    actualGeneration++;
                }

                for (ProblemSolution s : fileWithSolutions) {
                    saida4.print(s.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
                }

                offspring.clear();
                parentsAndOffspring.clear();
                combinedPareto.addAll(fileWithSolutions);
                fileWithSolutions.clear();
                population.clear();
                nonDominatedFronts.clear();
                hypervolumes.add(listOfHypervolumes);
            }

            dominanceAlgorithm(combinedPareto, finalPareto);
            printPopulation(finalPareto);
            for (ProblemSolution individual : finalPareto) {
                printStreamForCombinedPareto.print(individual + "\n");
                printStreamForObjectiveFunctionOfCombinedPareto.print(individual.getStringWithObjectives() + "\n");
                printStreamForAllObjectives.print(individual.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
                printStreamForAllObjectives2.print(individual.getAggregatedObjective1() + "\t" + individual.getAggregatedObjective2() + "\n");
            }

            new ResultsGraphicsForParetoCombinedSet(finalPareto, "ResultGraphics", "CombinedParetoSet");
            hypervolume = smetric(finalPareto, nadirPoint);
            //hypervolume = smetric(finalPareto);
            System.out.println("S-Metric = " + hypervolume);
//            System.out.println("Final Pareto");
//            DecimalFormat formatator = new DecimalFormat("0.0000");
//            finalPareto.forEach(u -> System.out.println(
//                    formatator.format(u.getAggregatedObjective1()).replace(",", ".")
//                    + "\t" + formatator.format(u.getAggregatedObjective2()).replace(",", "."))
//            );

            //System.out.println("List of Lists = " + hypervolumes);
            saveHypervolumesDatas(hypervolumes, maximumNumberOfGenerations, maximumNumberOfExecutions, folderName, fileName);
//            finalPareto.get(0).getStaticMapForEveryRoute(new NodeDAO("bh_nodes_little").getListOfNodes(),
//                    "adjacencies_bh_nodes_little_test", "bh_nodes_little");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return hypervolume;
    }

    public static double CL_NSGAII(String instanceName, List<Double> parameters, List<Double> nadirPoint, Integer populationSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> requests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {

        List<ProblemSolution> offspring = new ArrayList<>();
        List<ProblemSolution> population = new ArrayList<>();
        List<ProblemSolution> finalPareto = new ArrayList<>();
        List<ProblemSolution> nonDominatedSolutions = new ArrayList();
        List<ProblemSolution> fileWithSolutions = new ArrayList();
        List<Integer> parents = new ArrayList<>();
        List<ProblemSolution> parentsAndOffspring = new ArrayList();
        List<List<ProblemSolution>> nonDominatedFronts = new ArrayList<>();
        List<List<Double>> hypervolumes = new ArrayList<>();
        double hypervolume = 0;
        String folderName, fileName;

        LocalDateTime time = LocalDateTime.now();
        folderName = "AlgorithmsResults//9FO//CL_NSGA-II//" + instanceName + "k" + vehicleCapacity + "_" + time.getYear() + "_" + time.getMonthValue() + "_" + time.getDayOfMonth();
        fileName = "NSGAII";

        boolean success = (new File(folderName)).mkdirs();
        if (!success) {
            System.out.println("Folder already exists!");
        }
        try {
            List<ProblemSolution> combinedPareto = new ArrayList<>();
            PrintStream printStreamForCombinedPareto = new PrintStream(folderName + "/" + fileName + "-Pareto_Combinado.txt");
            PrintStream printStreamForObjectiveFunctionOfCombinedPareto = new PrintStream(folderName + "/" + fileName + "-Pareto_Combinado_Funcoes_Objetivo.txt");
            PrintStream printStreamForAllObjectives = new PrintStream(folderName + "/nsga_pareto_9fo.csv");
            PrintStream printStreamForAllObjectives2 = new PrintStream(folderName + "/nsga_pareto_reduced.txt");
            for (int executionCounter = 0; executionCounter < maximumNumberOfExecutions; executionCounter++) {
                String executionNumber;
                List<Double> listOfHypervolumes = new ArrayList<>();
                executionNumber = Integer.toString(executionCounter);
                PrintStream saida1 = new PrintStream(folderName + "/" + fileName + "-Execucao-" + executionNumber + ".txt");
                PrintStream saida2 = new PrintStream(folderName + "/" + fileName + "-tamanho_arquivo-" + executionNumber + ".txt");
                PrintStream saida3 = new PrintStream(folderName + "/" + fileName + "-teste-" + executionNumber + ".csv");
                PrintStream saida4 = new PrintStream(folderName + "/" + fileName + "-Pareto-" + executionNumber + ".csv");

                int maximumSize;

//                inicializePopulation(population, populationSize, requests,
//                        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
//                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
                inicializeRandomPopulation(parameters, population, populationSize, requests,
                        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

                int numberOfClusters = 2;
                HierarchicalCluster hc = new HierarchicalCluster(getMatrixOfObjetives(population, parameters), numberOfClusters, CorrelationType.KENDALL);
                hc.reduce();
                hc.getTransfomationList().forEach(System.out::println);
                hc.setTransformationList(createTransformationList());
                hc.getTransfomationList().forEach(System.out::println);
                //normalizeObjectiveFunctionsValues(population);
                //normalizeObjectiveFunctions(population);
                normalizeObjectiveFunctionsForSolutions(population);
                evaluateAggregatedObjectiveFunctions(parameters, hc.getTransfomationList(), population);

                //printPopulation(population);
                dominanceAlgorithm(population, nonDominatedSolutions);
                maximumSize = population.size();
                offspring.addAll(population);
                nonDominatedFrontiersSortingAlgorithm(offspring, nonDominatedFronts);
                fitnessEvaluationForMultiObjectiveOptimization(offspring);

                rouletteWheelSelectionAlgorithm(parents, offspring, maximumSize);

                twoPointsCrossover(parameters, offspring, population, maximumSize, probabilityOfCrossover, parents, requests,
                        requestList, setOfVehicles, listOfNonAttendedRequests, requestsWhichBoardsInNode,
                        requestsWhichLeavesInNode, timeBetweenNodes, distanceBetweenNodes, numberOfNodes,
                        vehicleCapacity, timeWindows);

                mutation2Shuffle(parameters, offspring, probabilityOfMutation, requests, requestsWhichBoardsInNode,
                        requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

                //normalizeObjectiveFunctionsValues(fileWithSolutions);
                normalizeObjectiveFunctionsForSolutions(fileWithSolutions);
                evaluateAggregatedObjectiveFunctions(parameters, hc.getTransfomationList(), fileWithSolutions);

                normalizeObjectiveFunctionsForSolutions(offspring);
                evaluateAggregatedObjectiveFunctions(parameters, hc.getTransfomationList(), offspring);
                //normalizeObjectiveFunctions(fileWithSolutions);
                //normalizeObjectiveFunctions(fileWithSolutions);
                for (ProblemSolution s : fileWithSolutions) {
                    saida1.print("\t" + s.getAggregatedObjective1() + "\t" + s.getAggregatedObjective2() + "\n");
                    saida3.print("\t" + s.getAggregatedObjective1Normalized() + "\t" + s.getAggregatedObjective2Normalized() + "\n");
                }
                saida1.print("\n\n");
                saida2.print(fileWithSolutions.size() + "\n");
                saida3.print("\n\n");

                //dominanceAlgorithm(offspring, nonDominatedSolutions);
                //fileWithSolutions.addAll(nonDominatedSolutions);
                System.out.println("Execution = " + executionCounter);
                int actualGeneration = 0;
                while (actualGeneration < maximumNumberOfGenerations) {
                    //saveCurrentPopulation(population, actualGeneration, folderName, fileName);
                    dominanceAlgorithm(offspring, nonDominatedSolutions);
                    fileWithSolutions.addAll(nonDominatedSolutions);

//                    if (actualGeneration % 10 == 0) {
                        hc = new HierarchicalCluster(getMatrixOfObjetives(population, parameters), numberOfClusters, CorrelationType.KENDALL);
                        hc.reduce();
                        hc.getTransfomationList().forEach(System.out::println);
//                    }

                    nonDominatedFrontiersSortingAlgorithm(offspring, nonDominatedFronts);
                    fitnessEvaluationForMultiObjectiveOptimization(offspring);
                    parentsAndOffspring.clear();

                    parentsAndOffspring.addAll(population);
                    parentsAndOffspring.addAll(offspring);

                    nonDominatedFrontiersSortingAlgorithm(parentsAndOffspring, nonDominatedFronts);
                    fitnessEvaluationForMultiObjectiveOptimization(parentsAndOffspring);
                    dominanceAlgorithm(parentsAndOffspring, nonDominatedSolutions);

                    updateNSGASolutionsFileTest(parentsAndOffspring, fileWithSolutions, maximumSize);
                    //normalizeObjectiveFunctionsValues(fileWithSolutions);
                    normalizeObjectiveFunctionsForSolutions(fileWithSolutions);
                    evaluateAggregatedObjectiveFunctions(parameters, hc.getTransfomationList(), fileWithSolutions);
                    //normalizeObjectiveFunctions(fileWithSolutions);
                    reducePopulation(population, nonDominatedFronts, maximumSize);
                    offspring.clear();

                    offspring.addAll(population);
                    rouletteWheelSelectionAlgorithm(parents, offspring, maximumSize);

                    twoPointsCrossover(parameters, offspring, population, maximumSize, probabilityOfCrossover, parents, requests, requestList, setOfVehicles, listOfNonAttendedRequests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, timeBetweenNodes, distanceBetweenNodes, numberOfNodes, vehicleCapacity, timeWindows);
                    mutation2Shuffle(parameters, offspring, probabilityOfMutation, requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
                    //normalizeObjectiveFunctionsValues(offspring);
                    normalizeAggregatedObjectiveFunctions(offspring);

                    normalizeObjectiveFunctionsForSolutions(offspring);
                    evaluateAggregatedObjectiveFunctions(parameters, hc.getTransfomationList(), offspring);

                    System.out.println("Generation = " + actualGeneration + "\t" + fileWithSolutions.size() + "\t" + population.size());

                    listOfHypervolumes.add(smetric(fileWithSolutions, nadirPoint));

                    for (ProblemSolution s : fileWithSolutions) {
                        saida1.print("\t" + s.getAggregatedObjective1() + "\t" + s.getAggregatedObjective2() + "\n");
                        saida3.print("\t" + s.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
                    }
                    saida1.print("\n\n");
                    saida2.print(fileWithSolutions.size() + "\n");
                    saida3.print("\n\n");
                    actualGeneration++;
                }

                for (ProblemSolution s : fileWithSolutions) {
                    saida4.print(s.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
                }

                offspring.clear();
                parentsAndOffspring.clear();
                combinedPareto.addAll(fileWithSolutions);
                fileWithSolutions.clear();
                population.clear();
                nonDominatedFronts.clear();
                hypervolumes.add(listOfHypervolumes);
            }

            dominanceAlgorithm(combinedPareto, finalPareto);
            printPopulation(finalPareto);
            for (ProblemSolution individual : finalPareto) {
                printStreamForCombinedPareto.print(individual + "\n");
                printStreamForObjectiveFunctionOfCombinedPareto.print(individual.getStringWithObjectives() + "\n");
                printStreamForAllObjectives.print(individual.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
                printStreamForAllObjectives2.print(individual.getAggregatedObjective1() + "\t" + individual.getAggregatedObjective2() + "\n");
            }

            //new ResultsGraphicsForParetoCombinedSet(finalPareto, "ResultGraphics", "CombinedParetoSet");
            hypervolume = smetric(finalPareto, nadirPoint);
            //hypervolume = smetric(finalPareto);
            System.out.println("S-Metric = " + hypervolume);
//            System.out.println("Final Pareto");
//            DecimalFormat formatator = new DecimalFormat("0.0000");
//            finalPareto.forEach(u -> System.out.println(
//                    formatator.format(u.getAggregatedObjective1()).replace(",", ".")
//                    + "\t" + formatator.format(u.getAggregatedObjective2()).replace(",", "."))
//            );

            //System.out.println("List of Lists = " + hypervolumes);
            saveHypervolumesDatas(hypervolumes, maximumNumberOfGenerations, maximumNumberOfExecutions, folderName, fileName);
//            finalPareto.get(0).getStaticMapForEveryRoute(new NodeDAO("bh_nodes_little").getListOfNodes(),
//                    "adjacencies_bh_nodes_little_test", "bh_nodes_little");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return hypervolume;
    }

    public static List<List<Integer>> createTransformationList() {
        List<List<Integer>> transformationList = new ArrayList<>();
        List<Integer> line1 = new ArrayList<>();
        List<Integer> line2 = new ArrayList<>();

        line1.add(1);
        line1.add(1);
        line1.add(1);
        line1.add(1);
        line1.add(1);
        line1.add(1);
        line1.add(1);
        line1.add(1);
        line1.add(0);
        transformationList.add(line1);

        line2.add(0);
        line2.add(0);
        line2.add(0);
        line2.add(0);
        line2.add(0);
        line2.add(0);
        line2.add(0);
        line2.add(0);
        line2.add(1);
        transformationList.add(line2);
        return transformationList;
    }

    public static double[][] getMatrixOfObjetives(List<ProblemSolution> population) {
        int rows = population.size();
        int columns = population.get(0).getObjectives().size();
        double[][] matrix = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = population.get(i).getObjectives().get(j);
            }
        }
        return matrix;
    }

    public static double[][] getMatrixOfObjetives(List<ProblemSolution> population, List<Double> parameters) {
        int rows = population.size();
        int columns = population.get(0).getObjectives().size();
        double[][] matrix = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = population.get(i).getObjectives().get(j) * parameters.get(j);
            }
        }
        return matrix;
    }

    private static void saveCurrentPopulation(List<ProblemSolution> population, int actualGeneration, String folderName,
            String fileName) throws FileNotFoundException {
        PrintStream printStreamForCombinedPareto = new PrintStream(folderName + "/" + fileName
                + "-Population_" + actualGeneration + ".csv");

        for (ProblemSolution individual : population) {
            printStreamForCombinedPareto.print(individual.getStringWithAllNonReducedObjectivesForCsvFile() + "\n");
        }

    }

    public static void saveHypervolumesDatas(List<List<Double>> hypervolumes, int maximumNumberOfGenerations,
            int maximumNumberOfExecutions, String folderName, String fileName) throws FileNotFoundException, IOException {

        PrintStream boxplot = new PrintStream(folderName + "/" + fileName + "-hypervolume-boxplot.txt");
        PrintStream convergence = new PrintStream(folderName + "/" + fileName + "-hypervolume-convergence.txt");
        List<Double> hypervolumeConvergence = new ArrayList<>();

        for (int i = 0; i < maximumNumberOfExecutions; i++) {
            boxplot.print(hypervolumes.get(i).get(maximumNumberOfGenerations - 1) + "\n");
        }

        for (int j = 0; j < maximumNumberOfGenerations; j++) {
            List<Double> hypervolumesForGeneration = new ArrayList<>();
            for (int i = 0; i < maximumNumberOfExecutions; i++) {
                hypervolumesForGeneration.add(hypervolumes.get(i).get(j));
            }
            double average = hypervolumesForGeneration.stream().mapToDouble(Double::valueOf).average().getAsDouble();
            convergence.print(average + "\n");
            hypervolumeConvergence.add(average);
        }

        //showHypervolumeConvergence(hypervolumeConvergence);
    }

    private static void showHypervolumeConvergence(List<Double> hypervolumeConvergence) throws IOException {
        new ResultsGraphicsForConvergence(hypervolumeConvergence, "ResultGraphics", "Convergence");
    }

    public static double smetric(List<ProblemSolution> solutions) {
        solutions.sort(Comparator.comparingDouble(ProblemSolution::getAggregatedObjective1));
        //        .thenComparingDouble(ProblemSolution::getAggregatedObjective2).reversed());

        double x_nadir = 1;
        double y_nadir = 1;

        List<Double> volumes = new ArrayList<>();

        for (int i = 0; i < solutions.size(); i++) {
            if (i == 0) {
                volumes.add((x_nadir - solutions.get(i).getAggregatedObjective1())
                        * (y_nadir - solutions.get(i).getAggregatedObjective2()));
            } else {
                volumes.add((x_nadir - solutions.get(i).getAggregatedObjective1())
                        * (solutions.get(i - 1).getAggregatedObjective2() - solutions.get(i).getAggregatedObjective2()));
            }
        }

        return volumes.stream().mapToDouble(Double::valueOf).sum();
    }

    public static double smetric(List<ProblemSolution> solutions, List<Double> nadirPoint) {
        solutions.sort(Comparator.comparingDouble(ProblemSolution::getAggregatedObjective1));

        double x_nadir = 1.0;
        double y_nadir = 1.0;
        normalizeAggregatedObjectiveFunctions(solutions, nadirPoint);

        List<Double> volumes = new ArrayList<>();

        for (int i = 0; i < solutions.size(); i++) {
            if (i == 0) {
                volumes.add((x_nadir - solutions.get(i).getAggregatedObjective1Normalized())
                        * (y_nadir - solutions.get(i).getAggregatedObjective2Normalized()));
            } else {
                volumes.add((x_nadir - solutions.get(i).getAggregatedObjective1Normalized())
                        * (solutions.get(i - 1).getAggregatedObjective2Normalized() - solutions.get(i).getAggregatedObjective2Normalized()));
            }
        }

        return volumes.stream().mapToDouble(Double::valueOf).sum();
    }

    public static void normalizeAggregatedObjectiveFunctions(List<ProblemSolution> solutions, List<Double> nadirPoint) {
        solutions.forEach(s -> {
            s.setAggregatedObjective1Normalized(s.getAggregatedObjective1() / nadirPoint.get(0));
            s.setAggregatedObjective2Normalized(s.getAggregatedObjective2() / nadirPoint.get(1));
        });
    }

    public static void SPEA2(String instanceName, List<Double> parameters, List<Double> nadirPoint, Integer populationSize,
            Integer fileSize, Integer maximumNumberOfGenerations,
            Integer maximumNumberOfExecutions, double probabilityOfMutation, double probabilityOfCrossover,
            List<Request> requests, Map<Integer, List<Request>> requestsWhichBoardsInNode,
            Map<Integer, List<Request>> requestsWhichLeavesInNode, Integer numberOfNodes, Integer vehicleCapacity,
            Set<Integer> setOfVehicles, List<Request> listOfNonAttendedRequests, List<Request> requestList,
            List<Integer> loadIndexList, List<List<Long>> timeBetweenNodes, List<List<Long>> distanceBetweenNodes,
            Long timeWindows, Long currentTime, Integer lastNode) throws IOException {

        List<ProblemSolution> population = new ArrayList<>();
        List<ProblemSolution> nonDominated = new ArrayList();
        List<ProblemSolution> file = new ArrayList();
        List<Integer> parents = new ArrayList<>();
        List<ProblemSolution> parentsAndOffspring = new ArrayList();
        String folderName, fileName;
        List<List<Double>> hypervolumes = new ArrayList<>();
        double hypervolume = 0;
        LocalDateTime time = LocalDateTime.now();

        folderName = "AlgorithmsResults//9FO//SPEA2//" + instanceName + "k" + vehicleCapacity + "_" + time.getYear() + "_" + time.getMonthValue() + "_" + time.getDayOfMonth();
        fileName = "SPEA2";

        boolean success = (new File(folderName)).mkdirs();
        if (!success) {
            System.out.println("Folder already exists!");
        }
        try {
            List<ProblemSolution> combinedPareto = new ArrayList<>();
            for (int executionCounter = 0; executionCounter < maximumNumberOfExecutions; executionCounter++) {
                List<Double> listOfHypervolumes = new ArrayList<>();
                String number;
                int tamMax;
                double dist[][] = new double[populationSize][populationSize];
                number = Integer.toString(executionCounter);
                PrintStream saida1 = new PrintStream(folderName + "/" + fileName + "-Execucao-" + number + ".txt");
                PrintStream saida2 = new PrintStream(folderName + "/" + fileName + "-tamanho_arquivo-" + number + ".txt");
                PrintStream saida3 = new PrintStream(folderName + "/" + fileName + "-Execucao-Normalizada-" + number + ".txt");

//                inicializePopulation(population, populationSize, requests,
//                        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
//                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
                inicializeRandomPopulation(parameters, population, populationSize, requests,
                        requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                        requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

                //printPopulation(population);
                normalizeObjectiveFunctionsValues(population);
                normalizeObjectiveFunctionsForSolutions(population);
                normalizeAggregatedObjectiveFunctions(population, nadirPoint);
                evaluateAggregatedObjectiveFunctions(parameters, population);

                tamMax = population.size();
                dominanceAlgorithm(population, file);
                evaluateDistanceBetweenSolutions(population, dist);

                int actualGeneration = 0;
                fitnessEvaluationForSPEA2(population, dist, populationSize, fileSize);
                System.out.println("Execution = " + executionCounter);

                while (actualGeneration < maximumNumberOfGenerations) {
                    fitnessEvaluationForSPEA2(population, dist, populationSize, fileSize);
                    parentsAndOffspring.addAll(population);
                    parentsAndOffspring.addAll(file);
                    dominanceAlgorithm(parentsAndOffspring, nonDominated);
                    updateSPEA2SolutionsFile(parentsAndOffspring, file, fileSize);

                    dominanceAlgorithm(file, nonDominated);
                    rouletteWheelSelectionAlgorithm(parents, file, tamMax);

                    onePointCrossover(parameters, population, file, fileSize, probabilityOfCrossover, parents, requests,
                            requestList, setOfVehicles, listOfNonAttendedRequests, requestsWhichBoardsInNode,
                            requestsWhichLeavesInNode, timeBetweenNodes, distanceBetweenNodes, numberOfNodes,
                            vehicleCapacity, timeWindows);

                    mutation2Opt(parameters, population, probabilityOfMutation, requests, requestsWhichBoardsInNode,
                            requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests,
                            requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);

                    evaluateAggregatedObjectiveFunctions(parameters, file);
                    evaluateAggregatedObjectiveFunctions(parameters, population);

                    fitnessEvaluationForSPEA2(file, dist, populationSize, fileSize);

                    normalizeObjectiveFunctionsValues(file);
                    normalizeObjectiveFunctionsForSolutions(file);
                    normalizeAggregatedObjectiveFunctions(file, nadirPoint);
                    dominanceAlgorithm(file, nonDominated);
                    actualGeneration++;
                    parentsAndOffspring.clear();
                    listOfHypervolumes.add(smetric(nonDominated, nadirPoint));
                    saveDataInTextFile(nonDominated, saida1, saida2, saida3);
                    System.out.println("Generation = " + actualGeneration + "\t" + nonDominated.size());
                    //actualGeneration
                }
                combinedPareto.addAll(file);
                file.clear();
                population.clear();
                parentsAndOffspring.clear();
                hypervolumes.add(listOfHypervolumes);
            }
            List<ProblemSolution> finalPareto = new ArrayList<>();
            dominanceAlgorithm(combinedPareto, finalPareto);
            PrintStream saida4 = new PrintStream(folderName + "/spea_pareto.txt");
            PrintStream saida5 = new PrintStream(folderName + "/spea_pareto_reduced.txt");
            PrintStream saida6 = new PrintStream(folderName + "/spea_pareto_9fo.txt");

            for (ProblemSolution solution : finalPareto) {
                saida4.print(solution + "\n");
                saida5.print(solution.getAggregatedObjective1() + "\t" + solution.getAggregatedObjective2() + "\n");
                saida6.print(solution.getStringWithAllNonReducedObjectives() + "\n");
            }
            printPopulation(finalPareto);

            //new ResultsGraphicsForParetoCombinedSet(finalPareto, "ResultGraphics", "CombinedParetoSet");
            hypervolume = smetric(finalPareto, nadirPoint);
            saveHypervolumesDatas(hypervolumes, maximumNumberOfGenerations, maximumNumberOfExecutions, folderName, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveDataInTextFile(List<ProblemSolution> arquivo, PrintStream saida1, PrintStream saida2, PrintStream saida3) {
        for (ProblemSolution s : arquivo) {
            saida1.print("\t" + s.getAggregatedObjective1() + "\t" + s.getAggregatedObjective2() + "\n");
            saida3.print("\t" + s.getAggregatedObjective1Normalized() + "\t" + s.getAggregatedObjective2Normalized() + "\n");
        }
        saida1.print("\n\n");
        saida2.print(arquivo.size() + "\n");
        saida3.print("\n\n");
    }

    public static void fitnessEvaluationForSPEA2(List<ProblemSolution> Pop, double[][] dist, Integer TamPop, Integer TamArq) {
        Integer k = (int) Math.sqrt(TamPop + TamArq);
        List<Double> lista = new ArrayList<>();
        double maximo = 0, minimo = 9999999;
        for (int i = 0; i < TamPop; i++) {
            lista.clear();
            for (int j = 0; j < TamPop; j++) {
                lista.add(dist[i][j]);
            }
            Collections.sort(lista);
            double fitness = Pop.get(i).getR() + 1 / (lista.get(k) + 2);//fitness(i) = R(i) + D(i)
            Pop.get(i).setFitness(fitness);
            if (fitness > maximo) {
                maximo = fitness;
            } else if (fitness < minimo) {
                minimo = fitness;
            }
        }

//        double maximo = Pop.stream().mapToDouble(ProblemSolution::getFitness).max().getAsDouble();
//        double minimo = Pop.stream().mapToDouble(ProblemSolution::getFitness).min().getAsDouble();
//
        double soma = 0;
        for (ProblemSolution s : Pop) {
            double fitness = (maximo - s.getFitness()) / (maximo - minimo);
            //double fitness = (s.getFitness() - minimo) / (maximo - minimo);
            s.setFitness(fitness);
            soma += fitness;
        }
        for (ProblemSolution s : Pop) {
            s.setFitness(s.getFitness() / soma);
        }
    }

    public static void reducePopulation(List<ProblemSolution> population, List<List<ProblemSolution>> fronts, int populationSize) {
        try {
            int frontsCounter = 0;
            population.clear();
            while (population.size() < populationSize) {
                population.addAll(fronts.get(frontsCounter));
                frontsCounter++;
                if (frontsCounter < fronts.size()) {
                    if ((population.size() + fronts.get(frontsCounter).size() > populationSize)
                            && (population.size() < populationSize)) {
                        crowdDistance(fronts.get(frontsCounter), population);
                        fronts.get(frontsCounter).subList(0, populationSize - population.size() - 1);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void crowdDistance(List<ProblemSolution> front, List<ProblemSolution> population) {

        front.sort(Comparator.comparing(ProblemSolution::getAggregatedObjective1Normalized)
                .thenComparing(ProblemSolution::getAggregatedObjective2Normalized)
                .reversed());

        double maxObjective1 = population.stream()
                .mapToDouble(ProblemSolution::getAggregatedObjective1Normalized)
                .max().getAsDouble();
        double minObjective1 = population.stream()
                .mapToDouble(ProblemSolution::getAggregatedObjective1Normalized)
                .min().getAsDouble();

        double maxObjective2 = population.stream()
                .mapToDouble(ProblemSolution::getAggregatedObjective2Normalized)
                .max().getAsDouble();
        double minObjective2 = population.stream()
                .mapToDouble(ProblemSolution::getAggregatedObjective2Normalized)
                .min().getAsDouble();

        front.get(0).setCrowdDistance(1000000000);
        front.get(front.size() - 1).setCrowdDistance(1000000000);

        for (int i = 1; i < front.size() - 1; i++) {
            int previousIndividual = i - 1;
            int posteriorIndividual = i + 1;

            double crowdDistance1 = front.get(i).getCrowdDistance()
                    + (front.get(posteriorIndividual).getAggregatedObjective1Normalized()
                    - front.get(previousIndividual).getAggregatedObjective1Normalized())
                    / (maxObjective1 - minObjective1);

            double crowdDistance2 = front.get(i).getCrowdDistance()
                    + (front.get(posteriorIndividual).getAggregatedObjective2Normalized()
                    - front.get(previousIndividual).getAggregatedObjective2Normalized())
                    / (maxObjective2 - minObjective2);

            front.get(i).setCrowdDistance(crowdDistance1 + crowdDistance2);
        }
        front.sort(Comparator.comparing(ProblemSolution::getCrowdDistance).reversed());
    }

    public static void fitnessEvalutaionForMOGA(List<ProblemSolution> Pop) {
        for (ProblemSolution s : Pop) {
            s.setFitness(s.getNumberOfSolutionsWichDomineThisSolution() + 1);
        }

        int soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            soma += Pop.get(i).getFitness();
        }
        List<Double> fit = new ArrayList<>();

        for (int i = 0; i < Pop.size(); i++) {
            fit.add((double) Pop.get(i).getFitness() / soma);
        }
        Collections.sort(fit);
        Collections.reverse(fit);

        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(fit.get(i));
        }
    }

    public static void fitnessEvalutaionForMOGA2(List<ProblemSolution> Pop) {
        List<Integer> frequencia = new ArrayList<>();
        List<Integer> fa = new ArrayList<>();
        for (ProblemSolution s : Pop) {
            s.setFitness(s.getNumberOfSolutionsWichDomineThisSolution() + 1);
        }

        Collections.sort(Pop);

        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i);
            fa.add(0);
        }

        for (int i = 0; i < frequencia.size(); i++) {
            int valor = frequencia.get(i);
            for (int j = 0; j < frequencia.size(); j++) {
                if (Pop.get(j).getNumberOfSolutionsWichDomineThisSolution() == valor) {
                    fa.set(valor, fa.get(i) + 1);
                }
            }
        }
        //System.out.println(fa);
        frequencia.clear();
        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i + 1);
        }

        //System.out.println(frequencia);
        int posicao = 0;
        for (int i = 0; i < fa.size(); i++) {
            int nvezes = fa.get(i);
            int soma = 0;

            for (int j = 0; j < nvezes; j++) {
                soma = soma + frequencia.get(j);
            }
            //System.out.println("soma = "+soma);
//           if(nvezes == 0){
//               nvezes = 1;
//               soma = frequencia.get(0);
//           }
            //System.out.println("soma = "+soma);
            double fitness;
            if (nvezes != 0) {
                fitness = (double) soma / nvezes;
            } else {
                fitness = (double) soma;
                //System.out.println("Teve zero!!!");
            }
            //System.out.println("fitness = "+ fitness);
            frequencia.subList(0, nvezes).clear();
            for (int k = posicao; k < (posicao + nvezes); k++) {
                Pop.get(k).setFitness(fitness);
            }
            posicao = posicao + nvezes;
        }

//       for(ProblemSolution s: Pop){
//           System.out.println("ProblemSolution s = "+s.getFitness());
//       }
        double soma = 0;

        double max = -999999;
        double min = 999999;
        for (int i = 0; i < Pop.size(); i++) {
            if (Pop.get(i).getFitness() > max) {
                max = Pop.get(i).getFitness();
            }
            if (Pop.get(i).getFitness() < min) {
                min = Pop.get(i).getFitness();
            }
        }
        //System.out.println(max);
        //System.out.println(min);
        for (int i = 0; i < Pop.size(); i++) {
            double fit = (max - Pop.get(i).getFitness()) / (max - min);
            soma = soma + fit;
            Pop.get(i).setFitness(fit);
        }
        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(Pop.get(i).getFitness() / soma);
        }
    }

    public static void fitnessEvalutaionForMOGA3(List<ProblemSolution> Pop) {
        List<Integer> mi = new ArrayList<>();
        List<Integer> frequencia = new ArrayList<>();
        List<Integer> fa = new ArrayList<>();

        for (ProblemSolution s : Pop) {
            s.setFitness(s.getNumberOfSolutionsWichDomineThisSolution() + 1);
        }
        populationSorting(Pop);

        int soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i);
            fa.add(0);
        }
        //System.out.println(frequencia);
        //System.out.println(fa);

        for (int i = 0; i < frequencia.size(); i++) {
            int valor = frequencia.get(i);
            for (int j = 0; j < frequencia.size(); j++) {
                if (Pop.get(j).getNumberOfSolutionsWichDomineThisSolution() == valor) {
                    fa.set(valor, fa.get(i) + 1);
                }
            }
        }
        //System.out.println(fa);
        frequencia.clear();

        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i + 1);
        }

        //----------------------------------------------------------------------------------    
        //System.out.println("Tamanho da população = " + Pop.size());
        ProblemSolution maior = new ProblemSolution();
        ProblemSolution menor = new ProblemSolution();

        maior.setSolution(Collections.max(Pop));
        menor.setSolution(Collections.min(Pop));
        //System.out.println(melhor);
        //System.out.println(pior);

        soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            soma += Pop.get(i).getFitness();
        }
        List<Double> fit = new ArrayList<>();

        for (int i = 0; i < Pop.size(); i++) {
            //fit.add((double) Pop.get(i).getFitness() / soma);
            fit.add((double) (maior.getFitness() - Pop.get(i).getFitness()) / soma);
        }
        //Collections.sort(fit);
        //Collections.reverse(fit);

        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(fit.get(i));
        }
        //----------------------------------------------------------------------------------
        double somaTeste = 0;
        for (int i = 0; i < Pop.size(); i++) {
            somaTeste += Pop.get(i).getFitness();
        }

        //System.out.println("Soma Total = " + somaTeste);
        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(Pop.get(i).getFitness() / somaTeste);
        }

        somaTeste = 0;
        for (int i = 0; i < Pop.size(); i++) {
            somaTeste += Pop.get(i).getFitness();
        }
    }

    public static void fitnessEvalutaionForMOGA4(List<ProblemSolution> Pop) {
        List<Integer> mi = new ArrayList<>();
        List<Integer> frequencia = new ArrayList<>();
        List<Integer> fa = new ArrayList<>();

        for (ProblemSolution s : Pop) {
            s.setFitness(s.getNumberOfSolutionsWichDomineThisSolution() + 1);
        }
        populationSorting(Pop);

        int soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i);
            fa.add(0);
        }
        //System.out.println(frequencia);
        //System.out.println(fa);

        for (int i = 0; i < frequencia.size(); i++) {
            int valor = frequencia.get(i);
            for (int j = 0; j < frequencia.size(); j++) {
                if (Pop.get(j).getNumberOfSolutionsWichDomineThisSolution() == valor) {
                    fa.set(valor, fa.get(i) + 1);
                }
            }
        }
        //System.out.println(fa);
        frequencia.clear();

        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i + 1);
        }

        //----------------------------------------------------------------------------------    
        //System.out.println("Tamanho da população = " + Pop.size());
        ProblemSolution maior = new ProblemSolution();
        ProblemSolution menor = new ProblemSolution();

        maior.setSolution(Collections.max(Pop));
        menor.setSolution(Collections.min(Pop));
        //System.out.println(melhor);
        //System.out.println(pior);

        soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            soma += Pop.get(i).getFitness();
        }
        List<Double> fit = new ArrayList<>();

        for (int i = 0; i < Pop.size(); i++) {
            //fit.add((double) Pop.get(i).getFitness() / soma);
            fit.add((double) (maior.getFitness() - Pop.get(i).getFitness()) / soma);
        }
        //Collections.sort(fit);
        //Collections.reverse(fit);

        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(fit.get(i));
        }
        //----------------------------------------------------------------------------------
        double somaTeste = 0;
        for (int i = 0; i < Pop.size(); i++) {
            somaTeste += Pop.get(i).getFitness();
        }

        //System.out.println("Soma Total = " + somaTeste);
        fit.clear();
        for (int i = 0; i < Pop.size(); i++) {
            fit.add((double) Pop.get(i).getFitness() / (somaTeste));//Pop.get(i).setFitness(Pop.get(i).getFitness()/somaTeste);
        }

        //System.out.println(fit);
        somaTeste = 0;
        for (int i = 0; i < Pop.size(); i++) {
            somaTeste += Pop.get(i).getFitness();
        }
    }

    public static void fitnessEvaluationForMultiObjectiveOptimization(List<ProblemSolution> Pop) {
        List<Integer> frequencia = new ArrayList<>();
        List<Integer> fa = new ArrayList<>();
        for (ProblemSolution s : Pop) {
            s.setFitness(s.getNumberOfSolutionsWichDomineThisSolution() + 1);
        }

        Collections.sort(Pop);

        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i);
            fa.add(0);
        }

        for (int i = 0; i < frequencia.size(); i++) {
            int valor = frequencia.get(i);
            for (int j = 0; j < frequencia.size(); j++) {
                if (Pop.get(j).getNumberOfSolutionsWichDomineThisSolution() == valor) {
                    fa.set(valor, fa.get(i) + 1);
                }
            }
        }
        frequencia.clear();
        for (int i = 0; i < Pop.size(); i++) {
            frequencia.add(i + 1);
        }

        int posicao = 0;
        for (int i = 0; i < fa.size(); i++) {
            int nvezes = fa.get(i);
            int soma = 0;

            for (int j = 0; j < nvezes; j++) {
                soma = soma + frequencia.get(j);
            }

            double fitness;
            if (nvezes != 0) {
                fitness = (double) soma / nvezes;
            } else {
                fitness = (double) soma;
            }
            frequencia.subList(0, nvezes).clear();
            for (int k = posicao; k < (posicao + nvezes); k++) {
                Pop.get(k).setFitness(fitness);
            }
            posicao = posicao + nvezes;
        }

        double soma = 0;

        double max = -999999;
        double min = 999999;
        for (int i = 0; i < Pop.size(); i++) {
            if (Pop.get(i).getFitness() > max) {
                max = Pop.get(i).getFitness();
            }
            if (Pop.get(i).getFitness() < min) {
                min = Pop.get(i).getFitness();
            }
        }

        for (int i = 0; i < Pop.size(); i++) {
            double fit = (max - Pop.get(i).getFitness()) / (max - min);
            soma = soma + fit;
            Pop.get(i).setFitness(fit);
        }
        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(Pop.get(i).getFitness() / soma);
        }
        //fitness sharing
        int TamPop = Pop.size();
        double dist[][] = new double[TamPop][TamPop];
        double sigmaSH = Math.sqrt(2) / 60;// 0.05;//deixado para o algoritmo calcular o raio do nicho
        double sigma = 1;
        double alfa = 1;

        fitnessValueSharing(Pop, dist, sigmaSH, sigma, alfa);
        normalizeFitness(Pop);

        double soma2 = 0;
        for (int i = 0; i < Pop.size(); i++) {
            soma2 += Pop.get(i).getFitness();
        }
    }

    public static void normalizeFitness(List<ProblemSolution> Pop) {
        double soma = 0;
        for (int i = 0; i < Pop.size(); i++) {
            soma += Pop.get(i).getFitness();
        }
        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setFitness(Pop.get(i).getFitness() / soma);
        }
    }

    public static void dominanceAlgorithm(List<ProblemSolution> population, List<ProblemSolution> nonDominated) {
        //--------------------------------------------------------------------------------------------------------------
        //List<Solucao> naoDominados = new ArrayList<>();
        nonDominated.clear();
        for (int i = 0; i < population.size(); i++) {
            population.get(i).setNumberOfSolutionsWichDomineThisSolution(0);
            population.get(i).setNumberOfDominatedSolutionsByThisSolution(0);
            population.get(i).setListOfSolutionsDominatedByThisSolution(new ArrayList<>());
        }
        //Ficar atento nesse reset aqui em cima, pode ser que de problema depois
        //--------------------------------------------------------------------------------------------------------------

        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.size(); j++) {
                if (i != j) {
                    //if((Pop.get(i).getF1()<Pop.get(j).getF1())&&(Pop.get(i).getF2()<Pop.get(j).getF2())){
                    if (((population.get(i).getAggregatedObjective1() < population.get(j).getAggregatedObjective1()) && (population.get(i).getAggregatedObjective2() < population.get(j).getAggregatedObjective2())
                            || (population.get(i).getAggregatedObjective1() < population.get(j).getAggregatedObjective1()) && (population.get(i).getAggregatedObjective2() == population.get(j).getAggregatedObjective2())
                            || (population.get(i).getAggregatedObjective1() == population.get(j).getAggregatedObjective1()) && (population.get(i).getAggregatedObjective2() < population.get(j).getAggregatedObjective2()))) {
                        population.get(i).addnDom();
                        population.get(j).addeDom();
                        population.get(i).addL(j);//adiciona a posição da solucao que é dominada - usado no NSGA-II
                    }
                }
            }
        }

        for (int i = 0; i < population.size(); i++) {//Determina S, número de soluções que são dominadas pela solução i
            population.get(i).setS(population.get(i).getNumberOfDominatedSolutionsByThisSolution());
        }

        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.size(); j++) {
                if (((population.get(j).getAggregatedObjective1() < population.get(i).getAggregatedObjective1()) && (population.get(j).getAggregatedObjective2() < population.get(i).getAggregatedObjective2())
                        || (population.get(j).getAggregatedObjective1() < population.get(i).getAggregatedObjective1()) && (population.get(j).getAggregatedObjective2() == population.get(i).getAggregatedObjective2())
                        || (population.get(j).getAggregatedObjective1() == population.get(i).getAggregatedObjective1()) && (population.get(j).getAggregatedObjective2() < population.get(i).getAggregatedObjective2()))) {
                    population.get(i).setR(population.get(i).getR() + population.get(j).getNumberOfDominatedSolutionsByThisSolution());
                }
            }
        }

        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getNumberOfSolutionsWichDomineThisSolution() == 0) {
                nonDominated.add(population.get(i));
            }
        }
        removeEqualSolutions(nonDominated);
    }

    public static void normalizeObjectiveFunctionsValues2(List<ProblemSolution> Pop) {
        double max = -999999999;
        double min = 999999999;

        //para F1
        //obtendo o valor maximo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective1() > max) {
                max = s.getAggregatedObjective1();
            }
        }
        //obtendo o valor minimo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective1() < min) {
                min = s.getAggregatedObjective1();
            }
        }
        //System.out.println("F1: max = " + max +"\tmin = " + min);
        for (ProblemSolution s : Pop) {
            s.setAggregatedObjective1Normalized((s.getAggregatedObjective1() - min) / (max - min));
        }

        max = -999999999;
        min = 999999999;

        //para F2
        //obtendo o valor maximo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective2() > max) {
                max = s.getAggregatedObjective2();
            }
        }
        //obtendo o valor minimo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective2() < min) {
                min = s.getAggregatedObjective2();
            }
        }
        //System.out.println("F2: max = " + max +"\tmin = " + min);
        for (ProblemSolution s : Pop) {
            s.setAggregatedObjective2Normalized((s.getAggregatedObjective2() - min) / (max - min));
        }
    }

    public static void evaluateDistanceBetweenSolutions(List<ProblemSolution> Pop, double dist[][]) {
        //calcula a distancia entre os elementos - usa somente metada da matriz
        List<Double> linha = new ArrayList<>();
        for (int i = 0; i < Pop.size(); i++) {

            for (int j = i + 1; j < Pop.size(); j++) {
                dist[i][j] = Math.sqrt(Math.pow(Pop.get(i).getAggregatedObjective1Normalized() - Pop.get(j).getAggregatedObjective1Normalized(), 2) + Math.pow(Pop.get(i).getAggregatedObjective2Normalized() - Pop.get(j).getAggregatedObjective2Normalized(), 2));
            }
        }
        //coloca zero na diagonal principal
        for (int i = 0; i < Pop.size(); i++) {
            dist[i][i] = 0;
        }
        //Replico na metade inferior da matriz de distancia
        for (int i = 0; i < Pop.size(); i++) {
            for (int j = i + 1; j < Pop.size(); j++) {
                dist[j][i] = dist[i][j];
            }
        }
        //imprime a matriz de distancias
//        System.out.println("Matriz d = ");
//        for(int i=0; i<Pop.size();i++){
//            linha.clear();
//            for(int j=0;j<Pop.size();j++){
//                linha.add(dist[i][j]);
//            }
//            System.out.println(linha);
//        };
    }

    public static void fitnessValueSharing(List<ProblemSolution> Pop, double dist[][], double sigmaSH, double sigma, double alfa) {
        for (int i = 0; i < Pop.size(); i++) {
            double soma = 0;
            List<Double> s = new ArrayList<>();
            List<Double> sh = new ArrayList<>();
            for (int j = 0; j < Pop.size(); j++) {
                if (dist[i][j] <= sigmaSH) {
                    //System.out.println("Entrou");
                    s.add(1 - Math.pow((dist[i][j] / sigma), alfa));
                } else {
                    s.add(0.0);
                }
            }

            for (int j = 0; j < Pop.size(); j++) {
                soma = soma + s.get(j);//somatorio de s(d(i,j))
            }
            sh.add(soma);
            //System.out.println("sh = "+ s);
            //System.out.println("Fitness antes = " + Pop.get(i).getFitness());
            Pop.get(i).setFitness(Pop.get(i).getFitness() / soma); //fi(i) = fi(i)/sigma
            //System.out.println("Fitness depois = " + Pop.get(i).getFitness());
        }
    }

    public static void initializePopulation(List<ProblemSolution> Pop, int TamPop, List<Request> listRequests, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
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

    public static void normalizeAggregatedObjectiveFunctions(List<ProblemSolution> population) {
        if (population.size() != 0) {
            double maxObjective1 = population.stream()
                    .mapToDouble(ProblemSolution::getAggregatedObjective1)
                    .max().getAsDouble();
            double minObjective1 = population.stream()
                    .mapToDouble(ProblemSolution::getAggregatedObjective1)
                    .min().getAsDouble();

            double maxObjective2 = population.stream()
                    .mapToDouble(ProblemSolution::getAggregatedObjective2)
                    .max().getAsDouble();
            double minObjective2 = population.stream()
                    .mapToDouble(ProblemSolution::getAggregatedObjective2)
                    .min().getAsDouble();
            population.forEach(individual -> {
                individual.setAggregatedObjective1Normalized((individual.getAggregatedObjective1() - minObjective1)
                        / (maxObjective1 - minObjective1));
                individual.setAggregatedObjective2Normalized((individual.getAggregatedObjective2() - minObjective2)
                        / (maxObjective2 - minObjective2));
            });
        }
    }

    public static void normalizeObjectiveFunctionsValues(List<ProblemSolution> Pop) {
        double max = -999999999;
        double min = 999999999;

        //para F1
        //obtendo o valor maximo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective1() > max) {
                max = s.getAggregatedObjective1();
            }
        }
        //obtendo o valor minimo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective1() < min) {
                min = s.getAggregatedObjective1();
            }
        }
        //System.out.println("F1: max = " + max +"\tmin = " + min);
        for (int i = 0; i < Pop.size(); i++) {
            //Pop.get(i).setF1n((Pop.get(i).getF1() - min)/(max - min));
            Pop.get(i).setAggregatedObjective1Normalized((Pop.get(i).getAggregatedObjective1()) / (3500));
        }

        max = -999999999;
        min = 999999999;

        //para F2
        //obtendo o valor maximo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective2() > max) {
                max = s.getAggregatedObjective2();
            }
        }
        //obtendo o valor minimo
        for (ProblemSolution s : Pop) {
            if (s.getAggregatedObjective2() < min) {
                min = s.getAggregatedObjective2();
            }
        }
        //System.out.println("F2: max = " + max +"\tmin = " + min);
        for (int i = 0; i < Pop.size(); i++) {
            Pop.get(i).setAggregatedObjective2Normalized((Pop.get(i).getAggregatedObjective2()) / (20000));
        }
    }

    public static void normalizeObjectiveFunctionsValues3(List<ProblemSolution> Pop) {
        ProblemSolution maior = new ProblemSolution();
        ProblemSolution menor = new ProblemSolution();

        maior.setSolution(Collections.max(Pop));
        menor.setSolution(Collections.min(Pop));

        for (ProblemSolution s : Pop) {
            s.setFitness((maior.getFitness() - s.getFitness()) / (maior.getFitness() - menor.getFitness()));
        }
    }

    public static void updateSolutionsFile(List<ProblemSolution> Pop, List<ProblemSolution> arquivo, int TamMax) {

        List<ProblemSolution> naoDominados = new ArrayList<>();
        List<ProblemSolution> Q = new ArrayList<>();
        Random rnd = new Random();
        double melhorFitness;
        if (Pop.size() > 0) {
            melhorFitness = Pop.get(0).getFitness();
        } else {
            //System.out.println("Populaçao teve tamanho zero!!!");
            melhorFitness = rnd.nextFloat();
        }

        arquivo.addAll(Pop);

        //ImprimePopulacao(Pop);
        dominanceAlgorithm(arquivo, naoDominados);
        arquivo.clear();
        arquivo.addAll(naoDominados);
        normalizeObjectiveFunctionsValues(arquivo);
        for (ProblemSolution s : arquivo) {
            s.setFitness(melhorFitness);
        }
        //retiraIguais(arquivo);

        //System.out.println("Tamanho arquivo = " + arquivo.size());
//        Q.addAll(Pop);
//        Q.addAll(arquivo);
//        dominanceAlgorithm(arquivo,naoDominados);
//        System.out.println("Q"+Q.size());
//        //arquivo.clear();
//        arquivo.addAll(naoDominados);
//        dominanceAlgorithm(Pop,naoDominados);
//        arquivo.addAll(naoDominados);
//        naoDominados.clear();
//        dominanceAlgorithm(arquivo,naoDominados);
//        arquivo.clear();
//        arquivo.addAll(naoDominados);
        double sigmaSH = Math.sqrt(2) / 60;
        List<Integer> c = new ArrayList<>();
        if (arquivo.size() > TamMax) {//reduzir arquivo
            //double dist[][] = new double[arquivo.size()][arquivo.size()];
            while (arquivo.size() > TamMax) {
                c.clear();
                //Distancia(arquivo,dist);
                // System.out.println("Ultrapassou o tamanho maximo do arquivo!!! Tem que reduzir");
                //  System.out.println("Redução");
                normalizeObjectiveFunctionsValues(arquivo);
                for (int i = 0; i < arquivo.size(); i++) {
                    int soma = 0;
                    double dist[][] = new double[arquivo.size()][arquivo.size()];
                    for (int j = 0; j < arquivo.size(); j++) {

                        dist[i][j] = Math.sqrt(Math.pow(arquivo.get(i).getAggregatedObjective1Normalized() - arquivo.get(j).getAggregatedObjective1Normalized(), 2) + Math.pow(arquivo.get(i).getAggregatedObjective2Normalized() - arquivo.get(j).getAggregatedObjective2Normalized(), 2));
                        if (dist[i][j] < sigmaSH) {
                            soma++;
                        }
                    }
                    c.add(soma);
                }
                int max = Collections.max(c);

                for (int i = 0; i < arquivo.size(); i++) {

                    if (c.get(i) == max) {
                        arquivo.remove(i);
                        c.remove(i);
                        break;
                    }
                }
                //System.out.println(c);
                //System.out.println("Tamanho do arquivo = " + arquivo.size());
            }
        }
        //
    }

    public static void updateNSGAIISolutionsFile(List<ProblemSolution> Pop, List<ProblemSolution> arquivo, int TamMax) {
        //retiraIguais(Pop);        

        List<ProblemSolution> naoDominados = new ArrayList<>();
        List<ProblemSolution> Q = new ArrayList<>();
        Random rnd = new Random();
        double melhorFitness;
        if (Pop.size() > 0) {
            melhorFitness = Pop.get(0).getFitness();
        } else {
            //System.out.println("Populaçao teve tamanho zero!!!");
            melhorFitness = rnd.nextFloat();
        }

        arquivo.addAll(Pop);
        //System.out.println("Teste tamanho do arquivo = " + arquivo.size());
        //System.out.println(Pop.size());
        //Pop.clear();
        //ImprimePopulacao(Pop);
        dominanceAlgorithm(arquivo, naoDominados);
        arquivo.clear();
        arquivo.addAll(naoDominados);
        removeEqualSolutions(arquivo);
        normalizeObjectiveFunctionsValues(arquivo);
        for (ProblemSolution s : arquivo) {
            s.setFitness(melhorFitness);
        }
        //---------------------------------------------------------------------
        //System.out.println("Teste tamanho do arquivo = " + arquivo.size());

        if (arquivo.size() > TamMax) {//reduzir arquivo

            int k = (int) Math.ceil(Math.sqrt(TamMax));
            //System.out.println(k);
            while (arquivo.size() > TamMax) {
                //System.out.println("Tamanho arquivo = " + arquivo.size());
                double dist[][] = new double[arquivo.size()][arquivo.size()];
                normalizeObjectiveFunctionsValues(arquivo);
                evaluateDistanceBetweenSolutions(arquivo, dist);
                List<Double> ci = new ArrayList<>();
                List<Double> linha = new ArrayList<>();
                for (int i = 0; i < arquivo.size(); i++) {
                    for (int l = 0; l < arquivo.size(); l++) {
                        if (dist[i][l] != 0) {
                            linha.add(dist[i][l]);
                        }
                    }
                    Collections.sort(linha);
                    //System.out.println(linha);
                    double soma = 0;
                    for (int j = 0; j < k; j++) {
                        soma += linha.get(k);
                    }
                    ci.add(soma);
                }
                //System.out.println(ci);
                double max = Collections.max(ci);
                int pos = 0;
                for (int i = 0; i < arquivo.size(); i++) {
                    if (max == ci.get(i)) {
                        pos = i;
                    }
                }
                arquivo.remove(pos);
            }
        }
        fileSorting(arquivo);
    }

    public static void updateNSGAIISolutionsFile2(List<ProblemSolution> Pop, List<ProblemSolution> arquivo, int TamMax) {
        //retiraIguais(Pop);        

        List<ProblemSolution> naoDominados = new ArrayList<>();
        List<ProblemSolution> Q = new ArrayList<>();
        Random rnd = new Random();
        double melhorFitness;
        if (Pop.size() > 0) {
            melhorFitness = Pop.get(0).getFitness();
        } else {
            //System.out.println("Populaçao teve tamanho zero!!!");
            melhorFitness = rnd.nextFloat();
        }

        //arquivo.addAll(Pop);
        //System.out.println("Teste tamanho do arquivo = " + arquivo.size());
        //System.out.println(Pop.size());
        //Pop.clear();
        //ImprimePopulacao(Pop);
        dominanceAlgorithm(Pop, naoDominados);
        //arquivo.clear();
        arquivo.addAll(naoDominados);
        removeEqualSolutions(arquivo);
        naoDominados.clear();
        dominanceAlgorithm(arquivo, naoDominados);
        arquivo.clear();
        arquivo.addAll(naoDominados);

        normalizeObjectiveFunctionsValues(arquivo);

        for (ProblemSolution s : arquivo) {
            s.setFitness(melhorFitness);
        }
        //---------------------------------------------------------------------
        //System.out.println("Teste tamanho do arquivo = " + arquivo.size());

        if (arquivo.size() > TamMax) {//reduzir arquivo

            int k = (int) Math.ceil(Math.sqrt(TamMax));
            //System.out.println(k);
            while (arquivo.size() > TamMax) {
                //System.out.println("Tamanho arquivo = " + arquivo.size());
                double dist[][] = new double[arquivo.size()][arquivo.size()];
                normalizeObjectiveFunctionsValues(arquivo);
                evaluateDistanceBetweenSolutions(arquivo, dist);
                List<Double> ci = new ArrayList<>();
                List<Double> linha = new ArrayList<>();
                for (int i = 0; i < arquivo.size(); i++) {
                    for (int l = 0; l < arquivo.size(); l++) {
                        if (dist[i][l] != 0) {
                            linha.add(dist[i][l]);
                        }
                    }
                    Collections.sort(linha);
                    //System.out.println(linha);
                    double soma = 0;
                    for (int j = 0; j < k; j++) {
                        soma += linha.get(k);
                    }
                    ci.add(soma);
                }
                //System.out.println(ci);
                double max = Collections.max(ci);
                int pos = 0;
                for (int i = 0; i < arquivo.size(); i++) {
                    if (max == ci.get(i)) {
                        pos = i;
                    }
                }
                arquivo.remove(pos);
            }
        }
        fileSorting(arquivo);
    }

    public static void updateTestFile(List<ProblemSolution> Pop, List<ProblemSolution> arquivo, int TamMax) {
        List<ProblemSolution> naoDominados = new ArrayList<>();
        dominanceAlgorithm(Pop, naoDominados);
        arquivo.addAll(naoDominados);
        naoDominados.clear();
        dominanceAlgorithm(arquivo, naoDominados);
        arquivo.clear();
        arquivo.addAll(naoDominados);
        if (arquivo.size() > TamMax) {
            System.out.println("Reduzir");
        }
    }

    public static void updateSPEA2SolutionsFile(List<ProblemSolution> Pop, List<ProblemSolution> arquivo, int TamMax) {
        List<ProblemSolution> naoDominados = new ArrayList<>();
        arquivo.addAll(Pop);
        removeEqualSolutions(arquivo);
        dominanceAlgorithm(arquivo, naoDominados);
        arquivo.clear();
        arquivo.addAll(naoDominados);

        if (arquivo.size() > TamMax) {
            System.out.println("Reduzir");
        }
        //OrdenaArquivo(Pop);
        int i = 0;
        if (arquivo.size() < TamMax) {
            List<ProblemSolution> NewPop = new ArrayList<>();
            NewPop.addAll(Pop);
            NewPop.addAll(arquivo);
            do {
                arquivo.add(NewPop.get(i));
                i++;
                //System.out.println("Tamanho arquivo = " + arquivo.size());
            } while (arquivo.size() < TamMax);
        }
        //ImprimePopulacao(arquivo);
        //System.out.println("Tamanho arquivo = " + arquivo.size());
        fileSorting(arquivo);
    }

    public static void updateNSGASolutionsFile(List<ProblemSolution> Pop, List<ProblemSolution> arquivo, int TamMax) {

        List<ProblemSolution> naoDominados = new ArrayList<>();
        arquivo.addAll(Pop);
        removeEqualSolutions(arquivo);
        dominanceAlgorithm(arquivo, naoDominados);
        arquivo.clear();
        arquivo.addAll(naoDominados);

        if (arquivo.size() > TamMax) {//reduzir arquivo

            int k = (int) Math.ceil(Math.sqrt(TamMax));
            //System.out.println(k);
            while (arquivo.size() > TamMax) {
                //System.out.println("Tamanho arquivo = " + arquivo.size());
                double dist[][] = new double[arquivo.size()][arquivo.size()];
                normalizeObjectiveFunctionsValues(arquivo);
                evaluateDistanceBetweenSolutions(arquivo, dist);
                List<Double> ci = new ArrayList<>();
                List<Double> linha = new ArrayList<>();
                for (int i = 0; i < arquivo.size(); i++) {
                    for (int l = 0; l < arquivo.size(); l++) {
                        if (dist[i][l] != 0) {
                            linha.add(dist[i][l]);
                        }
                    }
                    Collections.sort(linha);
                    //System.out.println(linha);
                    double soma = 0;
                    for (int j = 0; j < k; j++) {
                        soma += linha.get(k);
                    }
                    ci.add(soma);
                }
                //System.out.println(ci);
                double max = Collections.max(ci);
                int pos = 0;
                for (int i = 0; i < arquivo.size(); i++) {
                    if (max == ci.get(i)) {
                        pos = i;
                    }
                }
                arquivo.remove(pos);
            }
        }
        fileSorting(arquivo);
    }

    public static void updateNSGASolutionsFileTest(List<ProblemSolution> population, List<ProblemSolution> file, int TamMax) {

        List<ProblemSolution> naoDominados = new ArrayList<>();
        file.addAll(population);
        removeEqualSolutions(file);
        dominanceAlgorithm(file, naoDominados);
        file.clear();
        file.addAll(naoDominados);

        if (file.size() > TamMax) {//reduzir arquivo

            int k = (int) Math.ceil(Math.sqrt(TamMax));
            //System.out.println(k);
            while (file.size() > TamMax) {
                //System.out.println("Tamanho arquivo = " + arquivo.size());
                double dist[][] = new double[file.size()][file.size()];
                normalizeObjectiveFunctionsValues(file);
                evaluateDistanceBetweenSolutions(file, dist);
                List<Double> ci = new ArrayList<>();
                List<Double> linha = new ArrayList<>();
                for (int i = 0; i < file.size(); i++) {
                    for (int l = 0; l < file.size(); l++) {
                        if (dist[i][l] != 0) {
                            linha.add(dist[i][l]);
                        }
                    }
                    Collections.sort(linha);
                    //System.out.println(linha);
                    double soma = 0;
                    for (int j = 0; j < k; j++) {
                        soma += linha.get(k);
                    }
                    ci.add(soma);
                }
                //System.out.println(ci);
                double max = Collections.max(ci);
                int pos = 0;
                for (int i = 0; i < file.size(); i++) {
                    if (max == ci.get(i)) {
                        pos = i;
                    }
                }
                file.remove(pos);
            }
        } else if (file.size() < TamMax) {
            population.sort(Comparator.comparing(ProblemSolution::getFitness));
            int solutionsNumber = TamMax - file.size();
            file.addAll(population.subList(0, solutionsNumber));
        }
        fileSorting(file);
    }

    public static void fileSorting(List<ProblemSolution> arquivo) {
        int tamanhoArquivo = arquivo.size();

        for (int i = 0; i < tamanhoArquivo; i++) {
            for (int j = 0; j < tamanhoArquivo; j++) {
                if (arquivo.get(i).getAggregatedObjective1() < arquivo.get(j).getAggregatedObjective1()) {
                    ProblemSolution s = new ProblemSolution();
                    s.setSolution(arquivo.get(i));
                    arquivo.get(i).setSolution(arquivo.get(j));
                    arquivo.get(j).setSolution(s);
                }
            }
        }
    }

    public static void removeEqualSolutions(List<ProblemSolution> arquivo) {
        int tamanhoArquivo = arquivo.size();
        for (int i = 0; i < tamanhoArquivo; i++) {
            for (int j = i + 1; j < tamanhoArquivo; j++) {
                if ((arquivo.get(i).getAggregatedObjective1() == arquivo.get(j).getAggregatedObjective1()) && (arquivo.get(i).getAggregatedObjective2() == arquivo.get(j).getAggregatedObjective2())) {
                    arquivo.remove(j);
                    tamanhoArquivo = arquivo.size();
                    //j=0;
                    j = i + 1;
                }
            }
        }
    }
//    public static void FNDS(List<Solucao> Pop, List<List<Solucao>> fronts){
//        List<Solucao> PopAux = new ArrayList<>();//lista para remover soluções
//        PopAux.addAll(Pop);
//        List<Solucao> front = new ArrayList<>();//lista com apenas uma fronteira
//        //List<Solucao> front = new ArrayList<>();
//        int contador = 0;
//        while(PopAux.size() > 0){//enquanto todos os indivíduos não forem classificados
//            fronts.add(new ArrayList<>());
//            
//            for(int i=0; i< PopAux.size(); i++){
//                if(PopAux.get(i).geteDom() == 0){
//                    front.add(Pop.get(i));
//                }
//            }
//            
//            System.out.println("primeira fronteira = " + front);
//            List<Integer> dominadas = new ArrayList<>();
//            
//            for(ProblemSolution s: front){
//                
//                dominadas.addAll(s.getL());
//                
//                
//                //System.out.println("Solucoes Dominadas = " + dominadas);
//            }
//            
//            for(int posicao: dominadas){
//                    Pop.get(posicao).redeDom();
//            }
//            //Dominancia(Pop,naoDominados);
//            dominadas.clear();
//            PopAux.removeAll(front);
//            //System.out.println(PopAux.size());
//            fronts.add(new ArrayList<>());
//            fronts.get(contador).addAll(front);
//            front.clear();
//            contador++;
//        }
//    }

    public static void FNDS(List<ProblemSolution> Pop, List<List<ProblemSolution>> fronts) {
        List<ProblemSolution> PopAux = new ArrayList<>();//lista para remover soluções
        PopAux.addAll(Pop);
        fronts.clear();
        List<ProblemSolution> front = new ArrayList<>();//lista com apenas uma fronteira
        int contador = 0;
        //ImprimePopulacao(Pop);
        while (PopAux.size() > 0) {
            for (int i = 0; i < PopAux.size(); i++) {
                if (PopAux.get(i).getNumberOfSolutionsWichDomineThisSolution() == 0) {
                    front.add(PopAux.get(i));
                }
            }

            for (ProblemSolution s : front) {
                for (int posicao : s.getListOfSolutionsDominatedByThisSolution()) {
                    Pop.get(posicao).redeDom();
                }
            }

            PopAux.removeAll(front);
            fronts.add(new ArrayList<>());
            fronts.get(contador).addAll(front);
            //System.out.println(front);
            //System.out.println(fronts.size());
            //ImprimePopulacao(front);
            for (ProblemSolution s : PopAux) {//forçando a barra - olhar o motivo de dar numero negativo no eDom
                if (s.getNumberOfSolutionsWichDomineThisSolution() < 0) {
                    s.setNumberOfSolutionsWichDomineThisSolution(0);
                }
            }
            //System.out.println("FNDS");
            front.clear();
            contador++;
            //System.out.println("popaux = " + PopAux);
        }
        //System.out.println("Saiu do laço");
        //----- teste ------

        for (int i = 0; i < fronts.size(); i++) {
            //System.out.println(i);
            front.addAll(fronts.get(i));
            for (ProblemSolution s : front) {
                s.setNumberOfSolutionsWichDomineThisSolution(i);
            }
            //System.out.println(front.size());
            front.clear();
        }
        //System.out.println("\n");
        Pop.clear();
        for (List<ProblemSolution> f : fronts) {
            Pop.addAll(f);
            //System.out.println(f);
        }
    }

    public static void FNDS2(List<ProblemSolution> Pop, List<List<ProblemSolution>> fronts) {
        List<ProblemSolution> PopAux = new ArrayList<>();//lista para remover soluções
        PopAux.addAll(Pop);
        List<ProblemSolution> front = new ArrayList<>();//lista com apenas uma fronteira

        //fronts.add(new ArrayList<>());
        int contador = 0;
//        for(int i=0; i<Pop.size();i++){
//            for(int j=0; j<PopAux.size();j++){
//                for(ProblemSolution s: PopAux){
//                    if(s.geteDom()==i){
//                        front.add(s);
//                    }
//                }
//            System.out.println(front);
//            fronts.get(contador).addAll(front);
//            PopAux.removeAll(front);
//            front.clear();
//            contador++;
//            }           
//        }
        while (PopAux.size() > 0) {
            for (int j = 0; j < PopAux.size(); j++) {
                if (PopAux.get(j).getNumberOfSolutionsWichDomineThisSolution() == 0) {
                    front.add(PopAux.get(j));
                }
            }
            PopAux.removeAll(front);
            for (int j = 0; j < PopAux.size(); j++) {
                PopAux.get(j).redeDom();
            }
            //System.out.println("Fronteira 1 = " + front);
            fronts.add(new ArrayList<>());

            fronts.get(contador).addAll(front);

            front.clear();
            contador++;
            //System.out.println("Tamanho" + PopAux.size());
        }
        //fronts.get(fronts.size()-1).clear();
        //System.out.println("Fronteiras = " + fronts);
        //System.out.println("Fronteiras Não Dominadas");
        for (int i = 0; i < fronts.size(); i++) {
            //System.out.println(fronts.get(i));
        }
        //System.out.println("Tamanho = " + fronts.size());
    }

    public static void nonDominatedFrontiersSortingAlgorithm(List<ProblemSolution> population, List<List<ProblemSolution>> frontiers) {
        List<ProblemSolution> auxiliarPopulation = new ArrayList<>();//lista para remover soluções
        auxiliarPopulation.addAll(population);
        frontiers.clear();
        List<ProblemSolution> front = new ArrayList<>();
        int frontierCounter = 0;

        while (auxiliarPopulation.size() > 0) {
            frontiers.add(new ArrayList<>());
            dominanceAlgorithm(auxiliarPopulation, front);
            frontiers.get(frontierCounter).addAll(front);
            auxiliarPopulation.removeAll(front);
            front.clear();
            frontierCounter++;
        }

        for (int i = 0; i < frontiers.size(); i++) {

            front.addAll(frontiers.get(i));
            for (ProblemSolution solution : front) {
                solution.setNumberOfSolutionsWichDomineThisSolution(i);
            }
            front.clear();
        }

//        population.clear();
//        for (List<Solution> frontInFrontiers : frontiers) {
//            population.addAll(frontInFrontiers);
//        }
    }

    public static void ReduzPopulacao(List<ProblemSolution> Pop_linha, List<List<ProblemSolution>> fronts, int TamPop) {
        for (int i = 0; i < fronts.size(); i++) {
            Pop_linha.addAll(fronts.get(i));
            printPopulation(Pop_linha);
            System.out.println("\n");

            if (Pop_linha.size() > TamPop) {//confere se ao adicinar a fronteira i, ultrapassa o tamanho original da populacao
                System.out.println("Reduzir!!!!!!!!!!");
            }
        }
    }

    public static ProblemSolution MOVND(List<Double> parameters, ProblemSolution s_0, List<Request> listRequests, List<Request> P, Set<Integer> K, List<Request> U,
            Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<List<Long>> d, List<List<Long>> c,
            Integer n, Integer Qmax, Long TimeWindows) {

        Random rnd = new Random();
        ProblemSolution melhor = new ProblemSolution(s_0);
        ProblemSolution s_linha = new ProblemSolution();
        ProblemSolution s = new ProblemSolution();
        int cont1 = 0;
        int k, r;
        r = 4;
        //r = 6;
        k = 1;

        while (k <= r) {
            System.out.println("k = " + k);
            s.setSolution(firstImprovementInMultiObjectiveOptimization(parameters, s_0, k, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
            //System.out.println(s);
            //s.setSolution(bestImprovementInMultiObjectiveOptimization(s_0,k,listRequests,P,K,U,Pin,Pout, d, c, n, Qmax,TimeWindows));
            if (((s.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (s.getAggregatedObjective2() < melhor.getAggregatedObjective2())) || ((s.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (s.getAggregatedObjective2() == melhor.getAggregatedObjective2()))
                    || ((s.getAggregatedObjective1() == melhor.getAggregatedObjective1()) && (s.getAggregatedObjective2() < melhor.getAggregatedObjective2()))) {
                melhor.setSolution(s);
                k = 1;
            } else {
                k = k + 1;
            }
            cont1++;
        }
        return melhor;
    }

    public static void buscaLocal(List<Double> parameters, List<ProblemSolution> arquivo, List<Request> listRequests, List<Request> P, Set<Integer> K,
            List<Request> U, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout, List<List<Long>> d,
            List<List<Long>> c, Integer n, Integer Qmax, Long TimeWindows) {
        Random rnd = new Random();
        int numeroSolucoes = arquivo.size() / 5;
        //numeroSolucoes = 10;
        //System.out.println("Numero solucoes para busca = " + numeroSolucoes);

        List<ProblemSolution> naoDominados = new ArrayList<>();
        dominanceAlgorithm(arquivo, naoDominados);
        numeroSolucoes = naoDominados.size() / 5;
        //ImprimePopulacao(naoDominados);
        for (int i = 0; i < numeroSolucoes; i++) {
//            int posicao = rnd.nextInt(arquivo.size());
//            System.out.println(posicao);
//            ProblemSolution s = new ProblemSolution(MOVND(arquivo.get(posicao), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
//            arquivo.get(posicao).setSolution(s);
            int posicao = rnd.nextInt(naoDominados.size());
            System.out.println(posicao);
            ProblemSolution s = new ProblemSolution(MOVND(parameters, naoDominados.get(posicao), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
            arquivo.add(s);
        }

    }

    public static ProblemSolution MOILS(List<Double> parameters, ProblemSolution s_0, List<Request> listRequests, Map<Integer, List<Request>> Pin, Map<Integer, List<Request>> Pout,
            Integer n, Integer Qmax, Set<Integer> K, List<Request> U, List<Request> P, List<Integer> m, List<List<Long>> d,
            List<List<Long>> c, Long TimeWindows) {
        //Solução inicial já é gerada pelo GA
        ProblemSolution s = new ProblemSolution(s_0);
        ProblemSolution s_linha = new ProblemSolution();
        ProblemSolution s_2linha = new ProblemSolution();
        List<ProblemSolution> historico = new ArrayList<>();
        int MAXITER = 20;

        //BuscaLocal
        s.setSolution(MOVND(parameters, s_0, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
        //s.setSolution(firstImprovementAlgorithm(s_0,2,listRequests,P,K,U,Pin,Pout, d, c, n, Qmax,TimeWindows));
        int cont = 0;
        while (cont < MAXITER) {
            //System.out.println("Entrou no laço do IteratedLocalSearch\tFO = " + s.getfObjetivo());

            System.out.println("Interação MOILS = " + cont);

            //Perturbação
            s_linha.setSolution(perturbation(parameters, s, listRequests, Pin, Pout, n, Qmax, K, U, P, m, d, c, TimeWindows));
            //System.out.println("Apos perturbação s'= " + s_linha);

            //BuscaLocal
            s_2linha.setSolution(MOVND(parameters, s_linha, listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));
            //s_2linha.setSolution(firstImprovementAlgorithm(s_0,2,listRequests,P,K,U,Pin,Pout, d, c, n, Qmax,TimeWindows));
            //System.out.println("Apos busca local s'' = " + s_2linha);
            //CriterioAceitacao
            if ((s_2linha.getAggregatedObjective1() < s_0.getAggregatedObjective1()) && (s_2linha.getAggregatedObjective2() < s_0.getAggregatedObjective2())
                    || ((s_2linha.getAggregatedObjective1() < s_0.getAggregatedObjective1()) && (s_2linha.getAggregatedObjective2() == s_0.getAggregatedObjective2()))
                    || ((s_2linha.getAggregatedObjective1() == s_0.getAggregatedObjective1()) && (s_2linha.getAggregatedObjective2() < s_0.getAggregatedObjective2()))) {
                //System.out.println("s_0 = " + s_0.getfObjetivo());

                //System.out.println("s_0 = " + s_0.getfObjetivo());//System.out.println("s_2linha = "+s_2linha.getfObjetivo());
                s.setSolution(s_2linha);
                s_0.setSolution(s_2linha);
                //historico.add(s_2linha);
                System.out.println("s_0 = " + s_0);
                //System.out.println("Atualizou\tFO = " + s.getfObjetivo1() );

                //System.out.println("Tamanho Historico =  " + historico.size() );
                //return s_0;
            }

            cont++;
        }
        //Collections.sort(historico);
        //System.out.println("Historico = ");
        System.out.println("Soluçao retornada do ILS = " + s_0);
        //ImprimePopulacao(historico);
        //s.setSolution(copyBestSolution(historico,s));
        //s.setSolution(historico.get(1));
        return s_0;
    }

    public static ProblemSolution firstImprovementInMultiObjectiveOptimization(List<Double> parameters, ProblemSolution s, int tipoMovimento, List<Request> listRequests, List<Request> P, Set<Integer> K,
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                    aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                    if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

    public static ProblemSolution bestImprovementInMultiObjectiveOptimization(List<Double> parameters, ProblemSolution s, int tipoMovimento, List<Request> listRequests, List<Request> P, Set<Integer> K,
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                    aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                    if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

                            aux.setSolution(rebuildSolution(parameters, new ArrayList<Integer>(vizinho), listRequests, P, K, U, Pin, Pout, d, c, n, Qmax, TimeWindows));

                            if ((aux.getAggregatedObjective1() < melhor.getAggregatedObjective1()) && (aux.getAggregatedObjective2() < melhor.getAggregatedObjective2())) {
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

}
