package filtre;

import java.awt.image.BufferedImage;

/*
 * classe qui décrit le filtre Sobel horizontal
 */
public class SobelFiltreY extends Filtre{

	public SobelFiltreY(Image img) {
		super(img);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int filtreFenetre() {
		
		return 0;
	}

	@Override
	public BufferedImage displayFiltre(String conversionChoix) {
		// TODO Auto-generated method stub
		return null;
	}



}
