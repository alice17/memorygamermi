
package src;


import java.net.InetAddress;
import java.net.UnknownHostException;



public class Player extends Node {
    private String username;
    private int points;
    


    public Player(String user, String host, int port)
            throws UnknownHostException {
        this(user,InetAddress.getByName(host),port);
    } 

    public Player(String user,InetAddress inetAddr, int port){
        super(inetAddr, port);
        this.username = user;
    }

    public String getUsername(){ return username; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

}
