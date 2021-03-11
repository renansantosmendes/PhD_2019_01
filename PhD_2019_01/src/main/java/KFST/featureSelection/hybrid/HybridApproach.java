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
package KFST.featureSelection.hybrid;

import KFST.featureSelection.FeatureSelection;

/**
 * The abstract class contains the main methods and fields that is used in all
 * hybrid-based feature selection methods. This class inherits from
 * FeatureSelection class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.FeatureSelection
 */
public abstract class HybridApproach extends FeatureSelection {

    /**
     * initializes the parameters
     */
    public HybridApproach() {
        super();
    }

    /**
     * create new object from one of the classes that have inherited from
     * HybridApproach class according to type of the feature selection method
     *
     * @param type type of the feature selection method
     * @param arguments a list of arguments that is applied in the feature
     * selection method
     *
     * @return created object that have inherited from HybridApproach class
     */
    public static HybridApproach newMethod(HybridType type, Object... arguments) {
        return null;
    }
}
