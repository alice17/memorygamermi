/**
 * Created by alice on 02/02/17.
 */
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.List;

public class Server extends UnicastRemoteObject implements ServerInterface {
    LinkedList<Player> playerList = new LinkedList<Player>();      // lista di giocatori
    int nPlayers = 0;        // numero di giocatori totali


    public static void main(String[] args) {

        System.out.println("Launching server...");

        try {
            // set security manager
            if (System.getSecurityManager() == null)
                System.setSecurityManager(new RMISecurityManager());
            else
                System.out.println("Warning: SecurityManager may not be RMISecurityManager");

            Server srv = new Server();
            Naming.rebind("rmi://127.0.0.1:1099/Server", srv);
            System.out.println("Connection established.");
        }
        catch (Exception e){
            System.out.println("Server err: " + e.getMessage());
            e.printStackTrace();
        }

        // raccoglie client e crea oggetti Player

        // inizia il gioco
    }

    public Server() throws RemoteException {}

    public int newPlayer(String user){
        /* Aggiunge un giocatore alla lista di giocatori e ne ritorna l'id */

        nPlayers+=1;
        Player pl = new Player(user, nPlayers-1);
        playerList.add( pl );

        System.out.println("New player: " + pl.getUsername());

        return nPlayers - 1;
    }

}
