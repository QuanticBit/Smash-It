package org.smash;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import org.smash.network.Sender;
import org.smash.network.host.SmashServer;
import org.smash.res.Skin;
import org.smash.res.SmashProperties;
import org.smash.ui.InteractivePanel;
import org.smash.ui.components.SUIComponent;
import org.xor.utils.GraphicsConfiguration;
import org.xor.utils.GraphicsTools;

import static org.smash.network.GameNetwork.*;
/**Represent a <i>Smash It</i> game object.
 * <p>Managing the data, network communications and game mechanics.
 * 
 * @author LordOfBees
 *
 */
public class SmashGame {
	/**
	 * The static font that is used to draw player names in the game
	 */
	private static final Font font = new Font("Verdana", Font.BOLD, 12);
	/**
	 * Represents black with some transparency, used as a background/plate for player names
	 */
	private static final Color name_back = new Color(0, 0, 0, (int)(0.7*255));
	private static final BufferedImage imgR = GraphicsTools.loadImageFromResource("punch.png");
	private static final BufferedImage imgL = GraphicsTools.horizontalFlip(GraphicsTools.loadImageFromResource("punch.png"));
	/**
	 * The player controlled by this client of the game
	 */
	private Player owner;
	/**
	 * An array list containing all other players excepted the one controlled by this client
	 */
	private ArrayList<Player> players;
	/**
	 * The number of players in this game including the player managed by this client
	 */
	private int total_players = 1;
	/**
	 * The map on which the game is currently played
	 */
	private Map map;
	/**
	 * The network sender of data
	 */
	private Sender sender;
	/**
	 * Whether the game has started or not
	 */
	private boolean started = false;
	/**
	 * Whether the game has been updated and so if the game should be re-painted
	 */
	private boolean repaint = true;
	/**
	 * Store the number of player that are updated, to be sure all are updated before repaint
	 * <p>see {@link #input(String)}
	 */
	private int players_updated = 0;
	/**
	 * Whether we are on the server side or not
	 */
	private boolean serverSide = false;
	/**
	 * A array that when a player kicks contains the kick location and whether
	 *  it's the right or left image
	 */
	private int[][] kickLocs = new int[4][3];
	/**Creates a new SmashGame object and create the client-side player with these properties
	 * 
	 * @param prop is the Smash properties of the game
	 */
	public SmashGame(SmashProperties prop){
		//We load the configured skin
		Skin skin = new Skin();
		skin.setPath(new File(prop.getSkin()));
		skin.load();
		//Now we create the owner player
		owner = new Player(prop.getUsername(), skin);
		//We set the player list to an empty list
		this.players = new ArrayList<Player>();
	}
	/**<p>Initiate the game by setting the map and the sender.</p>
	 * <p><i>NOTE : It does not start the game</i></p>
	 * @param map is the map to play the game on
	 * @param sender is the network object (see {@link Sender}) that will manage to send the data
	 */
	public void init(Map map, Sender sender){
		setMap(map);
		owner.setMap(map);
		this.sender = sender;
		serverSide = sender instanceof SmashServer;
	}
	/**Get the map on which the game is currently played
	 * 
	 * @return the map on which the game is played or <b>null</b> if none
	 */
	public Map getMap() {
		return map;
	}
	/**Set the map on which the game will be played
	 * 
	 * @param map is the map to set as the one we'll play on
	 */
	private void setMap(Map map) {
		this.map = map;
		map.setBackground(GraphicsTools.resizeImage(map.getBackground(), 1800, 900));
	}
	/**Get the player managed by this client
	 * 
	 * @return the player that the user is controlling
	 */
	public Player getOwner() {
		return owner;
	}
	/**Add a player to the list
	 * 
	 * @param file is the skin path of the player's skin
	 * @param name is the name of the player
	 */
	public void addPlayer(File file, String name){
		//We load its skin
		Skin skin = new Skin();
		skin.setPath(file);
		skin.load();
		//Now we add it to our players list
		players.add(new Player(name, skin));
	}
	/**Get whether the game has started or not
	 * 
	 * @return if the game has started or not
	 */
	public boolean hasStarted() {
		return started;
	}
	/**Set if the game has started or not
	 * 
	 * @param started is the boolean to set as started
	 */
	private void setStarted(boolean started) {
		this.started = started;
	}
	/**Start the game
	 * 
	 */
	public void start() {
		//We set the map for every players
		for(Player player : players){
			player.setMap(getMap());
		}
		//We indicates that the game has started
		setStarted(true);
		//We update the number of players
		total_players = 1 +players.size();
	}
	/**Interpret the received line
	 * 
	 * @param line is an instruction sent by the host player if we are on the client-side.
	 *  Otherwise it's a key input sent by one of the clients if we are on the server-side 
	 */
	public void input(String line) {
		//First we detach the player concerned and the instruction
		String name = line.split(NAME_SEPARATOR)[0];
		Player player = null ;
		for(Player player1 : players){
			if(player1.getName().equals(name)){
				//Now we've found our player so we stop
				player = player1;
				break;
			}
		}
		//If it's not a player and not either the owner then it's not for us ^^
		if(player==null && !owner.getName().equals(name))
			return;
		else if(player == null && owner.getName().equals(name))
			player = owner;
		//Now we have found our player we separate the command and the arguments
		String cmd = line.split(NAME_SEPARATOR)[1];
		String[] args = (cmd+" ").split(" ");
		cmd = args[0];
		//Now we can interpret
		if(cmd.equals(MOVE) && args.length >= 2 && serverSide){
			player.setMove(Integer.parseInt(args[1]));
			players_updated = 0;
		}else if(cmd.equals(UPDATE) && args.length >= 6 && !serverSide){
			player.setX(Double.parseDouble(args[1]));
			player.setY(Double.parseDouble(args[2]));
			player.setLastMove(Integer.parseInt(args[3]));
			player.setLife(Integer.parseInt(args[4]));
			player.setAlive(Boolean.parseBoolean(args[5]));
			players_updated ++;
			repaint = players_updated == total_players;
			if(repaint)
				players_updated = 0;
		}else if(cmd.equals(KICK) && args.length >= 2){
			player.setLookRight(Boolean.parseBoolean(args[1]));
			int x = owner.onKick(player);
			int y = (int) player.getY();
			for( int[] loc : kickLocs){
				if(loc[0]==-1){
					loc[0]=x;
					loc[1]=y;
					loc[2]= player.isLookRight() ? 1 : 0;
					break;
				}
			}
			for(Player player1 : players){
				player1.onKick(player);
			}
			players_updated = 0;
			//We try to transfer the kick
			transfer(line);
			repaint = true;
		}
	}
	/**In fact it's a key input from {@link InteractivePanel#handleKey(int)}
	 * 
	 * @param key is the key code pressed but if it only represents a control if
	 *  not it's equal to 99, and if it requires a tick update it's equal to 100
	 * @param move is the new move of the player given by the X move and the Y move
	 *  but modified depending on the key pressed
	 */
	public void output(int key, int move){
		String line = MOVE+" "+move;
		switch(key){
		case InteractivePanel.SHOOT:
			line = KICK+" "+owner.isLookRight();
			int x= owner.onKick(owner);
			int y = (int) owner.getY();
			for(Player player : players){
				x = player.onKick(owner);
			}
			for( int[] loc : kickLocs){
				if(loc[0]==-1){
					loc[0]=x;
					loc[1]=y;
					loc[2]= owner.isLookRight() ? 1 : 0;
					break;
				}
			}
			send(line);
			return;
		case 100:
			tick();
			return;
		case 99:
			return;
		}
		owner.setMove(move);
		if(!serverSide)
			send(line);
	}
	/**Send this command to the server (client-side) or to all clients (server-side)
	 *  but the player concerned by this command is set in the method to the {@link #owner}
	 * 
	 * @param line is the command to send
	 */
	private void send(String line) {
		sender.send(line);
	}
	/**It's equal to {@link #send(String)} but we do not set the player concerned
	 * <p><b>NOTE : Only server side</b>
	 * @param line is the line to send to each client
	 */
	private void transfer(String line) {
		if(serverSide)
			((SmashServer)sender).transfer(line);
	}
	/**
	 * Send life and positions update of all players to all clients
	 * <p><b>NOTE : Only server side</b>
	 */
	private void tick(){
		if(serverSide){
			String line = "";
			owner.move();
			line+=owner.getName()+NAME_SEPARATOR+UPDATE+" "+owner.getX()+" "+owner.getY()+" "+owner.getLastMove()+
					" "+owner.getLife()+" "+owner.isAlive();
			for(Player player : players){
				player.setMap(map);
				player.move();
				line+=MORE+player.getName()+NAME_SEPARATOR+UPDATE+" "+player.getX()+" "+player.getY()+" "+
						player.getLastMove()+" "+player.getLife()+" "+player.isAlive();
			}
			transfer(line);
		}
	}

