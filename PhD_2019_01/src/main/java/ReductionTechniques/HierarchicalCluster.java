package ReductionTechniques;

import Jama.Matrix;
import ReductionTechniques.CorrelationType;
import java.io.*;
import java.util.*;
import org.apache.commons.math3.linear.RealMatrix;
import Correlations.KendallsCorrelation;
import InformationTheory.MutualInformation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renansantos
 */
public class HierarchicalCluster {

    private List<List<Double>> listData;
    private double[][] data;
    private String fileName;
    private int numberOfClusters = 0;
    private List<Cluster> clusters;
    private double[][] similarity;
    private double[][] dissimilarity;
    private List<List<Integer>> transformationList;
    private int numberOfRows;
    private int numberOfColumns;
    private CorrelationType correlationType;

    private class Cluster {

        private List<Integer> points;

        public Cluster() {
            points = new ArrayList<>();
        }

        public void addPointPosition(int position) {
            points.add(position);
        }

        public void addPointPositions(List<Integer> positions) {
            points.addAll(positions);
        }

        public List<Integer> getPoints() {
            return this.points;
        }

        public String toString() {
            return this.points.toString();
        }
    }

    public HierarchicalCluster() {

    }

    public HierarchicalCluster(double[][] data, int numberOfClusters, CorrelationType corr) {
        this.data = data;
        this.numberOfClusters = numberOfClusters;
        this.clusters = new ArrayList<>();
        this.numberOfRows = this.data.length;
        this.numberOfColumns = this.data[0].length;
        this.correlationType = corr;
        //createMatrix();
        calculateSilimarity();
        calculateDissilimarity();
    }

    public HierarchicalCluster(double[][] data, int numberOfClusters) {
        this.data = data;
        this.numberOfClusters = numberOfClusters;
        this.clusters = new ArrayList<>();
        this.numberOfRows = this.data.length;
        this.numberOfColumns = this.data[0].length;
        //createMatrix();
        calculateSilimarity();
        calculateDissilimarity();
    }

    public HierarchicalCluster(String fileName, int numberOfClusters) throws IOException {
        this.fileName = fileName;
        this.numberOfClusters = numberOfClusters;
        this.clusters = new ArrayList<>();
        this.listData = readData();
        createMatrix();
        calculateSilimarity();
        calculateDissilimarity();
    }

    public List<List<Double>> getListData() {
        return listData;
    }

    public double[][] getData() {
        return data;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public double[][] getSimilarity() {
        return similarity;
    }

    public double[][] getDissimilarity() {
        return similarity;
    }

    public List<List<Integer>> getTransfomationList() {
        return transformationList;
    }

    public void printTransformationList() {
        this.transformationList.forEach(System.out::println);
        //System.out.println(this.transformationList);
    }

    public HierarchicalCluster setCorrelation(CorrelationType correlationType) {
        this.correlationType = correlationType;
        return this;
    }

    public void setTransformationList(List<List<Integer>> list) {
        this.transformationList = new ArrayList<>();
        this.transformationList = list;
    }

    private List<List<Double>> readData() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<List<Double>> listData = new ArrayList<>();
        while (br.ready()) {
            String linha = br.readLine();
            String[] teste = linha.split(",");
            List<Double> line = new ArrayList<>();

            for (String string : teste) {
                line.add(Double.parseDouble(string));
            }
            listData.add(line);
        }

        this.numberOfRows = listData.size();
        this.numberOfColumns = listData.get(0).size();
        br.close();

        return listData;
    }

    private void createMatrix() {
        this.data = new double[this.numberOfRows][this.numberOfColumns];
        for (int k = 0; k < this.numberOfRows; k++) {
            for (int i = 0; i < this.numberOfColumns; i++) {
                this.data[k][i] = listData.get(k).get(i);
            }
        }
    }

    private double[][] createMatrix(List<List<Double>> list, int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        for (int k = 0; k < rows; k++) {
            for (int i = 0; i < columns; i++) {
                matrix[k][i] = list.get(k).get(i);
            }
        }
        return matrix;
    }

