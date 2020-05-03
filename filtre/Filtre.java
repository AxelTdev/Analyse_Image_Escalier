package filtre;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * classe de base des filtres
 *
 */
public abstract class Filtre {
	private Image srcImage;

	private BufferedImage resultatFiltre;

	public BufferedImage getResultatFiltre() {
		return resultatFiltre;
	}

	public void setResultatFiltre(BufferedImage resultatFiltre) {
		this.resultatFiltre = resultatFiltre;
	}

	private int hg;
	private int hc;
	private int hd;
	private int cg;
	private int cc;
	private int cd;
	private int bg;
	private int bc;
	private int bd;

	public Filtre(Image img) {
		this.srcImage = img;
		this.resultatFiltre = null;

	}

	public abstract int filtreFenetre();

	public abstract BufferedImage displayFiltre(String conversionChoix);

	public Image getSrcImage() {
		return srcImage;
	}

	public void setSrcImage(Image srcImage) {
		this.srcImage = srcImage;
	}

	public int getHg() {
		return hg;
	}

	public void setHg(int hg) {
		this.hg = hg;
	}

	public int getHc() {
		return hc;
	}

	public void setHc(int hc) {
		this.hc = hc;
	}

	public int getHd() {
		return hd;
	}

	public void setHd(int hd) {
		this.hd = hd;
	}

	public int getCg() {
		return cg;
	}

	public void setCg(int cg) {
		this.cg = cg;
	}

	public int getCc() {
		return cc;
	}

	public void setCc(int cc) {
		this.cc = cc;
	}

	public int getCd() {
		return cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}

	public int getBg() {
		return bg;
	}

	public void setBg(int bg) {
		this.bg = bg;
	}

	public int getBc() {
		return bc;
	}

	public void setBc(int bc) {
		this.bc = bc;
	}

	public int getBd() {
		return bd;
	}

	public void setBd(int bd) {
		this.bd = bd;
	}

	public void fenetre(int i, int j) {

		// h : haut, g : gauche, d : droit, c : centre

		hg = srcImage.getPixelValue(i - 1, j - 1);
		hc = srcImage.getPixelValue(i - 1, j);
		hd = srcImage.getPixelValue(i - 1, j + 1);
		cg = srcImage.getPixelValue(i, j - 1);
		cc = srcImage.getPixelValue(i, j);
		cd = srcImage.getPixelValue(i, j + 1);
		bg = srcImage.getPixelValue(i + 1, j - 1);
		bc = srcImage.getPixelValue(i + 1, j);
		bd = srcImage.getPixelValue(i + 1, j + 1);
	}

	/**
	 * methode pour decider de la conversion
	 * 
	 * - "NB" une image en noire et blanc - "Gris" une image en gris
	 * 
	 * @return la transformation d'image
	 */
	public BufferedImage conversion(String conversionTarget) {
		BufferedImage result = null;
		switch (conversionTarget) {
		case "NB":
			try {
				this.srcImage.createNBImage(100).display();
				result = this.srcImage.createNBImage(100).getImg();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "Gris":
			try {
				this.srcImage.createGrayscaleImage().display();
				result = this.srcImage.createGrayscaleImage().getImg();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "":
			result = this.srcImage.getImg();
		}
		return result;

	}

	/*
	 * enregistre un resultat de filtre
	 */
	public void enregistreResultat(String nom) {
		
		File f = new File(System.getProperty("user.dir") + File.separator + "src"+File.separator + "ressource" + File.separator+ nom);

		try {
			ImageIO.write(this.resultatFiltre, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
