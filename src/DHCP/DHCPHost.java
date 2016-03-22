package DHCP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import DHCP.Message.Message;

public abstract class DHCPHost {
	
	/**
	 * Creates a UDP message in DHCP format with all given fields and sends it to the server.
	 * 
	 * @param message
	 * 		  The message to be sent.
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 * 
	 * @return The answer from the server as a message.
	 */
	protected Message sendUDPMessage(Message message, UDPHost client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		Message response = Message.convertToMessage(client.sendData(message.convertToByteArray(), socket));
		return waitForCorrectAnswer(message.getXid(), response, client, socket);
	}
	
	/**
	 * Creates a UDP message in DHCP format with all given fields and sends it to the server.
	 * 
	 * @param message
	 * 		  The message to be sent.
	 * @param client
	 *        The UDPClient currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 */
	protected void sendUDPMessageWithoutResponse(Message message, UDPHost client, DatagramSocket socket) throws UnknownHostException, SocketException, IOException {
		client.sendDataWithoutResponse(message.convertToByteArray(), socket);
	}
	
	/**
	 * Checks if the response received is the correct response and returns the response message.
	 * If an incorrect response is received, wait for the correct response to return.
	 * 
	 * @param xid
	 *        The transaction ID of the message sent by the client to compare with the transaction ID of the received message.
	 * @param response
	 *        The response received from the server.
	 * @param client
	 *        The UDP client currently in use.
	 * @param socket
	 *        The DatagramSocket currently in use.
	 *        
	 * @return The correct reply from the server. 
	 */
	protected Message waitForCorrectAnswer(int xid, Message response, UDPHost client, DatagramSocket socket) throws UnknownHostException, UnsupportedEncodingException, IllegalArgumentException, IOException{
		if(isCorrectResponseMessage(xid, response)){
			return response;
		}
		else{
			System.out.println("- Response did not have matching Xid. Awaiting correct response.");
			return waitForCorrectAnswer(xid, Message.convertToMessage(client.receiveData(socket).getData()), client, socket);
		}
	}
	
	/**
	 * Check if a response message is the answer to the sent message
	 * 
	 * @param xid
	 * 		  The transaction ID of the sent message.
	 * @param response
	 * 		  The response
	 * @return True if the given transaction ID is equal to the response transaction ID;
	 * 		   false otherwise.
	 */
	protected boolean isCorrectResponseMessage(int xid, Message response) {
		if(xid == response.getXid())
			return true;
		else
			return false;
	}

}
