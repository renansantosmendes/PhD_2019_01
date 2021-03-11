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
package KFST.featureSelection.wrapper.GABasedMethods.SimpleGA;

import KFST.featureSelection.wrapper.GABasedMethods.BasicPopulation;
import KFST.featureSelection.wrapper.GABasedMethods.CrossoverOperator;
import KFST.featureSelection.wrapper.GABasedMethods.MutationOperator;
import KFST.featureSelection.wrapper.GABasedMethods.SelectionOperator;
import KFST.gui.featureSelection.wrapper.GABased.CrossOverType;
import KFST.gui.featureSelection.wrapper.GABased.MutationType;
import KFST.gui.featureSelection.wrapper.GABased.ReplacementType;
import KFST.gui.featureSelection.wrapper.GABased.SelectionType;
import KFST.result.performanceMeasure.Criteria;
import java.util.Arrays;
import java.util.Random;

/**
 * This java class is used to implement a population of individuals in simple
 * genetic algorithm (Simple GA) method in which the type of individual is
 * extended from Individual class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.GABasedMethods.BasicPopulation
 */
public class Population extends BasicPopulation<Individual> {

    private int seedValue = 0;
    private Random rand = new Random(seedValue);
    private Individual[] matingPool;

    /**
     * initializes the parameters
     */
    public Population() {
        super(Individual.class);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Individual(PROBLEM_DIMENSION);
        }
    }

    /**
     * {@inheritDoc }
     * Each individual is randomly initialized.
     */
    @Override
    public void initialization() {
        for (int indiv = 0; indiv < POPULATION_SIZE; indiv++) {
            for (int gene = 0; gene < PROBLEM_DIMENSION; gene++) {
                population[indiv].genes[gene] = rand.nextDouble() > 0.5;
            }

            if (population[indiv].numSelectedFeatures() == 0) {
                population[indiv].genes[rand.nextInt(PROBLEM_DIMENSION)] = true;
            }
        }
    }

    /**
     * {@inheritDoc }
     * K-fold cross validation on training set is used for evaluating the
     * classification performance of selected feature subset by each individual.
     */
    @Override
    public void evaluateFitness() {
        for (int indiv = 0; indiv < POPULATION_SIZE; indiv++) {
            if (population[indiv].numSelectedFeatures() > 0) {
                Criteria criteria = fitnessEvaluator.crossValidation(population[indiv].selectedFeaturesSubset());
                population[indiv].setFitness(criteria.getAccuracy());
            } else {
                population[indiv].setFitness(0);
            }
        }
    }

    /**
     * {@inheritDoc }
     * <p>
     * The selection type is selected based on selectionType by user.
     */
    @Override
    public void operateSelection() {
        int[] selectedParentIndices = new int[POPULATION_SIZE];
        matingPool = new Individual[POPULATION_SIZE];

        for (int indiv = 0; indiv < matingPool.length; indiv++) {
            matingPool[indiv] = new Individual(PROBLEM_DIMENSION);
        }

        if (Population.SELECTION_TYPE == SelectionType.FITNESS_PROPORTIONAL_SELECTION) {
            selectedParentIndices = SelectionOperator.fitnessProportionalSelection(this.getFitness(), POPULATION_SIZE);
        } else if (Population.SELECTION_TYPE == SelectionType.RANK_BASED_SELECTION) {
            selectedParentIndices = SelectionOperator.rankBasedSelection(this.getFitness(), POPULATION_SIZE);
        }

        for (int indiv = 0; indiv < POPULATION_SIZE; indiv++) {
            matingPool[indiv].genes = Arrays.copyOf(population[selectedParentIndices[indiv]].genes, PROBLEM_DIMENSION);
            matingPool[indiv].setFitness(population[selectedParentIndices[indiv]].getFitness());
        }
    }

    /**
     * {@inheritDoc }
     * <p>
     * The crossover type is selected based on crossoverType by user.
     */
    @Override
    public void operateCrossOver() {
        if (Population.CROSSOVER_TYPE == CrossOverType.ONE_POINT_CROSS_OVER) {
            for (int indiv = 0; indiv < matingPool.length - 1; indiv += 2) {
                if (Math.random() < CROSS_OVER_RATE) {
                    CrossoverOperator.onePointCrossover(matingPool[indiv].genes, matingPool[indiv + 1].genes);
                }
            }
        } else if (Population.CROSSOVER_TYPE == CrossOverType.TWO_POINT_CROSS_OVER) {
            for (int indiv = 0; indiv < matingPool.length - 1; indiv += 2) {
                if (Math.random() < CROSS_OVER_RATE) {
                    CrossoverOperator.twoPointCrossover(matingPool[indiv].genes, matingPool[indiv + 1].genes);
                }
            }
        } else if (Population.CROSSOVER_TYPE == CrossOverType.UNIFORM_CROSS_OVER) {
            for (int indiv = 0; indiv < matingPool.length - 1; indiv += 2) {
                CrossoverOperator.uniformCrossover(matingPool[indiv].genes, matingPool[indiv + 1].genes, CROSS_OVER_RATE);
            }
        }
    }

    /**
     * {@inheritDoc }
     * <p>
     * The mutation type is selected based on mutationType by user.
     */
    @Override
    public void operateMutation() {
        if (Population.MUTATION_TYPE == MutationType.BITWISE_MUTATION) {
            for (Individual indiv : matingPool) {
                MutationOperator.bitwiseMutation(indiv.genes, MUTATION_RATE);
            }
        }
    }

    /**
     * {@inheritDoc }
     * <p>
     * The replacement type is selected based on replacementType by user.
     */
    @Override
    public void operateGenerationReplacement() {
        if (Population.REPLACEMENT_TYPE == ReplacementType.TOTAL_REPLACEMENT) {
            population = matingPool;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Individual getFittestIndividual() {
        int fittestIndex = 0;
        double fittestValue = population[0].getFitness();

        for (int indiv = 1; indiv < POPULATION_SIZE; indiv++) {
            if (population[indiv].getFitness() > fittestValue) {
                fittestValue = population[indiv].getFitness();
                fittestIndex = indiv;
            }
        }
        return population[fittestIndex];
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public double[] getFitness() {
        double[] fitnessValues = new double[POPULATION_SIZE];

        for (int indiv = 0; indiv < POPULATION_SIZE; indiv++) {
            fitnessValues[indiv] = population[indiv].getFitness();
        }

        return fitnessValues;
    }
}
