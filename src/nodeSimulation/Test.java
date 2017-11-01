package nodeSimulation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Test {
    public static void main(String[] args) {
        int staleness;
        for(staleness = 0; staleness < 20; staleness++) {
            System.out.println("" + (1/((double)staleness + 1)));
        }
    }
}

