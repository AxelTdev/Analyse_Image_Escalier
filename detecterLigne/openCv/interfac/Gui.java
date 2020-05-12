package interfac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Core;

import com.sun.deploy.uitoolkit.impl.fx.Utils;

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
		new Gui().loadLib(System.getProperty("user.dir") + "/Downloads/opencv/build/java/x64/opencv_java430.dll", "opencv_java430.dll");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
	
	private void loadLib(String path, String name) {
		  name = System.mapLibraryName(name); // extends name with .dll, .so or .dylib
		  InputStream inputStream = null;
		  OutputStream outputStream = null;
		  try {
		    inputStream = getClass().getResourceAsStream("/" + path + name);
		    File fileOut = new File(System.getProperty("user.dir") + "/Downloads/opencv/build/java/x64/opencv_java430.dll");
		    System.out.println(System.getProperty("user.dir") + "/Downloads/opencv/build/java/x64/opencv_java430.dll");
		    outputStream = new FileOutputStream(fileOut);
		    IOUtils.copy(inputStream, outputStream);

		    System.load(fileOut.toString());//loading goes here
		  } catch (Exception e) {
		    //handle
		  } finally {
		    if (inputStream != null) {
		      try {
		        inputStream.close();
		      } catch (IOException e) {
		        //log
		      }
		    }
		    if (outputStream != null) {
		      try {
		        outputStream.close();
		      } catch (IOException e) {
		        //log
		      }
		    }
		  }}
}
