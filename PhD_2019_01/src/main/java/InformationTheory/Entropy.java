/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InformationTheory;

import java.util.Map.Entry;

/**
 *
 * @author renan
 */
public abstract class Entropy {

    public static double LOG_BASE = 2.0;

    private Entropy() {
    }

    /**
     * Calculates the univariate entropy H(X) from a vector. Uses histograms to
     * estimate the probability distributions, and thus the entropy. The entropy
     * is bounded 0 &#8804; H(X) &#8804; log |X|, where log |X| is the log of
     * the number of states in the random variable X.
     *
     * @param dataVector Input vector (X). It is discretised to the floor of
     * each value before calculation.
     * @return The entropy H(X).
     */
    public static double calculateEntropy(double[] dataVector) {
        ProbabilityState state = new ProbabilityState(dataVector);

        double entropy = 0.0;
        for (Double prob : state.probMap.values()) {
            if (prob > 0) {
                entropy -= prob * Math.log(prob);
            }
        }

        entropy /= Math.log(LOG_BASE);

        return entropy;
    }//calculateEntropy(double [])

    /**
     * Calculates the conditional entropy H(X|Y) from two vectors. X =
     * dataVector, Y = conditionVector. Uses histograms to estimate the
     * probability distributions, and thus the entropy. The conditional entropy
     * is bounded 0 &#8804; H(X|Y) &#8804; H(X).
     *
     * @param dataVector Input vector (X). It is discretised to the floor of
     * each value before calculation.
     * @param conditionVector Input vector (Y). It is discretised to the floor
     * of each value before calculation.
     * @return The conditional entropy H(X|Y).
     */
    public strictfp static double calculateConditionalEntropy(double[] dataVector, double[] conditionVector) {
        JointProbabilityState state = new JointProbabilityState(dataVector, conditionVector);

        double jointValue, condValue;
        double condEntropy = 0.0;

        for (Entry<Pair<Integer, Integer>, Double> e : state.jointProbMap.entrySet()) {
            jointValue = e.getValue();
            condValue = state.secondProbMap.get(e.getKey().b);
            if ((jointValue > 0) && (condValue > 0)) {
                condEntropy -= jointValue * Math.log(jointValue / condValue);
            }
        }

        condEntropy /= Math.log(LOG_BASE);

        return condEntropy;
    }//calculateConditionalEntropy(double [],double [])

    /**
     * Calculates the joint entropy H(X,Y) from two vectors. The order of the
     * input vectors is irrelevant. Uses histograms to estimate the probability
     * distributions, and thus the entropy. The joint entropy is bounded 0
     * &#8804; H(X,Y) &#8804; log |XY|, where log |XY| is the log of the number
     * of states in the joint random variable XY.
     *
     * @param firstVector Input vector. It is discretised to the floor of each
     * value before calculation.
     * @param secondVector Input vector. It is discretised to the floor of each
     * value before calculation.
     * @return The joint entropy H(X,Y).
     */
    public static double calculateJointEntropy(double[] firstVector, double[] secondVector) {
        JointProbabilityState state = new JointProbabilityState(firstVector, secondVector);

        double entropy = 0.0;
        for (Double prob : state.jointProbMap.values()) {
            if (prob > 0) {
                entropy -= prob * Math.log(prob);
            }
        }

        entropy /= Math.log(LOG_BASE);

        return entropy;
    }//calculateJointEntropy(double [],double [])
}
