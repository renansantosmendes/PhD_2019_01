/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.FileNotFoundException;
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
        int originalDimension = 7;
        int reducedDimension = 3;
        int numberOfVariables = 10;
        int populationSize = 100;
        int resultPopulationSize = 100;
        int maxEvaluations = 40000;
        double neighborhoodSelectionProbability = 0.01;
        int maximumNumberOfReplacedSolutions = 10;
        int neighborSize = 10;
        
        //initializing benchmark problem
        Problem originalProblem = new DTLZ2(numberOfVariables, originalDimension); 
        Problem reducedProblem = new DTLZ2(numberOfVariables, reducedDimension); 
        
        //initializing algorithm operators
        CrossoverOperator crossover = new DifferentialEvolutionCrossover();
        MutationOperator mutation = new PolynomialMutation();
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
                "/home/renansantos/Área de Trabalho/Doutorado/WFG/WFG_1.15");

        algorithm.run();
    }
}
