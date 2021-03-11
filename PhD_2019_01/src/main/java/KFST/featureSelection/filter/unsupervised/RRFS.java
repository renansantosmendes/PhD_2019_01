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
import KFST.featureSelection.filter.FilterApproach;

/**
 * This java class is used to implement the relevance-redundancy feature
 * selection(RRFS) method. Also, it is the unsupervised version of the RRFS.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.FilterApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class RRFS extends FilterApproach {

    private final double MAX_SIM_VALUE; //maximum allowed similarity between two features

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains 
     * (<code>sizeSelectedFeatureSubset</code>, <code>maxSimilarity</code>) in 
     * which <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number 
     * of selected features, and <code><b><i>maxSimilarity</i></b></code> is 
     * maximum allowed similarity between two features
     */
    public RRFS(Object... arguments) {
        super((int)arguments[0]);
        MAX_SIM_VALUE = (double)arguments[1];
    }
    
    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     * @param maxSimilarity maximum allowed similarity between two features
     */
    public RRFS(int sizeSelectedFeatureSubset, double maxSimilarity) {
        super(sizeSelectedFeatureSubset);
        MAX_SIM_VALUE = maxSimilarity;
    }

    /**
     * computes the mean absolute difference(MAD) values of the features
     *
     * @param indexFeature index of the given feature
     * @param mean the mean value of the given feature (indexFeature)
     * 
     * @return the mean absolute difference
     */
    private double computeMAD(int indexFeature, double mean) {
        double sum = 0;
        for (double[] sample : trainSet) {
            sum += Math.abs(sample[indexFeature] - mean);
        }
        return sum;
    }

    /**
     * starts the feature selection process by relevance-redundancy
     * feature selection(RRFS) method
     */
    @Override
    public void evaluateFeatures() {
        double[] MADValues = new double[numFeatures]; // the mean absolute difference (MAD) values
        int[] indexFeatures;
        int prev, next;

        //computes the mean absolute difference(MAD) values
        for (int i = 0; i < numFeatures; i++) {
            MADValues[i] = computeMAD(i, MathFunc.computeMean(trainSet, i));
        }

        //sorts the features by their relevance values(MAD values)
        indexFeatures = ArraysFunc.sortWithIndex(MADValues, true);

        //starts the feature selection process
        selectedFeatureSubset[0] = indexFeatures[0];
        prev = 0;
        next = 1;
        for (int i = 1; i < numFeatures && next < numSelectedFeature; i++) {
            double simValue = Math.abs(MathFunc.computeSimilarity(trainSet, indexFeatures[i], indexFeatures[prev]));
            if (simValue < MAX_SIM_VALUE) {
                selectedFeatureSubset[next] = indexFeatures[i];
                prev = i;
                next++;
            }
        }

//        for (int i = 0; i < next; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }

        if (next < numSelectedFeature) {
            selectedFeatureSubset = Arrays.copyOfRange(selectedFeatureSubset, 0, next);
        }
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
    }
}
