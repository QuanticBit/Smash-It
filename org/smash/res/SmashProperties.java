package org.smash.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SmashProperties {
	
	private static final String fileName = "./properties.prop";
	//USER INFO
	private String username = "User";
	private String skin = "./skins/default.sskin";
	private int xp = 0;
	//CONTROLS
	private int leftCtrl = 37;
	private int rightCtrl = 39;
	private int upCtrl = 38;
	private int shootCtrl = 32;
	private int powerCtrl = 18;
	//STATIC INFOS
	private static final String version = "0.0.717";
	

	public SmashProperties(){
		File file = new File(fileName);
		if(!file.exists())
			return;
		load();
	}
	
	public void load(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username = prop.getProperty("username", "User");
		this.xp = Integer.parseInt(prop.getProperty("xp","0"));
		this.skin = prop.getProperty("skin", "./skins/default.sskin");
		
		this.upCtrl = Integer.parseInt(prop.getProperty("ctrl.up", "38"));
		this.rightCtrl = Integer.parseInt(prop.getProperty("ctrl.right", "39"));
		this.leftCtrl = Integer.parseInt(prop.getProperty("ctrl.left", "37"));
		this.shootCtrl = Integer.parseInt(prop.getProperty("ctrl.shoot", "32"));
		this.powerCtrl = Integer.parseInt(prop.getProperty("ctrl.power", "18"));
	}
	
	public void save(){
		File file = new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prop.put("username", username);
		prop.put("xp", ""+xp);
		prop.put("skin", skin);
		prop.put("ctrl.up", ""+upCtrl);
		prop.put("ctrl.right", ""+rightCtrl);
		prop.put("ctrl.left", ""+leftCtrl);
		prop.put("ctrl.shoot", ""+shootCtrl);
		prop.put("ctrl.power", ""+powerCtrl);
		prop.put("version", version);
		try {
			prop.store(new FileOutputStream(file), "Smash It : Properties File");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}
	/**Gain this amount of experience
	 * 
	 * @param amount is the amount of experience gained
	 * @return is the player has level up
	 */
	public boolean gainXp(int amount){
		int before = getLevel();
		setXp(getXp()+amount);
		return before!=getLevel();
	}
	
	public int getLevel(){
		int lvl = 1;
		int xpLeft = xp;
		for(;lvl*100<xpLeft;lvl++){
			xpLeft-=lvl*100;
		}
		return lvl;
	}
	
	public int getXPLeft(){
		int lvl = 1;
		int xpLeft = xp;
		for(;lvl*100<xpLeft;lvl++){
			xpLeft-=lvl*100;
		}
		return xpLeft;
	}
	
	public int getXpPercent() {
		return (getXPLeft()*100)/(getLevel()*100);
	}

	public int getLeftCtrl() {
		return leftCtrl;
	}

	public void setLeftCtrl(int leftCtrl) {
		this.leftCtrl = leftCtrl;
	}

	public int getRightCtrl() {
		return rightCtrl;
	}

	public void setRightCtrl(int rightCtrl) {
		this.rightCtrl = rightCtrl;
	}

	public int getUpCtrl() {
		return upCtrl;
	}

	public void setUpCtrl(int upCtrl) {
		this.upCtrl = upCtrl;
	}

	public int getShootCtrl() {
		return shootCtrl;
	}

	public void setShootCtrl(int shootCtrl) {
		this.shootCtrl = shootCtrl;
	}

	public int getPowerCtrl() {
		return powerCtrl;
	}

	public void setPowerCtrl(int powerCtrl) {
		this.powerCtrl = powerCtrl;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public static String getVersion() {
		return version;
	}

 
}