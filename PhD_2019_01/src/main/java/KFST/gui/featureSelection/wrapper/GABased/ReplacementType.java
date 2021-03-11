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
package KFST.gui.featureSelection.wrapper.GABased;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of different implementation of
 * generation replacement.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class ReplacementType extends EnumType {

    public static final ReplacementType NONE = new ReplacementType("none", 0);
    public static final ReplacementType TOTAL_REPLACEMENT = new ReplacementType("Total replacement", 1);

    /**
     * Creates new ReplacementType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of replacement
     */
    private ReplacementType(String name) {
        super(name);
    }

    /**
     * Creates new ReplacementType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of replacement
     * @param value the value of replacement
     */
    private ReplacementType(String name, int value) {
        super(name, value);
    }

    /**
     * Returns the names of different replacement
     *
     * @return an array of names of replacement
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            TOTAL_REPLACEMENT.toString()};
    }

    /**
     * Returns the common names of different replacement
     *
     * @return an array of common names of replacement
     */
    public static String[] commonAsList() {
        return new String[]{NONE.toString(),
            TOTAL_REPLACEMENT.toString()};
    }

    /**
     * Converts the replacement name to ReplacementType
     *
     * @param type the name of replacement as string
     *
     * @return the replacement type
     */
    public static ReplacementType parse(String type) {
        switch (type) {
            case "Total replacement":
                return TOTAL_REPLACEMENT;
            default:
                return NONE;
        }
    }
}
