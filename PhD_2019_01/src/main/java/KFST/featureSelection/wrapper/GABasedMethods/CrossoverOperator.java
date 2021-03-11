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

import java.util.Random;

/**
 * This java class is used to implement various crossover operators for
 * recombining the two parents to generate new offsprings
 * <p>
 * The methods in this class is contained brief description of the applications.
 *
 * @author Sina Tabakhi
 */
public class CrossoverOperator {

    /**
     * changes the values of the two elements in the two arrays identified by
     * index1 and index2
     *
     * @param <GeneType> the type of elements in the input arrays
     * @param parent1 the first input array
     * @param index1 the index of element in the parent1 array that should be
     * changed
     * @param parent2 the second input array
     * @param index2 the index of element in the parent2 array that should be
     * changed
     */
    public static <GeneType> void swap(GeneType[] parent1, int index1, GeneType[] parent2, int index2) {
        GeneType temp = parent1[index1];
        parent1[index1] = parent2[index2];
        parent2[index2] = temp;
    }

    /**
     * changes the values of the two elements in the two arrays identified by
     * index1 and index2
     *
     * @param parent1 the first input array
     * @param index1 the index of element in the parent1 array that should be
     * changed
     * @param parent2 the second input array
     * @param index2 the index of element in the parent2 array that should be
     * changed
     */
    public static void swap(boolean[] parent1, int index1, boolean[] parent2, int index2) {
        boolean temp = parent1[index1];
        parent1[index1] = parent2[index2];
        parent2[index2] = temp;
    }

    /**
     * changes the values of the two elements in the two arrays identified by
     * index1 and index2
     *
     * @param parent1 the first input array
     * @param index1 the index of element in the parent1 array that should be
     * changed
     * @param parent2 the second input array
     * @param index2 the index of element in the parent2 array that should be
     * changed
     */
    public static void swap(double[] parent1, int index1, double[] parent2, int index2) {
        double temp = parent1[index1];
        parent1[index1] = parent2[index2];
        parent2[index2] = temp;
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * one-point crossover
     *
     * @param <GeneType> the type of elements in the input arrays
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public static <GeneType> void onePointCrossover(GeneType[] parent1, GeneType[] parent2) {
        int point = (new Random()).nextInt(parent1.length);
        for (int gene = point; gene < parent1.length; gene++) {
            swap(parent1, gene, parent2, gene);
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * one-point crossover
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public static void onePointCrossover(boolean[] parent1, boolean[] parent2) {
        int point = (new Random()).nextInt(parent1.length);
        for (int gene = point; gene < parent1.length; gene++) {
            swap(parent1, gene, parent2, gene);
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * one-point crossover
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public static void onePointCrossover(double[] parent1, double[] parent2) {
        int point = (new Random()).nextInt(parent1.length);
        for (int gene = point; gene < parent1.length; gene++) {
            swap(parent1, gene, parent2, gene);
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * two-point crossover
     *
     * @param <GeneType> the type of elements in the input arrays
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public static <GeneType> void twoPointCrossover(GeneType[] parent1, GeneType[] parent2) {
        int point1 = (new Random()).nextInt(parent1.length);
        int point2 = (new Random()).nextInt(parent1.length);

        if (point2 < point1) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        for (int gene = point1; gene < point2; gene++) {
            swap(parent1, gene, parent2, gene);
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * two-point crossover
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public static void twoPointCrossover(boolean[] parent1, boolean[] parent2) {
        int point1 = (new Random()).nextInt(parent1.length);
        int point2 = (new Random()).nextInt(parent1.length);

        if (point2 < point1) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        for (int gene = point1; gene < point2; gene++) {
            swap(parent1, gene, parent2, gene);
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * two-point crossover
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public static void twoPointCrossover(double[] parent1, double[] parent2) {
        int point1 = (new Random()).nextInt(parent1.length);
        int point2 = (new Random()).nextInt(parent1.length);

        if (point2 < point1) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        for (int gene = point1; gene < point2; gene++) {
            swap(parent1, gene, parent2, gene);
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * uniform crossover
     *
     * @param <GeneType> the type of elements in the input arrays
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @param prob the probability of crossover operation
     */
    public static <GeneType> void uniformCrossover(GeneType[] parent1, GeneType[] parent2, double prob) {
        for (int gene = 0; gene < parent1.length; gene++) {
            if (Math.random() <= prob) {
                swap(parent1, gene, parent2, gene);
            }
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * uniform crossover
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @param prob the probability of crossover operation
     */
    public static void uniformCrossover(boolean[] parent1, boolean[] parent2, double prob) {
        for (int gene = 0; gene < parent1.length; gene++) {
            if (Math.random() <= prob) {
                swap(parent1, gene, parent2, gene);
            }
        }
    }

    /**
     * recombines (cross over) the two parents to generate new offsprings using
     * uniform crossover
     *
     * @param parent1 the first parent
     * @param parent2 the second parent
     * @param prob the probability of crossover operation
     */
    public static void uniformCrossover(double[] parent1, double[] parent2, double prob) {
        for (int gene = 0; gene < parent1.length; gene++) {
            if (Math.random() <= prob) {
                swap(parent1, gene, parent2, gene);
            }
        }
    }
}
