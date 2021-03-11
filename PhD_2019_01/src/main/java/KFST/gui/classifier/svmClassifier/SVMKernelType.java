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
package KFST.gui.classifier.svmClassifier;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of kernel used in SVM.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class SVMKernelType extends EnumType {

    public static final SVMKernelType NONE = new SVMKernelType("none");
    public static final SVMKernelType POLYNOMIAL = new SVMKernelType("Polynomial kernel");
    public static final SVMKernelType RBF = new SVMKernelType("RBF kernel");
    public static final SVMKernelType PEARSON_VII = new SVMKernelType("Pearson VII function-based universal kernel");

    /**
     * Creates new SVMKernelType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param kernelName the name of kernel
     */
    private SVMKernelType(String kernelName) {
        super(kernelName);
    }

    /**
     * Returns the names of kernel
     *
     * @return an array of names of kernel
     */
    public static String[] asList() {
        return new String[]{POLYNOMIAL.toString(),
            RBF.toString(),
            PEARSON_VII.toString()};
    }

    /**
     * Converts the kernel name to SVMKernelType
     *
     * @param type the name of kernel as string
     *
     * @return the SVM kernel type
     */
    public static SVMKernelType parse(String type) {
        switch (type) {
            case "Polynomial kernel":
                return POLYNOMIAL;
            case "RBF kernel":
                return RBF;
            case "Pearson VII function-based universal kernel":
                return PEARSON_VII;
            default:
                return NONE;
        }
    }
}
