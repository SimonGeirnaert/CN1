package DHCP;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP Server.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 *
 */
public class DHCPServer extends DHCP {
	
	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Initialize a new DHCP server.
	 * 
	 * @param serverIP
	 *        The IP at which the server can be reached.
	 *        
	 * @throws UnknownHostException 
	 */
	public DHCPServer(InetAddress serverIP, int lease) throws UnknownHostException{
		setServerIP(serverIP);
		setLeaseTime(lease);
		this.pool = new IPPool(POOL_IP_PREFIX, IP_FIRST, IP_LAST);
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
		UDP server = new UDP(InetAddress.getByName("localhost"), 0);
		DatagramSocket socket = new DatagramSocket(1602);
		ReceivedData rcvd = server.receiveData(socket);
		Message incomingMessage = Message.convertToMessage(rcvd.getData());
		server.setDestinationPort(rcvd.getPort());
		
		handleResponse(incomingMessage, server, socket);
		
		socket.close();
		operate();
	}
	
	
	private void handleResponse(Message response, UDP server, DatagramSocket socket) throws Exception{
		try{
			if(Utilities.convertToInt(response.getOptions().getOption(53).getContents()) == 1) {
				System.out.println("DHCPDISCOVER received.");
				InetAddress reqIP = InetAddress.getByAddress(response.getOptions().getOption(50).getContents());	
				handleResponse(DHCPOffer(response.getXid(), reqIP, response.getChaddr(), server, socket), server, socket);
			}
			else if(Utilities.convertToInt(response.getOptions().getOption(53).getContents()) == 3){
				System.out.println("DHCPREQUEST received.");
				InetAddress offeredIP = InetAddress.getByAddress(response.getOptions().getOption(50).getContents());
				if((getPool().isInPoolAndAvailable(offeredIP) && getPool().getIPFromPool(offeredIP).isLeased()==false) || (offeredIP.equals(getPool().getIPByMacAddr(response.getChaddr())))){
					getPool().getIPFromPool(offeredIP).setLeased(true);
					getPool().getIPFromPool(offeredIP).setMacAddr(response.getChaddr());
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
				getPool().getIPFromPoolByMacAddr(response.getChaddr()).setLeased(false); //dont remove MAC addr from pool to make quick initialization possible
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
		if(getPool().isInPoolAndAvailable(requestedIP))
			return requestedIP;
		else return getPool().getAvailableAddress(); 
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
	private Message DHCPOffer(int xid, InetAddress requestedIP, String macAddress, UDP server, DatagramSocket socket) {
		try {
			InetAddress offerIP = this.getOfferIP(requestedIP);
			InetAddress serverIP = server.getReceiverIP();
			DHCPOfferMessage offerMessage = new DHCPOfferMessage(xid, offerIP, macAddress, serverIP);
			
			System.out.println("DHCPOFFER sent.");
			Message response = sendUDPMessage(offerMessage, server, socket);
			return response;
		}
		catch (Exception excep) {
			return DHCPOffer(xid, requestedIP, macAddress, server, socket);
		}
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
	private void DHCPAck(int xid, InetAddress requestedIP, String macAddress, UDP server, DatagramSocket socket) throws Exception {
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
	private void DHCPNak(int xid, String macAddress, UDP server, DatagramSocket socket) throws Exception {
		DHCPNakMessage nakMessage = new DHCPNakMessage(xid, macAddress);
		System.out.println("DHCPNAK sent.");
		sendUDPMessageWithoutResponse(nakMessage, server, socket);
	}
}
