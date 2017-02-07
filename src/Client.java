/**
 * Created by alice on 02/02/17.
 */

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;

public class Client {

    private Client() {}

    public static void main(String[] args) {
        int myId;


        /* establish connection with server */
        try{
            System.out.println("Looking up server...");

            String url = "rmi://127.0.0.1:1099/Server";
            ServerInterface srv = (ServerInterface) Naming.lookup(url);
            System.out.println("Server found at address " + url);

            // add client to Server's player list
            myId = srv.newPlayer("username");
            System.out.println("Added to player list, your id is: " + myId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* wait for other players */
        try{
            Registry registry = LocateRegistry.getRegistry(1099);
            GameInterface game = (GameInterface) registry.lookup("game");

            System.out.println("Game found at server.");
            System.out.println("Waiting for other players...");

            game.waitClient();
            System.out.println("Wait ended. Let's start the game!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
