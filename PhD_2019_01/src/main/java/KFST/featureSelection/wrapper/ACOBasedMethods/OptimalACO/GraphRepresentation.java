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

import KFST.featureSelection.wrapper.ACOBasedMethods.BasicGraphRepresentation;

/**
 * This java class is used to represent a graph in optimal ant colony
 * optimization (optimal ACO) method.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.ACOBasedMethods.BasicGraphRepresentation
 */
public class GraphRepresentation extends BasicGraphRepresentation {

    /**
     * Initializes the parameters
     *
     * @param rows the number of rows in the pheromone array
     * @param cols the number of columns in the pheromone array
     */
    public GraphRepresentation(int rows, int cols) {
        super(rows, cols);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setPheromone(double value, int indexRow, int indexCol) {
        pheromone[indexRow][indexCol] = value;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public double getPheromone(int indexRow, int indexCol) {
        return pheromone[indexRow][indexCol];
    }
}
