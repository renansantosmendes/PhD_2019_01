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
package KFST.featureSelection.embedded;

import KFST.featureSelection.EnumType;
import java.util.Arrays;

/**
 * This java class is used to define the names of embedded-based feature
 * selection methods.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public class EmbeddedType extends EnumType {

    public static final EmbeddedType NONE = new EmbeddedType("none");
    public static final EmbeddedType DECISION_TREE_BASED = new EmbeddedType("Decision tree based methods");
    public static final EmbeddedType RANDOM_FOREST_METHOD = new EmbeddedType("Random forest method");
    public static final EmbeddedType SVM_RFE = new EmbeddedType("SVM_RFE");
    public static final EmbeddedType MSVM_RFE = new EmbeddedType("MSVM_RFE");
    public static final EmbeddedType OVO_SVM_RFE = new EmbeddedType("OVO_SVM_RFE");
    public static final EmbeddedType OVA_SVM_RFE = new EmbeddedType("OVA_SVM_RFE");

    /**
     * Creates new EmbeddedType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of embedded method
     */
    private EmbeddedType(String name) {
        super(name);
    }

    /**
     * Return the names of embedded-based feature selection methods
     *
     * @return an array of names of methods
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            DECISION_TREE_BASED.toString(),
            RANDOM_FOREST_METHOD.toString(),
            SVM_RFE.toString(),
            MSVM_RFE.toString(),
            OVO_SVM_RFE.toString(),
            OVA_SVM_RFE.toString()};
    }

    /**
     * Converts the embedded method name to EmbeddedType
     *
     * @param type the name of embedded method as string
     *
     * @return the embedded method type
     */
    public static EmbeddedType parse(String type) {
        switch (type) {
            case "Decision tree based methods":
                return DECISION_TREE_BASED;
            case "Random forest method":
                return RANDOM_FOREST_METHOD;
            case "SVM_RFE":
                return SVM_RFE;
            case "MSVM_RFE":
                return MSVM_RFE;
            case "OVO_SVM_RFE":
                return OVO_SVM_RFE;
            case "OVA_SVM_RFE":
                return OVA_SVM_RFE;
            default:
                return NONE;
        }
    }

    /**
     * Check whether the method is embedded-based feature selection method or
     * not
     *
     * @param type the name of feature selection method
     *
     * @return true if method is embedded-based method
     */
    public static boolean isEmbeddedMethod(String type) {
        return !type.equals(NONE.toString())
                && Arrays.asList(EmbeddedType.asList()).contains(type);
    }
}
