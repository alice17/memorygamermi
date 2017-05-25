package src;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*

Classe padre di Player,ogni player crea un istanza di questa con il proprio indirizzo di rete e la porta.
Quando viene creato un oggetto della classe link viene passato come parametro un oggetto node invece che player.
Viene fatto perchè a livello di rete servono solo le info di node e non tutte quelle contenute in player.

*/

public class Node implements Serializable, Comparable<Node> {

	private InetAddress inetAddr;
	private int port;
	private int id;
	private boolean active = true;	// indica se il nodo è attivo o no



	public Node(String host, int port) throws UnknownHostException {
		this(InetAddress.getByName(host), port);
	}

	public Node(InetAddress inetAddr, int port) {

		this.inetAddr = inetAddr;
		this.port = port;
	}

	public InetAddress getInetAddress() { return inetAddr; }
	public int getPort() { return port; } 
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public boolean isActive(){ return active; }
	public void setActive( boolean active ) { this.active = active; }

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

	public void setNodeCrashed() {
		active = false;
	}

	public boolean getActive() {
		return active;
	}

}
