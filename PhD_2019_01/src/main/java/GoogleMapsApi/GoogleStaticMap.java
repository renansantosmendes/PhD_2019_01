/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoogleMapsApi;

import InstanceReader.AdjacenciesDAO;
import ProblemRepresentation.Node;
import ProblemRepresentation.Request;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author renansantos
 */
public class GoogleStaticMap {

    private List<Node> nodesList;
    private final String URLRoot = "https://maps.googleapis.com/maps/api/staticmap?center=";
    private String directionsApiKey = "";
    private String staticMapKey = "";
    private StringBuilder stringOfNodes = new StringBuilder();
    private StringBuilder polylines = new StringBuilder();
    private StringBuilder polylinesForAllRotes = new StringBuilder();
    private String mapCenter;
    private String city;
    private String state;
    private String country;
    private int zoom;
    private int scale = 2;
    private int width = 1200;
    private int height = 1200;
    private String mapType = "roadmap";
    private int weight = 5;
    private StringBuilder waypoints = new StringBuilder();
    private StringBuilder encodedPolylines = new StringBuilder();
    private StringBuilder pathGeneratedForAllRoutes = new StringBuilder();
    private static int totalOfRoutes = 0;
    private String staticMapsFolder = "StaticMaps";
    private Set<List<Integer>> routesOfStopPoints;
    private Set<List<Request>> routesOfRequests;
    private List<Integer> route;
    private String adjacenciesTable;
    private String nodesTable;

    public GoogleStaticMap(List<Node> nodesList, String adjacenciesTable, String nodesTable) throws IOException {
        this.nodesList = nodesList;
        this.route = route;
        this.nodesTable = nodesTable;
        String folder;
        folder = "RouteDataForStaticMap";
        readApiKeysFromConfigFile();
        boolean successForCreateDataFolder = (new File(folder)).mkdirs();
        boolean successForCreateStaticMapsFolder = (new File(staticMapsFolder)).mkdirs();

        buildStringWithNodeMarkets();
    }

    private void readApiKeysFromConfigFile() {
        this.staticMapKey = this.getApiKeyFromFile("staticMapKey", ".\\resources\\config.json");
        this.directionsApiKey = this.getApiKeyFromFile("directionsKey", ".\\resources\\config.json");
    }

    public GoogleStaticMap(List<Node> nodesList, List<Integer> route, String adjacenciesTable, String nodesTable)
            throws IOException {

        this.nodesList = nodesList;
        this.route = route;
        this.nodesTable = nodesTable;
        String folder;
        folder = "RouteDataForStaticMap";
        readApiKeysFromConfigFile();
        boolean successForCreateDataFolder = (new File(folder)).mkdirs();
        boolean successForCreateStaticMapsFolder = (new File(staticMapsFolder)).mkdirs();
        if (totalOfRoutes == 0) {
            if (!successForCreateStaticMapsFolder) {
                deleteDir(new File(staticMapsFolder));
                successForCreateStaticMapsFolder = (new File(staticMapsFolder)).mkdirs();
            }
        }
        totalOfRoutes++;
        buildStringWithNodeMarkets();

        this.buildPathForMapWithOneRoute(adjacenciesTable, nodesTable);
        this.buildMapInWindow();
    }

    public GoogleStaticMap(List<Node> nodesList, Set<List<Integer>> routesOfStopPoints, String adjacenciesTable, String nodesTable)
            throws IOException {
        totalOfRoutes++;
        this.nodesList = nodesList;
        this.routesOfStopPoints = routesOfStopPoints;
        this.nodesTable = nodesTable;
        readApiKeysFromConfigFile(); 
        String folder;
        folder = "RouteDataForStaticMap";

        boolean successForCreateDataFolder = (new File(folder)).mkdirs();
        boolean successForCreateStaticMapsFolder = (new File(staticMapsFolder)).mkdirs();

        buildStringWithNodeMarkets();

        this.buildPathForMapWithAllRoutes(adjacenciesTable, nodesTable);
        this.buildMapInWindow();
    }

