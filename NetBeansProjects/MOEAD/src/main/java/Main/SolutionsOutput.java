package Main;


import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

/**
 *
 * @author renansantos
 */
public class SolutionsOutput {
    private Algorithm algorithm;
    private PrintStream streamForDataInCsv;
    
    public SolutionsOutput(Algorithm algorithm, String path){
        this.algorithm = algorithm;
        try {
            streamForDataInCsv = new PrintStream(path + "/MOEAD_RESULTS.csv");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void saveResult(){
        List<Solution> list = (List<Solution>) algorithm.getResult();

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).getNumberOfObjectives(); j++) {
               this.streamForDataInCsv.print(list.get(i).getObjective(j) + "|");
            }
            this.streamForDataInCsv.print("\n");
        }
    }
    
}
