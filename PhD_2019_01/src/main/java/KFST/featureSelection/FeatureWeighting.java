package KFST.featureSelection;

public abstract class FeatureWeighting extends FeatureSelection {

    protected double[] featureValues;

    public FeatureWeighting() {
        super();
    }

    public double[] getFeatureValues() {
        return this.featureValues;
    }
}
