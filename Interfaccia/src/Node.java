



package src;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Node implements Serializable, Comparable<Node> {

	private InetAddress inetAddr;
	private int port;
	//private int addr;
	private int id;



	public Node(String host, int port) throws UnknownHostException {
		this(InetAddress.getByName(host), port);
	}

	public Node(InetAddress inetAddr, int port) {
		this.inetAddr = inetAddr;
		//this.addr = inet2int(inetAddr);
		//System.out.println(addr);
		this.port = port;
	}

	public InetAddress getInetAddress() {
		return inetAddr;
	}

	public int getPort() {
		return port;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int compareTo(Node player) {
		if (port < player.port)
			return -1;
		if (port > player.port)
			return 1;
		if (inetAddr.equals(player.inetAddr))
			return 0;
		return -1;
	}

	public String toString() {
		return inetAddr.getHostAddress() + ":" + port;
	}

	/*private int inet2int(InetAddress inetAddr) {
        byte[] bytes = inetAddr.getAddress();
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;}*/


}