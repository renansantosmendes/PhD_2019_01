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
 * of the particle swarm optimization version 4-2 (PSO(4-2)) method.
 *
 * @author Sina Tabakhi
 * @see KFST.gui.ParameterPanel
 * @see KFST.featureSelection.wrapper.PSOBasedMethods.PSO42.PSO42
 */
public class PSO42Panel extends BasicPSOPanel {

    JLabel lbl_theta, lbl_thetaError;
    JTextField txt_theta;
    private double theta = 0.6;
    private static final double DEFAULT_THETA = 0.6;

    /**
     * Creates new form PSO42Panel. This method is called from within the
     * constructor to initialize the form.
     */
    public PSO42Panel() {
        super();
        Container contentPane = getContentPane();

        lbl_theta = new JLabel("Parameter theta:");
        lbl_theta.setBounds(50, 450, 170, 22);
        txt_theta = new JTextField(String.valueOf(DEFAULT_THETA));
        txt_theta.setBounds(170, 450, 120, 21);
        txt_theta.addKeyListener(this);
        lbl_thetaError = new JLabel("");
        lbl_thetaError.setBounds(300, 450, 50, 22);
        lbl_thetaError.setForeground(Color.red);

        contentPane.add(lbl_theta);
        contentPane.add(txt_theta);
        contentPane.add(lbl_thetaError);

        this.setMethodTitle("PSO(4-2) method settings:");
        this.setMethodDescription("<html> Particle swarm optimization version 4-2 (PSO(4-2)) is a version of PSO "
                + "with new initialization strategy and updating rule. Additionally, k-fold cross validation on "
                + "training set is used for evaluating the classification performance of "
                + "a selected feature subset during feature selection process. </html>");
        this.setMoreOptionDescription(super.getMoreOptionDescription()
                + "Parameter theta -> the threshold is used to determine whether a feature is selected or not.\n\n");
        this.setOkButtonPosition(new Rectangle(190, 500, 75, 23));
        this.setMoreButtonPosition(new Rectangle(310, 500, 75, 23));
        this.setPanelSize(new Dimension(570, 580));
        this.enablePositionValues(false);

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

        tempStr = txt_theta.getText();
        if (!MathFunc.isDouble(tempStr) || Double.parseDouble(tempStr) <= 0 || Double.parseDouble(tempStr) >= 1) {
            lbl_thetaError.setText("*");
            enableOkButton = false;
        } else {
            lbl_thetaError.setText("");
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
        setTheta(Double.parseDouble(txt_theta.getText()));
    }

    /**
     * This method returns the theta value.
     *
     * @return the <code>theta</code> parameter
     */
    public double getTheta() {
        return theta;
    }

    /**
     * This method sets the theta value.
     *
     * @param theta the theta value
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    /**
     * sets the default values of the PSO(4-2) parameters
     */
    @Override
    public void setDefaultValue() {
        super.setDefaultValue();
        txt_theta.setText(String.valueOf(DEFAULT_THETA));
        theta = DEFAULT_THETA;
    }

    /**
     * sets the last values of the PSO(4-2) parameters entered by user
     *
     * @param classifierType the selected classifier type
     * @param selectedClassifierPan the selected classifier panel
     * @param numIteration the maximum number of allowed iterations that
     * algorithm repeated
     * @param populationSize the size of population of candidate solutions
     * @param inertiaWeight the inertia weight in the velocity updating rule
     * @param c1 the acceleration constant in the velocity updating rule
     * @param c2 the acceleration constant in the velocity updating rule
     * @param startPosInterval the position interval start value of each
     * particle
     * @param endPosInterval the position interval end value of each particle
     * @param minVelocity the velocity interval end value of each particle
     * @param maxVelocity the velocity interval end value of each particle
     * @param kFolds the number of equal sized subsamples that is used in k-fold
     * cross validation
     * @param theta the threshold is used to determine whether a feature is
     * selected or not
     */
    public void setUserValue(ClassifierType classifierType, Object selectedClassifierPan, int numIteration, int populationSize,
            double inertiaWeight, double c1, double c2, double startPosInterval, double endPosInterval,
            double minVelocity, double maxVelocity, int kFolds, double theta) {
        super.setUserValue(classifierType, selectedClassifierPan, numIteration, populationSize,
                inertiaWeight, c1, c2, startPosInterval, endPosInterval, minVelocity, maxVelocity, kFolds);
        this.theta = theta;
        txt_theta.setText(String.valueOf(theta));
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        PSO42Panel dtpanel = new PSO42Panel();
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
//        System.out.println("theta = " + dtpanel.getTheta());
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
//        System.out.println("theta = " + dtpanel.getTheta());
//
//        dtpanel.setUserValue(ClassifierType.SVM, new SVMClassifierPanel(), 20, 10, 0.6, 0.5, 0.3, 10, 20, 30, 40, 50, 0.8);
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
//        System.out.println("theta = " + dtpanel.getTheta());
//    }
}
