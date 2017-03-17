
package src;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IPartecipant extends Remote {
	public void configure(Player[] players, List<Integer> cardVals) throws RemoteException;
}
