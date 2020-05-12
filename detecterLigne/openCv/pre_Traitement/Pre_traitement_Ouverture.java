package pre_Traitement;

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

import extractP.LineDetect;
import extractP.RectangleDetection;
import filtre.Image;
import javafx.scene.image.ImageView;
import openCv.Dilatation;
import openCv.Erosion;
import util.Util;

/**
 * pre traitement bien pour rogner vers lescalier mais pas pour reconnaissance
 * de marches
 * 
 * @author Axel
 *
 */
public class Pre_traitement_Ouverture {

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// src = Util.resize(src, 1000, 1000);
		Rect r = pTraitementOuverture(new Image("src/ressource/cancel_48.png", ""));
		
	

	}

	

	public static Rect pTraitementOuverture(Image srcImg) throws IOException {

		int seuil = 0;
		System.out.println(Util.averageHisto(srcImg.createHisto()));
		int moyenne = Util.averageHisto(srcImg.createHisto());
		if (moyenne < 157 && moyenne > 140) {
			seuil = 150;
		} else if (moyenne > 157 && moyenne < 190) {
			seuil = 160;

		} else if (moyenne < 140 && moyenne > 100) {
			seuil = 100;
		} else if (moyenne > 190) {
			seuil = 170;

		} else {
			seuil = 80;
			System.err.print("cas non certain");
		}

		srcImg = srcImg.createNBImage(seuil);// 160
		byte[] pixels = ((DataBufferByte) srcImg.getImg().getRaster().getDataBuffer()).getData();
		// chargement de l'image et conversion en noir et blanc
		Mat dest = new Mat(srcImg.getImg().getHeight(), srcImg.getImg().getWidth(), CvType.CV_16U);

		Mat src = new Mat(srcImg.getImg().getHeight(), srcImg.getImg().getWidth(), CvType.CV_8UC3);
		src.put(0, 0, pixels);

		int kernelSize = 1;
		int elementType = Imgproc.CV_SHAPE_RECT;
		Mat element = Imgproc.getStructuringElement(elementType, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
				new Point(kernelSize, kernelSize));

		// Imgproc.GaussianBlur(dest, dest, new Size(5, 5), 0, 0);

		dest = src;

		Imgproc.dilate(dest, dest, element);
		kernelSize = 100;
		Imgproc.erode(dest, dest, element);

		dest = Dilatation.getLine(dest, false);

		BufferedImage b = (BufferedImage) HighGui.toBufferedImage(dest);

		String str = Util.enregistreImage(b);
		System.out.println(str);
		Mat temp = new Mat();
		temp = Imgcodecs.imread(str, Imgcodecs.IMREAD_GRAYSCALE);

		// lier contours pour obtenir des rectangles
		Object[] result = RectangleDetection.rectangleDetect(temp);

		result = RectangleDetection.rectangleDetect(temp);

		return (Rect) result[0];

	}

}
