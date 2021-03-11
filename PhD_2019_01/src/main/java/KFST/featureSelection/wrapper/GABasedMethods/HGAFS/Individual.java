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
package KFST.featureSelection.wrapper.GABasedMethods.HGAFS;

import KFST.featureSelection.wrapper.GABasedMethods.BasicIndividual;
import java.util.ArrayList;

/**
 * This java class is used to represent an individual in simple genetic
 * algorithm (Simple GA) method in which the type of gene vector is boolean.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.GABasedMethods.BasicIndividual
 */
public class Individual extends BasicIndividual<Boolean> {

    /**
     * initializes the parameters
     *
     * @param dimension the dimension of problem which equals to total number of
     * features in the dataset
     */
    public Individual(int dimension) {
        super(Boolean.class, dimension);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int numSelectedFeatures() {
        int count = 0;
        for (Boolean x : this.genes) {
            if (x) {
                count++;
            }
        }
        return count;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int[] selectedFeaturesSubset() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < this.genes.length; i++) {
            if (this.genes[i]) {
                list.add(i);
            }
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}
