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
package KFST.gui.menu.selectMode;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of select mode used in the result
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public class SelectModeType extends EnumType {

    public static final SelectModeType NONE = new SelectModeType("none");
    public static final SelectModeType AVERAGE = new SelectModeType("Average");
    public static final SelectModeType TOTAL = new SelectModeType("Total");

    /**
     * Creates new SelectModeType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of select mode
     */
    private SelectModeType(String name) {
        super(name);
    }

    /**
     * Returns the names of select mode
     *
     * @return an array of names of select mode
     */
    public static String[] asList() {
        return new String[]{AVERAGE.toString(),
            TOTAL.toString()};
    }

    /**
     * Converts the select mode name to SelectModeType
     *
     * @param type the name of select mode as string
     *
     * @return the select mode type
     */
    public static SelectModeType parse(String type) {
        switch (type) {
            case "Average":
                return AVERAGE;
            case "Total":
                return TOTAL;
            default:
                return NONE;
        }
    }
}
