package util;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.sun.javafx.scene.traversal.Hueristic2D;

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

	public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);
		byteArrayOutputStream.flush();
		return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_ANYCOLOR);
	}

	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String default_file = "src/ressource/2.jpg";
		Mat src = Imgcodecs.imread(default_file);
		BufferedImage b = Util.resizeMat2B(src, 1000, 1500);
		
		Util.enregistreImage(b);


	}

	/**
	 * methode pour enregistrer une image
	 */
	public static String enregistreImage(BufferedImage b) {

		String path = System.getProperty("user.dir") + File.separator + "retouche";
		String name = "retouche" + Math.random() + ".jpg";
		System.out.println(rename(name, path));
		File f = new File(rename(name, path));

		try {
			ImageIO.write(b, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return System.getProperty("user.dir") + File.separator + name;
	}
	
	public static int averageHisto(int[] histo) {
		long deno = 0;
		long numo = 0;
		for(int i = 0; i < histo.length; i++) {
			numo += (i*histo[i]);
			deno +=histo[i];
		}
		
		return (int) (numo/deno);
		
		
	}
	
	public static int medianHisto(filtre.Image src) {
		
		long stop = (src.getImg().getHeight()*src.getImg().getWidth())/2;
		
		long count = 0;
		for(int i = 0; i < src.createHisto().length; i++) {
			count+= src.createHisto()[i];
			if(stop < count) {
				
				count = i;
				break;
			}
		}
		
		return (int) count;
		
		
	}

	/**
	 * meethode qui indique le nombre de lignes blanches rencontres
	 * 
	 * @param src la matrice
	 * @return le nombre de lignes
	 */
	public static int countLine(Mat src) {

		Boolean white = false;
		int count = 0;
		// traverse l axe y
		for (int i = 0; i < src.rows(); i++) {

			double[] data = src.get(i, 150);

			// si couleur blanche indiquer
			if (data[0] == 255 && data[0] == data[2] && data[0] == data[1]) {

				white = true;

			} else {
				// si couleur non blanche incrementer la ligne et continuer
				if (white) {
					count++;
					white = false;
				}
			}

		}

		return count;

	}

	/**
	 * methode pour enregistrer une image sans random
	 */
	public static String enregistreImage(BufferedImage b, String path) {

		File f = new File(path);

		try {
			ImageIO.write(b, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;
	}

	/**
	 * methode qui retourne le rectangle avec la plsu grosse aire
	 * 
	 * @param data un tableau de rectangles
	 * @return le plus grand rectangle
	 */
	public static Rect maxRectArea(Rect[] data) {
		Rect max = data[0];
		for (int i = 1; i < data.length; i++) {
			if (data[i].area() > max.area()) {
				max = data[i];
			}
		}
		return max;
	}
	
public static Mat enleveNoir(Mat src) {
		
		Mat srcT = src;

		for (int i = 0; i < srcT.rows(); i++) {
			for (int j = 0; j < srcT.cols(); j++) {

				double[] data = srcT.get(i, j);

				
				if (data[2] == 255) {
					if (data[1] == 255) {
						data[0] = 0;
						data[1] = 0;
						data[2] = 0;
					} else {
						data[0] = 255;
						data[1] = 255;
						data[2] = 255;
					}

				} else {
					data[0] = 0;
					data[1] = 0;
					data[2] = 0;

				}

				srcT.put(i, j, data);

			}
		}
		return srcT;
	}

	public static String rename(String name, String path) {

		String[] pathDecoupe = path.split(File.separator + File.separator);

		String result = "";
		for (int i = 0; i < pathDecoupe.length - 1; i++) {
			result += pathDecoupe[i] + File.separator;

		}
		result += name;

		return result;

	}

	public static List<Integer> readeDataFile(String path) {

		List<Integer> result = new ArrayList<Integer>();
		try (FileReader fr = new FileReader(path); BufferedReader br = new BufferedReader(fr)) {
			String ligne = null;

			while ((ligne = br.readLine()) != null) {
				result.add(Integer.parseInt(ligne));
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return result;
	}

	public static int[] matriceData(List<Integer> l1, List<Integer> l2) {
		int bon = 0;
		int pas_bon = 0;

		// tri
		Collections.sort(l1);
		Collections.sort(l2);
		if (l1.size() == l2.size()) {
			for (int i = 0; i < l1.size(); i++) {
				for (int y = 0; y < l1.size(); y++) {
					if (l1.get(i) == l2.get(y)) {
						bon++;
						break;
					}
				}
			}
		}
		pas_bon = l1.size() - bon;
		int[] result = { bon, pas_bon };

		return result;

	}

	/**
	 * permet de redimentionner des images
	 * 
	 * @param src image source
	 * @param h   longueur
	 * @param w   largeur
	 * @return l eimager redimentionne
	 */
	public static Mat resize(Mat src, int h, int w) {

		Mat resizeimage = new Mat();
		Size sz = new Size(h, w);
		Imgproc.resize(src, resizeimage, sz);
		return resizeimage;

	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
	
	public static BufferedImage resizeMat2B(Mat img, int newW, int newH) {
		double percent_scale;
		System.out.println(img.height());
		if(img.height()>=newH) {
			percent_scale =(double) img.height()/(double)newH;
			System.out.println(percent_scale);
				 Imgproc.resize(img, img, new Size(img.rows()*percent_scale, img.rows()*percent_scale), 0, 0, 
				         Imgproc.INTER_AREA);

		}
		
		return (BufferedImage) HighGui.toBufferedImage(img);
	}

	public static Mat rogne(Mat src, int x, int y, int h, int w) {

		Rect roi = new Rect(x, y, w, h);
		Mat cropped = new Mat(src, roi);
		return cropped;
	}

	public static Mat img2Mat(BufferedImage in) {
		Mat out;
		byte[] data;
		int r, g, b;

		if (in.getType() == BufferedImage.TYPE_INT_RGB) {
			out = new Mat(240, 320, CvType.CV_8UC3);
			data = new byte[320 * 240 * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, 320, 240, null, 0, 320);
			for (int i = 0; i < dataBuff.length; i++) {
				data[i * 3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
				data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
				data[i * 3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
			}
		} else {
			out = new Mat(240, 320, CvType.CV_8UC1);
			data = new byte[320 * 240 * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, 320, 240, null, 0, 320);
			for (int i = 0; i < dataBuff.length; i++) {
				r = (byte) ((dataBuff[i] >> 16) & 0xFF);
				g = (byte) ((dataBuff[i] >> 8) & 0xFF);
				b = (byte) ((dataBuff[i] >> 0) & 0xFF);
				data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b)); // luminosity
			}
		}
		out.put(0, 0, data);
		return out;
	}

}
