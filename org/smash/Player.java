package org.smash;

import java.awt.image.BufferedImage;

import org.smash.res.Skin;
import org.xor.utils.GraphicsTools;

import static org.smash.network.GameNetwork.*;

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
	
	public Player(String name, Skin skin){
		setName(name);
		setSkin(skin);
	}
	
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
			setAlive(true);
			this.canMove = false;
			if(dead_tick>20)
				respawn();
			return GraphicsTools.rotate(img, isLookRight() ? -90 : 90);
		}else
			dead_tick = 0;
		
		return canMove ? img : GraphicsTools.invert(img);
	}
	
	private void respawn() {
		life = 0;
		setX(Map.width/2-1);
		setY(0);
		vectorX = 0;
		vectorY = 0;
		lastMove= NO_MOVE;
		move = NO_MOVE;
	}

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
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getWeapon() {
		return weapon;
	}

	public void setWeapon(int weapon) {
		this.weapon = weapon;
	}

	public int getMove() {
		return move;
	}

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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
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

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public boolean isLookRight() {
		return lookRight;
	}

	public void setLookRight(boolean lookRight) {
		this.lookRight = lookRight;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public double getVectorX() {
		return vectorX;
	}

	public void setVectorX(double vectorX) {
		this.vectorX = vectorX;
	}

	public double getVectorY() {
		return vectorY;
	}

	public void setVectorY(double vectorY) {
		this.vectorY = vectorY;
	}

	public void setLastMove(int parseInt) {
		move = NO_MOVE;
		canMove = true;
		this.lastMove = parseInt;
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

	public int getLastMove() {
		return lastMove;
	}

	public boolean canMove() {
		return canMove;
	}

}
