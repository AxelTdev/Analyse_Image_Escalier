package config;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.sun.javafx.scene.traversal.Hueristic2D;

import extractP.LineDetect;
import filtre.Image;
import pre_Traitement.Pre_Traitement_NiveauxGris;
import util.Util;

/**
 * methode choisit pour la detection de marche descalier
 * 
 * @author Axel
 * 
 *
 */
public class Method2 extends Config {

	// parametes a moduler pour un effet different
	private int pOuverture;

	private int pFermeture;

	private int gaussX;

	private int gaussY;
	
	private boolean isBinary;
	
	private boolean option;

	public Method2(List<String> path, int pO, int pF, int gX, int gY, boolean isBinary, boolean option) {
		super(path);
		this.pOuverture = pO;
		this.pFermeture = pF;
		this.gaussX = gX;
		this.gaussY = gY;
		this.isBinary = isBinary;
		this.option = option;

	}

	@Override
	public void display() throws IOException {
		// pre traitement
		for (int i = 0; i < super.getListImgPath().size(); i++) {
			Mat src =null;
			if (isBinary) {
				int seuil;
				Image img = new Image(super.getListImgPath().get(i), "");
				int moyenne = Util.averageHisto(img.createHisto());
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
				
				img = img.createNBImage(seuil);// 160
				byte[] pixels = ((DataBufferByte) img.getImg().getRaster().getDataBuffer()).getData();
				// chargement de l'image et conversion en noir et blanc
				 src = new Mat(img.getImg().getHeight(), img.getImg().getWidth(), CvType.CV_8UC3);
				// Apply Gaussian blur to get good results
				src.put(0, 0, pixels);
			}else {
				 src = Imgcodecs.imread(super.getListImgPath().get(i), Imgcodecs.IMREAD_ANYCOLOR);
			}
			
			Mat dst = Pre_Traitement_NiveauxGris.pTraitementNG(src, this.pOuverture, this.pFermeture, this.gaussX,
					this.gaussY);

			// application de Canny pour detecter les contours

			Mat cannyOutput = new Mat();

			
			Imgproc.Canny(dst, cannyOutput, 80, 240, 3);
		
			// detection des lignes

			if(option) {//option opencv comptage
				cannyOutput = LineDetect.detectLine(cannyOutput);
				super.getResultatObtenu().add(Util.countRedLine(cannyOutput));
				this.getTransfoImgList().add(HighGui.toBufferedImage(cannyOutput));
			}else {
				super.getResultatObtenu().add(Util.countLine(cannyOutput));
				
				this.getTransfoImgList().add(HighGui.toBufferedImage(cannyOutput));
			}
			
			
		
		}
		
		System.out.println(super.getResultatObtenu());

	}
	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		List<String> str = new ArrayList<String>();
		str.add("src/ressource/escaliers/wesh.png");
		
		Method2 t = new Method2(str, 0, 0, 0, 0, false, true);
		t.display();
	}

	public int getpOuverture() {
		return pOuverture;
	}

	public void setpOuverture(int pOuverture) {
		this.pOuverture = pOuverture;
	}

	public int getpFermeture() {
		return pFermeture;
	}

	public void setpFermeture(int pFermeture) {
		this.pFermeture = pFermeture;
	}

	public int getGaussX() {
		return gaussX;
	}

	public void setGaussX(int gaussX) {
		this.gaussX = gaussX;
	}

	public int getGaussY() {
		return gaussY;
	}

	public void setGaussY(int gaussY) {
		this.gaussY = gaussY;
	}

}