package nftspy.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import nftspy.analyzer.CorrelationAnalyzer;
import nftspy.data.DateTime;

import java.net.URL;
import java.util.ResourceBundle;

public class AnalyzerController implements Initializable {
    private CorrelationAnalyzer analyzer = new CorrelationAnalyzer();

    @FXML
    private Label correlationLabel;

    @FXML
    private LineChart<Double, Double> lineChart;

    @FXML
    private Label postNumberLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ScatterChart<Double, Double> scatterChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double correlationCoefficient = analyzer.calculateCC();
        double x0 = 0;
        double y0 = 0;
        double x1 = 100;
        double y1 = correlationCoefficient * x1;
        XYChart.Series lineChartSeries = new XYChart.Series();
        lineChartSeries.getData().add(new XYChart.Data(x0, y0));
        lineChartSeries.getData().add(new XYChart.Data(x1, y1));
        lineChart.getData().add(lineChartSeries);

        correlationLabel.setText(String.format("%.4f", correlationCoefficient));
        DateTime now = DateTime.now();
        DateTime then = DateTime.now();
        then.backInMonth(3);
        try {
            int numberOfPosts = analyzer.getDb().getNumberOfPosts(then, now);
            postNumberLabel.setText("" + numberOfPosts);
            double priceOnAverage = analyzer.getDb().getPriceOnAverage(then, now);
            priceLabel.setText(String.format("$%.4f", priceOnAverage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        XYChart.Series series = analyzer.getSeries();
        scatterChart.getData().add(series);
    }
}
