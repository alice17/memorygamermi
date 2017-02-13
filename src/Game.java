package src;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import static java.lang.Thread.sleep;


public class Game implements GameInterface {
    public int nPlayers;                        // num di giocatori totali (aggiornato)
    public boolean gotPlayers;                  // abbiamo giocatori per iniziare? (forse da togliere)
    public int currentPlayer;                   // id del giocatore di questo turno
    public boolean isGameEnded = false;
    public final Object lock = new Object();

    public Game() throws RemoteException {}

    synchronized public void waitServer(){
        //synchronized (lock){
            try {
                int sec = 10000;
                System.out.println("Wait for " + sec + " ms");
                sleep(sec);
                System.out.println("Wait ended");
                notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
    }

    synchronized public void waitClient(){
        //synchronized (lock){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
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
