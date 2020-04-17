package filtre;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DisplayStaskChart extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//ajout d'une image
		Image img = new Image("src/filtre/escalier.jpg", "dd");
		primaryStage.setTitle("Histogramme de l'image");
		//histo de 0 à 100
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Valeurs");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Occurence");

		StackedBarChart<String, Double> stackedBarChart = new StackedBarChart(xAxis, yAxis);

		XYChart.Series<String, Double> dataSeries1 = new XYChart.Series<>();

		// histo de l'image
		int[] histoImage = img.createHisto();
		for (int i = 0; i < 100; i++) {
			dataSeries1.getData().add(new XYChart.Data(i + "", histoImage[i]));
		}
		//histo de 100 à 200
		CategoryAxis xAxis2 = new CategoryAxis();
		xAxis2.setLabel("Valeurs");

		NumberAxis yAxis2 = new NumberAxis();
		yAxis2.setLabel("Occurence");

		StackedBarChart<String, Double> stackedBarChart1 = new StackedBarChart(xAxis2, yAxis2);

		XYChart.Series<String, Double> dataSeries2 = new XYChart.Series<>();

		// histo de l'image

		for (int i = 100; i < 200; i++) {
			dataSeries2.getData().add(new XYChart.Data(i + "", histoImage[i]));
		}
		//histo de 200 à 250
		CategoryAxis xAxis3 = new CategoryAxis();
		xAxis3.setLabel("Valeurs");

		NumberAxis yAxis3 = new NumberAxis();
		yAxis3.setLabel("Occurence");

		StackedBarChart<String, Double> stackedBarChart2 = new StackedBarChart(xAxis3, yAxis3);

		XYChart.Series<String, Double> dataSeries3 = new XYChart.Series<>();

		// histo de l'image

		for (int i = 200; i < 256; i++) {
			dataSeries3.getData().add(new XYChart.Data(i + "", histoImage[i]));
		}

		stackedBarChart.getData().add(dataSeries1);
		stackedBarChart1.getData().add(dataSeries2);
		stackedBarChart2.getData().add(dataSeries3);

		VBox vb = new VBox();

		vb.getChildren().addAll(stackedBarChart, stackedBarChart1, stackedBarChart2);

		Scene scene = new Scene(vb, 400, 200);

		primaryStage.setScene(scene);
		primaryStage.setHeight(300);
		primaryStage.setWidth(1200);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
