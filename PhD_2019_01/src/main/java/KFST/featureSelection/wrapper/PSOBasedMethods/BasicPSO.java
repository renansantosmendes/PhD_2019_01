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
package KFST.featureSelection.wrapper.PSOBasedMethods;

import KFST.classifier.ClassifierType;
import KFST.featureSelection.FitnessEvaluator;
import KFST.featureSelection.wrapper.WrapperApproach;

/**
 * The abstract class contains the main methods and fields that are used in all
 * PSO-based feature selection methods.
 *
 * @param <SwarmType> the type of swarm implemented in PSO algorithm
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.WrapperApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public abstract class BasicPSO<SwarmType> extends WrapperApproach {

    protected SwarmType swarm;
    protected final int NUM_ITERATION;
    protected final int K_FOLDS;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains ( <code>path</code>,
     * <code>numFeatures</code>, <code>classifierType</code>,
     * <code>selectedClassifierPan</code>, <code>numIteration</code>
     * <code>populationSize</code>, <code>inertiaWeight</code>,
     * <code>parameter c1</code>, <code>parameter c2</code>,
     * <code>startPosInterval</code>, <code>endPosInterval</code>,
     * <code>minVelocity</code>, <code>maxVelocity</code>, <code>kFolds</code>)
     * in which <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>numFeatures</i></b></code> is the number of original features
     * in the dataset, <code><b><i>classifierType</i></b></code> is the
     * classifier type for evaluating the fitness of a solution,
     * <code><b><i>selectedClassifierPan</i></b></code> is the selected
     * classifier panel, <code><b><i>numIteration</i></b></code> is the maximum
     * number of allowed iterations that algorithm repeated,
     * <code><b><i>populationSize</i></b></code> is the size of population of
     * candidate solutions, <code><b><i>inertiaWeight</i></b></code> is the
     * inertia weight in the velocity updating rule, <code><b><i>parameter
     * c1</i></b></code> is the acceleration constant in the velocity updating
     * rule, <code><b><i>parameter c2</i></b></code> is the acceleration
     * constant in the velocity updating rule,
     * <code><b><i>startPosInterval</i></b></code> is the position interval
     * start value, <code><b><i>endPosInterval</i></b></code> is the position
     * interval end value, <code><b><i>minVelocity</i></b></code> is the
     * velocity interval start value, <code><b><i>maxVelocity</i></b></code> is
     * the velocity interval end value, and <code><b><i>kFolds</i></b></code> is
     * the number of equal sized subsamples that is used in k-fold cross
     * validation
     */
    public BasicPSO(Object... arguments) {
        super((String) arguments[0]);
        BasicSwarm.PROBLEM_DIMENSION = (int) arguments[1];
        this.NUM_ITERATION = (int) arguments[4];
        BasicSwarm.POPULATION_SIZE = (int) arguments[5];
        BasicSwarm.INERTIA_WEIGHT = (double) arguments[6];
        BasicSwarm.C1 = (double) arguments[7];
        BasicSwarm.C2 = (double) arguments[8];
        BasicSwarm.START_POS_INTERVAL = (double) arguments[9];
        BasicSwarm.END_POS_INTERVAL = (double) arguments[10];
        BasicSwarm.MIN_VELOCITY = (double) arguments[11];
        BasicSwarm.MAX_VELOCITY = (double) arguments[12];
        this.K_FOLDS = (int) arguments[13];
        BasicSwarm.fitnessEvaluator = new FitnessEvaluator(TEMP_PATH, (ClassifierType) arguments[2],
                arguments[3], this.K_FOLDS);
    }

    /**
     * This method creates the selected feature subset based on global best
     * position in the swarm.
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
            return "The parameter values of PSO-based method (number of folds) are incorred.";
        }
        return "";
    }
}
