package DHCP;

import java.net.InetAddress;

/**
 * Class representing an IP address for the server to issue and keeps track of the MAC address using it.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 */
public class IPAddress {
	
	/**
	 * Initialize the new IPAddress with given InetAddress
	 * 
	 * @param ip
	 * 		  The InetAddress to initialize the new IPAddress with.
	 */
	public IPAddress(InetAddress ip){
		setIpAddr(ip);
	}
	
	/**
	 * Variable representing the IP address.
	 */
	private InetAddress ipAddress = null;
	
	/**
	 * Return the InetAddress of this IPAddress.
	 * 
	 * @return The IP address.
	 */
	public InetAddress getIpAddr() {
		return ipAddress;
	}

	/**
	 * Sets the IP address.
	 * 
	 * @param ipAddr
	 *        The IP to set.
	 */
	private void setIpAddr(InetAddress ipAddr) {
		this.ipAddress = ipAddr;
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

	/**
	 * Variable representing the hardware address of the client using this IP
	 */
	private String macAddress = "";

	/**
	 * @return The hardware address of the client using this IP.
	 */
	public String getMacAddr() {
		return macAddress;
	}

	/**
	 * Sets the hardware address of the client using this IP.
	 * 
	 * @param macAddress
     *        The hardware address to set.
	 */
	public void setMacAddr(String macAddress) {
		this.macAddress = macAddress;
	}
	
	
}
