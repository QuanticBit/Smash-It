package org.smash.network.client;

import static org.smash.network.GameNetwork.*;
import static org.smash.res.RessourcesCreator.TEMP_FOLDER_PATH;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.smash.Map;
import org.smash.SmashGame;
import org.smash.network.Sender;
import org.xor.utils.FileTools;

public class SmashClient implements Sender{
	
	private DataOutputStream out;
	private DataInputStream in;
	private int bufferSize = 4096;
	private int player_connected = 2, max_players = 2;
	
	private Socket socket;
	private SmashGame game;
	
	private boolean quit = false;
	
	private String msg = "Connecting to the server...";
	
	public SmashClient(String ip, SmashGame game){
		setGame(game);
		msg = ip;
	}
	public void init(){
		if(quit || (socket != null && socket.isBound()))
			return;
		try {
			socket = new Socket(msg, 328);
			socket.setKeepAlive(true);
			socket.setSoTimeout(500);
			if(!socket.isConnected() || !socket.isBound()){
				msg = "Can't connect to "+msg;
				quit=true;
				return;
			}
			socket.setSoTimeout(0);
			bufferSize = socket.getReceiveBufferSize();
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (SocketException e) {
			msg = "Host is not responding";
			quit = true;
			e.printStackTrace();
			return;
		}catch (IOException e) {
			msg = "Fatal error has occured :"+e.getLocalizedMessage();
			quit = true;
			e.printStackTrace();
			return;
		}
		send(USERNAME+game.getOwner().getName());
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(socket == null){
					quit=true;
					msg = "Can't connect to "+socket.getInetAddress().getHostAddress();
					return;
				}
				try {
					while(!socket.isClosed() && !socket.isInputShutdown()){
						String line = in.readUTF();
						if(line!=null)
							receive(line);
					}
				}
				 catch (IOException e) {
					 msg = "Fatal error has occured :"+e.getLocalizedMessage();
					 e.printStackTrace();
					 quit=true;
					 return;
				}
			}
		},"Smash It Server Reader ("+msg+")").start();
		new Thread(new Runnable(){
			@Override
			public void run() {
				sendFile(getGame().getOwner().getSkin().getPath());
			}
		},"Smash It Skin Sender ("+msg+")").start();
		msg = "Connected !";
	}

	protected void receive(String line) {
		if(line.startsWith(FILE_START_MAP)){
			String name = TEMP_FOLDER_PATH+"map.smap";
			FileTools.saveFromInput(new File(name), in, bufferSize);
			Map map = new Map(new File(name));
			game.init(map, this);
			send(READY);
			msg = "Now waiting for other players to be ready";
		}
		else if(line.startsWith(FILE_START_SKIN)){
			String name = TEMP_FOLDER_PATH+line.substring(FILE_START_SKIN.length())+".sskin";
			FileTools.saveFromInput(new File(name), in, bufferSize);
			game.addPlayer(new File(name), line.substring(FILE_START_SKIN.length()));
		}else if(line.startsWith(READY)){
			game.start();
		}else if (line.startsWith(PLAYER_NUM)){
			player_connected = Integer.parseInt(line.split(" ")[1]);
			max_players = Integer.parseInt(line.split(" ")[2]);
			msg = "Currently "+player_connected+"/"+max_players+" players connected...";
		}else{
			if(line.contains(MORE)){
				String[] lines = line.split(MORE);
				for( String s : lines){
					game.input(s);
				}
			}else
				game.input(line);
		}
	}
	
	protected void sendFile(final File file) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				send(FILE_START_SKIN);
				FileTools.sendFile(out, file, bufferSize);
			}
		},"Smash It File Sender").start();
	}

	public void send(String line) {
		if(out == null){
			quit = true;
			msg = "Can't connect to "+socket.getInetAddress().getHostAddress();
			return;
		}
		try {
			out.writeUTF(line);
			out.flush();
		} catch (IOException e) {
			quit=true;
			msg = "Fatal error has occured :"+e.getLocalizedMessage();
			e.printStackTrace();
			return;
		}
	}

	public SmashGame getGame() {
		return game;
	}

	public void setGame(SmashGame game) {
		this.game = game;
	}

	public String getMessage() {
		return msg;
	}

	public boolean shouldQuit() {
		return quit;
	}
	public int getPlayer_number() {
		return player_connected;
	}
	public int getMax_players() {
		return max_players;
	}

}
