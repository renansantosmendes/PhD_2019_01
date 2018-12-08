/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import static Main.AbstractMOEAD.FunctionType.TCHE;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD.FunctionType;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;

/**
 *
 * @author renansantos
 */
public class MainClass {

    public static void main(String[] args) {
        Problem problem = new DTLZ1(); // The problem to solve
        CrossoverOperator crossover = new DifferentialEvolutionCrossover();
        MutationOperator mutation = new PolynomialMutation();
        SelectionOperator selection = new BinaryTournamentSelection();
        Algorithm algorithm = new MOEAD(problem, 100, 100, 30000, mutation,
                crossover, TCHE,
                "home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights",
                0.01, 10, 10);     
    }
}
