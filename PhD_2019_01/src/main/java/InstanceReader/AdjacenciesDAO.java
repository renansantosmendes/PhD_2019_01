/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author renansantos
 */
public class AdjacenciesDAO {

    private Connection connection;
    private String adjacenciesTable;
    private String nodesTable;

    public AdjacenciesDAO(String adjacenciesTable, String nodesTable) {
        this.connection = new ConnectionFactory().getConnection();
        this.adjacenciesTable = adjacenciesTable;
        this.nodesTable = nodesTable;
    }

    public void addAdjacenciesIntoDataBase(int numberOfNodes, List<List<Long>> time, List<List<Long>> distance) {
        String sql = "insert into " + this.adjacenciesTable + "(originNode, destinationNode, timeTo, distanceTo, overviewPolyline)"
                + " values (?,?,?,?,?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    statement.setString(1, Integer.toString(i));
                    statement.setString(2, Integer.toString(j));
                    statement.setString(3, Long.toString(time.get(i).get(j) * 60));
                    statement.setString(4, Long.toString(distance.get(i).get(j)));
                    statement.execute();
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfNodes() {
        try {
            String sql = "select count(nodeId) from " + this.nodesTable;
            PreparedStatement statement = this.connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            int numberOfNodes = 0;
            while (resultSet.next()) {
                numberOfNodes = resultSet.getInt("count(nodeId)");
            }
            resultSet.close();
            statement.close();
            return numberOfNodes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<List<Long>> getAdjacenciesListOfDistances() {
        try {
            int numberOfNodes = this.getNumberOfNodes();
            List<List<Long>> distanceBetweenNodes = new LinkedList<>();
            for (int i = 0; i < numberOfNodes; i++) {
                distanceBetweenNodes.add(new LinkedList<Long>());
                for (int j = 0; j < numberOfNodes; j++) {
                    long zero = 0;
                    distanceBetweenNodes.get(i).add(zero);
                }
            }
            String sql = "select * from " + this.adjacenciesTable;
            PreparedStatement statement = this.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer originNode = resultSet.getInt("originNode");
                Integer destinationNode = resultSet.getInt("destinationNode");
                Long distanceTo = resultSet.getLong("distanceTo");
                distanceBetweenNodes.get(originNode).set(destinationNode, distanceTo);
            }
            resultSet.close();
            statement.close();
            return distanceBetweenNodes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Long>> getAdjacenciesListOfTimes() {
        try {
            int numberOfNodes = this.getNumberOfNodes();
            List<List<Long>> timeBetweenNodes = new LinkedList<>();
            for (int i = 0; i < numberOfNodes; i++) {
                timeBetweenNodes.add(new LinkedList<Long>());
                for (int j = 0; j < numberOfNodes; j++) {
                    long zero = 0;
                    timeBetweenNodes.get(i).add(zero);
                }
            }
            String sql = "select * from " + this.adjacenciesTable;
            PreparedStatement statement = this.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer originNode = resultSet.getInt("originNode");
                Integer destinationNode = resultSet.getInt("destinationNode");
//                System.out.println(resultSet.getLong("timeTo"));
//                System.out.println(Math.round((resultSet.getLong("timeTo")) / 60));
                long timeTo = Math.round((resultSet.getLong("timeTo")) / 60);
                //long timeTo = resultSet.getLong("timeTo");
                timeBetweenNodes.get(originNode).set(destinationNode, timeTo);
            }
            resultSet.close();
            statement.close();
            return timeBetweenNodes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPolylineBetweenNodes(int origin, int destination) {
        try {
            String sql = "select overviewPolyline from " + this.adjacenciesTable + " where originNode = " + origin
                    + " and destinationNode = " + destination;
            PreparedStatement statement = this.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String polyline = null;
            while (resultSet.next()) {
                polyline = resultSet.getString("overviewPolyline");
            }
            resultSet.close();
            statement.close();
            this.connection.close();
            return polyline;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
}
