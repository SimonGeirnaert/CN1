package DHCP;

import java.net.InetAddress;

/**
 * Class representing a DHCP Discover message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPOfferMessage extends Message {
	
	/**
	 * Initialize the new DHCP offer message with given MAC address
	 * 
	 * @param macAddress
	 * 		  The MAC Address of the client.
	 * 
	 * @throws Exception 
	 *         When no IP addresses are available.
	 * 
	 * @effect The DHCP discover message is a message with standard fields and given MAC address and standard
	 * 		   options for a discover message.
	 */
	public DHCPOfferMessage(int xid, InetAddress requestedIP, InetAddress serverIP, String macAddress, DHCPServer server) throws Exception {
		super(2,1,6,0, xid, 0, FLAGS0, InetAddress.getByName("0.0.0.0"), server.getOfferIP(requestedIP), serverIP, InetAddress.getByName("0.0.0.0"), macAddress, "", "", null);
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 2));
		Option option255 = new Option(255, new byte[0]);
		OptionsList optionsList = new OptionsList(option53, option255);
		this.setOptions(optionsList);
	}

}
