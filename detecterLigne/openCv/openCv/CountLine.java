package openCv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import filtre.Image;
import filtre.SobelFiltreX;
import util.Util;

/**
 * compter les lignes avec OpenCv
 * 
 *
 */
public class CountLine {

	private List<Point[]> totalLine;

	private int enlever;

	private Mat cdst;

	public List<Point[]> getTotalLine() {
		return totalLine;
	}

	public void setTotalLine(List<Point[]> totalLine) {
		this.totalLine = totalLine;
	}

	public int getEnlever() {
		return enlever;
	}

	public void setEnlever(int enlever) {
		this.enlever = enlever;
	}

	public void getLine() throws IOException {
		// déclaration des valeurs de sortie
		Mat dst = new Mat();
		cdst = new Mat();
		Mat cdstP;
		// chemin du fichier
		String default_file = "src/ressource/1.jpg";
		// chargement de l'image et conversion en noir et blanc
		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);

		// Image img = new Image("src/ressource/1.jpg", "kktest");

		// SobelFiltreX test = new SobelFiltreX(img);
		// Detection des angles avec le filtre de Canny
		// src.put(0, 0, ((DataBufferByte)
		// test.displayFiltre("").getRaster().getDataBuffer()).getData());
		Imgproc.Canny(src, dst, 50, 200, 3, false);
		// System.out.println(img.getImgName());
		// img = img.createNBImage(100);
		

		// Util.display(img.getImg());

		// Copy edges to the images that will display the results in BGR
		Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
		cdstP = cdst.clone();
		// Standard Hough Line Transform
		Mat lines = new Mat(); // will hold the results of the detection
		Imgproc.HoughLines(dst, lines, 1, Math.PI / 180, 100); // detecte les lignes//plus le dernier parametre est
																// grand moins il faut dintersection pour comtabiliser
																// une ligne
		// Dessine les lignes

		totalLine = new ArrayList<>();
		for (int x = 0; x < lines.rows(); x++) {
			// pour chaque coordonne polaire
			double rho = lines.get(x, 0)[0], theta = lines.get(x, 0)[1];

			double a = Math.cos(theta), b = Math.sin(theta);

			// coordonnees carthesien
			double x0 = a * rho, y0 = b * rho;

			// de theta a tho obtient les deux point des extrimités de la ligne
			// plus variable 1000 grande plus la ligne est longue
			Point pt1 = new Point(Math.round(x0 + 1000 * (-b)), Math.round(y0 + 1000 * (a)));
			Point pt2 = new Point(Math.round(x0 - 1000 * (-b)), Math.round(y0 - 1000 * (a)));

			/**
			 * verification ligne paralle
			 * 
			 * 
			 * 
			 */
			Point[] PointTab = new Point[2];
			PointTab[0] = pt1;
			PointTab[1] = pt2;
			System.out.println("verif " + PointTab[0].x + " ordonee " + PointTab[0].y);
			totalLine.add(PointTab);

		}
		Map<Integer, List<Point[]>> classeCoeffDir = new HashMap<>();
		double coeffDirecteurCompare = 0;
		double coeffDirecteurItre = 0;
		double pasenlever = 0;
		boolean tt = false;
		for (int i = 0; i < totalLine.size(); i++) {
			coeffDirecteurCompare = this.coeffDir(totalLine.get(i)[0], totalLine.get(i)[1]);

			coeffDirecteurItre = 0;
			if (tt) {

			}
			for (int y = 0; y < totalLine.size(); y++) {
				coeffDirecteurItre = this.coeffDir(totalLine.get(y)[0], totalLine.get(y)[1]);
				System.out.println("on a les points  " + totalLine.get(y) + " et " + totalLine.get(i));
				if (this.distanceCorrect(totalLine.get(i)[0], totalLine.get(y)[0], 1.0)) {

				} else {
					// System.out.println("on a compare " + coeffDirecteurCompare + "iteration" +
					// coeffDirecteurItre);
					if (classeCoeffDir.get((int) Math.round(coeffDirecteurItre)) != null) {
						List<Point[]> tempList = classeCoeffDir.get((int) Math.round(coeffDirecteurItre));
						tempList.add(totalLine.get(y));
						classeCoeffDir.put((int) Math.round(coeffDirecteurItre), tempList);
					} else {
						List<Point[]> tempList = new ArrayList<Point[]>();
						tempList.add(totalLine.get(y));
						classeCoeffDir.put((int) Math.round(coeffDirecteurItre), tempList);

					}

				}

				// dessine la ligne

				pasenlever++;
			}

		}
		classeCoeffDir = this.sortHashmap(classeCoeffDir);
		Iterator<Entry<Integer, List<Point[]>>> i = classeCoeffDir.entrySet().iterator();

