package org.smash.ui.components;

import java.awt.Color;
import java.awt.Graphics;

public class SUIGrid extends SUIComponent {
	
	private int size = 0;
	private int lines = 0;
	private int columns = 0;

	public SUIGrid(int x, int y, int width, int height, int lines, int columns) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.lines = lines;
		this.columns = columns;
		size = Math.min(width/columns, height/lines);
	}
	
	public void paint(Graphics g){
		g.translate(getX(), getY());
		for (int x = 0; x< columns; x++){
			for(int y = 0; y< lines; y++){
				g.setColor(Color.WHITE);
				g.drawRect(x*size,  y*size, size, size);
			}
		}
		g.translate(-getX(), -getY());
	}
	
	public String getCaseIndex(double x, double y){
		if(x>=getX() && x<=getWidth()+getX() && y>=getY() && y<getHeight()+getY()){
			int x2 = (int) ((x-getX())/size);
			int y2 = (int) ((y-getY())/size);
			return x2+";"+y2;
		}
		return null;
	}

	public int getSize() {
		return size;
	}

}
