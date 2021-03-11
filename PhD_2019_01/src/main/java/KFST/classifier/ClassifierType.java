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
package KFST.classifier;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of classifiers.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class ClassifierType extends EnumType {

    public static final ClassifierType NONE = new ClassifierType("none", 0);
    public static final ClassifierType SVM = new ClassifierType("Support Vector Machine (SVM)", 1);
    public static final ClassifierType NB = new ClassifierType("Naive Bayes (NB)", 2);
    public static final ClassifierType DT = new ClassifierType("Decision Tree (C4.5)", 3);
    public static final ClassifierType KNN = new ClassifierType("K-Nearest Neighbours (KNN)", 4);

    /**
     * Creates new ClassifierType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of classifier
     */
    private ClassifierType(String name) {
        super(name);
    }

    /**
     * Creates new ClassifierType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of classifier
     * @param value the value of classifier
     */
    private ClassifierType(String name, int value) {
        super(name, value);
    }

    /**
     * Return the names of classifiers
     *
     * @return an array of names of classifiers
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            SVM.toString(),
            NB.toString(),
            DT.toString(),
            KNN.toString()};
    }

    /**
     * Converts the classifier name to ClassifierType
     *
     * @param type the name of classifier as string
     *
     * @return the classifier type
     */
    public static ClassifierType parse(String type) {
        switch (type) {
            case "Support Vector Machine (SVM)":
                return SVM;
            case "Naive Bayes (NB)":
                return NB;
            case "Decision Tree (C4.5)":
                return DT;
            case "K-Nearest Neighbours (KNN)":
                return KNN;
            default:
                return NONE;
        }
    }
}
