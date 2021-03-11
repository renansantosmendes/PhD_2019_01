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
package KFST.featureSelection.wrapper.ACOBasedMethods.OptimalACO;

import KFST.featureSelection.wrapper.ACOBasedMethods.BasicAnt;
import KFST.featureSelection.wrapper.ACOBasedMethods.BasicColony;
import java.util.ArrayList;

/**
 * This java class is used to represent an ant in optimal ant colony
 * optimization (Optimal ACO) method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.ACOBasedMethods.BasicAnt
 */
public class Ant extends BasicAnt {

    /**
     * Counts number of successive steps that an ant is not able to decrease the
     * classification error rate
     */
    private int countSteps;

    /**
     * Initializes the parameters
     */
    public Ant() {
        super();
        countSteps = 0;
    }

    /**
     * This method returns the current count steps of the ant.
     *
     * @return the current count steps of the ant
     */
    public int getCountSteps() {
        return countSteps;
    }

    /**
     * This method sets the count steps of the ant.
     *
     * @param countSteps the input count steps
     */
    public void setCountSteps(int countSteps) {
        this.countSteps = countSteps;
    }

    /**
     * This method increases one step of the count steps of the ant.
     */
    public void increaseCountSteps() {
        countSteps++;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ArrayList<Integer> getFeasibleFeatureSet() {
        ArrayList<Integer> feasibleSet = new ArrayList<>();
        for (int i = 0; i < BasicColony.NUM_ORIGINAL_FEATURE; i++) {
            if (!featureSubset.contains(i)) {
                feasibleSet.add(i);
            }
        }

        return feasibleSet;
    }
}