    public void printMatrixData() {
        for (int j = 0; j < this.numberOfRows; j++) {
            for (int i = 0; i < this.numberOfColumns; i++) {
                System.out.print(this.data[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void printSimilarity() {
        for (int j = 0; j < similarity.length; j++) {
            for (int i = 0; i < similarity.length; i++) {
                System.out.print(this.similarity[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void printSimilarity(int length) {
        for (int j = 0; j < length; j++) {
            for (int i = 0; i < length; i++) {
                System.out.print(this.similarity[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void printDissimilarity() {
        for (int j = 0; j < dissimilarity.length; j++) {
            for (int i = 0; i < dissimilarity.length; i++) {
                System.out.print(this.dissimilarity[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void printDissimilarity(int length) {
        for (int j = 0; j < length; j++) {
            for (int i = 0; i < length; i++) {
                System.out.print(this.dissimilarity[j][i] + " ");
            }
            System.out.println();
        }
    }

    private void calculateSilimarity() {
        if (this.correlationType == CorrelationType.PEARSON) {
            PearsonsCorrelation corr = new PearsonsCorrelation(this.data);
            this.similarity = corr.getCorrelationMatrix().getData();
        } else if (this.correlationType == CorrelationType.SPEARMAN) {
            SpearmansCorrelation corr = new SpearmansCorrelation();
            this.similarity = corr.computeCorrelationMatrix(this.data).getData();
        } else if (this.correlationType == CorrelationType.KENDALL) {
            KendallsCorrelation corr = new KendallsCorrelation(this.data);
            this.similarity = corr.getCorrelationMatrix().getData();
        } else if (this.correlationType == CorrelationType.MUTUAL_INFORMATION) {
            this.similarity = calculateMutualInformation(this.data);
        } else {
            PearsonsCorrelation corr = new PearsonsCorrelation(this.data);
            this.similarity = corr.getCorrelationMatrix().getData();
        }
    }

    private double[][] calculateSilimarity(double[][] data) {
        if (this.correlationType == CorrelationType.PEARSON) {
            PearsonsCorrelation corr = new PearsonsCorrelation(data);
            return corr.getCorrelationMatrix().getData();
        } else if (this.correlationType == CorrelationType.SPEARMAN) {
            SpearmansCorrelation corr = new SpearmansCorrelation();
            return corr.computeCorrelationMatrix(data).getData();
        } else if (this.correlationType == CorrelationType.KENDALL) {
            KendallsCorrelation corr = new KendallsCorrelation(data);
            return corr.getCorrelationMatrix().getData();
        } else if (this.correlationType == CorrelationType.MUTUAL_INFORMATION) {
            return calculateMutualInformation(data);
        } else {
            PearsonsCorrelation corr = new PearsonsCorrelation(data);
            return corr.getCorrelationMatrix().getData();
        }
    }

    private double[][] calculateMutualInformation(double[][] data) {
        MutualInformation mi = new MutualInformation();
        double[][] mutualInformationMatrix = new double[data[0].length][data[0].length];

        for (int column = 0; column < data[0].length; column++) {
            for (int line = 0; line < data[0].length; line++) {
                mutualInformationMatrix[line][column] = -mi
                        .calculateMutualInformation(getColumnVectorFromMatrix(data, line), getColumnVectorFromMatrix(data, column));
            }
        }

        return mutualInformationMatrix;
    }

    private double[] getColumnVectorFromMatrix(double[][] data, int column) {
        double[] values = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            values[i] = data[i][column];
        }
        return values;
    }

    private double[] getLineVectorFromMatrix(double[][] data, int line) {
        double[] values = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[line][i];
        }
        return values;
    }

    public void printSquareMatrix(double[][] matrix) {
        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void calculateDissilimarity() {
        dissimilarity = new double[similarity.length][similarity.length];
        for (int j = 0; j < similarity.length; j++) {
            for (int i = 0; i < similarity.length; i++) {
                if (!Double.isNaN(similarity[j][i])) {
                    dissimilarity[j][i] = 1 - similarity[j][i];
                } else {
                    dissimilarity[j][i] = 20;
                }
            }
        }
    }

    private void copySquareMatrix(double[][] destinationMatrix, double[][] sourceMatrix, int length) {
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1; j++) {
                destinationMatrix[i][j] = sourceMatrix[i][j];
            }
        }
    }

    public List<Integer> findMinDissimilarity(int rows, int columns) {
        double minDissimilarity = 20.0;
        List<Integer> list = new ArrayList<>();
        int column = 0;
        int row = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
//                System.out.println("current dissimilarity " + dissimilarity[i][j]);
                if (minDissimilarity > dissimilarity[i][j] && dissimilarity[i][j] != 0) {
                    minDissimilarity = dissimilarity[i][j];
                    column = i;
                    row = j;
//                    System.out.println("current dissimilarity " + dissimilarity[i][j] + " columns " + columns);
                }
            }
        }

//        if (column == row) {
//            System.out.println("problem with dissimilarity = " + minDissimilarity);
//            this.printMatrixData();
//            this.printDissimilarity();
//            Random r1 = new Random(System.currentTimeMillis());
//            Random r2 = new Random(System.currentTimeMillis() + 100000);
//            column = r1.nextInt(columns);
//            row = r2.nextInt(columns);
//            System.out.println("random values " + row + " " + column);
//        }
//        printSimilarity();
//        System.out.println("");
//        printDissimilarity();
//        if (column == row) {
////            this.printMatrixData();
////            this.printDissimilarity();
//            Random r1 = new Random(System.currentTimeMillis());
//            Random r2 = new Random(System.currentTimeMillis());
//            column = r1.nextInt(columns);
//            do {
//                row = r2.nextInt(columns);
//            } while (row == column);
//            
//            System.out.println("dimension with error " + columns);
//            System.out.println("column an row " + column + " " + row);
//            this.printMatrixData();
//            System.out.println("");
//            this.printDissimilarity();
////          
//        }
        list.add(row);
        list.add(column);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public void reduceMatrix(int column1, int column2) {
        Matrix m = new Matrix(createMatrix(this.listData, this.listData.size(), this.listData.get(0).size()));
        Matrix reducedData;
        int numberOfObjectives = this.numberOfColumns;

        reducedData = new Matrix(m.getRowDimension(), m.getColumnDimension() - 1);
        reducedData.setMatrix(0, m.getRowDimension() - 1, 0, column1 - 1, m.getMatrix(0, m.getRowDimension() - 1, 0, column1 - 1));
        reducedData.setMatrix(0, m.getRowDimension() - 1, column1, column1, m.getMatrix(0, m.getRowDimension() - 1, column1, column1)
                .plus(m.getMatrix(0, m.getRowDimension() - 1, column2, column2)));
        reducedData.setMatrix(0, m.getRowDimension() - 1, column1 + 1, column2 - 1, m.getMatrix(0, m.getRowDimension() - 1, column1 + 1, column2 - 1));
        reducedData.setMatrix(0, m.getRowDimension() - 1, column2, m.getColumnDimension() - 2, m.getMatrix(0, m.getRowDimension() - 1, column2 + 1, m.getColumnDimension() - 1));
    }

    public Matrix reduceMatrix(Matrix matrix, int column1, int column2) {
        Matrix m = matrix;
        Matrix reducedData;
        int numberOfObjectives = this.numberOfColumns;

        reducedData = null;
//        System.out.println("columns " + column1 + " "+ column2);
        reducedData = new Matrix(m.getRowDimension(), m.getColumnDimension() - 1);
        reducedData.setMatrix(0, m.getRowDimension() - 1, 0, column1 - 1, m.getMatrix(0, m.getRowDimension() - 1, 0, column1 - 1));
        reducedData.setMatrix(0, m.getRowDimension() - 1, column1, column1, m.getMatrix(0, m.getRowDimension() - 1, column1, column1)
                .plus(m.getMatrix(0, m.getRowDimension() - 1, column2, column2)));
        reducedData.setMatrix(0, m.getRowDimension() - 1, column1 + 1, column2 - 1, m.getMatrix(0, m.getRowDimension() - 1, column1 + 1, column2 - 1));
        reducedData.setMatrix(0, m.getRowDimension() - 1, column2, m.getColumnDimension() - 2, m.getMatrix(0, m.getRowDimension() - 1, column2 + 1, m.getColumnDimension() - 1));
        return reducedData;
    }

    private void copyFrom2DArrayToList(double[][] matrix, List<List<Double>> list, int rows, int columns) {
        list = new ArrayList<>();
        for (int j = 0; j < rows; j++) {
            List<Double> line = new ArrayList<>();
            for (int i = 0; i < columns; i++) {
                line.add(matrix[j][i]);
            }
            list.add(line);
        }
    }

    public HierarchicalCluster reduce() {
        Matrix m = new Matrix(this.data);
        int numberOfColumns = this.numberOfColumns;
        List<List<Integer>> columns = new ArrayList<>();
        initializeColumnsForCluster(columns);

        while (numberOfColumns > this.numberOfClusters) {
            List<Integer> indexes = new ArrayList<>();
            indexes.addAll(findMinDissimilarity(m.getRowDimension(), numberOfColumns));
//            System.out.println("indexes " + indexes);
            m = reduceMatrix(m, indexes.get(0), indexes.get(1));

            copySquareMatrix(this.similarity, calculateSilimarity(m.getArray()), numberOfColumns);
            calculateDissilimarity();
            Cluster cluster = new Cluster();
            cluster.addPointPositions(indexes);
            this.clusters.add(cluster);
            numberOfColumns--;
            columns.get(indexes.get(0)).addAll(columns.get(indexes.get(1)));
            columns.get(indexes.get(1)).clear();
            int index = indexes.get(1);
            columns.remove(index);

        }
        columns.forEach(list -> list.sort(Comparator.naturalOrder()));
        this.transformationList = generateClusterMatrix(columns);
        return this;
    }

    public HierarchicalCluster reduce(List<List<Integer>> transformationList) {
        try {
            Matrix m = new Matrix(this.data);
            int numberOfColumns = this.numberOfColumns;
            List<List<Integer>> columns = new ArrayList<>();
            initializeColumnsForCluster(columns);

            while (numberOfColumns > this.numberOfClusters) {
                List<Integer> indexes = new ArrayList<>();
                indexes.addAll(findMinDissimilarity(m.getRowDimension(), numberOfColumns));
                m = reduceMatrix(m, indexes.get(0), indexes.get(1));

                copySquareMatrix(this.similarity, calculateSilimarity(m.getArray()), numberOfColumns);
                calculateDissilimarity();
                Cluster cluster = new Cluster();
                cluster.addPointPositions(indexes);
                this.clusters.add(cluster);
                numberOfColumns--;
                columns.get(indexes.get(0)).addAll(columns.get(indexes.get(1)));
                columns.get(indexes.get(1)).clear();
                int index = indexes.get(1);
                columns.remove(index);

            }
            columns.forEach(list -> list.sort(Comparator.naturalOrder()));
            this.transformationList = generateClusterMatrix(columns);
        } catch (Exception e) {
            System.out.println("error while reducing matrix, using last cluster...");
            this.transformationList = transformationList;
        }
        return this;
    }

    private void initializeColumnsForCluster(List<List<Integer>> columns) {
        for (int i = 0; i < this.numberOfColumns; i++) {
            List<Integer> column = new ArrayList<>();
            column.add(i);
            columns.add(column);
        }
    }

    private List<List<Integer>> generateClusterMatrix(List<List<Integer>> columns) {
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < this.numberOfClusters; i++) {
            List<Integer> column = new ArrayList<>();
            for (int j = 0; j < this.numberOfColumns; j++) {
                column.add(0);
            }
            list.add(column);
        }

        for (int i = 0; i < this.numberOfClusters; i++) {
            for (int j = 0; j < columns.get(i).size(); j++) {
                list.get(i).set(columns.get(i).get(j), 1);
            }
        }
        return list;
    }

}
