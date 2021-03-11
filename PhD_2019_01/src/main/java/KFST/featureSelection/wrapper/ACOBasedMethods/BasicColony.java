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

import KFST.featureSelection.FitnessEvaluator;
import KFST.featureSelection.wrapper.ACOBasedMethods.OptimalACO.GraphRepresentation;
import KFST.result.performanceMeasure.Criteria;
import java.lang.reflect.Array;

/**
 * The abstract class contains the main methods and fields that are used in all
 * ACO-based feature selection methods. This class is used to implement a colony
 * of ants in ACO algorithm.
 *
 * @param <AntType> the type of ant implemented in ACO algorithm
 *
 * @author Sina Tabakhi
 */
public abstract class BasicColony<AntType> {

    protected AntType[] colony;
    public static FitnessEvaluator fitnessEvaluator;
    public static GraphRepresentation graphRepresentation;
    public static int NUM_ORIGINAL_FEATURE;
    protected static int COLONY_SIZE;
    protected static double INIT_PHEROMONE_VALUE;
    protected static double ALPHA;
    protected static double BETA;
    protected static double RHO;

    /**
     * Initializes the parameters
     *
     * @param ant the type of ant implemented in ACO algorithm
     */
    public BasicColony(Class<AntType> ant) {
        colony = (AntType[]) Array.newInstance(ant, COLONY_SIZE);
    }

    /**
     * This method initializes the problem parameters.
     */
    public abstract void initialization();

    /**
     * This method places any ant randomly to one feature as their initial
     * states.
     */
    public abstract void setInitialState();

    /**
     * This method evaluates the fitness of each ant in the colony by predefined
     * fitness function.
     *
     * @param antIndex index of the ant in the colony
     *
     * @return Criteria values of the fitness function
     * @see KFST.result.performanceMeasure.Criteria
     */
    public abstract Criteria evaluateCurrentSolution(int antIndex);

    /**
     * This method selects the next state and adds it to the current selected
     * feature subset by using state transition rule that is combination of
     * heuristic desirability and pheromone levels.
     *
     * @param antIndex index of the ant in the colony
     */
    public abstract void operateStateTransitionRule(int antIndex);

    /**
     * This method constructs solutions completely of each ant in the colony by
     * applying state transition rule repeatedly.
     */
    public abstract void constructSolution();

    /**
     * This method updates the current pheromone values by decreasing pheromone
     * concentrations and then deposit the quantity of pheromone by ants.
     */
    public abstract void operatePheromoneUpdateRule();

    /**
     * This method returns the best ant in the colony
     *
     * @return the best ant in the colony
     */
    public abstract AntType getBestAnt();

    /**
     * Returns a string representation of the colony.
     *
     * @return a string representation of the colony.
     */
    @Override
    public String toString() {
        String str = "Colony:\n";
        for (int ant = 0; ant < COLONY_SIZE; ant++) {
            str += "    ant(" + (ant + 1) + ")    " + colony[ant].toString() + "\n";
        }
        return str;
    }
}
