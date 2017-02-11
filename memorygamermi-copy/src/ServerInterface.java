package src;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ServerInterface extends Remote {
      
      public boolean subscribeAccepted(IPartecipant partecipant, Player player)
      	throws RemoteException;
}
