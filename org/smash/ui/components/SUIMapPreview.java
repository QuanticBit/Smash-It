package org.smash.ui.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.smash.Map;
import org.xor.utils.GraphicsTools;

public class SUIMapPreview extends SUIComponent {
	
	private Map map;
	private BufferedImage img;

	public SUIMapPreview(int x, int y, int width, int height, Map map) {
		super(x, y);
		this.width=width;
		this.height=height;
		setMap(map);
	}
	
	public void paint(Graphics g){
		if(map==null)
			return;
		g.setFont(normal);
		g.setColor(color);
		g.drawImage(img, x, y, null);
		g.drawString(map.getName()+" - "+map.getComments(), x, y+g.getFontMetrics().getHeight());
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		if(map!= null)
			img = GraphicsTools.resizeImage(map.getBackground(), width, height);
	}

}
