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
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.featureSelection.filter.unsupervised;

import KFST.util.ArraysFunc;
import KFST.util.MathFunc;
import java.util.Arrays;
import KFST.featureSelection.filter.WeightedFilterApproach;

/**
 * This java class is used to implement the term variance method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.WeightedFilterApproach
 * @see KFST.featureSelection.FeatureWeighting
 * @see KFST.featureSelection.FeatureSelection
 */
public class TermVariance extends WeightedFilterApproach {

    /**
     * initializes the parameters
     *
     * @param arguments array of parameter contains
     * (<code>sizeSelectedFeatureSubset</code>) in which
     * <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number of
     * selected features
     */
    public TermVariance(Object... arguments) {
        super((int) arguments[0]);
    }

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public TermVariance(int sizeSelectedFeatureSubset) {
        super(sizeSelectedFeatureSubset);
    }

    /**
     * starts the feature selection process by term variance(TV) method
     */
    @Override
    public void evaluateFeatures() {
        double[] meanValues = new double[numFeatures];
        featureValues = new double[numFeatures];
        int[] indecesTV;

        //computes the mean values of each feature
        for (int i = 0; i < numFeatures; i++) {
            meanValues[i] = MathFunc.computeMean(trainSet, i);
        }

        //computes the variance values of each feature
        for (int i = 0; i < numFeatures; i++) {
            featureValues[i] = MathFunc.computeVariance(trainSet, meanValues[i], i);
        }

        indecesTV = ArraysFunc.sortWithIndex(Arrays.copyOf(featureValues, featureValues.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + featureValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesTV, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }
}
