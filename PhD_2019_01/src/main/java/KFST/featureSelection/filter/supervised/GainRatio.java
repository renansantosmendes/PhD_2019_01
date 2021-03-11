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
import KFST.util.MathFunc;
import java.util.Arrays;
import KFST.featureSelection.filter.WeightedFilterApproach;

/**
 * This java class is used to implement the gain ratio method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.WeightedFilterApproach
 * @see KFST.featureSelection.FeatureWeighting
 * @see KFST.featureSelection.FeatureSelection
 */
public class GainRatio extends WeightedFilterApproach {

    private final double ERROR_DENOMINATOR = 0.0001;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains 
     * (<code>sizeSelectedFeatureSubset</code>) in which 
     * <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number of 
     * selected features
     */
    public GainRatio(Object... arguments) {
        super((int)arguments[0]);
    }
    
    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     */
    public GainRatio(int sizeSelectedFeatureSubset) {
        super(sizeSelectedFeatureSubset);
    }

    /**
     * computes the split information values of the data for all features
     *
     * @return an array of the split information values
     */
    private double[] splitInformation() {
        double[] splitInformationValues = new double[numFeatures];

        for (int i = 0; i < numFeatures; i++) {
            ArraysFunc.sortArray2D(trainSet, i); // sorts the dataset values corresponding to a given feature(feature i)
            int indexStart = 0;
            double startValue = trainSet[indexStart][i];
            for (int j = 1; j < trainSet.length; j++) {
                if (startValue != trainSet[j][i]) {
                    double prob = (j - indexStart) / (double) trainSet.length;
                    splitInformationValues[i] -= prob * MathFunc.log2(prob);
                    indexStart = j;
                    startValue = trainSet[indexStart][i];
                }
            }
            double prob = (trainSet.length - indexStart) / (double) trainSet.length;
            splitInformationValues[i] -= prob * MathFunc.log2(prob);
            if (splitInformationValues[i] == 0) {
                splitInformationValues[i] = ERROR_DENOMINATOR;
            }
        }

        return splitInformationValues;
    }

    /**
     * starts the feature selection process by gain ratio(GR) method
     */
    @Override
    public void evaluateFeatures() {
        double[] infoGainValues;
        double[] splitInfoValues;
        featureValues = new double[numFeatures];
        int[] indecesGR;

        //computes the information gain values of the data
        InformationGain infoGain = new InformationGain(numFeatures);
        infoGain.loadDataSet(trainSet, numFeatures, numClass);
        infoGain.evaluateFeatures();
        infoGainValues = infoGain.getFeatureValues();

        //computes the split information values of the data
        splitInfoValues = splitInformation();

        //computes the gain ratio values
        for (int i = 0; i < numFeatures; i++) {
            featureValues[i] = infoGainValues[i] / splitInfoValues[i];
//            System.out.println(i + ")= " + infoGainValues[i] + " , " + splitInfoValues[i] + " , " + featureValues[i]);
        }

        indecesGR = ArraysFunc.sortWithIndex(Arrays.copyOf(featureValues, featureValues.length), true);
        selectedFeatureSubset = Arrays.copyOfRange(indecesGR, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }
}
