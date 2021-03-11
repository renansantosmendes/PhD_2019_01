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
package KFST.featureSelection.wrapper.GABasedMethods;

import KFST.classifier.ClassifierType;
import KFST.featureSelection.FitnessEvaluator;
import KFST.featureSelection.wrapper.WrapperApproach;
import KFST.gui.featureSelection.wrapper.GABased.CrossOverType;
import KFST.gui.featureSelection.wrapper.GABased.MutationType;
import KFST.gui.featureSelection.wrapper.GABased.ReplacementType;
import KFST.gui.featureSelection.wrapper.GABased.SelectionType;

/**
 * The abstract class contains the main methods and fields that are used in all
 * GA-based feature selection methods.
 *
 * @param <PopulationType> the type of population implemented in GA algorithm
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.WrapperApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public abstract class BasicGA<PopulationType> extends WrapperApproach {

    protected PopulationType population;
    protected final int NUM_ITERATION;
    protected final int K_FOLDS;

    /**
     * Initializes the parameters
     *
     * @param arguments array of parameters contains ( <code>path</code>,
     * <code>numFeatures</code>, <code>classifierType</code>,
     * <code>selectedClassifierPan</code>, <code>selectionType</code>,
     * <code>crossoverType</code>, <code>mutationType</code>,
     * <code>replacementType</code>, <code>numIteration</code>
     * <code>populationSize</code>, <code>crossoverRate</code>,
     * <code>mutationRate</code>, <code>kFolds</code>) in which
     * <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>numFeatures</i></b></code> is the number of original features
     * in the dataset, <code><b><i>classifierType</i></b></code> is the
     * classifier type for evaluating the fitness of a solution,
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
     * </i></b></code> is the probability of mutation operation, and
     * <code><b><i>kFolds</i></b></code> is the number of equal sized subsamples
     * that is used in k-fold cross validation
     */
    public BasicGA(Object... arguments) {
        super((String) arguments[0]);
        BasicPopulation.PROBLEM_DIMENSION = (int) arguments[1];
        BasicPopulation.SELECTION_TYPE = (SelectionType) arguments[4];
        BasicPopulation.CROSSOVER_TYPE = (CrossOverType) arguments[5];
        BasicPopulation.MUTATION_TYPE = (MutationType) arguments[6];
        BasicPopulation.REPLACEMENT_TYPE = (ReplacementType) arguments[7];
        this.NUM_ITERATION = (int) arguments[8];
        BasicPopulation.POPULATION_SIZE = (int) arguments[9];
        BasicPopulation.CROSS_OVER_RATE = (double) arguments[10];
        BasicPopulation.MUTATION_RATE = (double) arguments[11];
        this.K_FOLDS = (int) arguments[12];
        BasicPopulation.fitnessEvaluator = new FitnessEvaluator(TEMP_PATH, (ClassifierType) arguments[2],
                arguments[3], this.K_FOLDS);
    }

    /**
     * This method creates the selected feature subset based on the fittest
     * individual in the population.
     *
     * @return the array of indices of the selected feature subset
     */
    protected abstract int[] createSelectedFeatureSubset();

    /**
     * {@inheritDoc }
     */
    @Override
    public String validate() {
        if (K_FOLDS > trainSet.length) {
            return "The parameter values of GA-based method (number of folds) are incorred.";
        }
        return "";
    }
}