		i.next();
		Entry<Integer, List<Point[]>> max = null;

		for (Entry<Integer, List<Point[]>> e : classeCoeffDir.entrySet()) {

			if (max == null || e.getValue().size() > max.getValue().size()) {
				max = e;
			}

		}

		List<Integer> t = new ArrayList<>(classeCoeffDir.keySet());
		Collections.sort(t);

		this.dessinerLigne(this.verifDistanceClass(this.enleveMapMin(classeCoeffDir)));

		cdst = CountLine.erode(cdst, true);
		
		
		// Show results
		// HighGui.imshow("Source", src);
		HighGui.imshow("wesh", cdst);
		// HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform",
		// cdst);
		// HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform",
		// cdstP);
		// Wait and Exit
		HighGui.waitKey();
		System.exit(0);
		totalLine.clear();

	}

	public double coeffDir(Point p1, Point p2) {
		double coeffDirecteurCompare;
		coeffDirecteurCompare = (p2.y - p1.y) / (double) (p2.x - p1.x);
		System.out.println("on a le point xa " + p1.x + " et le point ya" + p1.y);
		System.out.println("on a le point xb " + p2.x + " et le point yb" + p2.y);
		System.out.println("on a le point (p2.y - p1.y) " + (p2.y - p1.y) + " et on a finalement  "
				+ Math.abs(coeffDirecteurCompare));

		if (Double.isNaN(coeffDirecteurCompare) && Double.isInfinite(coeffDirecteurCompare)) {
			System.out.println("les points ont les memes coordonnees ou divise par zero" + "");
		}
		return Math.abs(coeffDirecteurCompare);
	}

	/**
	 * si une distance voulue entre les lignes est respectee
	 * 
	 * @return
	 */
	public boolean distanceCorrect(Point a, Point b, double distanceARespecter) {
		double distance = 0.0;
		try {
			if (a != null && b != null) {
				double xDiff = a.x - b.x;
				double yDiff = a.y - b.y;
				distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			}
		} catch (Exception e) {
			System.err.println("probleme ");
		}
		return distance <= distanceARespecter;

	}

	// erosion
	
	public static Mat erode(Mat src, boolean iserode) {
		Mat dest = new Mat();
		int elementType = Imgproc.CV_SHAPE_RECT;
		int kernelSize = 10;

		Mat element = Imgproc.getStructuringElement(elementType, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
				new Point(kernelSize, kernelSize));

		if(iserode) {
			kernelSize = 5;
			Imgproc.erode(src, dest, element);
			kernelSize = 0;
			Imgproc.dilate(src, dest, element);
		}else {
		
		}
		

		return dest;

	}
	private Map<Integer, List<Point[]>> verifDistanceClass(Map<Integer, List<Point[]>> map) {
		Map<Integer, List<Point[]>> result = new HashMap<>();

		for (Map.Entry<Integer, List<Point[]>> ligne : map.entrySet()) {

			for (int i = 0; i < ligne.getValue().size(); i++) {
				for (int y = 0; y < ligne.getValue().size(); y++) {
					if (this.distanceCorrect(ligne.getValue().get(y)[0], ligne.getValue().get(i)[0], 10.0)) {
						if (result.get((int) Math.round(ligne.getKey())) != null) {
							List<Point[]> tempList = result.get((int) Math.round(ligne.getKey()));
							tempList.add(ligne.getValue().get(y));
							result.put((int) Math.round(ligne.getKey()), tempList);

						} else {

							List<Point[]> tempList = new ArrayList<Point[]>();
							tempList.add(ligne.getValue().get(y));
							result.put((int) Math.round(ligne.getKey()), tempList);

						}
					}
				}

			}
		}

		return result;
	}

	public HashMap<Integer, List<Point[]>> sortHashmap(Map<Integer, List<Point[]>> classeCoeffDir) {

		Set<Entry<Integer, List<Point[]>>> entries = classeCoeffDir.entrySet();
		Comparator<Entry<Integer, List<Point[]>>> valueComparator = new Comparator<Entry<Integer, List<Point[]>>>() {
			@Override
			public int compare(Entry<Integer, List<Point[]>> e1, Entry<Integer, List<Point[]>> e2) {

				int v1 = e1.getValue().size();
				int v2 = e2.getValue().size();

				return v1 - v2;

			}
		}; // Sort method needs a List, so let's first convert Set to List in Java
		List<Entry<Integer, List<Point[]>>> listOfEntries = new ArrayList<Entry<Integer, List<Point[]>>>(entries); // sorting
																													// HashMap
																													// by
		// values using
		// comparator
		Collections.sort(listOfEntries, valueComparator);
		LinkedHashMap<Integer, List<Point[]>> sortedByValue = new LinkedHashMap<Integer, List<Point[]>>(
				listOfEntries.size()); // copying
		// entries
		// from
		// List
		// to
		// Map
		for (Entry<Integer, List<Point[]>> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		return sortedByValue;

	}

	public HashMap<Integer, List<Point[]>> unsortHashmap(Map<Integer, List<Point[]>> classeCoeffDir) {

		Set<Entry<Integer, List<Point[]>>> entries = classeCoeffDir.entrySet();
		Comparator<Entry<Integer, List<Point[]>>> valueComparator = new Comparator<Entry<Integer, List<Point[]>>>() {
			@Override
			public int compare(Entry<Integer, List<Point[]>> e1, Entry<Integer, List<Point[]>> e2) {

				int v1 = e1.getValue().size();
				int v2 = e2.getValue().size();

				return v2 - v1;

			}
		}; // Sort method needs a List, so let's first convert Set to List in Java
		List<Entry<Integer, List<Point[]>>> listOfEntries = new ArrayList<Entry<Integer, List<Point[]>>>(entries); // sorting
																													// HashMap
																													// by
		// values using
		// comparator
		Collections.sort(listOfEntries, valueComparator);
		LinkedHashMap<Integer, List<Point[]>> sortedByValue = new LinkedHashMap<Integer, List<Point[]>>(
				listOfEntries.size()); // copying
		// entries
		// from
		// List
		// to
		// Map
		for (Entry<Integer, List<Point[]>> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		return sortedByValue;

	}

	public List<List<Point[]>> classeMoyenne(Map<Integer, Point[]> data, double precision) {
		Map<Integer, Point[]> result = new HashMap<Integer, Point[]>();
		return null;

	}

	public void dessinerLigne(Map<Integer, List<Point[]>> map) {
		List<List<Point[]>> tempList = this.mapToList(map);
		for (int i = 0; i < tempList.size(); i++) {

			for (int y = 0; y < tempList.get(i).size(); y++) {

				Imgproc.line(cdst, tempList.get(i).get(y)[0], tempList.get(i).get(y)[1], new Scalar(0, 0, 255), 3,
						Imgproc.LINE_AA, 0);

			}

		}

	}

	public List<List<Point[]>> mapToList(Map<Integer, List<Point[]>> data) {
		List<List<Point[]>> result = new ArrayList<>();
		for (Map.Entry<Integer, List<Point[]>> e : data.entrySet()) {
			result.add(e.getValue());
		}
		return result;
	}

	public Map<Integer, List<Point[]>> enleveMapMin(Map<Integer, List<Point[]>> data) {
		Map<Integer, List<Point[]>> result = new HashMap<Integer, List<Point[]>>();

		data = this.unsortHashmap(data);
		int count = 0;
		for (Map.Entry<Integer, List<Point[]>> ligne : data.entrySet()) {
			count++;

			if (ligne.getKey() == -59) {
				System.out.println("on a le nombre " + ligne.getKey() + "avec " + ligne.getValue().size());
				result.put(ligne.getKey(), ligne.getValue());
			} else {
				result.put(ligne.getKey(), ligne.getValue());
			}

			if (count == 3) {
				break;
			}
		}

		return result;
	}

	public static void main(String[] args) throws IOException {
		CountLine test = new CountLine();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		test.getLine();

		// Point a = new Point(1,3);
		// Point b = new Point(3,1);

		// System.out.println("on a a et b avec aX = " + a.x + " aY " + a.y + " et bX "
		// + b.x + " et b " + b.y
		// + " finalement " + test.coeffDir(a, b));

	}

}
