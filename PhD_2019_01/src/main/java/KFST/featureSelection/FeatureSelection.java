package KFST.featureSelection;

import KFST.dataset.DatasetInfo;
import KFST.util.ArraysFunc;

public abstract class FeatureSelection {

    protected double[][] trainSet;

    protected int numFeatures;

    protected int numClass;

    protected int[] selectedFeatureSubset;

    protected int numSelectedFeature;

    public FeatureSelection() {
    }

    public void loadDataSet(DatasetInfo ob) {
        this.trainSet = ob.getTrainSet();
        this.numFeatures = ob.getNumFeature();
        this.numClass = ob.getNumClass();
    }

    public void loadDataSet(double[][] data, int numFeat, int numClasses) {
        this.trainSet = ArraysFunc.copyDoubleArray2D(data);
        this.numFeatures = numFeat;
        this.numClass = numClasses;
    }

    public abstract void evaluateFeatures();

    public String validate() {
        return "";
    }

    public void setNumSelectedFeature(int numSelectedFeature) {
        this.numSelectedFeature = numSelectedFeature;
        this.selectedFeatureSubset = new int[this.numSelectedFeature];
    }

    public int[] getSelectedFeatureSubset() {
        return this.selectedFeatureSubset;
    }
}
