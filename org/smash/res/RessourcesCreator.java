package org.smash.res;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import org.xor.utils.FileTools;
import org.xor.utils.GraphicsConfiguration;
/**A class that creates all the required folders, load the graphics configuration and register the font
 * 
 * @author LordOfBees
 *
 */
public final class RessourcesCreator {
	
	public static final String MAP_FOLDER_PATH = "./maps/";
	public static final String SKIN_FOLDER_PATH = "./skins/";
	public static final String TEMP_FOLDER_PATH = "./temp/";
	
	public static final File MAP_FOLDER = new File("./maps/");
	public static final File SKIN_FOLDER = new File("./skins/");
	public static final File TEMP_FOLDER = new File("./temp/");
	/**
	 * Creates all the required folders, load the graphics configuration and register the font
	 */
	public static void updateFolders(){
		try {
			registerFont();
			GraphicsConfiguration.load();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!MAP_FOLDER.exists()){
			MAP_FOLDER.mkdir();
			FileTools.extract(new File(MAP_FOLDER_PATH+"default.smap"), "default.smap");
		}
		if(!SKIN_FOLDER.exists()){
			SKIN_FOLDER.mkdir();
			FileTools.extract(new File(SKIN_FOLDER_PATH+"default.sskin"), "default.sskin");
		}
		if(!TEMP_FOLDER.exists()){
			TEMP_FOLDER.mkdir();
		}else{
			for( File file : TEMP_FOLDER.listFiles()){
				file.delete();
			}
		}
	}
	
	public static void registerFont() throws FontFormatException, IOException{
		Font g = new Font("pixelpunch", Font.PLAIN, 18);
		if(g.getFontName().equals("pixelpunch"))
			return;
		Font f = Font.createFont(Font.TRUETYPE_FONT, RessourcesCreator.class.getResourceAsStream("/res/PIXELPUN.TTF"));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //on l'ajoute a l'envirronement
        ge.registerFont(f);
	}

}
