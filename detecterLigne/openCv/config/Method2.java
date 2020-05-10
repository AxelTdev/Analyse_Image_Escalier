package config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import extractP.LineDetect;
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

	public Method2(List<String> path) {
		super(path);

	}

	@Override
	public void display() throws IOException {
		// pre traitement
		for (int i = 0; i < super.getListImgPath().size(); i++) {
			Mat src = Imgcodecs.imread(super.getListImgPath().get(i), Imgcodecs.IMREAD_ANYCOLOR);
			Mat dst = Pre_Traitement_NiveauxGris.pTraitementNG(src, this.pOuverture, this.pFermeture, this.gaussX,
					this.gaussY);

			// application de Canny pour detecter les contours

			Mat cannyOutput = new Mat();

			Imgproc.Canny(dst, cannyOutput, 80, 240, 3);

			// detection des lignes

			cannyOutput = LineDetect.detectLine(cannyOutput);

			super.getResultatObtenu().add(Util.countLine(cannyOutput));

		}
		
		System.out.println(super.getResultatObtenu());

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
