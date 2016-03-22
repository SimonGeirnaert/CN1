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
		while(true){
			server.checkPoolLeases();
		}
		//server.getPool().isInPoolAndAvailable(InetAddress.getByName("192.168.100.100"));
	}

}
