package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
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


    public Board(Deck deck) {
        /*----creo la struttura della board------*/
        setTitle("Memory");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExitControl();
            }
        });

        //creo la menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuImpostazioni = new JMenu("Impostazioni");
        JMenuItem esci = new JMenuItem("Esci", KeyEvent.VK_Q);
        KeyStroke crtlQKeyStroke = KeyStroke.getKeyStroke("control Q");
        esci.setAccelerator(crtlQKeyStroke);
        menuBar.add(menuImpostazioni);

        esci.setToolTipText("Exit Application");
        esci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExitControl();
            }
        });

        esci.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();

            }

            @Override
            public void menuKeyPressed(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();

            }
        });


        menuImpostazioni.add(esci);
        setJMenuBar(menuBar);

        //creo il secondo menu delle regole
        JMenu menuRegole = new JMenu("?");
        JMenuItem regole = new JMenuItem("Regole");
        JMenuItem about = new JMenuItem("about");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAboutControl();
            }
        });

        regole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegoleControl();
            }
        });
        menuRegole.add(regole);
        menuRegole.add(about);
        menuBar.add(menuRegole);


        /*------popolo la board-------*/
        List<CardGraphic> cardLists = new ArrayList<CardGraphic>();
        List<Integer> myDeck = deck.getDeck();

        int i = 0;
        for (int val : myDeck) {
            final CardGraphic c = new CardGraphic();
            c.setValue(val); // aggiungo il valore della carta
            c.setId(i); // aggiungo la posizione della carta
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCard = c;
                    doTurn();
                }
            });
            cardLists.add(c);
            i++; // incremento la posizione della carta
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

        /*-------Visualizzo la board----------*/
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(700,675));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true);
    }

        public void doTurn(){
            if(c1 == null  && c2 == null){
                c1 = selectedCard;
                c1.removeImage();
                c1.setImage();
                System.out.println(c1.getId());
                //c1.setText(String.valueOf(c1.getId()));
            }
            if(c1 != null && c1 != selectedCard && c2 == null){
                c2 = selectedCard;
                c2.removeImage();
                //c2.setText(String.valueOf(c2.getId()));
                c2.setImage();
                System.out.println(c2.getId());
                t.start();
            }

        }

        public void checkCards(){
            if(c1.getValue() == c2.getValue()){
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

    /* ----metodi per la gestione dell'intefaccia-------*/
    private static void setExitControl(){
        int input = JOptionPane.showOptionDialog(null,
                "Sicuro di voler uscire del gioco?",
                "Esci",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);
        System.out.println(input);
        if(input == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }


    private static void setAboutControl(){
        JOptionPane.showOptionDialog(null,
                "Il gioco di memory è stato realizzato da: Salvatore Alescio,\n" +
                        "Alice Valentini, Andrea Zuccarini. Per il progetto di \n" +
                        "Sistemi Distribuiti",
                "Esci",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);
    }

    private static void setRegoleControl(){
        JOptionPane.showOptionDialog(null,
                "Memory, noto anche come coppie, è un popolare gioco di carte che richiede concentrazione e memoria.\n" +
                        "Nel gioco, le carte sono inizialmente mescolate e disposte coperte sul tavolo. I giocatori, a turno,\n"+
                        "scoprono due carte;  se queste formano una \"coppia\", vengono incassate dal giocatore di turno, che può scoprirne altre due;\n" +
                        "altrimenti, vengono nuovamente coperte e rimesse nella loro posizione originale sul tavolo, e il turno passa al prossimo giocatore.\n"+
                        "Vince il giocatore che riesce a scoprire più coppie.",
                "Esci",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);

    }



}


