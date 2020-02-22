/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoogleMapsApi;

import InstanceReader.ConnectionFactory;
import ProblemRepresentation.Node;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author renansantos
 */
public class DataUpdaterUsingGoogleMapsApi {

    String directionsApiKey;
    String distanceMatrixApiKey;
    List<Node> nodesList;
    private Connection connection;
    private String adjacenciesTableInDataBase;

    public DataUpdaterUsingGoogleMapsApi(String directionsApiKey, List<Node> nodesList, String adjacenciesTableInDataBase) {
        this.connection = new ConnectionFactory().getConnection();
        this.directionsApiKey = directionsApiKey;
        this.distanceMatrixApiKey = distanceMatrixApiKey;
        this.nodesList = nodesList;
        this.adjacenciesTableInDataBase = adjacenciesTableInDataBase;
    }

    private void deleteAllDataInAdjacenciesTable() {
        String sql = "truncate " + this.adjacenciesTableInDataBase;
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAdjacenciesData() throws ApiException, InterruptedException, InterruptedException, InterruptedException, InterruptedException, IOException {
        this.deleteAllDataInAdjacenciesTable();
        for (int i = 0; i < nodesList.size(); i++) {
            for (int j = 0; j < nodesList.size(); j++) {
                Node origin = nodesList.get(i);
                Node destination = nodesList.get(j);
                System.out.println("Origin = " + i + " Destination = " + j);
                if (i != j) {
                    addDataFromGoogleMapsApi(origin, destination);
                } else {
                    addDataToTheSameNode(origin, destination);
                }
            }
        }
    }

    private void addDataToTheSameNode(Node origin, Node destination) {
        insertDataInAdjacenciesTable(origin, destination, 0, 0, "");
    }

    private void addDataFromGoogleMapsApi(Node origin, Node destination) throws ApiException {
        //Add data downloaded from the Google Maps API
        String folder;
        folder = "RouteData";
        boolean success = (new File(folder)).mkdirs();
        try {
            GeoApiContext geoApiContext = new GeoApiContext();
            geoApiContext.setApiKey(directionsApiKey);
            DirectionsApiRequest directionsApiRequest = DirectionsApi.getDirections(geoApiContext, origin.getLatLng(),
                    destination.getLatLng());
            directionsApiRequest.alternatives(true);

            DirectionsResult routesBetweenNodes = directionsApiRequest.await();

            List<DirectionsRoute> routesList = new ArrayList<>();
            DirectionsRoute[] routes = routesBetweenNodes.routes;

            for (int i = 0; i < routes.length; i++) {
                routesList.add(routes[i]);
            }
            routesList.sort(Comparator.comparing(u -> u.legs[0].distance.inMeters));
            DirectionsRoute smallerRouteBetweenNodes = routesList.get(0);
            Long routeDuration = smallerRouteBetweenNodes.legs[0].duration.inSeconds;
            Long routeDistance = smallerRouteBetweenNodes.legs[0].distance.inMeters;
            String routePolyline = smallerRouteBetweenNodes.overviewPolyline.toString();
            insertDataInAdjacenciesTable(origin, destination, routeDuration, routeDistance,
                    smallerRouteBetweenNodes.overviewPolyline.getEncodedPath());
            //System.out.println(routesBetweenNodes.routes[0].overviewPolyline.getEncodedPath());

        } catch (InterruptedException ex) {
            Logger.getLogger(DataUpdaterUsingGoogleMapsApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataUpdaterUsingGoogleMapsApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertDataInAdjacenciesTable(Node origin, Node destination, long totalTravelTime, long totalDistance) {
        String sql = "insert into " + this.adjacenciesTableInDataBase + " values (?,?,?,?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, origin.getNodeId().toString());
            statement.setString(2, destination.getNodeId().toString());
            statement.setString(3, Long.toString(totalTravelTime));
            statement.setString(4, Double.toString(totalDistance));
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertDataInAdjacenciesTable(Node origin, Node destination, long totalTravelTime, long totalDistance,
            String polyline) {
        String sql = "insert into " + this.adjacenciesTableInDataBase + " values (?,?,?,?,?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, origin.getNodeId().toString());
            statement.setString(2, destination.getNodeId().toString());
            statement.setString(3, Long.toString(totalTravelTime));
            statement.setString(4, Double.toString(totalDistance));
            statement.setString(5, polyline);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
