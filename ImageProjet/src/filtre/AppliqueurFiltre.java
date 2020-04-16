package filtre;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * classe principale que analyse les pixels de l'images selon les filtres à appliquer
 */
public class AppliqueurFiltre {
	private BufferedImage img;

	/*
	 * constructeur
	 * 
	 * @param path de l'image
	 */
	public AppliqueurFiltre(String path) throws IOException {
		img = ImageIO.read(new File(path));
	}

	/*
	 * methode pour afficher l'image
	 */
	public void display() {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(this.img)));

		frame.pack();
		frame.setVisible(true);
	}

	//tests
	public static void main(String[] args) {
		try {
			AppliqueurFiltre test = new AppliqueurFiltre("src/filtre/escalier.jpg");
			test.display();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
