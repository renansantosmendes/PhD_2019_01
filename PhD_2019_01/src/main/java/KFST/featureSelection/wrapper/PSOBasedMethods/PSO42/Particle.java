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
package KFST.featureSelection.wrapper.PSOBasedMethods.PSO42;

import KFST.featureSelection.wrapper.PSOBasedMethods.BasicParticle;
import java.util.ArrayList;

/**
 * This java class is used to represent a particle in particle swarm
 * optimization version 4-2(PSO(4-2)) method in which the type of position and
 * velocity vectors are double.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.PSOBasedMethods.BasicParticle
 */
public class Particle extends BasicParticle<Double, Double> {

    public static double THETA;

    /**
     * initializes the parameters
     *
     * @param dimension the dimension of problem which equals to total number of
     * features in the dataset
     */
    public Particle(int dimension) {
        super(Double.class, Double.class, dimension);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int numSelectedFeatures() {
        return numSelectedFeatures(this.position);
    }

    /**
     * This method returns the number of selected features by the particle based
     * on personal best position vector.
     *
     * @param position the position vector of particle
     *
     * @return number of selected features by the particle
     */
    public static int numSelectedFeatures(Double[] position) {
        int count = 0;
        for (Double x : position) {
            if (x > THETA) {
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
        for (int i = 0; i < this.position.length; i++) {
            if (this.position[i] > THETA) {
                list.add(i);
            }
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}
