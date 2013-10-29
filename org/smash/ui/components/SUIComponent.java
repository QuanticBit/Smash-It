package org.smash.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.xor.utils.GraphicsTools;

public class SUIComponent {
	public static final Font normal = new Font("pixelpunch", Font.PLAIN, 18).deriveFont((float) (18*GraphicsTools.getScreenWidth()/1920));
	public static final Color color = new Color(100, 0, 0);
	public static final Color focused = new Color(170, 0, 0);
	
	protected int x = 0;
	protected int y = 0;
	protected int width = -1;
	protected int height = -1;
	protected boolean shouldPaint = true;
	
	public SUIComponent(int x, int y){
		setX(x);
		setY(y);
	}
	
	public boolean isLocationInside(Graphics g, double posX, double posY){
		if( posX>getX() && posX< getX()+getWidth()){
			if( posY>getY() && posY<getY()+getHeight()){
				return true;
			}
		}
		return false;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean shouldPaint() {
		return shouldPaint;
	}

	public void setShouldPaint(boolean shouldPaint) {
		this.shouldPaint = shouldPaint;
	}

	public void paint(Graphics g) {}

	public boolean equals(Object obj){
		if(obj instanceof SUIComponent){
			SUIComponent o = (SUIComponent) obj;
			return x == o.getX() && y == o.getY() && shouldPaint == o.shouldPaint();
		}else
			return false;
			
	}
}
