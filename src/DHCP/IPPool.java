package DHCP;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Class representing a pool of IP address.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class IPPool {
	
	/**
	 * Variable representing a list of IP addresses.
	 */
	ArrayList<IPAddress> pool = new ArrayList<IPAddress>();

	/**
	 * Initialize the pool of IP addresses with given 
	 * IP prefix and begin IP suffix and end IP suffix.
	 * 
	 * @post The pool of IP addresses has (End suffix - begin suffix + 1)
	 * 		 IP addresses with given prefix.
	 */
	public IPPool(String IPPrefix, int beginIPSuffix, int endIPSuffix) throws UnknownHostException {
		for(int i = beginIPSuffix; i <= endIPSuffix; i++){
			addToPool(InetAddress.getByName(IPPrefix + i));
		}
	}
	
	/**
	 * Adds a given IP address to the pool of IP addresses.
	 * 
	 * @param ip
	 *        The IP to add to the pool.
	 */
	private void addToPool(InetAddress ip){
		this.getPool().add(new IPAddress(ip));
	}
	
	/**
	 * Return the pool of IP addresses.
	 * 
	 * @return The pool of IP addresses.
	 */
	public ArrayList<IPAddress> getPool() {
		return pool;
	}
	
	/**
	 * Gets an available IP address.
	 * 
	 * @return An available IP address.
	 * 
	 * @throws Exception
	 *         There is no IP address available.
	 */
	public InetAddress getAvailableAddress() throws Exception {
		for(int i=0; i<getPool().size(); i++){
			if(!getPool().get(i).isLeased())
					return getPool().get(i).getIpAddr();
		}
		throw new Exception("There are currently no IP addresses available.");
	}
	
	/**
	 * Checks if an IP address is in the pool and available.
	 * 
	 * @param ip
	 *        The IP to check
	 *        
	 * @return True if the IP is in the pool and is not yet in use.
	 */
	public boolean isInPoolAndAvailable(InetAddress ip){
		for(int i =0; i<getPool().size(); i++){
			if(getPool().get(i).getIpAddr().equals(ip) && !getPool().get(i).isLeased())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the IP address in use by the client with the give MAC address.
	 * 
	 * @param macAddress
	 *        The MAC address of the client of which the IP should be returned.
	 *        
	 * @return The IP address in use by the client with the give MAC address.
	 */
	public InetAddress getIPByMacAddr(String macAddress){
		for(int i=0; i<getPool().size(); i++){
			if(getPool().get(i).getMacAddr().equals(macAddress))
					return getPool().get(i).getIpAddr();
		}
		throw new IllegalArgumentException("The given MAC address has no active lease.");
	}
	
	/**
	 * Returns the IPAddress from the pool for the given InetAddress.
	 * 
	 * @param addr
	 *        The given InetAddress
	 *        
	 * @return The IPAddress if it is present in the pool, else null.
	 */
	public IPAddress getIPFromPool(InetAddress addr){
		for(int i=0; i<getPool().size(); i++){
			if(getPool().get(i).getIpAddr().equals(addr))
				return getPool().get(i);
		}
		return null;
	}
	
	/**
	 * Returns the IPAddress from the pool for the given InetAddress.
	 * 
	 * @param addr
	 *        The given InetAddress
	 *        
	 * @return The IPAddress if it is present in the pool, else null.
	 */
	public IPAddress getIPFromPoolByMacAddr(String macAddr){
		for(int i=0; i<getPool().size(); i++){
			if(getPool().get(i).getMacAddr().equals(macAddr))
				return getPool().get(i);
		}
		return null;
	}
}
