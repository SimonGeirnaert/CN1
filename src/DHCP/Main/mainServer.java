package DHCP.Main;

import java.net.InetAddress;

import DHCP.DHCPServer;

public class mainServer {
	public static void main(String[] args) throws Exception {
		new DHCPServer(InetAddress.getByName("localhost"), 10);
	}
}
