
package src;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.LinkedList;
import java.util.Arrays;

public class Client {

    private static LinkedList<Player> playerList = new LinkedList<>();
    private static ServerInterface srv;
    public static final int PORT = 1099;
    private static Game game;
    private static Player[] players;
    private static int nodeId;
    private static Link link;
    

    public static void main(String[] args) {

        InetAddress localHost = null;
        
        try{
            localHost = InetAddress.getLocalHost();
            System.out.println("Local host is " + localHost);
        } catch (UnknownHostException uh){
            System.exit(1);
        }
        String playerName = args[0];
        String server = "localhost";
        int port = PORT;
        if (args.length > 1)
            port = Integer.parseInt(args[1]);

        /*if (System.getSecurityManager() == null)
            System.getSecurityManager(new RMISecurityManager());
        else
            System.out.println("Security Manager not starts.");*/

        Player me = new Player(playerName, localHost, port);

        Partecipant partecipant = null;
        boolean result = false;

        /* establish connection with server */
        try{

            partecipant = new Partecipant();
            System.out.println("Looking up server...");
            String url = "rmi://" + server +":" + PORT + "/Subscribe";
            ServerInterface subscribe = (ServerInterface)Naming.lookup(url);
            System.out.println("Server found at address " + url);

            result = subscribe.subscribeAccepted(partecipant, me);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (result) {

           
          System.out.println("You have benn added to player list");
          players = partecipant.getPlayers();
          for (int i=0; i<players.length;i++){

                System.out.println(players[i].getUsername());
            }
           link = new Link(me, players);
           nodeId = link.getNodeId();
           System.out.println("My id is " + nodeId + " and my name is " + players[nodeId].getUsername());
           System.out.println("My left neighbour is " + players[link.getLeftId()].getUsername());
           System.out.println("My right neighbour is " + players[link.getRightId()].getUsername()); 

           //game = newGame(tutti i dati che defineremo per il gioco)

           //gameStart();

        }

        
    }
}
