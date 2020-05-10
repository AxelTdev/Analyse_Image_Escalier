package extractP;

import java.io.IOException;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import openCv.Test;

public class LineDetect {

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String default_file = "src/ressource/1.jpg";

		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);
		src = LineDetect.detectLine(src);

		HighGui.imshow("Before", src);
		HighGui.waitKey(0);

	}

	public static Mat detectLine(Mat dst) {
		Mat control = new Mat();
		Mat out_img = new Mat();
		Vector<Integer> y_keeper_for_lines = new Vector<>();
		Imgproc.cvtColor(dst, out_img, Imgproc.COLOR_GRAY2BGR);
		Imgproc.cvtColor(dst, control, Imgproc.COLOR_GRAY2BGR);
		Mat lines = new Mat();
		Imgproc.HoughLinesP(dst, lines, 1, Math.PI / 180, 30, 40, 5);

		for (int x = 0; x < lines.rows(); x++) {

			double[] vec = lines.get(x, 0);

			double[] val = new double[4];
			val[0] = 0;
			val[1] = ((float) vec[1] - vec[3]) / (vec[0] - vec[2]) * -vec[0] + vec[1];
			val[2] = dst.cols();
			val[3] = ((float) vec[1] - vec[3]) / (vec[0] - vec[2]) * (dst.cols() - vec[2]) + vec[3];

			Imgproc.line(control, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 3,
					Imgproc.LINE_AA);
		}

		if (lines.get(0, 0) != null) {
			double[] vec = lines.get(0, 0);
			double[] valLine1 = new double[4];

			valLine1[0] = 0;
			valLine1[1] = ((float) vec[1] - vec[3]) / (vec[0] - vec[2]) * -vec[0] + vec[1];
			valLine1[2] = dst.cols();
			valLine1[3] = ((float) vec[1] - vec[3]) / (vec[0] - vec[2]) * (dst.cols() - vec[2]) + vec[3];

			Imgproc.line(out_img, new Point(0, valLine1[1]), new Point(dst.cols(), valLine1[1]), new Scalar(0, 0, 255),
					3, Imgproc.LINE_AA);
			y_keeper_for_lines.add((int) valLine1[1]);

		} else {
			System.out.println("dd");
		}

		boolean okey = true;

		for (int i = 1; i < lines.rows(); i++) {
			double[] vec1 = lines.get(i, 0);
			double[] val = new double[4];

			val[0] = 0;
			val[1] = ((float) vec1[1] - vec1[3]) / (vec1[0] - vec1[2]) * -vec1[0] + vec1[1];
			val[2] = dst.cols();
			val[3] = ((float) vec1[1] - vec1[3]) / (vec1[0] - vec1[2]) * (dst.cols() - vec1[2]) + vec1[3];
			for (int m : y_keeper_for_lines) {
				if (Math.abs(m - val[1]) < 15)
					okey = false;

			}
			if (okey) {
				Imgproc.line(out_img, new Point(0, val[1]), new Point(dst.cols(), val[1]), new Scalar(0, 0, 255), 3,
						Imgproc.LINE_AA);
				y_keeper_for_lines.add((int) val[1]);

			}
			okey = true;

		}
		return out_img;
	}
}
