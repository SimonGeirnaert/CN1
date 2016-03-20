package DHCP;

public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DHCPClient client = new DHCPClient("JH57DF98RV15FH96");
		client.getIP();
		client.releaseIP();		
	}

}