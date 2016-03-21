package DHCP.Message;
import java.net.InetAddress;
import java.net.UnknownHostException;

import DHCP.Utilities;

/**
 * Class representing a DHCP request message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPRequestMessage extends Message {
	
	/**
	 * Initialize the new DHCP request message with given transaction ID, MAC address, offered address and server address.
	 * 
	 * @param transactionID
	 * 		  The transaction ID of the messages.
	 * @param macAddress
	 * 		  The MAC Address of the client.
	 * @param offeredAddress
	 * 		  The offered address, received from the server.
	 * @param serverAddress
	 * 	      The IP address of the server.
	 * @effect The DHCP request message is a message with standard fields, given transaction ID, MAC address,
	 * 		   offered address, server address and standard options for a request message.
	 */
	public DHCPRequestMessage(int transactionID, String macAddress, InetAddress offeredAddress, InetAddress serverAddress) throws IllegalArgumentException, UnknownHostException {
		super(1,1,6,0, transactionID, 0, FLAGS1, InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), InetAddress.getByName("0.0.0.0"), macAddress, "", "", null);
		Option option53 = new Option(53, Utilities.convertToByteArray(1, 3));
		Option option50 = new Option(50, offeredAddress.getAddress());
		Option option54 = new Option(54, serverAddress.getAddress());
		OptionsList optionsList = new OptionsList(option53, option50, option54);
		this.setOptions(optionsList);
	}
}
