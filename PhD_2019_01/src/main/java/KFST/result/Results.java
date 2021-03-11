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
package InformationTheory.KFST.result;

import KFST.classifier.ClassifierType;
import KFST.classifier.evaluation.wekaClassifier.TrainTestEvaluation;
import KFST.dataset.DatasetInfo;
import KFST.gui.classifier.DTClassifierPanel;
import KFST.gui.classifier.KNNClassifierPanel;
import KFST.gui.classifier.svmClassifier.SVMClassifierPanel;
import KFST.result.performanceMeasure.Criteria;
import KFST.result.performanceMeasure.PerformanceMeasures;
import KFST.util.FileFunc;
import KFST.util.MathFunc;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This java class is used to implement various methods for showing the results
 * in the result panel.
 *
 * @author Sina Tabakhi
 */
public class Results {

    DatasetInfo data;
    private String PATH_PROJECT;
    private String PATH_DATA_CSV;
    private String PATH_DATA_ARFF;
    private int numRuns;
    private int numSelectedSubsets;
    private String[][] selectedFeatureSubsets;
    private String featureSelectionMethodName;
    private double currentExecutionTime;
    private int[] currentSelectedSubset;
    private double[] currentFeatureValues;
    private Object selectedEvaluationClassifierPanel;
    private ClassifierType classifierType = ClassifierType.NONE;
    private PerformanceMeasures performanceMeasures;

    /**
     * initializes the parameters
     *
     * @param data the data class contains all information of the input dataset
     * @param numRuns numbers of runs of the feature selection method
     * @param numSelectedSubsets numbers of selected feature subsets
     * @param projectPath the path of the project
     * @param methodName the name of given feature selection method
     * @param classifierName the name of given classifier
     * @param selectedEvaluationClassifierPanel panel of the selected classifier
     * contained the parameter values
     */
    public Results(DatasetInfo data, int numRuns, int numSelectedSubsets, String projectPath,
            Object methodName, Object classifierName, Object selectedEvaluationClassifierPanel) {
        this.data = data;
        this.numRuns = numRuns;
        this.numSelectedSubsets = numSelectedSubsets;
        this.PATH_PROJECT = projectPath;
        PATH_DATA_CSV = PATH_PROJECT + "CSV\\";
        PATH_DATA_ARFF = PATH_PROJECT + "ARFF\\";
        featureSelectionMethodName = methodName.toString();
        this.selectedEvaluationClassifierPanel = selectedEvaluationClassifierPanel;
        this.classifierType = ClassifierType.parse(classifierName.toString());

        selectedFeatureSubsets = new String[this.numRuns][this.numSelectedSubsets];
        performanceMeasures = new PerformanceMeasures(this.numRuns, this.numSelectedSubsets);
    }

    /**
     * This method returns an empty string
     *
     * @return an empty string
     */
    @Override
    public String toString() {
        return toString(ResultType.NONE);
    }

