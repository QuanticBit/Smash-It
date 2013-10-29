package org.smash.ui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import org.smash.Map;
import org.smash.res.RessourcesCreator;
import org.smash.res.Skin;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIGrid;
import org.smash.ui.components.SUIText;
import org.smash.utils.SkinFilter;
import org.xor.utils.GraphicsTools;

public class SkinEditorPanel extends InteractivePanel {

	private static final long serialVersionUID = 8966140445490585879L;
	
	private SUIButton save;
	private SUIButton cancel;
	private SUIButton clear;
	private SUIButton colorChange;
	
	private SUIGrid standGrid;
	private SUIGrid walkGrid;
	private SUIGrid jumpGrid;
	
	private SUIText standDesc;
	private SUIText walkDesc;
	private SUIText jumpDesc;
	
	private int color = Color.WHITE.getRGB();
	
	private int[][] stand = new int[30][30];
	private int[][] walk = new int[30][30];
	private int[][] jump = new int[30][30];
	
	private int size = 0;
	
	private SFrame frame;
	
	public SkinEditorPanel(SFrame frame){
		super();
		this.frame=frame;
		frame.setTitle("Smash It - Skin Editor");
		repaint();
	}
	
	public void init(Graphics g){
		save = new SUIButton("Save", 3*getWidth()/5, 9*(getHeight()/10));
		cancel = new SUIButton("Cancel", 4*getWidth()/5, 9*(getHeight()/10));
		clear = new SUIButton("Clear", 2*getWidth()/5, 9*(getHeight()/10));
		colorChange = new SUIButton("Set Color", getWidth()/5, 9*(getHeight()/10));
		addComponent(save);
		addComponent(cancel);
		addComponent(clear);
		addComponent(colorChange);
		g.setFont(SUIComponent.normal);
		int semiH = g.getFontMetrics().getHeight()/2;
		int semiW = (int) (2.5*getWidth()/20);
		standGrid = new SUIGrid(getWidth()/20, getHeight()/6, (int) (2.5*getWidth()/10), 7*getHeight()/10, 30, 30);
		standDesc = new SUIText("Stand Right", getWidth()/20+semiW-g.getFontMetrics().stringWidth("Stand Right")/2, getHeight()/6-semiH);
		addComponent(standGrid);
		addComponent(standDesc);
		walkGrid = new SUIGrid((int) (3.8*getWidth()/10), getHeight()/6, (int) (2.5*getWidth()/10), 7*getHeight()/10, 30, 30);
		addComponent(walkGrid);
		walkDesc = new SUIText("Walk Right", (int) (3.8*getWidth()/10)+semiW-g.getFontMetrics().stringWidth("Walk Right")/2,
				getHeight()/6-semiH);
		addComponent(walkDesc);
		jumpGrid = new SUIGrid(7*getWidth()/10, getHeight()/6, (int) (2.5*getWidth()/10), 7*getHeight()/10, 30, 30);
		addComponent(jumpGrid);
		jumpDesc = new SUIText("Jump Right", 7*getWidth()/10+semiW-g.getFontMetrics().stringWidth("Jump Right")/2, getHeight()/6-semiH);
		addComponent(jumpDesc);
		size = walkGrid.getSize();
	}
	
	
	public void paintComponent(Graphics g){
		if(save==null)
			init(g);
		super.paintComponent(g);
		g.setColor(new Color(color));
		g.fillRect(100, (int) (8.5*(getHeight()/10)), 80, 80);
		g.translate(standGrid.getX(), standGrid.getY());
		for( int x = 0; x<30; x++){
			for(int y =0; y< 30; y++){
				if(stand[x][y]==0)
					continue;
				g.setColor(new Color(stand[x][y]));
				g.fillRect(x*size, y*size, size, size);
			}
		}
		g.translate(-standGrid.getX(), -standGrid.getY());
		g.translate(walkGrid.getX(), walkGrid.getY());
		for( int x = 0; x<30; x++){
			for(int y =0; y< 30; y++){
				if(walk[x][y]==0)
					continue;
				g.setColor(new Color(walk[x][y]));
				g.fillRect(x*size, y*size, size, size);
			}
		}
		g.translate(-walkGrid.getX(),-walkGrid.getY());
		g.translate(jumpGrid.getX(), jumpGrid.getY());
		for( int x = 0; x<30; x++){
			for(int y =0; y< 30; y++){
				if(jump[x][y]==0)
					continue;
				g.setColor(new Color(jump[x][y]));
				g.fillRect(x*size, y*size, size, size);
			}
		}
		g.translate(-jumpGrid.getX(), -jumpGrid.getY());
		repaint(50);
	}
	
	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(standGrid.getCaseIndex(x, y)!=null){
			String s = standGrid.getCaseIndex(x, y);
			int x2 = Integer.parseInt(s.split(";")[0]);
			int y2 = Integer.parseInt(s.split(";")[1]);
			stand[x2][y2]= stand[x2][y2]==color ? 0 :color;
		}
		else if(jumpGrid.getCaseIndex(x, y)!=null){
			String s = jumpGrid.getCaseIndex(x, y);
			int x2 = Integer.parseInt(s.split(";")[0]);
			int y2 = Integer.parseInt(s.split(";")[1]);
			jump[x2][y2]= jump[x2][y2]==color ? 0 :color;
		}
		else if(walkGrid.getCaseIndex(x, y)!=null){
			String s = walkGrid.getCaseIndex(x, y);
			int x2 = Integer.parseInt(s.split(";")[0]);
			int y2 = Integer.parseInt(s.split(";")[1]);
			walk[x2][y2]= walk[x2][y2]==color ? 0 :color;
		}
		else if(save.equals(concerned))
			exit(true);
		else if(cancel.equals(concerned))
			exit(false);
		else if(clear.equals(concerned)){
			stand = new int[30][30];
			walk = new int[30][30];
			jump = new int[30][30];
		}
		else if(colorChange.equals(concerned)){
			frame.setAlwaysOnTop(false);
			Color c = JColorChooser.showDialog(frame, "Choose color", new Color(color));
			if(c!=null)
				color = c.getRGB();
			frame.setAlwaysOnTop(true);
		}		
	}
	
	private void exit(boolean b) {
		if(b){
			frame.setAlwaysOnTop(false);
			Skin skin = new Skin();
			//Create the pictures
			BufferedImage walkR = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
			for( int x = 0; x<30; x++){
				for(int y =0; y< 30; y++){
					if(walk[x][y]==0)
						continue;
					walkR.setRGB(x, y, walk[x][y]);
				}
			}
			skin.setWalkR(GraphicsTools.resizeImage(walkR, Map.size, Map.size, false));
			BufferedImage standR = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
			for( int x = 0; x<30; x++){
				for(int y =0; y< 30; y++){
					if(stand[x][y]==0)
						continue;
					standR.setRGB(x, y, stand[x][y]);
				}
			}
			skin.setStandR(GraphicsTools.resizeImage(standR, Map.size, Map.size, false));
			BufferedImage jumpR = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
			for( int x = 0; x<30; x++){
				for(int y =0; y< 30; y++){
					if(jump[x][y]==0)
						continue;
					jumpR.setRGB(x, y, jump[x][y]);
				}
			}
			skin.setJumpR(GraphicsTools.resizeImage(jumpR, Map.size, Map.size, false));
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new SkinFilter());
			fc.setCurrentDirectory(RessourcesCreator.SKIN_FOLDER);
			fc.setAcceptAllFileFilterUsed(false);
			if(skin.getPath()!=null)
				fc.setCurrentDirectory(skin.getPath().getParentFile());
			int returnVal = fc.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            if(!SkinFilter.isValid(file))
	            	file = new File(file.getAbsolutePath()+".sskin");
	            skin.setPath(file);
	        }else{
	        	frame.setAlwaysOnTop(true);
	        	return;
	        }
			frame.setAlwaysOnTop(true);
			skin.save();
		}
		frame.goTo(new MainMenuPanel(frame));
	}

}
