public class Peer {

	/*
	 * Sammelt die Daten eines bekannten Peers
	 */
	
	
	private String name;
	private String ip;
	private int port;
	private long time;

	public Peer() {}
	public Peer(String n, String i, int p) {
		name = n;
		ip = "localhost";
		port = p;
		time = System.currentTimeMillis();
	}
	
	public String getName() {
		return name;
	}
	public String getIP() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public long getTime() {
		return time;
	}
	
	public void update() {
		time = System.currentTimeMillis();
	}
}
