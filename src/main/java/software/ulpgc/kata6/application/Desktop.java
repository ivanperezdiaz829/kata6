package software.ulpgc.kata6.application;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import software.ulpgc.kata6.architecture.io.Store;
import software.ulpgc.kata6.architecture.model.Movie;
import software.ulpgc.kata6.architecture.viewmodel.Histogram;
import software.ulpgc.kata6.architecture.viewmodel.HistogramBuilder;

import javax.swing.*;
import java.io.IOException;
import java.util.stream.Stream;

public class Desktop extends JFrame {

    private static Store store;

    private Desktop(Store store) {
        this.store = store;
        this.setTitle("Histogram");
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
    }

    public static Desktop create(Store store) {
        return new Desktop(store);
    }

    public Desktop display() throws IOException {
        this.getContentPane().add(chartPanelWith(histogram()));
        return this;
    }

    private ChartPanel chartPanelWith(Histogram histogram) {
        return new ChartPanel(chartWith(histogram));
    }

    private JFreeChart chartWith(Histogram histogram) {
        return ChartFactory.createHistogram(
                histogram.title(),
                histogram.xAxis(),
                histogram.yAxis(),
                datasetWith(histogram)
        );
    }

    private XYSeriesCollection datasetWith(Histogram histogram) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesIn(histogram));
        return dataset;
    }

    private XYSeries seriesIn(Histogram histogram) {
        XYSeries series = new XYSeries(histogram.legend());
        for (int bin : histogram) {
            series.add(bin, histogram.count(bin));
        }
        return series;
    }

    private static Histogram histogram() throws IOException {
        return HistogramBuilder
                .with(movies())
                .title("Movies Per Decade")
                .xAxis("Decade")
                .yAxis("Count")
                .legend("Kata 5")
                .use(Movie::year);
    }

    private static Stream<Movie> movies() throws IOException {
        return store.movies();
    }
}
