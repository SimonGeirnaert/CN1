package DHCP;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Class representing a DHCP Discover message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPDiscoverMessage extends Message {

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
	public DHCPDiscoverMessage(String macAddress) throws IllegalArgumentException, UnknownHostException {
		super(1,1,6,0, Utilities.generateXid(), 0, FLAGS1, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "", null);
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 1));
		Option option50 = new Option(50, InetAddress.getByName("0.0.0.0").getAddress());
		Option option57 = new Option(57, Utilities.convertToByteArray(2, Message.MESSAGE_SIZE));
		Option option255 = new Option(255, new byte[0]);
		OptionsList optionsList = new OptionsList(option53, option50, option57, option255);
		this.setOptions(optionsList);
	}
	
	

}
