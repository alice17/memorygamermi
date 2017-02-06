/**
 * Created by alice on 06/02/17.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
    void setnPlayers(int nPlayers) throws RemoteException;
    public boolean isGotPlayers() throws RemoteException;
    public void setGotPlayers(boolean gotPlayers) throws RemoteException;
}
