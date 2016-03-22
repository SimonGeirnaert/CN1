package DHCP;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * A class implementing UDP.
 * 
 * @version 1.0 - 2016
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class UDPHost {
	
	/**
	 * Constant referencing the packet size.
	 */
	private static final int PACKETSIZE = 576;
	
	/**
	 * Initialize the new UPD client.
	 * 
	 * @post The IP of the receiver is equal to the given IP.
	 * @post The destination port is equal to the given port.
	 */
	public UDPHost(InetAddress receiver, int port) {
		setReceiverIP(receiver);
		setDestinationPort(port);
	}
	
	/**
	 * Constant referencing the IP address of the receiver of UDP messages sent with this UDP object.
	 */
	private InetAddress receiverIP = null;	
	
	/**
	 * Return the IP of the receiver.
	 * 
	 * @return The IP address of the receiver.
	 */
	public InetAddress getReceiverIP() {
		return receiverIP;
	}

	/**
	 * Sets the IP address of the receiver of this UDP interaction.
	 * 
	 * @param receiverIP
	 *        The IP address to set.
	 */
	private void setReceiverIP(InetAddress receiverIP) {
		this.receiverIP = receiverIP;
	}

	/**
	 * Constant referencing the destination port number.
	 */
	private int destinationPort = 0;
	
	/**
	 * @return The destination port.
	 */
	public int getDestinationPort() {
		return destinationPort;
	}

	/**
	 * Sets the destination port.
	 * 
	 * @param destinationPort
	 *        The port to set.
	 */
	 void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}


	/**
	 * Send data to UDP server.
	 * 
	 * @param sendData
	 * 		  Data packet to be sent to the UDP server.
	 * @param socket
	 *        The DatagramSocket currently in use
	 * 
	 * @throws UnknownHostException
	 * 		   InetAddress.getByName() can't find a host for the given hostname.
	 * @throws IOException
	 * 		   socket.send() or socket.receive() encountered an error with the IO.
	 * @throws SocketException
	 *         The construction of the DatagramSocket has failed.
	 */
	public byte[] sendData(byte[] sendData, DatagramSocket socket) throws UnknownHostException, IOException {
		sendDataWithoutResponse(sendData, socket);
		return receiveData(socket).getData();
	}
	
	/**
	 * Send data to the UDP receiver without receiving a response.
	 * 
	 * @param sendData
	 * 		  Data packet to be sent to the other end of the UDP connection.
	 * @param socket
	 *        The DatagramSocket currently in use
	 *        
	 * @throws UnknownHostException
	 * 		   InetAddress.getByName() can't find a host for the given hostname.
	 * @throws IOException
	 * 		   socket.send() or socket.receive() encountered an error with the IO.
	 * @throws SocketException
	 *         The construction of the DatagramSocket has failed.
	 */
	public void sendDataWithoutResponse(byte[] sendData, DatagramSocket socket) throws UnknownHostException, IOException {
		DatagramPacket sendPacket = new DatagramPacket(sendData, PACKETSIZE, getReceiverIP(), getDestinationPort());
		socket.send(sendPacket);
	}
	
	/**
	 * Receives data via UDP and returns the data.
	 * 
	 * @param socket
	 *        The socket used in the transaction
	 *        
	 * @return The received data
	 */
	public ReceivedData receiveData(DatagramSocket socket) throws IOException {
        byte[] receiveData = new byte[PACKETSIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, PACKETSIZE);
        socket.receive(receivePacket);
        ReceivedData rcvd = new ReceivedData(receiveData, receivePacket.getPort());
        return rcvd;
	}
	
}
