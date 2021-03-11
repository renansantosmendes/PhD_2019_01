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
package KFST.gui.featureSelection.filter.rsm;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of multivariate method used in
 * RSM feature selection method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class MultivariateMethodType extends EnumType {

    public static final MultivariateMethodType NONE = new MultivariateMethodType("none");
    public static final MultivariateMethodType MUTUAL_CORRELATION = new MultivariateMethodType("Mutual correlation");

    /**
     * Creates new MultivariateMethodType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of multivariate method
     */
    private MultivariateMethodType(String name) {
        super(name);
    }

    /**
     * Return the names of multivariate method
     *
     * @return an array of multivariate methods
     */
    public static String[] asList() {
        return new String[]{MUTUAL_CORRELATION.toString()};
    }

    /**
     * Converts the multivariate method name to MultivariateMethodType
     *
     * @param type the name of multivariate method as string
     *
     * @return the multivariate method type
     */
    public static MultivariateMethodType parse(String type) {
        switch (type) {
            case "Mutual correlation":
                return MUTUAL_CORRELATION;
            default:
                return NONE;
        }
    }
}
