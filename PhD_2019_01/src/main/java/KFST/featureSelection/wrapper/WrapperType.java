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
package KFST.featureSelection.wrapper;

import KFST.featureSelection.EnumType;
import java.util.Arrays;

/**
 * This java class is used to define the names of wrapper-based feature
 * selection methods.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public class WrapperType extends EnumType {

    public static final WrapperType NONE = new WrapperType("none");
    public static final WrapperType BPSO = new WrapperType("Binary PSO");
    public static final WrapperType CPSO = new WrapperType("Continuous PSO");
    public static final WrapperType PSO42 = new WrapperType("PSO(4-2)");
    public static final WrapperType HPSO_LS = new WrapperType("HPSO-LS");
    public static final WrapperType SIMPLE_GA = new WrapperType("Simple GA");
    public static final WrapperType HGAFS = new WrapperType("HGAFS");
    public static final WrapperType OPTIMAL_ACO = new WrapperType("Optimal ACO");

    /**
     * Creates new WrapperType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of wrapper method
     */
    private WrapperType(String name) {
        super(name);
    }

    /**
     * Return the names of wrapper-based feature selection methods
     *
     * @return an array of names of methods
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            BPSO.toString(),
            CPSO.toString(),
            PSO42.toString(),
            HPSO_LS.toString(),
            SIMPLE_GA.toString(),
            HGAFS.toString(),
            OPTIMAL_ACO.toString()};
    }

    /**
     * Converts the wrapper method name to WrapperType
     *
     * @param type the name of wrapper method as string
     *
     * @return the wrapper method type
     */
    public static WrapperType parse(String type) {
        switch (type) {
            case "Binary PSO":
                return BPSO;
            case "Continuous PSO":
                return CPSO;
            case "PSO(4-2)":
                return PSO42;
            case "HPSO-LS":
                return HPSO_LS;
            case "Simple GA":
                return SIMPLE_GA;
            case "HGAFS":
                return HGAFS;
            case "Optimal ACO":
                return OPTIMAL_ACO;
            default:
                return NONE;
        }
    }

    /**
     * Check whether the method is wrapper-based feature selection method or not
     *
     * @param type the name of feature selection method
     *
     * @return true if method is wrapper-based method
     */
    public static boolean isWrapperMethod(String type) {
        return !type.equals(NONE.toString())
                && Arrays.asList(WrapperType.asList()).contains(type);
    }
}
