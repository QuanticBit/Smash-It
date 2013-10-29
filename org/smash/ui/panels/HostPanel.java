package org.smash.ui.panels;

import java.awt.Graphics;
import java.io.File;

import javax.swing.JFileChooser;

import org.smash.Map;
import org.smash.SmashGame;
import org.smash.network.host.SmashServer;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIMapPreview;
import org.smash.utils.MapFilter;

import static org.smash.res.RessourcesCreator.MAP_FOLDER_PATH;

public class HostPanel extends InteractivePanel {
	private static final long serialVersionUID = 2947037307373134067L;
	
	private SFrame frame;
	
	private SUIButton cancel;
	private SUIButton host;
	private SUIButton map_set;
	
	private SUIMapPreview preview;
	
	private Map map;

	public HostPanel(SFrame frame) {
		super();
		this.frame = frame;
		map = new Map(new File(MAP_FOLDER_PATH+"default.smap"));
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(cancel==null)
			init(g);
		super.paintComponent(g);
		repaint(50);
	}

	private void init(Graphics g) {
		preview = new SUIMapPreview((getWidth()-getHeight())/2, getHeight()/6, getHeight(), getHeight()/2, map);
		host = new SUIButton("Host", 2*getWidth()/4-g.getFontMetrics().stringWidth("Host")/2, 9*(getHeight()/10));
		cancel = new SUIButton("Cancel", 3*getWidth()/4-g.getFontMetrics().stringWidth("Cancel")/2, 9*(getHeight()/10));
		map_set = new SUIButton("Set Map", getWidth()/4-g.getFontMetrics().stringWidth("Set Map")/2, 9*(getHeight()/10));
		addComponent(cancel);
		addComponent(host);
		addComponent(map_set);
		addComponent(preview);
	}

	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(cancel.equals(concerned))
			frame.goTo(new PlayConfigPanel(frame));
		else if(host.equals(concerned))
			frame.goTo(new HostPlayPanel( new SmashServer( new SmashGame(frame.getProperties()), map), frame));
		else if(map_set.equals(concerned)){
			frame.setAlwaysOnTop(false);
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new MapFilter());
			fc.setAcceptAllFileFilterUsed(false);
			if(map.getPath()!=null)
				fc.setCurrentDirectory(map.getPath().getParentFile());
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            map = new Map(file);
	            preview.setMap(map);
	        }
			frame.setAlwaysOnTop(true);
		}
		
	}
}
