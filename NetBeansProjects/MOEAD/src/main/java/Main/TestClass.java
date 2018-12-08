/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author renansantos
 */
public class TestClass {

    void test() throws FileNotFoundException {
//        InputStream in;
//        in = getClass().getResourceAsStream("/home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights/W3D_100.dat");
////        in = getClass().getResourceAsStream("W3D_100.dat");
//        InputStreamReader isr = new InputStreamReader(in);
//        BufferedReader br = new BufferedReader(isr);
//        System.out.println(System.getProperty("java.class.path"));

        File file = new File("/home/renansantos/NetBeansProjects/MOEAD/MOEAD_Weights/W3D_100.dat");
        Scanner scnr = new Scanner(file);
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            System.out.println(line);
            String[] parts = line.split(" ");
            for (int i = 0; i < parts.length; i++) {
                System.out.println(new Double(parts[i]));
            }
        }
    }
}
