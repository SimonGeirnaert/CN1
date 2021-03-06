package DHCP.Message;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import DHCP.Utilities;

/**
 * A class representing a DHCP message.
 * 
 * @version 1.0 - 2016
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 */
public class Message {
	
	/**
	 * Constant representing the flag to receive the reply in broadcast
	 */
	protected static final int FLAGS1 = 32768;
	
	/**
	 * Constant representing the flag to receive the reply in unicast
	 */
	protected static final int FLAGS0 = 0;
	
	/**
	 * Variable referencing the size of a message.
	 */
	protected static final int MESSAGE_SIZE = 576;
	
	
	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Initialize the new message with given opcode, hardware type, hardware address length, number of hops, transactionID, number of seconds, flags, client IP address,
	 * your IP address, the server IP address, the relay agent IP address, the client hardware address, the server name, the file, and the options.
	 * 
	 * @param op
	 * 		  The opcode of the message
	 * @param htype
	 * 		  The hardware type of the message
	 * @param hlen
	 * 		  The hardware address length of the message
	 * @param hops
	 * 		  The number of hops of the message
	 * @param xid
	 * 	      The transaction ID of the message
	 * @param secs
	 * 	      The number of seconds of the message
	 * @param flags
	 * 		  The flags of the message
	 * @param ciaddr
	 * 		  The client IP address
	 * @param yiaddr
	 * 		  Your IP address
	 * @param siaddr
	 * 		  The sever IP address
	 * @param giaddr
	 * 		  The relay agent IP address
	 * @param chaddr
	 * 		  The client hardware address
	 * @param sname
	 * 		  The server name
	 * @param file
	 * 		  The file of the message
	 * @param options
	 * 		  The options of the message
	 */
	public Message(int op, int htype, int hlen, int hops, int xid, int secs, int flags, InetAddress ciaddr, InetAddress yiaddr, InetAddress siaddr, InetAddress giaddr, String chaddr, String sname, String file, OptionsList options) throws IllegalArgumentException {
		setOp(op);
		setHtype(htype);
		setHlen(hlen);
		setHops(hops);
		setXid(xid);
		setSecs(secs);
		setFlags(flags);
		setCiaddr(ciaddr);
		setYiaddr(yiaddr);
		setSiaddr(siaddr);
		setGiaddr(giaddr);
		setChaddr(chaddr);
		setSname(sname);
		setFile(file);
		setOptions(options);
	}
	
	/**********************************************************
	 * OpCode
	 **********************************************************/
	
	/**
	 * Variable referencing the opcode of the message.
	 */
	private int op = 1;
	
	/**
	 * Returns the opcode of the message.
	 * 
	 * @return opcode
	 * 		   The opcode of the message
	 */
	public int getOp() {
		return op;
	}
	
	/**
	 * Sets the opcode.
	 * 
	 * @param op
	 * 		  The opcode to set.
	 * @throws IllegalArgumentException
	 * 		   The opcode is not equal to 1 or 2.
	 */
	protected void setOp(int op) throws IllegalArgumentException {
		if(op < 1 || op > 2) 
			throw new IllegalArgumentException("The opcode has to be 1 or 2."); 
		else 
			this.op = op;
	}

	/**********************************************************
	 * Hardware Address Type
	 **********************************************************/
	
	/**
	 * Variable referencing the hardware address type of the message.
	 */
	private int htype = 1;
	
	/**
	 * Returns the hardware address type.
	 * 
	 * @return The hardware address type
	 */
	public int getHtype() {
		return htype;
	}
	
	/**
	 * Sets the hardware address type.
	 * 
	 * @param htype
	 * 		  The new hardware address type
	 * @throws IllegalArgumentException
	 * 		   The htype is specified illegally.
	 */
	protected void setHtype(int htype) throws IllegalArgumentException {
		if(htype < 1 || htype > 20 || (htype > 1 && htype < 6) || (htype > 7 && htype < 11) || htype == 13) 
			throw new IllegalArgumentException("Htype should be specified right");
		else 
			this.htype = htype;
	}
	
