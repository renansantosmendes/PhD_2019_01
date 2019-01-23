/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.HashMap;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

/**
 *
 * @author renansantos
 */
public class DoubleSolution 
    extends AbstractGenericSolution<Double, DoubleProblem>
    implements org.uma.jmetal.solution.DoubleSolution {

  /** Constructor */
  public DoubleSolution(DoubleProblem problem) {
    super(problem) ;

    initializeDoubleVariables();
    initializeObjectiveValues();
  }

  /** Copy constructor */
  public DoubleSolution(DoubleSolution solution) {
    super(solution.problem) ;

    for (int i = 0; i < problem.getNumberOfVariables(); i++) {
      setVariableValue(i, solution.getVariableValue(i));
    }

    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
      setObjective(i, solution.getObjective(i)) ;
    }

    attributes = new HashMap<Object, Object>(solution.attributes) ;
  }

  @Override
  public Double getUpperBound(int index) {
    return problem.getUpperBound(index);
  }

  @Override
  public Double getLowerBound(int index) {
    return problem.getLowerBound(index) ;
  }

  @Override
  public DoubleSolution copy() {
    return new DoubleSolution(this);
  }

  @Override
  public String getVariableValueString(int index) {
    return getVariableValue(index).toString() ;
  }
  
  private void initializeDoubleVariables() {
    for (int i = 0 ; i < problem.getNumberOfVariables(); i++) {
      Double value = randomGenerator.nextDouble(getLowerBound(i), getUpperBound(i)) ;
      setVariableValue(i, value) ;
    }
  }
}
