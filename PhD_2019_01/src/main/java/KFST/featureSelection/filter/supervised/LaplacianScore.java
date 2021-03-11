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
 * This java class is used to implement the Laplacian score method. Also, it is
 * the supervised version of the Laplacian score.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.WeightedFilterApproach
 * @see KFST.featureSelection.FeatureWeighting
 * @see KFST.featureSelection.FeatureSelection
 */
public class LaplacianScore extends WeightedFilterApproach {

    private final double CONSTANT_VALUE;
    private final double ERROR_DENOMINATOR = 0.0001;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains 
     * (<code>sizeSelectedFeatureSubset</code>, <code>constant</code>) in which 
     * <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number of 
     * selected features, and <code><b><i>kNNValue</i></b></code> is the 
     * k-nearest neighbor value
     */
    public LaplacianScore(Object... arguments) {
        super((int)arguments[0]);
        CONSTANT_VALUE = (double)arguments[1];
    }
    
    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     * @param constant the constant value used in the similarity measure
     */
    public LaplacianScore(int sizeSelectedFeatureSubset, double constant) {
        super(sizeSelectedFeatureSubset);
        CONSTANT_VALUE = constant;
    }

    /**
     * construct nearest neighbor graph (G) of the data space
     * 
     * @return the nearest neighbor graph
     */
    private boolean[][] constructNeighborGraph() {
        boolean[][] tempMatrix = new boolean[trainSet.length][trainSet.length];

        for (int i = 0; i < trainSet.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (trainSet[i][numFeatures] == trainSet[j][numFeatures]) {
                    tempMatrix[i][j] = tempMatrix[j][i] = true;
                } else {
                    tempMatrix[i][j] = tempMatrix[j][i] = false;
                }
            }
        }
        return tempMatrix;
    }

    /**
     * construct weight matrix(S) which models the local structure of the data space
     *
     * @param neighborGraph the nearest neighbor graph (G)
     * 
     * @return the weight matrix
     */
    private double[][] constructWeightMatrix(boolean[][] neighborGraph) {
        double[][] tempMatrix = new double[trainSet.length][trainSet.length];

        for (int i = 0; i < trainSet.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (neighborGraph[i][j] == true) {
                    // computes euclidean distance value between data i-th and j-th
                    double euclideanDistance = 0.0;
                    for (int k = 0; k < numFeatures; k++) {
                        euclideanDistance += Math.pow(trainSet[i][k] - trainSet[j][k], 2);
                    }
                    tempMatrix[i][j] = tempMatrix[j][i] = Math.pow(Math.E, -(euclideanDistance / CONSTANT_VALUE));
                } else {
                    tempMatrix[i][j] = tempMatrix[j][i] = 0;
                }
            }
        }
        return tempMatrix;
    }

    /**
     * construct the diagonal matrix (D) based on the weight matrix
     *
     * @param simMatrix the weight matrix(S)
     * 
     * @return the diagonal matrix
     */
    private double[][] constructDiagonalMatrix(double[][] simMatrix) {
        double[][] tempMatrix = new double[trainSet.length][trainSet.length];

        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix.length; j++) {
                tempMatrix[i][i] += simMatrix[i][j];
            }
        }
        return tempMatrix;
    }

    /**
     * estimates each feature values
     *
     * @param diag the diagonal matrix(D)
     * @param index the index of the feature
     * 
     * @return the estimation of the features
     */
    private double[][] estimateFeatureMatrix(double[][] diag, int index) {
        double numeratorValue = 0;
        double denominatorValue = 0;
        double fractionResult;
        double[][] estFeature = new double[trainSet.length][1];

        //f(index) * diagonal matrix(D) * identity matrix(1)
        for (int i = 0; i < trainSet.length; i++) {
            numeratorValue += trainSet[i][index] * diag[i][i];
        }

        //transpose identity matrix(1) * diagonal matrix(D) * identity matrix(1)
        for (int i = 0; i < diag.length; i++) {
            denominatorValue += diag[i][i];
        }
        if (denominatorValue == 0) {
            fractionResult = numeratorValue / ERROR_DENOMINATOR;
        } else {
            fractionResult = numeratorValue / denominatorValue;
        }

        for (int i = 0; i < trainSet.length; i++) {
            estFeature[i][0] = trainSet[i][index] - fractionResult;
        }

        return estFeature;
    }

    /**
     * checks the values of data corresponding to a given feature
     *
     * @param index the index of the feature
     * 
     * @return true if the all values is equal to zero
     */
    private boolean isZeroFeat(int index) {
        for (double[] sample : trainSet) {
            if (sample[index] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * chaining multiple three matrix
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @param matrix3 the third matrix
     * 
     * @return the result of the chaining multiple
     */
    private double multThreeMatrix(double[][] matrix1, double[][] matrix2, double[][] matrix3) {
        double[][] result = MathFunc.multMatrix(MathFunc.multMatrix(matrix1, matrix2), matrix3);
        return result[0][0];
    }

    /**
     * starts the feature selection process by Laplacian score(LS) method
     */
    @Override
    public void evaluateFeatures() {
        boolean[][] nearestNeighborGraph; // nearest neighbor graph
        double[][] weightMatrix; // weight matrix of the data space
        double[][] identityMatrix = new double[trainSet.length][1]; // identity matrix
        double[][] diagonalMatrix; // diagonal matrix
        double[][] graphLaplacian; // graph Laplacian
        featureValues = new double[numFeatures];
        int[] indecesLS;

        //constructs an identity matrix
        for (double[] row : identityMatrix) {
            row[0] = 1;
        }

        //constructs nearest neighbor graph
        nearestNeighborGraph = constructNeighborGraph();

        //constructs weight matrix
        weightMatrix = constructWeightMatrix(nearestNeighborGraph);

        //construct diagonal matrix
        diagonalMatrix = constructDiagonalMatrix(weightMatrix);

        //computes the graph Laplacian
        graphLaplacian = MathFunc.subMatrix(diagonalMatrix, weightMatrix);

        //computs the Laplacian score values for the features
        for (int i = 0; i < numFeatures; i++) {
            if (!isZeroFeat(i)) {
                double[][] estimateFeature = estimateFeatureMatrix(diagonalMatrix, i);
                double numeratorValue = multThreeMatrix(MathFunc.transMatrix(estimateFeature), graphLaplacian, estimateFeature);
                double denominatorValue = multThreeMatrix(MathFunc.transMatrix(estimateFeature), diagonalMatrix, estimateFeature);
                if (denominatorValue == 0) {
                    featureValues[i] = numeratorValue / ERROR_DENOMINATOR;
                } else {
                    featureValues[i] = numeratorValue / denominatorValue;
                }
            } else {
                featureValues[i] = 1 / ERROR_DENOMINATOR;
            }
        }

        indecesLS = ArraysFunc.sortWithIndex(Arrays.copyOf(featureValues, featureValues.length), false);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + featureValues[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesLS, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }
}
