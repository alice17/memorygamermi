package src;

import java.rmi.RemoteException;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/*
Classe Subscribe, creata ed istanziata dal server
Serve per raccogliere i client e passargli l'insieme delle carte mescolate.
*/

public class Subscribe extends UnicastRemoteObject implements SubscribeInterface {

	private Player[] players;
	private IPartecipant[] partecipants;
	private int playersMaxNo;
	private int playersNo = 0;
	private boolean openSubscribe = true;
	private List<Integer> cardVals;		
	private int nCards;

	public Subscribe(int playersMaxNo) throws RemoteException {
		this.playersMaxNo = playersMaxNo;
  		players = new Player[playersMaxNo];
  		partecipants = new IPartecipant[playersMaxNo];
	}

  	public synchronized boolean subscribeAccepted(IPartecipant partecipant, String username, InetAddress inetAddr, int port) throws RemoteException {
  	// metodo chiamato dal client
  		if (playersNo < playersMaxNo &&  openSubscribe) {
  			System.out.println("New player --> " + username);
  			partecipants[playersNo] = partecipant;

  			// creo il nuovo player
  			players[playersNo] = new Player(username, inetAddr, port);
  			playersNo++;
  			
  			if (playersNo==playersMaxNo) {
  			// raggiunto il num max di partecipanti
  				openSubscribe=false;
  				replyClients();
  				notify();
  			}
  			return true;
  		}
  		return false;
  	}

  	public synchronized void endSubscribe() {
  		if (openSubscribe) {
  			openSubscribe = false;
  			replyClients();
  			notify();
  		}
  	}

  	public synchronized Player[] getPlayers() {
  		if (openSubscribe)
  			try {
  				wait();
  			} catch (InterruptedException ie) {
  				ie.printStackTrace();
  			}
  		return players;
	}

	public synchronized int getPlayersNo() {
		if (openSubscribe)
			try {
				wait();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		return playersNo;	
	}

	private void replyClients() {
		final Player[] realPlayers = new Player[playersNo];
		System.arraycopy(players, 0, realPlayers, 0, playersNo);
		players = realPlayers;
		
		// genera insieme di carte
		nCards = 20;			//4 * playersNo;
		generateCards();

		// configure participants
		for (int i=0 ;i < playersNo;i++) {
			final IPartecipant p = partecipants[i];
			final int j=i;
		
			Thread t = new Thread() {
				public void run() {
					try {
						System.out.println("Configuring participant " + realPlayers[j].getUsername());
						p.configure(players, cardVals);
						System.out.println("Configuring done.");
					} catch (RemoteException re) {
						re.printStackTrace();
					}
				}
			};
			t.start();
		}
	}


	private void generateCards(){
	// genera e mescola il mazzo di carte
		int i;
		cardVals = new ArrayList<Integer>();
		
		for(i=0; i < (nCards/2) ; i++){
			cardVals.add(i);
			cardVals.add(i);
		}
		
		Collections.shuffle(cardVals);
	}
}
