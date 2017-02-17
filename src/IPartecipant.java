
package src;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface IPartecipant extends Remote {
	public void configure(Player[] players, Deck deck) throws RemoteException;
}
