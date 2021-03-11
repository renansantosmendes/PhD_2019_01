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

/**
 * The abstract class contains the main methods and fields that are used to
 * represent discrete search space as a graph in ACO algorithm. The graph is
 * implemented as an 2D array.
 *
 * @author Sina Tabakhi
 */
public abstract class BasicGraphRepresentation {

    protected double[][] pheromone;

    /**
     * Initializes the parameters
     *
     * @param rows the number of rows in the pheromone array
     * @param cols the number of columns in the pheromone array
     */
    public BasicGraphRepresentation(int rows, int cols) {
        pheromone = new double[rows][cols];
    }

    /**
     * This method sets a pheromone value in a specific entry of the array.
     *
     * @param value the input pheromone value
     * @param indexRow the index of the row in the pheromone array
     * @param indexCol the index of the column in the pheromone array
     */
    protected abstract void setPheromone(double value, int indexRow, int indexCol);

    /**
     * This method returns a pheromone value in a specific entry of the
     * pheromone array that is determined by indeces of the row and column
     *
     * @param indexRow the index of the row in the pheromone array
     * @param indexCol the index of the column in the pheromone array
     *
     * @return a pheromone value
     */
    public abstract double getPheromone(int indexRow, int indexCol);

    /**
     * This method fills all entries of the pheromone array with a specific
     * input value.
     *
     * @param value the input value
     */
    public void fillPheromoneArray(double value) {
        for (int row = 0; row < pheromone.length; row++) {
            for (int col = 0; col < pheromone[row].length; col++) {
                setPheromone(value, row, col);
            }
        }
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return a string representation of the graph.
     */
    @Override
    public String toString() {
        String str = " ";
        for (int row = 0; row < pheromone.length; row++) {
            for (int col = 0; col < pheromone[row].length; col++) {
                str += getPheromone(row, col) + ",";
            }
            str = str.substring(0, str.length() - 1) + "\n";
        }
        return str;
    }
}
