package config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import extractP.RectangleDetection;
import filtre.Image;
import interfac.Gui;
import pre_Traitement.Pre_traitement_Ouverture;
import util.Util;

/**
 * methode qui se deroule tel que
 * 
 * - niveaux de gris -extraction par lignes
 * 
 * @author Axel
 *
 */
public class Method1 extends Config {

	public Method1(List<String> path) {
		super(path);

	}

	private static int H = 1000;

	private static int W = 600;

	public static int getH() {
		return H;
	}

	public static void setH(int h) {
		H = h;
	}

	public static int getW() {
		return W;
	}

	public static void setW(int w) {
		W = w;
	}

	public void display() throws IOException {
		// Redimension des images a

		for (int i = 0; i < super.getListImgPath().size(); i++) {
			BufferedImage b = ImageIO.read(new File(super.getListImgPath().get(i)));

			b = Util.resize(b, 700, 1000);

			Util.enregistreImage(b, super.getListImgPath().get(i));
		}

		// rogner vers la partie qui nous interesse (prend le plus grand rectangle
		// contour)

		for (int i = 0; i < super.getListImgPath().size(); i++) {
			// pre traitement de la matrice
			// trouver le plus grand rectangle
			Rect r = Pre_traitement_Ouverture.pTraitementOuverture(new Image(super.getListImgPath().get(i), ""));

			// roger le rectangle par apport a la zone qui nous interresss
			//si limage est pas plus large que longue
			if (r.width < r.height) {
				System.out.println("area : " + r.area());
				Mat srcTemp = new Mat();
				srcTemp = Imgcodecs.imread(super.getListImgPath().get(i), Imgcodecs.IMREAD_ANYCOLOR);
				System.out.println("rectangle " + r.height + "  " + r.width + " " + r.x + "  " + r.y);
				srcTemp = Util.rogne(srcTemp, r.x, r.y, r.height, r.width);

				BufferedImage b = (BufferedImage) HighGui.toBufferedImage(srcTemp);
				System.out.println(i);
				Util.enregistreImage(b, super.getListImgPath().get(i));
			}

		}

	}

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		List<String> str = new ArrayList<String>();
		str.add("src/ressource/escaliers/2.jpg");
		Method1 t = new Method1(str);
		t.display();
	}

}
