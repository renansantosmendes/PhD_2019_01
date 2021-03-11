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
 * selection.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class SelectionType extends EnumType {

    public static final SelectionType NONE = new SelectionType("none", 0);
    public static final SelectionType FITNESS_PROPORTIONAL_SELECTION = new SelectionType("Fitness proportional selection", 1);
    public static final SelectionType RANK_BASED_SELECTION = new SelectionType("Rank-based selection", 2);

    /**
     * Creates new SelectionType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of selection
     */
    private SelectionType(String name) {
        super(name);
    }

    /**
     * Creates new SelectionType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of selection
     * @param value the value of selection
     */
    private SelectionType(String name, int value) {
        super(name, value);
    }

    /**
     * Returns the names of different selection
     *
     * @return an array of names of selection
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            FITNESS_PROPORTIONAL_SELECTION.toString(),
            RANK_BASED_SELECTION.toString()};
    }

    /**
     * Returns the common names of different selection
     *
     * @return an array of common names of selection
     */
    public static String[] commonAsList() {
        return new String[]{NONE.toString(),
            FITNESS_PROPORTIONAL_SELECTION.toString(),
            RANK_BASED_SELECTION.toString()};
    }

    /**
     * Converts the selection name to SelectionType
     *
     * @param type the name of selection as string
     *
     * @return the selection type
     */
    public static SelectionType parse(String type) {
        switch (type) {
            case "Fitness proportional selection":
                return FITNESS_PROPORTIONAL_SELECTION;
            case "Rank-based selection":
                return RANK_BASED_SELECTION;
            default:
                return NONE;
        }
    }
}
