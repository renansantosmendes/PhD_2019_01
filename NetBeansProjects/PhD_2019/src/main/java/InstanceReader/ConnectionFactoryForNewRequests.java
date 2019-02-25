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
public class ConnectionFactoryForNewRequests {

    private String password = "";
    private String database = "VRPDRT_Random";
    private String user = "root";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/" + database + "?useLegacyDatetimeCode=false"
                    + "&serverTimezone=UTC", user, password);
            //return DriverManager.getConnection("jdbc:mysql://localhost/instances","root","");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
