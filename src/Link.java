

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

	public Node[] nodes;
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
		int j;

		for (int i = 0; i < nodes.length; i++) {
			if (me.compareTo(nodes[i]) == 0 ) {
				myId = i;
				leftId = backward(i, nodes.length);	//da togliere?

				j=1;

				// prende il prossimo nodo attivo
				do{
					rightId = (i + j) % nodes.length;

					j=j+1;
				}while ( nodes[rightId].isActive()==false );
				
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

	public Node[] getNodes() {
		return nodes;
	}

	public void incRightId() {
		rightId = (rightId +1) % nodes.length;
	}

	/* Metodo che recupera il riferimento all'oggetto RemoteBroadcast del nodo vicino destro 
	tramite il metodo lookupnode per per potergli inviare i messaggi durante il gioco, successivamente
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
		try {
			System.out.println("looking up " + url);
			broadcast = (RemoteBroadcast)Naming.lookup(url);
		} catch (MalformedURLException e) {
			System.out.println("Malformed");
			nodes[id].setNodeCrashed();
		} catch (NotBoundException e) {
			System.out.println("Notbound");
			nodes[id].setNodeCrashed();
		} catch (RemoteException e) {
			System.out.println("Remote");
			nodes[id].setNodeCrashed();
		}
		return broadcast;
	}

	public boolean checkAliveNode() {

		int id = getRightId();
		boolean success = true;
		RemoteBroadcast broadcast = null;
		String url = "rmi://" + nodes[id].getInetAddress().getCanonicalHostName() + ":"
					+ nodes[id].getPort() + "/Broadcast";
		try {
			System.out.println("looking up " + url);
			broadcast = (RemoteBroadcast)Naming.lookup(url);
		} catch (MalformedURLException e) {
			System.out.println("Malformed");
			nodes[id].setNodeCrashed();
			success = false;
		} catch (NotBoundException e) {
			System.out.println("Notbound");
			nodes[id].setNodeCrashed();
			success = false;
		} catch (RemoteException e) {
			System.out.println("Remote");
			nodes[id].setNodeCrashed();
			success = false;
		}
		return success;
	}


}