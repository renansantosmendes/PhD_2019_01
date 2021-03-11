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
package KFST.featureSelection.wrapper.PSOBasedMethods;

import java.lang.reflect.Array;

/**
 * The abstract class contains the main methods and fields that are used in all
 * PSO-based feature selection methods. This class is used to represent a
 * particle in PSO algorithm.
 *
 * @param <PosType> the type of position vector of each particle
 * @param <VelType> the type of velocity vector of each particle
 *
 * @author Sina Tabakhi
 */
public abstract class BasicParticle<PosType, VelType> {

    public PosType[] position;
    public VelType[] velocity;
    private double fitness;
    public PosType[] pBest;
    private double pBestFitness;

    /**
     * initializes the parameters
     *
     * @param pos the type of position vector of each particle
     * @param vel the type of velocity vector of each particle
     * @param dimension the dimension of problem which equals to total number of
     * features in the dataset
     */
    public BasicParticle(Class<PosType> pos, Class<VelType> vel, Integer dimension) {
        position = (PosType[]) Array.newInstance(pos, dimension);
        velocity = (VelType[]) Array.newInstance(vel, dimension);
        pBest = (PosType[]) Array.newInstance(pos, dimension);
        pBestFitness = -1;
    }

    /**
     * This method returns the number of selected features by the particle based
     * on position vector.
     *
     * @return number of selected features by the particle
     */
    public abstract int numSelectedFeatures();

    /**
     * This method returns the indices of selected features by the particle
     * based on position vector.
     *
     * @return the array of indices of the selected features by the particle
     */
    public abstract int[] selectedFeaturesSubset();

    /**
     * This method returns the fitness value of the particle.
     *
     * @return the fitness value of the particle
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * This method sets the fitness value of the particle.
     *
     * @param fitness the fitness value of the particle
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * This method returns the fitness value of best position of the particle
     * (personal best)
     *
     * @return the fitness value of best position of the particle
     */
    public double getPBestFitness() {
        return pBestFitness;
    }

    /**
     * This method sets the fitness value of best position of the particle
     * (personal best)
     *
     * @param pBestFitness the fitness value of best position of the particle
     */
    public void setPBestFitness(double pBestFitness) {
        this.pBestFitness = pBestFitness;
    }

//    public void showPosition() {
//        for (PosType entry : position) {
//            System.out.print(entry + ",");
//        }
//        System.out.println("          = " + fitness);
//    }
//
//    public void showPBestPosition() {
//        for (PosType entry : pBest) {
//            System.out.print(entry + ",");
//        }
//        System.out.println("          = " + pBestFitness);
//    }
//
//    public void showVelocity() {
//        for (VelType entry : velocity) {
//            System.out.print(entry + ",");
//        }
//        System.out.println();
//    }
}
