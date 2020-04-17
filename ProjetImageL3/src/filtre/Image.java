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

	public static Image createGrayscaleImage(String imgPath) throws IOException {// Cree une autre image, correpondant a
																					// une image en couleur, en nuance
																					// de gris
		String grayscaleImagePath = null;
		BufferedImage image = null;
		File o = new File(imgPath);

		image = (ImageIO.read(o));

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

		grayscaleImagePath = "ressources" + File.separator + "images" + File.separator + getImgName(imgPath);// creation
																												// du
																												// chemin
																												// de l
																												// image
																												// ressources\images\nomDeLimageEnCouleur.png
																												// (la
																												// difference
																												// de
																												// chemin
																												// se
																												// fait
																												// sur
																												// leur
																												// chemin
																												// absolue
																												// toutes
																												// les
																												// images
																												// en
																												// gris
																												// se
																												// trouve
																												// dans
																												// le
																												// repertoire
																												// images
																												// du
																												// projet)

		ImageIO.write(grayscaleImage, "png", new File(grayscaleImagePath));

		return new Image(grayscaleImagePath, imgPath);

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

	public int getPixelValue(int i, int j) {

		Color c = new Color(img.getRGB(i, j));

		return c.getRed();
	}

	public BufferedImage getImg() {
		return img;
		
	}

	/*
	 * methode pour afficher l'image
	 */
	public void display() {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));

		frame.pack();
		frame.setVisible(true);

	}

	// tests
	public static void main(String[] args) throws IOException {
		Image img = new Image("src/filtre/escalier.jpg", "test");
		System.out.println(Arrays.toString(img.createHisto()));
		DisplayStaskChart.main(args);

	}
}
