/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgorithmsResults;

import ProblemRepresentation.ProblemSolution;
import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author renansantos
 */
public class ResultsGraphicsForConvergence {

    private List<Double> hypervolumeConvergence;
    private JFreeChart convergence;
    private String folder;
    private String convergenceFileName;

    public ResultsGraphicsForConvergence(List<Double> hypervolumeConvergence, String folder, String convergenceFileName) throws IOException {
        this.hypervolumeConvergence = hypervolumeConvergence;
        this.folder = folder;
        this.convergenceFileName = convergenceFileName;
        boolean successForCreateFolder = (new File(folder)).mkdirs();
        this.buildConvergenceGraphic();
        this.showGraphic();
    }

    public void buildConvergenceGraphic() throws FileNotFoundException, IOException {
        this.convergence = ChartFactory.createLineChart("Hypervolume Convergence", "Generation",
                "Hypervolume", createDataset(), PlotOrientation.VERTICAL, true, true, false);
        Shape serieShape = ShapeUtilities.createDiagonalCross(3, 1);

        CategoryPlot plot = (CategoryPlot) this.convergence.getPlot();

        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.gray);
        //plot.setRangeAxis(new ValueAxis("", new TickUnitSource()));
        
        plot.setBackgroundPaint(Color.white);
        plot.configureRangeAxes();

        //xyPlot.setDomainCrosshairLockedOnData(true);
        plot.addChangeListener(new PlotChangeListener() {
            @Override
            public void plotChanged(PlotChangeEvent pce) {
                System.out.println(pce.getType());
            }
        });

        //XYItemRenderer renderer = xyPlot.getRenderer();
        //renderer.setSeriesShape(0, serieShape);
        //renderer.setSeriesPaint(0, Color.red);
        this.saveGraphicInFile(new FileOutputStream(folder + "/" + convergenceFileName));
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        XYSeries series = new XYSeries("Hypervolume");
        for (int i = 0; i < this.hypervolumeConvergence.size(); i++) {
            double x = this.hypervolumeConvergence.get(i);
            result.addValue(Math.log(x), "", Integer.toString(i));
        }
        //result.addSeries(series);
        return result;
    }

    private void saveGraphicInFile(OutputStream out) throws IOException {
        ChartUtilities.writeChartAsPNG(out, this.convergence, 800, 600);
    }

    private void showGraphic() throws FileNotFoundException, IOException {
        JFrame frame = new JFrame("Combined Pareto Set");
        JPanel graphic = this.getPanel();
        graphic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //JOptionPane.showMessageDialog(null, "Test - Clicked Point = " + e.getPoint());
            }
        });

        frame.add(graphic);

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 480);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public JPanel getPanel() {
        return new ChartPanel(convergence);
    }

}
