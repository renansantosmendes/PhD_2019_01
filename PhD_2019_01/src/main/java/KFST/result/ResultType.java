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
package KFST.result;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the types of result.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public class ResultType extends EnumType {

    public static final ResultType NONE = new ResultType("none");
    public static final ResultType DATASET_INFORMATION = new ResultType("Dataset information");
    public static final ResultType SELECTED_FEATURE_SUBSET = new ResultType("Selected feature subset");
    public static final ResultType FEATURE_VALUES = new ResultType("Feature values");
    public static final ResultType PERFORMANCE_MEASURES = new ResultType("Performance measures");

    /**
     * Creates new ResultType. This method is called from within the constructor
     * to initialize the parameter.
     *
     * @param name the name of result
     */
    private ResultType(String name) {
        super(name);
    }

    /**
     * Return the names of result
     *
     * @return an array of names of result
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            DATASET_INFORMATION.toString(),
            SELECTED_FEATURE_SUBSET.toString(),
            FEATURE_VALUES.toString(),
            PERFORMANCE_MEASURES.toString()};
    }

    /**
     * Converts the result name to ResultType
     *
     * @param type the name of result as string
     *
     * @return the result type
     *
     * @throws Exception if the specified type is not available
     */
    public static ResultType parse(String type) throws Exception {
        switch (type) {
            case "none":
                return NONE;
            case "Dataset information":
                return DATASET_INFORMATION;
            case "Selected feature subset":
                return SELECTED_FEATURE_SUBSET;
            case "Feature values":
                return FEATURE_VALUES;
            case "Performance measures":
                return PERFORMANCE_MEASURES;
            default:
                throw new Exception("Undefine type");
        }
    }
}