	/**********************************************************
	 * Hardware Address Length
	 **********************************************************/
	
	/**
	 * Variable referencing the hardware address length of the message.
	 */
	private int hlen = 6;
	
	/**
	 * Returns the hardware address length.
	 * 
	 * @return The hardware address length
	 */
	public int getHlen() {
		return hlen;
	}
	
	/**
	 * Sets the hardware address length.
	 * 
	 * @param hlen
	 * 		  The new hardware address length.
	 */
	protected void setHlen(int hlen) {
			this.hlen = hlen;
	}
	
	/**********************************************************
	 * Hops
	 **********************************************************/
	
	/**
	 * Variable referencing the number of hops of the message.
	 */
	private int hops = 0;
	
	/**
	 * Returns the number of hops of the message.
	 * 
	 * @return The number of hops.
	 */
	public int getHops() {
		return hops;
	}
	
	/**
	 * Sets the number of hops.
	 * 
	 * @param hops
	 * 		  The new number of hops.
	 */
	protected void setHops(int hops) {
		this.hops = hops;
	}
	
	/**********************************************************
	 * Transaction ID
	 **********************************************************/
	
	/**
	 * Variable referencing the transaction ID of the message.
	 */
	private int xid = 0;
	
	/**
	 * Returns the transaction ID.
	 * 
	 * @return The transaction ID of the message.
	 */
	public int getXid() {
		return xid;
	}
	
	/**
	 * Sets the transaction ID.
	 * 	
	 * @param xid
	 * 		  The new transaction ID of the message.
	 */
	protected void setXid(int xid) {
		this.xid = xid;
	}
	
	/**********************************************************
	 * Seconds
	 **********************************************************/
	
	/**
	 * Variable referencing the number of seconds of the message.
	 */
	private int secs = 0;
	
	/**
	 * Returns the number of seconds passed since client began address acquisition or renewal process.
	 * 
	 * @return Number of seconds passed
	 */
	public int getSecs() {
		return secs;
	}
	/**
	 * Sets the number of seconds passed since client began address acquisition or renewal process.
	 * 
	 * @param secs
	 * 		  The new number of seconds.
	 * @throws IllegalArgumentException
	 * 		   The number of seconds must be positive.
	 */
	protected void setSecs(int secs) throws IllegalArgumentException {
		if(secs < 0) 
			throw new IllegalArgumentException("Message can't travel back in time: secs must be positive.");
		else 
			this.secs = secs;
	}
	
	/**********************************************************
	 * Flags
	 **********************************************************/
	
	/**
	 * Variable referencing the flags of the message.
	 */
	private int flags = 0;

	/**
	 * Return the flags of the message.
	 * 
	 * @return The flags of the message.
	 */
	public int getFlags() {
		return flags;
	}
	
	/**
	 * Set the flags of the message.
	 * 
	 * @param flags
	 * 		  The new flags of the message
	 */
	protected void setFlags(int flags) {
		this.flags = flags;
	}
	
	/**********************************************************
	 * Client IP Address
	 **********************************************************/
	
	/**
	 * Variable referencing the client IP address of the message.
	 */
	private InetAddress ciaddr = null;
	
	/**
	 * Return the client IP address of the message.
	 * 
	 * @return The client IP address
	 */
	public InetAddress getCiaddr() {
		return ciaddr;
	}
	
	/**
	 * Set the client IP address of the message.
	 * 
	 * @param ciaddr
	 * 		  The new client IP address.
	 */
	protected void setCiaddr(InetAddress ciaddr) {
		this.ciaddr = ciaddr;
	}
	
	/**********************************************************
	 * Your IP Address
	 **********************************************************/
	
	/**
	 * Variable referencing your IP address of the message.
	 */
	private InetAddress yiaddr = null;
	
	/**
	 * Get IP address of creator of the message ('your' address).
	 * 
	 * @return Your IP address
	 */
	public InetAddress getYiaddr() {
		return yiaddr;
	}
	
