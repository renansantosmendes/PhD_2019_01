///*
// * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
// * completely in Java, for performing feature selection process in different
// * areas of research.
// * For more information about KFST, please visit:
// *     http://kfst.uok.ac.ir/index.html
// *
// * Copyright (C) 2016-2018 KFST development team at University of Kurdistan,
// * Sanandaj, Iran.
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//package KFST.featureSelection.embedded.TreeBasedMethods;
//
//import KFST.gui.featureSelection.embedded.decisionTreeBased.TreeType;
//import KFST.util.ArraysFunc;
//import KFST.util.FileFunc;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.util.Arrays;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import weka.classifiers.trees.RandomForest;
//import weka.core.Instances;
//import weka.core.Utils;
//
///**
// * This java class is used to implement the random forest based method for
// * feature selection.
// *
// * @author Sina Tabakhi
// * @see KFST.featureSelection.embedded.TreeBasedMethods
// * @see KFST.featureSelection.embedded.EmbeddedApproach
// * @see KFST.featureSelection.FeatureSelection
// */
//public class RandomForestMethod extends TreeBasedMethods {
//
//    private int randomForestNumFeatures;
//    private int randomForestMaxDepth;
//    private int randomForestNumIterations;
//
//    /**
//     * initializes the parameters
//     *
//     * @param arguments array of parameter contains (<code>path</code>,
//     * <code>tree type</code>, <code>NumFeatures</code>, <code>MaxDepth</code>,
//     * <code>NumIterations</code>) in which <code><b><i>path</i></b></code> is
//     * the path of the project, <code><b><i>tree type</i></b></code> is the type
//     * of tree, <code><b><i>NumFeatures</i></b></code> is the number of randomly
//     * selected features, <code><b><i>MaxDepth</i></b></code> is the maximum
//     * depth of the tree, <code><b><i>NumIterations</i></b></code> is the number
//     * of iterations to be performed
//     */
//    public RandomForestMethod(Object... arguments) {
//        super(arguments);
//        randomForestNumFeatures = (int) arguments[2];
//        randomForestMaxDepth = (int) arguments[3];
//        randomForestNumIterations = (int) arguments[4];
//    }
//
//    /**
//     * initializes the parameters
//     *
//     * @param path the path of the project
//     * @param randomForestNumFeatures the number of randomly selected features
//     * @param randomForestMaxDepth the maximum depth of the tree
//     * @param randomForestNumIterations the number of iterations to be performed
//     */
//    public RandomForestMethod(String path, int randomForestNumFeatures, int randomForestMaxDepth, int randomForestNumIterations) {
//        super(path, TreeType.RANDOM_FOREST);
//        this.randomForestNumFeatures = randomForestNumFeatures;
//        this.randomForestMaxDepth = randomForestMaxDepth;
//        this.randomForestNumIterations = randomForestNumIterations;
//    }
//
//    /**
//     * find the feature subset from the nodes of the created tree (Used for
//     * Random Forest)
//     *
//     * @param tree the generated tree based on the train set
//     */
//    @Override
//    protected void selectedFeatureSubset(String tree) {
//        String[] lines = tree.split(" ");
//        int[] featureIndices = new int[numFeatures];
//
//        for (int i = 0; i < featureIndices.length; i++) {
//            featureIndices[i] = Integer.parseInt(lines[i]);
////            System.out.println("" + featureIndices[i]);
//        }
//
//        selectedFeatureSubset = Arrays.copyOfRange(featureIndices, 0, numSelectedFeature);
//        ArraysFunc.sortArray1D(selectedFeatureSubset, false);
//
////        for (int i = 0; i < numSelectedFeature; i++) {
////            System.out.println("ranked  = " + selectedFeatureSubset[i]);
////        }
//    }
//
//    /**
//     * {@inheritDoc }
//     */
//    @Override
//    protected String buildClassifier(Instances dataTrain) {
//        try {
//            RandomForest decisionTreeRandomForest = new RandomForest();
//            decisionTreeRandomForest.setNumFeatures(randomForestNumFeatures);
//            decisionTreeRandomForest.setMaxDepth(randomForestMaxDepth);
//            decisionTreeRandomForest.setNumIterations(randomForestNumIterations);
//            decisionTreeRandomForest.setComputeAttributeImportance(true);
//            decisionTreeRandomForest.buildClassifier(dataTrain);
//
//            /**
//             * Creating an array of indices of the features based on descending
//             * order of features' importance
//             */
//            double[] nodeCounts = new double[numFeatures + 1];
//            double[] impurityScores = decisionTreeRandomForest.computeAverageImpurityDecreasePerAttribute(nodeCounts);
//            int[] sortedIndices = Utils.sort(impurityScores);
//            String sortedIndicesToString = "";
//            for (int i = sortedIndices.length - 1; i >= 0; i--) {
//                if (sortedIndices[i] != numFeatures) {
//                    sortedIndicesToString += String.valueOf(sortedIndices[i]) + " ";
//                }
//            }
//
//            return sortedIndicesToString.trim();
//            //return decisionTreeRandomForest.toString();
//        } catch (Exception ex) {
//            Logger.getLogger(RandomForestMethod.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "";
//    }
//
//    /**
//     * starts the feature selection process by Random Forest based method
//     */
//    @Override
//    public void evaluateFeatures() {
//        FileFunc.createDirectory(TEMP_PATH);
//        String nameDataCSV = TEMP_PATH + "dataCSV.csv";
//        String nameDataARFF = TEMP_PATH + "dataARFF.arff";
//
//        FileFunc.createCSVFile(trainSet, originalFeatureSet(), nameDataCSV, nameFeatures, classLabel);
//        FileFunc.convertCSVtoARFF(nameDataCSV, nameDataARFF, TEMP_PATH, numFeatures, numFeatures, nameFeatures, numClass, classLabel);
//
//        try {
//            BufferedReader readerTrain = new BufferedReader(new FileReader(nameDataARFF));
//            Instances dataTrain = new Instances(readerTrain);
//            readerTrain.close();
//            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
//
//            selectedFeatureSubset(buildClassifier(dataTrain));
//        } catch (Exception ex) {
//            Logger.getLogger(RandomForestMethod.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        FileFunc.deleteDirectoryWithAllFiles(TEMP_PATH);
//    }
//}
