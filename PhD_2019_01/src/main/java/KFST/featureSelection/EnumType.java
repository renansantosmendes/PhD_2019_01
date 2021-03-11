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
package KFST.featureSelection;

/**
 * The abstract class contains the main methods and fields that is used in all
 * enumerable types.
 *
 * @author Sina Tabakhi
 */
public abstract class EnumType {

    protected String name;
    protected int value;

    /**
     * initializes the parameters
     *
     * @param name the name of object
     */
    public EnumType(String name) {
        this(name, 0);
    }

    /**
     * initializes the parameters
     *
     * @param name the name of object
     * @param value the value of object
     */
    public EnumType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Converts the object name to string
     *
     * @return the name of object
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns value of the object
     *
     * @return value of the object
     */
    public int getValue() {
        return this.value;
    }
}
