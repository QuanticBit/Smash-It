package org.smash;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.xor.utils.FileTools;
/**Represent a smash Map object
 * 
 * @author LordOfBees
 *
 */
public class Map {
	/**
	 * The constant that represents the width in cells of a map
	 */
	public static final int width = 30;
	/**
	 * The constant that represents the height in cells of a map
	 */
	public static final int height = 15;
	/**
	 * The constant that represents the width and the height of one cell of a map
	 */
	public static final int size = 60;
	/**
	 * Represents an empty cell where you can move
	 */
	public static final int CELL_FREE = 0;
	/**
	 * Represent a ground/wall cell where you can't move
	 */
	public static final int CELL_OBSTACLE = 1;
	/**
	 * Represent an empty cell but where sometimes bonus can spawn in
	 */
	public static final int CELL_BONUS = 2;
	/**
	 * Represent a health bonus that will heal the grabber of 15% of his life
	 */
	public static final int BONUS_HEALTH = 1;
	/**
	 * Represent a bonus that when grabbed will change the grabber weapon as the simple kick
	 */
	public static final int BONUS_WEAP_1 = 2;
	/**
	 * Represent a bonus that when grabbed will change the grabber weapon as a sword
	 */
	public static final int BONUS_WEAP_2 = 3;
	/**
	 * Represent a bonus that when grabbed will change the grabber weapon as a hammer
	 */
	public static final int BONUS_WEAP_3 = 4;
	/**
	 * Represent a bonus that when grabbed will change the grabber weapon as a bomb
	 */
	public static final int BONUS_WEAP_4 = 5;
	/**
	 * Represent a bonus that when grabbed will give you one call to the super power
	 */
	public static final int BONUS_POWER = 6;
	/**
	 * The array that contains all the cells data about the map
	 */
	private int[][] map = new int[width][height];
	private String name = "New Map", comments = "Made by ????";
	/**
	 * The background image that will be drawn as a graphic representation of the map
	 */
	private BufferedImage background;
	/**
	 * The file where the map should be loaded/saved
	 */
	private File path;
	/**Creates a new empty map with the given name
	 * 
	 * @param name is the name of the map
	 */
	public Map(String name){
		setName(name);
	}
	/**Load a map from the given file
	 * 
	 * @param path is the file from where it loads the map
	 */
	public Map(File path){
		setPath(path);
		load();
	}
	/**Set the cell in the specified coordinates to the given type
	 * 
	 * @param caseType is the new type of the case
	 * @param x is the horizontal coordinates
	 * @param y is the vertical coordinates
	 */
	public void setCase(int caseType, int x, int y){
		this.map[x][y]=caseType;
	}
	/**Get whether there's a bonus or not and what type there's on the cell in the specified coordinates
	 * 
	 * @param x is the horizontal coordinates
	 * @param y is the vertical coordinates
	 * @return 0 if it's an empty bonus case, a negative value if it's not, and a positive one matching the BONUS fields if there is one
	 */
	public int getBonus(int x, int y){
		return this.map[x][y]-2;
	}
	/**
	 * Randomly spawn a random bonus in a random bonus cell between the one available
	 */
	public void spawnBonus(){
		int[][] bonusCoords = new int[100][2];
		int index = 0;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int type = this.map[x][y];
				if(type == CELL_BONUS){
					bonusCoords[index][0]=x;
					bonusCoords[index][1]=y;
					index++;
				}
			}
		}
		int j = (int) (Math.random()*index);
		int bonus = (int) (Math.random()*(BONUS_POWER-BONUS_HEALTH))+BONUS_HEALTH+CELL_BONUS;
		setCase(bonus, bonusCoords[j][0], bonusCoords[j][1]);
	}
	/**
	 * Remove all the bonus currently spawned
	 */
	public void clearBonus(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int type = this.map[x][y];
				setCase(type > CELL_BONUS ? CELL_BONUS : type, x, y);
			}
		}
	}
	/**Get the case type of this cell
	 * 
	 * @param x is the horizontal coordinates
	 * @param y is the vertical coordinates
	 * @return <ul><li>{@link #CELL_FREE} or<li>{@link #CELL_OBSTACLE} or<li>{@link #CELL_BONUS}</ul>
	 */
	public int getCell(int x, int y){
		return this.map[x][y];
	}
	/**Get the raw case means you'll just know what type of case is it but not which bonus or whatever
	 * 
	 * @param x is the horizontal coordinates
	 * @param y is the vertical coordinates
	 * @return <ul><li>{@link #CELL_FREE} or<li>{@link #CELL_OBSTACLE} or<li>{@link #CELL_BONUS}</ul>
	 */
	public int getRawCell(int x, int y){
		int type = this.map[x][y];
		return type > CELL_BONUS ? CELL_BONUS : type;
	}
	/**Get whether or not you can walk in the specified cell
	 * 
	 * @param x is the horizontal coordinates
	 * @param y is the vertical coordinates
	 * @return if the cell is not an obstacle
	 */
	public boolean isFree(int x, int y){
		return this.map[x][y]!= CELL_OBSTACLE;
	}
	/**Get the background image that will be drawn as a graphic representation of the map
	 * 
	 * @return a buffered image background representation of this map
	 */
	public BufferedImage getBackground() {
		return background;
	}
	/**Set the background image that will be drawn as a graphic representation of the map
	 * 
	 * @param background is the new background image
	 */
	public void setBackground(BufferedImage background) {
		this.background = background;
	}
	/**Get the name of this map
	 * 
	 * @return the name of this map
	 */
	public String getName() {
		return name;
	}
	/**Set the name of this map
	 * 
	 * @param name is the name to set to
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**Get the comments about this map
	 * 
	 * @return the comments about this map
	 */
	public String getComments() {
		return comments;
	}
	/**Set the comments about this map
	 * 
	 * @param comments are the comments to set to
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * Get The file where the map should be loaded/saved
	 * @return the file where this map is loaded/saved ; can be null if non set
	 */
	public File getPath() {
		return path;
	}
	/**
	 * Set The file where the map should be loaded/saved
	 * @param path is the file to set to
	 */
	public void setPath(File path) {
		this.path = path;
	}
	/**
	 * Load the map contained in the {@link #path} file
	 */
	public void load(){
		if(!path.exists())
			return;
		try {
			//We load our archive and its entries
			ZipFile zipFile = new ZipFile(path);
			Enumeration<? extends ZipEntry> files = zipFile.entries();

			while (files.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) files.nextElement();
				
				// if the entry is our background image then we load it
				if (!entry.isDirectory() && entry.getName().endsWith("background.png")) {
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					// Read the image
					background = ImageIO.read(bis);
				}
				// if the entry is our properties file the we load it
				else if(!entry.isDirectory() && entry.getName().endsWith("properties.prop")){
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					//Read the properties
					Properties prop = new Properties();
					prop.load(bis);
					setName(prop.getProperty("name", "Map"));
					setComments(prop.getProperty("comments", "A awesome map ! By ???"));
					for(int x = 0; x < width; x++){
						for(int y = 0; y < height; y++){
							int type = Integer.parseInt(prop.getProperty(x+"."+y, ""+CELL_FREE));
							setCase(type, x, y);
						}
					}
				} else {
					continue;
				}
				//We close our archive file
				zipFile.close();
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Save this map to the {@link #path} file
	 */
	public void save(){
		if(!path.exists())
			try {
				path.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		//We create the temporary fies
		File pic = new File(path.getParent()+"/background.png");
		if(!pic.exists())
			try {
				pic.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		File properties = new File(path.getParent()+"/properties.prop");
		if(!properties.exists())
			try {
				properties.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		//We store all the map data in this properties
		Properties prop = new Properties();
		prop.put("name", name);
		prop.put("comments", comments);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(getRawCell(x, y)==CELL_FREE)
					continue;
				prop.put(x+"."+y, ""+getRawCell(x, y));
			}
		}
		try {
			//First we write down the files
			ImageIO.write(background, "png", pic);
			prop.store(new FileWriter(properties), name+" map properties file for Smash It");
			//Now we create the archive
			FileOutputStream fos = new FileOutputStream(path);
			ZipOutputStream zos = new ZipOutputStream(fos);
			zos.setLevel(ZipOutputStream.STORED);
			//We add the files
			FileTools.addToZipFile(pic, zos);
			FileTools.addToZipFile(properties, zos);
			//We now close it
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Now we delete or temporary files
		pic.delete();
		properties.delete();
	}
}
