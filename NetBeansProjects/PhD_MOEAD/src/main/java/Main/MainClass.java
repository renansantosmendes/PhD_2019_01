/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import AggregatedProblems.AggDTLZ5_5;
import Algorithms.AbstractMOEAD.FunctionType;
import Algorithms.MOEAD;
import Algorithms.SolutionsOutput;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.*;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ5;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;


/**
 *
 * @author renansantos
 */
public class MainClass {

    public static void main(String[] args) throws JMetalException, FileNotFoundException {
        
        //initializing problem and algorithm variables
        int numberOfObjectives = 2;//5,10,15
        int numberOfVariables = 10;
        int populationSize = 100;
        int resultPopulationSize = 100;
        int maxEvaluations = 80000;
        double neighborhoodSelectionProbability = 0.01;
        int maximumNumberOfReplacedSolutions = 10;
        int neighborSize = 10;
        
        //initializing benchmark problem
        Problem problem = new AggDTLZ5_5(numberOfVariables, numberOfObjectives,2); 
        
        //initializing algorithm operators
        CrossoverOperator crossover = new DifferentialEvolutionCrossover();
        MutationOperator mutation = new PolynomialMutation(0.02, 20);
        SelectionOperator selection = new BinaryTournamentSelection();
        
        MOEAD algorithm = new MOEAD(
                problem,
                populationSize,
                resultPopulationSize,
                maxEvaluations,
                mutation,
                crossover,
                FunctionType.AGG,
                "home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights",
                neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions,
                neighborSize);

       
        algorithm.run();
        System.out.println("MOEAD Finished!");
        System.out.println(algorithm.getResult());
        
        
        new SolutionsOutput(problem, algorithm.getResult(), "MOEAD_Result").saveSolutions();
        
        
        List<DoubleSolution> population = new ArrayList<>();
        int numberOfSolutions = 10000;
        population.addAll(algorithm.initializePopulation(problem, numberOfSolutions));
//        population.forEach(u -> System.out.println(u));
        
        //new SolutionsOutput(problem, population).saveSolutions();
        
        
    }
}
