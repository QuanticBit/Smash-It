package org.smash.ui.components;

import java.awt.Graphics;
import java.awt.MouseInfo;

public class SUIButton extends SUIComponent{
	
	private String txt = "Button";
	
	public SUIButton(String txt,int x,int y){
		super(x, y);
		setText(txt);
	}
	
	public void paint(Graphics g){
		double posX = MouseInfo.getPointerInfo().getLocation().getX();
		double posY = MouseInfo.getPointerInfo().getLocation().getY();
		g.setFont(normal);
		g.setColor(color);
		updateDimension(g);
		if( isLocationInside(g, posX, posY))
			g.setColor(focused);
		g.drawString(txt, getX(),  getY());
	}
	
	public boolean isLocationInside(Graphics g, double posX, double posY){
		updateDimension(g);
		if( posX>getX() && posX< getX()+getWidth()){
			if( posY>getY()-getHeight()/2 && posY<getY()+getHeight()/2){
				return true;
			}
		}
		return false;
	}
	
	private void updateDimension(Graphics g){
		if(width != -1)
			return;
		width = g.getFontMetrics().stringWidth(txt);
		height = g.getFontMetrics().getHeight();
	}

	public String getText() {
		return txt;
	}

	public void setText(String txt) {
		this.txt = txt;
	}

}
