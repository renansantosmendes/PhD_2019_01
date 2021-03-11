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

import KFST.featureSelection.FitnessEvaluator;
import java.lang.reflect.Array;

/**
 * The abstract class contains the main methods and fields that are used in all
 * PSO-based feature selection methods. This class is used to implement a swarm
 * of particles in PSO algorithm.
 *
 * @param <PosType> the type of position vector of each particle
 * @param <ParType> the type of particle implemented in PSO algorithm
 *
 * @author Sina Tabakhi
 */
public abstract class BasicSwarm<PosType, ParType> {

    protected ParType[] population;
    protected PosType[] gBest;
    private double gBestFitness;
    public static FitnessEvaluator fitnessEvaluator;
    public static int PROBLEM_DIMENSION;
    protected static int POPULATION_SIZE;
    protected static double INERTIA_WEIGHT;
    protected static double C1;
    protected static double C2;
    protected static double START_POS_INTERVAL;
    protected static double END_POS_INTERVAL;
    protected static double MIN_VELOCITY;
    protected static double MAX_VELOCITY;

    /**
     * initializes the parameters
     *
     * @param pos the type of position vector of each particle
     * @param par the type of particle implemented in PSO algorithm
     */
    public BasicSwarm(Class<PosType> pos, Class<ParType> par) {
        population = (ParType[]) Array.newInstance(par, POPULATION_SIZE);
        gBest = (PosType[]) Array.newInstance(pos, PROBLEM_DIMENSION);
        gBestFitness = -1;
    }

    /**
     * This method initializes the position and velocity vectors of each
     * particle in the swarm.
     */
    public abstract void initialization();

    /**
     * This method evaluates the fitness of each particle in the swarm by
     * predefined fitness function.
     */
    public abstract void evaluateFitness();

    /**
     * This method updates the best position (personal best) of each particle in
     * the swarm.
     */
    public abstract void updatePersonalBest();

    /**
     * This method updates the global best position (global best) of the swarm.
     */
    public abstract void updateGlobalBest();

    /**
     * This method updates the velocity vector of each particle in the swarm.
     */
    public abstract void updateParticleVelocity();

    /**
     * This method updates the position vector of each particle in the swarm.
     */
    public abstract void updateParticlePosition();

    /**
     * This method returns the best position in the swarm (global best)
     *
     * @return the best position in the swarm
     */
    public PosType[] getGBest() {
        return gBest;
    }

    /**
     * This method sets the best position in the swarm (global best)
     *
     * @param gBest the best position in the swarm
     */
    public void setGBest(PosType[] gBest) {
        this.gBest = gBest;
    }

    /**
     * This method returns the fitness value of best position in the swarm
     * (global best)
     *
     * @return the fitness value of best position in the swarm
     */
    public double getGBestFitness() {
        return gBestFitness;
    }

    /**
     * This method sets the fitness value of best position in the swarm (global
     * best)
     *
     * @param gBestFitness the fitness value of best position in the swarm
     */
    public void setGBestFitness(double gBestFitness) {
        this.gBestFitness = gBestFitness;
    }

//    public void showPBestPosition() {
//        for (PosType entry : gBest) {
//            System.out.print(entry + ",");
//        }
//        System.out.println("          = " + gBestFitness);
//    }
}
