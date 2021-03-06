package src;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
La classe player estende la classe node per aggiungere funzionalità 
specifiche del giocatore (username,points)
*/

public class Player extends Node {
    private String username;
    private int points;
    
    public Player(String user, String host, int port) throws UnknownHostException {
        this(user,InetAddress.getByName(host),port);
    } 

    public Player(String user,InetAddress inetAddr, int port){
        super(inetAddr, port);
        this.username = user;
        this.points = 0;
    }

    public String getUsername(){ return username; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public void incPoints(){ points++; }

}
