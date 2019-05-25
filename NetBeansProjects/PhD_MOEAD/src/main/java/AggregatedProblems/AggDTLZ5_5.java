package AggregatedProblems;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

/**
 *
 * @author renansantos
 */
public class AggDTLZ5_5 extends AbstractDoubleProblem {

    private Integer reducedNumberOfObjectives;
    public AggDTLZ5_5() {
        this(12, 3, 3);
    }

    /**
     * Creates a DTLZ5 problem instance
     *
     * @param numberOfVariables Number of variables
     * @param originalNumberOfObjectives Number of objective functions
     */
    public AggDTLZ5_5(Integer numberOfVariables, Integer originalNumberOfObjectives, Integer reducedNumberOfObjectives)
            throws JMetalException {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(originalNumberOfObjectives);
        setName("AggDTLZ5");
        
        this.reducedNumberOfObjectives = reducedNumberOfObjectives;

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    /**
     * Evaluate() method
     */
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = 5;//getNumberOfObjectives();
        double[] theta = new double[numberOfObjectives - 1];
        double g = 0.0;

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        int k = getNumberOfVariables() - getNumberOfObjectives() + 1;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i);
        }

        for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
            g += (x[i] - 0.5) * (x[i] - 0.5);
        }

        double t = java.lang.Math.PI / (4.0 * (1.0 + g));

        theta[0] = x[0] * java.lang.Math.PI / 2.0;
        for (int i = 1; i < (numberOfObjectives - 1); i++) {
            theta[i] = t * (1.0 + 2.0 * g * x[i]);
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            f[i] = 1.0 + g;
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
                f[i] *= java.lang.Math.cos(theta[j]);
            }
            if (i != 0) {
                int aux = numberOfObjectives - (i + 1);
                f[i] *= java.lang.Math.sin(theta[aux]);
            }
        }

        Double F = 0.0;
        for (int i = 0; i < numberOfObjectives - 1; i++) {
            F += f[i];
            
        }
        solution.setObjective(0, F);
        solution.setObjective(1, f[numberOfObjectives - 1]);
        //solution = new DoubleSolution();
        
    }

}
