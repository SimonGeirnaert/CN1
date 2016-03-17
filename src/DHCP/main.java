package DHCP;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DHCPClient client = new DHCPClient();
		client.getIP();
		client.renewLease();
		client.releaseIP();		
	}


}