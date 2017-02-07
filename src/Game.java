import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import static java.lang.Thread.sleep;

/**
 * Created by alice on 06/02/17.
 */
public class Game implements GameInterface {
    public int nPlayers;            // num di giocatori
    public boolean gotPlayers;      // abbiamo giocatori per iniziare?
    public int currentPlayer;       // id del giocatore di questo turno
    public boolean isGameEnded;

    public Game() throws RemoteException {}

    public void waitServer(){
        synchronized (this){
            try {
                int sec = 10000;
                System.out.println("Wait for " + sec + " ms");
                sleep(sec);
                System.out.println("Wait ended");
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void waitClient(){
        synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
