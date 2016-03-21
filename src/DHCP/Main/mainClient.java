package DHCP.Main;

import DHCP.DHCPClient;

public class mainClient {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// Two different clients after each other: same IP
		System.out.println("Case two seperate clients: second after release first.");
		DHCPClient client = new DHCPClient("JH57DF98RV15FH96");
		client.getIP();
		// client.renewLease(InetAddress.getByName("localhost"));
		client.releaseIP();	
		DHCPClient client2 = new DHCPClient("JH57DF98RV15FH95");
		client2.getIP();
		//client2.releaseIP();
		client2.getIP();
	}

}