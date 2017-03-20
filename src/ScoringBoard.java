package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.swing.border.EmptyBorder;

/**
 * 
 */
public class ScoringBoard extends JPanel {
    private String username;
    private Player[] allPlayers;
    private JLabel[] lbScore ;

    public ScoringBoard(Player[] players) {
        this.allPlayers = players;
        lbScore = new JLabel[allPlayers.length];
    }


    public void buildGridForScore() {
        setLayout(new GridLayout(3, 1)); // setto il Layout della board
        setBorder(new EmptyBorder(0, 30, 500, 0));
        createLabelScore();
    }

    // aggiorna il giocatore corrente colorando la label
    public void setCurrentPlayer(int id){
        for(int i=0; i < allPlayers.length; i++){
            if(i==id) lbScore[id].setForeground(Color.red);
            else lbScore[id].setForeground(Color.black);
        }
    }

    public void setPlayerScore(int nodeId, int score){
        lbScore[nodeId].setText(allPlayers[nodeId].getUsername() + ": " + score + " Punti");
    }

    public void createLabelScore() {
        for (int i = 0; i < allPlayers.length; i++) {
            lbScore[i] = new JLabel(allPlayers[i].getUsername() + ": " + allPlayers[i].getPoints() + " Punti");
            add(lbScore[i]);

        }
    }

}
