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
package KFST.featureSelection.filter;

import java.util.Arrays;

/**
 * This java class is used to define the names of non weighted filter-based
 * feature selection methods.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.FilterType
 */
public class NonWeightedFilterType extends FilterType {

    /**
     * Creates new NonWeightedFilterType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of non weighted filter method
     */
    private NonWeightedFilterType(String name) {
        super(name);
    }

    /**
     * Return the names of filter-based feature selection methods that are not
     * feature weighting
     *
     * @return an array of names of methods
     */
    public static String[] asList() {
        return new String[]{MRMR.toString(),
            RRFS.toString(),
            MUTUAL_CORRELATION.toString(),
            RSM.toString(),
            UFSACO.toString(),
            RRFSACO_1.toString(),
            RRFSACO_2.toString(),
            IRRFSACO_1.toString(),
            IRRFSACO_2.toString(),
            MGSACO.toString()};
    }

    /**
     * Check whether the method is filter-based feature weighting method or not
     *
     * @param type the name of feature selection method
     *
     * @return true if method is filter-based method(Methods that are not
     * classified as feature weighting method)
     */
    public static boolean isNonWeightedFilterMethod(String type) {
        return Arrays.asList(NonWeightedFilterType.asList()).contains(type);
    }
}
