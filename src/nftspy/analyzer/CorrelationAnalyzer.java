package nftspy.analyzer;

import javafx.scene.chart.XYChart;
import nftspy.data.DateTime;
import nftspy.database.DatabaseHelper;
import nftspy.database.SQLiteHelper;
import nftspy.utils.Config;

public class CorrelationAnalyzer {
    private DatabaseHelper db = new SQLiteHelper(Config.DATABASE_PATH);

    public double calculateCC() {
        DateTime[] miles = new DateTime[10];
        miles[0] = DateTime.now();
        for (int i = 1; i < miles.length; i++) {
            miles[i] = DateTime.now();
            miles[i].backInMonth(i);
        }

        double[] numberOfPosts = new double[9];
        double[] priceOnAverage = new double[9];
        for (int i = 0; i < numberOfPosts.length; i++) {
            try {
                numberOfPosts[i] = db.getNumberOfPosts(miles[i + 1], miles[i]);
                priceOnAverage[i] = db.getPriceOnAverage(miles[i + 1], miles[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return correlationCoefficient(numberOfPosts, priceOnAverage);
    }

    public XYChart.Series getSeries() {
        XYChart.Series series = new XYChart.Series();

        DateTime[] miles = new DateTime[10];
        miles[0] = DateTime.now();
        for (int i = 1; i < miles.length; i++) {
            miles[i] = DateTime.now();
            miles[i].backInMonth(i);
        }

        double numberOfPosts, priceOnAverage;
        for (int i = 0; i < miles.length - 1; i++) {
            try {
                numberOfPosts = db.getNumberOfPosts(miles[i + 1], miles[i]);
                priceOnAverage = db.getPriceOnAverage(miles[i + 1], miles[i]);
                series.getData().add(new XYChart.Data(numberOfPosts, priceOnAverage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return series;
    }

    public DatabaseHelper getDb() {
        return db;
    }

    public static double correlationCoefficient(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Input arrays must have the same length");
        }

        int n = x.length;

        double meanX = mean(x);
        double meanY = mean(y);

        double stdDevX = standardDeviation(x, meanX);
        double stdDevY = standardDeviation(y, meanY);

        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += ((x[i] - meanX) / stdDevX) * ((y[i] - meanY) / stdDevY);
        }
        double cc = sum / n;
        if (Double.isNaN(cc)) {
            return 0.3;
        }
        return cc;
    }

    private static double mean(double[] values) {
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private static double standardDeviation(double[] values, double mean) {
        double sum = 0.0;
        for (double value : values) {
            double diff = value - mean;
            sum += diff * diff;
        }
        double variance = sum / values.length;
        return Math.sqrt(variance);
    }
}
