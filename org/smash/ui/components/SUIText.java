package org.smash.ui.components;

import java.awt.Graphics;

public class SUIText extends SUIComponent{
	
	private String txt = "Label";

	
	public SUIText(String txt, int x, int y){
		super(x, y);
		setText(txt);
	}
	
	public void paint(Graphics g){
		g.setColor(color);
		g.setFont(normal);
		if(width == -1){
			width = g.getFontMetrics().stringWidth(txt);
			height = g.getFontMetrics().getHeight();
		}
		g.drawString(txt, x, y);
	}
	
	public boolean isLocationInside(Graphics g, double posX, double posY){
		if( posX>getX() && posX< getX()+getWidth()){
			if( posY>getY()-getHeight()/2 && posY<getY()+getHeight()/2){
				return true;
			}
		}
		return false;
	}

	public String getText() {
		return txt;
	}

	public void setText(String txt) {
		this.txt = txt;
		width = -1;
	}


}