	/**Paint this game with the graphics object given
	 * <p><i>NOTE : We assume in this method that the graphic object is the graphic of a 1800 w x 980 h image</i>
	 * @param graphics is the graphic object to draw with
	 */
	public void paint(Graphics2D graphics){
		//We set the graphics parameters
		GraphicsConfiguration.configure(graphics);
		//We update our player number
		total_players = 1 +players.size();
		//First we draw the map
		graphics.drawImage(map.getBackground(), 0, 0, null);	
		//Now we draw the kicks
		for( int[] loc : kickLocs){
			if(loc[0]!=-1){
				graphics.drawImage(loc[2] == 1 ? imgR : imgL, loc[0]*Map.size, loc[1]*Map.size, null);
				loc[0]=-1;
			}
		}
		//We create our re-use variables
		String s = "";
		int index = 1;
		int width = 0;
		int font_height = 0;
		//Now we'll draw the name plate and skin of each players
		for(Player player : players){
			
			//Drawing the player on the game	
			graphics.drawImage(player.getImage(), (int)(player.getX()*Map.size), (int) (player.getY()*Map.size) , null);
			graphics.setFont(font);
			s= player.getName();
			width = graphics.getFontMetrics().stringWidth(s);
			//Drawing the background of the name
			font_height = graphics.getFontMetrics().getHeight();
			graphics.setColor(name_back);
			graphics.fillRoundRect((int)((player.getX()+0.5)*Map.size)-width/2-5,
						(int) (player.getY()*Map.size)-5-font_height, width+15, font_height, 15, 15);
			//Drawing the name
			graphics.setColor(SUIComponent.focused);
			graphics.drawString(s, (int)((player.getX()+0.5)*Map.size)-width/2,
					(int) (player.getY()*Map.size)-5-font_height/4);
			
			//Drawing on the bottom the name plate
			graphics.setFont(SUIComponent.normal.deriveFont(18));
			font_height = graphics.getFontMetrics().getHeight();
			//Drawing the background
			graphics.drawImage(player.getSkin().getPlate(), 450 *index, 900, 450, 80, null);
			//Drawing player's name and %
			s=player.getLife()+"%";		
			width = graphics.getFontMetrics().stringWidth(s);
			graphics.drawString(s, (int)(450*(index+0.5))-width/2+30, 940+font_height/4);
			
			index++;
		}
		
		//Now we draw the owner's stuff to be sure he's on the top
		//Drawing the player on the game	
		graphics.drawImage(owner.getImage(), (int)(owner.getX()*Map.size), (int) (owner.getY()*Map.size) , null);
		graphics.setFont(font);
		s= owner.getName();
		width = graphics.getFontMetrics().stringWidth(s);
		//Drawing the background of the name
		font_height = graphics.getFontMetrics().getHeight();
		graphics.setColor(name_back);
		graphics.fillRoundRect((int)((owner.getX()+0.5)*Map.size)-width/2-5,
					(int) (owner.getY()*Map.size)-5-font_height, width+15, font_height, 15, 15);
		//Drawing the name
		graphics.setColor(SUIComponent.focused);
		graphics.drawString(s, (int)((owner.getX()+0.5)*Map.size)-width/2,
				(int) (owner.getY()*Map.size)-5-font_height/4);
		
		//Drawing on the bottom the name plate
		graphics.setFont(SUIComponent.normal.deriveFont(18));
		font_height = graphics.getFontMetrics().getHeight();
		//Drawing the background
		graphics.drawImage(owner.getSkin().getPlate(), 0, 900, 450, 80, null);
		//Drawing player's name and %
		graphics.setColor(SUIComponent.focused);
		s= owner.getLife()+"%";		
		width = graphics.getFontMetrics().stringWidth(s);
		graphics.drawString(s, (int)(450*0.5)-width/2+30, 940+font_height/4);
	}
	/**Get if the game should be re-painted or not and if it should
	 *  set that it shouldn't, <i>assuming that if we know it should repaint we'll repaint it</i>
	 * 
	 * @return it the game should be painted
	 */
	public boolean shouldRepaint() {
		boolean should = repaint;
		repaint = false;
		return should;
	}

}
