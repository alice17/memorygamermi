package src;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SubscribeInterface extends Remote {
      
      public boolean subscribeAccepted(IPartecipant partecipant, Player player) throws RemoteException;
}
