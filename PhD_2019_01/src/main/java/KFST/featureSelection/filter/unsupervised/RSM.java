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
import java.util.Arrays;
import java.util.Random;
import KFST.featureSelection.filter.FilterApproach;
import KFST.gui.featureSelection.filter.rsm.MultivariateMethodType;

/**
 * This java class is used to implement the random subspace method(RSM) method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.FilterApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class RSM extends FilterApproach {

    private final int NUM_ITERATION;
    private final int SIZE_SUB_SPACE;
    private final int THRESHOLD_ELIMINATION;
    private final MultivariateMethodType NAME_MULTI_APPROACH;
    private int[] featureScore;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains
     * (<code>sizeSelectedFeatureSubset</code>, <code>numIter</code>,
     * <code>size</code>, <code>threshold</code>, <code>nameApproach</code>) in
     * which <code><b><i>sizeSelectedFeatureSubset</i></b></code> is the number
     * of selected features, <code><b><i>numIter</i></b></code> is the number of
     * iteration in the RSM method, <code><b><i>size</i></b></code> is the size
     * of the subspace, <code><b><i>threshold</i></b></code> is the number of
     * selected features in each subspace, and
     * <code><b><i>nameApproach</i></b></code> is the name of the multivariate
     * approach used in the RSM
     */
    public RSM(Object... arguments) {
        super((int) arguments[0]);
        NUM_ITERATION = (int) arguments[1];
        SIZE_SUB_SPACE = (int) arguments[2];
        THRESHOLD_ELIMINATION = (int) arguments[3];
        NAME_MULTI_APPROACH = (MultivariateMethodType) arguments[4];
    }

    /**
     * initializes the parameters
     *
     * @param sizeSelectedFeatureSubset the number of selected features
     * @param numIter the number of iteration in the RSM method
     * @param size the size of the subspace
     * @param threshold the number of selected features in each subspace
     * @param nameApproach the name of the multivariate approach used in the RSM
     */
    public RSM(int sizeSelectedFeatureSubset, int numIter, int size, int threshold, MultivariateMethodType nameApproach) {
        super(sizeSelectedFeatureSubset);
        NUM_ITERATION = numIter;
        SIZE_SUB_SPACE = size;
        THRESHOLD_ELIMINATION = threshold;
        NAME_MULTI_APPROACH = nameApproach;
    }

    /**
     * permutes the index of features
     *
     * @param indexFeat the array of the index of features
     * @param seed determines the index of the seed
     */
    private void permutation(int[] indexFeat, int seed) {
        Random rand = new Random(seed);
        for (int i = 0; i < indexFeat.length; i++) {
            int index1 = rand.nextInt(indexFeat.length);
            int index2 = rand.nextInt(indexFeat.length);

            //swap the two values of the indexFeat array
            int temp = indexFeat[index1];
            indexFeat[index1] = indexFeat[index2];
            indexFeat[index2] = temp;
        }
    }

    /**
     * selects the top feature with size THRESHOLD_ELIMINATION by given method
     *
     * @param data the new dataset
     *
     * @return the top feature with size THRESHOLD_ELIMINATION
     */
    private int[] multivariateApproach(double[][] data) {
        int[] resultSelectedFeature;
        if (NAME_MULTI_APPROACH == MultivariateMethodType.MUTUAL_CORRELATION) {
            MutualCorrelation mc = new MutualCorrelation(THRESHOLD_ELIMINATION);
            mc.loadDataSet(data, SIZE_SUB_SPACE, numClass);
            mc.evaluateFeatures();
            resultSelectedFeature = mc.getSelectedFeatureSubset();
        } else {
            resultSelectedFeature = new int[THRESHOLD_ELIMINATION];
        }
        return resultSelectedFeature;
    }

    /**
     * creates a new dataset based on the given indeces of the features
     *
     * @param index an array of the indeces of features
     *
     * @return a new dataset
     */
    private double[][] createNewDataset(int[] index) {
        double[][] newData = new double[trainSet.length][SIZE_SUB_SPACE + 1];

        for (int i = 0; i < trainSet.length; i++) {
            for (int j = 0; j < SIZE_SUB_SPACE; j++) {
                newData[i][j] = trainSet[i][index[j]];
            }
            newData[i][SIZE_SUB_SPACE] = trainSet[i][numFeatures];
        }

        return newData;
    }

    /**
     * starts the feature selection process by random subspace method(RSM)
     */
    @Override
    public void evaluateFeatures() {
        featureScore = new int[numFeatures];
        int[] indexFeatures = new int[numFeatures];
        int[] indecesFeatScore;

        //initializes the feature index values
        for (int i = 0; i < indexFeatures.length; i++) {
            indexFeatures[i] = i;
        }

        for (int i = 0; i < NUM_ITERATION; i++) {
//            System.out.println("\nIteration " + i + ":\n\n");
            permutation(indexFeatures, i);

            int[] featSpace = Arrays.copyOfRange(indexFeatures, 0, SIZE_SUB_SPACE);
            ArraysFunc.sortArray1D(featSpace, false);

            //creates a new dataset based on featSpace array
            double[][] newDataset = createNewDataset(featSpace);

            //selects the top feature with size THRESHOLD_ELIMINATION by given method
            int[] featSelected = multivariateApproach(newDataset);

            //updates the score of the feature selected by mutual correlation
            for (int j = 0; j < THRESHOLD_ELIMINATION; j++) {
                featureScore[featSpace[featSelected[j]]]++;
            }
        }

        indecesFeatScore = ArraysFunc.sortWithIndex(Arrays.copyOf(featureScore, featureScore.length), true);
//        for (int i = 0; i < numFeatures; i++) {
//            System.out.println(i + ") =  " + featureScore[i]);
//        }

        selectedFeatureSubset = Arrays.copyOfRange(indecesFeatScore, 0, numSelectedFeature);
        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate() {
        if (SIZE_SUB_SPACE > numFeatures || THRESHOLD_ELIMINATION > SIZE_SUB_SPACE) {
            return "The parameter values of RSM (size of subspace or elimination threshold) are incorred.";
        }
        return "";
    }
}
