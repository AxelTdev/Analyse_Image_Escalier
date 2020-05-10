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

		Rect r = null;
		Object[] result = RectangleDetection.rectangleDetect(dst);

		// ercire les recctangles
		dst = RectangleDetection.drawRectangle((Rect[]) result[1], (Mat) result[2], (MatOfPoint2f[]) result[3]);
		r = (Rect) result[0];

		return dst;

	}

}
