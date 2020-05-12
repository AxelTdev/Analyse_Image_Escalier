package openCv;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.sun.javafx.geom.Rectangle;

import extractP.LineDetect;
import extractP.RectangleDetection;
import filtre.Image;
import util.Util;

public class Test {

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat src = Test.method1("src/ressource/1.jpg", true, 150);
		HighGui.imshow("Before", src);

		HighGui.waitKey(0);

	}

	public static Mat method1(String path, boolean binary, int binarySeuil) throws IOException {
		Mat src = new Mat();
		if (binary) {

			Image img = new Image(path, "");
			img = img.createNBImage(binarySeuil);// 160
			byte[] pixels = ((DataBufferByte) img.getImg().getRaster().getDataBuffer()).getData();
			// chargement de l'image et conversion en noir et blanc
			src = new Mat(img.getImg().getHeight(), img.getImg().getWidth(), CvType.CV_8UC3);
			// Apply Gaussian blur to get good results
			src.put(0, 0, pixels);
		} else {
			src = Imgcodecs.imread(path, Imgcodecs.IMREAD_GRAYSCALE);
		}

		Imgproc.GaussianBlur(src, src, new Size(5, 5), 0, 0);

		Mat dst = new Mat(), out_img = new Mat(), control = new Mat();
		Imgproc.Canny(src, dst, 80, 240, 3);
		
		
		int kernelSize = 4;
		// Preparer la matrice kernel
		Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
				new Size(2 * kernelSize + 1, 2 * kernelSize + 1), new Point(kernelSize, kernelSize));

		// Application de louverture (1.erosion 2/dilatation)
		//Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, element);
		kernelSize = 0;
		element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
				new Size(2 * kernelSize + 1, 2 * kernelSize + 1), new Point(kernelSize, kernelSize));
		// Application de louverture (1.erosion 2/dilatation)
		//Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_OPEN, element);
		Object[] result = RectangleDetection.rectangleDetect(dst);

		result = RectangleDetection.rectangleDetect(dst);
		
		//dst  = RectangleDetection.drawRectangle((Rect[])result[1], src, (MatOfPoint2f[])result[3]);
//System.out.println(Util.countLine(dst));
	dst = LineDetect.detectLine(dst);
		
		

		return dst;

	}

}
