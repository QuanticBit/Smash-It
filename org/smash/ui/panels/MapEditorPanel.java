package org.smash.ui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.smash.Map;
import org.smash.res.RessourcesCreator;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIGrid;
import org.smash.utils.MapFilter;
import org.smash.utils.ImageFilter;
import org.xor.utils.GraphicsTools;

public class MapEditorPanel extends InteractivePanel {
	private static final long serialVersionUID = 723804235216883473L;
	
	private SFrame frame;
	private Map current;
	
	private SUIButton save;
	private SUIButton cancel;
	private SUIButton clear;
	private SUIButton load;
	
	private SUIButton background;
	private SUIButton name;
	private SUIButton comments;
	private SUIButton brick;
	
	private SUIGrid grid;
	
	private int startX;
	private int startY;
	private int size;
	
	private BufferedImage back;
	private BufferedImage brick_pic;
	
	public MapEditorPanel(SFrame frame){
		super();
		this.frame=frame;
		current = new Map("random");
		repaint();
	}
	
	public void init(Graphics g){
		startX = (3*getWidth()/26);
		startY = getHeight()/12+size+50;
		grid = new SUIGrid(startX, startY, getWidth()-2*startX, getHeight()-2*startY, Map.height, Map.width);
		size = grid.getSize();
		
		save = new SUIButton("Save", 3*getWidth()/5, 9*(getHeight()/10));
		cancel = new SUIButton("Cancel", 4*getWidth()/5, 9*(getHeight()/10));
		clear = new SUIButton("Clear", getWidth()/5, 9*(getHeight()/10));
		load = new SUIButton("Load", 2*getWidth()/5, 9*(getHeight()/10));

		g.setFont(SUIComponent.normal);
		name = new SUIButton("Name", 1*getWidth()/5-g.getFontMetrics().stringWidth("Name")/2, getHeight()/12);
		background = new SUIButton("Background", 3*getWidth()/5-g.getFontMetrics().stringWidth("Background")/2, getHeight()/12);
		comments = new SUIButton("Comments", 2*getWidth()/5-g.getFontMetrics().stringWidth("Comments")/2, getHeight()/12);
		brick = new SUIButton("Brick", 4*getWidth()/5-g.getFontMetrics().stringWidth("Brick")/2, getHeight()/12);
		
		addComponent(save);
		addComponent(cancel);
		addComponent(clear);
		addComponent(load);
		addComponent(brick);
		addComponent(name);
		addComponent(background);
		addComponent(comments);
	}
	
	
	public void paintComponent(Graphics g){
		if(save==null)
			init(g);
		super.paintComponent(g);
		if(back!=null)
			g.drawImage(back, startX, startY, this);
		grid.paint(g);
		g.translate(startX, startY);
		for (int x = 0; x< Map.width; x++){
			for(int y = 0; y< Map.height; y++){
				int type = current.getRawCell(x, y);
				switch(type){
				case Map.CELL_BONUS:
					g.setColor(Color.BLUE);
					g.fillRect(x*size, y*size, size, size);
					break;
				case Map.CELL_OBSTACLE:
					if(brick_pic!=null)
						g.drawImage(brick_pic, x*size, y*size, this);
					else{
						g.setColor(Color.RED);
						g.fillRect(x*size, y*size, size, size);
					}
					break;
				}
			}
		}
		g.translate(-startX, -startY);
		repaint(50);
	}
	
	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(concerned == null && grid.getCaseIndex(x, y)!=null){
			String s = grid.getCaseIndex(x, y);
			int x2 = Integer.parseInt(s.split(";")[0]);
			int y2 = Integer.parseInt(s.split(";")[1]);
			int type = current.getRawCell(x2, y2);
			current.setCase(type == Map.CELL_BONUS ? Map.CELL_FREE : type+1, x2, y2);
		}
		if(save.equals(concerned))
			exit(true);
		else if(cancel.equals(concerned))
			exit(false);
		else if(clear.equals(concerned)){
			current = new Map("random");
			back = null;
			brick_pic = null;
		}
		else if(name.equals(concerned)){
			frame.setAlwaysOnTop(false);
			String name = JOptionPane.showInputDialog("Enter here the new name of the map :", current.getName());
			if(name!= null)
				current.setName(name);
			frame.setAlwaysOnTop(true);
		}
		else if(comments.equals(concerned)){
			frame.setAlwaysOnTop(false);
			String name = JOptionPane.showInputDialog("Enter here the comments you want for this map :", current.getComments());
			if(name!= null)
				current.setComments(name);
			frame.setAlwaysOnTop(true);
		}
		else if(background.equals(concerned)){
			frame.setAlwaysOnTop(false);
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new ImageFilter());
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            try {
					current.setBackground(GraphicsTools.resizeImage(ImageIO.read(file), 1800, 900));
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		back=GraphicsTools.resizeImage(current.getBackground(), Map.width*size, Map.height*size);
	        }
			frame.setAlwaysOnTop(true);
		}	
		else if(brick.equals(concerned)){
			frame.setAlwaysOnTop(false);
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new ImageFilter());
			fc.setAcceptAllFileFilterUsed(false);
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            try {
					brick_pic = GraphicsTools.resizeImage(ImageIO.read(file), size, size);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
			frame.setAlwaysOnTop(true);
		}	
		else if(load.equals(concerned)){
			frame.setAlwaysOnTop(false);
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new MapFilter());
			fc.setAcceptAllFileFilterUsed(false);
			fc.setCurrentDirectory(RessourcesCreator.MAP_FOLDER);
			if(current.getPath()!=null)
				fc.setCurrentDirectory(current.getPath().getParentFile());
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            current.setPath(file);
	            current.load();
	            back=GraphicsTools.resizeImage(current.getBackground(), Map.width*size, Map.height*size);
	            brick_pic = null;
	        }else{
	        	frame.setAlwaysOnTop(true);
	        	return;
	        }
			frame.setAlwaysOnTop(true);
		}
		frame.setTitle("Smash It - "+current.getName()+" : "+current.getComments());
	}

	private void exit(boolean b) {
		if(b){
			frame.setAlwaysOnTop(false);
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new MapFilter());
			fc.setCurrentDirectory(RessourcesCreator.MAP_FOLDER);
			fc.setAcceptAllFileFilterUsed(false);
			if(current.getPath()!=null)
				fc.setCurrentDirectory(current.getPath().getParentFile());
			int returnVal = fc.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            if(!MapFilter.isValid(file))
	            	file = new File(file.getAbsolutePath()+".smap");
	            current.setPath(file);
	            if(brick_pic!=null){
	            	BufferedImage buf = current.getBackground();
		            Graphics g = buf.getGraphics();
		            BufferedImage draw = GraphicsTools.resizeImage(brick_pic, Map.size, Map.size);
		            for (int x = 0; x< Map.width; x++){
		    			for(int y = 0; y<Map.height; y++){
		    				int type = current.getRawCell(x, y);
		    				if(type==Map.CELL_OBSTACLE)
		    					g.drawImage(draw, x*Map.size, y*Map.size, null);
		    			}
		    		}
		            current.setBackground(buf);
	            }
	        }else{
	        	frame.setAlwaysOnTop(true);
	        	return;
	        }
			frame.setAlwaysOnTop(true);
			current.save();
		}
		frame.goTo(new MainMenuPanel(frame));
	}

}
