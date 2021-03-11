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
package KFST.featureSelection.wrapper.PSOBasedMethods.BPSO;

import KFST.featureSelection.wrapper.PSOBasedMethods.BasicSwarm;
import KFST.result.performanceMeasure.Criteria;
import KFST.util.MathFunc;
import java.util.Arrays;
import java.util.Random;

/**
 * This java class is used to implement a swarm of particles in binary particle
 * swarm optimization (BPSO) method in which the type of position vector is
 * boolean and the type of particle is extended from Particle class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.PSOBasedMethods.BasicSwarm
 */
public class Swarm extends BasicSwarm<Boolean, Particle> {

    private int seedValue = 0;
    private Random rand = new Random(seedValue);

    /**
     * initializes the parameters
     */
    public Swarm() {
        super(Boolean.class, Particle.class);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Particle(PROBLEM_DIMENSION);
        }
    }

    /**
     * {@inheritDoc }
     * Each particle is randomly initialized in the predefined ranges of values.
     */
    @Override
    public void initialization() {
        for (int par = 0; par < POPULATION_SIZE; par++) {
            for (int dim = 0; dim < PROBLEM_DIMENSION; dim++) {
                population[par].position[dim] = MathFunc.generateRandNum(START_POS_INTERVAL, END_POS_INTERVAL, rand) > 0.5;
                population[par].velocity[dim] = MathFunc.generateRandNum(MIN_VELOCITY, MAX_VELOCITY, rand);
            }

            if (population[par].numSelectedFeatures() == 0) {
                population[par].position[rand.nextInt(PROBLEM_DIMENSION)] = true;
            }
        }
    }

    /**
     * {@inheritDoc }
     * K-fold cross validation on training set is used for evaluating the
     * classification performance of selected feature subset by each particle.
     */
    @Override
    public void evaluateFitness() {
        for (int par = 0; par < POPULATION_SIZE; par++) {
            if (population[par].numSelectedFeatures() > 0) {
                Criteria criteria = fitnessEvaluator.crossValidation(population[par].selectedFeaturesSubset());
                population[par].setFitness(criteria.getAccuracy());
            } else {
                population[par].setFitness(0);
            }
        }
    }

    /**
     * {@inheritDoc }
     * Personal best position is updated when the classification performance of
     * the particle's new position is better than personal best.
     */
    @Override
    public void updatePersonalBest() {
        for (int par = 0; par < POPULATION_SIZE; par++) {
            if (population[par].getFitness() > population[par].getPBestFitness()) {
                population[par].pBest = Arrays.copyOf(population[par].position, PROBLEM_DIMENSION);
                population[par].setPBestFitness(population[par].getFitness());
            }
        }
    }

    /**
     * {@inheritDoc }
     * Global best position is updated when the classification performance of
     * any personal best position of the particles is better than global best.
     */
    @Override
    public void updateGlobalBest() {
        int maxIndex = 0;
        double maxValue = population[0].getPBestFitness();
        for (int par = 1; par < POPULATION_SIZE; par++) {
            if (population[par].getPBestFitness() > maxValue) {
                maxIndex = par;
                maxValue = population[par].getPBestFitness();
            }
        }

        if (maxValue > this.getGBestFitness()) {
            this.setGBest(Arrays.copyOf(population[maxIndex].pBest, PROBLEM_DIMENSION));
            this.setGBestFitness(maxValue);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateParticleVelocity() {
        for (int par = 0; par < POPULATION_SIZE; par++) {
            for (int dim = 0; dim < PROBLEM_DIMENSION; dim++) {
                double firstPart = INERTIA_WEIGHT * population[par].velocity[dim];
                double secondPart = C1 * rand.nextDouble() * subtractBoolean(population[par].pBest[dim], population[par].position[dim]);
                double thirdPart = C2 * rand.nextDouble() * subtractBoolean(gBest[dim], population[par].position[dim]);
                population[par].velocity[dim] = firstPart + secondPart + thirdPart;
                if (population[par].velocity[dim] > MAX_VELOCITY) {
                    population[par].velocity[dim] = MAX_VELOCITY;
                } else if (population[par].velocity[dim] < MIN_VELOCITY) {
                    population[par].velocity[dim] = MIN_VELOCITY;
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateParticlePosition() {
        for (int par = 0; par < POPULATION_SIZE; par++) {
            for (int dim = 0; dim < PROBLEM_DIMENSION; dim++) {
                population[par].position[dim] = rand.nextDouble() < sigmoidFunc(population[par].velocity[dim]);
            }
        }
    }

    /**
     * This method subtracts two boolean values.
     * <p>
     * If the input value equals to true, the result will be considered as 1,
     * otherwise 0.
     *
     * @param num1 the first input value
     * @param num2 the second input value
     *
     * @return the result of subtraction
     */
    private int subtractBoolean(boolean num1, boolean num2) {
        int intNum1 = num1 ? 1 : 0;
        int intNum2 = num2 ? 1 : 0;
        return intNum1 - intNum2;
    }

    /**
     * This method transforms a input value to the range of (0,1) using sigmoid
     * function.
     *
     * @param value the input value that will be transformed
     *
     * @return the transformed value of the input value
     */
    private double sigmoidFunc(double value) {
        return 1.0 / (1.0 + Math.pow(Math.E, -value));
    }
}
