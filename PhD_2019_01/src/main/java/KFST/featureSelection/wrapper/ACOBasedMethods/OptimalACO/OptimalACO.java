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
package KFST.featureSelection.wrapper.ACOBasedMethods.OptimalACO;

import KFST.featureSelection.wrapper.ACOBasedMethods.BasicACO;
import KFST.featureSelection.wrapper.ACOBasedMethods.BasicColony;
import KFST.util.ArraysFunc;

/**
 * This java class is used to implement feature selection method based on
 * optimal ant colony optimization (Optimal ACO) in which the type of Colony is
 * extended from Colony class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.ACOBasedMethods.BasicACO
 * @see KFST.featureSelection.wrapper.WrapperApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class OptimalACO extends BasicACO<Colony> {

    /**
     * Initializes the parameters
     *
     * @param arguments array of parameters contains (<code>path</code>,
     * <code>numFeatures</code>, <code>classifierType</code>,
     * <code>selectedClassifierPan</code>, <code>numIteration</code>,
     * <code>colonySize</code>, <code>alphaParameter</code>,
     * <code>betaParameter</code>, <code>evaporationRate</code>,
     * <code>kFolds</code>, <code>initPheromone</code>,
     * <code>phiParameter</code>) in which <code><b><i>path</i></b></code> is
     * the path of the project, <code><b><i>numFeatures</i></b></code> is the
     * number of original features in the dataset,
     * <code><b><i>classifierType</i></b></code> is the classifier type for
     * evaluating the fitness of a solution,
     * <code><b><i>selectedClassifierPan</i></b></code> is the selected
     * classifier panel, <code><b><i>numIteration</i></b></code> is the maximum
     * number of allowed iterations that algorithm repeated,
     * <code><b><i>colonySize</i></b></code> is the size of colony of candidate
     * solutions, <code><b><i>alphaParameter</i></b></code> is the alpha
     * parameter used in the state transition rule that shows the relative
     * importance of the pheromone, <code><b><i>betaParameter</i></b></code> is
     * the beta parameter used in the state transition rule that shows the
     * relative importance of heuristic information,
     * <code><b><i>evaporationRate</i></b></code> is the evaporation rate of the
     * pheromone, <code><b><i>kFolds</i></b></code> is the number of equal sized
     * subsamples that is used in k-fold cross validation,
     * <code><b><i>initPheromone</i></b></code> is the initial value of the
     * pheromone, and <code><b><i>phiParameter</i></b></code> is the phi
     * parameter used in the pheromone update rule for controlling the relative
     * weight of classifier performance and feature subset length
     */
    public OptimalACO(Object... arguments) {
        super(arguments);
        BasicColony.graphRepresentation = new GraphRepresentation(1, BasicColony.NUM_ORIGINAL_FEATURE);
        colony = new Colony((double) arguments[11]);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected int[] createSelectedFeatureSubset() {
        Ant betsAnt = colony.getBestAnt();
        return ArraysFunc.convertArrayListToInt(betsAnt.getFeatureSubset());
    }

    /**
     * starts the feature selection process by optimal ant colony optimization
     * (Optimal ACO) method
     */
    @Override
    public void evaluateFeatures() {
        Colony.fitnessEvaluator.createTempDirectory();
        Colony.fitnessEvaluator.setDataInfo(trainSet, nameFeatures, classLabel);
        colony.initialization();
        for (int i = 0; i < NUM_ITERATION; i++) {
            System.out.println("\nIteration " + i + ":\n\n");
            colony.setInitialState();
            colony.constructSolution();
            colony.operatePheromoneUpdateRule();
        }

        selectedFeatureSubset = createSelectedFeatureSubset();
        numSelectedFeature = selectedFeatureSubset.length;

        ArraysFunc.sortArray1D(selectedFeatureSubset, false);

//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
        Colony.fitnessEvaluator.deleteTempDirectory();
    }
}
