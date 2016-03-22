package DHCP;

/**
 * Represents data received via UDP. Contains the data and the port used by the sender.
 * 
 * @author Simon Geirnaert
 *         Laurent De Laere
 */
public class ReceivedData {
	
	/**
	 * Creates a ReceivedData object.
	 * 
	 * @param data
	 *        The data received as a byte array.
	 * @param port
	 *        The port used by the sender of the data.
	 */
	public ReceivedData(byte[] data, int port){
		setData(data);
		setPort(port);
	}

	/**
	 * Variable representing the data received.
	 */
	private byte[] data;
	
	/**
	 * @return The data received.
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Sets the data received.
	 * @param data
	 *        The data to set.
	 */
	void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Variable representing the port used by the sender of the data.
	 */
	private int port;
	
	/**
	 * @return The port used by the sender of the data.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the port used by the sender of the data.
	 * 
	 * @param port
	 *        The port to set.
	 */
	void setPort(int port) {
		this.port = port;
	}

}
