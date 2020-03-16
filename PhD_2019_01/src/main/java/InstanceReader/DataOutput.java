/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.ProblemSolution;
import ProblemRepresentation.RankedList;
import VRPDRT.VRPDRT;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author renansantos
 */
public class DataOutput {

    private String algorithmName;
    private String resultsPath;
    private String fileName;
    private PrintStream streamForTxt;
    private PrintStream streamForSolutions;
    private PrintStream streamForCombinedPareto;
    private PrintStream streamForCombinedParetoObjectivesInCsv;
    private PrintStream streamForCombinedParetoObjectivesInTxt;
    private PrintStream streamForCombinedParetoObjectivesInExecution;
    private PrintStream streamForCombinedParetoReducedObjectives;
    private PrintStream streamForConvergence;
    private PrintStream streamForCsv;

    private VRPDRT subProblem;
    private RankedList rankedList;
    private String instancePath;
    private Instance instance = new Instance();

    public DataOutput(String algorithmName, String instanceName, int execution) {
        this.algorithmName = algorithmName;
        this.resultsPath = "AlgorithmsResults//" + algorithmName + "//" + instanceName + "//";
        this.fileName = this.algorithmName + "_execution_" + execution;
        initalizePathAndFiles();
        initalizeStreams();
    }

    public DataOutput(String algorithmName, String instanceName) {
        this.algorithmName = algorithmName;
        this.resultsPath = "AlgorithmsResults//" + algorithmName + "//" + instanceName + "//";
        this.fileName = this.algorithmName;
        initalizePathAndFiles();
        initalizeStreams();
    }

    public DataOutput(String algorithmName, String instanceName, String instancePath) {
        this.algorithmName = algorithmName;
        this.resultsPath = "AlgorithmsResults//" + algorithmName + "//" + instanceName + "//";
        this.fileName = this.algorithmName;
        this.instancePath = instancePath;
        initalizePathAndFiles();
        initalizeStreams();
    }
    
    private void initalizePathAndFiles() {
        boolean success = (new File(this.resultsPath)).mkdirs();
    }

    private void initalizeStreams() {
        try {
            streamForTxt = new PrintStream(resultsPath + "/" + fileName + ".txt");
            streamForSolutions = new PrintStream(resultsPath + "/" + fileName + "_Solutions.txt");
            streamForCombinedPareto = new PrintStream(resultsPath + "/" + fileName + "_CoombinedPareto.txt");
            streamForCombinedParetoObjectivesInCsv = new PrintStream(resultsPath + "/" + fileName + "_CombinedPareto_Objectives.csv");
            streamForCombinedParetoObjectivesInTxt = new PrintStream(resultsPath + "/" + fileName + "_CombinedPareto_Objectives.txt");
            streamForCombinedParetoReducedObjectives = new PrintStream(resultsPath + "/" + fileName + "_CombinedPareto_ReducedObjectives.txt");
            streamForConvergence = new PrintStream(resultsPath + "/" + fileName + "_Convergence.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public void saveBestSolutionInTxtFile(ProblemSolution solution, int currentIteration) {
        this.streamForTxt.print(currentIteration + "\t" + solution + "\n");
        this.streamForSolutions.print(solution + "\n");
    }

    public void saveBestSolutionFoundInTxtFile(ProblemSolution solution, int currentIteration) {
        this.streamForTxt.print(currentIteration + "\t" + solution + "\n");
        this.streamForSolutions.print(solution + "\n");
    }

    public void saveBestSolutionFoundInTxtFile(ProblemSolution solution) {
        this.streamForCombinedPareto.print(solution + "\n");
        this.streamForCombinedParetoObjectivesInCsv.print(solution.getStringWithOriginalObjectivesForCsvFile() + "\n");
        this.streamForCombinedParetoObjectivesInTxt.print(solution.getStringWithOriginalObjectives() + "\n");
        this.streamForCombinedParetoReducedObjectives.print(solution.getOriginalObjectives() + "\n");
    }

//    public void saveBestSolutionFoundInTxtFile(Solution solution) {
//        this.streamForCombinedPareto.print(copyArrayToListDouble(solution.getObjectives()) + "\n");
//    }

    public void savePopulation(List<ProblemSolution> population) {
        for (ProblemSolution solution : population) {
            saveBestSolutionFoundInTxtFile(solution);
        }
    }

//    public void savePopulationOfSolutions(List<Solution> population) {
//        for (Solution solution : population) {
//            saveBestSolutionFoundInTxtFile(solution);
//        }
//    }

    private List<Double> copyArrayToListDouble(double[] array) {
        List<Double> list = new ArrayList<>();
        int size = array.length;

        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

//    public void saveListOfNondominatedPopulation(List<NondominatedPopulation> result) {
//        int executionNumber = 0;
//        initializeData();
//        for (NondominatedPopulation population : result) {
//            initializeFileForExecutionNumber(executionNumber);
//            for (Solution solution : population) {
//                streamForCombinedParetoObjectivesInExecution
//                        .print(convertSolution(solution)
//                                .getStringWithOriginalObjectivesForCsvFile() + "\n");
//            }
//            executionNumber++;
//        }
//    }

    private void initializeFileForExecutionNumber(int executionNumber) {
        try {
            streamForCombinedParetoObjectivesInExecution = new PrintStream(resultsPath + "/"
                    + fileName + "_Execution_" + executionNumber + ".csv");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeData() {
        RankedList rankedList = new RankedList(instance.getNumberOfNodes());
        rankedList.setAlphaD(0.20)
                .setAlphaP(0.15)
                .setAlphaT(0.10)
                .setAlphaV(0.55);

        instance.setNumberOfRequests(50)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        subProblem = new VRPDRT(instance, instancePath, rankedList);

//        problem = new MOEAVRPDRT(instancePath)
//                .setNumberOfObjectives(9)
//                .setNumberOfVariables(1)
//                .setNumberOfConstraints(0);
    }

    private List<Integer> copyArrayToListInteger(int[] array) {
        List<Integer> list = new ArrayList<>();
        int size = array.length;

        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

//    public ProblemSolution convertSolution(Solution solution) {
//        initializeData();
//        ProblemSolution ps = subProblem
//                .rebuildSolution(copyArrayToListInteger(EncodingUtils.getPermutation(solution.getVariable(0))),
//                        subProblem.getData().getRequests());
//        return ps;
//    }
}
