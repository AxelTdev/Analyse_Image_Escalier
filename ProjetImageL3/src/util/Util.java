package util;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * ensemble de methodes utilitaires
 */
public class Util {
	

	/*
	 * methode pour afficher l'image
	 */
	public static void display(BufferedImage img) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));

		frame.pack();
		frame.setVisible(true);

	}
}
