/**
 * Created by alice on 02/02/17.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
        int newPlayer(String user) throws RemoteException;
}
