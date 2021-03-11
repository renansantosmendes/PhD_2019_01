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
package KFST.featureSelection.filter.supervised;

import KFST.util.MathFunc;
import KFST.util.ArraysFunc;
import java.util.Arrays;
import KFST.featureSelection.filter.WeightedFilterApproach;

/**
 * This java class is used to implement the Fisher score method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.WeightedFilterApproach
 * @see KFST.featureSelection.FeatureWeighting
 * @see KFST.featureSelection.FeatureSelection
 */
public class FisherScore extends WeightedFilterApproach {

    private final double ERROR_DENOMINATOR = 0.0001;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameter contains 
     * (<code>sizeSelectedFeatureSubset</code>) in which 
     * <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number of 
     * selected features
     */
    public FisherScore(Object... arguments) {
        super((int)arguments[0]);
    }
    
    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public FisherScore(int sizeSelectedFeatureSubset) {
        super(sizeSelectedFeatureSubset);
    }

    /**
     * starts the feature selection process by Fisher score(FS) method
     */
    @Override
    public void evaluateFeatures() {
        featureValues = new double[numFeatures];
        double[] meanFeatures = new double[numFeatures]; // the mean values of each feature
        double[][] meanFeatureClass = new double[numClass][numFeatures]; // the mean values of each feature on each class
        double[][] varianceFeatureClass = new double[numClass][numFeatures]; // the variance values of each feature on each class
        int[] numClassSample = new int[numClass]; // the number of samples in each class
        int[] indecesFS;

        //counts the number of samples in each class
        for (double[] sample : trainSet) {
            numClassSample[(int) sample[numFeatures]]++;
        }

        //computes the mean values of each feature
        for (int i = 0; i < numFeatures; i++) {
            meanFeatures[i] = MathFunc.computeMean(trainSet, i);
        }

        //computes the mean values of each feature on each class
        for (double[] sample : trainSet) {
            int indexClass = (int) sample[numFeatures];
            for (int j = 0; j < numFeatures; j++) {
                meanFeatureClass[indexClass][j] += sample[j];
            }
        }
        for (int i = 0; i < numClass; i++) {
            for (int j = 0; j < numFeatures; j++) {
                meanFeatureClass[i][j] /= numClassSample[i];
            }
        }

        //computes the variance values of each feature on each class
        for (double[] sample : trainSet) {
            int indexClass = (int) sample[numFeatures];
            for (int j = 0; j < numFeatures; j++) {
                varianceFeatureClass[indexClass][j] += Math.pow(sample[j] - meanFeatureClass[indexClass][j], 2);
            }
        }
        for (int i = 0; i < numClass; i++) {
            for (int j = 0; j < numFeatures; j++) {
                varianceFeatureClass[i][j] /= numClassSample[i];
            }
        }

        //computes the Fisher score values of the features
        for (int i = 0; i < numFeatures; i++) {
            double sum1 = 0;
            double sum2 = 0;
            for (int j = 0; j < numClass; j++) {
                sum1 += numClassSample[j] * Math.pow(meanFeatureClass[j][i] - meanFeatures[i], 2);
                sum2 += numClassSample[j] * varianceFeatureClass[j][i];
            }
            if (sum2 == 0) {
                featureValues[i] = sum1 / ERROR_DENOMINATOR;
            } else {
                featureValues[i] = sum1 / sum2;
            }
        }

        indecesFS = ArraysFunc.sortWithIndex(Arrays.copyOf(featureValues, featureValues.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println("Fisher(f" + i + ") = " + featureValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesFS, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }
}
