/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.FileNotFoundException;
import jmetal.problems.ZDT.ZDT2;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.*;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.problem.multiobjective.wfg.*;

/**
 *
 * @author renansantos
 */
public class MainClass {

    public static void main(String[] args) throws FileNotFoundException {
        
        //initializing problem and algorithm variables
        int originalDimension = 3;
        int reducedDimension = 2;
        int numberOfVariables = 10;
        int populationSize = 100;
        int resultPopulationSize = 100;
        int maxEvaluations = 80000;
        double neighborhoodSelectionProbability = 0.01;
        int maximumNumberOfReplacedSolutions = 10;
        int neighborSize = 10;
        
        //initializing benchmark problem
        Problem originalProblem = new DTLZ3(numberOfVariables, originalDimension); 
        Problem reducedProblem = new DTLZ3(numberOfVariables, reducedDimension); 
        
        //initializing algorithm operators
        CrossoverOperator crossover = new DifferentialEvolutionCrossover();
        MutationOperator mutation = new PolynomialMutation(0.02, 20);
        SelectionOperator selection = new BinaryTournamentSelection();

        //initializing and running MOEA/D
        Algorithm algorithm = new MOEAD(
                originalProblem,
                reducedProblem,
                reducedDimension,
                populationSize,
                resultPopulationSize,
                maxEvaluations,
                mutation,
                crossover,
                null,
                "home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights",
                neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions,
                neighborSize,
                "/home/renansantos/Área de Trabalho/Doutorado/Experimentos/OnCL-MOEAD");

        algorithm.run();
        System.out.println("OnCL-MOEAD Finished!");
        Algorithm algorithm2 = new MOEAD(
                originalProblem,
                
                populationSize,
                resultPopulationSize,
                maxEvaluations,
                mutation,
                crossover,
                null,
                "home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights",
                neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions,
                neighborSize,
                "/home/renansantos/Área de Trabalho/Doutorado/Experimentos/MOEAD");

        algorithm2.run();
        System.out.println("MOEAD Finished!");
    }
}
