package nodeSimulation;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Draws a graph for price simulation for SYNC parallel
 * Created by sethlee on 8/10/17.
 */
public class PriceGrapher extends Application {
    final int TOTAL_NUMBER = 10;
    @Override
    public void start(Stage primaryStage) {
        double totalTime;

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        // define new chart and axis
        NumberAxis nodeNumAxis = new NumberAxis();
        NumberAxis axis = new NumberAxis();
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
        mLsettings.setDevTime(20);
        mLsettings.setIterNum(50);
        mLsettings.setNodeNum(50);
        mLsettings.setStaleness(3);

        // Price Simulation for SSP

        XYChart.Series series = new XYChart.Series();   // set a new series for synchronous simulation
        series.setName("Simulated Time-SSP");
        StaleBoundProbability staleBoundProbability = new StaleBoundProbability(mLsettings);

        for(int i=1; i < TOTAL_NUMBER; i += 1) {
            System.out.printf("Simul for : " + i + "\n");
            mLsettings.setStaleness(i);
//            staleBoundProbability.setNodeNum(i);
            series.getData().add(new XYChart.Data(i, staleBoundProbability.getTotalTime(mLsettings.getStaleness())));
        }


//        // real-simulation for synchronous parallel
//        // with 0 staleness : synchronous
//        MLsettings mLsettings = new MLsettings();
//        SimpleMLsystem simpleMLsystem = new SimpleMLsystem(mLsettings);
//        mLsettings.setStaleness(0);
//
//        XYChart.Series realSeries = new XYChart.Series();
//        realSeries.setName("real");
//        for(int i=10; i < TOTAL_ITERATION; i+= 10) {
//            mLsettings.setIterNum(i);
//            simpleMLsystem.setmLsettings(mLsettings);
//            simpleMLsystem.initNodes();
//            simpleMLsystem.execute();
//            totalTime = simpleMLsystem.getClock();
//            realSeries.getData().add(new XYChart.Data(i, totalTime));
//            System.out.printf("real simul for : " + i + "\n");
//        }
        // simulation on simulated nodes
//        SimpleMLsystem simpleMLsystem = new SimpleMLsystem(mLsettings);
//        XYChart.Series realSeries = new XYChart.Series();
//        realSeries.setName("Node Simul");
//        double staleTotalTime;
//        for(int i=1; i < TOTAL_NUMBER; i += 1) {
//            System.out.printf("real simul for : " + i + "\n");
////            mLsettings.setNodeNum(i);
//            mLsettings.setStaleness(i);
//            simpleMLsystem.setmLsettings(mLsettings);
//            simpleMLsystem.initNodes();
//            simpleMLsystem.execute();
//            staleTotalTime = simpleMLsystem.getClock();
//            realSeries.getData().add(new XYChart.Data(i, staleTotalTime));
//        }

        lineChart.getData().add(series);
//        lineChart.getData().add(realSeries);
        Scene scene = new Scene(lineChart, 600, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
