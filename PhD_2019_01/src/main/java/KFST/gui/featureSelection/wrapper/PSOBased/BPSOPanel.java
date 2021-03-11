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
package KFST.gui.featureSelection.wrapper.PSOBased;

//import KFST.classifier.ClassifierType;
//import KFST.gui.classifier.DTClassifierPanel;
//import KFST.gui.classifier.KNNClassifierPanel;
//import KFST.gui.classifier.svmClassifier.SVMClassifierPanel;
//import java.awt.Dialog;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the binary particle swarm optimization (BPSO) method.
 *
 * @author Sina Tabakhi
 * @see KFST.gui.ParameterPanel
 * @see KFST.featureSelection.wrapper.PSOBasedMethods.BPSO.BPSO
 */
public class BPSOPanel extends BasicPSOPanel {

    /**
     * Creates new form BPSOPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public BPSOPanel() {
        super();

        this.setMethodTitle("Binary PSO method settings:");
        this.setMethodDescription("<html> Feature selection based on binary particle swarm optimization (BPSO) "
                + "is basic PSO method in which each particle is randomly initialized. Additionally, "
                + "k-fold cross validation on training set is used for evaluating the classification "
                + "performance of a selected feature subset during feature selection process. </html>");
        this.enablePositionValues(false);
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        BPSOPanel dtpanel = new BPSOPanel();
//        Dialog dlg = new Dialog(dtpanel);
//        dtpanel.setVisible(true);
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
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("population size = " + dtpanel.getPopulationSize());
//        System.out.println("inertia weight = " + dtpanel.getInertiaWeight());
//        System.out.println("c1 = " + dtpanel.getC1());
//        System.out.println("c2 = " + dtpanel.getC2());
//        System.out.println("start position = " + dtpanel.getStartPosInterval());
//        System.out.println("end position = " + dtpanel.getEndPosInterval());
//        System.out.println("max velocity = " + dtpanel.getMinVelocity());
//        System.out.println("min velocity = " + dtpanel.getMaxVelocity());
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
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("population size = " + dtpanel.getPopulationSize());
//        System.out.println("inertia weight = " + dtpanel.getInertiaWeight());
//        System.out.println("c1 = " + dtpanel.getC1());
//        System.out.println("c2 = " + dtpanel.getC2());
//        System.out.println("start position = " + dtpanel.getStartPosInterval());
//        System.out.println("end position = " + dtpanel.getEndPosInterval());
//        System.out.println("max velocity = " + dtpanel.getMinVelocity());
//        System.out.println("min velocity = " + dtpanel.getMaxVelocity());
//        System.out.println("K fold = " + dtpanel.getKFolds());
//
//        dtpanel.setUserValue(ClassifierType.SVM, new SVMClassifierPanel(), 20, 10, 0.6, 0.5, 0.3, 10, 20, 30, 40, 50);
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
//        System.out.println("num iteration = " + dtpanel.getNumIteration());
//        System.out.println("population size = " + dtpanel.getPopulationSize());
//        System.out.println("inertia weight = " + dtpanel.getInertiaWeight());
//        System.out.println("c1 = " + dtpanel.getC1());
//        System.out.println("c2 = " + dtpanel.getC2());
//        System.out.println("start position = " + dtpanel.getStartPosInterval());
//        System.out.println("end position = " + dtpanel.getEndPosInterval());
//        System.out.println("min velocity = " + dtpanel.getMinVelocity());
//        System.out.println("max velocity = " + dtpanel.getMaxVelocity());
//        System.out.println("K fold = " + dtpanel.getKFolds());
//    }
}
