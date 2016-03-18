package DHCP;

public class ReceivedData {
	
	public ReceivedData(byte[] data, int port){
		setData(data);
		setPort(port);
	}

	private byte[] data;
	private int port;
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

}
