/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author renansantos
 */
public class ConnectionFactory {

    private String password = "dnweapons";//93.188.160.206
    private String database = "VRPDRT";//u634792811_renan
    private String user = "root";//u634792811_renan

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/" + database + "?useLegacyDatetimeCode=false"
                    + "&serverTimezone=UTC", user, password);
//            return DriverManager.getConnection("jdbc:mysql://localhost/"+ database +"?useLegacyDatetimeCode=false"
//                    + "&serverTimezone=UTC",user,password);
            //return DriverManager.getConnection("jdbc:mysql://localhost/instances","root","");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
