package config;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * classe mere des configs possibles
 * 
 * @author Axel
 *
 */
public abstract class Config {

	private List<String> listImgPath = new ArrayList<>();

	private List<Integer> resultatObtenu = new ArrayList<Integer>();

	private List<Image> transfoImgList = new ArrayList<>();

	public List<Integer> getResultatObtenu() {
		return resultatObtenu;
	}

	public void setResultatObtenu(List<Integer> resultatObtenu) {
		this.resultatObtenu = resultatObtenu;
	}

	public List<Image> getTransfoImgList() {
		return transfoImgList;
	}

	public void setTransfoImgList(List<Image> transfoImgList) {
		this.transfoImgList = transfoImgList;
	}

	public Config(List<String> path) {
		this.listImgPath = path;
	}

	public List<String> getListImgPath() {
		return listImgPath;
	}

	public void setListImgPath(List<String> listImgPath) {
		this.listImgPath = listImgPath;
	}
	/**
	 * methode execution de la configuration
	 * @throws IOException
	 */
	public abstract void display() throws IOException;

}
