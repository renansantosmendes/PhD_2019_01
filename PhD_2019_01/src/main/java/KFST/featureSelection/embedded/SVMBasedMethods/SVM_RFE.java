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
package KFST.featureSelection.embedded.SVMBasedMethods;

import KFST.gui.classifier.svmClassifier.SVMKernelType;
import KFST.util.ArraysFunc;
import KFST.util.FileFunc;
import KFST.util.MathFunc;
import java.util.Arrays;

/**
 * This java class is used to implement support vector machine method based on
 * recursive feature elimination (SVM_RFE).
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.embedded.EmbeddedApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class SVM_RFE extends SVMBasedMethods {

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains (<code>path</code>,
     * <code>kernelType</code>, <code>Parameter c</code>) in which
     * <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>kernelType</i></b></code> is the type of kernel to use, and
     * <code><b><i>Parameter c</i></b></code> is the complexity parameter C
     */
    public SVM_RFE(Object... arguments) {
        super(arguments);
    }

    /**
     * initializes the parameters
     *
     * @param path the path of the project
     * @param kernelType the type of kernel to use
     * @param c the complexity parameter C
     */
    public SVM_RFE(String path, SVMKernelType kernelType, double c) {
        super(path, kernelType, c);
    }

    /**
     * generates a classifier (SVM) using input data and based on selected
     * feature subset, and finally returns the weights of features based on
     * ranking criterion (weight magnitude)
     *
     * @param selectedFeature an array of indices of the selected feature subset
     *
     * @return an array of the weights of features
     */
    private double[] getFeaturesWeights(int[] selectedFeature) {
        double[] featureValues = new double[selectedFeature.length];
        double[] weights = buildSVM_OneAgainstOne(selectedFeature)[0][1];

        for (int j = 0; j < weights.length; j++) {
            featureValues[j] = weights[j] * weights[j];
        }

        return featureValues;
    }

    /**
     * starts the feature selection process by support vector machine method
     * based on recursive feature elimination (SVM_RFE)
     */
    @Override
    public void evaluateFeatures() {
        FileFunc.createDirectory(TEMP_PATH);
        this.createClassLabel();
        int[] indexFeatures = new int[numFeatures];

        //initializes the feature index values
        for (int i = 0; i < indexFeatures.length; i++) {
            indexFeatures[i] = i;
        }
        for (int i = 0; i < numFeatures - numSelectedFeature; i++) {
            System.out.println("\nIteration " + i + ":\n\n");

            ArraysFunc.sortArray1D(indexFeatures, false, 0, numFeatures - i);
            int[] featSpace = Arrays.copyOfRange(indexFeatures, 0, numFeatures - i);

            /**
             * trains SVM classifier, computes weights of features, and find
             * index of a feature with smallest weight
             */
            int minIndex = MathFunc.findMinimumIndex(getFeaturesWeights(featSpace));

            /**
             * changing the place of the feature with the minIndex to the last
             * feature in the indexFeatures array
             */
            MathFunc.swap(indexFeatures, minIndex, numFeatures - i - 1);
        }
        selectedFeatureSubset = Arrays.copyOfRange(indexFeatures, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);

//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
        FileFunc.deleteDirectoryWithAllFiles(TEMP_PATH);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String validate() {
        if (numClass > 2) {
            return "SVM method based on Recursive Feature Elimination (SVM_RFE) cannot be applied to multiclass classification.";
        }
        return "";
    }
}
