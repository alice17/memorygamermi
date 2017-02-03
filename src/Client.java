/**
 * Created by alice on 02/02/17.
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;

public class Client {

    private Client() {}

    public static void main(String[] args) {
        int myId;

        try{
            System.out.println("Looking up server...");

            ServerInterface srv = (ServerInterface) Naming.lookup("rmi://127.0.0.1:1099/Server");
            System.out.println("Server found.");

            // add client to player list
            myId = srv.newPlayer("username");

            System.out.println("Added to player list, your id is: " + myId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
