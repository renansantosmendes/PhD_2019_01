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
 * This java class is used to define the names of weighted filter-based feature
 * selection methods.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.filter.FilterType
 */
public class WeightedFilterType extends FilterType {

    /**
     * Creates new WeightedFilterType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of weighted filter method
     */
    private WeightedFilterType(String name) {
        super(name);
    }

    /**
     * Return the names of weighted filter-based feature selection methods
     *
     * @return an array of names of methods
     */
    public static String[] asList() {
        return new String[]{INFORMATION_GAIN.toString(),
            GAIN_RATIO.toString(),
            SYMMETRICAL_UNCERTAINTY.toString(),
            FISHER_SCORE.toString(),
            GINI_INDEX.toString(),
            LAPLACIAN_SCORE.toString(),
            TERM_VARIANCE.toString()};
    }

    /**
     * Check whether the method is filter-based feature weighting method or not
     *
     * @param type the name of feature selection method
     *
     * @return true if method is weighted filter-based method
     */
    public static boolean isWeightedFilterMethod(String type) {
        return Arrays.asList(WeightedFilterType.asList()).contains(type);
    }
}