	/**
	 * Set IP address of creator of the message ('your' address).
	 * 
	 * @param yiaddr
	 * 		  New your IP address
	 */
	protected void setYiaddr(InetAddress yiaddr) {
		this.yiaddr = yiaddr;
	}
	
	/**********************************************************
	 * Server IP Address
	 **********************************************************/

	/**
	 * Variable referencing the server IP address of the message.
	 */
	private InetAddress siaddr = null;
	
	/**
	 * Returns IP address of next server to use in bootstrap returned in DHCPOFFER, DHCPACK by server.
	 * 
	 * @return Server IP Address
	 */
	public InetAddress getSiaddr() {
		return siaddr;
	}
	
	/**
	 * Sets IP address of next server to use in bootstrap returned in DHCPOFFER, DHCPACK by server.
	 * 
	 * @param siaddr
	 * 		  The new server IP address
	 */
	protected void setSiaddr(InetAddress siaddr) {
		this.siaddr = siaddr;
	}
	
	/**********************************************************
	 * Gateway IP Address
	 **********************************************************/
	
	/**
	 * Variable referencing the gateway IP address of the message.
	 */
	private InetAddress giaddr = null;
	
	/**
	 * Returns relay agent IP address of the message.
	 * 
	 * @return Gateway IP address
	 */
	public InetAddress getGiaddr() {
		return giaddr;
	}
	
	/**
	 * Sets relay agent IP address of the message.
	 * 
	 * @param giaddr
	 * 		  The new gateway IP address
	 */
	protected void setGiaddr(InetAddress giaddr) {
		this.giaddr = giaddr;
	}
	
	/**********************************************************
	 * Client Hardware Address
	 **********************************************************/
	
	/**
	 * Variable referencing the client hardware address of the message.
	 */
	private String chaddr = "";
	
	/**
	 * Returns the client hardware address of the message.
	 * 
	 * @return The client hardware address
	 */
	public String getChaddr() {
		return chaddr;
	}
	
	/**
	 * Sets the client hardware address of the message.
	 * 
	 * @param chaddr
	 * 		  The new client hardware address.
	 * @throws IllegalArgumentException
	 * 		   The MAC address shoud be 16 bytes
	 */
	protected void setChaddr(String chaddr) throws IllegalArgumentException {
		if((chaddr.getBytes().length) != 16) 
			throw new IllegalArgumentException("MAC address should be 16 bytes."); 
		else
			this.chaddr = chaddr;
	}
	
	/**********************************************************
	 * Server name
	 **********************************************************/
	
	/**
	 * Variable referencing the server name of the message.
	 */
	private String sname = "";
	
	/**
	 * Returns the server name of the message.
	 * 
	 * @return The server name
	 */
	public String getSname() {
		return sname;
	}
	
	/**
	 * Sets the server name of the message.
	 * 
	 * @param sname
	 * 		  The new server name
	 */
	protected void setSname(String sname) {
		this.sname = sname;
	}
	
	/**********************************************************
	 * File
	 **********************************************************/
	
	/**
	 * Variable referencing the boot file name of the message.
	 */
	private String file = "";
	
	/**
	 * Returns the boot file name of the message.
	 * 
	 * @return The boot file name.
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * Sets the boot file name of the message.
	 * 
	 * @param file
	 * 		  The new boot file name
	 */
	protected void setFile(String file) {
		this.file = file;
	}
	
	/**********************************************************
	 * Cookie
	 **********************************************************/
	
	/**
	 * Variable referencing the cookie of the message.
	 */
	private static final byte[] COOKIE = {0x63, (byte) 0x82, 0x53, 0x63};

	/**
	 * Return the cookie of the message.
	 * 
	 * @return The cookie of the message.
	 */
	public byte[] getCookie() {
		return COOKIE;
	}
	
	/**********************************************************
	 * Options
	 **********************************************************/

	/**
	 * Variable referencing the options of the message.
	 */
	private OptionsList options;
	
	/**
	 * Returns the options of the message.
	 * 
	 * @return The options list.
	 */
	public OptionsList getOptions() {
		return options;
	}
	
