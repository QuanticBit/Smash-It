package org.xor.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**An utility class, that provides useful network methods
 * 
 * @author LordOfBees
 * @version 1.00
 */
public final class NetworkUtils {
	/**Get the local address IP
	 * 
	 * @return a string representation of the local address IP
	 */
	public static String getIP(){
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
		return addr.getHostAddress();
	}
	/**Perform a ping by a system-dependent command
	 * 
	 * @param host is a string representation of the address IP you want to ping
	 * @return <b>true</b> if we successfully ping the IP; otherwise <b>false</b>
	 */
	public static boolean isReachableByPing(String host) {
	    try{
	         String cmd = "";
	         if(System.getProperty("os.name").startsWith("Windows")) {   
	        	 // For Windows
	        	 cmd = "ping -n 1 " + host;
	         } else {
	        	 // For Linux and OSX
	        	 cmd = "ping -c 1 " + host;
	         }
	         Process myProcess = Runtime.getRuntime().exec(cmd);
	         myProcess.waitFor();
	         return myProcess.exitValue() == 0;
	    } catch( Exception e ) {
	     	e.printStackTrace();
	     	return false;
	    }
	}
}
