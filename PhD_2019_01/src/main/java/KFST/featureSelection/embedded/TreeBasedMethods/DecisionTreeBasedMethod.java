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
package KFST.featureSelection.embedded.TreeBasedMethods;

import KFST.gui.featureSelection.embedded.decisionTreeBased.TreeType;
import KFST.util.ArraysFunc;
import KFST.util.FileFunc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;

/**
 * This java class is used to implement the decision tree based methods.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.embedded.TreeBasedMethods
 * @see KFST.featureSelection.embedded.EmbeddedApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class DecisionTreeBasedMethod extends TreeBasedMethods {

    private double confidenceValue;
    private int minNumSampleInLeaf;
    private int randomTreeKValue;
    private int randomTreeMaxDepth;
    private double randomTreeMinNum;
    private double randomTreeMinVarianceProp;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameter contains
     * <p>
     * if the type of tree is C4.5 (<code>path</code>, <code>tree type</code>,
     * <code>confidenceValue</code>, <code>minNumSampleInLeaf</code>) in which
     * <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>tree type</i></b></code> is the type of tree,
     * <code><b><i>confidenceValue</i></b></code> is the confidence factor used
     * for pruning, <code><b><i>minNumSampleInLeaf</i></b></code> is the minimum
     * number of samples per leaf
     *
     * <p>
     * if the type of tree is random tree (<code>path</code>,
     * <code>tree type</code>, <code>KValue</code>, <code>MaxDepth</code>,
     * <code>MinNum,</code>, <code>MinVarianceProp</code>) in which
     * <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>tree type</i></b></code> is the type of tree,
     * <code><b><i>KValue</i></b></code> is the number of randomly chosen
     * attributes, <code><b><i>MaxDepth</i></b></code> is the maximum depth of
     * the tree, <code><b><i>MinNum</i></b></code> is the minimum total weight
     * of the instances in a leaf, <code><b><i>MinVarianceProp</i></b></code> is
     * the minimum proportion of the total variance
     */
    public DecisionTreeBasedMethod(Object... arguments) {
        super(arguments);
        if (TREE_TYPE == TreeType.C45) {
            confidenceValue = (double) arguments[2];
            minNumSampleInLeaf = (int) arguments[3];
        } else if (TREE_TYPE == TreeType.RANDOM_TREE) {
            randomTreeKValue = (int) arguments[2];
            randomTreeMaxDepth = (int) arguments[3];
            randomTreeMinNum = (double) arguments[4];
            randomTreeMinVarianceProp = (double) arguments[5];
        }
    }

    /**
     * initializes the parameters
     *
     * @param path the path of the project
     * @param confidence the confidence factor used for pruning
     * @param minNum the minimum number of samples per leaf
     */
    public DecisionTreeBasedMethod(String path, double confidence, int minNum) {
        super(path, TreeType.C45);
        this.confidenceValue = confidence;
        this.minNumSampleInLeaf = minNum;
    }

    /**
     * initializes the parameters
     *
     * @param path the path of the project
     * @param randomTreeKValue the number of randomly chosen attributes
     * @param randomTreeMaxDepth the maximum depth of the tree
     * @param randomTreeMinNum the minimum total weight of the instances in a
     * leaf
     * @param randomTreeMinVarianceProp the minimum proportion of the total
     * variance (over all the data) required for split
     */
    public DecisionTreeBasedMethod(String path, int randomTreeKValue, int randomTreeMaxDepth,
            double randomTreeMinNum, double randomTreeMinVarianceProp) {
        super(path, TreeType.RANDOM_TREE);
        this.randomTreeKValue = randomTreeKValue;
        this.randomTreeMaxDepth = randomTreeMaxDepth;
        this.randomTreeMinNum = randomTreeMinNum;
        this.randomTreeMinVarianceProp = randomTreeMinVarianceProp;
    }

    /**
     * find the feature subset from the nodes of the created tree (Used for C4.5
     * and Random Tree)
     *
     * @param tree the generated tree based on the train set
     */
    @Override
    protected void selectedFeatureSubset(String tree) {
        String[] lines = tree.split("\n");
        ArrayList<Integer> featureSubset = new ArrayList<>();

        for (String line : lines) {
            line = line.replace("|   ", " ").trim();
            if (line.lastIndexOf(" <= ") != -1) {
                line = line.substring(0, line.lastIndexOf(" <= "));
            } else if (line.lastIndexOf(" >= ") != -1) {
                line = line.substring(0, line.lastIndexOf(" >= "));
            } else if (line.lastIndexOf(" = ") != -1) {
                line = line.substring(0, line.lastIndexOf(" = "));
            } else if (line.lastIndexOf(" > ") != -1) {
                line = line.substring(0, line.lastIndexOf(" > "));
            } else if (line.lastIndexOf(" < ") != -1) {
                line = line.substring(0, line.lastIndexOf(" < "));
            } else {
                line = "";
            }

            line = line.trim();

            if (line.length() != 0) {
                int index = Arrays.asList(nameFeatures).indexOf(line);
                if (!featureSubset.contains(index)) {
                    featureSubset.add(index);
                }
            }
        }

        this.setNumSelectedFeature(featureSubset.size());

        for (int i = 0; i < numSelectedFeature; i++) {
            selectedFeatureSubset[i] = featureSubset.get(i);
        }

        ArraysFunc.sortArray1D(selectedFeatureSubset, false);

//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected String buildClassifier(Instances dataTrain) {
        try {
            if (TREE_TYPE == TreeType.C45) {
                J48 decisionTreeC45 = new J48();
                decisionTreeC45.setConfidenceFactor((float) confidenceValue);
                decisionTreeC45.setMinNumObj(minNumSampleInLeaf);
                decisionTreeC45.buildClassifier(dataTrain);
                return decisionTreeC45.toString();
            } else if (TREE_TYPE == TreeType.RANDOM_TREE) {
                RandomTree decisionTreeRandomTree = new RandomTree();
                decisionTreeRandomTree.setKValue(randomTreeKValue);
                decisionTreeRandomTree.setMaxDepth(randomTreeMaxDepth);
                decisionTreeRandomTree.setMinNum(randomTreeMinNum);
                decisionTreeRandomTree.setMinVarianceProp(randomTreeMinVarianceProp);
                decisionTreeRandomTree.buildClassifier(dataTrain);
                return decisionTreeRandomTree.toString();
            }
        } catch (Exception ex) {
            Logger.getLogger(DecisionTreeBasedMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * starts the feature selection process by Decision Tree based methods
     */
    @Override
    public void evaluateFeatures() {
        FileFunc.createDirectory(TEMP_PATH);
        String nameDataCSV = TEMP_PATH + "dataCSV.csv";
        String nameDataARFF = TEMP_PATH + "dataARFF.arff";

        FileFunc.createCSVFile(trainSet, originalFeatureSet(), nameDataCSV, nameFeatures, classLabel);
        FileFunc.convertCSVtoARFF(nameDataCSV, nameDataARFF, TEMP_PATH, numFeatures, numFeatures, nameFeatures, numClass, classLabel);

        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(nameDataARFF));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            selectedFeatureSubset(buildClassifier(dataTrain));
            //System.out.println(buildClassifier(dataTrain));
        } catch (Exception ex) {
            Logger.getLogger(DecisionTreeBasedMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileFunc.deleteDirectoryWithAllFiles(TEMP_PATH);
    }
}
