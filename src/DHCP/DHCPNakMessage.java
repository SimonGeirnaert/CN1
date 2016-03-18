package DHCP;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP n&k message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPNakMessage extends Message {
	
	/**
	 * Initialize a new DHCP nak message with given transaction ID and MAC address
	 * 
	 * @param xid
	 *        The transaction ID to use
	 * @param macAddress
	 *        The MAC address of the receiver of the message
	 *        
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 */
	public DHCPNakMessage(int xid, String macAddress) throws IllegalArgumentException, UnknownHostException {
		super(2,1,6,0, xid, 0, FLAGS1, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "",  null);
		OptionsList options = new OptionsList(new Option(53, Utilities.convertToByteArray(1, 6)));
		this.setOptions(options);
	}
}
