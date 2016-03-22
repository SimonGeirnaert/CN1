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
	ArrayList<IPAddress> ipPool = new ArrayList<IPAddress>();

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
		this.getIpPool().add(new IPAddress(ip));
	}
	
	/**
	 * Return the pool of IP addresses.
	 * 
	 * @return The pool of IP addresses.
	 */
	public ArrayList<IPAddress> getIpPool() {
		return ipPool;
	}
	
	/**
	 * Return the number of IP addresses in the maintained pool by the server.
	 * 
	 * @return The number of IP addresses in the pool.
	 */
	public int getNumberOfIPAddresses() {
		return this.getIpPool().size();
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
		for(int i=0; i<getIpPool().size(); i++){
			if(!getIpPool().get(i).isLeased())
					return getIpPool().get(i).getIpAddress();
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
		for(int i =0; i<getIpPool().size(); i++){
			if(getIpPool().get(i).getIpAddress().equals(ip) && !getIpPool().get(i).isLeased())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the IP address from the pool for the given InetAddress.
	 * 
	 * @param address
	 *        The given InetAddress
	 *        
	 * @return The IPAddress if it is present in the pool, else null.
	 */
	public IPAddress getIPFromPool(InetAddress address){
		for(int i=0; i<getIpPool().size(); i++){
			if(getIpPool().get(i).getIpAddress().equals(address))
				return getIpPool().get(i);
		}
		return null;
	}
	
	/**
	 * Returns the IP address from the pool for the given MAC address.
	 * 
	 * @param macAddress
	 *        The given MAC address of the client.
	 *        
	 * @return The IP address matching the given MAC address
	 * @throws IllegalArgumentException
	 * 		   The given MAC address has no active lease.
	 */
	public IPAddress getIPByMacAddress(String macAddress){
		for(int i=0; i<getIpPool().size(); i++){
			if(getIpPool().get(i).getMacAddress().equals(macAddress))
				return getIpPool().get(i);
		}
		throw new IllegalArgumentException("The given MAC address has no active lease.");
	}
	
	/**
	 * Checks all IP's in the pool for expired leases and changes the lease status if necessary.
	 */
	public void checkPoolLeases(){
		for(int i=0; i<this.getNumberOfIPAddresses(); i++){
			if((getIpPool().get(i).getLeaseExpirationTime() < System.currentTimeMillis()) && this.getIpPool().get(i).isLeased()){
				getIpPool().get(i).setLeased(false);
				System.out.println("Lease of client with MAC address " + this.getIpPool().get(i).getMacAddress() + " has expired.");
			}
		}
	}
	
	/**
	 * Return all leased addresses.
	 * 
	 * @return A list of leased IP addresses.
	 */
	public ArrayList<IPAddress> returnLeasedAddresses() {
		ArrayList<IPAddress> leasedAddresses = new ArrayList<IPAddress>();
		for(int i=0; i<this.getNumberOfIPAddresses(); i++) {
			IPAddress currentIP = getIpPool().get(i);
			if(currentIP.isLeased())
				leasedAddresses.add(currentIP);
		}
		return leasedAddresses;
	}
}
