package org.smash.ui.panels;

import static org.smash.network.GameNetwork.MOVE_DOWN;
import static org.smash.network.GameNetwork.MOVE_LEFT;
import static org.smash.network.GameNetwork.MOVE_RIGHT;
import static org.smash.network.GameNetwork.MOVE_UP;
import static org.smash.network.GameNetwork.NO_MOVE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import org.smash.SmashGame;
import org.smash.network.host.SmashServer;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIComponent;
import org.xor.utils.GraphicsTools;
import org.xor.utils.NetworkUtils;
import org.xor.utils.SwingDelayedRepainter;

public class HostPlayPanel extends InteractivePanel {
	private static final long serialVersionUID = -3937207918741280985L;
	private SmashGame game;
	private SmashServer client;
	
	private BufferedImage background;
	
	private int moveX = 0;
	private int moveY = 0;
	
	private long lastTick = 0;
	private int height;
	
	public HostPlayPanel(SmashServer smashClient, SFrame frame) {
		super();
		frame.setAlwaysOnTop(false);
		smashClient.setMaxPlayers((int) JOptionPane.showInputDialog(frame, "Choose the number of Players", "Players",
				JOptionPane.QUESTION_MESSAGE, null, new Object[]{1, 2, 3, 4}, 2));
		frame.setAlwaysOnTop(true);
		this.setClient(smashClient);
		this.setGame(smashClient.getGame());
		background = new BufferedImage(1800, 980, BufferedImage.TYPE_INT_ARGB);
		height = (int) (980*GraphicsTools.getScreenWidth()/1800);
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(game.hasStarted()){
			g.drawImage(background, 0, 0, getWidth(), height, null);
			if(System.currentTimeMillis()-lastTick>=150){
				handleKey(100);
				lastTick = System.currentTimeMillis();
				game.paint(background.createGraphics());
			}
			else if(game.shouldRepaint()){
				game.paint(background.createGraphics());
			}	
			new SwingDelayedRepainter(this, 50);
		}
		else{
			g.setFont(SUIComponent.normal);
			g.setColor(SUIComponent.focused);
			String s = "Your IP : "+NetworkUtils.getIP();
			g.drawString(s, getWidth()/2-g.getFontMetrics().stringWidth(s)/2, getHeight()/2-50);
			s= "Waiting for players : "+client.getPlayers()+"/"+client.getMAX_PLAYERS();
			g.drawString(s, getWidth()/2-g.getFontMetrics().stringWidth(s)/2, getHeight()/2+50);
			repaint(100);
		}
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
		case 100:
			game.output(100, moveX+moveY);
			return;
		default:
			key2 = 99;
			break;
		}
		game.output(key2, moveX+moveY);
	}

	private void setClient(SmashServer client) {
		this.client = client;
	}

	private void setGame(SmashGame game) {
		this.game = game;
	}

}
