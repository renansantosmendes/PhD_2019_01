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
package KFST.gui.featureSelection.wrapper.GABased;

//import KFST.classifier.ClassifierType;
//import KFST.gui.classifier.DTClassifierPanel;
//import KFST.gui.classifier.KNNClassifierPanel;
//import KFST.gui.classifier.svmClassifier.SVMClassifierPanel;
import java.awt.Container;
//import java.awt.Dialog;
import java.awt.Rectangle;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the simple genetic algorithm (Simple GA).
 *
 * @author Sina Tabakhi
 * @see KFST.gui.ParameterPanel
 * @see KFST.featureSelection.wrapper.GABasedMethods.SimpleGA.SimpleGA
 */
public class SimpleGAPanel extends BasicGAPanel {

    /**
     * Creates new form SimpleGAPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public SimpleGAPanel() {
        super();
        Container contentPane = getContentPane();

        this.setMethodTitle("Simple GA method settings:");
        this.setMethodDescription("<html> Simple genetic algorithm (Simple GA) "
                + "is a version of GA in which each individual is randomly initialized. Additionally, "
                + "k-fold cross validation on training set is used for evaluating the classification "
                + "performance of a selected feature subset during feature selection process. </html>");

        this.setMethodDescriptionPosition(new Rectangle(10, 35, 545, 90));
        
        contentPane.validate();
//        contentPane.revalidate();
        contentPane.repaint();
//        this.pack();
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        SimpleGAPanel dtpanel = new SimpleGAPanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
//
//        System.out.println("classifier type = " + dtpanel.getClassifierType().toString());
//        if (dtpanel.getClassifierType() == ClassifierType.SVM) {
//            SVMClassifierPanel pan = (SVMClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:   kernel = " + pan.getKernel().toString()
//                    + "   C = " + pan.getParameterC());
//        } else if (dtpanel.getClassifierType() == ClassifierType.DT) {
//            DTClassifierPanel pan = (DTClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:    min num = " + pan.getMinNum()
//                    + "  confidence = " + pan.getConfidence());
//        } else if (dtpanel.getClassifierType() == ClassifierType.KNN) {
//            KNNClassifierPanel pan = (KNNClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:    KNN Value = " + pan.getKNNValue());
//        }
//
//        System.out.println("selection type = " + dtpanel.getSelectionType().toString());
//        System.out.println("crossover type = " + dtpanel.getCrossOverType().toString());
//        System.out.println("mutation type = " + dtpanel.getMutationType().toString());
//        System.out.println("replacement type = " + dtpanel.getReplacementType().toString());
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("population size = " + dtpanel.getPopulationSize());
//        System.out.println("crossover rate = " + dtpanel.getCrossoverRate());
//        System.out.println("mutation rate = " + dtpanel.getMutationRate());
//        System.out.println("K fold = " + dtpanel.getKFolds());
//
//        dtpanel.setDefaultValue();
//
//        System.out.println("classifier type = " + dtpanel.getClassifierType().toString());
//        if (dtpanel.getClassifierType() == ClassifierType.SVM) {
//            SVMClassifierPanel pan = (SVMClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:   kernel = " + pan.getKernel().toString()
//                    + "   C = " + pan.getParameterC());
//        } else if (dtpanel.getClassifierType() == ClassifierType.DT) {
//            DTClassifierPanel pan = (DTClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:    min num = " + pan.getMinNum()
//                    + "  confidence = " + pan.getConfidence());
//        } else if (dtpanel.getClassifierType() == ClassifierType.KNN) {
//            KNNClassifierPanel pan = (KNNClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:    KNN Value = " + pan.getKNNValue());
//        }
//        System.out.println("selection type = " + dtpanel.getSelectionType().toString());
//        System.out.println("crossover type = " + dtpanel.getCrossOverType().toString());
//        System.out.println("mutation type = " + dtpanel.getMutationType().toString());
//        System.out.println("replacement type = " + dtpanel.getReplacementType().toString());
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("population size = " + dtpanel.getPopulationSize());
//        System.out.println("crossover rate = " + dtpanel.getCrossoverRate());
//        System.out.println("mutation rate = " + dtpanel.getMutationRate());
//        System.out.println("K fold = " + dtpanel.getKFolds());
//
//        dtpanel.setUserValue(ClassifierType.SVM, new SVMClassifierPanel(),
//                SelectionType.RANK_BASED_SELECTION, CrossOverType.UNIFORM_CROSS_OVER,
//                MutationType.BITWISE_MUTATION, ReplacementType.TOTAL_REPLACEMENT,
//                20, 10, 0.2, 0.02, 4);
//
//        System.out.println("classifier type = " + dtpanel.getClassifierType().toString());
//        if (dtpanel.getClassifierType() == ClassifierType.SVM) {
//            SVMClassifierPanel pan = (SVMClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:   kernel = " + pan.getKernel().toString()
//                    + "   C = " + pan.getParameterC());
//        } else if (dtpanel.getClassifierType() == ClassifierType.DT) {
//            DTClassifierPanel pan = (DTClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:    min num = " + pan.getMinNum()
//                    + "  confidence = " + pan.getConfidence());
//        } else if (dtpanel.getClassifierType() == ClassifierType.KNN) {
//            KNNClassifierPanel pan = (KNNClassifierPanel) dtpanel.getSelectedClassifierPan();
//            System.out.println("user:    KNN Value = " + pan.getKNNValue());
//        }
//        System.out.println("selection type = " + dtpanel.getSelectionType().toString());
//        System.out.println("crossover type = " + dtpanel.getCrossOverType().toString());
//        System.out.println("mutation type = " + dtpanel.getMutationType().toString());
//        System.out.println("replacement type = " + dtpanel.getReplacementType().toString());
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("population size = " + dtpanel.getPopulationSize());
//        System.out.println("crossover rate = " + dtpanel.getCrossoverRate());
//        System.out.println("mutation rate = " + dtpanel.getMutationRate());
//        System.out.println("K fold = " + dtpanel.getKFolds());
//    }
}
