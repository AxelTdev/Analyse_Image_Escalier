package interfac;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Gui extends Application {

	static boolean isSplash = false;

	@Override
	public void start(Stage stage) throws Exception {
		// chargement de la vue en fxml
		FXMLLoader fxlml = new FXMLLoader(getClass().getResource("/ressource/gui.fxml"));

		Pane myPane = fxlml.load();

		Scene scene = new Scene(myPane);

		stage.setScene(scene);

		stage.show();
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}

}
