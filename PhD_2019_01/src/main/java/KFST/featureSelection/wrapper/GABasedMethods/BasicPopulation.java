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

import KFST.featureSelection.FitnessEvaluator;
import KFST.gui.featureSelection.wrapper.GABased.CrossOverType;
import KFST.gui.featureSelection.wrapper.GABased.MutationType;
import KFST.gui.featureSelection.wrapper.GABased.ReplacementType;
import KFST.gui.featureSelection.wrapper.GABased.SelectionType;
import java.lang.reflect.Array;

/**
 * The abstract class contains the main methods and fields that are used in all
 * GA-based feature selection methods. This class is used to implement a
 * population of individuals in GA algorithm.
 *
 * @param <IndividualType> the type of individual implemented in GA algorithm
 *
 * @author Sina Tabakhi
 */
public abstract class BasicPopulation<IndividualType> {

    protected IndividualType[] population;
    public static FitnessEvaluator fitnessEvaluator;
    public static int PROBLEM_DIMENSION;
    protected static int POPULATION_SIZE;
    protected static double CROSS_OVER_RATE;
    protected static double MUTATION_RATE;
    protected static SelectionType SELECTION_TYPE;
    protected static CrossOverType CROSSOVER_TYPE;
    protected static MutationType MUTATION_TYPE;
    protected static ReplacementType REPLACEMENT_TYPE;

    /**
     * initializes the parameters
     *
     * @param individual the type of individual implemented in GA algorithm
     */
    public BasicPopulation(Class<IndividualType> individual) {
        population = (IndividualType[]) Array.newInstance(individual, POPULATION_SIZE);
    }

    /**
     * This method initializes each individual in the population.
     */
    public abstract void initialization();

    /**
     * This method evaluates the fitness of each individual in the population by
     * predefined fitness function.
     */
    public abstract void evaluateFitness();

    /**
     * This method selects parents from the individuals of a population
     * according to their fitness that will recombine for next generation.
     */
    public abstract void operateSelection();

    /**
     * This method recombines (cross over) the parents to generate new
     * offsprings.
     */
    public abstract void operateCrossOver();

    /**
     * This method mutates new offsprings by changing the value of some genes in
     * them.
     */
    public abstract void operateMutation();

    /**
     * This method handles populations from one generation to the next
     * generation.
     */
    public abstract void operateGenerationReplacement();

    /**
     * This method returns the fittest individual in the population
     *
     * @return the fittest individual in the population
     */
    public abstract IndividualType getFittestIndividual();

    /**
     * This method returns an array of fitness values of individuals in a
     * population
     *
     * @return an array of fitness values of individuals
     */
    public abstract double[] getFitness();
}
