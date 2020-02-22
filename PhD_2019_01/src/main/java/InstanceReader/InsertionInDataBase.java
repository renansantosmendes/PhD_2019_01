/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Request;
import java.util.*;

/**
 *
 * @author renansantos
 */
public class InsertionInDataBase {

    private List<Request> requests1;
    private List<Request> requests2;
    private List<Request> requests3;
    private String instance1 = "r250n12tw03";
    private String instance2 = "r250n12tw05";
    private String instance3 = "r250n12tw10";
    private String numberOfRequests[] = {"50", "100", "150", "200", "250"};
    private String timeWindows[] = {"03", "05", "10"};// "03"-> removed
    private String numberOfNodes = "12";

    public InsertionInDataBase() {
        this.readData();
        this.insertData();
    }

    private void readData() {
        this.requests1 = new RequestDAO(this.instance1).getListOfRequest();
        this.requests2 = new RequestDAO(this.instance2).getListOfRequest();
        this.requests3 = new RequestDAO(this.instance3).getListOfRequest();
    }

    public void insertData() {
        Collections.shuffle(requests1);
        Collections.shuffle(requests2);
        Collections.shuffle(requests3);

        List<List<Request>> listOfRequests = new ArrayList<>();

        listOfRequests.add(requests1);
        listOfRequests.add(requests2);
        listOfRequests.add(requests3);

        for (int i = 0; i < listOfRequests.size(); i++) {
            for (int k = 50; k <= 250; k = k + 50) {
                //for (int l = 0; l < timeWindows.length; l++) {
                String requestsInstance;
                List<Request> requestsToDataBase = new ArrayList<>();
                requestsToDataBase.addAll(listOfRequests.get(i).subList(0, k));
                //listOfRequests.get(i).subList(0, k).forEach(System.out::println);
                if (k == 50) {
                    requestsInstance = "r0" + k + "n" + numberOfNodes + "tw" + timeWindows[i];
                } else {
                    requestsInstance = "r" + k + "n" + numberOfNodes + "tw" + timeWindows[i];
                }

                System.out.println("Instance = " + requestsInstance);
                RequestDAOForDataInsertion requestDAO = new RequestDAOForDataInsertion(requestsInstance);
                requestsToDataBase.forEach(r -> requestDAO.addRequestIntoDataBase(r));
                //System.out.println("Instance = " + requestsInstance);
                // }
            }
        }

    }
}
