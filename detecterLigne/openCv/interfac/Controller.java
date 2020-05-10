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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import config.Method1;
import extractP.LineDetect;
import filtre.DisplayStaskChart;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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

	private List<Integer> resultAttentdu = new ArrayList<Integer>();

	private List<String> pathImage = new ArrayList<>();

	@FXML
	private MenuItem ajoutUneImage;

	@FXML
	private ImageView imageView1;

	@FXML
	private Rectangle cache1;

	@FXML
	private ImageView imageView2;

	@FXML
	private Rectangle cache2;

	private String path;

	private Image imgSrc;

	@FXML
	private MenuItem histo;

	private List<Integer> resultatObtenuM1 = new ArrayList<Integer>();

	private List<Integer> resultatObtenuM2 = new ArrayList<Integer>();

	@FXML
	private MenuItem helpBug;

	@FXML
	private MenuItem helpJavadoc;

	@FXML
	private ImageView source1;

	@FXML
	private MenuItem helpManuelUtilisation;

	@FXML
	private MenuItem data;

	@FXML
	private Label step;

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

		ajoutUneImage.setOnAction(((event) -> {

			FileInputStream input = null;
			try {
				path = this.fileChooserToPath("Search image to analyze");
				input = new FileInputStream(path);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			Image image = new Image(input);

			this.imgSrc = image;

			source1.setImage(image);

			if (source1 != null) {
				cache1.setVisible(false);
				cache2.setVisible(false);
			}

		}));

		imageData.setOnAction(((event) -> {

			this.pathImage = this.getPathImage((this.directoryChosser("Choose a directory with images")));
			this.methode1Process(this.pathImage);
			
			if(this.pathImage.size()>0) {
				Method1 t = new Method1(this.pathImage);
				
				try {
					t.display();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}

		}));

		data.setOnAction(((event) -> {

			this.resultAttentdu = Util.readeDataFile(this.fileChooserToPath("Search data File's"));

		}));

		histo.setOnAction(((event) -> {
			if (this.imgSrc != null) {
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
					histoImage = new filtre.Image(path, "").createHisto();
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
			}

		}));

		helpJavadoc.setOnAction(((event) -> {

			try {

				// lecture du fichier

				desktop.browse(new URI("https://proemial-walk.000webhostapp.com/"));

			} catch (IOException | URISyntaxException e) {

				e.printStackTrace();
			}

		}));

		/**
		 * cas ou l utilisateur clique sur l'aide manuel utilisation
		 */
		helpManuelUtilisation.setOnAction(((event) -> {

			try {

				// lecture du fichier de la doc

				desktop.browse(new URI("https://proemial-walk.000webhostapp.com/Manuel_d_utilisation.pdf"));

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

	private void methode1Process(List<String> pathI) {

		for (int i = 0; i < pathI.size(); i++) {
			// creation une matrice pour chaque image
			try {
				// Mat src = Erosion.getErosionDilation(new filtre.Image(this.pathImage.get(i),
				// ""));

				// application de la methode1

				Mat src = Test.method1(pathI.get(i), false, 160);
				Util.resize(src, 536, 750);
				//this.resultatObtenuM1.add(Erosion.countLine(Dilatation.enleveNoir(src)));

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		System.out.println(this.resultatObtenuM1);
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
}
