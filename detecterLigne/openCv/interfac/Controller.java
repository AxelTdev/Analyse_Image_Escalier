package interfac;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import config.Method1;
import config.Method2;
import extractP.LineDetect;
import filtre.DisplayStaskChart;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import openCv.Test;
import util.Util;

/**
 * controller de l'interface permmetant de gerer les interactions avec
 * l'utilisateur
 * 
 * @author Axel
 *
 */
public class Controller implements Initializable {

	// redimentionement du graphique de l'erreur
	final double SCALE_DELTA = 1.1;

	// le chemin d'acces du fichier pour le chargement d'un treillis
	private String pathFile;

	@FXML
	// label de la classe 1 cas vrai
	private Label classe1T;

	@FXML
	private Label classe2N;

	@FXML
	// label de la classe 1 cas vrai
	private Label classe1N;

	@FXML
	private Label classe2T;

	@FXML
	private Pane root;

	@FXML
	private CheckBox resize;

	@FXML
	private CheckBox crop;

	@FXML

	private Label result1;

	@FXML

	private Label result;

	@FXML

	private Label gFilterL;

	@FXML

	private Label openingL;

	@FXML

	private CheckBox option1;

	@FXML

	private CheckBox option2;

	@FXML

	private CheckBox bina;

	@FXML

	private Label errorAL;

	@FXML

	private Label closingL;

	@FXML

	private Slider slideError;

	@FXML

	private Slider slideGaussian;

	@FXML

	private Slider slideOpening;

	@FXML

	private Slider slideClosing;

	@FXML

	private FlowPane flowData;
	
	private int countBon;

	@FXML

	private Label tx;
	private List<Integer> resultAttentdu = new ArrayList<Integer>();

	private List<String> pathImage = new ArrayList<>();

	@FXML
	private MenuItem histo;

	private List<Integer> resultatObtenuM1 = new ArrayList<Integer>();

	@FXML
	private MenuItem helpBug;

	@FXML
	private MenuItem helpJavadoc;

	@FXML
	private MenuItem data;

	@FXML
	private MenuItem imageData;

	// preparation d'un navigateur web
	private Desktop desktop = Desktop.getDesktop();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// verifie que le splash screen a ete deploye
		if (!Gui.isSplash) {
			this.splashscreen();
		}

		/**
		 * cas ou utilisateur veut ajouter des images srouces
		 */
		imageData.setOnAction(((event) -> {
			
			if (resultAttentdu.size() > 0) {
				this.pathImage = this.getPathImage((this.directoryChosser("Choose a directory with images")));
				
			}

		}));

		this.slideClosing.setOnDragDetected(((event) -> {
			this.closingL.setText("Closing : " + Math.round(this.slideClosing.getValue()));

		}));

		this.slideOpening.setOnDragDetected(((event) -> {
			this.openingL.setText("Opening : " + Math.round(this.slideOpening.getValue()));

		}));

		this.slideError.setOnDragDetected(((event) -> {
			this.errorAL.setText("Error accepted : " + Math.round(this.slideError.getValue()));
			// declenche le programme
			this.go();
		}));
		
		this.slideGaussian.setOnDragDetected(((event)->{
			this.gFilterL.setText("Gaussian filter : " +Math.round(this.slideGaussian.getValue()));
		}));

		/**
		 * cas ou utilisateur veut ajouter des resultats attendus
		 */
		data.setOnAction(((event) -> {
			this.resultAttentdu.clear();
			this.resultAttentdu = Util.readeDataFile(this.fileChooserToPath("Search data File's"));

		}));
		/**
		 * 
		 */
		histo.setOnAction(((event) -> {

			// demande de limage

			String path1 = this.fileChooserToPath("Choose image to analyse");
			Stage st = new Stage();

			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Valeurs");

			NumberAxis yAxis = new NumberAxis();
			yAxis.setLabel("Occurence");

			StackedBarChart<String, Double> stackedBarChart = new StackedBarChart(xAxis, yAxis);

			XYChart.Series<String, Double> dataSeries1 = new XYChart.Series<>();

			// histo de l'image
			int[] histoImage = null;
			try {
				histoImage = new filtre.Image(path1, "").createHisto();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < 100; i++) {
				dataSeries1.getData().add(new XYChart.Data(i + "", histoImage[i]));
			}
			// histo de 100 à 200
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
			// histo de 200 à 250
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

			st.setScene(scene);
			st.setHeight(300);
			st.setWidth(1200);

			st.show();

		}));

		helpJavadoc.setOnAction(((event) -> {

			try {

				// lecture du fichier

				desktop.browse(new URI("https://proemial-walk.000webhostapp.com/ccc"));

			} catch (IOException | URISyntaxException e) {

				e.printStackTrace();
			}

		}));

