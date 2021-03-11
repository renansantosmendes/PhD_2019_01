package KFST.featureSelection.filter;

import KFST.featureSelection.*;
import KFST.featureSelection.filter.supervised.*;
import KFST.featureSelection.filter.unsupervised.*;

public abstract class FilterApproach extends FeatureSelection {

    public FilterApproach(int sizeSelectedFeatureSubset) {
        super();
        this.numSelectedFeature = sizeSelectedFeatureSubset;
        this.selectedFeatureSubset = new int[this.numSelectedFeature];
    }

    public static FilterApproach newMethod(FilterType type, boolean isSupervised, Object... arguments) {
        if (type == FilterType.MRMR) {
            return new MRMR(arguments);
        } else if (type == FilterType.RRFS && isSupervised) {
            return new KFST.featureSelection.filter.supervised.RRFS(arguments);
        } else if (type == FilterType.RRFS && !isSupervised) {
            return new KFST.featureSelection.filter.unsupervised.RRFS(arguments);
        } else if (type == FilterType.MUTUAL_CORRELATION) {
            return new MutualCorrelation(arguments);
        } else if (type == FilterType.RSM) {
            return new RSM(arguments);
        } else if (type == FilterType.UFSACO) {
            return new UFSACO(arguments);
        } else if (type == FilterType.RRFSACO_1) {
            return new RRFSACO_1(arguments);
        } else if (type == FilterType.RRFSACO_2) {
            return new RRFSACO_2(arguments);
        } else if (type == FilterType.IRRFSACO_1) {
            return new IRRFSACO_1(arguments);
        } else if (type == FilterType.IRRFSACO_2) {
            return new IRRFSACO_2(arguments);
        } else if (type == FilterType.MGSACO) {
            return new MGSACO(arguments);
        }
        return null;
    }
}
