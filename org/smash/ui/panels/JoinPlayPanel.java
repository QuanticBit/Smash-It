package org.smash.ui.panels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.smash.SmashGame;
import org.smash.network.client.SmashClient;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIMapPreview;
import org.xor.utils.GraphicsTools;
import org.xor.utils.SwingDelayedRepainter;

import static org.smash.network.GameNetwork.*;

public class JoinPlayPanel extends InteractivePanel {
	private static final long serialVersionUID = -1314787885920413147L;
	
	private SmashGame game;
	private SmashClient client;
	
	private BufferedImage background;
	
	private int moveX = 0;
	private int moveY = 0;
	
	private int height;
	
	private String msg = null;
	
	private SFrame frame;
	private SUIMapPreview preview;

	private boolean quit;

	public JoinPlayPanel(SmashClient smashClient, SFrame frame) {
		this.frame = frame;
		this.setClient(smashClient);
		this.setGame(smashClient.getGame());
		background = new BufferedImage(1800, 980, BufferedImage.TYPE_INT_ARGB);
		height = (int) (980*GraphicsTools.getScreenWidth()/1800);
		repaint();
	}
	
	public void init(){
		client.init();
		preview = new SUIMapPreview(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, null);
		addComponent(preview);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(client.shouldQuit())
			quit();
		if(game.hasStarted() && !quit){	
			preview.setShouldPaint(false);
			if(game.shouldRepaint())
				game.paint(background.createGraphics());
			g.drawImage(background, 0, 0, getWidth(), height, null);
			new SwingDelayedRepainter(this, 50);
		}
		else{
			g.setFont(SUIComponent.normal);
			g.setColor(SUIComponent.focused);
			String s = msg == null ? client.getMessage() : msg;
			g.drawString(s, getWidth()/2-g.getFontMetrics().stringWidth(s)/2, 7*getHeight()/8);
			repaint(50);
		}
		if(preview != null)
			preview.setMap(game.getMap());
	}
	private void quit() {
		quit = true;
		msg = client.getMessage();
		repaint();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.goTo(new JoinPanel(frame));
	}

	@Override
	public void handleKey(int key){
		int key2 = key;
		int move = game.getOwner().getMove();
		moveX = move == MOVE_LEFT+MOVE_DOWN || move == MOVE_LEFT+MOVE_UP || move == MOVE_LEFT 
				? MOVE_LEFT :  move == MOVE_RIGHT+MOVE_DOWN || move == MOVE_RIGHT+MOVE_UP || move == MOVE_RIGHT 
						? MOVE_RIGHT : NO_MOVE;
		moveY = move == MOVE_LEFT+MOVE_DOWN || move == MOVE_RIGHT+MOVE_DOWN || move == MOVE_DOWN 
				? MOVE_DOWN :  move == MOVE_LEFT+MOVE_UP || move == MOVE_RIGHT+MOVE_UP || move == MOVE_UP 
					? MOVE_UP : NO_MOVE;
		switch(key){
		case InteractivePanel.UP:
			moveY = MOVE_UP;
			break;
		case InteractivePanel.LEFT:
			moveX = MOVE_LEFT;
			break;
		case InteractivePanel.RIGHT:
			moveX = MOVE_RIGHT;
			break;
		case InteractivePanel.SHOOT:
			break;
		default:
			key2 = 99;
			break;
		}
		game.output(key2, moveX+moveY);
	}

	private void setClient(SmashClient client) {
		this.client = client;
	}

	private void setGame(SmashGame game) {
		this.game = game;
	}

}
