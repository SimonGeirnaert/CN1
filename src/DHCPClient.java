import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Class representing a DHCP Client.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPClient {
	
	/**
	 * Constant representing the MAC address of the client
	 */
	private static final String MAC_ADDRESS = "KB58AS96QM91LS95";
	
	/**
	 * Constant representing the flag to receive the DHCPOFFER in broadcast
	 */
	private static final int FLAGS1 = 32768;
	
	/**
	 * Constant representing the flag to receive the DHCPOFFER in unicast
	 */
	private static final int FLAGS0 = 0;
	
	
	/**********************************************************
	 * Client IP
	 **********************************************************/
	
	
	/**
	 * Variable representing the client IP address
	 */
	private InetAddress ciaddr = null;
	
	/**
	 * @return The IP address of the DHCP client.
	 */
	public InetAddress getCiaddr() {
		return ciaddr;
	}

	/**
	 * Sets the IP address of the client.
	 * 
	 * @param ciaddr
	 *        The IP address to set.
	 */
	private void setCiaddr(InetAddress ciaddr) {
		this.ciaddr = ciaddr;
	}
	
	/**********************************************************
	 * Server IP
	 **********************************************************/
	
	
	/**
	 * Variable representing the server IP address that issued the IP of the client.
	 */
	private InetAddress siaddr = null;
	
	/**
	 * @return The IP address of the DHCP server that issued the IP of the client.
	 */
	public InetAddress getSiaddr() {
		return siaddr;
	}

	/**
	 * Sets the IP address of the DHCP server.
	 * 
	 * @param siaddr
	 *        The IP address to set.
	 */
	private void setSiaddr(InetAddress siaddr) {
		this.siaddr = siaddr;
	}
	
	
	/**********************************************************
	 * Lease time
	 **********************************************************/
	

	/**
	 * Variable representing the lease time (in seconds) issued by the DHCP server.
	 */
	private int leaseTime = 0;
	
	/**
	 * @return The lease time given by the server.
	 */
	public int getLeaseTime() {
		return leaseTime;
	}

	/**
	 * Sets the lease time given by the server.
	 * 
	 * @param leaseTime
	 *        The lease time to set.
	 */
	private void setLeaseTime(int leaseTime) {
		this.leaseTime = leaseTime;
	}
	
	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Constructor.
	 */
	public DHCPClient(){
		
	}
	
	/**********************************************************
	 * Methods for client - server communication
	 **********************************************************/
	
	/**
	 * Gets an IP for the client.
	 * 
	 * @throws IllegalArgumentException
	 * @throws SocketException
	 * @throws IOException
	 */
	public void getIP() throws IllegalArgumentException, SocketException, IOException{
		DatagramSocket socket = new DatagramSocket();
		UDPClient client = new UDPClient();
		
		Message offer = DHCPDiscover(client, socket);
		System.out.println("DHCPOFFER received.");
		System.out.println("- Suggested IP: " + offer.getYiaddr().toString());
		//System.out.println("- Length options DHCPOFFER: " + offer.getOptions().getNumberOfOptions());

		Message acknowledge = DHCPRequest(offer.getXid(), offer.getYiaddr(), offer.getSiaddr(), client, socket);
		System.out.println("DHCPACK received.");

		setCiaddr(acknowledge.getYiaddr());
		System.out.println("SYSTEM IP SET TO "+ getCiaddr().toString());
		setLeaseTime(Utilities.convertToInt(acknowledge.getOptions().getOption(51).getContents()));
		System.out.println("- Lease time: "+Utilities.convertToInt(acknowledge.getOptions().getOption(51).getContents())+" seconds.");
		setSiaddr(acknowledge.getSiaddr());
		
		socket.close();
	}
	
	/**
	 * Releases the IP address of the client.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void releaseIP() throws UnknownHostException, IOException {
		DatagramSocket socket = new DatagramSocket();
		UDPClient client = new UDPClient();
		
		DHCPRelease(client, socket);
		setCiaddr(null);
		System.out.println("IP RELEASED.");
		
		socket.close();
	}
	
	/**
	 * Renews the lease of the client
	 * 
	 * @return
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public void renewLease() throws SocketException, IOException{
		System.out.println("LEASE RENEWAL STARTED.");
		DatagramSocket socket = new DatagramSocket();
		UDPClient client = new UDPClient();
		
		Message ack = DHCPRequest(getCiaddr(), getSiaddr(), client, socket);
		setCiaddr(ack.getYiaddr());
		System.out.println("LEASE RENEWAL COMPLETE, SYSTEM IP SET TO "+ getCiaddr().toString());
		setLeaseTime(Utilities.convertToInt(ack.getOptions().getOption(51).getContents()));
		System.out.println("- Lease time: "+Utilities.convertToInt(ack.getOptions().getOption(51).getContents())+" seconds.");
		
		socket.close();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Message rebind(){
		return null;
	}
	
	/**
	 * Sends a DHCPDISCOVER and returns the answer from the server.
	 * 
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 * @return The message sent and the response of the server.
	 * 
	 * @throws IllegalArgumentException
	 * @throws SocketException
	 * @throws IOException
	 */
	private Message DHCPDiscover(UDPClient client, DatagramSocket socket) throws IllegalArgumentException, SocketException, IOException {
		
		InetAddress ipNull = InetAddress.getByName("192.172.16.250"); //geeft array met [0,0,0,0]
        
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 1));
		Option option50 = new Option(50, InetAddress.getByName("0.0.0.0").getAddress());
		Option option57 = new Option(57, Utilities.convertToByteArray(2, Message.MESSAGE_SIZE));
		Option option255 = new Option(255, new byte[0]);
		OptionsList optionsList = new OptionsList(option53, option50, option57, option255);
		
		System.out.println("DHCPDISCOVER sent.");
		Message response = sendUDPMessage(1,1,6,0, Utilities.generateXid(), 0, FLAGS1, ipNull, ipNull, ipNull, ipNull, MAC_ADDRESS, "", "", optionsList, client, socket);
		response = checkResponseOption(53, Utilities.convertToByteArray(1, 2), response, client, socket);
		return response;
	}
	
	/**
	 * Sends a DHCPREQUEST as a reply to a DHCPOFFER and returns the answer from the server.
	 * 
	 * @param serverXid
	 *        The transaction ID sent by the server in its DHCPOFFER message.
	 * @param offeredAddress
	 *        The IP address offered by the server in a DHCPOFFER message
	 * @param serverAddress
	 *        The IP address used by the server.
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The reply from the server.
	 * 
	 * @throws IOException 
	 * @throws SocketException 
	 */
	private Message DHCPRequest(int serverXid, InetAddress offeredAddress, InetAddress serverAddress, UDPClient client, DatagramSocket socket) throws SocketException, IOException{
		
		InetAddress ipNull = InetAddress.getByName("0.0.0.0"); //geeft array met [0,0,0,0]
		
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 3));
		Option option50 = new Option(50, offeredAddress.getAddress());
		Option option54 = new Option(54, serverAddress.getAddress());
		OptionsList optionsList = new OptionsList(option53, option50, option54);
		
		System.out.println("DHCPREQUEST sent to request IP " + offeredAddress.toString()+" at server " + serverAddress.toString());
		Message response = sendUDPMessage(1,1,6,0, serverXid, 0, FLAGS1, ipNull, ipNull, ipNull, ipNull, MAC_ADDRESS, "", "", optionsList, client, socket);
		response = checkResponseOption(53, Utilities.convertToByteArray(1, 5), response, client, socket);
		return response;
	}
	
	/**
	 * Sends a DHCPREQUEST to renew lease.
	 * 
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The reply from the server.
	 * 
	 * @throws IOException 
	 * @throws SocketException 
	 */
	private Message DHCPRequest(InetAddress currentAddress, InetAddress serverAddress, UDPClient client, DatagramSocket socket) throws SocketException, IOException{
		
		InetAddress ipNull = InetAddress.getByName("0.0.0.0"); //geeft array met [0,0,0,0]
		
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 3));
		Option option50 = new Option(50, currentAddress.getAddress());
		Option option54 = new Option(54, serverAddress.getAddress());
		OptionsList optionsList = new OptionsList(option53, option50, option54);
		
		System.out.println("DHCPREQUEST sent to request IP " + currentAddress.toString()+" at server " + serverAddress.toString());
		Message response = sendUDPMessage(1,1,6,0, Utilities.generateXid(), 0, FLAGS1, ipNull, ipNull, ipNull, ipNull, MAC_ADDRESS, "", "", optionsList, client, socket);
		response = checkResponseOption(53, Utilities.convertToByteArray(1, 5), response, client, socket);
		return response;
	}

	/**
	 * Sends a DHCPRELEASE message to the DHCP server.
	 * 
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws IOException
	 */
	private void DHCPRelease(UDPClient client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException{
		InetAddress ipNull = InetAddress.getByName("0.0.0.0"); //geeft array met [0,0,0,0]
		OptionsList options = new OptionsList();
		sendUDPMessageWithoutResponse(1,1,6,0, Utilities.generateXid(), 0, FLAGS0, getCiaddr(), ipNull, ipNull, ipNull, MAC_ADDRESS, "", "", options, client, socket);
	}
	
	/**
	 * Creates a UDP message in DHCP format with all given fields and sends it to the server.
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
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 * @return The answer from the server as a message.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws IOException
	 */
	private Message sendUDPMessage(int op, int htype, int hlen, int hops, int xid, int secs, int flags, InetAddress ciaddr, InetAddress yiaddr, InetAddress siaddr, InetAddress giaddr, String chaddr, String sname, String file, OptionsList options, UDPClient client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		Message m = new Message(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr, giaddr, chaddr, sname, file, options);
		Message response = Message.convertToMessage(client.sendData(m.convertToByteArray(), socket));
		return checkResponseXid(xid, response, client, socket);
	}
	
	/**
	 * Creates a UDP message in DHCP format with all given fields and sends it to the server.
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
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws IOException
	 */
	private void sendUDPMessageWithoutResponse(int op, int htype, int hlen, int hops, int xid, int secs, int flags, InetAddress ciaddr, InetAddress yiaddr, InetAddress siaddr, InetAddress giaddr, String chaddr, String sname, String file, OptionsList options, UDPClient client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		Message m = new Message(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr, giaddr, chaddr, sname, file, options);
		client.sendDataWithoutResponse(m.convertToByteArray(), socket);
	}
	
	/**
	 * Checks if the response received is the correct response. If not, waits for the correct response to return.
	 * 
	 * @param xid
	 *        The transaction ID of the message sent by the client to compare with the transaction ID of the received message.
	 * @param response
	 *        The response received from the server.
	 * @param client
	 *        The UDP client currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The correct reply from the server.
	 * 
	 * @throws UnknownHostException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private Message checkResponseXid(int xid, Message response, UDPClient client, DatagramSocket socket) throws UnknownHostException, UnsupportedEncodingException, IllegalArgumentException, IOException{
		if(xid==response.getXid()){
			System.out.println("- Response with matching Xid ("+xid+") received.");
			return response;
		}
		else{
			System.out.println("- Response did not have matching Xid. Awaiting correct response.");
			return checkResponseXid(xid, Message.convertToMessage(client.waitForAndReturnResponse(socket)), client, socket);
		}
	}
	
	/**
	 * Checks if a given message has certain option and if contents are equal to given contents. If not, waits for the correct response to return.
	 * 
	 * @param optionCode
	 *        The option code of the option to be compared.
	 * @param response
	 *        The response to check the options from.
	 * @param contents
	 *        The contents of compare the contents of the option to.
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The response of which the specified option contents match the given contents.
	 * 
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws UnsupportedEncodingException 
	 * @throws UnknownHostException 
	 */
	private Message checkResponseOption(int optionCode, byte[] contents, Message response, UDPClient client, DatagramSocket socket) throws UnknownHostException, UnsupportedEncodingException, IllegalArgumentException, IOException{
		Option responseOption = response.getOptions().getOption(optionCode);
		if(optionCode == responseOption.getOptionCode() && Arrays.equals(responseOption.getContents(), contents)){
			System.out.println("- Option "+optionCode+" equals the given contents.");
			return response;
		}
		else{
			System.out.println("- Option "+optionCode+" does not equal the given contents, awaiting correct response.");
			return checkResponseOption(optionCode, contents, Message.convertToMessage(client.waitForAndReturnResponse(socket)), client, socket);
		}
	}
}
