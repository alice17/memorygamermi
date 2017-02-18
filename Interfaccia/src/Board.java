package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Created by salvatorealescio on 05/02/17.
 */

public class Board extends JFrame {
    private List<CardGraphic> cards;
    private CardGraphic selectedCard;
    private CardGraphic c1;
    private CardGraphic c2;
    private Timer t;
    private Score myScore = new Score();

    public Board() {
        int pairs = 10;
        List<CardGraphic> cardLists = new ArrayList<CardGraphic>();
        List<Integer> cardVals = new ArrayList<Integer>();


        for (int i = 0; i < pairs; i++) {
            cardVals.add(i);
            cardVals.add(i);
        }

        Collections.shuffle(cardVals);

        for (int val : cardVals) {
            final CardGraphic c = new CardGraphic();
            c.setId(val);
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCard = c;
                    doTurn();
                }
            });
            cardLists.add(c);
        }

        this.cards = cardLists;

        t = new Timer(750, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCards();
            }
        });

        t.setRepeats(false);

        Container pane = this.getContentPane();
        pane.setLayout(new GridLayout(4, 5));
        for (CardGraphic c : cards) {
            c.setImageLogo();
            pane.add(c);
        }
    }

        public void doTurn(){
            if(c1 == null  && c2 == null){
                c1 = selectedCard;
                c1.removeImage();
                c1.setImage();
                //c1.setText(String.valueOf(c1.getId()));
            }
            if(c1 != null && c1 != selectedCard && c2 == null){
                c2 = selectedCard;
                c2.removeImage();
                //c2.setText(String.valueOf(c2.getId()));
                c2.setImage();
                t.start();
            }

        }

        public void checkCards(){
            if(c1.getId() == c2.getId()){
                c1.setEnabled(false);
                c2.setEnabled(false);
                c1.setMatched(true);
                c2.setMatched(true);
                myScore.updateScore();

                if(this.isGameWon()){
                    JOptionPane.showMessageDialog(this, "Hai vinto!!! " + String.valueOf(myScore.getScore()) +" punti");

                }
            }
            else{
                c1.setText("");
                c2.setText("");
                c1.setImageLogo();
                c2.setImageLogo();
            }
            c1 = null;
            c2 = null;

        }

        public boolean isGameWon(){
            for(CardGraphic c : this.cards){
                if(c.getMatched() == false){
                    return false;
                }
            }
            return true;
        }

}
