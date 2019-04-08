/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author renansantos
 */
public class ScriptGenerator {

    private String instance;
    private String instanceFullName;
    private String fileName;
    private String folder = "scripts_for_cluster_executions";

    public ScriptGenerator(String instance, String instanceSize, Integer vehicleCapacity) {
        this.instance = instance;
        this.instanceFullName = instance + "k" + vehicleCapacity + instanceSize;
        this.fileName = "script_" + this.instanceFullName + ".sh";
        createFolder();
    }
    
    public ScriptGenerator(String fileName, String executableName) {
        this.instanceFullName = executableName;
        this.fileName = "script_" + fileName + ".sh";
        createFolder();
    }

    private void createFolder() {
        boolean success = (new File(folder)).mkdirs();
        if (!success) {
            System.out.println("Folder already exists!");
        }
    }

    public void generate(String timeOfExecution, String partition) {
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(folder + "/" + fileName);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        printStream.print("#!/bin/bash \n");
        printStream.print("#SBATCH --qos=part" + timeOfExecution + "\n");
        printStream.print("#SBATCH --partition=" + partition + "\n");
        printStream.print("module load jdk8_32" + "\n");
        printStream.print("java -jar " + this.instanceFullName + ".jar");
    }
}
