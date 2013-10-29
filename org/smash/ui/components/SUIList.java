package org.smash.ui.components;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public class SUIList extends SUIComponent {
	
	private ArrayList<SUIText> texts;
	private ArrayList<String> strings;
	private int number = 0;
	
	private int start = 0;
	private int selected = -1;

	private Polygon arrowUp, arrowDown;

	public SUIList(int x, int y, int width, int height, ArrayList<String> strings) {
		super(x, y);
		this.width= width;
		this.height = height;
		this.strings = strings;
		texts = new ArrayList<SUIText>();
		SUIText sample = new SUIText(strings.get(0), x+5, y+5);
		texts.add(sample);
	}
	
	public void paint(Graphics g){
		for(SUIText text : texts){
			if(text.shouldPaint())
				text.paint(g);
		}
		if(number == 0){
			number = height /(texts.get(0).getHeight());
			texts.get(0).setY(texts.get(0).getY()+texts.get(0).getHeight()/2);
			for(int i =1; i<number; i++){
				SUIText sample = new SUIText(strings.size()>i 
						? strings.get(i) : "", x+5, 5+y+i*texts.get(0).getHeight()+texts.get(0).getHeight()/2);
				texts.add(sample);
			}
			int[] xPoints = {getX()+getWidth()-50, getX()+getWidth()-25, getX()+getWidth()};
			int[] yPoints = {getY()+50, getY(), getY()+50};
			arrowUp = new Polygon(xPoints, yPoints, 3);
			int[] yPoints1 = {getY()+getHeight()-50, getY()+getHeight(), getY()+getHeight()-50};
			arrowDown = new Polygon(xPoints, yPoints1, 3);
			update(g);
		}
		g.setColor(focused);
		g.fillPolygon(arrowUp);
		g.fillPolygon(arrowDown);
		if(selected != -1){
			SUIText sel = texts.get(selected);
			g.drawRect(sel.getX()-5, sel.getY()-sel.getHeight()/2, getWidth()-50, sel.getHeight()/2+5);
		}
	}
	
	public void click(Graphics g, double x,double y, double x1,double y1){
		if(arrowUp.contains(x1, y1)){
			boolean possible= start <= 0;
			start = possible ? 0 : start-1;
			selected = possible 
					? selected : selected+1 >= number 
							? -1 : selected+1;
		}
		else if(arrowDown.contains(x1, y1)){
			boolean possible= start >= Math.max(0, strings.size()-number);
			start = possible ? Math.max(0, strings.size()-number) : start+1;
			selected = selected == -1 
					? -1 : possible
						? selected : selected-1 < 0 
								? -1 : selected-1;
		}
		else{
			int index = 0;
			for(SUIText text : texts){
				if(text.isLocationInside(g, x, y)){
					selected = selected == index ? -1 : index;
					break;
				}
				index++;
			}
		}
		update(g);
	}
	
	public void setStrings(Graphics g, ArrayList<String> strings){
		this.strings=strings;
		update(g);
	}
	
	public void update(Graphics g){
		g.setFont(normal);
		for(int i =0; i<number; i++){
			SUIText sample = texts.get(i);
			sample.setShouldPaint(true);
			if( strings.size()>i+start)
				sample.setText(strings.get(i+start));
			else
				sample.setShouldPaint(false);
		}
		selected = selected >= strings.size() ? -1 : selected;
	}
	
	public int getSelected() {
		return selected==-1? -1 :selected+start;
	}

}
