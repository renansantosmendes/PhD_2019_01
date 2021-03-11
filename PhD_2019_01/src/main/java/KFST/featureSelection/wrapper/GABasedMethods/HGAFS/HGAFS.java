/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016-2018 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.featureSelection.wrapper.GABasedMethods.HGAFS;

import KFST.featureSelection.wrapper.GABasedMethods.BasicGA;
import KFST.util.ArraysFunc;
import java.util.ArrayList;

/**
 * This java class is used to implement feature selection method based on hybrid
 * genetic algorithm for feature selection using local search (HGAFS) in which
 * the type of Population is extended from Population class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.GABasedMethods.BasicGA
 * @see KFST.featureSelection.wrapper.WrapperApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class HGAFS extends BasicGA<Population> {

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains ( <code>path</code>,
     * <code>numFeatures</code>, <code>classifierType</code>,
     * <code>selectedClassifierPan</code>, <code>selectionType</code>,
     * <code>crossoverType</code>, <code>mutationType</code>,
     * <code>replacementType</code>, <code>numIteration</code>
     * <code>populationSize</code>, <code>crossoverRate</code>,
     * <code>mutationRate</code>, <code>kFolds</code> , <code>epsilon</code> ,
     * <code>mu</code> ) in which <code><b><i>path</i></b></code> is the path of
     * the project, <code><b><i>numFeatures</i></b></code> is the number of
     * original features in the dataset,
     * <code><b><i>classifierType</i></b></code> is the classifier type for
     * evaluating the fitness of a solution,
     * <code><b><i>selectedClassifierPan</i></b></code> is the selected
     * classifier panel, <code><b><i>selectionType</i></b></code> is used for
     * selecting parents from the individuals of a population according to their
     * fitness, <code><b><i>crossoverType</i></b></code> is used for recombining
     * the parents to generate new offsprings based on crossover rate,
     * <code><b><i>mutationType</i></b></code> is used for mutating new
     * offsprings by changing the value of some genes in them based on mutation
     * rate, <code><b><i>replacementType</i></b></code> is used for handling
     * populations from one generation to the next generation,
     * <code><b><i>numIteration</i></b></code> is the maximum number of allowed
     * iterations that algorithm repeated,
     * <code><b><i>populationSize</i></b></code> is the size of population of
     * candidate solutions, <code><b><i>crossoverRate</i></b></code> is the
     * probability of crossover operation, <code><b><i>mutationRate
     * </i></b></code> is the probability of mutation operation,
     * <code><b><i>kFolds</i></b></code> is the number of equal sized subsamples
     * that is used in k-fold cross validation,
     * <code><b><i>epsilon</i></b></code> is the epsilon parameter used in the
     * subset size determining scheme, and <code><b><i>mu</i></b></code> is the
     * mu parameter used in the local search operation
     */
    public HGAFS(Object... arguments) {
        super(arguments);
        population = new Population((double) arguments[13], (double) arguments[14]);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected int[] createSelectedFeatureSubset() {
        ArrayList<Integer> featSubset = new ArrayList();
        population.evaluateFitness();
        Individual fittestIndividual = population.getFittestIndividual();

        for (int i = 0; i < Population.PROBLEM_DIMENSION; i++) {
            if (fittestIndividual.genes[i]) {
                featSubset.add(i);
            }
        }
        return featSubset.stream().mapToInt(i -> i).toArray();
    }

    /**
     * starts the feature selection process by hybrid genetic algorithm for
     * feature selection using local search (HGAFS)
     */
    @Override
    public void evaluateFeatures() {
        Population.fitnessEvaluator.createTempDirectory();
        Population.fitnessEvaluator.setDataInfo(trainSet, nameFeatures, classLabel);
        population.setDataInfo(trainSet);
        population.initialization();
        for (int i = 0; i < NUM_ITERATION; i++) {
            System.out.println("\nIteration " + i + ":\n\n");
            population.evaluateFitness();
            population.operateSelection();
            population.operateCrossOver();
            population.operateMutation();
            population.operateLocalSearch();
            population.operateGenerationReplacement();
        }

        selectedFeatureSubset = createSelectedFeatureSubset();
        numSelectedFeature = selectedFeatureSubset.length;

        ArraysFunc.sortArray1D(selectedFeatureSubset, false);

//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
        Population.fitnessEvaluator.deleteTempDirectory();
    }
}
