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

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of filter-based feature selection
 * methods.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public class FilterType extends EnumType {

    public static final FilterType NONE = new FilterType("none");

    //Supervised filter-based feature selection methods
    public static final FilterType INFORMATION_GAIN = new FilterType("Information gain");
    public static final FilterType GAIN_RATIO = new FilterType("Gain ratio");
    public static final FilterType SYMMETRICAL_UNCERTAINTY = new FilterType("Symmetrical uncertainty");
    public static final FilterType FISHER_SCORE = new FilterType("Fisher score");
    public static final FilterType GINI_INDEX = new FilterType("Gini index");
    public static final FilterType MRMR = new FilterType("Minimal redundancy maximal relevance (MRMR)");

    //Unsupervised filter-based feature selection methods
    public static final FilterType TERM_VARIANCE = new FilterType("Term variance");
    public static final FilterType MUTUAL_CORRELATION = new FilterType("Mutual correlation");
    public static final FilterType RSM = new FilterType("Random subspace method (RSM)");
    public static final FilterType UFSACO = new FilterType("UFSACO");
    public static final FilterType RRFSACO_1 = new FilterType("RRFSACO_1");
    public static final FilterType RRFSACO_2 = new FilterType("RRFSACO_2");
    public static final FilterType IRRFSACO_1 = new FilterType("IRRFSACO_1");
    public static final FilterType IRRFSACO_2 = new FilterType("IRRFSACO_2");
    public static final FilterType MGSACO = new FilterType("MGSACO");

    //Supervised and unsupervised-based feature selection methods
    public static final FilterType LAPLACIAN_SCORE = new FilterType("Laplacian score");
    public static final FilterType RRFS = new FilterType("Relevance-redundancy feature selection (RRFS)");
    //public static final FilterType LAPLACIAN_SCORE = new FilterType("Laplacian score");
    //public static final FilterType RRFS = new FilterType("Relevance-redundancy feature selection (RRFS)");

    /**
     * Creates new FilterType. This method is called from within the constructor
     * to initialize the parameter.
     *
     * @param name the name of filter method
     */
    public FilterType(String name) {
        super(name);
    }

    /**
     * Converts the filter method name to FilterType
     *
     * @param type the name of filter method as string
     *
     * @return the filter method type
     */
    public static FilterType parse(String type) {
        switch (type) {
            case "none":
                return NONE;
            case "Information gain":
                return INFORMATION_GAIN;
            case "Gain ratio":
                return GAIN_RATIO;
            case "Symmetrical uncertainty":
                return SYMMETRICAL_UNCERTAINTY;
            case "Fisher score":
                return FISHER_SCORE;
            case "Gini index":
                return GINI_INDEX;
            case "Laplacian score":
                return LAPLACIAN_SCORE;
            case "Minimal redundancy maximal relevance (MRMR)":
                return MRMR;
            case "Relevance-redundancy feature selection (RRFS)":
                return RRFS;
            case "Term variance":
                return TERM_VARIANCE;
            case "Mutual correlation":
                return MUTUAL_CORRELATION;
            case "Random subspace method (RSM)":
                return RSM;
            case "UFSACO":
                return UFSACO;
            case "RRFSACO_1":
                return RRFSACO_1;
            case "RRFSACO_2":
                return RRFSACO_2;
            case "IRRFSACO_1":
                return IRRFSACO_1;
            case "IRRFSACO_2":
                return IRRFSACO_2;
            case "MGSACO":
                return MGSACO;
            default:
                return NONE;
        }
    }
}
