package DHCP;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class representing a DHCP ack message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class DHCPAckMessage extends Message {
	
	/**
	 * Initialize a new DHCP ack message
	 * 
	 * @param xid
	 *        The transaction ID provided by the client
	 * @param assignedAddr
	 *        The address assigned to the client by the server
	 * @param serverIP
	 *        The IP of the server
	 * @param macAddress
	 *        The MAC address of the client
	 * @param leaseTime
	 *        The lease time provided by the server
	 *        
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 */
	public DHCPAckMessage(int xid, InetAddress assignedAddr, InetAddress serverIP, String macAddress, int leaseTime) throws IllegalArgumentException, UnknownHostException {
		super(2,1,6,0, xid, 0, FLAGS1, InetAddress.getByName("0.0.0.0"), assignedAddr, serverIP, InetAddress.getByName("0.0.0.0"), macAddress, "", "",  null);
		OptionsList options = new OptionsList(new Option(53, Utilities.convertToByteArray(1, 5)), new Option(51,Utilities.convertToByteArray(4, leaseTime)));
		this.setOptions(options);
	}
}
