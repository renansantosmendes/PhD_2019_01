/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgorithmsResults;

import InstanceReader.NodeDAO;
import ProblemRepresentation.ProblemSolution;
import java.awt.Color;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.NumberAxis;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author renansantos
 */
public class ResultsGraphicsForParetoCombinedSet {

    private List<ProblemSolution> population;
    private JFreeChart paretoCombined;
    private String folder;
    private String paretoCombinedFileName;
    private ChartMouseListener chartMouseListener;

    public ResultsGraphicsForParetoCombinedSet(List<ProblemSolution> population, String folder, String paretoCombinedFileName) throws IOException {
        this.population = population;
        this.folder = folder;
        this.paretoCombinedFileName = paretoCombinedFileName;
        boolean successForCreateFolder = (new File(folder)).mkdirs();
        this.buildParetoGraphic();
        this.showGraphic();
    }

    public void buildParetoGraphic() throws FileNotFoundException, IOException {
        this.paretoCombined = ChartFactory.createScatterPlot("Combined Pareto Set", "Objective Function 1",
                "Objective Function 2", createDataset(),
                PlotOrientation.VERTICAL, true, true, false);
        Shape serieShape = ShapeUtilities.createDiagonalCross(3, 1);

        XYPlot xyPlot = (XYPlot) paretoCombined.getPlot();

        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setRangeGridlinesVisible(true);
        xyPlot.setRangeGridlinePaint(Color.gray);
        xyPlot.setDomainGridlinesVisible(true);
        xyPlot.setDomainGridlinePaint(Color.gray);
        xyPlot.setBackgroundPaint(Color.white);
        xyPlot.setDomainCrosshairLockedOnData(true);

        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesShape(0, serieShape);
        renderer.setSeriesPaint(0, Color.red);

        this.saveGraphicInFile(new FileOutputStream(folder + "/" + paretoCombinedFileName));
    }

    private XYDataset createDataset() {
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series = new XYSeries("NSGA-II");
        for (int i = 0; i < this.population.size(); i++) {
            double x = population.get(i).getAggregatedObjective1();
            double y = population.get(i).getAggregatedObjective2();
            series.add(x, y);
        }
        result.addSeries(series);
        return result;
    }

    private void saveGraphicInFile(OutputStream out) throws IOException {
        ChartUtilities.writeChartAsPNG(out, this.paretoCombined, 1024, 600);
    }

    private void showGraphic() throws FileNotFoundException, IOException {
        JFrame frame = new JFrame("Combined Pareto Set");
        JPanel graphic = this.getPanel();
        graphic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //JOptionPane.showMessageDialog(null, "Test - Clicked Point = " + e.getPoint());
                XYPlot xyPlot = (XYPlot) paretoCombined.getPlot();
                double x = xyPlot.getDomainCrosshairValue();
                double y = xyPlot.getRangeCrosshairValue();
                System.out.println(xyPlot.getDomainCrosshairValue());
                System.out.println(xyPlot.getRangeCrosshairValue());
                try {
                    showSolution(x, y);
                } catch (IOException ex) {
                    Logger.getLogger(ResultsGraphicsForParetoCombinedSet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        frame.add(graphic);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public JPanel getPanel() {
        ChartPanel panel = new ChartPanel(paretoCombined);
//        panel.addChartMouseListener(new ChartMouseListener() {
//            @Override
//            public void chartMouseClicked(ChartMouseEvent cme) {
//                //JOptionPane.showMessageDialog(null, "Test - Clicked Point = " + e.getPoint());
//                System.out.println("Clicked = " + cme.getTrigger().getLocationOnScreen());
//            }
//
//            @Override
//            public void chartMouseMoved(ChartMouseEvent cme) {
//                
//            }
//        });
//	
        return panel;
    }

    private void showSolution(double x, double y) throws IOException {
        if (x != 0.0 && y != 0.0) {
            for (ProblemSolution solution : population) {
                if (solution.getAggregatedObjective1() == x && solution.getAggregatedObjective2() == y) {
                    JOptionPane.showMessageDialog(null, "Solution Information:\n Total Distance = " + solution.getTotalDistance()
                            + " m\n Total Delivery Delay = " + solution.getTotalDeliveryDelay() + " min \n Charge Balance = "
                            + solution.getTotalRouteTimeChargeBanlance() + " min \n Non Attended Requests = " + solution.getNumberOfNonAttendedRequests()
                            + "\n Number of Vehicles = " + solution.getNumberOfVehicles());

                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    JOptionPane.showConfirmDialog(null, "Do you want to see the routes?", "WARNING", dialogButton);
                    if (dialogButton == JOptionPane.YES_OPTION) {
                        String nodesData = "bh_n" + 12 + "s";
                        String adjacenciesData = "bh_adj_n" + 12 + "s";
                        solution.getStaticMapForEveryRoute(new NodeDAO(nodesData).getListOfNodes(), adjacenciesData, nodesData);
                    }else if (dialogButton == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                }
            }
        }
    }
}
