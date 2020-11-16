/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ProblemRepresentation.Request;

/**
 *
 * @author renansantos
 */
public class RequestDAO {

    private Connection connection;
    private String instanceName;

    public RequestDAO(String instanceName) {
        this.connection = new ConnectionFactory().getConnection();
        this.instanceName = instanceName;
    }

    public void addRequestIntoDataBase(Request request) {
        //r250n12tw03
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

    public void addRequestIntoDataBaseVRPDRTSD(Request request) {
        String sql = "insert into VRPDRTSD_requests250 "
                + "(id, passengerOrigin, passengerDestination, requestDay, pickUpTime, "
                + "deliveryTimeWindowLower, deliveryTimeWindowUpper)"
                + " values (?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);

            statement.setString(1, request.getId().toString());
            statement.setString(2, Integer.toString(request.getOrigin()));
            statement.setString(3, Integer.toString(request.getDestination()));
            LocalDateTime pickUpTimeWindowLower, pickUpTimeWindowUpper, deliveryTimeWindowLower, deliveryTimeWindowUpper;
            LocalDateTime pickUpTime = LocalDateTime.of(2017, 1, 1, 0, 0);
            LocalDateTime requestDay = LocalDateTime.of(2017, 1, 1, 0, 0);
            //pickUpTimeWindowLower = LocalDateTime.of(2017, 1, 1, (int) request.getPickupTimeWIndowLower()/60, (int) request.getPickupTimeWIndowLower()%60);
            pickUpTimeWindowLower = LocalDateTime.of(2017, 1, 1, (int) request.getPickupTimeWindowLower() / 60,
                    (int) request.getPickupTimeWindowLower() % 60);
            pickUpTimeWindowUpper = LocalDateTime.of(2017, 1, 1, (int) request.getPickupTimeWindowUpper() / 60,
                    (int) request.getPickupTimeWindowUpper() % 60);
            deliveryTimeWindowLower = LocalDateTime.of(2017, 1, 1, (int) request.getDeliveryTimeWindowLower() / 60,
                    (int) request.getDeliveryTimeWindowLower() % 60);
            deliveryTimeWindowUpper = LocalDateTime.of(2017, 1, 1, (int) request.getDeliveryTimeWindowUpper() / 60,
                    (int) request.getDeliveryTimeWindowUpper() % 60);

            statement.setString(4, requestDay.toString());
            statement.setString(5, pickUpTime.toLocalTime().toString());
            statement.setString(6, deliveryTimeWindowLower.toLocalTime().toString());
            statement.setString(7, deliveryTimeWindowUpper.toLocalTime().toString());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getListOfRequest() {
        try {
            String sql = "select * from " + this.instanceName;
            List<Request> listOfRequests = new ArrayList<Request>();
            PreparedStatement statement = this.connection.prepareStatement(sql);

            ResultSet resultSetForRequests = statement.executeQuery();

            while (resultSetForRequests.next()) {
                int valueAdded = 0;
                Integer requestId = resultSetForRequests.getInt("id");
                Integer passengerOrigin = resultSetForRequests.getInt("passengerOrigin");
                Integer passengerDestination = resultSetForRequests.getInt("passengerDestination");
                
                LocalDateTime teste = LocalDateTime.of(LocalDate.now(), resultSetForRequests.getTime("pickUpTimeWindowLower").toLocalTime());
                
                Integer pickUpTimeWindowLower = 60 * (resultSetForRequests.getTime("pickUpTimeWindowLower").toLocalTime().getHour())
                        + resultSetForRequests.getTime("pickUpTimeWindowLower").toLocalTime().getMinute() + valueAdded;

                Integer pickUpTimeWindowUpper = 60 * (resultSetForRequests.getTime("pickUpTimeWindowUpper").toLocalTime().getHour())
                        + resultSetForRequests.getTime("pickUpTimeWindowUpper").toLocalTime().getMinute() + valueAdded;

                Integer deliveryTimeWindowLower = 60 * (resultSetForRequests.getTime("deliveryTimeWindowLower").toLocalTime().getHour())
                        + resultSetForRequests.getTime("deliveryTimeWindowLower").toLocalTime().getMinute() + valueAdded;

                Integer deliveryTimeWindowUpper = 60 * (resultSetForRequests.getTime("deliveryTimeWindowUpper").toLocalTime().getHour())
                        + resultSetForRequests.getTime("deliveryTimeWindowUpper").toLocalTime().getMinute() + valueAdded;
                Request request = new Request(requestId, passengerOrigin, passengerDestination, pickUpTimeWindowLower,
                        pickUpTimeWindowUpper, deliveryTimeWindowLower, deliveryTimeWindowUpper);
//                Request request = new Request(requestId, passengerOrigin, passengerDestination, pickUpTimeWindowLower,
//                        deliveryTimeWindowLower);
                listOfRequests.add(request);
            }
            resultSetForRequests.close();
            statement.close();
            this.connection.close();
            return listOfRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
