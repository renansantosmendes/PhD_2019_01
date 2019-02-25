/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoogleMapsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author renansantos
 */
public class ColorGenerator {

    private static int colorCounter = 0;
    private static List<String> listOfColors = new ArrayList<>();

    public String generatesColor() {
        StringBuilder color = new StringBuilder();
        Random rnd = new Random();

        color.append("0x");
        for (int i = 0; i < 6; i++) {
            int number = rnd.nextInt(16);
            if(number == 10){
                color.append("a");
            }else if(number == 11){
                color.append("b");
            }else if(number == 12){
                color.append("c");
            }else if(number == 13){
                color.append("d");
            }else if(number == 14){
                color.append("e");
            }else if(number == 15){
                color.append("f");
            }else{
                color.append(number);
            }
            
        }
        listOfColors.add(color.toString());
        return color.toString();
    }

}
