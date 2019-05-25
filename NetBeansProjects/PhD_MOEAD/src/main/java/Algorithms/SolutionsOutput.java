/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author renansantos
 */
public class SolutionsOutput {

    List<DoubleSolution> population = new ArrayList<>();
    Problem problem;
    public SolutionsOutput(Problem problemPar, List<DoubleSolution> populationPar) {
        population.clear();
        population.addAll(populationPar);
        problem = problemPar;
    }

    public void saveSolutions() {
        String folderName = "ClusterAnalysisRandomSolutions";
        String fileName = problem.getName() + "-" + problem.getNumberOfObjectives();

        boolean success = (new File(folderName)).mkdirs();
        if (!success) {
            System.out.println("Folder already exists!");
        }
        try {
            PrintStream printStreamSolutions = new PrintStream(folderName + "/" + fileName + "-random_solutions.csv");
            
            for(DoubleSolution solution: population){
                printStreamSolutions.print(solution + "\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SolutionsOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
