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
package KFST.featureSelection.wrapper.ACOBasedMethods.OptimalACO;

import KFST.featureSelection.wrapper.ACOBasedMethods.BasicColony;
import KFST.featureSelection.wrapper.GABasedMethods.SelectionOperator;
import KFST.result.performanceMeasure.Criteria;
import KFST.util.ArraysFunc;
import KFST.util.MathFunc;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This java class is used to implement a colony of ants in optimal ant colony
 * optimization (Optimal ACO) method in which the type of ant is extended from
 * Ant class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.GABasedMethods.BasicPopulation
 */
public class Colony extends BasicColony<Ant> {

    public static double PHI;

    /**
     * Initializes the parameters
     *
     * @param phi the phi parameter used in the pheromone update rule for
     * controlling the relative weight of classifier performance and feature
     * subset length
     */
    public Colony(double phi) {
        super(Ant.class);
        Colony.PHI = phi;
        for (int i = 0; i < COLONY_SIZE; i++) {
            colony[i] = new Ant();
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void initialization() {
        graphRepresentation.fillPheromoneArray(INIT_PHEROMONE_VALUE);
//        System.out.println(graphRepresentation.toString());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setInitialState() {
        Boolean[] checkListFeatures = new Boolean[NUM_ORIGINAL_FEATURE];
        Arrays.fill(checkListFeatures, false);

        for (int ant = 0; ant < COLONY_SIZE; ant++) {
            colony[ant].emptyFeatureSubset();
            colony[ant].setFitness(0);
            colony[ant].setCountSteps(0);
        }

        for (int feat = 0; feat < COLONY_SIZE; feat++) {
            checkListFeatures[feat] = true;
        }
        MathFunc.randomize(checkListFeatures);

        for (int feat = 0, ant = 0; feat < NUM_ORIGINAL_FEATURE; feat++) {
            if (checkListFeatures[feat]) {
                colony[ant++].addFeature(feat);
            }
        }

//        for (int ant = 0; ant < COLONY_SIZE; ant++) {
//            System.out.println(colony[ant].toString());
//        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Criteria evaluateCurrentSolution(int antIndex) {
        int[] featureSet = ArraysFunc.convertArrayListToInt(colony[antIndex].getFeatureSubset());
        ArraysFunc.sortArray1D(featureSet, false);
        
        return fitnessEvaluator.crossValidation(featureSet);
    }

    /**
     * {@inheritDoc }
     * <p>
     * Classifier performance is used as heuristic desirability.
     */
    @Override
    public void operateStateTransitionRule(int antIndex) {
        ArrayList<Integer> feasibleFeatureSet = colony[antIndex].getFeasibleFeatureSet();
        double[] probabilities = new double[feasibleFeatureSet.size()];
        double[] fitnessValues = new double[feasibleFeatureSet.size()];
        double sumProb = 0;

        for (int feat = 0; feat < probabilities.length; feat++) {
            int currFeasible = feasibleFeatureSet.get(feat);
            colony[antIndex].addFeature(currFeasible);
            
            fitnessValues[feat] = evaluateCurrentSolution(antIndex).getAccuracy() / 100.0;
            double pheromone = graphRepresentation.getPheromone(0, currFeasible);
            probabilities[feat] = Math.pow(pheromone, ALPHA) * Math.pow(fitnessValues[feat], BETA);
            sumProb += probabilities[feat];
            
            colony[antIndex].removeFeature(currFeasible);
        }

        for (int feat = 0; feat < probabilities.length; feat++) {
            probabilities[feat] = probabilities[feat] / sumProb;
        }

        int selectedIndex = SelectionOperator.rouletteWheel(probabilities);
        int selectedFeat = feasibleFeatureSet.get(selectedIndex);
        colony[antIndex].addFeature(selectedFeat);
        colony[antIndex].setFitness(fitnessValues[selectedIndex]);

//        System.out.println(colony[antIndex].toString());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void constructSolution() {
        for (int ant = 0; ant < COLONY_SIZE; ant++) {
            while (colony[ant].getFeatureSubsetSize() < NUM_ORIGINAL_FEATURE
                    && colony[ant].getCountSteps() < 10) {
                double beforeFitness = colony[ant].getFitness();
                operateStateTransitionRule(ant);
                double afterFitness = colony[ant].getFitness();
                if (afterFitness <= beforeFitness) {
                    colony[ant].increaseCountSteps();
                } else {
                    colony[ant].setCountSteps(0);
                }
            }
        }

//        System.out.println(this.toString());
    }

    /**
     * {@inheritDoc }
     * <p>
     * Best ant deposits additional pheromone on nodes of the best solution.
     */
    @Override
    public void operatePheromoneUpdateRule() {
        /**
         * Pheromone evaporation on all nodes is triggered
         */
        for (int feat = 0; feat < NUM_ORIGINAL_FEATURE; feat++) {
            double evaporatedValue = (1.0 - RHO) * graphRepresentation.getPheromone(0, feat);
            graphRepresentation.setPheromone(evaporatedValue, 0, feat);
        }

        /**
         * Best ant deposits additional pheromone on nodes of the best solution
         */
        Ant bestAnt = getBestAnt();
        double firstValue = PHI * bestAnt.getFitness();
        double secondValue = ((1.0 - PHI) * (NUM_ORIGINAL_FEATURE - bestAnt.getFeatureSubsetSize())) / NUM_ORIGINAL_FEATURE;
        ArrayList<Integer> featureSet = bestAnt.getFeatureSubset();
        for (int i = 0; i < featureSet.size(); i++) {
            double prevValue = graphRepresentation.getPheromone(0, featureSet.get(i));
            graphRepresentation.setPheromone(prevValue + firstValue + secondValue, 0, featureSet.get(i));
        }

        /**
         * Each ant deposit a quantity of pheromone on each node that it has
         * used
         */
        for (int ant = 0; ant < COLONY_SIZE; ant++) {
            firstValue = PHI * colony[ant].getFitness();
            secondValue = ((1.0 - PHI) * (NUM_ORIGINAL_FEATURE - colony[ant].getFeatureSubsetSize())) / NUM_ORIGINAL_FEATURE;
            featureSet = colony[ant].getFeatureSubset();
            for (int i = 0; i < featureSet.size(); i++) {
                double prevValue = graphRepresentation.getPheromone(0, featureSet.get(i));
                graphRepresentation.setPheromone(prevValue + firstValue + secondValue, 0, featureSet.get(i));
            }
        }
    }

    /**
     * {@inheritDoc }
     * <p>
     * Best ant is selected based on classifier performance and length of
     * selected subsets.
     */
    @Override
    public Ant getBestAnt() {
        int fittestIndex = 0;
        double fittestValue = colony[0].getFitness();
        int subsetSize = colony[0].getFeatureSubsetSize();
        for (int ant = 1; ant < COLONY_SIZE; ant++) {
            if ((colony[ant].getFitness() > fittestValue)
                    || ((colony[ant].getFitness() == fittestValue)
                    && (colony[ant].getFeatureSubsetSize() < subsetSize))) {
                fittestIndex = ant;
                fittestValue = colony[ant].getFitness();
                subsetSize = colony[ant].getFeatureSubsetSize();
            }
        }
//        System.out.println(colony[fittestIndex].toString());
        return colony[fittestIndex];
    }
}
