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
 * Created by salvo92 on 06/02/17.
 */
public class ScoringBoard extends JPanel {
    private int playerScore;
    private String username;
    private Player[] allPlayers;
    private JLabel lbScore;

    public ScoringBoard(Player[] players) {
        this.allPlayers = players;
    }


    public void buildGridForScore() {
        setLayout(new GridLayout(3, 1)); // setto il Layout della board
        setBorder(new EmptyBorder(0, 30, 500, 0));
        createLabelScore();
        //updateScore();
    }

    public void setPlayerScore(int score){
        this.playerScore = score;
    }

    public void createLabelScore() {
        for (int i = 0; i < allPlayers.length; i++) {
            lbScore = new JLabel(allPlayers[i].getUsername() + ": " + allPlayers[i].getPoints() + " Punti");
            add(lbScore);

        }


    }

   /* public void updateScore(){
        for (int i = 0; i < allPlayers.length; i++) {
            lbScore[i].setText(allPlayers[i].getUsername() + ": " + allPlayers[i].getPoints() + " Punti");
        }

    }*/
}




