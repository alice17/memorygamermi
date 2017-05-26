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
*/

public class Link {

	public Node[] nodes;
	private int myId = 0;
	private int rightId = 0;
	private int leftId = 0;		
	private Node me;
	private RemoteBroadcast rightNode = null;



	public Link(Node me, Node[] nodes) {
		this.me = me;
		this.nodes = nodes;
		configure();
	}

    //ricerca il proprio id e quello dei vicino all'interno dell'array di node restituito dal server.

	private void configure() {
		int j;

		for (int i = 0; i < nodes.length; i++) {
			if (me.compareTo(nodes[i]) == 0 ) {
				myId = i;	
				j=1;

				// prende il prossimo nodo attivo
				do{
					rightId = (i + j) % nodes.length;

					j=j+1;
				}while ( nodes[rightId].isActive()==false );
				
			}
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

	public void setRightId(int id) {
		rightId = id;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public void incRightId() {
		rightId = (rightId +1) % nodes.length;
	}

	/* Metodo che recupera il riferimento all'oggetto RemoteBroadcast del nodo vicino destro 
	tramite il metodo lookupnode per poi potergli inviare i messaggi durante il gioco, successivamente
	crea un oggetto di tipo ServiceBulk.*/

	public ServiceBulk getRight() {

		rightNode = lookupNode(rightId);
		return new ServiceBulk(rightNode,rightId);

	}

	//Metodo che utilizza RMI, restituisce un riferimento di tipo RemoteBroadcast 
	
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

	//Metodo che controlla i nodi attivi, differente da lookupnode
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

	//Metodo che controlla i nodi durante un controllo AYA
	public boolean checkAYANode(int rightId,int playerId) {

		
		boolean success = true;
		RemoteBroadcast broadcast = null;
		String url = "rmi://" + nodes[rightId].getInetAddress().getCanonicalHostName() + ":"
					+ nodes[rightId].getPort() + "/Broadcast";
		try {
			System.out.println("looking up " + url);
			broadcast = (RemoteBroadcast)Naming.lookup(url);
		} catch (MalformedURLException e) {
			System.out.println("Malformed");
			success = false;
		} catch (NotBoundException e) {
			System.out.println("Notbound");
			success = false;
		} catch (RemoteException e) {
			System.out.println("Remote");
			success = false;
		}
		return success;
	}

}