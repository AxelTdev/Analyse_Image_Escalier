package openCv;

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
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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

	public void getLine() {
		// déclaration des valeurs de sortie
		Mat dst = new Mat();
		cdst = new Mat();
		Mat cdstP;
		// chemin du fichier
		String default_file = "C:\\Users\\Axel\\eclipse-workspace\\Image\\src\\ressource\\escalier1.jpg";
		// chargement de l'image et conversion en noir et blanc
		Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_GRAYSCALE);

		// Detection des angles avec le filtre de Canny
		Imgproc.Canny(src, dst, 50, 200, 3, false);

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
			totalLine.add(PointTab);

		}
		Map<Integer, List<Point[]>> classeCoeffDir = new HashMap<>();
		double coeffDirecteurCompare = 0;
		double coeffDirecteurItre = 0;
		double pasenlever = 0;
		for (int i = 0; i < totalLine.size(); i++) {
			coeffDirecteurCompare = this.coeffDir(totalLine.get(i)[0], totalLine.get(i)[1]);

			coeffDirecteurItre = 0;
			for (int y = 0; y < totalLine.size(); y++) {
				coeffDirecteurItre = this.coeffDir(totalLine.get(y)[0], totalLine.get(y)[1]);

				if (Math.abs(coeffDirecteurItre - coeffDirecteurCompare) < 0.000001 && coeffDirecteurItre > 0) {
					System.out.println("on a compare " + coeffDirecteurCompare + "iteration" + coeffDirecteurItre);
					if (classeCoeffDir.get((int) Math.round(coeffDirecteurItre)) != null) {
						List<Point[]> tempList = classeCoeffDir.get((int) Math.round(coeffDirecteurItre));
						tempList.add(totalLine.get(y));
						classeCoeffDir.put((int) Math.round(coeffDirecteurItre), tempList);
					} else {
						List<Point[]> tempList = new ArrayList<Point[]>();
						tempList.add(totalLine.get(y));
						classeCoeffDir.put((int) Math.round(coeffDirecteurItre), tempList);

					}

					// dessine la ligne

					pasenlever++;
				} else {
					enlever++;
				}
			}
		}
		classeCoeffDir = this.sortHashmap(classeCoeffDir, 0.1);
		Iterator<Entry<Integer, List<Point[]>>> i = classeCoeffDir.entrySet().iterator();

		i.next();
		Entry<Integer, List<Point[]>> max = null;

		for (Entry<Integer, List<Point[]>> e : classeCoeffDir.entrySet()) {

			if (max == null || e.getValue().size() > max.getValue().size()) {
				max = e;
			}
			System.out.println(e.getValue().size());
		}

		List<Integer> t = new ArrayList<>(classeCoeffDir.keySet());
		Collections.sort(t);
		System.out.println(max.getValue().size() + "dd " + max.getKey());
		System.out.println(t + "ss");
		System.out.println(this.getEnlever());
		System.out.println(this.getTotalLine().size());
		System.out.println(this.enleveMapMin(classeCoeffDir).size());
		this.dessinerLigne(this.enleveMapMin(classeCoeffDir));

		// Show results
		// :HighGui.imshow("Source", src);
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
		coeffDirecteurCompare = (p2.x - p1.x) / (double) (p2.y - p1.y);

		return coeffDirecteurCompare;
	}

	public HashMap<Integer, List<Point[]>> sortHashmap(Map<Integer, List<Point[]>> classeCoeffDir, double precision) {

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
			
			if(ligne.getKey()==1) {
				System.out.println(ligne.getKey() + "ee" + ligne.getValue().size());
			}else {
				result.put(ligne.getKey(), ligne.getValue());
			}
			
			if (count ==3) {
				break;
			}
		}

		return result;
	}

	public static void main(String[] args) {
		CountLine test = new CountLine();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		test.getLine();

	}

}
