package DHCP;
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
	
	
	/**********************************************************
	 * Client IP
	 **********************************************************/
	
	/**
	 * Variable representing the client IP address.
	 */
	private InetAddress ciaddr = null;
	
	/**
	 * Return the IP address of the DHCP client.
	 * 
	 * @return The IP address of the DHCP client.
	 */
	public InetAddress getCiaddr() {
		return ciaddr;
	}

	/**
	 * Sets the IP address of the client.
	 * 
	 * @param ciaddr
	 *        The new IP address.
	 */
	private void setCiaddr(InetAddress ciaddr) {
		this.ciaddr = ciaddr;
	}

	
	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Initialize the new DHCPClient
	 * 
	 * @post The client has no IP address.
	 */
	public DHCPClient(){
		this.setCiaddr(null);
	}
	
	/**********************************************************
	 * Methods for client - server communication
	 **********************************************************/
	
	/**
	 * Gets an IP for the client. If the lease time for the IP address is expired, renewing the lease.
	 * 
	 */
	public void getIP() throws IllegalArgumentException, SocketException, IOException{
		// Initialize connection sockets and settings
		DatagramSocket socket = new DatagramSocket();
		UDPClient client = new UDPClient();
		
		// Discover
		Message offer = DHCPDiscover(client, socket);
		System.out.println("DHCPOFFER received.");
		System.out.println("- Suggested IP: " + offer.getYiaddr().toString());

		// Request
		Message acknowledge = DHCPRequest(offer.getXid(), offer.getYiaddr(), offer.getSiaddr(), client, socket);
		
		// Acknowledge received and waiting till lease expired to renew
		if(Utilities.convertToInt(acknowledge.getOptions().getOption(53).getContents()) == 5) {
			System.out.println("DHCPACK received.");
		
			setCiaddr(acknowledge.getYiaddr());
			System.out.println("SYSTEM IP SET TO " + getCiaddr().toString());
//			int leaseTime = Utilities.convertToInt(acknowledge.getOptions().getOption(51).getContents());
			int leaseTime = 10; //to simulate lease time of 10 seconds
			System.out.println("- Lease time: " + leaseTime + " seconds.");
			socket.close();
			long timeBeginLease = System.currentTimeMillis();
			while(System.currentTimeMillis() - timeBeginLease < 0.5*leaseTime*1000){}
			renewLease(acknowledge.getSiaddr());
		}
		
		// Negative acknowledge received, reconfiguration
		else {
			System.out.println("DCHPNACK received. Restarting the configuration.");
			socket.close();
			getIP();
		}
	}
	
	/**
	 * Releases the IP address of the client.
	 * 
	 * @post The client has no IP address.
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
	 * Renews the lease of the client.
	 * 
	 * @post The IP address of the client is equal to the IP address before, with a new lease time period.
	 */
	public void renewLease(InetAddress siaddr) throws SocketException, IOException{
		System.out.println("LEASE RENEWAL STARTED.");
		DatagramSocket socket = new DatagramSocket();
		UDPClient client = new UDPClient();
		
		Message ack = DHCPRequest(Utilities.generateXid(), getCiaddr(), siaddr, client, socket);
		setCiaddr(ack.getYiaddr());
		System.out.println("LEASE RENEWAL COMPLETE, SYSTEM IP SET TO "+ getCiaddr().toString());
		System.out.println("- Lease time: "+Utilities.convertToInt(ack.getOptions().getOption(51).getContents())+" seconds.");
		
		socket.close();
	}
	
	
	/**
	 * Sends a DHCPDISCOVER and returns the answer from the server.
	 * 
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 * @return The response of the server.
	 */
	private Message DHCPDiscover(UDPClient client, DatagramSocket socket) throws IllegalArgumentException, SocketException, IOException {
		DHCPDiscoverMessage discoverMessage = new DHCPDiscoverMessage(MAC_ADDRESS);
		
		System.out.println("DHCPDISCOVER sent.");
		Message response = sendUDPMessage(discoverMessage, client, socket);
		return response;
	}
	
	/**
	 * Sends a DHCPREQUEST as a reply to a DHCPOFFER and returns the answer from the server.
	 * 
	 * @param transactionID
	 *        The transaction ID of the communication with the server.
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
	 */
	private Message DHCPRequest(int transactionID, InetAddress offeredAddress, InetAddress serverAddress, UDPClient client, DatagramSocket socket) throws SocketException, IOException{
		DHCPRequestMessage requestMessage = new DHCPRequestMessage(transactionID, MAC_ADDRESS, offeredAddress, serverAddress);
		
		System.out.println("DHCPREQUEST sent to request IP " + offeredAddress.toString()+" at server " + serverAddress.toString());
		Message response = sendUDPMessage(requestMessage, client, socket);
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
	 * @post  The client has no IP address.
	 */
	private void DHCPRelease(UDPClient client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException{
		DHCPReleaseMessage releaseMessage = new DHCPReleaseMessage(MAC_ADDRESS);
		sendUDPMessageWithoutResponse(releaseMessage, client, socket);
		this.setCiaddr(null);
	}
	
	/**
	 * Creates a UDP message in DHCP format with all given fields and sends it to the server.
	 * 
	 * @param message
	 * 		  The message to be sent.
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 * @return The answer from the server as a message.
	 */
	private Message sendUDPMessage(Message message, UDPClient client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		Message response = Message.convertToMessage(client.sendData(message.convertToByteArray(), socket));
		return waitForCorrectAnswer(message.getXid(), response, client, socket);
	}
	
	/**
	 * Creates a UDP message in DHCP format with all given fields and sends it to the server.
	 * 
	 * @param message
	 * 		  The message to be sent.
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 */
	private void sendUDPMessageWithoutResponse(Message message, UDPClient client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		client.sendDataWithoutResponse(message.convertToByteArray(), socket);
	}
	
	/**
	 * Checks if the response received is the correct response and returns the response message.
	 * If an incorrect response is received, wait for the correct response to return.
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
	 */
	private Message waitForCorrectAnswer(int xid, Message response, UDPClient client, DatagramSocket socket) throws UnknownHostException, UnsupportedEncodingException, IllegalArgumentException, IOException{
		if(isCorrectResponseMessage(xid, response)){
			System.out.println("- Response with matching Xid ("+xid+") received.");
			return response;
		}
		else{
			System.out.println("- Response did not have matching Xid. Awaiting correct response.");
			return waitForCorrectAnswer(xid, Message.convertToMessage(client.waitForAndReturnResponse(socket)), client, socket);
		}
	}
	
	/**
	 * Check if a response message is the answer to the sent message
	 * 
	 * @param xid
	 * 		  The transaction ID of the sent message.
	 * @param response
	 * 		  The response
	 * @return True if the given transaction ID is equal to the response transaction ID;
	 * 		   false otherwise.
	 */
	private boolean isCorrectResponseMessage(int xid, Message response) {
		if(xid == response.getXid())
			return true;
		else
			return false;
	}
}
