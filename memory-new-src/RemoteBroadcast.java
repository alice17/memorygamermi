

package src;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteBroadcast extends Remote {

	public void forward(GameMessage msg) throws RemoteException;	



}