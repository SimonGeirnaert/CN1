package DHCP;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP release message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPReleaseMessage extends Message {
	
	/**
	 * Constant representing the flag to receive the DHCPOFFER in unicast
	 */
	private static final int FLAGS0 = 0;
	
	/**
	 * Intialize the new DHCP release message with given MAC address
	 * @throws UnknownHostException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	public DHCPReleaseMessage(String macAddress) throws IllegalArgumentException, UnknownHostException {
		super(1,1,6,0, Utilities.generateXid(), 0, FLAGS0, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "",  new OptionsList());
	}
}
