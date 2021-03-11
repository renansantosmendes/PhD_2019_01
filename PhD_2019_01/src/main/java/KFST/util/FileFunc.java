/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016-2018 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.util;

import KFST.dataset.DatasetInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * This java class is used to implement various utility methods for manipulating
 * files and directories. The methods in this class is contained brief 
 * description of the applications.
 *
 * @author Sina Tabakhi
 */
public final class FileFunc {
    
    /**
     * creates a new directory denoted by pathname
     *
     * @param pathname path of the directory 
     */
    public static void createDirectory(String pathname) {
        File dir = new File(pathname);
        dir.mkdir();
    }

    /**
     * deletes all files in the directory denoted by pathname
     * 
     * @param pathname path of the directory 
     */
    public static void deleteFilesInDirectory(String pathname) {
        File dir = new File(pathname);
        if (dir.isDirectory()) {
            File[] directory = dir.listFiles();
            for (File directoryPath : directory) {
                directoryPath.delete();
            }
        }
    }
    
    /**
     * deletes the current directory with all files in the directory denoted by 
     * pathname
     * 
     * @param pathname path of the directory 
     */
    public static void deleteDirectoryWithAllFiles(String pathname) {
        deleteFilesInDirectory(pathname);
        File dir = new File(pathname);
        if (dir.isDirectory()) {
            dir.delete();
        }
    }
    
    /**
     * This method creates a CSV (Comma delimited) file of the input data
     *
     * @param oldData the input data
     * @param selectedFeature the list of selected Feature
     * @param name name of the path for created CSV file
     * @param featureNames a string array of features names
     * @param classNames a string array of class labels names
     */
    public static void createCSVFile(double[][] oldData, int[] selectedFeature, String name, String[] featureNames, String[] classNames) {
        int numSamples = oldData.length;
        int sizeFeatureSet = selectedFeature.length;
        int numFeats = oldData[0].length - 1;
        try {
            FileWriter fw = new FileWriter(name, false);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < sizeFeatureSet; i++) {
                pw.print(featureNames[selectedFeature[i]] + ",");
            }
            pw.println(featureNames[numFeats]);
            for (int i = 0; i < numSamples; i++) {
                for (int j = 0; j < sizeFeatureSet; j++) {
                    pw.print(oldData[i][selectedFeature[j]] + ",");
                }
                int index = (int) oldData[i][numFeats];
                pw.println(classNames[index]);
            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFunc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method converts CSV file to ARFF file for the Weka Software
     *
     * @param nameCSV the path of the CSV file
     * @param nameARFF the path for the created ARFF file
     * @param pathProject the path of the project
     * @param sizeFeatureSubset the number of selected features
     * @param ob the object of the DatasetInfo
     */
    public static void convertCSVtoARFF(String nameCSV, String nameARFF, String pathProject, int sizeFeatureSubset, DatasetInfo ob) {
        convertCSVtoARFF(nameCSV, nameARFF, pathProject, sizeFeatureSubset,
                ob.getNumFeature(), ob.getNameFeatures(), ob.getNumClass(), ob.getClassLabel());
    }
    
    /**
     * This method converts CSV file to ARFF file for the Weka Software
     *
     * @param nameCSV the path of the CSV file
     * @param nameARFF the path for the created ARFF file
     * @param pathProject the path of the project
     * @param sizeFeatureSubset the number of selected features
     * @param numFeature the number of original features with class
     * @param nameFeatures the array of features' names
     * @param numClass the number of classes
     * @param classLabel the array of class labels' names
     */
    public static void convertCSVtoARFF(String nameCSV, String nameARFF, String pathProject, int sizeFeatureSubset,
                                        int numFeature, String[] nameFeatures, int numClass, String[] classLabel) {
        int selectLine = sizeFeatureSubset + 3;
        File tempFile = new File(pathProject + "tempFile.arff");

        String createLabelString = "@attribute " + nameFeatures[numFeature] + " {";
        for (int i = 0; i < numClass - 1; i++) {
            createLabelString += classLabel[i] + ",";
        }
        createLabelString += classLabel[numClass - 1] + "}";
        try {
            FileInputStream fis = new FileInputStream(nameCSV);
            //load CSV File
            CSVLoader loader = new CSVLoader();
            loader.setSource(fis);
            Instances data = loader.getDataSet();
            //load ARFF File
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(tempFile);
            saver.writeBatch();
            //Refine Header ARFF File
            BufferedReader br = new BufferedReader(new FileReader(tempFile));
            FileWriter fw = new FileWriter(nameARFF, false);
            PrintWriter pw = new PrintWriter(fw);
            while (selectLine-- > 1) {
                pw.println(br.readLine());
            }
            pw.println(createLabelString);
            br.readLine();
            while (br.ready()) {
                pw.println(br.readLine());
            }
            br.close();
            fw.close();
            fis.close();
            tempFile.delete();
        } catch (IOException ex) {
            Logger.getLogger(FileFunc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
