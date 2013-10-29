package org.smash.res;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ServersList {
	
	private ArrayList<String> servers = new ArrayList<String>();
	private static final File file = new File("./servers.list");
	
	public ServersList(){
		load();
	}

	private void load() {
		if(!file.exists())
			save();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String server = "";
			while((server = in.readLine()) != null)
				servers.add(server);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(servers.isEmpty())
			addServer("LocalHost","127.0.0.1");
	}

	public void save() {
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			PrintWriter out = new PrintWriter(file);
			for(String server : servers){
				out.println(server);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addServer(String s) {
		servers.add(s);
	}
	
	public void addServer(String name, String ip){
		servers.add(name+"@"+ip);
	}
	
	public void removeServer(int index){
		servers.remove(index);
	}

	public ArrayList<String> getServers() {
		return servers;
	}

	public void setServers(ArrayList<String> servers) {
		this.servers = servers;
	}
	
	public static String getServerIP(String server){
		return server.substring(server.indexOf("@")+1);
	}
	
	public static String getServerName(String server){
		return server.substring(0, server.indexOf("@"));
	}

}
