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
 * mutation.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class MutationType extends EnumType {

    public static final MutationType NONE = new MutationType("none", 0);
    public static final MutationType BITWISE_MUTATION = new MutationType("Bitwise mutation", 1);

    /**
     * Creates new MutationType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of mutation
     */
    private MutationType(String name) {
        super(name);
    }

    /**
     * Creates new MutationType. This method is called from within the
     * constructor to initialize the parameter.
     *
     * @param name the name of mutation
     * @param value the value of mutation
     */
    private MutationType(String name, int value) {
        super(name, value);
    }

    /**
     * Returns the names of different mutation
     *
     * @return an array of names of mutation
     */
    public static String[] asList() {
        return new String[]{NONE.toString(),
            BITWISE_MUTATION.toString()};
    }

    /**
     * Returns the common names of different mutation
     *
     * @return an array of common names of mutation
     */
    public static String[] commonAsList() {
        return new String[]{NONE.toString(),
            BITWISE_MUTATION.toString()};
    }

    /**
     * Converts the mutation name to MutationType
     *
     * @param type the name of mutation as string
     *
     * @return the mutation type
     */
    public static MutationType parse(String type) {
        switch (type) {
            case "Bitwise mutation":
                return BITWISE_MUTATION;
            default:
                return NONE;
        }
    }
}
