/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

import InstanceReader.Instance;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author renansantos
 */
public class Parameters {

    private Instance instance;
    private List<Double> values = new ArrayList<>();
    
    public Parameters(int numberOfParameters){
        values.clear();
        for (int i = 0; i < numberOfParameters; i++) {
            values.add(1.0);
        }
    }
    
    public Parameters(Instance instance) {
        this.instance = instance;
        this.calculate();
    }
    
    public void calculate(){
        values.clear();
        values.add(0.10);//1
        values.add((double) instance.getRequestTimeWindows());//delta_t
        values.add((double) instance.getNumberOfNodes());//n
        values.add((double) instance.getNumberOfRequests()* instance.getNumberOfNodes() * instance.getRequestTimeWindows());// r n delta_t
        values.add((double) instance.getNumberOfRequests() * instance.getNumberOfNodes());//r n
        values.add((double) instance.getNumberOfRequests());//r
        values.add((double) instance.getNumberOfNodes());//n
        values.add(1.0);//1
        values.add((double) instance.getNumberOfRequests() * instance.getNumberOfNodes() * instance.getRequestTimeWindows());//
    }
    
    public List<Double> getParameters(){
        return this.values;
    }
    
}
