/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

//import GoogleMapsApi.GoogleMapsRoute;
//import static GoogleMapsApi.GoogleMapsRoute.FileExtension.json;
//import static GoogleMapsApi.GoogleMapsRoute.TravelMode.driving;
import ProblemRepresentation.Node;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author renansantos
 */
public class DataUpdaterDAO {

    private Connection connection;
    private String tableInDataBase;

    public DataUpdaterDAO(String tableInDataBase) {
        this.connection = new ConnectionFactory().getConnection();
        this.tableInDataBase = tableInDataBase;
    }

    public void updateAdjacenciesData(List<Node> listOfNodes) throws MalformedURLException, IOException {
        this.deleteAllDataInAdjacenciesTable();
        for (int i = 0; i < listOfNodes.size(); i++) {
            for (int j = 0; j < listOfNodes.size(); j++) {
                Node origin = listOfNodes.get(i);
                Node destination = listOfNodes.get(j);
                System.out.println("Origin = " + i + " Destination = " + j);
                if (i != j) {
                    //Add data downloaded from the Google Maps API
                    String folder;
                    folder = "RouteData";
                    boolean success = (new File(folder)).mkdirs();

                    //GoogleMapsRoute route = new GoogleMapsRoute(json, folder + "/route_" + origin.getNodeId().toString() + "_"
                    //        + destination.getNodeId().toString(), driving, origin, destination, listOfNodes);
                    //route.downloadDataFile();
                    //route.getDataFromFile();
                    //int totalTravelTime = route.getTotalRouteTimeInSeconds();
                    //double totalDistance = route.getTotalRouteDistanceInMeters();
                    //insertDataInAdjacenciesTable(origin, destination, totalTravelTime, totalDistance);
                } else {
                    //Add data related to the same node
                    insertDataInAdjacenciesTable(origin, destination, 0, 0);
                }
            }
        }
    }

    private void insertDataInAdjacenciesTable(Node origin, Node destination, int totalTravelTime, double totalDistance) {
        String sql = "insert into " + this.tableInDataBase + " values (?,?,?,?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);

            statement.setString(1, origin.getNodeId().toString());
            statement.setString(2, destination.getNodeId().toString());
            statement.setString(3, Integer.toString(totalTravelTime));
            statement.setString(4, Double.toString(totalDistance));
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteAllDataInAdjacenciesTable() {
        String sql = "truncate " + this.tableInDataBase;
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
