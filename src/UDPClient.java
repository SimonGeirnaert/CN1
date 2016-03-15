import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * A class implementing a UDP client.
 * 
 * @version 1.0 - 2016
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class UDPClient {
	
	/**
	 * Constant referencing the IP address of the local host.
	 */
	private static final String IP_SERVER = "10.33.14.246";	
	
	/**
	 * Constant referencing the destination port number.
	 */
	private static final int DESTINATION_PORT = 1234;
	
	/**
	 * Constant referencing the packet size.
	 */
	private static final int PACKETSIZE = 512;
	
	/**
	 * Initialize the new UPD client.
	 */
	public UDPClient() {
	}

	/**
	 * Send data to UDP server.
	 * @param sendData
	 * 		  Data packet to be sent to the UDP server.
	 * @throws UnknownHostException
	 * 		   InetAddress.getByName() can't find a host for the given hostname.
	 * @throws IOException
	 * 		   socket.send() or socket.receive() encountered an error with the IO.
	 * @throws SocketException
	 *         The construction of the DatagramSocket has failed.
	 */
	public byte[] sendData(byte[] sendData, DatagramSocket socket) throws UnknownHostException, IOException {
		
		// Make datagram socket
		// DatagramSocket socket = new DatagramSocket();
		
		// Send packet
		InetAddress IPAddress = InetAddress.getByName(IP_SERVER);
		//System.out.println(IPAddress);
		DatagramPacket sendPacket = new DatagramPacket(sendData, PACKETSIZE, IPAddress, DESTINATION_PORT);
		socket.send(sendPacket);
		//System.out.println("packet sent");
		return waitForAndReturnResponse(socket);
	}
	
	/**
	 * Waits for a reply and returns the reply when received as a byte array.
	 * 
	 * @return The response as a byte array.
	 * @throws IOException
	 *         There has been an exception while receiving the package.
	 */
	public byte[] waitForAndReturnResponse(DatagramSocket socket) throws IOException{
		// Make datagram socket
		// DatagramSocket socket = new DatagramSocket();
				
		// Receive packet
		byte[] receiveData = new byte[PACKETSIZE];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, PACKETSIZE);
		//System.out.println("packet received made");
		socket.receive(receivePacket);
		//System.out.println("Response: " + receiveData);
		
		// Return response and close socket
		//socket.close();
		return receiveData;
	}
}
