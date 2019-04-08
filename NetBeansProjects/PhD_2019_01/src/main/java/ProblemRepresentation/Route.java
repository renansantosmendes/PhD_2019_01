package ProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

public class Route implements Comparable<Route> {

    private List<Integer> nodesVisitationList;
    private List<Integer> vehicleOccupationWhenLeavesNode;
    private List<Long> timeListTheVehicleLeavesTheNode;
    private List<Request> requestAttendanceList;
    private Integer tempoExtra;
    private double occupationRate;

    public Route() {
        nodesVisitationList = new ArrayList<Integer>();
        vehicleOccupationWhenLeavesNode = new ArrayList<Integer>();
        timeListTheVehicleLeavesTheNode = new ArrayList<Long>();
        requestAttendanceList = new ArrayList<Request>();
    }

    public Route(Route route) {
        nodesVisitationList = new ArrayList<Integer>(route.getNodesVisitationList());
        vehicleOccupationWhenLeavesNode = new ArrayList<Integer>(route.getVehicleOccupationWhenLeavesNode());
        timeListTheVehicleLeavesTheNode = new ArrayList<Long>(route.getTimeListTheVehicleLeavesTheNode());
        requestAttendanceList = new ArrayList<Request>(route.getRequestAttendanceList());
        occupationRate = route.getOccupationRate();
    }

    public Route(List<Integer> nodesVisitationList, List<Integer> vehicleOccupationWhenLeavesNode, 
            List<Long> timeListTheVehicleLeavesTheNode, List<Request> requestAttendanceList, Integer tempoExtra, 
            double occupationRate) {
        
        this();
        this.nodesVisitationList.clear();
        this.nodesVisitationList.addAll(nodesVisitationList);
        
        this.vehicleOccupationWhenLeavesNode.clear();
        this.vehicleOccupationWhenLeavesNode.addAll(vehicleOccupationWhenLeavesNode);
        
        this.timeListTheVehicleLeavesTheNode.clear();
        this.timeListTheVehicleLeavesTheNode.addAll(timeListTheVehicleLeavesTheNode);
        
        this.requestAttendanceList.clear();
        this.requestAttendanceList.addAll(requestAttendanceList);
        this.tempoExtra = tempoExtra;
        this.occupationRate = occupationRate;
    }
    
    

    public List<Integer> getNodesVisitationList() {
        return nodesVisitationList;
    }

    public void setNodesVisitationList(List<Integer> nodesVisitationList) {
        this.nodesVisitationList.clear();
        this.nodesVisitationList.addAll(nodesVisitationList);
    }

    public List<Integer> getVehicleOccupationWhenLeavesNode() {
        return vehicleOccupationWhenLeavesNode;
    }

    public void setVehicleOccupationWhenLeavesNode(List<Integer> vehicleOccupationWhenLeavesNode) {
        this.vehicleOccupationWhenLeavesNode.clear();
        this.vehicleOccupationWhenLeavesNode.addAll(vehicleOccupationWhenLeavesNode);
    }

    public List<Long> getTimeListTheVehicleLeavesTheNode() {
        return timeListTheVehicleLeavesTheNode;
    }

    public void setTimeListTheVehicleLeavesTheNode(List<Long> timeListTheVehicleLeavesTheNode) {
        this.timeListTheVehicleLeavesTheNode.clear();
        this.timeListTheVehicleLeavesTheNode.addAll(timeListTheVehicleLeavesTheNode);
    }

    public List<Request> getRequestAttendanceList() {
        return requestAttendanceList;
    }

    public void setRequestAttendanceList(List<Request> requestAttendanceList) {
        this.requestAttendanceList.clear();
        this.requestAttendanceList.addAll(new ArrayList<Request>(requestAttendanceList));
    }

    public Integer getLastNode() {
        int position = nodesVisitationList.size() - 1;
        return nodesVisitationList.get(position);
    }

    public Integer getActualOccupation() {
        int posicao = vehicleOccupationWhenLeavesNode.size() - 1;
        return vehicleOccupationWhenLeavesNode.get(posicao);
    }

    public Long getActualMoment() {
        int position = timeListTheVehicleLeavesTheNode.size() - 1;
        return timeListTheVehicleLeavesTheNode.get(position);
    }

    public Integer getTempoExtra() {
        return tempoExtra;
    }

    public double getOccupationRate() {
        return occupationRate;
    }

