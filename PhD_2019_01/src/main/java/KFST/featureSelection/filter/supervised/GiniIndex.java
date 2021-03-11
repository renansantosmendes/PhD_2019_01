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

import KFST.util.ArraysFunc;
import java.util.Arrays;
import KFST.featureSelection.filter.WeightedFilterApproach;

/**
 * This java class is used to implement the Gini index method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.WeightedFilterApproach
 * @see KFST.featureSelection.FeatureWeighting
 * @see KFST.featureSelection.FeatureSelection
 */
public class GiniIndex extends WeightedFilterApproach {

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains 
     * (<code>sizeSelectedFeatureSubset</code>) in which 
     * <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number of 
     * selected features
     */
    public GiniIndex(Object... arguments) {
        super((int)arguments[0]);
    }
    
    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public GiniIndex(int sizeSelectedFeatureSubset) {
        super(sizeSelectedFeatureSubset);
    }

    /**
     * computes the gini value of the data given by start and end indices
     *
     * @param indexStart the start index of the dataset
     * @param indexEnd the end index of the dataset
     * 
     * @return the gini value
     */
    private double computeGini(int indexStart, int indexEnd) {
        double gini = 0;
        int sizeUsedData = indexEnd - indexStart;
        int[] countClassSample = new int[numClass];

        //counts the number of samples in each class
        for (int i = indexStart; i < indexEnd; i++) {
            countClassSample[(int) trainSet[i][numFeatures]]++;
        }

        //computes the gini value of the data given by start and end indeces
        for (int i = 0; i < numClass; i++) {
            if (countClassSample[i] != 0) {
                double prob = countClassSample[i] / (double) sizeUsedData;
                gini += Math.pow(prob, 2);
            }
        }

        return 1 - gini;
    }

    /**
     * starts the feature selection process by Gini index(GI) method
     */
    @Override
    public void evaluateFeatures() {
        double giniSystem = computeGini(0, trainSet.length); // computes the gini value of the system (over all dataset)
        featureValues = new double[numFeatures];
        int[] indecesGI;

        //computes the Gini index values of each feature
        for (int i = 0; i < numFeatures; i++) {
            double giniFeature = 0;
            ArraysFunc.sortArray2D(trainSet, i); // sorts the dataset values corresponding to a given feature(feature i)
            int indexStart = 0;
            double startValue = trainSet[indexStart][i];
            for (int j = 1; j < trainSet.length; j++) {
                if (startValue != trainSet[j][i]) {
                    double prob = (j - indexStart) / (double) trainSet.length;
                    giniFeature += prob * computeGini(indexStart, j);
                    indexStart = j;
                    startValue = trainSet[indexStart][i];
                }
            }
            double prob = (trainSet.length - indexStart) / (double) trainSet.length;
            giniFeature += prob * computeGini(indexStart, trainSet.length);
            featureValues[i] = giniSystem - giniFeature;
        }

        indecesGI = ArraysFunc.sortWithIndex(Arrays.copyOf(featureValues, featureValues.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + featureValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesGI, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }
}
