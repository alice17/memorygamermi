/**
 * Created by alice on 02/02/17.
 */

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
import static java.lang.Thread.sleep;

public class Client {

    private Client() {}

    public static void main(String[] args) {
        int myId;

        try{
            System.out.println("Looking up server...");

            String url = "rmi://127.0.0.1:1099/Server";
            ServerInterface srv = (ServerInterface) Naming.lookup(url);
            System.out.println("Server found at address " + url);

            // add client to player list
            myId = srv.newPlayer("username");

            System.out.println("Added to player list, your id is: " + myId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
