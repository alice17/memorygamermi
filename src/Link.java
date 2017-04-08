

package src;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Questa è la classe che gestisce i collegamenti con i nodi vicini e recupera le info 
sul proprio nodo e sui vicini.
In questa classe andranno aggiunti i metodi per recuperare il riferimento ad un nuovo vicino
in caso di crash.

*/

public class Link {

	private Node[] nodes;
	private int myId = 0;
	private int rightId = 0;
	private int leftId = 0;		// da togliere?
	private Node me;
	private Lock lock = null;
	private RemoteBroadcast rightNode = null;


	public Link(Node me, Node[] nodes) {
		this.lock = new ReentrantLock();
		this.me = me;
		this.nodes = nodes;
		configure();
	}

    //ricerca il proprio id e quello dei vicino all'interno dell'array di node restituito dal server.
	//Viene ricercato anche il nodo sinistro che non viene utilizzato, si può eliminare il leftid
	//se decidiamo di rimanere con un anello direzionale.

	private void configure() {
		for (int i = 0; i < nodes.length; i++) {
			if (me.compareTo(nodes[i]) == 0 ) {
				myId = i;
				leftId = backward(i, nodes.length);

				// da cambiare per la fault tolerance
				rightId = (i + 1) % nodes.length;
			}
		}
	}

	private int backward(int i, int length) {
		if (i - 1 < 0) {
			return length - 1 ;
		} else {
			return i - 1;
		}
	}

	public int getNodeId() {
		return myId;
	}

	public int getLeftId() {
		return leftId;
	}
	public int getRightId() {
		return rightId;
	}

	/* Metodo che recupera il riferimento all'oggetto RemoteBroadcast del nodo vicino destro 
	tramite il metodo lookupnodeper per potergli inviare i messaggi durante il gioco, successivamente
	crea un oggetto di tipo ServiceBulk.*/

	public ServiceBulk getRight() {

		rightNode = lookupNode(rightId);
		return new ServiceBulk(rightNode,rightId);

	}

	/* Metodo che utilizza RMI, restituisce un riferimento di tipo RemoteBroadcast */
	
	private RemoteBroadcast lookupNode(int id)  {
		RemoteBroadcast broadcast = null;
		String url = "rmi://" + nodes[id].getInetAddress().getCanonicalHostName() + ":"
					+ nodes[id].getPort() + "/Broadcast";
		boolean success = false;
		try {
			System.out.println("looking up " + url);
			broadcast = (RemoteBroadcast)Naming.lookup(url);
			success = true;
		} catch (MalformedURLException e) {
			System.out.println("Malformed");
		} catch (NotBoundException e) {
			System.out.println("Notbound");
		} catch (RemoteException e) {
			System.out.println("Remote");
		}
		return broadcast;
	}

}