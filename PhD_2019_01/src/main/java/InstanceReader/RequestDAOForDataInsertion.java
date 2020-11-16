/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author renansantos
 */
public class RequestDAOForDataInsertion {
    private Connection connection;
    private String instanceName;

    public RequestDAOForDataInsertion(String instanceName) {
        this.connection = new ConnectionFactoryForNewRequests().getConnection();
        this.instanceName = instanceName;
    }

    public void addRequestIntoDataBase(Request request) {
        String sql = "insert into " + this.instanceName + " values (?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);

            statement.setString(1, null);
            statement.setString(2, Integer.toString(request.getOrigin()));
            statement.setString(3, Integer.toString(request.getDestination()));
            LocalDateTime pickUpTimeWindowLower, pickUpTimeWindowUpper, deliveryTimeWindowLower, deliveryTimeWindowUpper;
            LocalDateTime pickUpTime = null;
            LocalDateTime requestDay = null;
            //pickUpTimeWindowLower = LocalDateTime.of(2017, 1, 1, (int) request.getPickupTimeWIndowLower()/60, (int) request.getPickupTimeWIndowLower()%60);
            pickUpTimeWindowLower = LocalDateTime.of(2017, 1, 1, (int) request.getPickupTimeWindowLower() / 60,
                    (int) request.getPickupTimeWindowLower() % 60);
            pickUpTimeWindowUpper = LocalDateTime.of(2017, 1, 1, (int) request.getPickupTimeWindowUpper() / 60,
                    (int) request.getPickupTimeWindowUpper() % 60);
            deliveryTimeWindowLower = LocalDateTime.of(2017, 1, 1, (int) request.getDeliveryTimeWindowLower() / 60,
                    (int) request.getDeliveryTimeWindowLower() % 60);
            deliveryTimeWindowUpper = LocalDateTime.of(2017, 1, 1, (int) request.getDeliveryTimeWindowUpper() / 60,
                    (int) request.getDeliveryTimeWindowUpper() % 60);

            statement.setString(4, pickUpTimeWindowLower.toLocalTime().toString());
            statement.setString(5, pickUpTimeWindowUpper.toLocalTime().toString());
            statement.setString(6, deliveryTimeWindowLower.toLocalTime().toString());
            statement.setString(7, deliveryTimeWindowUpper.toLocalTime().toString());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
