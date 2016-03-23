package DHCP;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import DHCP.Message.DHCPDiscoverMessage;
import DHCP.Message.DHCPReleaseMessage;
import DHCP.Message.DHCPRequestMessage;
import DHCP.Message.Message;

/**
 * Class representing a DHCP Client.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPClient extends DHCPHost {
	
	/**********************************************************
	 * Client MAC address
	 **********************************************************/
	
	/**
	 * Constant representing the MAC address of the client, set in the constructor
	 */
	private String macAddress = "";
	
	/**
	 * Return the MAC address of the client.
	 * 
	 * @return The MAC address of the client.
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Sets the MAC address of the client.
	 * @param macAddress
	 *        The MAC address to set.
	 */
	private void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

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
	 * @post The MAC address of the client is equal to the given MAC address.
	 */
	public DHCPClient(String macAddress){
		this.setMacAddress(macAddress);
		this.setCiaddr(null);
	}
	
	/**********************************************************
	 * Methods for client - server communication
	 **********************************************************/
	
	/**
	 * Gets an IP for the client. If the lease time for the IP address is expired, renewing the lease.
	 */
	public void getIP() throws IllegalArgumentException, SocketException, IOException{
		// Initialize connection sockets and settings
		DatagramSocket socket = new DatagramSocket();
//		UDPHost client = new UDPHost(InetAddress.getByName("10.33.14.246"), 1234); //Server KU Leuven
		UDPHost client = new UDPHost(InetAddress.getByName("localhost"), 1602);
		
		// Discover
		Message offer = DHCPDiscover(client, socket);
		if(Utilities.convertToInt(offer.getOptions().getOption(53).getContents()) == 2) {
			System.out.println("DHCPOFFER received.");
			System.out.println("- Suggested IP: " + offer.getYiaddr().toString());
		}
		else {
			System.out.println("No DHCPOFFER received. Restarting the configuration.");
			socket.close();
			getIP();
		}

		// Request
		Message acknowledge = DHCPRequest(offer.getXid(), offer.getYiaddr(), offer.getSiaddr(), client, socket);
		
		// Acknowledge received and waiting till lease expired to renew
		if(Utilities.convertToInt(acknowledge.getOptions().getOption(53).getContents()) == 5) {
			System.out.println("DHCPACK received.");
			setCiaddr(acknowledge.getYiaddr());
			System.out.println("SYSTEM IP SET TO " + getCiaddr().toString());
			int leaseTime = Utilities.convertToInt(acknowledge.getOptions().getOption(51).getContents());
			System.out.println("- Lease time: " + leaseTime + " seconds.");
			socket.close();
			long timeBeginLease = System.currentTimeMillis();
			while(System.currentTimeMillis() - timeBeginLease < 0.5*leaseTime*1000){}
			renewLease(acknowledge.getSiaddr());
		}
		
		// Negative acknowledge received, reconfiguration
		else {
			System.out.println("DCHPNAK received. Restarting the configuration.");
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
//		UDPHost client = new UDPHost(InetAddress.getByName("10.33.14.246"), 1234); //Server KU Leuven
		UDPHost client = new UDPHost(InetAddress.getByName("localhost"), 1602);
		
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
//		UDPHost client = new UDPHost(InetAddress.getByName("10.33.14.246"), 1234); //Server KU Leuven
		UDPHost client = new UDPHost(InetAddress.getByName("localhost"), 1602);
		
		Message ack = DHCPRequest(Utilities.generateXid(), getCiaddr(), siaddr, client, socket);
		setCiaddr(ack.getYiaddr());
		System.out.println("LEASE RENEWAL COMPLETE, SYSTEM IP SET TO "+ getCiaddr().toString());
		System.out.println("- Lease time: "+ Utilities.convertToInt(ack.getOptions().getOption(51).getContents())+ " seconds.");
		
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
	private Message DHCPDiscover(UDPHost client, DatagramSocket socket) throws IllegalArgumentException, SocketException, IOException {
		DHCPDiscoverMessage discoverMessage = new DHCPDiscoverMessage(getMacAddress());
		
		System.out.println("DHCPDISCOVER sent by " + this.getMacAddress() + ".");
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
	private Message DHCPRequest(int transactionID, InetAddress offeredAddress, InetAddress serverAddress, UDPHost client, DatagramSocket socket) throws SocketException, IOException{
		DHCPRequestMessage requestMessage = new DHCPRequestMessage(transactionID, getMacAddress(), offeredAddress, serverAddress);
		
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
	private void DHCPRelease(UDPHost client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException{
		DHCPReleaseMessage releaseMessage = new DHCPReleaseMessage(getMacAddress());
		sendUDPMessageWithoutResponse(releaseMessage, client, socket);
		this.setCiaddr(null);
	}
	
}
