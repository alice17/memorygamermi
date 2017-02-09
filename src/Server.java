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

import static java.lang.Thread.sleep;


public class Server extends UnicastRemoteObject implements ServerInterface {
    public static LinkedList<Player> playerList = new LinkedList<>();      // lista di giocatori
    private static int registeredPlayers;                                    // numero di giocatori totali registrati
    //public static boolean gotPlayers = false;
    public final Object lock = new Object();

    public static void main(String[] args) {
        /* setting up connection */
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

        /* create first Player (server) with id 0*/
        playerList.add( new Player("server", 0) );
        registeredPlayers+=1;

        /* wait for sec mseconds for the players' registration */

        // creo oggetto Game e registro lo stub nell'RMI registry
        try {
            GameInterface game = new Game();
            GameInterface gamestub = (GameInterface) UnicastRemoteObject.exportObject(game ,0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("game", gamestub);

            game.waitServer();

            game.setnPlayers(registeredPlayers);
        }catch(Exception e){
            e.printStackTrace();
        }


        // start game

    }

    public Server() throws RemoteException {}

    public int newPlayer(String user){
        /* Aggiunge un giocatore alla lista di giocatori e ne ritorna l'id */

        registeredPlayers+=1;
        Player pl = new Player(user, registeredPlayers-1);
        playerList.add( pl );

        System.out.println("New player: " + pl.getUsername());

        return registeredPlayers - 1;
    }

    public LinkedList<Player> getPlayerList(){
        /* returns players' list */

        return playerList;
    }
}
