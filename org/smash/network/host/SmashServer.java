package org.smash.network.host;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.smash.Map;
import org.smash.SmashGame;
import org.smash.network.Sender;
import org.xor.utils.FileTools;

import static org.smash.network.GameNetwork.*;
import static org.smash.res.RessourcesCreator.TEMP_FOLDER_PATH;

public class SmashServer implements Sender{
	
	private int MAX_PLAYERS = 10;
	
	private int players = 1;
	private ServerSocket server;
	private SmashClientHost[] clients = new SmashClientHost[getMAX_PLAYERS()-1];
	private SmashServer instance;
	
	private SmashGame game;
	
	public SmashServer(SmashGame gamer, Map map){
		this.game=gamer;
		game.init(map, this);
		instance = this;
		try {
			setServer(new ServerSocket(328));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(getMAX_PLAYERS()==10)
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				while(players < getMAX_PLAYERS()){
					try {
						Socket socket = server.accept();
						socket.setKeepAlive(true);
						clients[players-1] = new SmashClientHost(socket, instance);
						sendMap(game.getMap().getPath(), players-1);
						players++;
						transfer(PLAYER_NUM+" "+players+" "+MAX_PLAYERS);
					} catch (IOException e) {
						break;
					}
					if(getMAX_PLAYERS() == 1)
						break;
				}
				if(getMAX_PLAYERS() == 1)
					start();
			}
		},"Smash It Server").start();
	}
	
	protected void sendMap(final File file,final int index) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				SmashClientHost client = clients[index];
				client.send(FILE_START_MAP);
				FileTools.sendFile(client.getOutput(), file, client.getBufferSize());
			}
		},"Smash It File Transfer - "+index).start();
	}
	
	protected void sendSkin(final File file,final int index, final String name) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				SmashClientHost client = clients[index];
				client.send(FILE_START_SKIN+name);
				FileTools.sendFile(client.getOutput(), file, client.getBufferSize());
			}
		},"Smash It File Transfer - "+index).start();
	}
	
	public void stop(){
		for(SmashClientHost client : clients){
			try {
				client.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
		try {
			this.server.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public void receive(SmashClientHost client, String line) {
		int index = 0;
		for(; index < players-1; index++){
			if(client.equals(clients[index]))
				break;
		}
		if(line.startsWith(FILE_START_SKIN)){
			String name = TEMP_FOLDER_PATH+client.getName()+".sskin";
			FileTools.saveFromInput(new File(name), client.getInput(), client.getBufferSize());
			game.addPlayer(new File(name), client.getName());
			transfer(PLAYER_NUM+" "+players+" "+MAX_PLAYERS);
		}else if(line.startsWith(USERNAME)){
			client.setName(line.substring(USERNAME.length()));
			transfer(PLAYER_NUM+" "+players+" "+MAX_PLAYERS);
		}else if(line.startsWith(READY)){
			client.setReady(true);
			boolean all = true;
			for(int i = 0; i < clients.length; i++){
				all = all && clients[i] != null	&& clients[i].isReady();
			}
			if(all)
				start();
		}else{
			game.input(client.getName()+NAME_SEPARATOR+line);
		}
	}

	private void start() {
		for(int i = 0; i < clients.length; i++){
			SmashClientHost client = clients[i];
			for (int j = 0; j< clients.length ; j++){
				if(j == i)
					continue;
				sendSkin(new File(TEMP_FOLDER_PATH+client.getName()+".sskin"), i, client.getName());
			}
			sendSkin(game.getOwner().getSkin().getPath(), i, game.getOwner().getName());
			client.send(READY);
		}
		game.start();
	}

	@Override
	public void send(String line) {
		for(SmashClientHost client : clients){
			if(client != null)
				client.send(game.getOwner().getName()+NAME_SEPARATOR+line);
		}
	}
	
	public void transfer(String line) {
		if(clients == null)
			return;
		for(SmashClientHost client : clients){
			if(client != null)
				client.send(line);
		}
	}

	public SmashGame getGame() {
		return game;
	}

	public void setMaxPlayers(int players) {
		this.MAX_PLAYERS = players;
		clients = new SmashClientHost[getMAX_PLAYERS()-1];
		if(getMAX_PLAYERS() == 1)
			start();
	}

	public int getMAX_PLAYERS() {
		return MAX_PLAYERS;
	}

}
