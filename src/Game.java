import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by alice on 06/02/17.
 */
public class Game extends UnicastRemoteObject implements GameInterface {
    public int nPlayers;            // num di giocatori
    public boolean gotPlayers;      // abbiamo giocatori per iniziare?
    public int currentPlayer;       // id del giocatore di questo turno
    public boolean isGameEnded;

    public Game() throws RemoteException {}

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public boolean isGotPlayers() {
        return gotPlayers;
    }

    public void setGotPlayers(boolean gotPlayers) {
        this.gotPlayers = gotPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }
}