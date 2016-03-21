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
	 * Intialize the new DHCP release message with given MAC address
	 * 
	 * @param macAddress
	 * 		  The MAC address of the client.
	 * @effect The DHCP release message is a message with standard fields and no options.
	 */
	public DHCPReleaseMessage(String macAddress) throws IllegalArgumentException, UnknownHostException {
		super(1,1,6,0, Utilities.generateXid(), 0, FLAGS0, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "",  new OptionsList());
	}
}
