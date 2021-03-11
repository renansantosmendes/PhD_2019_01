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
package KFST.featureSelection;

import KFST.classifier.ClassifierType;
import KFST.classifier.evaluation.wekaClassifier.CrossValidation;
import KFST.gui.classifier.DTClassifierPanel;
import KFST.gui.classifier.KNNClassifierPanel;
import KFST.gui.classifier.svmClassifier.SVMClassifierPanel;
import KFST.result.performanceMeasure.Criteria;
import KFST.util.FileFunc;

/**
 * This java class is used to implement fitness evaluator of a solution in which
 * k-fold cross validation on training set is used for evaluating the
 * classification performance of a selected feature subset.
 *
 * @author Sina Tabakhi
 * @see KFST.classifier.evaluation.wekaClassifier.CrossValidation
 */
public class FitnessEvaluator {

    private final String TEMP_PATH;
    private double[][] trainSet;
    private String[] nameFeatures;
    private String[] classLabel;
    private Object selectedEvaluationClassifierPanel;
    private ClassifierType classifierType = ClassifierType.NONE;
    private int kFolds;

    /**
     * initializes the parameters
     *
     * @param path the temp path in the project
     * @param classifierName the name of given classifier
     * @param selectedEvaluationClassifierPanel panel of the selected classifier
     * contained the parameter values
     * @param kFolds the number of equal sized subsamples that is used in k-fold
     * cross validation
     */
    public FitnessEvaluator(String path, Object classifierName,
            Object selectedEvaluationClassifierPanel, int kFolds) {
        this.TEMP_PATH = path;
        this.classifierType = ClassifierType.parse(classifierName.toString());
        this.selectedEvaluationClassifierPanel = selectedEvaluationClassifierPanel;
        this.kFolds = kFolds;
    }

    /**
     * This method sets the information of the dataset.
     *
     * @param data the input dataset values
     * @param nameFeatures the string array of features names
     * @param classLabel the string array of class labels names
     */
    public void setDataInfo(double[][] data, String[] nameFeatures, String[] classLabel) {
        this.trainSet = data;
        this.nameFeatures = nameFeatures;
        this.classLabel = classLabel;
    }

    /**
     * This method performs k-fold cross validation on the reduced training set
     * which is achieved by selected feature subset.
     *
     * @param selectedFeature an array of indices of the selected feature subset
     *
     * @return the different criteria values
     */
    public Criteria crossValidation(int[] selectedFeature) {
        Criteria critria = new Criteria();
        String nameDataCSV = TEMP_PATH + "dataCSV.csv";
        String nameDataARFF = TEMP_PATH + "dataARFF.arff";
        FileFunc.createCSVFile(trainSet, selectedFeature, nameDataCSV, nameFeatures, classLabel);
        FileFunc.convertCSVtoARFF(nameDataCSV, nameDataARFF, TEMP_PATH, selectedFeature.length, nameFeatures.length - 1, nameFeatures, classLabel.length, classLabel);

        if (classifierType == ClassifierType.SVM) {
            SVMClassifierPanel svmPanel = (SVMClassifierPanel) selectedEvaluationClassifierPanel;
            critria = CrossValidation.SVM(nameDataARFF, svmPanel.getKernel(), svmPanel.getParameterC(), this.kFolds);
        } else if (classifierType == ClassifierType.NB) {
            critria = CrossValidation.naiveBayes(nameDataARFF, this.kFolds);
        } else if (classifierType == ClassifierType.DT) {
            DTClassifierPanel dtPanel = (DTClassifierPanel) selectedEvaluationClassifierPanel;
            critria = CrossValidation.dTree(nameDataARFF, dtPanel.getConfidence(), dtPanel.getMinNum(), this.kFolds);
        } else if (classifierType == ClassifierType.KNN) {
            KNNClassifierPanel dtPanel = (KNNClassifierPanel) selectedEvaluationClassifierPanel;
            critria = CrossValidation.kNN(nameDataARFF, dtPanel.getKNNValue(), this.kFolds);
        }

        return critria;
    }

    /**
     * This method creates a directory based on the specific path
     */
    public void createTempDirectory() {
        FileFunc.createDirectory(TEMP_PATH);
    }

    /**
     * This method deletes the current directory with all files in the directory
     */
    public void deleteTempDirectory() {
        FileFunc.deleteDirectoryWithAllFiles(TEMP_PATH);
    }
}
