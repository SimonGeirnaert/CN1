package DHCP;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import DHCP.Message.DHCPAckMessage;
import DHCP.Message.DHCPNakMessage;
import DHCP.Message.DHCPOfferMessage;
import DHCP.Message.Message;

/**
 * Class representing a DHCP Server.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 *
 */
public class DHCPServer extends DHCPHost implements Runnable {
	
	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Initialize a new DHCP server.
	 * 
	 * @param serverIP
	 *        The IP at which the server can be reached.
	 * @param leaseTime
	 * 		  The lease time (in seconds).
	 */
	public DHCPServer(InetAddress serverIP, int leaseTime) throws UnknownHostException{
		setServerIP(serverIP);
		setLeaseTime(leaseTime);
		this.pool = new IPPool(POOL_IP_PREFIX, IP_FIRST, IP_LAST);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/**********************************************************
	 * Server IP
	 **********************************************************/
	
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
	
	/**********************************************************
	 * Lease time
	 **********************************************************/
	
	/**
	 * Variable representing the lease time.
	 */
	private int leaseTime = 0;
	
	/**
	 * Return the lease time;
	 * 
	 * @return The server lease time.
	 */
	public int getLeaseTime() {
		return leaseTime;
	}

	/**
	 * Set the server lease time.
	 * 
	 * @param leaseTime
	 *        The lease time to set
	 * @pre   The given lease time has to be positive.
	 */
	private void setLeaseTime(int leaseTime) {
		this.leaseTime = leaseTime;
	}

	
	/**********************************************************
	 * Pool of IP addresses
	 **********************************************************/

	/**
	 * Variable representing the pool of IP addresses the server can issue.
	 */
	IPPool pool = null;
	
	/**
	 * Constant representing the first part of all pool IP addresses.
	 */
	private static final String POOL_IP_PREFIX = "192.168.100.";
	
	/**
	 * Constant representing the last part of the first IP in the pool.
	 */
	private static final int IP_FIRST = 100;
	
	/**
	 * Constant representing the last part of the last IP in the pool.
	 */
	private static final int IP_LAST = 200;
	
	/**
	 * Return the pool of IP addresses
	 * 
	 * @return The pool of IP addresses.
	 */
	public IPPool getPool() {
		return pool;
	}
	
	/**********************************************************
	 * Server
	 **********************************************************/
	
	/**
	 * Simulates normal operation of the server.
	 */
	public void operate() throws Exception {
		UDPHost server = new UDPHost(InetAddress.getByName("localhost"), 0);
		DatagramSocket socket = new DatagramSocket(1602);
		ReceivedData rcvd = server.receiveData(socket);
		Message incomingMessage = Message.convertToMessage(rcvd.getData());
		server.setDestinationPort(rcvd.getPort());
		
		handleResponse(incomingMessage, server, socket);
		
		socket.close();
		operate();
	}
	
	/**
	 * Handle a response of the client and return normal operation.
	 * 
	 * @param response
	 * 		  The response of the client.
	 * @param server
	 * 		  The UDP server.
	 * @param socket
	 * 		  The bidirectional connection socket;
	 */
	private void handleResponse(Message response, UDPHost server, DatagramSocket socket) throws Exception{
		try{
			// DHCPDiscover received
			if(Utilities.convertToInt(response.getOptions().getOption(53).getContents()) == 1) {
				System.out.println("DHCPDISCOVER received.");
				InetAddress requestedIP = InetAddress.getByAddress(response.getOptions().getOption(50).getContents());	
				// If the client has already an IP in use: don't answer
				if(this.clientHasAlreadyIP(response.getChaddr())) {
					System.out.println("Client has already IP; waiting for release.");
					socket.close();
					operate();
				}
				try {
					handleResponse(DHCPOffer(response.getXid(), requestedIP, response.getChaddr(), server, socket), server, socket);
				}
				catch(Exception ex) {
					// Do nothing
				}
			}
			// DHCPRequest received
			else if(Utilities.convertToInt(response.getOptions().getOption(53).getContents()) == 3){
				System.out.println("DHCPREQUEST received.");
				InetAddress offeredIP = InetAddress.getByAddress(response.getOptions().getOption(50).getContents());
				if((getPool().isInPoolAndAvailable(offeredIP) && !getPool().getIPFromPool(offeredIP).isLeased()) || (offeredIP.equals(getPool().getIPByMacAddress(response.getChaddr()).getIpAddress()))){
					getPool().getIPFromPool(offeredIP).setLeaseExpired(System.currentTimeMillis()+getLeaseTime()*1000);
					getPool().getIPFromPool(offeredIP).setLeased(true);
					getPool().getIPFromPool(offeredIP).setMacAddress(response.getChaddr());
					DHCPAck(response.getXid(), InetAddress.getByAddress(response.getOptions().getOption(50).getContents()), response.getChaddr(), server, socket);
					socket.close();
					operate();
				}
				else{
					DHCPNak(response.getXid(), response.getChaddr(), server, socket);
					socket.close();
					operate();
				}
			}
		} catch (IllegalArgumentException e) {
			if(response.getYiaddr().equals(InetAddress.getByName("0.0.0.0"))) {		
				System.out.println("DHCPRELEASE received.");
				getPool().getIPByMacAddress(response.getChaddr()).setLeased(false); //dont remove MAC addr from pool to make quick initialization possible
				getPool().getIPByMacAddress(response.getChaddr()).setLeaseExpired(System.currentTimeMillis());
				socket.close();
				operate();
			}
		
			else{
				System.out.println("Unknown message received. Ignoring message and resuming normal operation.");
				socket.close();
				operate();
			}
		}
	}
	
	private static final double PORTION_TIME_WAITING = 0.5;
	
	/**
	 * Returns the requested IP if available, returns a random available IP if the requested IP is not available.
	 * 
	 * @param requestedIP
	 *        The IP requested by the client.
	 *        
	 * @return The requested IP if available, returns a random available IP if the requested IP is not available.
	 * 
	 * @throws Exception
	 *         When no IP addresses are available.
	 */
	public InetAddress getOfferIP(InetAddress requestedIP) throws Exception {
		long timeBeginSearch = System.currentTimeMillis();
		InetAddress offerIP = null;
		while(System.currentTimeMillis() - timeBeginSearch < PORTION_TIME_WAITING*this.getLeaseTime() && offerIP == null){
			if(getPool().isInPoolAndAvailable(requestedIP))
				offerIP = requestedIP;
			else {
				try {
					offerIP = getPool().getAvailableAddress();
				}
				catch(Exception ex) {
					// Do nothing
				}
			}
		}
		if(offerIP == null)
			throw new Exception("No offer IP founded in time.");
		else
			return offerIP;
	}
	
	/**
	 * Creates and sends a DHCPOFFER message and returns the response.
	 * 
	 * @param xid
	 *        The transaction ID used by the client for DHCPDISCOVER.
	 * @param requestedIP
	 *        The requested IP by the client.
	 * @param macAddress
	 *        The MAC address of the client.
	 * @param server
	 *        The UDP connection currently in use by the server.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The response from the client.
	 */
	private Message DHCPOffer(int xid, InetAddress requestedIP, String macAddress, UDPHost server, DatagramSocket socket) throws Exception {
		InetAddress offerIP = this.getOfferIP(requestedIP);
		InetAddress serverIP = server.getReceiverIP();
		DHCPOfferMessage offerMessage = new DHCPOfferMessage(xid, offerIP, macAddress, serverIP);
			
		System.out.println("DHCPOFFER sent.");
		Message response = sendUDPMessage(offerMessage, server, socket);
		return response;
	}
	
	/**
	 * Sends a DHCPACK message to the client to confirm that 
	 * the IP address is now leased to the client.
	 * 
	 * @param xid
	 *        The transaction ID provided by the client.
	 * @param requestedIP
	 *        The IP offered by the server.
	 * @param macAddress
	 *        The MAC address of the client.
	 * @param server
	 *        The UDP connection currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 */
	private void DHCPAck(int xid, InetAddress requestedIP, String macAddress, UDPHost server, DatagramSocket socket) throws Exception {
		DHCPAckMessage ackMessage = new DHCPAckMessage(xid, requestedIP, this.getServerIP(), macAddress, this.getLeaseTime());
		System.out.println("DHCPACK sent.");
		sendUDPMessageWithoutResponse(ackMessage, server, socket);
	}
	
	/**
	 * Sends a DHCPNAK message to the client to state that the IP address offered can not be leased.
	 * 
	 * @param xid
	 *        The transaction ID provided by the client.
	 * @param macAddress
	 *        The MAC address of the client.
	 * @param server
	 *        The UDP connection currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 */
	private void DHCPNak(int xid, String macAddress, UDPHost server, DatagramSocket socket) throws Exception {
		DHCPNakMessage nakMessage = new DHCPNakMessage(xid, macAddress);
		System.out.println("DHCPNAK sent.");
		sendUDPMessageWithoutResponse(nakMessage, server, socket);
	}
	
	/**
	 * Check whether a client has already an IP address in use.
	 * 
	 * @param macAddress
	 * 	 	  The MAC address of the client to check.
	 * @return True if the client has already an IP address in use;
	 * 		   false otherwise.
	 */
	private boolean clientHasAlreadyIP(String macAddress) {
		try {
			this.getPool().getIPByMacAddress(macAddress);
			return true;
		} catch(IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * 
	 */
	public void run() {
		try {
			operate();
		} catch (Exception e) {
			System.out.println("Error occured in operation");
		}
	}
	
	/**
	 * 
	 */
	public void checkPoolLeases(){
		for(int i=0; i<getPool().getPool().size(); i++){
			if((getPool().getPool().get(i).getLeaseExpired() < System.currentTimeMillis()) && getPool().getPool().get(i).isLeased()){
				getPool().getPool().get(i).setLeased(false);
				System.out.println("Lease of client with MAC address " + getPool().getPool().get(i).getMacAddress() +" has expired.");
			}
		}
	}
}
