package openCv;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Dilatation {

	public static Mat dilate(Mat src, boolean iserode) {
		Mat dest = new Mat();
		int elementType = Imgproc.CV_SHAPE_RECT;
		int kernelSize = 1;

		Mat element = Imgproc.getStructuringElement(elementType, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
				new Point(kernelSize, kernelSize));

		if (iserode) {
			kernelSize = 1;

			Imgproc.erode(src, dest, element);
			kernelSize = 100;
			Imgproc.dilate(src, dest, element);

		} else {
			kernelSize = 0;

			// Imgproc.dilate(src, dest, element);
		}

		return dest;

	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat cdst = new Mat();
		Mat cdstP = new Mat();
		String default_file = "src/ressource/2.jpg";
		// chargement de l'image et conversion en noir et blanc
		Mat dest = new Mat();
		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);
		cdst = Dilatation.getLine(src, true);

		// visualisation de limage
		cdst = Dilatation.dilate(cdst, true);
		cdst = Dilatation.enleveNoir(cdst);

		HighGui.imshow("wesh", src);
		;

		HighGui.waitKey();

	}

	public static Mat getLine(Mat src, boolean isOuverture) {
		Mat cdst = new Mat();

		// chargement de l'image et conversion en noir et blanc
		Mat dest = new Mat();

		Imgproc.Canny(src, dest, 80, 240, 3, false);

		Imgproc.cvtColor(dest, cdst, Imgproc.COLOR_GRAY2BGR);

		// visualisation de limage
		cdst = Dilatation.dilate(cdst, true);

		// Standard Hough Line Transform
		Mat lines = new Mat(); // will hold the results of the detection
		// visualisation de limage
		if (isOuverture) {
			dest = Dilatation.dilate(dest, true);
		}

		
		// dilatation pour visuel
		cdst = Dilatation.dilate(cdst, true);

		return cdst;

	}

	public static Mat enleveNoir(Mat src) {

		for (int i = 0; i < src.rows(); i++) {
			for (int j = 0; j < src.cols(); j++) {

				double[] data = src.get(i, j);


				if (data[2] == 255) {
					if (data[1] == 255) {
						data[0] = 0;
						data[1] = 0;
						data[2] = 0;
					} else {
						data[0] = 255;
						data[1] = 255;
						data[2] = 255;
					}

				} else {
					data[0] = 0;
					data[1] = 0;
					data[2] = 0;

				}

				src.put(i, j, data);

			}
		}
		return src;
	}
}
