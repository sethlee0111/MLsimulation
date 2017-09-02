package nodeSimulation;
/**
 * GUI simulator for ML simulation
 * draws graph depending on various components
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by utri on 2017-05-01.
 */
public class MLSimulator extends Application{
    private TextField meanTimeField = new TextField();
    private TextField nodeNumField = new TextField();
    private TextField devTimeField = new TextField();
    private TextField minTimeField = new TextField();
    private TextField iterNumField = new TextField();

    private ArrayList<TextField> seriesFieldArrayList= new ArrayList<>();

    MLsettings mLsettings = new MLsettings();

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5,5,5,5));
        GridPane meeseeksPane = new GridPane();
        meeseeksPane.setAlignment(Pos.CENTER);
        ImageView meeseeksImgView = new ImageView(new Image(getClass().getResourceAsStream("meeseeks.jpg")));
        meeseeksImgView.setPreserveRatio(true);
        meeseeksImgView.setFitHeight(140);
        meeseeksPane.add(meeseeksImgView, 0,0);
        Label look = new Label("Hello! I'm Mr. Meeseeks!  \nLook at Me!\nLet's show them\nwhat we've got!");
        look.setFont(new Font("Gabriola", 13));
//        meeseeksPane.add(look, 0, 0);

        GridPane componentsPane = new GridPane();
        componentsPane.setAlignment(Pos.CENTER);
        componentsPane.setHgap(5.5);
        componentsPane.setVgap(5.5);
        componentsPane.setPadding(new Insets(21.5, 22.5, 23.5, 24.5));

        GridPane graphSetPane = new GridPane();
        graphSetPane.setAlignment(Pos.CENTER);
        graphSetPane.setHgap(5.5);
        graphSetPane.setVgap(5.5);
        graphSetPane.setPadding(new Insets(10,10,10,10));

        GridPane seriesSetPane = new GridPane();
        seriesSetPane.setAlignment(Pos.CENTER);
        seriesSetPane.setHgap(5.5);
        seriesSetPane.setVgap(5.5);
        seriesSetPane.setPadding(new Insets(10,10,10,10));

        meanTimeField.setAlignment(Pos.CENTER_RIGHT);
        meanTimeField.setText(MLsettings.LEARNING_MEAN_TIME + "");
        meanTimeField.setOnAction(e -> mLsettings.setMeanTime(Integer.valueOf(meanTimeField.getText())));
        nodeNumField.setText(MLsettings.NUMBER_OF_NODES + "");
        nodeNumField.setAlignment(Pos.CENTER_RIGHT);
        nodeNumField.setOnAction(e -> mLsettings.setNodeNum(Integer.valueOf(nodeNumField.getText())));
        devTimeField.setAlignment(Pos.CENTER_RIGHT);
        devTimeField.setText(MLsettings.TIME_DEVIATION_BY_NODES + "");
        devTimeField.setOnAction(e -> mLsettings.setDevTime(Integer.valueOf(devTimeField.getText())));
        minTimeField.setAlignment(Pos.CENTER_RIGHT);
        minTimeField.setText(MLsettings.MINIMUM_ITERATION_TIME + "");
        minTimeField.setOnAction(e -> mLsettings.setMinIter(Integer.valueOf(minTimeField.getText())));
        iterNumField.setAlignment(Pos.CENTER_RIGHT);
        iterNumField.setText(MLsettings.TOTAL_ITERATION + "");
        iterNumField.setOnAction(e -> mLsettings.setIterNum(Integer.valueOf(iterNumField.getText())));

        componentsPane.add(new Label("Number of Nodes : "), 0,0);
        componentsPane.add(nodeNumField, 1, 0);
        componentsPane.add(new Label("Mean Time : "), 0, 1);
        componentsPane.add(meanTimeField, 1, 1);
        componentsPane.add(new Label("Dev. Time : "), 0, 2);
        componentsPane.add(devTimeField, 1, 2);
        componentsPane.add(new Label("Minimum Time : "), 0, 3);
        componentsPane.add(minTimeField, 1, 3);
        componentsPane.add(new Label("Number of Iter. : "), 0, 4);
        componentsPane.add(iterNumField, 1, 4);

        TextField maxVarField = new TextField();
        maxVarField.setMaxWidth(90);
        maxVarField.setAlignment(Pos.CENTER_RIGHT);
        maxVarField.setOnAction(e -> mLsettings.setMaxMode(Integer.valueOf(maxVarField.getText())));
        TextField minVarField = new TextField();
        minVarField.setMaxWidth(90);
        minVarField.setText("0");
        minVarField.setAlignment(Pos.CENTER_RIGHT);
        minVarField.setOnAction(e -> mLsettings.setMinMode(Integer.valueOf(minVarField.getText())));
        ComboBox<String> varSetCombo = new ComboBox<>();
        varSetCombo.getItems().addAll("Staleness", "Number of nodes", "Mean Time", "Dev. Time", "Num. of Iter.");
        varSetCombo.setStyle("-fx-color: #ffb2d1");
        varSetCombo.setValue("Staleness");
        varSetCombo.setOnAction(e -> mLsettings.setxMode(varSetCombo.getValue()));
        maxVarField.minWidthProperty().bind(varSetCombo.widthProperty());

        graphSetPane.add(new Label("Set X as : "), 0, 0);
        graphSetPane.add(varSetCombo, 2, 0);
        graphSetPane.add(minVarField, 0, 1);
        graphSetPane.add(new Label(" ~ "), 1, 1);
        graphSetPane.add(maxVarField, 2, 1);
        graphSetPane.setPadding(new Insets(10,10,20,10));

        ImageView showImg = new ImageView(new Image(getClass().getResourceAsStream("showme.jpg")));
        showImg.setPreserveRatio(true);
        showImg.setFitHeight(65);
        Button showBtn = new Button();
        showBtn.setGraphic(showImg);
        showBtn.setPadding(new Insets(2,2,10,2));
        showThem showthem = new showThem(mLsettings);
        showBtn.setOnAction(showthem);

        ComboBox<String> seriesSetCombo = new ComboBox<>();
        seriesSetCombo.getItems().addAll("Dev T", "Mean T", "Stale", "Node #", "Iter #");
        seriesSetCombo.setStyle("-fx-color: #5da2ff");
        seriesSetCombo.setValue("Dev T");
        seriesSetCombo.setOnAction(e -> mLsettings.setSeriesMode(seriesSetCombo.getValue()));


        TextField seriesField = new TextField();
        seriesField.setAlignment(Pos.CENTER_RIGHT);
        seriesField.setText(mLsettings.getSeriesArrayList().get(0).toString());
        seriesField.setOnAction(e -> mLsettings.getSeriesArrayList().set(0, Integer.valueOf(seriesField.getText())));
        seriesFieldArrayList.add(seriesField);
        TextField seriesField1 = new TextField();
        seriesField1.setVisible(false);
        seriesField1.setAlignment(Pos.CENTER_RIGHT);
        seriesField1.setOnAction(e -> mLsettings.getSeriesArrayList().set(1, Integer.valueOf(seriesField1.getText())));
        seriesFieldArrayList.add(seriesField1);
        TextField seriesField2 = new TextField();
        seriesField2.setVisible(false);
        seriesField2.setAlignment(Pos.CENTER_RIGHT);
        seriesField2.setOnAction(e -> mLsettings.getSeriesArrayList().set(2, Integer.valueOf(seriesField2.getText())));
        seriesFieldArrayList.add(seriesField2);
        TextField seriesField3 = new TextField();
        seriesField3.setVisible(false);
        seriesField3.setAlignment(Pos.CENTER_RIGHT);
        seriesField3.setOnAction(e -> mLsettings.getSeriesArrayList().set(3, Integer.valueOf(seriesField3.getText())));
        seriesFieldArrayList.add(seriesField3);
        TextField seriesField4 = new TextField();
        seriesField4.setVisible(false);
        seriesField4.setAlignment(Pos.CENTER_RIGHT);
        seriesField4.setOnAction(e -> mLsettings.getSeriesArrayList().set(4, Integer.valueOf(seriesField4.getText())));
        seriesFieldArrayList.add(seriesField4);

        Slider seriesNumSetSlider = new Slider();
        seriesNumSetSlider.setShowTickMarks(true);
        seriesNumSetSlider.setShowTickLabels(true);
        seriesNumSetSlider.setOrientation(Orientation.HORIZONTAL);
        seriesNumSetSlider.setMin(1);
        seriesNumSetSlider.setMax(5);
        seriesNumSetSlider.valueProperty().addListener(ov -> {
            for(int i=0; i < 5; i++) {
                mLsettings.setSeriesNum((int)seriesNumSetSlider.getValue());
                if(i < seriesNumSetSlider.getValue())
                    seriesFieldArrayList.get(i).setVisible(true);
                else {
                    seriesFieldArrayList.get(i).setVisible(false);
                    mLsettings.getSeriesArrayList().set(i, -1); // disable(-1) unnecessary values
                }
            }
        });

        seriesSetPane.add(seriesSetCombo, 0,0);
        seriesSetPane.add(seriesNumSetSlider, 1, 0);
        Iterator<TextField> iterator = seriesFieldArrayList.iterator();
        for(int i=0; i < 5; i++) {
            seriesSetPane.add(iterator.next(), 1, i + 1);
        }

        vBox.getChildren().add(meeseeksPane);
        vBox.getChildren().add(componentsPane);
        vBox.getChildren().add(graphSetPane);
        vBox.getChildren().add(seriesSetPane);
        vBox.getChildren().add(showBtn);

        Scene componentsScene = new Scene(vBox);
        primaryStage.setTitle("Parallel Machine Learning Simulator");
        primaryStage.setScene(componentsScene);
        primaryStage.show();

    }

