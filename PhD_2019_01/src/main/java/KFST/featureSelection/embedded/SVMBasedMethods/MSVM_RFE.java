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

import KFST.classifier.WekaSVMKernel;
import KFST.gui.classifier.svmClassifier.SVMKernelType;
import KFST.util.ArraysFunc;
import KFST.util.FileFunc;
import KFST.util.MathFunc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

/**
 * This java class is used to implement MSVM_RFE method for binary
 * classification based on SVM_RFE method (support vector machine method based
 * on recursive feature elimination) in which multiple linear SVMs trained on
 * subsamples of the original training data. K-fold cross validation is used as
 * the resampling method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.embedded.SVMBasedMethods
 * @see KFST.featureSelection.embedded.EmbeddedApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class MSVM_RFE extends SVMBasedMethods {

    private final double ERROR_DENOMINATOR = 1.0;
    private int kFoldValue;
    private int numRun;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains (<code>path</code>,
     * <code>kernelType</code>, <code>Parameter c</code>,
     * <code>kFoldValue</code>, <code>numRun</code>) in which
     * <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>kernelType</i></b></code> is the type of kernel to use,
     * <code><b><i>Parameter c</i></b></code> is the complexity parameter C,
     * <code><b><i>kFoldValue</i></b></code> is the number of subsamples in
     * k-fold cross validation, and <code><b><i>numRun</i></b></code> is the
     * number of multiple runs of k-fold CV
     */
    public MSVM_RFE(Object... arguments) {
        super(arguments);
        kFoldValue = (int) arguments[3];
        numRun = (int) arguments[4];
    }

    /**
     * initializes the parameters
     *
     * @param path the path of the project
     * @param kernelType the type of kernel to use
     * @param c the complexity parameter C
     * @param kFoldValue the number of subsamples in k-fold cross validation
     * @param numRun the number of multiple runs of k-fold CV
     */
    public MSVM_RFE(String path, SVMKernelType kernelType, double c, int kFoldValue, int numRun) {
        super(path, kernelType, c);
        this.kFoldValue = kFoldValue;
        this.numRun = numRun;
    }

    /**
     * generates binary classifiers (SVM by applying k-fold cross validation
     * resampling strategy) using input data and based on selected feature
     * subset.
     *
     * @param selectedFeature an array of indices of the selected feature subset
     *
     * @return an array of the weights of features
     */
    protected double[][] buildSVM_KFoldCrossValidation(int[] selectedFeature) {
        double[][] weights = new double[numRun * kFoldValue][selectedFeature.length];
        int classifier = 0;

        for (int i = 0; i < numRun; i++) {
            double[][] copyTrainSet = ArraysFunc.copyDoubleArray2D(trainSet);

            //shuffles the train set
            MathFunc.randomize(copyTrainSet);

            int numSampleInFold = copyTrainSet.length / kFoldValue;
            int remainder = copyTrainSet.length % kFoldValue;
            int indexStart = 0;
            for (int k = 0; k < kFoldValue; k++) {
                int indexEnd = indexStart + numSampleInFold;
                if (k < remainder) {
                    indexEnd++;
                }
                double[][] subTrainSet = ArraysFunc.copyDoubleArray2D(copyTrainSet, indexStart, indexEnd);

                String nameDataCSV = TEMP_PATH + "dataCSV[" + i + "-" + k + "].csv";
                String nameDataARFF = TEMP_PATH + "dataARFF[" + i + "-" + k + "].arff";

                FileFunc.createCSVFile(subTrainSet, selectedFeature, nameDataCSV, nameFeatures, classLabel);
                FileFunc.convertCSVtoARFF(nameDataCSV, nameDataARFF, TEMP_PATH, selectedFeature.length, numFeatures, nameFeatures, numClass, classLabel);

                try {
                    BufferedReader readerTrain = new BufferedReader(new FileReader(nameDataARFF));
                    Instances dataTrain = new Instances(readerTrain);
                    readerTrain.close();
                    dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

                    SMO svm = new SMO();
                    svm.setC(parameterC);
                    svm.setKernel(WekaSVMKernel.parse(kernelType));
                    svm.buildClassifier(dataTrain);

                    double[] weightsSparse = svm.sparseWeights()[0][1];
                    int[] indicesSparse = svm.sparseIndices()[0][1];
                    for (int m = 0; m < weightsSparse.length; m++) {
                        weights[classifier][indicesSparse[m]] = weightsSparse[m];
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MSVM_RFE.class.getName()).log(Level.SEVERE, null, ex);
                }

                indexStart = indexEnd;
                classifier++;
            }
        }

        return weights;
    }

    /**
     * generates binary classifiers (SVM by applying k-fold cross validation
     * resampling strategy) using input data and based on selected feature
     * subset, and finally returns the weights of features
     *
     * @param selectedFeature an array of indices of the selected feature subset
     *
     * @return an array of the weights of features
     */
    private double[] getFeaturesWeights(int[] selectedFeature) {
        double[] featureValues = new double[selectedFeature.length];
        double[] averageValues = new double[selectedFeature.length];
        double[] standardDeviationValues = new double[selectedFeature.length];
        double[][] weights = buildSVM_KFoldCrossValidation(selectedFeature);

        //Normalize weight vector
        for (double[] weight : weights) {
            MathFunc.normalizeVector(weight);
        }

        for (double[] weight : weights) {
            for (int j = 0; j < weight.length; j++) {
                weight[j] *= weight[j];
            }
        }

        //Compute average values of features
        for (int j = 0; j < selectedFeature.length; j++) {
            averageValues[j] = MathFunc.computeMean(weights, j);
        }

        //Compute standard deviation values of features
        for (int j = 0; j < selectedFeature.length; j++) {
            standardDeviationValues[j] = MathFunc.computeStandardDeviation(weights, averageValues[j], j, weights.length - 1.0);
        }

        //Compute scores of features
        for (int j = 0; j < selectedFeature.length; j++) {
            featureValues[j] = averageValues[j] / (standardDeviationValues[j] + ERROR_DENOMINATOR);
        }

        return featureValues;
    }

    /**
     * starts the feature selection process by multiple support vector machine
     * method based on recursive feature elimination using k-fold cross
     * validation resampling strategy (MSVM_RFE)
     */
    @Override
    public void evaluateFeatures() {
        FileFunc.createDirectory(TEMP_PATH);
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
             * generates SVM classifiers, computes weights of features, and find
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
            return "Multiple SVM method based on Recursive Feature Elimination "
                    + "(MSVM_RFE) cannot be applied to multiclass classification.";
        }
        if (kFoldValue > trainSet.length) {
            return "The parameter values of MSVM_RFE (number of folds) are incorred.";
        }
        return "";
    }
}
