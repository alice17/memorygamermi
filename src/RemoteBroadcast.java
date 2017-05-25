//package src;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
Classe che definisce i metodi che possono essere chiamati attraverso chiamate RMI.
*/

public interface RemoteBroadcast extends Remote {

	public void forward(GameMessage msg) throws RemoteException;	

	public void checkNode() throws RemoteException;

}
