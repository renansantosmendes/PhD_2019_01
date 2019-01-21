/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author renansantos
 */
import java.io.FileNotFoundException;
import java.io.PrintStream;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing the MOEA/D-DE algorithm described in : Hui Li; Qingfu
 * Zhang, "Multiobjective Optimization Problems With Complicated Pareto Sets,
 * MOEA/D and NSGA-II," Evolutionary Computation, IEEE Transactions on , vol.13,
 * no.2, pp.284,302, April 2009. doi: 10.1109/TEVC.2008.925798
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MOEAD extends AbstractMOEAD<DoubleSolution> {

    protected DifferentialEvolutionCrossover differentialEvolutionCrossover;
    protected PrintStream streamForPopulationInCsv;
    protected PrintStream streamForFinalPopulationInCsv;

    public MOEAD(Problem<DoubleSolution> originalProblem,
            Problem<DoubleSolution> reducedProblem,
            int reducedDimension,
            int populationSize,
            int resultPopulationSize,
            int maxEvaluations,
            MutationOperator<DoubleSolution> mutation,
            CrossoverOperator<DoubleSolution> crossover,
            FunctionType functionType,
            String dataDirectory,
            double neighborhoodSelectionProbability,
            int maximumNumberOfReplacedSolutions,
            int neighborSize, String outPutFilePath) {

        super(originalProblem, reducedProblem, reducedDimension, populationSize, resultPopulationSize,
                maxEvaluations, crossover, mutation, functionType, dataDirectory, neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions, neighborSize);

        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;
        try {
            streamForPopulationInCsv = new PrintStream(outPutFilePath + "/PARETO_FRONTS.TXT");
            streamForFinalPopulationInCsv = new PrintStream(outPutFilePath + "/FINAL_PARETO_FRONT.TXT");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public MOEAD(Problem<DoubleSolution> problem,
            int populationSize,
            int resultPopulationSize,
            int maxEvaluations,
            MutationOperator<DoubleSolution> mutation,
            CrossoverOperator<DoubleSolution> crossover,
            FunctionType functionType,
            String dataDirectory,
            double neighborhoodSelectionProbability,
            int maximumNumberOfReplacedSolutions,
            int neighborSize, String outPutFilePath) {

        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);

        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;
        try {
            streamForPopulationInCsv = new PrintStream(outPutFilePath + "/PARETO_FRONTS.TXT");
            streamForFinalPopulationInCsv = new PrintStream(outPutFilePath + "/FINAL_PARETO_FRONT.TXT");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        initializePopulation();
        initializeReducedPopulationForMOEAD();
        saveFirstPopulation();
        insertFirstHashTagInFile();
        savePopulation();
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPointForMOEAD();

        evaluations = populationSize;
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);
            reducePopulationDimension();

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = permutation[i];

                AbstractMOEAD.NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                evaluateChild(child);

                evaluations++;

                updateIdealPointForMOEAD(child);
                updateNeighborhood(child, subProblemId, neighborType);
            }

            restorePopulationForMOEAD();
            savePopulation();
        } while (evaluations < maxEvaluations);
        saveFinalPopulation();
    }

    private void restorePopulationForMOEAD() {
        if (reducedProblem != null) {
            restorePopulation();
        }
    }

    private void initializeReducedPopulationForMOEAD() {
        if (reducedProblem != null) {
            initializeReducedPopulation();
        }
    }

    private void initializeIdealPointForMOEAD() {
        if (reducedProblem != null) {
            initializeIdealPoint(reducedDimension);
        } else {
            initializeIdealPoint();
        }
    }

    private void reducePopulationDimension() {
        if (reducedProblem != null) {
            reduceDimension();
        }
    }

    private void updateIdealPointForMOEAD(DoubleSolution child) {
        if (reducedProblem != null) {
            updateIdealPoint(child, reducedDimension);
        } else {
            updateIdealPoint(child);
        }
    }

    private void evaluateChild(DoubleSolution child) {
        if (reducedProblem != null) {
            reducedProblem.evaluate(child);
        } else {
            problem.evaluate(child);
        }
    }

    public void runExperiment() {
        // implementar o experimento que executa o algoritmo 30 vezes
    }

    protected void saveFirstPopulation() {
        String name = this.problem.getName() + "_" + this.problem.getNumberOfVariables()
                + "_" + this.problem.getNumberOfObjectives();
        try {
            PrintStream streamForDataInCsv = new PrintStream("/home/renansantos/Área de Trabalho/Doutorado/GMM/"
                    + name + "_SOLUTIONS.csv");
            List<DoubleSolution> list = this.population;

            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(0).getNumberOfObjectives(); j++) {
                    streamForDataInCsv.print(list.get(i).getObjective(j));
                    if (j < list.get(0).getNumberOfObjectives() - 1) {
                        streamForDataInCsv.print("|");
                    }

                }
                streamForDataInCsv.print("\n");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    protected void insertFirstHashTagInFile() {
        this.streamForPopulationInCsv.print("#");
    }

    protected void savePopulation() {
        List<DoubleSolution> list = this.getResult();
        this.streamForPopulationInCsv.print("\n");
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).getNumberOfObjectives(); j++) {
                this.streamForPopulationInCsv.print(list.get(i).getObjective(j) + " ");
            }
            this.streamForPopulationInCsv.print("\n");
        }
        this.streamForPopulationInCsv.print("#");

    }

    protected void saveFinalPopulation() {
        List<DoubleSolution> list = this.getResult();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).getNumberOfObjectives(); j++) {
                this.streamForFinalPopulationInCsv.print(list.get(i).getObjective(j));
                if (j < list.get(0).getNumberOfObjectives() - 1) {
                    this.streamForFinalPopulationInCsv.print(";");
                }
            }
            this.streamForFinalPopulationInCsv.print("\n");
        }
    }

    protected void initializePopulation() {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) problem.createSolution();

            problem.evaluate(newSolution);
            population.add(newSolution);
        }
    }

    protected void initializeReducedPopulation() {
        reducedPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) reducedProblem.createSolution();

            reducedProblem.evaluate(newSolution);
            reducedPopulation.add(newSolution);
        }
    }

    @Override
    public String getName() {
        return "MOEAD";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition";
    }
}
