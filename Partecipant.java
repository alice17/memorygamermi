package src;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/*Classe Partecipant utilizzata per notificare le info di gioco a tutti i giocatori*/
public class Partecipant extends UnicastRemoteObject implements IPartecipant {

	private Player[] players;
	private boolean gotPartecipants = false;
	private List<Integer> cardVals;	
	
	
	public Partecipant() throws RemoteException {}

	public synchronized void configure(Player[] players, List<Integer> cardVals) throws RemoteException {

	// metodo chiamato da subscribe per configurare le variabili di partecipant
		this.players = players;
		this.cardVals = cardVals;
		gotPartecipants = true;
		notifyAll();
		System.out.println("Participants and card list has been received.");
	}

	public synchronized Player[] getPlayers() {
		if (!gotPartecipants)
			try {
				System.out.println("Participants list unavailable: waiting...");
			 	wait();
				System.out.println("Timeout end or object notified.");
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		return players;
	}

	public List<Integer> getCardVals() {
		return cardVals;
	}
}
