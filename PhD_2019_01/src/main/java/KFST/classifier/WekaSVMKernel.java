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

import KFST.gui.classifier.svmClassifier.SVMKernelType;
import weka.classifiers.functions.supportVector.*;

/**
 * This java class is used to convert SVM kernel implemented in KFST tool to SVM
 * kernel implemented in weka software.
 *
 * @author Sina Tabakhi
 */
public final class WekaSVMKernel {

    /**
     * This method return the weka SVM kernel type according to the KFST SVM
     * kernel type.
     *
     * @param type KFST SVM kernel type
     *
     * @return weka SVM kernel type
     */
    public static Kernel parse(SVMKernelType type) {
        try {
            if (type == SVMKernelType.POLYNOMIAL) {
                return PolyKernel.class.newInstance();
            } else if (type == SVMKernelType.RBF) {
                return RBFKernel.class.newInstance();
            } else if (type == SVMKernelType.PEARSON_VII) {
                return Puk.class.newInstance();
            }
            return null;
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }
}
