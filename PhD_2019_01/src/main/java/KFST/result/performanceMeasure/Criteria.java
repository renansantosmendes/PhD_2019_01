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

/**
 * This java class is used to set different criteria values used in the feature
 * selection area of research.
 *
 * @author Sina Tabakhi
 */
public class Criteria {

    private double accuracy;
    private double errorRate;
    private double time;

    /**
     * initializes the parameters
     */
    public Criteria() {
        accuracy = 0;
        errorRate = 0;
        time = 0;
    }

    /**
     * This method sets the accuracy value.
     *
     * @param accuracy the accuracy value
     */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * This method returns the accuracy.
     *
     * @return the <code>accuracy</code> value
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * This method sets the error rate value.
     *
     * @param errorRate the error rate value
     */
    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    /**
     * This method returns the error rate.
     *
     * @return the <code>error rate</code> value
     */
    public double getErrorRate() {
        return errorRate;
    }

    /**
     * This method sets the time value.
     *
     * @param time the time value
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * This method returns the time.
     *
     * @return the <code>time</code> value
     */
    public double getTime() {
        return time;
    }
}
