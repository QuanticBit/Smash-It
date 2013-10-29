package org.smash.ui.panels;

import java.awt.Graphics;
import java.io.File;

import javax.swing.JFileChooser;

import org.smash.res.Skin;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.utils.SkinFilter;

public class PlayConfigPanel extends InteractivePanel {
	
	private static final long serialVersionUID = -1841876803557557562L;
	
	private SFrame frame;
	
	private SUIButton host;
	private SUIButton cancel;
	private SUIButton join;
	private SUIButton avatar_set;
	
	private boolean walking = false;
	private int count = 0;
	
	private String user;
	
	private Skin skin;

	public PlayConfigPanel(SFrame frame) {
		super();
		this.frame = frame;
		user = frame.getProperties().getUsername();
		skin = new Skin();
		File def = new File(frame.getProperties().getSkin());
		skin.setPath(def.exists() ? def : new File("./skins/default.sskin"));
		skin.load();
		this.frame.getProperties().setSkin(skin.getPath().getAbsolutePath());
		this.frame.getProperties().save();
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(host==null)
			init(g);
		super.paintComponent(g);
		g.setColor(SUIComponent.color);
		g.setFont(SUIComponent.normal);
		g.drawString(user,  getWidth()/2-30-g.getFontMetrics().stringWidth(user)/2, 100+g.getFontMetrics().getHeight()/2+23);
		g.drawImage(walking ? skin.getWalkR() : skin.getStandR() , getWidth()/2-30+g.getFontMetrics().stringWidth(user), 100, this);
		walking = count%30 == 0 ? !walking : walking;
		count ++;
		repaint(50);
	}

	private void init(Graphics g) {
		g.setFont(SUIComponent.normal);
		host = new SUIButton("Host", getWidth()/2-g.getFontMetrics().stringWidth("Host")/2, getHeight()/2-150);
		join = new SUIButton("Join", getWidth()/2-g.getFontMetrics().stringWidth("Join")/2, getHeight()/2);
		avatar_set = new SUIButton("Change Skin", getWidth()/2-g.getFontMetrics().stringWidth("Change Skin")/2, getHeight()/2+150);
		cancel = new SUIButton("Cancel", getWidth()/2-g.getFontMetrics().stringWidth("Cancel")/2, getHeight()/2+300);
		addComponent(host);
		addComponent(cancel);
		addComponent(join);
		addComponent(avatar_set);
	}

	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(host.equals(concerned))
			frame.goTo(new HostPanel(frame));
		else if(cancel.equals(concerned))
			frame.goTo(new MainMenuPanel(frame));
		else if(join.equals(concerned))
			frame.goTo(new JoinPanel(frame));
		else if(avatar_set.equals(concerned)){
			frame.setAlwaysOnTop(false);
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new SkinFilter());
			fc.setAcceptAllFileFilterUsed(false);
			if(skin.getPath()!=null)
				fc.setCurrentDirectory(skin.getPath().getParentFile());
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            skin.setPath(file);
	            skin.load();
	            frame.getProperties().setSkin(skin.getPath().getPath());
	            frame.getProperties().save();
	        }
			frame.setAlwaysOnTop(true);
		}		
	}
}