    public void setTempoExtra(Integer tempo) {
        tempoExtra = tempo;
    }

    public void setOccupationRate(double occupationRate) {
        this.occupationRate = occupationRate;
    }

    public void setDepartureTimeFromDepot(long horario) {
        timeListTheVehicleLeavesTheNode.set(0, horario);
    }

    public void calculateOccupationRate(int vehicleCapacity) {
        this.setOccupationRate(this.getVehicleOccupationWhenLeavesNode().stream()
                .mapToDouble(Integer::valueOf).max()
                .getAsDouble() / vehicleCapacity);
    }

    public void addVisitedNodes(Integer visitedNode) {
        nodesVisitationList.add(visitedNode);

        int posicao = vehicleOccupationWhenLeavesNode.size() - 1;
        int lotacao;

        if (posicao >= 0) {
            lotacao = vehicleOccupationWhenLeavesNode.get(posicao);
            vehicleOccupationWhenLeavesNode.add(lotacao);
        } else {
            vehicleOccupationWhenLeavesNode.add(0);
        }

        timeListTheVehicleLeavesTheNode.add((long) -1);
    }

    public void boardPassenger(Request request, Long horario) {
        int posicao = vehicleOccupationWhenLeavesNode.size() - 1;
        int lotacao = vehicleOccupationWhenLeavesNode.get(posicao);

        vehicleOccupationWhenLeavesNode.set(posicao, lotacao + 1);

        timeListTheVehicleLeavesTheNode.set(posicao, horario);

        request.setPickupTime(horario);
        addAttendedRequest(request);
    }

    public void leavePassenger(Request request, long horario) {
        int posicao = vehicleOccupationWhenLeavesNode.size() - 1;
        if (posicao == -1 || posicao != timeListTheVehicleLeavesTheNode.size() - 1) {
            System.out.println("POSICAO INVALIDA");
        }
        int lotacao = vehicleOccupationWhenLeavesNode.get(posicao);
        vehicleOccupationWhenLeavesNode.set(posicao, lotacao - 1);
        timeListTheVehicleLeavesTheNode.set(posicao, horario);

        int posListaAtendimento = getRequestAttendanceList().indexOf(request);
        if (posListaAtendimento == -1) {
            System.out.println("O CARA " + request);//EIN???????
        }
        Request reqArmazenada = getRequestAttendanceList().get(posListaAtendimento);

        reqArmazenada.setDeliveryTime(horario);
        getRequestAttendanceList().set(posListaAtendimento, reqArmazenada);
    }

    public void addAttendedRequest(Request request) {
        requestAttendanceList.add((Request) request.clone());
    }

    public void removeAttendedRequest(Request request) {
        requestAttendanceList.remove((Request) request.clone());
    }

    @Override
    public String toString() {
        String s = "";

        for (Integer v : nodesVisitationList) {
            s += v + " ";
        }
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Route && equals((Route) obj);
    }

    public boolean equals(Route rota2) {
        if (this == rota2) {
            return true;
        }

        if (rota2 == null) {
            return false;
        }

        if (nodesVisitationList.size() != rota2.getNodesVisitationList().size()) {
            return false;
        }

        for (int i = 0; i < nodesVisitationList.size(); i++) {
            if (nodesVisitationList.get(i) != rota2.getNodesVisitationList().get(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {

        if (nodesVisitationList == null) {
            return -1;
        }

        int hash = 0;
        String s = "";

        for (Integer i : nodesVisitationList) {
            s += i.toString();
        }

        hash = s.hashCode();
        //return hash;
        //System.out.println(this.getRequestAttendanceList().get(0).getId().hashCode());
        return this.getRequestAttendanceList().get(0).getId().hashCode();
    }

    @Override
    public int compareTo(Route r) {
        if (this.getRequestAttendanceList().size() > r.getRequestAttendanceList().size()) {
            return 1;
        }
        if (this.getRequestAttendanceList().size() < r.getRequestAttendanceList().size()) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Object clone(){
        
        List<Request> requestListClone = new ArrayList<>();
        
        for(Request request: this.requestAttendanceList){
            requestListClone.add(request);
        }
        
        return new Route( nodesVisitationList, vehicleOccupationWhenLeavesNode, 
            timeListTheVehicleLeavesTheNode, requestListClone,  tempoExtra, 
             occupationRate);
    }

}
