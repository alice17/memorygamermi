/**
 * Created by alice on 02/02/17.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ServerInterface extends Remote {
        int newPlayer(String user) throws RemoteException;
        LinkedList<Player> getPlayerList() throws RemoteException;
}
