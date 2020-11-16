/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoogleMapsApi;

//import static GoogleMapsApi.GoogleMapsRoute.FileExtension.json;
//import static GoogleMapsApi.GoogleMapsRoute.TravelMode.driving;
import InstanceReader.NodeDAO;
import ProblemRepresentation.Node;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

/**
 *
 * @author renansantos
 */
public class StaticGoogleMap {

    //Google Maps Directions API Key =  AIzaSyC--KVvuj7xGnkRBoLrUIaXLx5lGfedlBI  
    private List<Node> nodesList;
    private final String URLRoot = "https://maps.googleapis.com/maps/api/staticmap?center=";
    private final String directionsApiKey = "AIzaSyCgaZr9fRAUs3_8lftkt026_MfZ3yZVN4E";
    private final String staticMapKey = "AIzaSyDGIY0wYBd9hwvsUqLbaUjNvVnhHOBCdxU";
    private StringBuilder stringOfNodes = new StringBuilder();
    private StringBuilder polylines = new StringBuilder();
    private StringBuilder polylinesForAllRotes = new StringBuilder();
    private String mapCenter = "Galeria+Ouvidor";
    private String city = "Belo+Horizonte";
    private String state = "Minas+Gerais";
    private String country = "Brasil";
    private int zoom = 15;
    private int scale = 2;
    private int width = 1200;
    private int height = 1200;
    private String mapType = "roadmap";
    private String color = "red";
    private int weight = 4;
    private StringBuilder waypoints = new StringBuilder();
    private StringBuilder encodedPolylines = new StringBuilder();
    //private List<GoogleMapsRoute> listOfGoogleMapsRoutes = new ArrayList<>();
    private StringBuilder pathGeneratedForAllRoutes = new StringBuilder();
    private static int totalOfRoutes = 0;
    private String staticMapsFolder = "StaticMaps";
    private Set<List<Integer>> routes;

    public StaticGoogleMap(List<Node> nodesList, Set<List<Integer>> routes) throws IOException {
        totalOfRoutes++;

        this.nodesList = nodesList;
        this.routes = routes;
        for (int i = 0; i < this.nodesList.size(); i++) {
            stringOfNodes.append(this.nodesList.get(i).toStringForMapQuery().toString());
        }

        int routeNumber = 0;
        String folder;
        folder = "RouteDataForStaticMap";

        boolean successForCreateDataFolder = (new File(folder)).mkdirs();
        boolean successForCreateStaticMapsFolder = (new File(staticMapsFolder)).mkdirs();

        for (List<Integer> route : routes) {
//            GoogleMapsRoute googleMapsRoute = new GoogleMapsRoute(json, folder + "/route_data_" + routeNumber, driving,
//                    route, nodesList);
//            googleMapsRoute.downloadDataFile();
//            googleMapsRoute.getDataFromFile();
//            listOfGoogleMapsRoutes.add(googleMapsRoute);
//            pathGeneratedForAllRoutes.append(googleMapsRoute.getPathForGoogleMap());
            routeNumber++;
        }
    }

    public StringBuilder getStringOfNodes() {
        return stringOfNodes;
    }

    public URL buildURL() throws MalformedURLException, IOException {
        URL url = new URL(URLRoot + mapCenter + "," + city + "," + state + "," + country + "&zoom=" + zoom + "&scale=" + scale
                + "&size=" + width + "x" + height + "&maptype=" + mapType + stringOfNodes.toString()
                + pathGeneratedForAllRoutes + "&key=" + staticMapKey + "&format=jpg");
        return url;
    }

    public void buildMapInWindow() throws IOException {
        JFrame frame = new JFrame("Google Maps");

        try {
            String imageUrl = buildURL().toString();
            System.out.println("URL");
            System.out.println(imageUrl);

            String destinationFile = staticMapsFolder + "/Route_" + totalOfRoutes + "_Static_Map.jpg";
            String str = destinationFile;
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

        frame.add(new JLabel(new ImageIcon((new ImageIcon(staticMapsFolder + "/Route_" + totalOfRoutes + "_Static_Map.jpg"))
                .getImage().getScaledInstance(730, 700, java.awt.Image.SCALE_SMOOTH))));
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    

}
