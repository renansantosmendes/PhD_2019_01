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

import KFST.classifier.ClassifierType;
//import KFST.gui.classifier.DTClassifierPanel;
//import KFST.gui.classifier.KNNClassifierPanel;
//import KFST.gui.classifier.svmClassifier.SVMClassifierPanel;
import KFST.util.MathFunc;
import java.awt.Color;
import java.awt.Container;
//import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the hybrid genetic algorithm using local search (HGAFS).
 *
 * @author Sina Tabakhi
 * @see KFST.gui.ParameterPanel
 * @see KFST.featureSelection.wrapper.GABasedMethods.HGAFS.HGAFS
 */
public class HGAFSPanel extends BasicGAPanel {

    JLabel lbl_epsilon, lbl_epsilonError,
            lbl_mu, lbl_muError;
    JTextField txt_epsilon, txt_mu;
    private double epsilon = 0.5, mu = 0.65;
    private static final double DEFAULT_EPSILON = 0.5, DEFAULT_MU = 0.65;

    /**
     * Creates new form HGAFSPanel. This method is called from within the
     * constructor to initialize the form.
     */
    public HGAFSPanel() {
        super();
        Container contentPane = getContentPane();

        lbl_epsilon = new JLabel("Parameter epsilon:");
        lbl_epsilon.setBounds(50, 505, 170, 22);
        txt_epsilon = new JTextField(Double.toString(DEFAULT_EPSILON));
        txt_epsilon.setBounds(170, 505, 120, 21);
        txt_epsilon.addKeyListener(this);
        lbl_epsilonError = new JLabel("");
        lbl_epsilonError.setBounds(300, 505, 50, 22);
        lbl_epsilonError.setForeground(Color.red);

        lbl_mu = new JLabel("Parameter mu:");
        lbl_mu.setBounds(50, 540, 170, 22);
        txt_mu = new JTextField(Double.toString(DEFAULT_MU));
        txt_mu.setBounds(170, 540, 120, 21);
        txt_mu.addKeyListener(this);
        lbl_muError = new JLabel("");
        lbl_muError.setBounds(300, 540, 50, 22);
        lbl_muError.setForeground(Color.red);

        contentPane.add(lbl_epsilon);
        contentPane.add(txt_epsilon);
        contentPane.add(lbl_epsilonError);

        contentPane.add(lbl_mu);
        contentPane.add(txt_mu);
        contentPane.add(lbl_muError);

        this.setMethodTitle("HGAFS method settings:");
        this.setMethodDescription("<html> Hybrid genetic algorithm for feature selection using local search (HGAFS) is a version "
                + "of GA in which a local search strategy is used to select the less correlated feature subset. Also, a subset size "
                + "determination scheme is used to select a subset of features with reduced size. Additionally, k-fold cross validation "
                + "on training set is used for evaluating the classification performance. </html>");

        this.setMoreOptionDescription(super.getMoreOptionDescription()
                + "Parameter epsilon -> the epsilon parameter used in the subset size determining scheme (a real number in the range of (0, 1)).\n\n"
                + "Parameter mu -> the mu parameter used in the local search operation to control similar/dissimilar (a real number in the range of (0, 1)).\n\n");
        this.setMethodDescriptionPosition(new Rectangle(10, 35, 545, 100));
        this.setOkButtonPosition(new Rectangle(190, 580, 75, 23));
        this.setMoreButtonPosition(new Rectangle(310, 580, 75, 23));
        this.setPanelSize(new Dimension(570, 660));
        this.changeDefaultValue(SelectionType.RANK_BASED_SELECTION, CrossOverType.TWO_POINT_CROSS_OVER,
                MutationType.BITWISE_MUTATION, ReplacementType.TOTAL_REPLACEMENT,
                100, 40, 0.6, 0.02, 10);

        contentPane.validate();
//        contentPane.revalidate();
        contentPane.repaint();
//        this.pack();
    }

