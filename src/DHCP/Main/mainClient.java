package DHCP.Main;

import DHCP.DHCPClient;

public class mainClient {
	public static void main(String[] args) throws Exception {
		
		// Two different clients after each other: same IP
		DHCPClient client = new DHCPClient("JH57DF98RV15FH96");
		DHCPClient client2 = new DHCPClient("JH57DF98RV15FH95");
		
//		class ThreadClient implements Runnable {
//			DHCPClient client;
//			ThreadClient(DHCPClient client){
//				this.client = client;
//			}
//			public void run(){
//				try {
//					client.getIP();
//				} catch (Exception e) {
//					System.out.println("Error occured while retrieving IP.");
//				}
//			}
//		}
//		
//		Thread t1 = new Thread(new ThreadClient(client));
//		Thread t2 = new Thread(new ThreadClient(client2));
//		
//		t1.run();
//		t2.run();
		
		client.getIP();
		System.out.println("");
		client2.getIP();
		client.releaseIP();	
		client2.getIP();
	}

	
}