	/**
	 * Sets the options of the message.
	 * 
	 * @param options
	 * 		  The new options of the message
	 */
	protected void setOptions(OptionsList options) {
		this.options = options;
	}

	/**
	 * Converts a message to bytes.
	 * 
	 * @return The byte array matching this message.
	 */
	public byte[] convertToByteArray() {
		byte[] byteArray = new byte[MESSAGE_SIZE];
		byteArray[0] = Utilities.convertToByteArray(1, getOp())[0];
		byteArray[1] = Utilities.convertToByteArray(1, getHtype())[0];
		byteArray[2] = Utilities.convertToByteArray(1, getHlen())[0];
		byteArray[3] = Utilities.convertToByteArray(1, getHops())[0];
		byteArray = Utilities.insertSubArrayInArrayAt(Utilities.convertToByteArray(4, getXid()), byteArray, 4);
		byteArray = Utilities.insertSubArrayInArrayAt(Utilities.convertToByteArray(2, getSecs()), byteArray, 8);
		byteArray = Utilities.insertSubArrayInArrayAt(Utilities.convertToByteArray(2, getFlags()), byteArray, 10);
		byteArray = Utilities.insertSubArrayInArrayAt(getCiaddr().getAddress(), byteArray, 12);
		byteArray = Utilities.insertSubArrayInArrayAt(getYiaddr().getAddress(), byteArray, 16);
		byteArray = Utilities.insertSubArrayInArrayAt(getSiaddr().getAddress(), byteArray, 20);
		byteArray = Utilities.insertSubArrayInArrayAt(getGiaddr().getAddress(), byteArray, 24);
		byteArray = Utilities.insertSubArrayInArrayAt(getChaddr().getBytes(), byteArray, 28);
		byteArray = Utilities.insertSubArrayInArrayAt(getSname().getBytes(), byteArray, 44);
		byteArray = Utilities.insertSubArrayInArrayAt(getFile().getBytes(), byteArray, 108);
		byteArray = Utilities.insertSubArrayInArrayAt(getCookie(), byteArray, 236);
		byteArray = Utilities.insertSubArrayInArrayAt(getOptions().returnBytes(), byteArray, 240);

		return byteArray;
	}
	
	/**
	 * Converts bytes to a message.
	 * 
	 * @param array
	 * 		  The byte array to convert to a message.
	 * @return The converted byte array into a message.
	 */
	public static Message convertToMessage(byte[] array) throws UnknownHostException, UnsupportedEncodingException, IllegalArgumentException {
		int op = (int) array[0];
		int htype = (int) array[1];
		int hlen = (int) array[2];
		int hops = (int) array[3];
		int xid = Utilities.convertToInt(Utilities.getPartArray(4,7, array));
		int secs = Utilities.convertToInt(Utilities.getPartArray(8,9, array));
		int flags = Utilities.convertToInt(Utilities.getPartArray(10,11, array));
		InetAddress ciaddr = InetAddress.getByAddress(Utilities.getPartArray(12, 15, array));
		InetAddress yiaddr = InetAddress.getByAddress(Utilities.getPartArray(16, 19, array));
		InetAddress siaddr = InetAddress.getByAddress(Utilities.getPartArray(20, 23, array));
		InetAddress giaddr = InetAddress.getByAddress(Utilities.getPartArray(24, 27, array));
		String chaddr = new String(Utilities.trimZeros(Utilities.getPartArray(28, 43, array)), "UTF-8");
		String sname = new String(Utilities.trimZeros(Utilities.getPartArray(44, 107, array)), "UTF-8");
		String file = new String(Utilities.trimZeros(Utilities.getPartArray(108, 235, array)), "UTF-8");
		OptionsList options = OptionsList.returnOptionsList(Utilities.getPartArray(240, MESSAGE_SIZE-1, array));
		Message message = new Message(op, htype, hlen, hops, xid, secs, flags, ciaddr, yiaddr, siaddr, giaddr, chaddr, sname, file, options);
		return message;
	}
}
	
	
	
