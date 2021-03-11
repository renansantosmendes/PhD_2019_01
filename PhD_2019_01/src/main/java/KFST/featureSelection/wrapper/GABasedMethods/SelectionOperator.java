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

import KFST.util.ArraysFunc;
import KFST.util.MathFunc;

/**
 * This java class is used to implement various selection operators for
 * selecting parents from the individuals of a population according to their
 * fitness that will recombine for next generation.
 * <p>
 * The methods in this class is contained brief description of the applications.
 *
 * @author Sina Tabakhi
 */
public class SelectionOperator {

    /**
     * selects an individual in a population according to their probabilities
     * using roulette wheel algorithm
     *
     * @param probabilities the array of probabilities of selecting individuals
     *
     * @return the index of selected individual
     */
    public static int rouletteWheel(double[] probabilities) {
        double randomNumber = Math.random();
        double sumInRoulette = 0;
        for (int indiv = 0; indiv < probabilities.length; indiv++) {
            sumInRoulette += probabilities[indiv];
            if (randomNumber <= sumInRoulette) {
                return indiv;
            }
        }

        return probabilities.length - 1;
    }

    /**
     * selects parents from the individuals of a population according to their
     * fitness values using fitness proportional selection method in which
     * roulette wheel algorithm is used for selecting each individual based on
     * their probabilities
     *
     * @param fitnessValues the array of fitness values of individuals in a
     * population
     * @param matingPoolSize the size of the mating pool
     *
     * @return an array of indices of the selected individuals
     */
    public static int[] fitnessProportionalSelection(double[] fitnessValues, int matingPoolSize) {
        int[] maitingPool = new int[matingPoolSize];
        int populationSize = fitnessValues.length;
        double[] probabilities = new double[populationSize];
        double sumFitness = MathFunc.sum(fitnessValues);

        /**
         * computes probability of each individual based on their fitness
         */
        for (int indiv = 0; indiv < populationSize; indiv++) {
            probabilities[indiv] = fitnessValues[indiv] / sumFitness;
        }

        for (int i = 0; i < matingPoolSize; i++) {
            maitingPool[i] = rouletteWheel(probabilities);
        }

        return maitingPool;
    }

    /**
     * selects parents from the individuals of a population according to their
     * fitness values using rank-based selection method in which roulette wheel
     * algorithm is used for selecting each individual based on their ranks
     *
     * @param fitnessValues the array of fitness values of individuals in a
     * population
     * @param matingPoolSize the size of the mating pool
     *
     * @return an array of indices of the selected individuals
     */
    public static int[] rankBasedSelection(double[] fitnessValues, int matingPoolSize) {
        int[] maitingPool = new int[matingPoolSize];
        int populationSize = fitnessValues.length;
        double[] probabilities = new double[populationSize];
        int[] indecesIndividual = ArraysFunc.sortWithIndex(fitnessValues, false);
        double sumRank;
        int[] rank = new int[populationSize];

        for (int i = 0; i < populationSize; i++) {
            rank[indecesIndividual[i]] = i + 1;
        }

        sumRank = MathFunc.sum(rank);

        /**
         * computes probability of each individual based on their ranks
         */
        for (int indiv = 0; indiv < populationSize; indiv++) {
            probabilities[indiv] = rank[indiv] / sumRank;
        }

        for (int i = 0; i < matingPoolSize; i++) {
            maitingPool[i] = rouletteWheel(probabilities);
        }

        return maitingPool;
    }
}
