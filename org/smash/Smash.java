package org.smash;

import javax.swing.JDialog;

import org.smash.res.RessourcesCreator;
import org.smash.res.SmashProperties;
import org.smash.ui.SFrame;
import org.smash.ui.StartDialog;
import static org.smash.res.RessourcesCreator.*;

public class Smash {

	public static void main(String[] args) {
		//First we create the required resources
		RessourcesCreator.updateFolders();
		//We wait for their creation as the compiler would rather start the game
		while(!MAP_FOLDER.exists() || !SKIN_FOLDER.exists() || !TEMP_FOLDER.exists())
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		//Now we start the game
		SmashProperties properties = new SmashProperties();
		//If it's the first launch we prompt is its username
		if(properties.getUsername().equalsIgnoreCase("User")){
			StartDialog dialog = new StartDialog(properties);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}else{
			SFrame frame = new SFrame(properties);
			frame.setVisible(true);
		}
	}

}
