package filtre;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.Util;

/**
 * 
 * classe décrivant une image
 *
 */
public class Image {
	private String srcPath;
	private String imgPath;
	private String imgName;
	private BufferedImage img;// image qui va etre manipuler dans le code

	public Image(String imgPath, String srcPath) throws IOException {
		this.imgPath = imgPath;
		this.srcPath = srcPath;

		StringBuffer str = new StringBuffer(imgPath);// recuperation du nom de l image a partir de son chemin
		str.delete(0, str.lastIndexOf(File.separator) + 1);
		imgName = str.toString();
		File o = new File(imgPath);

		img = ImageIO.read(o); // creation du BufferedImage

	}

	// constructeur avec un bufferedImage
	public Image(BufferedImage img, String imgPath, String srcPath) throws IOException {
		this.imgPath = imgPath;
		this.srcPath = srcPath;

		StringBuffer str = new StringBuffer(imgPath);// recuperation du nom de l image a partir de son chemin
		str.delete(0, str.lastIndexOf(File.separator) + 1);
		imgName = str.toString();
		File o = new File(imgPath);

		this.img = img;

	}

	/*
	 * methode pour sauvegarder un histogramme d'une image
	 */
	public int[] createHisto() {
		BufferedImage br = img;

		int[] histo = new int[256];

		for (int i = 1; i < br.getWidth() - 1; i++) {
			for (int j = 1; j < br.getHeight() - 1; j++) {

				histo[this.getPixelValue(i, j)]++; // ajoute 1 au "baton" de l'histogramme correspondant a la valeur du
													// LBP
				// trouvé

			}
		}

		return histo;
	}

	public Image createNBImage(int seuil) throws IOException {// Cree une autre image, correpondant a
		// une image en NB
		String grayscaleImagePath = null;
		BufferedImage image = this.img;

		BufferedImage grayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_RGB);// creation de l'image

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				Color c = new Color(image.getRGB(i, j));

				int r = c.getRed();

				int nb;
				if (r >= seuil) {
					nb = 255;
				} else {
					nb = 0;
				}

				Color gColor = new Color(nb, nb, nb);
				grayscaleImage.setRGB(i, j, gColor.getRGB()); // parametrer la nuance grise du pixel concerné

			}
		}

		grayscaleImagePath = this.rename("NoirEtBlanc.jpg");// creation
		// du
		// chemin
		// de l
		// image

		ImageIO.write(grayscaleImage, "jpg", new File(grayscaleImagePath));

		return new Image(grayscaleImagePath, imgName);

	}

	public Image createGrayscaleImage() throws IOException {// Cree une autre image, correpondant a
															// une image en couleur, en nuance
															// de gris
		String grayscaleImagePath = null;
		BufferedImage image = this.img;

		BufferedImage grayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_RGB);// creation de l'image

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				Color c = new Color(image.getRGB(i, j));

				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();

				int gray = (r + g + b) / 3; // creation de la nuace de gris a partir des couleur r g b de l image en
											// couleur

				Color gColor = new Color(gray, gray, gray);
				grayscaleImage.setRGB(i, j, gColor.getRGB()); // parametrer la nuance grise du pixel concerné

			}
		}

		grayscaleImagePath = this.rename("Gris.jpg");
		// du
		// chemin
		// de l
		// image

		ImageIO.write(grayscaleImage, "jpg", new File(grayscaleImagePath));

		return new Image(grayscaleImagePath, imgName);

	}

	public String getImgPath() {
		return imgPath;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public static String getImgName(String imgPath) {

		StringBuffer str = new StringBuffer(imgPath);// recuperation du nom de l image a partir de son chemin
		str.delete(0, str.lastIndexOf(File.separator) + 1);
		return str.toString();
	}

	public String getImgName() {
		return imgName;
	}

	/**
	 * methode pour enregistrer une image
	 * 
	 * 
	 */

	public void enregistreImage() {

		File f = new File(this.rename("retouche.jpg"));

		try {
			ImageIO.write(this.img, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPixelValue(int i, int j) {

		Color c = new Color(img.getRGB(i, j));

		return c.getRed();
	}

	public BufferedImage getImg() {
		return img;

	}

	public void display() {
		Util.display(img);
	}

	// tests
	public static void main(String[] args) throws IOException {
		Image img = new Image("src/ressource/escalier1.jpg", "escalier");
		img.createGrayscaleImage();
		img.createNBImage(100);

	}

	public String rename(String name) {
		String path = this.imgPath;
		String[] pathDecoupe = path.split("/");
		String result = pathDecoupe[0] + File.separator + pathDecoupe[1] + File.separator
				+ pathDecoupe[2].substring(0, pathDecoupe[2].length() - 4) + name;
		return result;

	}
}
