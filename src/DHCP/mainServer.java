package DHCP;

import java.net.InetAddress;

public class mainServer {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DHCPServer server = new DHCPServer(InetAddress.getByName("localhost"));
		server.operate();

	}

}
