package DHCP.Message;

import java.net.InetAddress;

import DHCP.Utilities;

/**
 * Class representing a DHCP offer message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPOfferMessage extends Message {
	
	/**
	 * Initialize the new DHCP offer message with given MAC address, transaction ID, 
	 * requested IP, server
	 * 
	 * @param xid
	 * 		  The transaction ID of the communication
	 * @param offerIP
	 * 		  The offered IP address
	 * @param macAddress
	 * 		  The MAC Address of the client.
	 * @param serverIP
	 * 		  The IP address of the server
	 * 
	 * @throws Exception 
	 *         When no IP addresses are available.
	 * 
	 * @effect The DHCP offer message is a message with standard fields and given MAC address, offerIP
	 * 		   serverIP and transaction ID and standard
	 * 		   options for an offer message.
	 */
	public DHCPOfferMessage(int xid, InetAddress offerIP, String macAddress, InetAddress serverIP) throws Exception {
		super(2,1,6,0, xid, 0, FLAGS0, InetAddress.getByName("0.0.0.0"), offerIP, serverIP, InetAddress.getByName("0.0.0.0"), macAddress, "", "", null);
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 2));
		Option option255 = new Option(255, new byte[0]);
		OptionsList optionsList = new OptionsList(option53, option255);
		this.setOptions(optionsList);
	}

}
