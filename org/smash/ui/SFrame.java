package org.smash.ui;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

import org.smash.res.SmashProperties;
import org.smash.ui.panels.MainMenuPanel;
import org.xor.utils.GraphicsTools;

public class SFrame extends JFrame {

	private static final long serialVersionUID = -1855397565527633316L;
	
	private SmashProperties properties;
	private InteractivePanel contentPane;
	/**
	 * Create the frame.
	 * 
	 * @param properties is the user's name
	 */
	public SFrame(SmashProperties prop) {
		prop.gainXp(1);
		prop.save();
		this.properties = prop;
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int info = arg0.getKeyCode();
				info = transform(info);
				contentPane.handleKey(info);
			}
		});
		setResizable(false);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setAlwaysOnTop(true);
		setIgnoreRepaint( true );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Smash It - "+properties.getUsername());
		setIconImage(GraphicsTools.loadImageFromResource("icon.png"));
		setVisible(true);
		goTo(new MainMenuPanel(this));
	}
	
	public void goTo(InteractivePanel pane){
		if(pane==null)
			return;
		this.contentPane = pane;
		this.contentPane.setBounds(0, 0, getWidth(), getHeight());
		setContentPane(this.contentPane);
		repaint();
	}
	
	public SmashProperties getProperties(){
		return properties;
	}
	
	public int transform(int info){
		if(info==properties.getUpCtrl()){
			return InteractivePanel.UP;
		}
		else if(info==properties.getLeftCtrl()){
			return InteractivePanel.LEFT;
		}
		else if(info==properties.getRightCtrl()){
			return InteractivePanel.RIGHT;
		}
		else if(info==properties.getShootCtrl())
			return InteractivePanel.SHOOT;
		else if(info==properties.getPowerCtrl())
			return InteractivePanel.POWER;
		return info;
	}
	
	public int untransform(int info){
		if(info==InteractivePanel.UP)
			return properties.getUpCtrl();
		else if(info==InteractivePanel.LEFT)
			return properties.getLeftCtrl();
		else if(info==InteractivePanel.RIGHT)
			return properties.getRightCtrl();
		else if(info==InteractivePanel.SHOOT)
			return properties.getShootCtrl();
		else if(info==InteractivePanel.POWER)
			return properties.getPowerCtrl();
		return info;
	}

}