//    @Override
//    public void start(Stage primaryStage) {
//        //Button btOk = new Button("OK");
//        //Scene scene = new Scene(btOk, 60, 30);
//
//        NumberAxis stalenessAxis = new NumberAxis(0, MLsettings.TOTAL_ITERATION/10, 1);
//        NumberAxis staleGoodnessAxis = new NumberAxis(2000, 6000, 100);
//        AreaChart<Number, Number> areaChart = new AreaChart<>(stalenessAxis, staleGoodnessAxis);
//        areaChart.setTitle(" on Staleness ");
//
//        XYChart.Series seriesStaleness = new XYChart.Series();
//        seriesStaleness.setName("dev 20");
//        XYChart.Series seriesDeviance = new XYChart.Series();
//        seriesDeviance.setName("dev 25");
//        XYChart.Series seriesDeviance2 = new XYChart.Series();
//        seriesDeviance2.setName("dev 30");
//        for(int i=0; i<MLsettings.TOTAL_ITERATION/10;i++) {
//            seriesStaleness.getData().add(new XYChart.Data(i,SimpleML.simulation(i)));
//            System.out.printf(i + " ");
//        }
//        MLsettings.TIME_DEVIATION_BY_NODES = 25;
//        for(int i=0; i<MLsettings.TOTAL_ITERATION/10;i++) {
//            seriesDeviance.getData().add(new XYChart.Data(i,SimpleML.simulation(i)));
//            System.out.printf(i + " ");
//        }
//        MLsettings.TIME_DEVIATION_BY_NODES = 30;
//        for(int i=0; i<MLsettings.TOTAL_ITERATION/10;i++) {
//            seriesDeviance2.getData().add(new XYChart.Data(i,SimpleML.simulation(i)));
//            System.out.printf(i + " ");
//        }
//
//        Scene scene = new Scene(areaChart, 800, 600);
//        areaChart.getData().addAll(seriesStaleness);
//        areaChart.getData().addAll(seriesDeviance);
//        areaChart.getData().addAll(seriesDeviance2);
//        primaryStage.setTitle("Machine Learning Simulator");
//        primaryStage.setScene(scene);
//        primaryStage.setWidth(900);
//        primaryStage.setHeight(600);
//        primaryStage.show();
//    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
