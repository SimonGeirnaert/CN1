import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		UDPClient client = new UDPClient();
//		
//		InetAddress ipNull = InetAddress.getByName("0.0.0.0"); //geeft array met [0,0,0,0]
//        
//		Option option53 = new Option(53, Utilities.convertToByteArray(1, 1));
//		Option option50 = new Option(50, InetAddress.getByName("0.0.0.0").getAddress());
//		OptionsList optionsList = new OptionsList(option53, option50);
//		
//		Message m = new Message(1, 1, 6, 0, 6445, 0, 32768, ipNull, ipNull, ipNull, ipNull, "AB12DC34EF56GH78", "", "",1234567890 , optionsList);
//		byte[] messageArray = new byte[512];
//		messageArray = m.convertToByteArray();
//		for(int i= 0; i< 512; i++){
//			System.out.print(i+ ". ");
//			System.out.println(Integer.toBinaryString(messageArray[i] & 0xFF));
//		}
//		
//		
//		
//		Message m2 = Message.convertToMessage(messageArray);
//		OptionsList optionsListM2 = m2.getOptions();
//		
//		if(m.getOp() == m2.getOp()) System.out.println("Op: MATCH");
//		else System.out.println("Op: FAIL");
//		if(m.getHtype() == m2.getHtype()) System.out.println("Htype: MATCH");
//		else System.out.println("Htype: FAIL");
//		if(m.getHlen() == m2.getHlen()) System.out.println("Hlen: MATCH");
//		else System.out.println("Hlen: FAIL");
//		if(m.getHops() == m2.getHops()) System.out.println("Hops: MATCH");
//		else System.out.println("Hops: FAIL");
//		if(m.getXid() == m2.getXid()) System.out.println("Xid: MATCH");
//		else System.out.println("Xid: FAIL");
//		if(m.getSecs() == m2.getSecs()) System.out.println("Secs: MATCH");
//		else System.out.println("Secs: FAIL");
//		if(m.getFlags() == m2.getFlags()) System.out.println("Flags: MATCH");
//		else System.out.println("Flags: FAIL");
//		if(m.getCiaddr().equals(m2.getCiaddr())) System.out.println("Ciaddr: MATCH");
//		else System.out.println("Ciaddr: FAIL");
//		if(m.getYiaddr().equals(m2.getYiaddr())) System.out.println("Yiaddr: MATCH");
//		else System.out.println("Yiaddr: FAIL");
//		if(m.getSiaddr().equals(m2.getSiaddr())) System.out.println("Siaddr: MATCH");
//		else System.out.println("Siaddr: FAIL");
//		if(m.getGiaddr().equals(m2.getGiaddr())) System.out.println("Giaddr: MATCH");
//		else System.out.println("Giaddr: FAIL");
//		if(m.getChaddr().equals(m2.getChaddr())) System.out.println("Chaddr: MATCH");
//		else System.out.println("Chaddr: FAIL");
//		if(m.getSname().equals(m2.getSname())) System.out.println("Sname: MATCH");
//		else System.out.println("Sname: FAIL");
//		if(m.getFile().equals(m2.getFile())) System.out.println("File: MATCH");
//		else System.out.println("File: FAIL");
//		if(m.getCookie() == m2.getCookie()) System.out.println("Cookie: MATCH");
//		else System.out.println("Cookie: FAIL");
//		//Deze equal geeft error maar de option lists zijn wel degelijk gelijk... (zie debugger)
//		if(Arrays.deepEquals(optionsList.getOptions(), optionsListM2.getOptions())) System.out.println("Options: MATCH");
//		else System.out.println("Options: FAIL");
//		
//		byte[] responseArray = client.sendData(messageArray);
//		Message response = Message.convertToMessage(responseArray);
		
		DHCPClient client = new DHCPClient();
		client.getIP();
		
//		DatagramSocket socket = new DatagramSocket();
//		Message response = client.DHCPDiscover(socket);
//		
//		System.out.println("Response Op: " + response.getOp());
//		System.out.println("Response Htype: " + response.getHtype());
//		System.out.println("Response Hlen: " + response.getHlen());
//		System.out.println("Response Hops: " + response.getHops());
//		System.out.println("Response Xid: " + response.getXid());
//		System.out.println("Response Secs: " + response.getSecs());
//		System.out.println("Response Flags: " + response.getFlags());
//		System.out.println("Response Ciaddr: " + response.getCiaddr().getHostAddress());
//		System.out.println("Response Yiaddr: " + response.getYiaddr().getHostAddress());
//		System.out.println("Response Siaddr: " + response.getSiaddr().getHostAddress());
//		System.out.println("Response Giaddr: " + response.getGiaddr().getHostAddress());
//		System.out.println("Response Chaddr: " + response.getChaddr());
//		System.out.println("Response Sname: " + response.getSname());
//		System.out.println("Response File: " + response.getFile());
//		System.out.println("Response Cookie: " + response.getCookie());
//		System.out.println("Response Options: " + response.getOptions().toString());
		
		
		
//		OptionsList responseOptions = response.getOptions();
//		
//		System.out.println(responseOptions.toString());
		
		
		
	}


}