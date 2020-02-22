/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author renansantos
 */
public class NodeDAO {

    private Connection connection;
    private String tableInDataBase;

    public NodeDAO(String tableInDataBase) {
        this.connection = new ConnectionFactory().getConnection();
        this.tableInDataBase = tableInDataBase;
    }

    public Set<Integer> getSetOfNodes() {
        try {
            Set<Integer> setOfNodes = new HashSet<>();
            PreparedStatement stmt = this.connection.prepareStatement("select nodeId from " + tableInDataBase);
            
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Integer node = resultSet.getInt("nodeId");
                setOfNodes.add(node);
            }
            resultSet.close();
            stmt.close();
            return setOfNodes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    public List<Node> getListOfNodes() {
        try {

            List<Node> listOfNodes = new ArrayList<>();
            PreparedStatement stmt = this.connection.prepareStatement("select * from "+ tableInDataBase);
            //PreparedStatement stmt = this.connection.prepareStatement("select * from Nodes");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               Node node = new Node(rs.getInt("nodeId"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("adress"));               
               listOfNodes.add(node);
            }
            rs.close();
            stmt.close();
            return listOfNodes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
