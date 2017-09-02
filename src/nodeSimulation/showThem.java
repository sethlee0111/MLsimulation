package nodeSimulation;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * EventHandler class for 'show me what you got' button
 * Created by sethlee on 5/10/17.
 */
public class showThem implements EventHandler<ActionEvent> {
    private int meanTime, nodeNum, devTime, minTime, iterNum;
    private MLsettings mLsettings = new MLsettings();
    private String displayString = new String();

    public showThem(MLsettings mLsettings) {
        this.mLsettings = mLsettings;
    }

    @Override
    public void handle(ActionEvent e) {
        Stage graphStage = new Stage();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        // define new chart and axis
        NumberAxis stalenessAxis = new NumberAxis(mLsettings.getMinMode(), mLsettings.getMaxMode(), 1);
        NumberAxis staleGoodnessAxis = new NumberAxis();
        AreaChart<Number, Number> areaChart = new AreaChart<>(stalenessAxis, staleGoodnessAxis);
        areaChart.setTitle(" on " + mLsettings.getxMode());
        areaChart.prefWidth(750);
        areaChart.minHeightProperty().bind(graphStage.heightProperty().divide(2));
        areaChart.setLegendVisible(true);

        Button stopBtn = new Button("I like what you got!");
        Button quitBtn = new Button("quit");
        Button saveBtn = new Button("Save as Image");
        saveBtn.setOnAction(event -> {
            WritableImage image = areaChart.snapshot(new SnapshotParameters(), null);

            // TODO: probably implement a file chooser here
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            File file = new File("/Users/sethlee/documents/chart_" + sdf.format(cal.getTime()) + ".png");
            try {
                OutputStream out = new FileOutputStream(file);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                out.close();
            } catch (IOException exe) {
                System.out.printf("Error occured while saving image\n");
            }

        });
        HBox btnBox = new HBox();
        btnBox.setSpacing(7);
        btnBox.setPadding(new Insets(10, 0, 0, 0));
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(stopBtn, quitBtn, saveBtn);

        TextArea taNote = new TextArea("");
        taNote.setPrefColumnCount(5);
        taNote.setPrefRowCount(12);
        taNote.setWrapText(true);
        taNote.setStyle("-fx-text-fill: #1619ff");
        taNote.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 16; -fx-focus-color: transparent");
        taNote.setFont(Font.font("Times", 12));
        taNote.setStyle("-fx-display-caret: false");
        taNote.setPadding(new Insets(3,3,3,3));
        displayString = "Number of Nodes: " + mLsettings.getNodeNum() + ",  Mean Execution Time: " + mLsettings.getMeanTime() +
                            ",  Deviation of Exe. Time: " + mLsettings.getDevTime() + ",  Iterations: " + mLsettings.getIterNum() + "\n";

        ArrayList<String> dispArrList = new ArrayList<>();

        vBox.getChildren().add(areaChart);
        vBox.getChildren().add(taNote);
        vBox.getChildren().add(btnBox);

        Scene scene = new Scene(vBox, 800, 800);
//        scene.getStylesheets().add("GraphStyleSheet.css");
        areaChart.getStylesheets().add("GraphStyleSheet.css");

        ArrayList<XYChart.Series> seriesArrayList = new ArrayList<>();
        ArrayList<XYChart.Series> dragArrayList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> dragArrArr = new ArrayList<>();
        //arraylist of arraylist which stores drag index of specific series
        ArrayList<ArrayList<Integer>> staleGoodnessArrArr = new ArrayList<>();
        //arraylist of arraylist which stores staleGoodness of specific series

        // generate series
        int j=0;
        for(int varNum : mLsettings.getSeriesArrayList()) {
            if(varNum != -1) {  // if series is not disabled(-1)
                XYChart.Series seriesStaleness = new XYChart.Series();
                XYChart.Series seriesDrag = new XYChart.Series();
                seriesStaleness.setName(mLsettings.getSeriesMode() + " " + varNum);   // set name for series Mode
                seriesDrag.setName("Drag Index of " + mLsettings.getSeriesMode() + " " + varNum);   // set name for graph for Draㅎ
                for (int i = mLsettings.getMinMode(); i <= mLsettings.getMaxMode(); i++) {
                    seriesStaleness.getData().add(new XYChart.Data(i, 0));
                    seriesDrag.getData().add(new XYChart.Data(i, 0));
                    staleGoodnessArrArr.add(new ArrayList<Integer>());
                    staleGoodnessArrArr.get(j).add(0);
                    dragArrArr.add(new ArrayList<Integer>());
                    dragArrArr.get(j).add(0);
                    dispArrList.add("0 ");
                }
//                seriesArrayList.add(seriesStaleness);
//                dragArrayList.add(seriesDrag);
                areaChart.getData().addAll(seriesStaleness);
                areaChart.getData().addAll(seriesDrag);
                j++;
            }
        }

        @SuppressWarnings("unchecked")
        class GraphAniThread extends Thread {
            private AreaChart<Number, Number> areaChart;
            private SimulationResult simulationResult;

            public GraphAniThread(AreaChart areaChart) {
                this.areaChart = areaChart;

            }
            public void run() {
                int testNum = 1;    // total number of tests
                int i, j, k;
                while(true) {
                    i = 0; j = 0;   // i is data number, j is series number
                    for (XYChart.Series<Number, Number> series : areaChart.getData()) {
                        k = j / 2;
                        mLsettings.setSeries(k);    // set to the certain series set
                        for (XYChart.Data<Number, Number> data : series.getData()) {
                            mLsettings.setStaleness(i);
                            simulationResult = SimpleML.simulation(mLsettings); // run simulation and get result
                            if(j % 2 == 0) {    // if this series is a graph for staleness,
                                staleGoodnessArrArr.get(k).set(i, staleGoodnessArrArr.get(k).get(i) + simulationResult.getStaleGoodness());
                                data.setYValue(staleGoodnessArrArr.get(k).get(i) / testNum);
                                dispArrList.set(k * (mLsettings.getMaxMode() - mLsettings.getMinMode() + 1) + i,
                                        staleGoodnessArrArr.get(k).get(i) / testNum + " ");
                            }
                            else if(j % 2 == 1) {    // if this series is a graph for dragDiff,
                                dragArrArr.get(k).set(i, dragArrArr.get(k).get(i) + simulationResult.getDragDiff());
                                data.setYValue(dragArrArr.get(k).get(i) / testNum);
                            }
                            i++;
                        }
                        j++;
                        i=0;
                    }
                    j = 0;

                    for(int varNum : mLsettings.getSeriesArrayList()) {
                        if (varNum != -1) {
                            displayString +=  "Test: " + testNum + ", " + mLsettings.getSeriesMode() + " : " + varNum + "\n";
                            for (k = mLsettings.getMinMode(); k <= mLsettings.getMaxMode(); k++) {
                                displayString += dispArrList.get(j*(mLsettings.getMaxMode()-mLsettings.getMinMode()+1) +
                                        k - mLsettings.getMinMode());
                            }
                            displayString += "\n";
                        }
                        j++;
                    }
                    testNum++;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            taNote.setText(displayString);
                        }
                    });
                }
            }
        }


        GraphAniThread graphAniThread = new GraphAniThread(areaChart);
        graphAniThread.start();

        quitBtn.setOnAction(event -> {graphAniThread.stop(); graphStage.close();});

        stopBtn.setOnAction(event->{graphAniThread.stop();});

        graphStage.setTitle("Machine Learning Simulator");
        graphStage.setScene(scene);
        graphStage.setWidth(900);
        graphStage.setHeight(800);
        graphStage.show();



//        for (int testNum = 1; testNum <= MLsettings.TOTAL_TESTS; testNum++) {
//
//            for (int i = 0; i < MLsettings.TOTAL_ITERATION / 10; i++) {
//                staleGoodnessArr.set(i, staleGoodnessArr.get(i) + SimpleML.simulation(i));
//                seriesStaleness.getData().add(new XYChart.Data(i, staleGoodnessArr.get(i)/testNum));
//                System.out.printf(i + " ");
//            }
//            areaChart.getData().removeAll();
//            areaChart.getData().addAll(seriesStaleness);
//        }

    }


}
