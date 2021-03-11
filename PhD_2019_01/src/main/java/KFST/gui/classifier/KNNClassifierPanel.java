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
package KFST.gui.classifier;

import KFST.gui.ParameterPanel;
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
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

/**
 * This java class is used to create and show a panel for the parameter settings
 * of the k-nearest neighbours classifier.
 *
 * @author Sina Tabakhi
 * @see KFST.gui.ParameterPanel
 */
public class KNNClassifierPanel extends ParameterPanel {

    JLabel lbl_kNNValue, lbl_kNNValueError;
    JTextField txt_kNNValue;
    private static final int DEFAULT_KNN_VALUE = 1;
    private int kNNValue = 1;

    /**
     * Creates new form KNNClassifierPanel. This method is called from within
     * the constructor to initialize the form.
     */
    public KNNClassifierPanel() {
        super("Parameter Settings Panel",
                "K-nearest neighbours settings:",
                "The K-nearest neighbours classifier.",
                "Option\n\n"
                + "KNN value -> the number of neighbours to use.\n\n",
                new Rectangle(10, 10, 200, 20),
                new Rectangle(10, 35, 400, 60),
                new Rectangle(120, 190, 75, 23),
                new Rectangle(240, 190, 75, 23),
                new Dimension(440, 270));

        Container contentPane = getContentPane();

        lbl_kNNValue = new JLabel("KNN value:");
        lbl_kNNValue.setBounds(30, 135, 120, 22);
        txt_kNNValue = new JTextField(String.valueOf(DEFAULT_KNN_VALUE));
        txt_kNNValue.setBounds(130, 135, 120, 21);
        txt_kNNValue.addKeyListener(this);
        lbl_kNNValueError = new JLabel("");
        lbl_kNNValueError.setBounds(260, 135, 50, 22);
        lbl_kNNValueError.setForeground(Color.red);

        contentPane.add(lbl_kNNValue);
        contentPane.add(txt_kNNValue);
        contentPane.add(lbl_kNNValueError);

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
        boolean enableOkButton = true;
        String tempStr;

        tempStr = txt_kNNValue.getText();
        if (!MathFunc.isInteger(tempStr) || Integer.parseInt(tempStr) <= 0) {
            lbl_kNNValueError.setText("*");
            enableOkButton = false;
        } else {
            lbl_kNNValueError.setText("");
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
        setKNNValue(Integer.parseInt(txt_kNNValue.getText()));
        super.btn_okActionPerformed(e);
    }

    /**
     * This method returns the number of neighbours to use.
     *
     * @return the <code>kNNValue</code> parameter
     */
    public int getKNNValue() {
        return kNNValue;
    }

    /**
     * This method sets the number of neighbours to use.
     *
     * @param kNNValue the number of neighbours to use
     */
    public void setKNNValue(int kNNValue) {
        this.kNNValue = kNNValue;
    }

    /**
     * sets the default values of the k-nearest neighbours parameters
     */
    public void setDefaultValue() {
        txt_kNNValue.setText(String.valueOf(DEFAULT_KNN_VALUE));
        kNNValue = DEFAULT_KNN_VALUE;
    }

    /**
     * sets the last values of the k-nearest neighbours parameters entered by
     * user
     *
     * @param kNNValue the number of neighbours to use
     */
    public void setUserValue(int kNNValue) {
        this.kNNValue = kNNValue;
        txt_kNNValue.setText(String.valueOf(this.kNNValue));
    }

//    public static void main(String[] arg) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            System.out.println("Error setting native LAF: " + e);
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            KNNClassifierPanel dtpanel = new KNNClassifierPanel();
//            Dialog dlg = new Dialog(dtpanel);
//            dtpanel.setVisible(true);
//            System.out.println("kNNValue = " + dtpanel.getKNNValue());
//        });
//    }
}
