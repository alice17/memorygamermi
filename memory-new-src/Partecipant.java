

package src;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Partecipant extends UnicastRemoteObject implements IPartecipant {

	private Player[] players;
	private boolean gotPartecipants = false;


	
	public Partecipant() throws RemoteException {}


	public synchronized void configure(Player[] players) throws RemoteException {
		this.players =players;
		gotPartecipants = true;
		notifyAll();
		System.out.println("Partecipants list has been received.");
	}

	public synchronized Player[] getPlayers() {
		if (!gotPartecipants)
			try {
				System.out.println("Partecipants list unavailable: waiting...");
			 	wait();
				System.out.println("Timeout end or object notified.");
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		return players;
	}
}