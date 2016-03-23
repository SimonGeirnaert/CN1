package DHCP.Main;

import DHCP.DHCPClient;

public class mainClient {
	public static void main(String[] args) throws Exception {
		// Two different clients after each other: same IP
		DHCPClient client = new DHCPClient("SG18SK12LD25BW01");
		DHCPClient client2 = new DHCPClient("JH57DF98RV15FH95");
		
		client.getIP();
		System.out.println("");
		client2.getIP();
		client.releaseIP();	
		client2.getIP();
	}
}
