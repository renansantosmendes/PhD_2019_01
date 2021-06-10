package KFST.featureSelection.embedded;

import KFST.featureSelection.embedded.SVMBasedMethods.MSVM_RFE;
import KFST.featureSelection.embedded.SVMBasedMethods.OVA_SVM_RFE;
import KFST.featureSelection.embedded.SVMBasedMethods.OVO_SVM_RFE;
import KFST.featureSelection.embedded.SVMBasedMethods.SVM_RFE;
//import KFST.featureSelection.embedded.TreeBasedMethods.RandomForestMethod;
import KFST.featureSelection.embedded.TreeBasedMethods.DecisionTreeBasedMethod;
import KFST.dataset.DatasetInfo;
import KFST.featureSelection.FeatureSelection;
import java.util.ArrayList;

public abstract class EmbeddedApproach extends FeatureSelection {

    protected String[] nameFeatures;

    protected String[] classLabel;

    protected final String PROJECT_PATH;

    public EmbeddedApproach(String path) {
        super();
        this.PROJECT_PATH = path;
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

    public static EmbeddedApproach newMethod(EmbeddedType type, Object... arguments) {
        if (type == EmbeddedType.DECISION_TREE_BASED) {
            return new DecisionTreeBasedMethod(arguments);
        } else if (type == EmbeddedType.RANDOM_FOREST_METHOD) {
            return null;
        } else if (type == EmbeddedType.SVM_RFE) {
            return new SVM_RFE(arguments);
        } else if (type == EmbeddedType.MSVM_RFE) {
            return new MSVM_RFE(arguments);
        } else if (type == EmbeddedType.OVO_SVM_RFE) {
            return new OVO_SVM_RFE(arguments);
        } else if (type == EmbeddedType.OVA_SVM_RFE) {
            return new OVA_SVM_RFE(arguments);
        }
        return null;
    }
}
