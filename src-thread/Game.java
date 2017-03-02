/*	Classe game
	Contiene variabili di gioco
	Locale in ogni client
*/

package src;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import static java.lang.Thread.sleep;

public class Game {
    private int nPlayers;                        // num di giocatori totali (aggiornato)
    private int currentPlayer;                   // id del giocatore di questo turno
    private boolean isGameEnded;
    private Deck boardDeck;
    private OnesMove myMove;

    public Game(int nPlayers) {
    	this.nPlayers = nPlayers;
    	this.isGameEnded = false;
    	this.currentPlayer = 0;
    }

    public int getnPlayers() { return nPlayers; }
    public void setnPlayers(int nPlayers) { this.nPlayers = nPlayers; }
    public int getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }
    public boolean isGameEnded() { return isGameEnded; }
    public void setGameEnded(boolean gameEnded) { isGameEnded = gameEnded; }

    /*public OnesMove myTurn(Board board) {
        myMove = board.retrieveMove();
        if (myMove.getPair() == false) {
            currentPlayer++;
        }
        return myMove;
    }*/

    public void update(GameMessage m) {
        setCurrentPlayer((getCurrentPlayer()+1) % nPlayers);
    }
}
