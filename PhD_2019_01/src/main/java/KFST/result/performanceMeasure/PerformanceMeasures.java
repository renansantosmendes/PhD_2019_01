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
package KFST.result.performanceMeasure;

import KFST.util.MathFunc;

/**
 * This java class is used to save all performance measure values obtained from
 * different runs of given feature selection method.
 *
 * @author Sina Tabakhi
 * @see KFST.result.performanceMeasure.Criteria
 */
public class PerformanceMeasures {

    private Criteria[][] measures;
    private Criteria[] averageValues;

    /**
     * initializes the parameters
     *
     * @param numRuns numbers of runs of the feature selection method
     * @param numSelectedSubsets numbers of selected feature subsets
     */
    public PerformanceMeasures(int numRuns, int numSelectedSubsets) {
        measures = new Criteria[numRuns][numSelectedSubsets];
        for (int i = 0; i < measures.length; i++) {
            for (int j = 0; j < measures[i].length; j++) {
                measures[i][j] = new Criteria();
            }
        }

        averageValues = new Criteria[numSelectedSubsets];
        for (int i = 0; i < averageValues.length; i++) {
            averageValues[i] = new Criteria();
        }
    }

    /**
     * Sets the obtained criteria values from feature selection method
     *
     * @param i the current run of the feature selection method
     * @param j the current subset of the selected features
     * @param criteria values obtained from the feature selection method
     */
    public void setCriteria(int i, int j, Criteria criteria) {
        measures[i][j] = criteria;
    }

    /**
     * Computes the average values of all criteria over number of runs
     */
    public void computeAverageValues() {
        for (int j = 0; j < measures[0].length; j++) {
            for (int i = 0; i < measures.length; i++) {
                averageValues[j].setAccuracy(averageValues[j].getAccuracy() + measures[i][j].getAccuracy());
                averageValues[j].setErrorRate(averageValues[j].getErrorRate() + measures[i][j].getErrorRate());
                averageValues[j].setTime(averageValues[j].getTime() + measures[i][j].getTime());
            }
            averageValues[j].setAccuracy(Double.parseDouble(MathFunc.roundDouble(averageValues[j].getAccuracy() / measures.length, 3)));
            averageValues[j].setErrorRate(Double.parseDouble(MathFunc.roundDouble(averageValues[j].getErrorRate() / measures.length, 3)));
            averageValues[j].setTime(Double.parseDouble(MathFunc.roundDouble(averageValues[j].getTime() / measures.length, 3)));
        }
    }

    /**
     * Returns the accuracy values of the feature selection method
     *
     * @return an array of the accuracy values
     */
    public double[][] getAccuracyValues() {
        double[][] array = new double[measures.length][measures[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = measures[i][j].getAccuracy();
            }
        }
        return array;
    }

    /**
     * Returns the error rate values of the feature selection method
     *
     * @return an array of the error rate values
     */
    public double[][] getErrorRateValues() {
        double[][] array = new double[measures.length][measures[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = measures[i][j].getErrorRate();
            }
        }
        return array;
    }

    /**
     * Returns the execution time values of the feature selection method
     *
     * @return an array of the execution time values
     */
    public double[][] getTimeValues() {
        double[][] array = new double[measures.length][measures[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = measures[i][j].getTime();
            }
        }
        return array;
    }

    /**
     * Returns the average values of the accuracy of the feature selection
     * method over number of runs
     *
     * @return an array of the average values of the accuracy
     */
    public double[][] getAverageAccuracyValues() {
        double[][] array = new double[1][averageValues.length];
        for (int j = 0; j < array[0].length; j++) {
            array[0][j] = averageValues[j].getAccuracy();
        }
        return array;
    }

    /**
     * Returns the average values of the error rate of the feature selection
     * method over number of runs
     *
     * @return an array of the average values of the error rate
     */
    public double[][] getAverageErrorRateValues() {
        double[][] array = new double[1][averageValues.length];
        for (int j = 0; j < array[0].length; j++) {
            array[0][j] = averageValues[j].getErrorRate();
        }
        return array;
    }

    /**
     * Returns the average values of the execution time of the feature selection
     * method over number of runs
     *
     * @return an array of the average values of the execution time
     */
    public double[][] getAverageTimeValues() {
        double[][] array = new double[1][averageValues.length];
        for (int j = 0; j < array[0].length; j++) {
            array[0][j] = averageValues[j].getTime();
        }
        return array;
    }
}
