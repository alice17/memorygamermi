//package src;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


/*Interfaccia remota della classe Partecipant, qui possono venire dichiarati
i metodi utilizzati tramite chiamate RMI*/

public interface IPartecipant extends Remote {

	public void configure(Player[] players, List<Integer> cardVals) throws RemoteException;
}
