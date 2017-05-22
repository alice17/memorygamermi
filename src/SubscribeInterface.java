package src;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.InetAddress;

/* 
Interfaccia remota della classe Subscribe
*/

public interface SubscribeInterface extends Remote {
      
      public boolean subscribeAccepted(IPartecipant partecipant, String username, InetAddress inetAddr, int portt) throws RemoteException;
}
