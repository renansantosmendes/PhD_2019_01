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


/**
 *
 * @author renansantos
 */
public class MainClass {

    public static void main(String[] args) throws FileNotFoundException {
        Problem problem = new DTLZ7(10, 2); // The problem to solve
        CrossoverOperator crossover = new DifferentialEvolutionCrossover();
        MutationOperator mutation = new PolynomialMutation();
        SelectionOperator selection = new BinaryTournamentSelection();
        
        Algorithm algorithm = new MOEAD(problem, 1000, 1000, 300000, mutation,
                crossover, null,
                "home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights",
                0.01, 10, 10);

        algorithm.run();
        SolutionsOutput s = new SolutionsOutput(algorithm, "/home/renansantos/Área de Trabalho/MOEAD");
        s.saveResult();
        
    }
}