    /**
     * This method converts the obtained results over all runs to the string
     * based on result type for showing in the result panel.
     *
     * @param type type of the result
     *
     * @return a string of the result
     */
    public String toString(ResultType type) {
        if (type == ResultType.DATASET_INFORMATION) {
            return addTextToPanel();
        } else if (type == ResultType.SELECTED_FEATURE_SUBSET) {
            return addTextToPanel(currentSelectedSubset);
        } else if (type == ResultType.FEATURE_VALUES) {
            return addTextToPanel(currentFeatureValues, featureSelectionMethodName);
        } else if (type == ResultType.PERFORMANCE_MEASURES) {
            return addTextToPanel(selectedFeatureSubsets, "Subsets of selected features in each iteration")
                    + addTextToPanel(performanceMeasures.getAccuracyValues(), "Classification accuracies")
                    + addTextToPanel(performanceMeasures.getAverageAccuracyValues(), "Average classification accuracies", true)
                    + addTextToPanel(performanceMeasures.getTimeValues(), "Execution times")
                    + addTextToPanel(performanceMeasures.getAverageTimeValues(), "Average execution times", true);
        }
        return "";
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about some information of the dataset.
     *
     * @return the created message as a string
     *
     * @see KFST.gui.ResultPanel
     * @see KFST.dataset.DatasetInfo
     */
    private String addTextToPanel() {
        String messages = "General infomation:\n";
        messages += "   Path of the workspace: " + PATH_PROJECT + "\n";
        messages += "   Number of samples in the training set: " + data.getNumTrainSet() + "\n";
        messages += "   Number of samples in the test set: " + data.getNumTestSet() + "\n";
        messages += "   Number of features: " + data.getNumFeature() + "\n";
        messages += "   Number of classes: " + data.getNumClass() + "\n";
        messages += "   Name of classes {";
        for (int i = 0; i < data.getNumClass() - 1; i++) {
            messages += data.getClassLabel()[i] + ", ";
        }
        messages += data.getClassLabel()[data.getNumClass() - 1] + "}\n\n";
        messages += "Start feature selection process:\n";
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the subset of selected features.
     *
     * @param array an array of text to insert
     *
     * @return the created message as a string
     *
     * @see KFST.gui.ResultPanel
     */
    private String addTextToPanel(int[] array) {
        String messages = "\n        subset selected {";
        for (int k = 0; k < array.length - 1; k++) {
            messages += String.valueOf(array[k] + 1) + ", ";
        }
        messages += String.valueOf(array[array.length - 1] + 1) + "}\n\n";
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the relevance values of features computed by given
     * feature selection method.
     *
     * @param array an array of text to insert
     * @param nameMethod the name of feature selection method
     *
     * @return the created message as a string
     *
     * @see KFST.gui.ResultPanel
     */
    private String addTextToPanel(double[] array, String nameMethod) {
        String messages = "";
        for (int k = 0; k < array.length; k++) {
            messages += "        " + nameMethod + "(" + data.getNameFeatures()[k] + ") = " + MathFunc.roundDouble(array[k], 4) + "\n";
        }
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the final subsets of selected features.
     *
     * @param array an array of text to insert
     * @param title the name of results
     *
     * @return the created message as a string
     *
     * @see KFST.gui.ResultPanel
     */
    private String addTextToPanel(String[][] array, String title) {
        String messages = "\n\n" + title + ":\n";
        for (int i = 0; i < array.length; i++) {
            messages += "     Iteration (" + String.valueOf(i + 1) + "):\n";
            for (int j = 0; j < array[0].length; j++) {
                messages += "          " + String.valueOf(array[i][j]) + "\n";
            }
            messages += "\n";
        }
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the classification accuracy and execution time in each
     * iteration.
     *
     * @param array an array of text to insert
     * @param title the name of results
     *
     * @return the created message as a string
     *
     * @see KFST.gui.ResultPanel
     */
    private String addTextToPanel(double[][] array, String title) {
        String messages = "\n\n" + title + ":\n";
        for (int i = 0; i < array.length; i++) {
            messages += "     Iteration (" + String.valueOf(i + 1) + "):";
            for (int j = 0; j < array[0].length; j++) {
                messages += "  " + MathFunc.roundDouble(array[i][j], 3);
            }
            messages += "\n";
        }
        return messages;
    }

    /**
     * creates the given text for showing in the results panel.<br>
     * The text is about the average classification accuracies and average
     * execution times in all iteration.
     *
     * @param array an array of text to insert
     * @param title the name of results
     * @param isAverage shows that the average values must be displayed
     *
     * @return the created message as a string
     *
     * @see KFST.gui.ResultPanel
     */
    private String addTextToPanel(double[][] array, String title, boolean isAverage) {
        String messages = "\n\n" + title + ":\n";
        messages += "     All Iterations:  ";
        for (int j = 0; j < array[0].length; j++) {
            messages += MathFunc.roundDouble(array[0][j], 3) + "  ";
        }
        messages += "\n";
        return messages;
    }

    /**
     * This method creates a text file of the subsets of selected features
     */
    public void createFeatureFiles() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(PATH_PROJECT + "FeatureSubsetsFile.txt"));
            pw.println("Subsets of selected features in each iteration:\n");
            for (int i = 0; i < selectedFeatureSubsets.length; i++) {
                pw.println("     Iteration (" + (i + 1) + "):");
                for (int j = 0; j < selectedFeatureSubsets[0].length; j++) {
                    pw.println("          " + selectedFeatureSubsets[i][j]);
                }
                pw.println();
            }
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(Results.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the execution time of the feature selection method in the current
     * run.
     *
     * @param value execution time of the feature selection method
     */
    public void setTime(double value) {
        currentExecutionTime = value;
    }

    /**
     * Sets the subset of selected features in the current run
     *
     * @param i the current run of the feature selection method
     * @param j the current subset of the selected features
     * @param currentSelectedSubset an array of the indices of the selected
     * features
     */
    public void setCurrentSelectedSubset(int i, int j, int[] currentSelectedSubset) {
        this.currentSelectedSubset = currentSelectedSubset;
        selectedFeatureSubsets[i][j] = data.createFeatNames(this.currentSelectedSubset);
    }

    /**
     * Sets the values of each feature in the current run over given subset of
     * selected features
     *
     * @param currentFeatureValues an array of the values of each feature
     */
    public void setCurrentFeatureValues(double[] currentFeatureValues) {
        this.currentFeatureValues = currentFeatureValues;
    }

    /**
     * Computes the performance measures based on given classifier
     *
     * @param i the current run of the feature selection method
     * @param j the current subset of the selected features
     */
    public void computePerformanceMeasures(int i, int j) {
        Criteria critria = new Criteria();
        String nameTrainDataCSV = PATH_DATA_CSV + "trainSet[" + (i + 1) + "-" + currentSelectedSubset.length + "].csv";
        String nameTrainDataARFF = PATH_DATA_ARFF + "trainSet[" + (i + 1) + "-" + currentSelectedSubset.length + "].arff";
        String nameTestDataCSV = PATH_DATA_CSV + "testSet[" + (i + 1) + "-" + currentSelectedSubset.length + "].csv";
        String nameTestDataARFF = PATH_DATA_ARFF + "testSet[" + (i + 1) + "-" + currentSelectedSubset.length + "].arff";

        FileFunc.createCSVFile(data.getTrainSet(), currentSelectedSubset, nameTrainDataCSV, data.getNameFeatures(), data.getClassLabel());
        FileFunc.createCSVFile(data.getTestSet(), currentSelectedSubset, nameTestDataCSV, data.getNameFeatures(), data.getClassLabel());
        FileFunc.convertCSVtoARFF(nameTrainDataCSV, nameTrainDataARFF, PATH_PROJECT, currentSelectedSubset.length, data);
        FileFunc.convertCSVtoARFF(nameTestDataCSV, nameTestDataARFF, PATH_PROJECT, currentSelectedSubset.length, data);

        if (classifierType == ClassifierType.SVM) {
            SVMClassifierPanel svmPanel = (SVMClassifierPanel) selectedEvaluationClassifierPanel;
            critria = TrainTestEvaluation.SVM(nameTrainDataARFF, nameTestDataARFF, svmPanel.getKernel(), svmPanel.getParameterC());
        } else if (classifierType == ClassifierType.NB) {
            critria = TrainTestEvaluation.naiveBayes(nameTrainDataARFF, nameTestDataARFF);
        } else if (classifierType == ClassifierType.DT) {
            DTClassifierPanel dtPanel = (DTClassifierPanel) selectedEvaluationClassifierPanel;
            critria = TrainTestEvaluation.dTree(nameTrainDataARFF, nameTestDataARFF, dtPanel.getConfidence(), dtPanel.getMinNum());
        } else if (classifierType == ClassifierType.KNN) {
            KNNClassifierPanel dtPanel = (KNNClassifierPanel) selectedEvaluationClassifierPanel;
            critria = TrainTestEvaluation.kNN(nameTrainDataARFF, nameTestDataARFF, dtPanel.getKNNValue());
        }

        critria.setTime(currentExecutionTime);
        performanceMeasures.setCriteria(i, j, critria);
    }

    /**
     * Computes the average values of all criteria over number of runs
     */
    public void computeAverageValuesOfPerformanceMeasures() {
        performanceMeasures.computeAverageValues();
    }

    /**
     * Returns the performance measures of the obtained result
     *
     * @return measures of the PerformanceMeasures class
     */
    public PerformanceMeasures getPerformanceMeasures() {
        return performanceMeasures;
    }
}
