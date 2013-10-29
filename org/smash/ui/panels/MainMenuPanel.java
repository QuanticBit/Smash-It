package org.smash.ui.panels;

import java.awt.Font;
import java.awt.Graphics;
import org.smash.res.SmashProperties;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIText;
import org.xor.utils.GraphicsTools;

public class MainMenuPanel extends InteractivePanel{
	private static final long serialVersionUID = 4597851362706376317L;
	private static final Font normal = new Font("Simplified Arabic Fixed", Font.BOLD, 30);
	private SUIButton play, skin_editor, map_editor, settings; 
	private SUIText version;

	private SFrame frame;
	
	public MainMenuPanel(SFrame frame){
		super();
		this.frame=frame;
		this.frame.setTitle("Smash It - "+this.frame.getProperties().getUsername());
		setBackground(GraphicsTools.loadImageFromResource("main_background.png"));
	}
	@Override
	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(concerned == null)
			return;
		if(play.equals(concerned)){
			frame.goTo(new PlayConfigPanel(frame));
		}
		else if(skin_editor.equals(concerned)){
			frame.goTo(new SkinEditorPanel(frame));
		}
		else if(map_editor.equals(concerned)){
			frame.goTo(new MapEditorPanel(frame));
		}
		else if(settings.equals(concerned)){
			frame.goTo(new SettingsPanel(frame));
		}
	}
		
	@Override
	public void paintComponent(Graphics g){
		if(play==null)
			init(g);
		super.paintComponent(g);
		repaint(50);
	}
	
	public void init(Graphics g){
		g.setFont(normal);
		version = new SUIText("Version "+SmashProperties.getVersion(), 0, getHeight()-g.getFontMetrics().getHeight());
		play = new SUIButton("PLAY", getWidth()/5-g.getFontMetrics().stringWidth("PLAY")/2, 6*getHeight()/8);
		skin_editor = new SUIButton("SKIN EDITOR", 2*getWidth()/5-g.getFontMetrics().stringWidth("SKIN EDITOR")/2, 6*getHeight()/8);
		map_editor = new SUIButton("MAP EDITOR", 3*getWidth()/5-g.getFontMetrics().stringWidth("MAP EDITOR")/2, 6*getHeight()/8);
		settings = new SUIButton("SETTINGS", 4*getWidth()/5-g.getFontMetrics().stringWidth("SETTINGS")/2, 6*getHeight()/8);
		addComponent(version);
		addComponent(play);
		addComponent(skin_editor);
		addComponent(map_editor);
		addComponent(settings);
	}
	
	
}
