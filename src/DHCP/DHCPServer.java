package DHCP;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP Server.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 *
 */
public class DHCPServer extends DHCP {
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void server() throws Exception {
		UDP server = new UDP(InetAddress.getByName("localhost"), 0);
		DatagramSocket socket = new DatagramSocket(SERVER_PORT);
		
		ReceivedData rcvd = server.receiveData(socket);
		Message incomingMessage = Message.convertToMessage(rcvd.getData());
		server.setDestinationPort(rcvd.getPort());
		
		if(Utilities.convertToInt(incomingMessage.getOptions().getOption(53).getContents()) == 1) {
			System.out.println("DHCPDISCOVER received.");
			//DHCPOffer(int xid, InetAddress reqIP, String macAddr, UDP server, DatagramSocket socket);
			InetAddress reqIP = InetAddress.getByAddress(incomingMessage.getOptions().getOption(50).getContents());
					
			DHCPOffer(incomingMessage.getXid(), reqIP, incomingMessage.getChaddr(), server, socket);
		}
		else if(Utilities.convertToInt(incomingMessage.getOptions().getOption(53).getContents()) == 3){
			System.out.println("DHCPREQUEST received.");
			
		}

	}
	
	/**
	 * Variable representing the IP of the server.
	 */
	InetAddress serverIP = null;

	/**
	 * @return The server IP address.
	 */
	public InetAddress getServerIP() {
		return serverIP;
	}

	/**
	 * Sets the server IP address.
	 * 
	 * @param serverIP
	 *        The IP address to set.
	 */
	private void setServerIP(InetAddress serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * Initialize a new DHCP server.
	 * 
	 * @param serverIP
	 *        The IP at which the server can be reached.
	 */
	public DHCPServer(InetAddress serverIP){
		setServerIP(serverIP);
	}
	
	/**
	 * Returns the requested IP if available, returns a random available IP if the requested IP is not available.
	 * 
	 * @param requestedIP
	 *        The IP requested by the client.
	 *        
	 * @return The requested IP if available, returns a random available IP if the requested IP is not available.
	 * @throws UnknownHostException 
	 */
	public InetAddress getOfferIP(InetAddress requestedIP) throws UnknownHostException{
		return InetAddress.getByName("192.168.110.6"); // TODO obviously
	}
	
	/**
	 * Creates and sends a DHCPOFFER message and returns the response.
	 * 
	 * @param xid
	 *        The transaction ID used by the client for DHCPDISCOVER.
	 * @param reqIP
	 *        The requested IP by the client.
	 * @param macAddr
	 *        The MAC address of the client.
	 * @param server
	 *        The UDP connection currently in use by the server.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The response from the client.
	 * 
	 * @throws IllegalArgumentException
	 * @throws SocketException
	 * @throws IOException
	 */
	private Message DHCPOffer(int xid, InetAddress reqIP, String macAddr, UDP server, DatagramSocket socket) throws IllegalArgumentException, SocketException, IOException {
		DHCPOfferMessage offerMessage = new DHCPOfferMessage(xid, reqIP, getServerIP(), macAddr, this);
		
		System.out.println("DHCPOFFER sent.");
		Message response = sendUDPMessage(offerMessage, server, socket);
		return response;
	}
}
