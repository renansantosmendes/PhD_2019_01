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
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        initializePopulation();
        saveFirstPopulation();
        savePopulation();
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint();

        evaluations = populationSize;
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = permutation[i];

                AbstractMOEAD.NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                updateNeighborhood(child, subProblemId, neighborType);
            }
        } while (evaluations < maxEvaluations);
    }

    protected void saveFirstPopulation() {
        try {
            PrintStream streamForDataInCsv = new PrintStream("/home/renansantos/Área de Trabalho/Doutorado/GMM/SOLUTIONS.csv");
            List<DoubleSolution> list = this.population;

            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(0).getNumberOfObjectives(); j++) {
                    streamForDataInCsv.print(list.get(i).getObjective(j) + "|");
                }
                streamForDataInCsv.print("\n");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    protected void savePopulation() {
        List<DoubleSolution> list = this.population;
        this.streamForPopulationInCsv.print("#\n");
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).getNumberOfObjectives(); j++) {
                this.streamForPopulationInCsv.print(list.get(i).getObjective(j) + " ");
            }
            this.streamForPopulationInCsv.print("\n");
        }
        this.streamForPopulationInCsv.print("#");

    }

    protected void initializePopulation() {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) problem.createSolution();

            problem.evaluate(newSolution);
            population.add(newSolution);
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
