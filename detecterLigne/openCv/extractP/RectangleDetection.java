package extractP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import util.Util;

public class RectangleDetection {

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String default_file = "src/ressource/mmm.jpg";

		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_ANYCOLOR);

		// image en niveau de gris
		Mat srcGris = new Mat();
		Imgproc.cvtColor(src, srcGris, Imgproc.COLOR_BGR2GRAY);
		// application du filtre de Canny pour trouver le contours
		Mat cannyOutput = new Mat();
		int threshold = 100;
		Imgproc.Canny(srcGris, cannyOutput, threshold, threshold * 2);

		// lier contours pour obtenir des rectangles
		Object[] result = rectangleDetect(cannyOutput);

		// ercire les recctangles
		src = RectangleDetection.drawRectangle((Rect[]) result[1], (Mat) result[2], (MatOfPoint2f[]) result[3]);

		HighGui.imshow("Before", src);
		HighGui.waitKey(0);

	}

	/**
	 * methoque qui permet lextraction de formes rectangulaires
	 * 
	 * @param contours la liste des contours obtenus
	 * @param src      la matrice apres le filtre Canny
	 * @return une matrice avec des rectangles representant des marches
	 */
	public static Object[] rectangleDetect(Mat src) {

		// chercher les contours
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

		MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];

		Rect[] boundRect = new Rect[contours.size()];

		for (int i = 0; i < contours.size(); i++) {
			contoursPoly[i] = new MatOfPoint2f();
			
			Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);

			boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
			

		}

		Rect max = Util.maxRectArea(boundRect);
		Object[] result = new Object[4];

		result[0] = max;
		result[1] = boundRect;
		result[3] = contoursPoly;
		result[2] = src;

		return result;

	}

	public static Mat drawRectangle(Rect[] boundRect, Mat src, MatOfPoint2f[] contoursPoly) {
		// ecrire les rectangles dans l image
		// initialisation de la matrice
		Mat ecrireRect = Mat.zeros(src.size(), CvType.CV_8UC3);

		// pour chaque rectangle lajouter a la liste
		List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
		for (MatOfPoint2f poly : contoursPoly) {
			contoursPolyList.add(new MatOfPoint(poly.toArray()));
		}
		// nombre aleatoir
		Random rng = new Random(12345);

		for (int i = 0; i < boundRect.length; i++) {
			Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
			Imgproc.drawContours(ecrireRect, contoursPolyList, i, color);
			Imgproc.rectangle(ecrireRect, boundRect[i].tl(), boundRect[i].br(), color, 2);

		}
		return ecrireRect;

	}
}