		/**
		 * cas ou l utilisateur clique sur l'aide manuel utilisation
		 */
		helpBug.setOnAction(((event) -> {

			// lien mail

			Alert errorAlert = new Alert(AlertType.INFORMATION);
			errorAlert.setHeaderText("Make a suggestion / report a bug");
			errorAlert.setContentText("projet@mail.com");
			errorAlert.showAndWait();

		}));

	}

	private void splashscreen() {

		try {

			// mise a jour de la variable pour ne l executer que une fois
			Gui.isSplash = true;

			// chargement de la vue
			FXMLLoader fxlml = new FXMLLoader(getClass().getResource("/ressource/splash.fxml"));

			Pane pane = fxlml.load();

			// ajout de la vue
			root.getChildren().setAll(pane);

			// animations d entree de 1 seconde
			FadeTransition animationIn = new FadeTransition(Duration.seconds(1), pane);

			animationIn.setFromValue(0);

			animationIn.setToValue(1);

			animationIn.setCycleCount(1);

			animationIn.play();

			// animation de sortie de 2 seconde
			FadeTransition animationOut = new FadeTransition(Duration.seconds(2), pane);

			animationOut.setFromValue(1);

			animationOut.setToValue(0);

			animationOut.setCycleCount(1);

			// a la fin de lanimation d entree enclencher l animation de sortie
			animationIn.setOnFinished(((event) -> {

				// lancer lanimation de sortie
				animationOut.play();

			}));

			animationIn.setOnFinished(((event) -> {
				ImageView imgV = null;
				// chercher le imageview dans le pane pour le modifier
				for (Node e : root.getChildren()) {

					if (e instanceof AnchorPane) {
						AnchorPane anch = (AnchorPane) e;

						imgV = (ImageView) anch.getChildren().get(0);

					}
				}
				FileInputStream input = null;
				try {
					// ImageIO.read(new File("ressource/splashNB.jpg"));
					input = new FileInputStream("src/ressource/splashNB.jpg");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Image image = new Image(input);

				imgV.setImage(image);
				animationOut.play();

			}));

			animationOut.setOnFinished(((event) -> {
				// chargement de la vue
				FXMLLoader fxlml1 = new FXMLLoader(getClass().getResource("/ressource/gui.fxml"));

				Pane pane1 = null;
				try {
					pane1 = fxlml1.load();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// ajout de la vue
				root.getChildren().setAll(pane1);
			}));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * methode pour connaitre le chemin d acces a partir d un explorateur de fichier
	 * 
	 * @param titreFileChooser le titre de l explorateur de fichier
	 * @return le chemin d acces
	 */
	public String fileChooserToPath(String titreFileChooser) {

		// si le cache du graphique de l erreur alors le rendre invisible

		// creation explorateur de fichier
		FileChooser fch = new FileChooser();

		// mise a jour du titre de l explorateur de fichier
		fch.setTitle(titreFileChooser);

		// mise a jour du chemin de l explorateur de fichier par defaut
		fch.setInitialDirectory(new File(System.getProperty("user.home")));

		// creation du fichier correspondant au chemin recherche
		File fichierTreillis = fch.showOpenDialog(root.getScene().getWindow());

		pathFile = fichierTreillis.getPath();

		return pathFile;

	}



	private List<String> getPathImage(File dossier) {
		List<String> result = new ArrayList<String>();

		if (dossier.isDirectory()) { // make sure it's a directory
			for (final File f : dossier.listFiles()) {

				result.add(f.getAbsolutePath());
			}
		}

		return result;
	}

	private File directoryChosser(String setTitle) {
		DirectoryChooser chooser = new DirectoryChooser();

		chooser.setTitle(setTitle);
		File defaultDirectory = new File((System.getProperty("user.home")));
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(root.getScene().getWindow());

		return selectedDirectory;
	}

	private Pane paneFactory(BufferedImage b, int expectedV, int resultV, boolean isCorrect)
			throws FileNotFoundException {
		Pane p = new Pane();

		GridPane gp = new GridPane();

		gp.setGridLinesVisible(true);
		gp.setLayoutX(15.0);
		gp.setLayoutY(14.0);
		gp.setPrefHeight(205.0);
		gp.setPrefWidth(156.0);

		ColumnConstraints col1 = new ColumnConstraints();

		col1.setHgrow(Priority.SOMETIMES);
		col1.setMinWidth(10.0);
		col1.setPrefWidth(100.0);

		RowConstraints row1 = new RowConstraints();

		row1.setMaxHeight(204.0);
		row1.setMinHeight(10.0);
		row1.setPrefHeight(145.0);
		row1.setVgrow(Priority.SOMETIMES);

		RowConstraints row2 = new RowConstraints();

		row2.setMaxHeight(84.0);
		row2.setMinHeight(10.0);
		row2.setPrefHeight(40.0);
		row2.setVgrow(Priority.SOMETIMES);

		RowConstraints row3 = new RowConstraints();

		row3.setMaxHeight(54.0);
		row3.setMinHeight(10.0);
		row3.setPrefHeight(28.0);
		row3.setVgrow(Priority.SOMETIMES);

		RowConstraints row4 = new RowConstraints();

		row4.setMaxHeight(54.0);
		row4.setMinHeight(10.0);
		row4.setPrefHeight(30.0);
		row4.setVgrow(Priority.SOMETIMES);

		gp.getColumnConstraints().add(col1);

		gp.getRowConstraints().addAll(row1, row2, row3, row4);

		ImageView imgV1 = new ImageView();

		imgV1.setFitHeight(136.0);
		imgV1.setFitWidth(158.0);
		imgV1.setPickOnBounds(true);
		imgV1.setSmooth(true);

		Image img = SwingFXUtils.toFXImage(b, null);
		imgV1.setImage(img);

		gp.add(imgV1, 0, 0);

		Label expected = new Label("Expected : " + expectedV);

		expected.setPrefHeight(23.0);
		expected.setPrefWidth(239.0);
		expected.setTextAlignment(TextAlignment.CENTER);

		Insets one = new Insets(0.0f, 0.0f, 0.0f, 45.0f);

		gp.add(expected, 0, 1);

		GridPane.setMargin(expected, one);

		Label result = new Label("Result : " + resultV);

		result.setPrefHeight(23.0);
		result.setPrefWidth(239.0);
		result.setTextAlignment(TextAlignment.CENTER);

		gp.add(result, 0, 2);
		GridPane.setMargin(result, one);

		ImageView imgV2 = new ImageView();

		imgV2.setFitHeight(29.0);
		imgV2.setFitWidth(242.0);
		imgV2.setPickOnBounds(true);
		imgV2.setPreserveRatio(true);
		FileInputStream input = null;
		try {
			// ImageIO.read(new File("ressource/splashNB.jpg"));
			input = new FileInputStream("src/ressource/splashNB.jpg");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (isCorrect) {
			input = new FileInputStream("src/ressource/accepted_48.png");

		} else {
			input = new FileInputStream("src/ressource/cancel_48.png");

		}
		Image image = new Image(input);
		imgV2.setImage(image);
		Insets insetsGauche = new Insets(0.0f, 0.0f, 0.0f, 65.0f);
		GridPane.setMargin(imgV2, insetsGauche);
		gp.add(imgV2, 0, 3);

		p.getChildren().addAll(gp);
		p.setStyle("-fx-background-color: black");

		return p;

	}

	private void go() {
		countBon = 0;

			if (this.pathImage.size() > 0) {

				Method1 t = new Method1(this.pathImage, resize.isSelected(), crop.isSelected());
			
				Method2 t1 = new Method2(this.pathImage, (int) slideOpening.getValue(), (int) slideClosing.getValue(),
						(int) slideGaussian.getValue(), (int) slideGaussian.getValue(), this.bina.isSelected(),
						this.option1.isSelected());

				try {
					System.out.println("nb path"+  t1.getListImgPath().size());
					//t.display();
					t1.display();
					System.out.println(t1.getResultatObtenu());
					System.out.println("nb path"+  t1.getListImgPath().size());
					System.out.println(this.resultAttentdu);
					/**
					 * remplissage du FlowPane
					 * 
					 */
					flowData.getChildren().clear();
					boolean isCorrect;
					flowData.setHgap(10);
					flowData.setVgap(10);
					for (int i = 0; i < t1.getListImgPath().size(); i++) {
						isCorrect = this.resultAttentdu.get(i).equals(t1.getResultatObtenu().get(i));
						
						if(ValueRange.of(this.resultAttentdu.get(i)-(int)this.slideError.getValue() -1, this.resultAttentdu.get(i)+(int)this.slideError.getValue()+1).isValidIntValue(t1.getResultatObtenu().get(i))) {
							countBon++;
						}
						flowData.getChildren().add(this.paneFactory((BufferedImage) t1.getTransfoImgList().get(i),
								this.resultAttentdu.get(i), t1.getResultatObtenu().get(i), isCorrect));
					}
					
					

				} catch (IOException e) {

					e.printStackTrace();
				}
				
				//remplit tableau
				
				this.result.setText(countBon+"");
				
				this.result1.setText((this.pathImage.size()-countBon)+"");
				System.out.println("on a plutot " + countBon);
				System.out.println("on a plutot " + this.pathImage.size());
				this.tx.setText(Math.round(((double)countBon/(double)this.pathImage.size()*100))+"%");
			}
			
			
		}

	

}
