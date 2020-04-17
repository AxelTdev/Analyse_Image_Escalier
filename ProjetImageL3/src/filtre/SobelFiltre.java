package filtre;

import java.awt.image.BufferedImage;
import java.io.IOException;

import util.Util;

/**
 * 
 * classe qui décrit le filtre Sobel vertical
 *
 */
public class SobelFiltre extends Filtre {

	public SobelFiltre(Image src) {
		super(src);
	}

	@Override
	public BufferedImage displayFiltre() {

		BufferedImage br = null;
		try {
			// image en noir
			br = super.getSrcImage().createNBImage(100).getImg();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		int pixelValue;
		for (int i = 1; i < br.getWidth() - 1; i++) {
			for (int j = 1; j < br.getHeight() - 1; j++) {

				super.fenetre(i, j); // remplissage des voisins
				pixelValue = this.filtreFenetre();
				br.setRGB(i, j, pixelValue);
			}
		}
		return br;
	}

	@Override
	public int filtreFenetre() {
		int pixelFiltre = super.getHg() * (-1) + super.getHc() * (0) + super.getHd() * (1)
				+ super.getCg() * (-2) * super.getCc() * (0) + super.getCd() * (2) + super.getBg() * (-1)
				+ super.getBc() * (0) + super.getBd() * (1);
		return pixelFiltre;
	}

	public static void main(String[] args) throws IOException {
		Image img = new Image("src/ressource/escalier6.jpg", "test");
		
		SobelFiltre test = new SobelFiltre(img);
		Util.display(test.displayFiltre());

	}

}
