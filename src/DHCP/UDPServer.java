package DHCP;

/**
 * A class implementing a UDP server.
 * 
 * @version 1.0 - 2016
 * @author  Laurent De Laere
 * 		    Simon Geirnaert
 *
 */
public class UDPServer {
	
//	/**
//	 * @throws Exception 
//	 */
//	public static void main(String[] args) throws Exception {
//        DatagramSocket socket = new DatagramSocket(PORT);
//        
//        while(true)
//           {
//        	  // Receive packet
//              byte[] receiveData = new byte[PACKAGESIZE];
//              DatagramPacket receivePacket = new DatagramPacket(receiveData, PACKAGESIZE);
//              socket.receive(receivePacket);
//              
//              // Print data
//              String sentence = new String(receivePacket.getData());
//              System.out.println("RECEIVED: " + sentence);
//
//              // Process sentence and create response
//              String capitalizedSentence = sentence.toUpperCase();
//              byte[] sendData = new byte[PACKAGESIZE];
//              sendData = capitalizedSentence.getBytes();
//              
//              // Send response data to client
//              InetAddress IPAddress = receivePacket.getAddress();
//              int port = receivePacket.getPort();
//              DatagramPacket sendPacket = new DatagramPacket(sendData, PACKAGESIZE, IPAddress, port);
//              socket.send(sendPacket);
//           }
//	}

}
