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
	 * Initialize the new IPAddress with given InetAddress.
	 * 
	 * @param ip
	 * 		  The InetAddress to initialize the new IPAddress with.
	 * @post The IP address is set to the given InetAddress.
	 * @post The lease expiration time is set to zero.
	 * @post The lease flag is set to false.
	 * @post The MAC address is empty.
	 */
	public IPAddress(InetAddress ip){
		this.setIpAddress(ip);
		this.setLeaseExpirationTime(0);
		this.setLeased(false);
		this.setMacAddress("");
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
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the IP address.
	 * 
	 * @param ipAddr
	 *        The IP to set.
	 */
	private void setIpAddress(InetAddress ipAddr) {
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
	void setLeased(boolean leased) {
		this.leased = leased;
	}

	/**
	 * Variable representing the hardware address of the client using this IP
	 */
	private String macAddress = "";

	/**
	 * @return The hardware address of the client using this IP.
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Sets the hardware address of the client using this IP.
	 * 
	 * @param macAddress
     *        The hardware address to set.
	 */
	void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	/**
	 * Variable representing the server time at which the lease of this IP expires.
	 */
	private long leaseExpirationTime = 0;

	/**
	 * Return the expiration time of the lease of this IP.
	 * 
	 * @return The server time at which the lease of this IP expires.
	 */
	public long getLeaseExpirationTime() {
		return leaseExpirationTime;
	}

	/**
	 * Sets the time at which the lease of this IP expires.
	 * 
	 * @param leaseExpirationTime
	 *        The expiration time to set.
	 */
	 public void setLeaseExpirationTime(long leaseExpirationTime) {
		this.leaseExpirationTime = leaseExpirationTime;
	}
	
	
	
}
