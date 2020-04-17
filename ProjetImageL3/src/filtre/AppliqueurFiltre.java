package filtre;

import java.awt.FlowLayout;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.Util;

/*
 * classe principale que analyse les pixels de l'images selon les filtres à appliquer
 */
public class AppliqueurFiltre {
	private Image img;

	private double[] histoCouleur;
	private double[] histoNB;

	/*
	 * constructeur
	 * 
	 * @param path de l'image
	 */
	public AppliqueurFiltre(Image img) throws IOException {
		this.img = img;
		this.histoCouleur = new double[256];
		this.histoNB = new double[2];
	}

	

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public double[] getHistoCouleur() {
		return histoCouleur;
	}

	public void setHistoCouleur(double[] histoCouleur) {
		this.histoCouleur = histoCouleur;
	}

	public double[] getHistoNB() {
		return histoNB;
	}

	public void setHistoNB(double[] histoNB) {
		this.histoNB = histoNB;
	}

}
