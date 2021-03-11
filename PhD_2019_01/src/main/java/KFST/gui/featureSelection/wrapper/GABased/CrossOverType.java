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
 * crossover.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class CrossOverType extends EnumType {

    public static final CrossOverType NONE = new CrossOverType("none", 0);
    public static final CrossOverType ONE_POINT_CROSS_OVER = new CrossOverType("One-point crossover", 1);
    public static final CrossOverType TWO_POINT_CROSS_OVER = new CrossOverType("Two-point crossover", 2);
    public static final CrossOverType UNIFORM_CROSS_OVER = new CrossOverType("Uniform crossover", 3);

    /**
     * Creates new CrossOverType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of crossover
     */
    private CrossOverType(String name) {
        super(name);
    }

    /**
     * Creates new CrossOverType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of crossover
     * @param value the value of crossover
     */
    private CrossOverType(String name, int value) {
        super(name, value);
    }

    /**
     * Returns the names of different crossover
     *
     * @return an array of names of crossover
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            ONE_POINT_CROSS_OVER.toString(),
            TWO_POINT_CROSS_OVER.toString(),
            UNIFORM_CROSS_OVER.toString()};
    }

    /**
     * Returns the common names of different crossover
     *
     * @return an array of common names of crossover
     */
    public static String[] commonAsList() {
        return new String[]{NONE.toString(),
            ONE_POINT_CROSS_OVER.toString(),
            TWO_POINT_CROSS_OVER.toString(),
            UNIFORM_CROSS_OVER.toString()};
    }

    /**
     * Converts the crossover name to CrossOverType
     *
     * @param type the name of crossover as string
     *
     * @return the crossover type
     */
    public static CrossOverType parse(String type) {
        switch (type) {
            case "One-point crossover":
                return ONE_POINT_CROSS_OVER;
            case "Two-point crossover":
                return TWO_POINT_CROSS_OVER;
            case "Uniform crossover":
                return UNIFORM_CROSS_OVER;
            default:
                return NONE;
        }
    }
}
