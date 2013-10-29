package org.smash.ui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIGrid;
import org.smash.ui.components.SUIList;
import org.xor.utils.GraphicsTools;

public class InteractivePanel extends JPanel{
	private static final long serialVersionUID = 5004251439432220187L;
	
	public static final int LEFT = -1;
	public static final int RIGHT = -2;
	public static final int UP = -3;
	public static final int SHOOT = -5;
	public static final int POWER = -6;
	
	protected ArrayList<SUIComponent> components = new ArrayList<SUIComponent>();
	private BufferedImage background = GraphicsTools.loadImageFromResource("background.png");

	public InteractivePanel(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()!=MouseEvent.BUTTON1)
					return;
				double x = e.getXOnScreen();
				double y = e.getYOnScreen();
				Graphics g = getGraphics();
				for(SUIComponent comp : components){
					if(comp.isLocationInside(g, x, y)){
						if(comp instanceof SUIGrid)
							handleLeftClick(e.getX(), e.getY(), comp);
						else if(comp instanceof SUIList)
							((SUIList)comp).click(g, x, y, e.getX(), e.getY());
						else
							handleLeftClick(x, y, comp);
						return;
					}
				}
				handleLeftClick(e.getX(), e.getY(), null);
			}
		});
	}
	
	public void setBackground(BufferedImage background) {
		this.background = background;
	}

	/**Handles a key input if the key pressed is equal to a configured one then the value will match one of the static field above, otherwise it will be the default keycode
	 * 
	 * @param key is the keyCode or a static field
	 */
	public void handleKey(int key){}
	/**Handles a left click
	 * 
	 * @param x is the X coordinate on screen
	 * @param y is the Y coordinate on screen
	 * @param concerned is the SUIComponent under the click, if none then this argument is null
	 */
	public void handleLeftClick(double x, double y, SUIComponent concerned){}
	
	@Override
	public void update(Graphics g){
		paintComponent(g);
	}
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		for(SUIComponent comp : components){
			if(comp.shouldPaint())
				comp.paint(g);
		}
	}
	
	public void addComponent(SUIComponent comp){
		components.add(comp);
	}
	
	public SUIComponent getSUIComponent(int index){
		return components.get(index);
	}
	
	public void removeComponent(SUIComponent comp){
		components.remove(comp);
	}
}
