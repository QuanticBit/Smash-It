package org.smash;

import java.awt.image.BufferedImage;

import org.smash.res.Skin;
import org.xor.utils.GraphicsTools;

import static org.smash.network.GameNetwork.*;
/**
 * Represents a Smash player
 */
public class Player {
	
	private static final int xMax = Map.width-1;
	private static final int yMax = Map.height-1;
	
	private String name = "Unknown";
	private Skin skin;
	
	private boolean alive = true;
	private int  life = 0;
	private int move = 0;
	private int lastMove = 0;
	
	private int weapon = 1;
	
	private double x = 19;
	private double y = 0;
	
	private double vectorX = 0;
	private double vectorY = 0;
	
	private boolean lookRight = true;
	private boolean canMove = true;
	
	private int dead_tick = 0;
	
	private Map map;
	/**
	 * Create a new player with the specified name and skin
	 * @param name is the name of the Player
	 * @param skin is the skin of the player
	 */
	public Player(String name, Skin skin){
		setName(name);
		setSkin(skin);
	}
	/**Get the current image of the player and update its state
	 * 
	 * @return an image of the player
	 */
	public BufferedImage getImage(){
		boolean canMove = this.canMove;
		this.canMove = true;
		BufferedImage img = null;
		if(lastMove == NO_MOVE)
			img = isLookRight() ? skin.getStandR() : skin.getStandL();
		else if(lastMove > MOVE_RIGHT)
			img = isLookRight() ? skin.getJumpR() : skin.getJumpL();
		else
			img = isLookRight() ? skin.getWalkR() : skin.getWalkL();
		if(!isAlive()){
			dead_tick++;
			this.canMove = false;
			//1 tick == 150 ms so >=20 == >=3 s  
			if(dead_tick>=20)
				respawn();
			return GraphicsTools.rotate(img, isLookRight() ? -90 : 90);
		}else
			dead_tick = 0;
		
		return canMove ? img : GraphicsTools.invert(img);
	}
	/**
	 * Respawn the player
	 */
	private void respawn() {
		setAlive(true);
		life = 0;
		setX(Map.width/2-1);
		setY(0);
		vectorX = 0;
		vectorY = 0;
		lastMove= NO_MOVE;
		move = NO_MOVE;
	}
	/**Fired every time a player kick somewhere this check if the player is concerned and "apply" the kick if he's concerned
	 * @param p is the player that kicks
	 */
	public int onKick(Player p){
		if(p.getY()!=getY() || !p.isAlive() || !isAlive())
			return -1;
		if(p.isLookRight() && p.getX()+1==getX()){
			vectorX += 1;
			int num = (int) (Math.random()*99)+1;
			life ++;
			if(num<=life)
				setAlive(false);
			canMove = false;
			move = NO_MOVE;
		}
		else if(!p.isLookRight() && p.getX()-1==getX()){
			vectorX -= 1;
			life++;
			int num = (int) (Math.random()*99)+1;
			life ++;
			if(num<=life)
				setAlive(false);
			canMove = false;
			move = NO_MOVE;
		}
		return (int) (p.isLookRight() ? p.getX()+1 : p.getX()-1);
	}
	
