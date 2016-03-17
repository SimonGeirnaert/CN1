import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP request message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPRequestMessage extends Message {

	/**
	 * Constant representing the flag to receive the DHCPOFFER in broadcast
	 */
	private static final int FLAGS1 = 32768;
	
	/**
	 * Intialize the new DHCP discover message with given MAC address
	 * 
	 * @param macAddress
	 * 		  The MAC Address of the client.
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 */
	public DHCPRequestMessage(int serverXid, String macAddress, InetAddress offeredAddress, InetAddress serverAddress) throws IllegalArgumentException, UnknownHostException {
		super(1,1,6,0, serverXid, 0, FLAGS1, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "", null);
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 3));
		Option option50 = new Option(50, offeredAddress.getAddress());
		Option option54 = new Option(54, serverAddress.getAddress());
		OptionsList optionsList = new OptionsList(option53, option50, option54);
		this.setOptions(optionsList);
	}
}
