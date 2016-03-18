package DHCP;

import java.net.InetAddress;

/**
 * Class representing an IP address for the server to issue.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 */
public class IPAddress {
	
	public IPAddress(InetAddress ip){
		setIpAddr(ip);
	}
	
	/**
	 * Variable representing the IP address.
	 */
	private InetAddress ipAddr = null;
	
	/**
	 * @return The IP address.
	 */
	public InetAddress getIpAddr() {
		return ipAddr;
	}

	/**
	 * Sets the IP address.
	 * 
	 * @param ipAddr
	 *        The IP to set.
	 */
	public void setIpAddr(InetAddress ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * Variable representing if an address is already in use by a client.
	 */
	private boolean leased = false;

	/**
	 * @return True if an IP address is already leased (in use by a client).
	 */
	public boolean isLeased() {
		return leased;
	}

	/**
	 * Sets the leased status of an IP address.
	 * 
	 * @param leased
	 *        The leased status to set.
	 */
	public void setLeased(boolean leased) {
		this.leased = leased;
	}

}