    /**
     * The listener method for receiving keyboard events (keystrokes). Invoked
     * when a key has been released.
     *
     * @param e an action event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        boolean enableOkButton = btn_ok.isEnabled();
        String tempStr;

        tempStr = txt_epsilon.getText();
        if (!MathFunc.isDouble(tempStr) || Double.parseDouble(tempStr) <= 0 || Double.parseDouble(tempStr) >= 1) {
            lbl_epsilonError.setText("*");
            enableOkButton = false;
        } else {
            lbl_epsilonError.setText("");
        }

        tempStr = txt_mu.getText();
        if (!MathFunc.isDouble(tempStr) || Double.parseDouble(tempStr) <= 0 || Double.parseDouble(tempStr) >= 1) {
            lbl_muError.setText("*");
            enableOkButton = false;
        } else {
            lbl_muError.setText("");
        }

        btn_ok.setEnabled(enableOkButton);
    }

    /**
     * This method sets an action for the btn_ok button.
     *
     * @param e an action event
     */
    @Override
    protected void btn_okActionPerformed(ActionEvent e) {
        super.btn_okActionPerformed(e);
        setEpsilon(Double.parseDouble(txt_epsilon.getText()));
        setMu(Double.parseDouble(txt_mu.getText()));
    }

    /**
     * This method returns the epsilon value.
     *
     * @return the <code>epsilon</code> parameter
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * This method sets the epsilon value.
     *
     * @param epsilon the epsilon value
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * This method returns the mu value.
     *
     * @return the <code>mu</code> parameter
     */
    public double getMu() {
        return mu;
    }

    /**
     * This method sets the mu value.
     *
     * @param mu the mu value
     */
    public void setMu(double mu) {
        this.mu = mu;
    }

    /**
     * sets the default values of the HGAFS parameters
     */
    @Override
    public void setDefaultValue() {
        super.setDefaultValue();
        txt_epsilon.setText(String.valueOf(DEFAULT_EPSILON));
        txt_mu.setText(String.valueOf(DEFAULT_MU));

        epsilon = DEFAULT_EPSILON;
        mu = DEFAULT_MU;
    }

    /**
     * sets the last values of the basic GA parameters entered by user
     *
     * @param classifierType the selected classifier type
     * @param selectedClassifierPan the selected classifier panel
     * @param selectionType the selected selection type
     * @param crossoverType the selected crossover type
     * @param mutationType the selected mutation type
     * @param replacementType the selected replacement type
     * @param numIteration the maximum number of allowed iterations that
     * algorithm repeated
     * @param populationSize the size of population of candidate solutions
     * @param crossoverRate the probability of crossover operation
     * @param mutationRate the probability of mutation operation
     * @param kFolds the number of equal sized subsamples that is used in k-fold
     * cross validation
     * @param epsilon the epsilon parameter used in the subset size determining
     * scheme
     * @param mu the mu parameter used in the local search operation (control
     * similar/dissimilar)
     */
    public void setUserValue(ClassifierType classifierType, Object selectedClassifierPan,
            SelectionType selectionType, CrossOverType crossoverType,
            MutationType mutationType, ReplacementType replacementType,
            int numIteration, int populationSize,
            double crossoverRate, double mutationRate, int kFolds, double epsilon, double mu) {
        super.setUserValue(classifierType, selectedClassifierPan,
                selectionType, crossoverType, mutationType, replacementType,
                numIteration, populationSize, crossoverRate, mutationRate, kFolds);
        this.epsilon = epsilon;
        this.mu = mu;

        txt_epsilon.setText(String.valueOf(this.epsilon));
        txt_mu.setText(String.valueOf(this.mu));
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        HGAFSPanel dtpanel = new HGAFSPanel();
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
//        System.out.println("epsilon = " + dtpanel.getEpsilon());
//        System.out.println("mu = " + dtpanel.getMu());
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
//        System.out.println("epsilon = " + dtpanel.getEpsilon());
//        System.out.println("mu = " + dtpanel.getMu());
//
//        dtpanel.setUserValue(ClassifierType.SVM, new SVMClassifierPanel(),
//                SelectionType.RANK_BASED_SELECTION, CrossOverType.UNIFORM_CROSS_OVER,
//                MutationType.BITWISE_MUTATION, ReplacementType.TOTAL_REPLACEMENT,
//                20, 10, 0.2, 0.02, 4, 0.9, 0.99);
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
//        System.out.println("epsilon = " + dtpanel.getEpsilon());
//        System.out.println("mu = " + dtpanel.getMu());
//    }
}
