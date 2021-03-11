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
package KFST.featureSelection.wrapper.ACOBasedMethods;

import java.util.ArrayList;

/**
 * The abstract class contains the main methods and fields that are used in all
 * ACO-based feature selection methods. This class is used to represent an ant
 * in ACO algorithm.
 *
 * @author Sina Tabakhi
 */
public abstract class BasicAnt {

    protected ArrayList<Integer> featureSubset;
    private double fitness;

    /**
     * Initializes the parameters
     */
    public BasicAnt() {
        featureSubset = new ArrayList<>();
        fitness = 0;
    }

    /**
     * This method adds a specific feature to the current selected feature
     * subset by the ant.
     *
     * @param featIndex the index of the feature
     */
    public void addFeature(int featIndex) {
        featureSubset.add(featIndex);
    }

    /**
     * This method removes a specific feature of the current selected feature
     * subset by the ant.
     *
     * @param featIndex the index of the feature
     */
    public void removeFeature(int featIndex) {
        featureSubset.remove(new Integer(featIndex));
    }

    /**
     * This method returns the current selected feature subset by the ant.
     *
     * @return an array list of the selected feature subset by the ant
     */
    public ArrayList<Integer> getFeatureSubset() {
        return featureSubset;
    }

    /**
     * This method returns the feasible features that can be added to the
     * current solution of the ant.
     *
     * @return an array list of the feasible features
     */
    public abstract ArrayList<Integer> getFeasibleFeatureSet();

    /**
     * This method returns the size of current selected feature subset by the
     * ant.
     *
     * @return the size of current selected feature subset
     */
    public int getFeatureSubsetSize() {
        return featureSubset.size();
    }

    /**
     * This method clears the current selected feature subset by the ant.
     */
    public void emptyFeatureSubset() {
        featureSubset.clear();
    }

    /**
     * This method returns the fitness value of the ant.
     *
     * @return the fitness value of the ant
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * This method sets the fitness value of the ant.
     *
     * @param fitness the fitness value of the ant
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Returns a string representation of the ant.
     *
     * @return a string representation of the ant.
     */
    @Override
    public String toString() {
        String str = "Selected Subset = [ ";
        for (int i = 0; i < getFeatureSubsetSize(); i++) {
            str += featureSubset.get(i) + ",";
        }
        str = str.substring(0, str.length() - 1) + " ]";
        str += "        Fitness = " + getFitness();
        return str;
    }
}
