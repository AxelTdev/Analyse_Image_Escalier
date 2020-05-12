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

	private boolean isResize;

	private boolean isCrop;

	public Method1(List<String> path, boolean resize, boolean crop) {
		super(path);
		this.isResize = resize;
		this.isCrop = crop;

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
		// Redimension des images si lhauteur est sup à 1500

		if (isResize) {
			for (int i = 0; i < super.getListImgPath().size(); i++) {
				BufferedImage b = ImageIO.read(new File(super.getListImgPath().get(i)));
				
				if(b.getHeight()>1500) {
					
					double coeff = b.getHeight()/1500;
					b = Util.resize(b, (int)b.getHeight()/coeff, (int)b.getWidth()/coeff);
					
				}
				

				

				Util.enregistreImage(b, super.getListImgPath().get(i));
			}
		}

		// rogner vers la partie qui nous interesse (prend le plus grand rectangle
		// contour)
		if (isCrop) {
			for (int i = 0; i < super.getListImgPath().size(); i++) {
				// pre traitement de la matrice
				// trouver le plus grand rectangle
				Image img = new Image(super.getListImgPath().get(i), "");
				Rect r = Pre_traitement_Ouverture.pTraitementOuverture(img);

				// roger le rectangle par apport a la zone qui nous interresss
				// si limage est pas plus large que longue
				if (r.area()<img.getImg().getHeight()*img.getImg().getWidth()) {
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
	}

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		List<String> str = new ArrayList<String>();
		str.add("src/ressource/escaliers/Imag1.jpg");
		Method1 t = new Method1(str, true, true);
		t.display();
	}

}
