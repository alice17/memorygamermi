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
    public LinkedList<Player> playerList = new LinkedList<Player>();      // lista di giocatori
    private int registeredPlayers = 0;                                // numero di giocatori totali registrati
    public static boolean gotPlayers = false;
    public Object lock = new Object();

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


        /* wait for sec mseconds for the players' registration */

        // creo oggetto Game e lo inserisco nell'RMI registry

        try {
            GameInterface igame = new Game();
            GameInterface igamestub = (GameInterface) UnicastRemoteObject.exportObject(igame ,0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("game", igamestub);

            igamestub.waitServer();

            /*
            Game igame = new Game();
            igame.setGotPlayers(false);


            Registry registry = LocateRegistry.createRegistry(1098);
            registry.bind("game", igame);
            GameInterface game = (GameInterface) registry.lookup("game");
            System.out.println(System.identityHashCode(igame));
            game.waitServer();
            */

            //game.setGotPlayers(true);
        }catch(Exception e){
            e.printStackTrace();
        }


        // run the wait thread
        //WaitThread wt = new WaitThread();
        //new Thread(wt).start();

        /*
        int sec = 60000;
        System.out.println("Wait for " + sec + " ms");

        try {
            sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Wait ended.");

        igame.setGotPlayers(true);
        //notifyAll();
        */

        // setto variabili gioco

        // distribuisco lista di Player
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
}
