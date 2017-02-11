

package src;


import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;




public class Server {

    public static final int PORT = 1099;
    public static LinkedList<Player> playerList = new LinkedList<>(); //non utilizzato
    private static int registeredPlayers;  //non utilizzato 
    public final Object lock = new Object(); // non utilizzato

    public static void main(String[] args) {


        final int seconds = Integer.parseInt(args[0]);
        final int maxPlayers = 10;
        /* setting up connection */
        System.out.println("Launching server...");

        //Security Manager
        /*if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
        else
            System.out.println("SecurityManager doesn't activate.");*/

        try {
            final Subscribe s = new Subscribe(maxPlayers);
            LocateRegistry.createRegistry(PORT);
            final String nameUrl = "rmi://localhost:" + PORT + "/Subscribe";
            Naming.rebind(nameUrl, s);
            System.out.println("Connection established.");

            Thread t = new Thread() {
                public void run() {
                    try {
                        sleep(seconds * 1000);
                        s.endSubscribe();
                        Naming.unbind(nameUrl);
                        System.out.println("Subscribes ended,service is down.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
            Player[] players = s.getPlayers();
            if (s.getPlayersNo() > 0) {
                System.out.println("Players list -->");
                for (int i = 0; i< s.getPlayersNo(); i++) {
                    Player p = players[i];
                    System.out.println("Player " + (i + 1) + " " 
                                        + p.getUsername() + " ("
                                        + p.getInetAddress().getHostAddress()
                                        + ":" + p.getPort() + ")");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    } 
}
