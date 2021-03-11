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
package KFST.featureSelection.embedded.TreeBasedMethods;

import KFST.featureSelection.embedded.EmbeddedApproach;
import KFST.gui.featureSelection.embedded.decisionTreeBased.TreeType;
import weka.core.Instances;

/**
 * The abstract class contains the main methods and fields that are used in all
 * Tree-based feature selection methods. This class inherits from
 * EmbeddedApproach class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.embedded.EmbeddedApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public abstract class TreeBasedMethods extends EmbeddedApproach {

    protected final String TEMP_PATH;
    protected final TreeType TREE_TYPE;

    /**
     * initializes the parameters
     *
     * @param arguments array of parameter contains (<code>path</code>,
     * <code>tree type</code>) in which <code><b><i>path</i></b></code> is the
     * path of the project, and <code><b><i>tree type</i></b></code> is the type
     * of tree
     */
    public TreeBasedMethods(Object... arguments) {
        super((String) arguments[0]);
        TREE_TYPE = (TreeType) arguments[1];
        TEMP_PATH = PROJECT_PATH + "Temp\\";
    }

    /**
     * find the feature subset from the nodes of the created tree
     *
     * @param tree the generated tree based on the train set
     */
    abstract protected void selectedFeatureSubset(String tree);

    /**
     * generates a classifier using input data
     *
     * @param dataTrain the data to train the classifier
     *
     * @return the output of the generated classifier
     */
    abstract protected String buildClassifier(Instances dataTrain);
}