	/**
	 * Get the name of the player
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set the name of the player
	 * @param name is the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Get the skin of the player
	 * @return the skin of the player
	 */
	public Skin getSkin() {
		return skin;
	}
	/**
	 * Set the skin of the player
	 * @param skin is the new skin
	 */
	public void setSkin(Skin skin) {
		this.skin = skin;
	}
	/**
	 * Get wether or not this player is alive
	 * @return if the player is alive
	 */
	public boolean isAlive() {
		return alive;
	}
	/**Set wether or not the player is alive
	 * @param alive is the new state of the player
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	/**
	 * Not implemented yet
	 */
	public int getWeapon() {
		return weapon;
	}
	/**
	 * Not implemented yet
	 */
	public void setWeapon(int weapon) {
		this.weapon = weapon;
	}
	/**
	 * Get the current move direction of the player
	 * <p>The move direction is not where the player will move but where he wants to
	 * @return an integer merge of {@link GameNetwork}
	 */
	public int getMove() {
		return move;
	}
	/**
	 * Set the new move direction of the player to the given one
	 * <p>The move direction is not where the player will move but where he wants to
	 * @param move is the new move direction
	 */
	public void setMove(int move) {
		//If we can't move then we souldn't change the move 
		if(!canMove)
			return;
		//For we do not update their move, we should perform a check
		if(this.move == MOVE_DOWN){
			switch(move){
			case MOVE_LEFT+MOVE_UP:
				this.move += MOVE_LEFT;
				break;
			case MOVE_RIGHT+MOVE_UP:
				this.move += MOVE_RIGHT;
				break;
			case MOVE_LEFT+MOVE_DOWN:
				this.move = move;
				break;
			case MOVE_RIGHT+MOVE_DOWN:
				this.move = move;
				break;
			case MOVE_LEFT:
				this.move += MOVE_LEFT;
				break;
			case MOVE_RIGHT:
				this.move += MOVE_RIGHT;
				break;
			}
		}
		else
			this.move = move;
	}
	/**
	 * Get the x coordinates of the player on the map
	 * @return the horizontal coordinates
	 */
	public double getX() {
		return x;
	}
	/**
	 * Set the x coordinates of the player on the map
	 * @param x is the new horizontal coordinates
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * Get the y coordinates of the player on the map
	 * @return the vertical coordinates
	 */
	public double getY() {
		return y;
	}
	/**
	 * Set the y coordinates of the player on the map
	 * @param y is the new vertical coordinates
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * Perform the move action for this player or if you prefer Update the player's position
	 */
	public void move(){
		double posX = x;
		double posY = y;
		boolean onGround = Math.floor(y)+1<=yMax && !map.isFree((int)x, (int)Math.floor(y)+1);
		if((posX < 0 || posX > xMax) || posY > yMax){
			setAlive(false);
			return;
		}
		switch(move){
		case MOVE_LEFT+MOVE_UP:
			if(onGround)
				vectorY-=5;
			vectorX-=1;
			setLookRight(false);
			break;
		case MOVE_RIGHT+MOVE_UP:
			if(onGround)
				vectorY-=5;
			vectorX+=1;
			setLookRight(true);
			break;
		case MOVE_LEFT+MOVE_DOWN:
			vectorY = Math.min(vectorY+0.5, 3);
			vectorX-=1;
			setLookRight(false);
			break;
		case MOVE_RIGHT+MOVE_DOWN:
			vectorY = Math.min(vectorY+0.5, 3);
			vectorX+=1;
			setLookRight(true);
			break;
		case MOVE_DOWN:
			vectorY = Math.min(vectorY+0.5, 3);
			break;
		case MOVE_UP:
			if(onGround)
				vectorY-=5;
			break;
		case MOVE_LEFT:
			vectorX-=1;
			setLookRight(false);
			break;
		case MOVE_RIGHT:
			vectorX+=1;
			setLookRight(true);
			break;
		}
		lastMove= move;
		posX = getX()+vectorX;
		posY = getY()+vectorY;
		boolean right = false;
		boolean down = false;
		
		//PART Y - 1
		if(y < posY && Math.floor(y)+1<=yMax && map.isFree((int)x, (int)Math.floor(y)+1)){
			y++;
			down =true;
			if(x < posX && Math.floor(x)+1<=xMax && map.isFree((int)Math.floor(x)+1, (int)y)){
				x++;
				right = true;
			}
			else if((x < posX && Math.floor(x)+1>= xMax)){
				setAlive(false);
			}
			else if(x < posX && Math.floor(x)+1<=xMax && !map.isFree((int)Math.floor(x)+1,(int) y))
				vectorX = 0;
			if(x > posX && Math.floor(x)-1>= 0 && map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				x--;
			else if(x > posX && Math.floor(x)-1< 0  && !right){
				setAlive(false);
			}
			else if(x > posX && Math.floor(x)-1>= 0 && !map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				vectorX = 0;
		}
		if(y < posY && Math.floor(y)+1>yMax)
			setAlive(false);
		else if(y < posY && Math.floor(y)+1<=yMax && !map.isFree((int)x, (int)Math.floor(y)+1))
			vectorY = 0;
		
		//PART Y - 2
		if(y > posY && Math.floor(y)-1>= 0 && map.isFree((int)x,(int) Math.floor(y)-1) && !down){
			y--;
			if(x < posX && Math.floor(x)+1<=xMax && map.isFree((int)Math.floor(x)+1, (int)y)){
				x++;
				right = true;
			}
			else if((x < posX && Math.floor(x)+1>= xMax)){
				setAlive(false);
			}
			else if(x < posX && Math.floor(x)+1<=xMax && !map.isFree((int)Math.floor(x)+1,(int) y))
				vectorX = 0;
			if(x > posX && Math.floor(x)-1>= 0 && map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				x--;
			else if(x > posX && Math.floor(x)-1< 0  && !right){
				setAlive(false);
			}
			else if(x > posX && Math.floor(x)-1>= 0 && !map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				vectorX = 0;
		}
		if(y > posY && Math.floor(y)-1< 0)
			setAlive(false);
		else if(y > posY && Math.floor(y)-1>= 0 && !map.isFree((int)x, (int)Math.floor(y)-1) && !down)
			vectorY = 0;
		
		if(y < posY && Math.floor(y)+1<=yMax && map.isFree((int)x, (int)Math.floor(y)+1) && vectorY > 2){
			y++;
			down =true;
			if(x < posX && Math.floor(x)+1<=xMax && map.isFree((int)Math.floor(x)+1, (int)y)){
				x++;
				right = true;
			}
			else if((x < posX && Math.floor(x)+1>= xMax)){
				setAlive(false);
			}
			else if(x < posX && Math.floor(x)+1<=xMax && !map.isFree((int)Math.floor(x)+1,(int) y))
				vectorX = 0;
			if(x > posX && Math.floor(x)-1>= 0 && map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				x--;
			else if(x > posX && Math.floor(x)-1< 0  && !right){
				setAlive(false);
			}
			else if(x > posX && Math.floor(x)-1>= 0 && !map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				vectorX = 0;
		}
		if(y < posY && Math.floor(y)+1>yMax)
			setAlive(false);
		else if(y < posY && Math.floor(y)+1<=yMax && !map.isFree((int)x, (int)Math.floor(y)+1))
			vectorY = 0;
		
		//PART Y - 2
		if(y > posY && Math.floor(y)-1>= 0 && map.isFree((int)x,(int) Math.floor(y)-1) && !down && vectorY < -2){
			y--;
			if(x < posX && Math.floor(x)+1<=xMax && map.isFree((int)Math.floor(x)+1, (int)y)){
				x++;
				right = true;
			}
			else if((x < posX && Math.floor(x)+1>= xMax)){
				setAlive(false);
			}
			else if(x < posX && Math.floor(x)+1<=xMax && !map.isFree((int)Math.floor(x)+1,(int) y))
				vectorX = 0;
			if(x > posX && Math.floor(x)-1>= 0 && map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				x--;
			else if(x > posX && Math.floor(x)-1< 0  && !right){
				setAlive(false);
			}
			else if(x > posX && Math.floor(x)-1>= 0 && !map.isFree((int)Math.floor(x)-1,(int) y) && !right)
				vectorX = 0;
		}
		if(y > posY && Math.floor(y)-1< 0)
			setAlive(false);
		else if(y > posY && Math.floor(y)-1>= 0 && !map.isFree((int)x, (int)Math.floor(y)-1) && !down)
			vectorY = 0;
		
		//PART X - 1
		if(x < posX && Math.floor(x)+1<=xMax && map.isFree((int)Math.floor(x)+1, (int)y)){
			x++;
			right = true;
			if(y < posY && Math.floor(y)+1<=yMax && map.isFree((int)x, (int)Math.floor(y)+1)){
				y++;
				down =true;
			}
			else if(y < posY && Math.floor(y)+1>yMax){
				setAlive(false);
			}
			else if(y < posY && Math.floor(y)+1<=yMax && !map.isFree((int)x, (int)Math.floor(y)+1))
				vectorY = 0;
			if(y > posY && Math.floor(y)-1>= 0 && map.isFree((int)x, (int)Math.floor(y)-1) && !down)
				y--;
			else if(y > posY && Math.floor(y)-1>= 0 && !map.isFree((int)x, (int)Math.floor(y)-1) && !down)
				vectorY = 0;
		}
		if(x < posX && Math.floor(x)+1>= xMax)
			setAlive(false);
		if(x < posX && Math.floor(x)+1<=xMax && !map.isFree((int)Math.floor(x)+1,(int) y))
			vectorX = 0;
		
		//PART X - 2
		if(x > posX && Math.floor(x)-1>= 0 && map.isFree((int)Math.floor(x)-1,(int) y) && !right){
			x--;
			if(y < posY && Math.floor(y)+1<=yMax && map.isFree((int)x, (int)Math.floor(y)+1)){
				y++;
				down =true;
			}
			else if(y < posY && Math.floor(y)+1>yMax){
				setAlive(false);
			}
			else if(y < posY && Math.floor(y)+1<=yMax && !map.isFree((int)x, (int)Math.floor(y)+1))
				vectorY = 0;
			if(y > posY && Math.floor(y)-1>= 0 && map.isFree((int)x, (int)Math.floor(y)-1) && !down)
				y--;
			else if(y > posY && Math.floor(y)-1>= 0 && !map.isFree((int)x, (int)Math.floor(y)-1) && !down)
				vectorY = 0;
		}
		if(x > posX && Math.floor(x)-1< 0 && !right)
			setAlive(false);
		if(x > posX && Math.floor(x)-1>= 0 && !map.isFree((int)Math.floor(x)-1,(int) y) && !right)
			vectorX = 0;

		//PART 3
		vectorX*=0.5;
		vectorY*=0.5;
		if(vectorX >= -0.5 && vectorX <= 0.5)
			vectorX = 0;
		if(vectorY >= -0.5 && vectorY <= 0.5)
			vectorY = 0;
		if((x < 0 || x > xMax) || y > yMax){
			setAlive(false);
			return;
		}
		move = NO_MOVE;
		if(Math.floor(y)+1>yMax || (map != null && map.isFree((int) x, (int)Math.floor(y)+1)))
			move = MOVE_DOWN;
	}
	/**
	 * Get the map the player is currently playing one
	 * @return the current map or <b>null</b> if it wasn't set
	 */
	public Map getMap() {
		return map;
	}
	/**
	 * Set the map the player is currently playing one
	 * @param map is the new map
	 */
	public void setMap(Map map) {
		this.map = map;
	}
	/**
	 *@return <b>true</b> if he's looking right and <b>false</b> if he's looking to the left
	 * Get whether the player is looking to the right or to the left
	 **/
	public boolean isLookRight() {
		return lookRight;
	}
	/**
	 * Set whether the player is looking to the right or to the left
	 * @param lookRight : <b>true</b> if he's looking right and <b>false</b> if he's looking to the left
	 */
	public void setLookRight(boolean lookRight) {
		this.lookRight = lookRight;
	}
	/**Get the current life of the player
	 * <p>NOTE : the life is a percentage of luck to be killed by the next attack
	 * @return the current life of the player
	 */
	public int getLife() {
		return life;
	}
	/**
	 * Set the current life of the player
	 * <p>NOTE : the life is a percentage of luck to be killed by the next attack
	 * @param life is the new life of the player
	 */
	public void setLife(int life) {
		this.life = life;
	}
	/**Set the last move direction and update player's direction ( right or left )
	 * @param lastMove is the new last move direction
	 */
	public void setLastMove(int lastMove) {
		move = NO_MOVE;
		canMove = true;
		this.lastMove = lastMove;
		switch(lastMove){
		case MOVE_LEFT+MOVE_UP:
			setLookRight(false);
			break;
		case MOVE_RIGHT+MOVE_UP:
			setLookRight(true);
			break;
		case MOVE_LEFT+MOVE_DOWN:
			setLookRight(false);
			break;
		case MOVE_RIGHT+MOVE_DOWN:
			setLookRight(true);
			break;
		case MOVE_LEFT:
			setLookRight(false);
			break;
		case MOVE_RIGHT:
			setLookRight(true);
			break;
		}
	}
	/**
	 * Get the last move the player performed
	 * @return his last move direction
	 */
	public int getLastMove() {
		return lastMove;
	}
	/**
	 * Get whether or not the player can choose the move direction
	 * @return if the player can choose hois move direction or not
	 */
	public boolean canMove() {
		return canMove;
	}

}
