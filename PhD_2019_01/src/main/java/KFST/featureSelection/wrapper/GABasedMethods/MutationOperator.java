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

/**
 * This java class is used to implement various mutation operators for mutating
 * new offsprings by changing the value of some genes in them.
 * <p>
 * The methods in this class is contained brief description of the applications.
 *
 * @author Sina Tabakhi
 */
public class MutationOperator {

    /**
     * mutates new offsprings by changing the value of some genes in them using
     * bitwise mutation
     *
     * @param parent the first parent
     * @param prob the probability of mutation operation
     */
    public static void bitwiseMutation(boolean[] parent, double prob) {
        for (int gene = 0; gene < parent.length; gene++) {
            if (Math.random() <= prob) {
                parent[gene] = !parent[gene];
            }
        }
    }

    /**
     * mutates new offsprings by changing the value of some genes in them using
     * bitwise mutation
     *
     * @param parent the first parent
     * @param prob the probability of mutation operation
     */
    public static void bitwiseMutation(Boolean[] parent, double prob) {
        for (int gene = 0; gene < parent.length; gene++) {
            if (Math.random() <= prob) {
                parent[gene] = !parent[gene];
            }
        }
    }
}
