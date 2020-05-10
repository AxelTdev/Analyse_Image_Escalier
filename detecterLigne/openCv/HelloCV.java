package openCv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class HoughLinesRun {
	public void run(String[] args) {
		// déclaration des valeurs de sortie
		Mat dst = new Mat();
		Mat cdst = new Mat();
		Mat cdstP;
		// chemin du fichier
		String default_file = "C:\\Users\\Axel\\eclipse-workspace\\Image\\src\\ressource\\escalier1.jpg";
		// chargement de l'image et conversion en noir et blanc
		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);

		// Detection des angles avec le filtre de Canny
		Imgproc.Canny(src, dst, 50, 200, 3, false);

		// Copy edges to the images that will display the results in BGR
		Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
		cdstP = cdst.clone();
		// Standard Hough Line Transform
		Mat lines = new Mat(); // will hold the results of the detection
		Imgproc.HoughLines(dst, lines, 1, Math.PI / 180, 120); // detecte les lignes//plus le dernier parametre est
																// grand moins il faut dintersection pour comtabiliser
																// une ligne
		// Dessine les lignes

		List<Point[]> totalLine = new ArrayList<>();
		for (int x = 0; x < lines.rows(); x++) {
			// pour chaque coordonne polaire
			double rho = lines.get(x, 0)[0], theta = lines.get(x, 0)[1];

			double a = Math.cos(theta), b = Math.sin(theta);

			// coordonnees carthesien
			double x0 = a * rho, y0 = b * rho;

			// de theta a tho obtient les deux point des extrimités de la ligne
			// plus variable 1000 grande plus la ligne est longue
			Point pt1 = new Point(Math.round(x0 + 1000 * (-b)), Math.round(y0 + 1000 * (a)));
			Point pt2 = new Point(Math.round(x0 - 1000 * (-b)), Math.round(y0 - 1000 * (a)));

			/**
			 * verification ligne paralle
			 * 
			 * 
			 * 
			 */
			Point[] PointTab = new Point[2];
			PointTab[0] = pt1;
			PointTab[1] = pt2;
			totalLine.add(PointTab);

		}
		double coeffDirecteurCompare = 0;
		double coeffDirecteurItre = 0;
		for (int i = 0; i < totalLine.size(); i++) {
			coeffDirecteurCompare = (totalLine.get(i)[1].x - totalLine.get(i)[0].x)
					/ (double) (totalLine.get(i)[1].y - totalLine.get(i)[0].y);
			coeffDirecteurItre = 0;
			for (int y = 0; y < totalLine.size(); y++) {

				coeffDirecteurItre = (totalLine.get(y)[1].x - totalLine.get(y)[0].x)
						/ (double) (totalLine.get(y)[1].y - totalLine.get(y)[0].y);
				if (1==1 ) {
					System.out.println("on a compare " + coeffDirecteurCompare + "iteration" + coeffDirecteurItre);
					// dessine la ligne
					Imgproc.line(cdst, totalLine.get(y)[0], totalLine.get(y)[1], new Scalar(0, 0, 255), 3,
							Imgproc.LINE_AA, 0);
				}
			}
		}

	
		// Show results
		//:HighGui.imshow("Source", src);
		HighGui.imshow("wesh", cdst);
		// HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform",
		// cdst);
		// HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform",
		// cdstP);
		// Wait and Exit
		HighGui.waitKey();
		System.exit(0);
	}
}

public class HelloCV {

	public static void main(String[] args) {
		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new HoughLinesRun().run(args);
	}

}