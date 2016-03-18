package DHCP;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP nack message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPNackMessage extends Message {
	
	/**
	 * Intialize the new DHCP nack message with given MAC address
	 * 
	 * @param macAddress
	 * 		  The MAC address of the client.
	 * @effect The DHCP nack message is a message with standard fields and no options.
	 */
	public DHCPNackMessage(int xid, String macAddress) throws IllegalArgumentException, UnknownHostException {
		super(1,1,6,0, xid, 0, FLAGS0, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "",  new OptionsList());
	}
}
