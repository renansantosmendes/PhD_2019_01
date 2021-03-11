package KFST.featureSelection.wrapper;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.FeatureSelection;
import KFST.featureSelection.wrapper.ACOBasedMethods.OptimalACO.OptimalACO;
import KFST.featureSelection.wrapper.GABasedMethods.HGAFS.HGAFS;
import KFST.featureSelection.wrapper.GABasedMethods.SimpleGA.SimpleGA;
import KFST.featureSelection.wrapper.PSOBasedMethods.BPSO.BPSO;
import KFST.featureSelection.wrapper.PSOBasedMethods.CPSO.CPSO;
import KFST.featureSelection.wrapper.PSOBasedMethods.HPSO_LS.HPSO_LS;
import KFST.featureSelection.wrapper.PSOBasedMethods.PSO42.PSO42;
import java.util.ArrayList;

public abstract class WrapperApproach extends FeatureSelection {

    protected String[] nameFeatures;

    protected String[] classLabel;

    protected final String PROJECT_PATH;

    protected final String TEMP_PATH;

    public WrapperApproach(String path) {
        super();
        this.PROJECT_PATH = path;
        this.TEMP_PATH = this.PROJECT_PATH + "Temp\\";
    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        super.loadDataSet(ob);
        this.nameFeatures = ob.getNameFeatures();
        this.classLabel = ob.getClassLabel();
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {
        super.loadDataSet(data, numFeat, numClasses);
        this.nameFeatures = new String[this.numFeatures + 1];
        for (int i = 0; i < this.nameFeatures.length; i++) {
            this.nameFeatures[i] = "f" + i;
        }
        ArrayList<String> labels = new ArrayList();
        for (double[] sample : data) {
            if (!labels.contains(Double.toString(sample[this.numFeatures]))) {
                labels.add(Double.toString(sample[this.numFeatures]));
            }
        }
        this.classLabel = new String[this.numClass];
        this.classLabel = labels.toArray(this.classLabel);
    }

    protected int[] originalFeatureSet() {
        int[] featureSet = new int[this.numFeatures];
        for (int i = 0; i < featureSet.length; i++) {
            featureSet[i] = i;
        }
        return featureSet;
    }

    public static WrapperApproach newMethod(WrapperType type, Object... arguments) {
        if (type == WrapperType.BPSO) {
            return new BPSO(arguments);
        } else if (type == WrapperType.CPSO) {
            return new CPSO(arguments);
        } else if (type == WrapperType.PSO42) {
            return new PSO42(arguments);
        } else if (type == WrapperType.HPSO_LS) {
            return new HPSO_LS(arguments);
        } else if (type == WrapperType.SIMPLE_GA) {
            return new SimpleGA(arguments);
        } else if (type == WrapperType.HGAFS) {
            return new HGAFS(arguments);
        } else if (type == WrapperType.OPTIMAL_ACO) {
            return new OptimalACO(arguments);
        }
        return null;
    }
}
