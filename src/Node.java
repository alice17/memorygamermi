



package src;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
Ogni player crea una classe node con il proprio indirizzo di rete e la porta.
La classe node è la classe padre di player.
Quando successivamente viene creato un oggetto della classe link viene passato
come parametro un oggetto node invece che player.
Viene fatto perchè a livello di rete servono solo le info di node e non tutte 
quelle contenute in player.
Node e player si potrebbero anche unire, tenendole divise secondo me si fà meno
fatica nella gestione dei crash nel momento di sostituire il vicino che è andato in crash.
*/

public class Node implements Serializable, Comparable<Node> {

	private InetAddress inetAddr;
	private int port;
	//private int addr;
	private int id;
	private boolean active = true;	// indica se il nodo è attivo o no



	public Node(String host, int port) throws UnknownHostException {
		this(InetAddress.getByName(host), port);
	}

	public Node(InetAddress inetAddr, int port) {
		this.inetAddr = inetAddr;
		//this.addr = inet2int(inetAddr);
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

	/*private int inet2int(InetAddress inetAddr) {
        byte[] bytes = inetAddr.getAddress();
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;}*/


}