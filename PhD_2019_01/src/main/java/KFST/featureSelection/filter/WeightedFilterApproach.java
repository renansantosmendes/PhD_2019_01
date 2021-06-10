package KFST.featureSelection.filter;

import KFST.featureSelection.FeatureWeighting;
import KFST.featureSelection.filter.supervised.*;
import KFST.featureSelection.filter.unsupervised.LaplacianScore;

public abstract class WeightedFilterApproach extends FeatureWeighting {

    public WeightedFilterApproach(int sizeSelectedFeatureSubset) {
        super();
        this.numSelectedFeature = sizeSelectedFeatureSubset;
        this.selectedFeatureSubset = new int[this.numSelectedFeature];
    }

    public static WeightedFilterApproach newMethod(FilterType type, boolean isSupervised, Object... arguments) {
        if (type == FilterType.INFORMATION_GAIN) {
            return new InformationGain(arguments);
        } else if (type == FilterType.GAIN_RATIO) {
            return new GainRatio(arguments);
        } else if (type == FilterType.SYMMETRICAL_UNCERTAINTY) {
            return new SymmetricalUncertainty(arguments);
        } else if (type == FilterType.FISHER_SCORE) {
            return new FisherScore(arguments);
        } else if (type == FilterType.GINI_INDEX) {
            return new GiniIndex(arguments);
        } else if (type == FilterType.LAPLACIAN_SCORE && isSupervised) {
            return new LaplacianScore(arguments);
        } else if (type == FilterType.LAPLACIAN_SCORE && !isSupervised) {
            return new LaplacianScore(arguments);
        } else if (type == FilterType.TERM_VARIANCE) {
            return null;
            //return new TermVariance(arguments);
        }
        return null;
    }
}
