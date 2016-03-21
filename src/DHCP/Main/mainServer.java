package DHCP.Main;

import java.net.InetAddress;

import DHCP.DHCPServer;

public class mainServer {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DHCPServer server = new DHCPServer(InetAddress.getByName("localhost"), 10);
		server.operate();

	}

}
