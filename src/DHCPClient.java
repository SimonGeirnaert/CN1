import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP Client.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPClient {
	
	/**
	 * Constant representing the MAC address of ...
	 */
	private static final String MAC_ADDRESS = "KD58AS96QM91LS95";
	
	/**
	 * Constant representing the flag to receive the DHCPOFFER in broadcast
	 */
	private static final int FLAGS1 = 32768;
	
	/**
	 * Constant representing the flag to receive the DHCPOFFER in unicast
	 */
	private static final int FLAGS0 = 0;
	
	/**
	 * Variable representing the client IP address
	 */
	private InetAddress ciaddr = null;
	
	/**
	 * Constructor.
	 */
	public DHCPClient(){
		
	}
	
	/**
	 * Gets an IP for the client.
	 * 
	 * @throws IllegalArgumentException
	 * @throws SocketException
	 * @throws IOException
	 */
	public void getIP() throws IllegalArgumentException, SocketException, IOException{
		DatagramSocket socket = new DatagramSocket();
		Message discoverResponse = DHCPDiscover(socket);
		System.out.println("Discover response received.");
		System.out.println("Suggested IP: " + discoverResponse.getYiaddr().toString());

		Message requestResponse = DHCPRequest(discoverResponse.getXid(), discoverResponse.getYiaddr(), discoverResponse.getSiaddr(), socket);
		System.out.println("Request response received.");
		OptionsList options = requestResponse.getOptions();
		System.out.println(options.getNumberOfOptions());
		
		socket.close();
	}
	
	/**
	 * Sends a DHCPDISCOVER and returns the answer from the server.
	 * 
	 * @return The message sent and the response of the server.
	 * 
	 * @throws IllegalArgumentException
	 * @throws SocketException
	 * @throws IOException
	 */
	public Message DHCPDiscover(DatagramSocket socket) throws IllegalArgumentException, SocketException, IOException {
		
		InetAddress ipNull = InetAddress.getByName("0.0.0.0"); //geeft array met [0,0,0,0]
        
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 1));
		Option option50 = new Option(50, InetAddress.getByName("0.0.0.0").getAddress());
		OptionsList optionsList = new OptionsList(option53, option50);
		
		byte[] responseArray = new byte[512];
		responseArray = sendUDPMessage(1,1,6,0, Utilities.generateXid(), 0, FLAGS1, ipNull, ipNull, ipNull, ipNull, MAC_ADDRESS, "", "", optionsList, socket);
		System.out.println("DHCPDISCOVER sent");
		return Message.convertToMessage(responseArray);
	}
	
	/**
	 * 
	 * @param serverXid
	 * @param socket
	 * @return
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public Message DHCPRequest(int serverXid, InetAddress offeredAddress, InetAddress serverAddress, DatagramSocket socket) throws SocketException, IOException{
		
		InetAddress ipNull = InetAddress.getByName("0.0.0.0"); //geeft array met [0,0,0,0]
		
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 3));
		Option option50 = new Option(50, offeredAddress.getAddress());
		Option option54 = new Option(54, serverAddress.getAddress());
		OptionsList optionsList = new OptionsList(option53, option50, option54);
		
		byte[] responseArray = new byte[512];
		responseArray = sendUDPMessage(1,1,6,0, serverXid, 0, FLAGS1, ipNull, ipNull, ipNull, ipNull, MAC_ADDRESS, "", "", optionsList, socket);
		System.out.println("DHCPREQUEST sent to request IP " + offeredAddress.toString()+" at server " + serverAddress.toString());
		return Message.convertToMessage(responseArray);	
	}

	/**
	 * Creates a UDP message in DHCP format with all given fields
	 * 
	 * @param op
	 *        The opcode for the message.
	 * @param htype
	 *        The hardware type for the message.
	 * @param hlen
	 *        The hardware address length for the message.
	 * @param hops
	 *        The hops for the message.
	 * @param xid
	 *        The transaction ID for the message.
	 * @param secs
	 *        The seconds passed for the message.
	 * @param flags
	 *        The flags for the message.
	 * @param ciaddr
	 *        The client IP address for the message.
	 * @param yiaddr
	 *        Your IP address for the message.
	 * @param siaddr
	 *        The server IP address for the message.
	 * @param giaddr
	 *        The gateway IP address for the message.
	 * @param chaddr
	 *        The client hardware address for the message.
	 * @param sname
	 *        The server name for the message.
	 * @param file
	 *        The file for the message.
	 * @param cookie
	 *        The cookie for the message.
	 * @param options
	 *        The options for the message.
	 * 
	 * @return The answer from the server as a byte array.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws IOException
	 */
	private byte[] sendUDPMessage(int op, int htype, int hlen, int hops, int xid, int secs, int flags, InetAddress ciaddr, InetAddress yiaddr, InetAddress siaddr, InetAddress giaddr, String chaddr, String sname, String file, OptionsList options, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		UDPClient client = new UDPClient();
		Message m = new Message(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr, giaddr, chaddr, sname, file, options);
		return client.sendData(m.convertToByteArray(), socket);
	}
}
