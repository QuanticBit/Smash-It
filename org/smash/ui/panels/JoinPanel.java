package org.smash.ui.panels;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.smash.SmashGame;
import org.smash.network.client.SmashClient;
import org.smash.res.ServersList;
import org.smash.ui.InteractivePanel;
import org.smash.ui.SFrame;
import org.smash.ui.components.SUIButton;
import org.smash.ui.components.SUIComponent;
import org.smash.ui.components.SUIList;
import org.xor.utils.NetworkUtils;

public class JoinPanel extends InteractivePanel {

	private static final long serialVersionUID = -8933034760626808022L;
	
	private SFrame frame;
	
	private SUIButton cancel;
	private SUIButton join;
	private SUIButton add;
	private SUIButton remove;
	private SUIList list;
	
	private ServersList servers;

	public JoinPanel(SFrame frame) {
		super();
		this.frame = frame;
		servers = new ServersList();
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(cancel==null)
			init(g);
		super.paintComponent(g);
		repaint(50);
	}

	private void init(Graphics g) {
		g.setFont(SUIComponent.normal);
		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<String> strings2 =servers.getServers();
		for( String server : strings2){
			strings.add(ServersList.getServerName(server)+"  -  "+ServersList.getServerIP(server));
		}
		list = new SUIList(getWidth()/2-(getWidth()/4), getHeight()/6, getWidth()/2, getHeight()/2, strings);
		join = new SUIButton("Join", 3*getWidth()/5-g.getFontMetrics().stringWidth("Join")/2, 9*(getHeight()/10));
		cancel = new SUIButton("Cancel", 4*getWidth()/5-g.getFontMetrics().stringWidth("Cancel")/2, 9*(getHeight()/10));
		add = new SUIButton("Add", getWidth()/5-g.getFontMetrics().stringWidth("Add")/2, 9*(getHeight()/10));
		remove = new SUIButton("Remove", 2*getWidth()/5-g.getFontMetrics().stringWidth("Remove")/2, 9*(getHeight()/10));
		addComponent(list);
		addComponent(cancel);
		addComponent(join);
		addComponent(add);
		addComponent(remove);
	}

	public void handleLeftClick(double x, double y, SUIComponent concerned){
		if(cancel.equals(concerned))
			frame.goTo(new PlayConfigPanel(frame));
		else if(join.equals(concerned) && list.getSelected()!=-1){
			String ip = ServersList.getServerIP(servers.getServers().get(list.getSelected()));
			if(!NetworkUtils.isReachableByPing(ip)){
				frame.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(frame, "The address \""+ip+"\" is not reachable. Please try another",
						"Unvalid IP !", JOptionPane.ERROR_MESSAGE);
				frame.setAlwaysOnTop(true);
				return;
			}
			JoinPlayPanel playPanel = new JoinPlayPanel(
					new SmashClient(ip, new SmashGame(frame.getProperties())), frame);
			frame.goTo(playPanel);
			playPanel.init();
		}
		else if(add.equals(concerned)){
			frame.setAlwaysOnTop(false);
			String s = JOptionPane.showInputDialog(frame, "Enter here the server name and its ip in the box below",
					"Name@IP");
			servers.addServer(s);
			servers.save();
			ArrayList<String> strings = new ArrayList<String>();
			ArrayList<String> strings2 =servers.getServers();
			for( String server : strings2){
				strings.add(ServersList.getServerName(server)+"  -  "+ServersList.getServerIP(server));
			}
			list.setStrings(getGraphics(), strings);
			frame.setAlwaysOnTop(true);
		}
		else if(remove.equals(concerned) && list.getSelected()!=-1){
			servers.removeServer(list.getSelected());
			servers.save();
			ArrayList<String> strings = new ArrayList<String>();
			ArrayList<String> strings2 =servers.getServers();
			for( String server : strings2){
				strings.add(ServersList.getServerName(server)+"  -  "+ServersList.getServerIP(server));
			}
			list.setStrings(getGraphics(), strings);
		}
		
	}
}
