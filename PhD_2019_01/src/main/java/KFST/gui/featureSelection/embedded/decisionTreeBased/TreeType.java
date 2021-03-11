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
package KFST.gui.featureSelection.embedded.decisionTreeBased;

import KFST.featureSelection.EnumType;

/**
 * This java class is used to define the names of different implementation of
 * Tree.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.EnumType
 */
public final class TreeType extends EnumType {

    public static final TreeType NONE = new TreeType("none");
    public static final TreeType C45 = new TreeType("C4.5");
    public static final TreeType RANDOM_TREE = new TreeType("Random tree");
    public static final TreeType RANDOM_FOREST = new TreeType("Random forest");

    /**
     * Creates new TreeType. This method is called from within the constructor
     * to initialize the parameter.
     *
     * @param treeName the name of tree
     */
    private TreeType(String treeName) {
        super(treeName);
    }

    /**
     * Returns the names of different tree
     *
     * @return an array of names of tree
     */
    public static String[] asList() {
        return new String[]{C45.toString(),
            RANDOM_TREE.toString(),
            RANDOM_FOREST.toString()};
    }

    /**
     * Converts the tree name to TreeType
     *
     * @param type the name of tree as string
     *
     * @return the tree type
     */
    public static TreeType parse(String type) {
        switch (type) {
            case "C4.5":
                return C45;
            case "Random tree":
                return RANDOM_TREE;
            case "Random forest":
                return RANDOM_FOREST;
            default:
                return NONE;
        }
    }
}
