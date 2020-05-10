package pre_Traitement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import extractP.LineDetect;
import extractP.RectangleDetection;
import util.Util;

public class Pre_Traitement_NiveauxGris {

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String default_file = "src/ressource/wesh.jpg";

		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_ANYCOLOR);

		src = pTraitementNG(src, 0, 0, 0, 0);
		// application du filtre de Canny pour trouver le contours
		Mat cannyOutput = new Mat();

		Imgproc.Canny(src, cannyOutput, 80, 240, 3);

		cannyOutput = LineDetect.detectLine(cannyOutput);

		HighGui.imshow("NB", cannyOutput);
		HighGui.waitKey(0);

	}

	public static Mat pTraitementNG(Mat src, int pouverture, int pfermeture, int gaussX, int gaussY) {
		Mat srcGris = new Mat();
		Mat dst = new Mat();
		Mat egalH = new Mat();

		// conversion en niveaux de gris
		Imgproc.cvtColor(src, srcGris, Imgproc.COLOR_BGR2GRAY);

		// egaliser histogramme
		Imgproc.equalizeHist(srcGris, egalH);

		// fermeture puis ouverture

		// taille du kernel
		int kernelSize = pfermeture;
		// Preparer la matrice kernel
		Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
				new Size(2 * kernelSize + 1, 2 * kernelSize + 1), new Point(kernelSize, kernelSize));

		// Application de louverture (1.erosion 2/dilatation)
		Imgproc.morphologyEx(egalH, dst, Imgproc.MORPH_CLOSE, element);
		kernelSize = pouverture;
		// Application de louverture (1.erosion 2/dilatation)
		Imgproc.morphologyEx(srcGris, dst, Imgproc.MORPH_OPEN, element);

		Core.normalize(dst, dst, 0, 255, Core.NORM_MINMAX);

		Imgproc.GaussianBlur(src, src, new Size(5, 5), gaussX, gaussY);

		return dst;
	}
}