    private String getApiKeyFromFile(String keyName, String configFilePath) {

        try {
            JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray) parser.parse(new FileReader(configFilePath));
            System.out.println("data");
            String apiKey = "";
            for (int i = 0; i < data.size(); i++) {
                JSONObject object = (JSONObject) data.get(i);
                if (object.get("apiName").equals(keyName)) {
                    apiKey = (String) object.get("apiKey");
                }
            }
            return apiKey;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(GoogleStaticMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private void buildStringWithNodeMarkets() {
        for (int i = 0; i < this.nodesList.size(); i++) {
            stringOfNodes.append(this.nodesList.get(i).toStringForMapQuery().toString());
        }
    }

    private URL buildURL() throws MalformedURLException, IOException {
        setStaticMapParameters();
        URL url = new URL(URLRoot + mapCenter + "," + city + "," + state + "," + country + "&zoom=" + zoom + "&scale=" + scale
                + "&size=" + width + "x" + height + "&maptype=" + mapType + stringOfNodes.toString()
                + pathGeneratedForAllRoutes + "&key=" + staticMapKey + "&format=jpg");
        System.out.println("URL");
        System.out.println(url);
        return url;
    }

    private URL buildURLForInstanceMap() throws MalformedURLException, IOException {
        setStaticMapParameters();
        URL url = new URL(URLRoot + mapCenter + "," + city + "," + state + "," + country + "&zoom=" + zoom + "&scale=" + scale
                + "&size=" + width + "x" + height + "&maptype=" + mapType + stringOfNodes.toString()
                + "&key=" + staticMapKey + "&format=jpg");
        return url;
    }

    private void buildPathForMapWithOneRoute(String adjacenciesTable, String nodesTable) {
        String color = new ColorGenerator().generatesColor();
        color = "0xb30b47";
        if (this.route.get(0) != 0) {
            correctRouteAddingTheDepot(this.route);
        }
        for (int i = 0; i < route.size() - 1; i++) {
            int origin = this.route.get(i);
            int destination = this.route.get(i + 1);

            String polyline = new AdjacenciesDAO(adjacenciesTable, nodesTable).getPolylineBetweenNodes(origin, destination);
            pathGeneratedForAllRoutes.append("&path=weight:" + weight + "|color:" + color + "|enc:" + polyline);
        }
    }

    private void buildPathForMapWithAllRoutes(String adjacenciesTable, String nodesTable) {
        for (List<Integer> route : routesOfStopPoints) {
            String color = new ColorGenerator().generatesColor();
            if (route.get(0) != 0) {
                correctRouteAddingTheDepot(this.route);
            }
            for (int i = 0; i < route.size() - 1; i++) {
                int origin = route.get(i);
                int destination = route.get(i + 1);
                String polyline = new AdjacenciesDAO(adjacenciesTable, nodesTable).getPolylineBetweenNodes(origin, destination);
                pathGeneratedForAllRoutes.append("&path=weight:" + weight + "|color:" + color + "|enc:" + polyline);
            }
        }
    }

    private void correctRouteAddingTheDepot(List<Integer> route) {
        if (route.get(0) != 0) {
            route.add(0, 0);
            route.add(0);
        }
    }

    public void getPathForGoogleMap() {
        //"&path=weight:" + weight + "|color:" + color + "|enc:" + this.overviewPolyline;
    }

    private void buildMapInWindow() throws IOException {
        JFrame frame = new JFrame("Google Maps");

        String destinationFile;
        if (this.routesOfStopPoints == null) {
            destinationFile = staticMapsFolder + "/Route_" + totalOfRoutes + "_" + this.route + ".jpg";
        } else {
            destinationFile = staticMapsFolder + "/Route_" + totalOfRoutes + "_" + ".jpg";
        }

        try {
            String imageUrl = this.buildURL().toString();
            System.out.println(imageUrl);

            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (this.routesOfStopPoints == null) {
            frame.add(new JLabel(new ImageIcon((new ImageIcon(destinationFile)).getImage()
                    .getScaledInstance(730, 700, java.awt.Image.SCALE_SMOOTH))));
            System.out.println(destinationFile);
        } else {
            frame.add(new JLabel(new ImageIcon(new ImageIcon(destinationFile).getImage()
                    .getScaledInstance(730, 700, java.awt.Image.SCALE_SMOOTH))));
        }

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void getStaticMapForInstance() {
        JFrame frame = new JFrame("Google Maps");

        String destinationFile;
        if (this.routesOfStopPoints == null) {
            destinationFile = staticMapsFolder + "/Route_" + totalOfRoutes + "_" + this.route + ".jpg";
        } else {
            destinationFile = staticMapsFolder + "/Route_" + totalOfRoutes + "_" + ".jpg";
        }

        try {
            String imageUrl = this.buildURLForInstanceMap().toString();
            System.out.println(imageUrl);

            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //waitScreen.hideScreen();
        frame.add(new JLabel(new ImageIcon(new ImageIcon(destinationFile).getImage()
                .getScaledInstance(730, 700, java.awt.Image.SCALE_SMOOTH))));
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        //frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setStaticMapParameters() {
        if (this.nodesTable.charAt(this.nodesTable.length() - 1) == 's') {
            setParameterForSmallInstances();
        } else if (this.nodesTable.charAt(this.nodesTable.length() - 1) == 'm') {
            setParametersForMediumInstances();
        } else if (this.nodesTable.charAt(this.nodesTable.length() - 1) == 'l') {
            setParametersForLargeInstances();
        }
    }

    private void setParametersForMediumInstances() {
        this.mapCenter = "Terminal+Rodoviario";
        this.city = "Belo+Horizonte";
        this.state = "Minas+Gerais";
        this.country = "Brasil";
        this.zoom = 12;
        this.scale = 2;
    }

    private void setParametersForLargeInstances() {
        this.mapCenter = "Hospital+Odilon+Behrens";
        this.city = "Belo+Horizonte";
        this.state = "Minas+Gerais";
        this.country = "Brasil";
        this.zoom = 12;
        this.scale = 2;
    }

    private void setParameterForSmallInstances() {
        this.mapCenter = "Galeria+Ouvidor";
        this.city = "Belo+Horizonte";
        this.state = "Minas+Gerais";
        this.country = "Brasil";
        this.zoom = 15;
        this.scale = 2;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
