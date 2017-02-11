


package src;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Subscribe extends UnicastRemoteObject implements ServerInterface {

	private Player[] players;
	private IPartecipant[] partecipants;
	private int playersMaxNo;
	private int playersNo = 0;
	private boolean openSubscribe = true;


	public Subscribe(int playersMaxNo) throws RemoteException {

		this.playersMaxNo = playersMaxNo;
  		players = new Player[playersMaxNo];
  		partecipants = new IPartecipant[playersMaxNo];
	}

  	public synchronized boolean subscribeAccepted(IPartecipant partecipant,
  			Player player) throws RemoteException {
  		if (playersNo < playersMaxNo &&  openSubscribe) {
  			System.out.println("New player --> " + player.getUsername());
  			partecipants[playersNo] = partecipant;
  			players[playersNo] = player;
  			playersNo++;
  			if (playersNo==playersMaxNo) {
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

		for (int i=0 ;i<playersNo;i++) {
			final IPartecipant p= partecipants[i];
			final int j=i;
			Thread t = new Thread() {
				public void run() {
					try {
						System.out.println("Configuring partecipant " + realPlayers[j].getUsername());
						p.configure(players);
						System.out.println("Configuring done.");
					} catch (RemoteException re) {
						re.printStackTrace();
					}
				}
			};
			t.start();
		}
	}
}