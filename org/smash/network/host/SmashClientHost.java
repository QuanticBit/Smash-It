package org.smash.network.host;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SmashClientHost {
	
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	
	private SmashServer server;
	private SmashClientHost client;
	private int bufferSize = 4096;
	
	private String name = "Unknown";
	
	private boolean ready = false;
	
	public SmashClientHost(Socket socket, SmashServer instance){
		this.setSocket(socket);
		client = this;
		server = instance;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			bufferSize = socket.getReceiveBufferSize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while(!getSocket().isClosed() && !getSocket().isInputShutdown()){
						String line = in.readUTF();
						if(line!=null)
							server.receive(client, line);
						Thread.yield();
					}
				}
				 catch (IOException e) {
					 return;
				}
			}
		},"Smash It Client Reader ("+socket.getInetAddress().getHostAddress()+")").start();
	}
	
	public void send(String s){
		try {
			out.writeUTF(s);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public boolean equals(Object o){
		if(o instanceof SmashClientHost)
			return ((SmashClientHost)o).getSocket().getInetAddress().equals(getSocket().getInetAddress());
		return false;
	}

	public DataOutputStream getOutput() {
		return out;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public DataInputStream getInput() {
		return in;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
