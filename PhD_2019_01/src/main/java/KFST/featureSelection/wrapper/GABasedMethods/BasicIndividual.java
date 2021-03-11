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
package KFST.featureSelection.wrapper.GABasedMethods;

import java.lang.reflect.Array;

/**
 * The abstract class contains the main methods and fields that are used in all
 * GA-based feature selection methods. This class is used to represent a
 * individual in GA algorithm.
 *
 * @param <GeneType> the type of genes of each individual
 *
 * @author Sina Tabakhi
 */
public abstract class BasicIndividual<GeneType> {

    public GeneType[] genes;
    private double fitness;

    /**
     * initializes the parameters
     *
     * @param gene the type of genes of each individual
     * @param dimension the dimension of problem which equals to total number of
     * features in the dataset
     */
    public BasicIndividual(Class<GeneType> gene, Integer dimension) {
        genes = (GeneType[]) Array.newInstance(gene, dimension);
    }

    /**
     * This method returns the number of selected features by the individual
     * based on its gene vector.
     *
     * @return number of selected features by the individual
     */
    public abstract int numSelectedFeatures();

    /**
     * This method returns the indices of selected features by the individual
     * based on its gene vector.
     *
     * @return the array of indices of the selected features by the individual
     */
    public abstract int[] selectedFeaturesSubset();

    /**
     * This method returns the fitness value of the individual.
     *
     * @return the fitness value of the individual
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * This method sets the fitness value of the individual.
     *
     * @param fitness the fitness value of the individual
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

//    public void showIndividual() {
//        for (GeneType entry : genes) {
//            System.out.print(entry + ",");
//        }
//        System.out.println("          = " + fitness);
//    }
}
