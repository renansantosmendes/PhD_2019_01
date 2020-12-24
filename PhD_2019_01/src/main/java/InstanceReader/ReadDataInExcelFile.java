/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Node;
import ProblemRepresentation.Request;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class ReadDataInExcelFile {

    private String filePath;
    private String requestsData;
    private String nodesData;
    private String adjacenciesData;
    private String requestsFile = "instances.xls";//"instance_paper.xls";
    private String nodesFile = "nodes.xls";
    private String adjacenciesFile = "adjacencies.xls";
    private int numberOfNodes = 0;

    public ReadDataInExcelFile(String filePath) {
        this.filePath = filePath;
    }

    public ReadDataInExcelFile(String filePath, String requestsData, String nodesData, String adjacenciesData) {
        this.filePath = filePath;
        this.requestsData = requestsData;
        this.nodesData = nodesData;
        this.adjacenciesData = adjacenciesData;
    }

    public ReadDataInExcelFile(String filePath, Instance instance) {
        this.filePath = filePath;
        instance.buildInstaceNames();
        this.requestsData = instance.getInstanceName();
        this.nodesData = instance.getNodesData();
        this.adjacenciesData = instance.getAdjacenciesData();
    }

    public void saveData(List<Request> requests) {
        requests.forEach(r -> System.out.println(r.getStringToFile()));
    }

    public List<Request> getRequests(){
        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(this.filePath + this.requestsFile), conf);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(requestsData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<Request> requests = new ArrayList<>();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            Cell origin = sheet.getCell(1, i);
            Cell destination = sheet.getCell(2, i);
            Cell pickupTimeWindowLower = sheet.getCell(3, i);
            Cell pickupTimeWindowUpper = sheet.getCell(4, i);
            Cell deliveryTimeWindowLower = sheet.getCell(5, i);
            Cell deliveryTimeWindowUpper = sheet.getCell(6, i);
            try{
                Request request = new Request(Integer.parseInt(id.getContents()),
                        Integer.parseInt(origin.getContents()),
                        Integer.parseInt(destination.getContents()),
                        Integer.parseInt(pickupTimeWindowLower.getContents()),
                        Integer.parseInt(pickupTimeWindowUpper.getContents()),
                        Integer.parseInt(deliveryTimeWindowLower.getContents()),
                        Integer.parseInt(deliveryTimeWindowUpper.getContents()));

                requests.add(request);
            }
            catch(Exception e){
                System.out.println(e);
            }

        }
        return requests;
    }

    public Set<Integer> getSetOfNodes() {
        Set<Integer> nodesSet = new HashSet<>();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(this.filePath + this.nodesFile), conf);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(nodesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            nodesSet.add(Integer.parseInt(id.getContents()));
        }
        this.numberOfNodes = nodesSet.size();
        return nodesSet;
    }

    public List<Node> getListOfNodes()  {
        List<Node> nodes = new ArrayList<>();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File(this.filePath + this.nodesFile), conf);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
        //workbook = tryToReadWorkbook(workbook, conf);
        Sheet sheet = workbook.getSheet(nodesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            Cell latitude = sheet.getCell(1, i);
            Cell longitude = sheet.getCell(2, i);
            Cell address = sheet.getCell(3, i);

            Node node = new Node(Integer.parseInt(id.getContents()), Double.parseDouble(latitude.getContents()),
                    Double.parseDouble(longitude.getContents()), address.getContents());
            nodes.add(node);
        }
        this.numberOfNodes = nodes.size();
        return nodes;
    }

    public List<List<Long>> getAdjacenciesListOfDistances() {

        initializaNumberOfNodesIfEqualsZero();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = null;
        workbook = tryToReadWorkbook(workbook, conf);
        Sheet sheet = workbook.getSheet(adjacenciesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<List<Long>> distanceBetweenNodes = new LinkedList<>();
        initializeAdjacenciesWithZeros(distanceBetweenNodes);

        for (int i = 1; i < rows; i++) {
            Integer originNode = Integer.parseInt(sheet.getCell(0, i).getContents());//)resultSet.getInt("originNode");
            Integer destinationNode = Integer.parseInt(sheet.getCell(1, i).getContents());
            Long distanceTo = (long) Double.parseDouble(sheet.getCell(3, i).getContents());
            distanceBetweenNodes.get(originNode).set(destinationNode, distanceTo);

        }

        return distanceBetweenNodes;
    }

    private Workbook tryToReadWorkbook(Workbook workbook, WorkbookSettings conf) {
        try {
            workbook = Workbook.getWorkbook(new File(this.filePath + this.adjacenciesFile), conf);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
        return workbook;
    }

    private void initializeAdjacenciesWithZeros(List<List<Long>> adjacencie) {
        for (int i = 0; i < numberOfNodes; i++) {
            adjacencie.add(new LinkedList<Long>());
            for (int j = 0; j < numberOfNodes; j++) {
                long zero = 0;
                adjacencie.get(i).add(zero);
            }
        }
    }

    private void initializaNumberOfNodesIfEqualsZero() {
        if (this.numberOfNodes == 0) {
            getListOfNodes();
        }
    }

    public List<List<Long>> getAdjacenciesListOfTimes() {

        initializaNumberOfNodesIfEqualsZero();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = null;
        workbook = tryToReadWorkbook(workbook, conf);
        Sheet sheet = workbook.getSheet(adjacenciesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<List<Long>> timeBetweenNodes = new LinkedList<>();
        initializeAdjacenciesWithZeros(timeBetweenNodes);

        for (int i = 1; i < rows; i++) {
            Integer originNode = Integer.parseInt(sheet.getCell(0, i).getContents());//)resultSet.getInt("originNode");
            Integer destinationNode = Integer.parseInt(sheet.getCell(1, i).getContents());
            Long timeTo = (long) Double.parseDouble(sheet.getCell(2, i).getContents()) / 60;
            timeBetweenNodes.get(originNode).set(destinationNode, timeTo);
        }

        return timeBetweenNodes;
    }

    public int getNumberOfNodes(){
        return this.getSetOfNodes().size();
    }

}
