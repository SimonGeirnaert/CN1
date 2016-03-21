package DHCP.Main;

import java.net.InetAddress;

import DHCP.DHCPClient;

public class mainClient {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DHCPClient client = new DHCPClient("JH57DF98RV15FH96");
		DHCPClient client2 = new DHCPClient("JH57DF98RV15FH95");
		client.getIP();
		client2.getIP();
		client.renewLease(InetAddress.getByName("localhost"));
		client.releaseIP();	
		client2.releaseIP();
	}

}