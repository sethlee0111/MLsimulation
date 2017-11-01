package nodeSimulation;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Draws a graph for price simulation for SYNC parallel
 * Created by sethlee on 8/10/17.
 */
public class PriceGrapher extends Application {
    final int TOTAL_NUMBER = 30;    // draws a graph from staleness = 0 to TOTAL_NUMBER
    @Override
    public void start(Stage primaryStage) {
        double totalTime;
        double tempTime; // variable for temporary saving results;
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        ArrayList<Double> writeData1 = new ArrayList<>();
        ArrayList<Double> writeData2 = new ArrayList<>();
        ArrayList<Double> writeData3 = new ArrayList<>();

        // define new chart and axis
        NumberAxis nodeNumAxis = new NumberAxis();
        NumberAxis axis = new NumberAxis();
        nodeNumAxis.setAutoRanging(false);
        nodeNumAxis.setLabel("Stale Threshold");
        nodeNumAxis.setTickUnit(1);
        nodeNumAxis.setMinorTickVisible(false);
        nodeNumAxis.setLowerBound(0);
        nodeNumAxis.setUpperBound(TOTAL_NUMBER);
        LineChart<Number, Number> lineChart = new LineChart<>(nodeNumAxis, axis);
//        lineChart.setTitle("Sync Simulation");
        lineChart.setTitle("SSP Simulation");
        lineChart.prefWidth(750);
        lineChart.minHeightProperty().bind(primaryStage.heightProperty().divide(2));
        lineChart.setLegendVisible(true);

        // price-simulation for synchronous parallel
//        PriceSimul priceSimul = new PriceSimul(MLsettings.LEARNING_MEAN_TIME, MLsettings.TIME_DEVIATION_BY_NODES);
//        XYChart.Series series = new XYChart.Series();   // set a new series for synchronous simulation
//        series.setName("Simulated Time");
//        for(int i=10; i < TOTAL_ITERATION; i += 10) {
//            series.getData().add(new XYChart.Data(i, priceSimul.getSyncTime(i, MLsettings.NUMBER_OF_NODES)));
//            System.out.printf("Simul for : " + i + "\n");
//        }
        // Configure settings
        MLsettings mLsettings = new MLsettings();
        mLsettings.setMeanTime(100);
        mLsettings.setIterNum(100);
        mLsettings.setNodeNum(100);

//        axis.setLowerBound(mLsettings.getMeanTime() * mLsettings.getIterNum() - 1000);
//        axis.setUpperBound(10000);
//        axis.setTickUnit(1000);
//        axis.setAutoRanging(false);
        axis.setLabel("Cost");

        // Price Simulation for SSP

        XYChart.Series series = new XYChart.Series();   // set a new series for synchronous simulation
        series.setName("Simul Dev: 30");
        mLsettings.setDevTime(30);
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);


        for(int i=0; i <= TOTAL_NUMBER; i += 1) {
            System.out.printf("1st Simul for : " + i + "\n");
            mLsettings.setStaleness(i);
            tempTime = staleBoundProbability.getTotalTime(mLsettings.getStaleness());
            series.getData().add(new XYChart.Data(i, tempTime));
            writeData1.add(tempTime);
        }

        XYChart.Series series1 = new XYChart.Series();   // set a new series for synchronous simulation
        series1.setName("Dev: 34");
        mLsettings.setDevTime(34);
        staleBoundProbability = new StaleBoundProbability(mLsettings);

        for(int i=0; i <= TOTAL_NUMBER; i += 1) {
            System.out.printf("2nd Simul for : " + i + "\n");
            mLsettings.setStaleness(i);
//            staleBoundProbability.setNodeNum(i);
            tempTime = staleBoundProbability.getTotalTime(mLsettings.getStaleness());
            series1.getData().add(new XYChart.Data(i, tempTime));
            writeData2.add(tempTime);
        }

        XYChart.Series series2 = new XYChart.Series();   // set a new series for synchronous simulation
        series2.setName("Simul: Dev 34");
        mLsettings.setMeanTime(100);
        mLsettings.setDevTime(80);
        mLsettings.setIterNum(150);
        mLsettings.setNodeNum(200);
        ArrayList<Integer> hostileList = new ArrayList<>();
        mLsettings.setGaussUpdateFreq(hostileList);

        SimpleMLsystem simpleMLsystem = new SimpleMLsystem();
        simpleMLsystem.initialize(mLsettings);

        for(int i=0; i <= TOTAL_NUMBER; i += 1) {
            System.out.printf("3rd Simul for : " + i + "\n");
            simpleMLsystem.getmLsettings().setStaleness(i);
            System.out.println("----For Staleness : " + i);
            simpleMLsystem.execute();
            totalTime = simpleMLsystem.getClock();
            series2.getData().add(new XYChart.Data(i, totalTime));
            writeData3.add(totalTime);
        }

//        XYChart.Series series2 = new XYChart.Series();   // set a new series for synchronous simulation
//        series2.setName("Dev: 60");
//        mLsettings.setDevTime(60);
//        staleBoundProbability = new StaleBoundProbability(mLsettings);
//
//        for(int i=0; i <= TOTAL_NUMBER; i += 1) {
//            System.out.printf("3rd Simul for : " + i + "\n");
//            mLsettings.setStaleness(i);
////            staleBoundProbability.setNodeNum(i);
//            tempTime = staleBoundProbability.getTotalTime(mLsettings.getStaleness());
//            series2.getData().add(new XYChart.Data(i, tempTime));
//            writeData3.add(tempTime);
//        }

        File file = new File("price_compared.txt");
        PrintStream ps;
        try {
            ps = new PrintStream(file);

            for(double tmp : writeData1) {
                ps.print(tmp);
                ps.append(' ');
            }
            ps.println();
            for(double tmp : writeData2) {
                ps.print(tmp);
                ps.append(' ');
            }
            ps.println();
            for(double tmp : writeData3) {
                ps.print(tmp);
                ps.append(' ');
            }
        } catch (FileNotFoundException e) {   }


        lineChart.getData().add(series1);
        lineChart.getData().add(series);
        lineChart.getData().add(series2);

        Scene scene = new Scene(lineChart, 600, 800);
        primaryStage.setWidth(1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
