package org.smash.ui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import org.smash.res.SmashProperties;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIText;

public class SettingsPanel extends InteractivePanel {
	private static final long serialVersionUID = -6828037642506074386L;	
	
	private SmashProperties properties;
	private SFrame frame;
	
	private SUIText left;
	private SUIText right;
	private SUIText up;
	private SUIText shoot;
	private SUIText power;
	
	private SUIButton save;
	private SUIButton cancel;
	
	private SUIButton leftCtrl;
	private SUIButton rightCtrl;
	private SUIButton upCtrl;
	private SUIButton shootCtrl;
	private SUIButton powerCtrl;
	
	private int current = 0;
	
	public SettingsPanel(SFrame frame){
		super();
		this.frame=frame;
		this.properties=frame.getProperties();
		repaint();
	}
	
	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(save.equals(concerned))
			exit(true);
		else if(cancel.equals(concerned))
			exit(false);
		else if(leftCtrl.equals(concerned))
			current = InteractivePanel.LEFT;
		else if(rightCtrl.equals(concerned))
			current = InteractivePanel.RIGHT;
		else if(upCtrl.equals(concerned))
			current = InteractivePanel.UP;
		else if(shootCtrl.equals(concerned))
			current = InteractivePanel.SHOOT;
		else if(powerCtrl.equals(concerned))
			current = InteractivePanel.POWER;
	}
	
	public void handleKey(int action){
		if(action<0 || current == 0){
			if(action==current)
				current=0;
			return;
		}
		switch(current){
		case InteractivePanel.LEFT:
			properties.setLeftCtrl(action);
			leftCtrl.setText(KeyEvent.getKeyText(action));
			break;
		case InteractivePanel.RIGHT:
			properties.setRightCtrl(action);
			rightCtrl.setText(KeyEvent.getKeyText(action));
			break;
		case InteractivePanel.UP:
			properties.setUpCtrl(action);
			upCtrl.setText(KeyEvent.getKeyText(action));
			break;
		case InteractivePanel.SHOOT:
			properties.setShootCtrl(action);
			shootCtrl.setText(KeyEvent.getKeyText(action));
			break;
		case InteractivePanel.POWER:
			properties.setPowerCtrl(action);
			powerCtrl.setText(KeyEvent.getKeyText(action));
			break;
		}
		current =0;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(left==null)
			init();
		super.paintComponent(g);
		if(current!=0){
			g.setColor(new Color(0, 0, 0, (int)(0.7*255)));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.DARK_GRAY);
			g.fillRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
			g.setColor(Color.WHITE);
			g.drawString("Please press a key to set this control", getWidth()/2-g.getFontMetrics().stringWidth("Please press a key to set this control")/2, getHeight()/2);
		}
		repaint(50);
	}
	
	private void init(){
		left = new SUIText("Left :", getWidth()/4, getHeight()/8);
		right = new SUIText("Right :", getWidth()/4, 2*(getHeight()/8));
		up = new SUIText("Up :", getWidth()/4, 3*(getHeight()/8));
		shoot = new SUIText("Shoot :", getWidth()/4, 4*(getHeight()/8));
		power = new SUIText("Use power :", getWidth()/4, 5*(getHeight()/8));
		
		save = new SUIButton("Apply", getWidth()/2-200, 8*(getHeight()/10));
		cancel = new SUIButton("Cancel", getWidth()/2+200, 8*(getHeight()/10));
		
		leftCtrl = new SUIButton(KeyEvent.getKeyText(properties.getLeftCtrl()), (int) (2.5*(getWidth()/4)), getHeight()/8);
		rightCtrl = new SUIButton(KeyEvent.getKeyText(properties.getRightCtrl()), (int) (2.5*(getWidth()/4)), 2*(getHeight()/8));
		upCtrl = new SUIButton(KeyEvent.getKeyText(properties.getUpCtrl()), (int) (2.5*(getWidth()/4)), 3*(getHeight()/8));
		shootCtrl = new SUIButton(KeyEvent.getKeyText(properties.getShootCtrl()), (int) (2.5*(getWidth()/4)), 4*(getHeight()/8));
		powerCtrl = new SUIButton(KeyEvent.getKeyText(properties.getPowerCtrl()), (int) (2.5*(getWidth()/4)), 5*(getHeight()/8));
		
		addComponent(left);
		addComponent(right);
		addComponent(up);
		addComponent(shoot);
		addComponent(power);
		addComponent(save);
		addComponent(cancel);
		addComponent(leftCtrl);
		addComponent(rightCtrl);
		addComponent(upCtrl);
		addComponent(shootCtrl);
		addComponent(powerCtrl);
	}
	
	public void exit(boolean save){
		if(save)
			properties.save();
		frame.goTo(new MainMenuPanel(frame));
	}

}
