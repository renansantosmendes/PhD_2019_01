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
package KFST.classifier.evaluation.wekaClassifier;

import KFST.classifier.WekaSVMKernel;
import KFST.gui.classifier.svmClassifier.SVMKernelType;
import KFST.result.performanceMeasure.Criteria;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

/**
 * This java class is used to apply the classifiers for computing the
 * performance of the feature selection methods. The classifiers have been
 * implemented as the Weka software. The training-test sets method is used for
 * evaluating classifiers.
 *
 * @author Sina Tabakhi
 */
public class TrainTestEvaluation {

    /**
     * This method builds and evaluates the support vector machine(SVM)
     * classifier. The SMO is used as the SVM classifier implemented in the Weka
     * software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     * @param svmKernel the kernel to use
     * @param c the complexity parameter C
     *
     * @return the different criteria values
     * @see KFST.result.performanceMeasure.Criteria
     */
    public static Criteria SVM(String pathTrainData, String pathTestData, SVMKernelType svmKernel, double c) {
        Criteria criteria = new Criteria();
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);

            SMO svm = new SMO();
            svm.setC(c);
            svm.setKernel(WekaSVMKernel.parse(svmKernel));
            svm.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTrain);
            eval.evaluateModel(svm, dataTest);

            //Set different criteria values
            criteria.setErrorRate(eval.errorRate() * 100);
            criteria.setAccuracy(100 - criteria.getErrorRate());
        } catch (Exception ex) {
            Logger.getLogger(TrainTestEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return criteria;
    }

    /**
     * This method builds and evaluates the naiveBayes(NB) classifier. The
     * naiveBayes is used as the NB classifier implemented in the Weka software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     *
     * @return the different criteria values
     * @see KFST.result.performanceMeasure.Criteria
     */
    public static Criteria naiveBayes(String pathTrainData, String pathTestData) {
        Criteria criteria = new Criteria();
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);

            NaiveBayes nb = new NaiveBayes();
            nb.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTrain);
            eval.evaluateModel(nb, dataTest);

            //Set different criteria values
            criteria.setErrorRate(eval.errorRate() * 100);
            criteria.setAccuracy(100 - criteria.getErrorRate());
        } catch (Exception ex) {
            Logger.getLogger(TrainTestEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return criteria;
    }

    /**
     * This method builds and evaluates the decision tree(DT) classifier. The
     * j48 is used as the DT classifier implemented in the Weka software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     * @param confidenceValue The confidence factor used for pruning
     * @param minNumSampleInLeaf The minimum number of instances per leaf
     *
     * @return the different criteria values
     * @see KFST.result.performanceMeasure.Criteria
     */
    public static Criteria dTree(String pathTrainData, String pathTestData, double confidenceValue, int minNumSampleInLeaf) {
        Criteria criteria = new Criteria();
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);

            J48 decisionTree = new J48();
            decisionTree.setConfidenceFactor((float) confidenceValue);
            decisionTree.setMinNumObj(minNumSampleInLeaf);
            decisionTree.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTrain);
            eval.evaluateModel(decisionTree, dataTest);

            //Set different criteria values
            criteria.setErrorRate(eval.errorRate() * 100);
            criteria.setAccuracy(100 - criteria.getErrorRate());
        } catch (Exception ex) {
            Logger.getLogger(TrainTestEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return criteria;
    }

    /**
     * This method builds and evaluates the k-nearest neighbours(knn)
     * classifier. The IBk is used as the knn classifier implemented in the Weka
     * software.
     *
     * @param pathTrainData the path of the train set
     * @param pathTestData the path of the test set
     * @param kNNValue the number of neighbours to use
     *
     * @return the different criteria values
     * @see KFST.result.performanceMeasure.Criteria
     */
    public static Criteria kNN(String pathTrainData, String pathTestData, int kNNValue) {
        Criteria criteria = new Criteria();
        try {
            BufferedReader readerTrain = new BufferedReader(new FileReader(pathTrainData));
            Instances dataTrain = new Instances(readerTrain);
            readerTrain.close();
            dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

            BufferedReader readerTest = new BufferedReader(new FileReader(pathTestData));
            Instances dataTest = new Instances(readerTest);
            readerTest.close();
            dataTest.setClassIndex(dataTest.numAttributes() - 1);

            IBk knn = new IBk();
            knn.setKNN(kNNValue);
            knn.buildClassifier(dataTrain);
            Evaluation eval = new Evaluation(dataTrain);
            eval.evaluateModel(knn, dataTest);

            //Set different criteria values
            criteria.setErrorRate(eval.errorRate() * 100);
            criteria.setAccuracy(100 - criteria.getErrorRate());
        } catch (Exception ex) {
            Logger.getLogger(TrainTestEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return criteria;
    }
}
