package openCv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.sun.javafx.geom.Rectangle;

import extractP.RectangleDetection;
import filtre.Image;
import util.Util;

public class Erosion {

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat dest = Erosion.getErosionDilation(new Image("src/ressource/escaliers/2.jpg", ""));
		HighGui.imshow("wesh", dest);
		HighGui.waitKey();
	}

	public static Mat getErosionDilation(Image srcImg) throws IOException {
		System.out.println(Util.averageHisto(srcImg.createHisto()));
		srcImg = srcImg.createNBImage(160);// 160
		
		byte[] pixels = ((DataBufferByte) srcImg.getImg().getRaster().getDataBuffer()).getData();
		// chargement de l'image et conversion en noir et blanc
		Mat dest = new Mat(srcImg.getImg().getHeight(), srcImg.getImg().getWidth(), CvType.CV_16U);

		Mat src = new Mat(srcImg.getImg().getHeight(), srcImg.getImg().getWidth(), CvType.CV_8UC3);
		src.put(0, 0, pixels);

		int kernelSize = 1;
		int elementType = Imgproc.CV_SHAPE_RECT;
		Mat element = Imgproc.getStructuringElement(elementType, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
				new Point(kernelSize, kernelSize));

		//Imgproc.GaussianBlur(dest, dest, new Size(5, 5), 0, 0);

		dest = src;

		Imgproc.dilate(dest, dest, element);
		kernelSize = 100;
		Imgproc.erode(dest, dest, element);

		dest = Dilatation.getLine(dest, false);

		BufferedImage b = (BufferedImage) HighGui.toBufferedImage(dest);

		String str = Util.enregistreImage( b);
		System.out.println(str);
		Mat temp = new Mat();
		temp = Imgcodecs.imread(str, Imgcodecs.IMREAD_GRAYSCALE);

		// lier contours pour obtenir des rectangles
		Object[] result = RectangleDetection.rectangleDetect(temp);

		result = RectangleDetection.rectangleDetect(temp);
		// ercire les recctangles
		temp = RectangleDetection.drawRectangle((Rect[]) result[1], (Mat) result[2], (MatOfPoint2f[]) result[3]);
		return temp;
	}

	/**
	 * meethode qui indique le nombre de lignes blanches rencontres
	 * 
	 * @param src la matrice
	 * @return le nombre de lignes
	 */
	public static int countLine(Mat src) {

		Boolean white = false;
		int count = 0;
		// traverse l axe y
		for (int i = 0; i < src.rows(); i++) {

			double[] data = src.get(i, 150);

			// si couleur blanche indiquer
			if (data[0] == 255 && data[0] == data[2] && data[0] == data[1]) {

				white = true;

			} else {
				// si couleur non blanche incrementer la ligne et continuer
				if (white) {
					count++;
					white = false;
				}
			}

		}

		return count;

	}
}
