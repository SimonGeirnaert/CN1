package DHCP;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Class representing a DHCP Server.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 *
 */
public class DHCPServer extends DHCP {
	
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
	 * Pool of IP addresses
	 **********************************************************/
	
	/**
	 * Variable representing the pool of IP addresses the server can issue.
	 */
	ArrayList<IPAddress> pool = new ArrayList<IPAddress>();
	
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
	 * @return The pool of IP addresses.
	 */
	public ArrayList<IPAddress> getPool() {
		return pool;
	}

//	/**
//	 * Sets the pool of IP addresses.
//	 * 
//	 * @param pool
//	 *        The pool of IP addresses to set.
//	 */
//	private void setPool(ArrayList<IPAddress> pool) {
//		this.pool = pool;
//	}
	
	/**
	 * Adds a given IP address to the pool of IP addresses.
	 * 
	 * @param ip
	 *        The IP to add to the pool.
	 */
	private void addToPool(InetAddress ip){
		getPool().add(new IPAddress(ip));
	}
	
	/**
	 * Checks if an IP address is in the pool and available.
	 * 
	 * @param ip
	 *        The IP to check
	 *        
	 * @return True if the IP is in the pool and is not yet in use.
	 */
	public boolean isInPool(InetAddress ip){
		for(int i =0; i<getPool().size(); i++){
			if(getPool().get(i).getIpAddr().equals(ip) && getPool().get(i).isLeased()==false)
				return true;
		}
		return false;
	}
	
	/**
	 * Gets an available IP address.
	 * 
	 * @return An available IP address.
	 * 
	 * @throws Exception
	 *         When no IP addresses are available.
	 */
	public InetAddress getAvailableAddress() throws Exception{
		for(int i=0; i<getPool().size(); i++){
			if(getPool().get(i).isLeased()==false) return
					getPool().get(i).getIpAddr();
		}
		throw new Exception("There are currently no IP addresses available.");
	}
	
	/**
	 * Creates a pool of IP addresses.
	 *        
	 * @throws UnknownHostException 
	 */
	private void createPool() throws UnknownHostException{
		for(int i = IP_FIRST; i <= IP_LAST; i++){
			addToPool(InetAddress.getByName(POOL_IP_PREFIX + i));
		}
	}
	
	/**
	 * Returns the IPAddress from the pool for the given InetAddress.
	 * 
	 * @param addr
	 *        The given InetAddress
	 *        
	 * @return The IPAddress if it is present in the pool, else null.
	 */
	private IPAddress getIPFromPool(InetAddress addr){
		for(int i=0; i<getPool().size(); i++){
			if(getPool().get(i).getIpAddr().equals(addr))
				return getPool().get(i);
		}
		return null;
	}
	
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
	public DHCPServer(InetAddress serverIP) throws UnknownHostException{
		setServerIP(serverIP);
		createPool();
	}
	
	/**********************************************************
	 * Server
	 **********************************************************/
	
	/**
	 * Simulates normal operation of the server.
	 */
	public void operate() throws Exception {
		UDP server = new UDP(InetAddress.getByName("localhost"), 0);
		DatagramSocket socket = new DatagramSocket(SERVER_PORT);
		ReceivedData rcvd = server.receiveData(socket);
		Message incomingMessage = Message.convertToMessage(rcvd.getData());
		server.setDestinationPort(rcvd.getPort());
		
		handleResponse(incomingMessage, server, socket);
		
		socket.close();
		operate();
	}
	
	
	private void handleResponse(Message response, UDP server, DatagramSocket socket) throws Exception{
		if(Utilities.convertToInt(response.getOptions().getOption(53).getContents()) == 1) {
			System.out.println("DHCPDISCOVER received.");
			InetAddress reqIP = InetAddress.getByAddress(response.getOptions().getOption(50).getContents());	
			handleResponse(DHCPOffer(response.getXid(), reqIP, response.getChaddr(), server, socket), server, socket);
		}
		else if(Utilities.convertToInt(response.getOptions().getOption(53).getContents()) == 3){
			System.out.println("DHCPREQUEST received.");
			InetAddress offeredIP = InetAddress.getByAddress(response.getOptions().getOption(50).getContents());
			if(isInPool(offeredIP) && getIPFromPool(offeredIP).isLeased()==false){
				getIPFromPool(offeredIP).setLeased(true);
				handleResponse(DHCPAck(response.getXid(), InetAddress.getByAddress(response.getOptions().getOption(50).getContents()), response.getChaddr(), server, socket), server, socket);
			}
			else{
				//DHCPNack();
			}
			
		}
		else{
			if(response.getYiaddr().equals(InetAddress.getByName("0.0.0.0"))) {
				System.out.println("DHCPRELEASE received.");
				operate();
			}
			// TODO MAC addr zal nog nodig zijn in IPAddress om bij te houden in pool
			
			else{
				System.out.println("Unknown message received. Ignoring message and resuming normal operation.");
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
	public InetAddress getOfferIP(InetAddress requestedIP) throws Exception{
		if(isInPool(requestedIP))
			return requestedIP;
		else return getAvailableAddress(); 
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
	 * @throws Exception 
	 */
	private Message DHCPOffer(int xid, InetAddress reqIP, String macAddr, UDP server, DatagramSocket socket) throws Exception {
		DHCPOfferMessage offerMessage = new DHCPOfferMessage(xid, reqIP, getServerIP(), macAddr, this);
		
		System.out.println("DHCPOFFER sent.");
		Message response = sendUDPMessage(offerMessage, server, socket);
		return response;
	}
	
	/**
	 * Sends a DHCPACK message to the client to confirm that the IP address is now leased to the client.
	 * 
	 * @param xid
	 * @param reqIP
	 * @param macAddr
	 * @param server
	 * @param socket
	 * @return
	 * @throws Exception
	 */
	private Message DHCPAck(int xid, InetAddress reqIP, String macAddr, UDP server, DatagramSocket socket) throws Exception {
		DHCPAckMessage ackMessage = new DHCPAckMessage(xid, reqIP, getServerIP(), macAddr);
		
		System.out.println("DHCPACK sent.");
		Message response = sendUDPMessage(ackMessage, server, socket);
		return response;
	}
	
	/**
	 * Sends a DHCPNACK message to the client to state that the IP address offered can not be leased.
	 * 
	 * @param xid
	 * @param reqIP
	 * @param macAddr
	 * @param server
	 * @param socket
	 * @return
	 * @throws Exception
	 */
//	private Message DHCPNack(int xid, InetAddress reqIP, String macAddr, UDP server, DatagramSocket socket) throws Exception {
//		DHCPNackMessage nackMessage = new DHCPNackMessage(xid, reqIP, getServerIP(), macAddr, this);
//		
//		System.out.println("DHCPNACK sent.");
//		Message response = sendUDPMessage(nackMessage, server, socket);
//		return response;
//	}
}
