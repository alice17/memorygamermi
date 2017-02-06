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

        try{
            //GameInterface game = (GameInterface) Naming.lookup("game");
            Registry registry = LocateRegistry.getRegistry(1098);
            GameInterface game = (GameInterface) registry.lookup("game");

            System.out.println("Game found at server.");

            // wait for the game to start
            System.out.println("Waiting for other players...");

            synchronized (game){
                try {
                    game.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Wait ended. Let's start the game!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
