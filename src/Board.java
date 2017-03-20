package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.lang.Thread;

/**
 * La classe Board permette di istanziare la board con le carte per eseguire il gioco del memory
 */

public class Board extends JFrame {//l'estensione a JFrame mi permette di creare direttamente una finestra grafica
    private List<CardGraphic> cards; // è la lista di carta che verrà rappresentata nella board
    private List<Integer> cardVals;
    private CardGraphic selectedCard; // è un oggetto tmp che mi tiene memorizzato la prima carta quando devo ricercare la seconda
    private CardGraphic c1; // primo oggetto carta che mi serve il confronto
    private CardGraphic c2; // secondo oggetto carta che mi serve il confronto
    private int remainedCards;
    private Timer t; // è un timer che mi rende visibile la coppia di carte matchate (vale nel sia caso in cui il match abbia esito positivo che negativo
    private boolean pair = false;
    private boolean retrievePairs;
    public static Client cl;
    private Player[] players;
    private OnesMove move;
    private boolean turn;
    private final WindowRegistration initialWindow;
    private static ScoringBoard scoring;

    private static JLabel waiting;
    private static JLabel feedback;

    public Board(final WindowRegistration initialWindow) {
        this.initialWindow = initialWindow;
    }

    public void init(String userName) {

        this.turn = false;

        //gestisco l'evento alla chiusura della finstra board (simbolo in alto a sinistra)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExitControl(); //funzione che mi gestisce la chiusura della finestra (spiegata di seguito)
            }
        });

        //creo la menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuImpostazioni = new JMenu("Settings"); // creo il primo menù delle impostazioni
        JMenuItem esci = new JMenuItem("Exit", KeyEvent.VK_Q); // creo l'item di chiusura (VK_Q è il simbolo che mi permette di vedere la shortcut della chiusura)
        KeyStroke crtlQKeyStroke = KeyStroke.getKeyStroke("control Q"); // creo la shortcut CRTL-Q per la chiusura della finestra
        esci.setAccelerator(crtlQKeyStroke); //attacco l'evento all'oggetto item del menu impostazioni
        menuBar.add(menuImpostazioni); //aggiungo il menù impostazioni all'interno della menubar

        esci.setToolTipText("Exit Application");
        esci.addActionListener(new ActionListener() { // attacco la gestione dell'evento all'item esci Al click
            @Override
            public void actionPerformed(ActionEvent e) {
                setExitControl();
            }
        });

        /*gestisco l'evento della chiusura tramite la shortcut
        * In Java quando si gestisce l'evento di tipo keyPressed si hanno sempre tre metodi da settare
        * uno riguardante il tipo di tasto premuto, uno che gestisce l'evento della pressione del tasto,
        * uno che gestisce il rilascio del tasto. Io onde evitare problemi, li ho settati tutti allo stesso
        * modo, in modo tale che eseguano lo stesso metodo.
        */
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


        /*
        Poichè il menu delle impostazioni è molto scarno in quanto c'è solo l'item "esci" se avete delle altre features
        che si possono implementare ditemelo pure
         */

        menuImpostazioni.add(esci); // infine aggiungo l'item esci all'interno del menù delle impostazioni
        setJMenuBar(menuBar); // infine attacco la menubar all'interno della finestra

        //creo il secondo menu delle regole
        JMenu menuRegole = new JMenu("?"); // aggiungo il menù riguardante un minimo di documentazione
        JMenuItem regole = new JMenuItem("Rules"); // creo l'item delle regole
        JMenuItem about = new JMenuItem("About"); // creo l'item riguardante il nostro gruppo
        // gestisco l'evento al click riguardante l'about
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAboutControl(); // tale metodo verrà spiegato nel dettaglio più avanti
            }
        });
        // gestisco l'evento al click riguardante le regole
        regole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegoleControl(); // tale metodo verrà spiegato nel dettaglio più avanti
            }
        });
        menuRegole.add(regole); // aggiungo l'item regole nel menù della documentazione
        menuRegole.add(about); // aggiugneto l'item dell'about al menu della documentazione
        menuBar.add(menuRegole); // aggiungo il menu delle regole all menubar


        /*------popolo la board-------*/
        initialWindow.notifySubscribe();
        cl = new Client(userName, this, initialWindow);
        cardVals = cl.getCardVals();
        players = cl.getPlayers();
        remainedCards = cardVals.size();


        setTitle("Memory - "+ userName);
        Container boardLayout = this.getContentPane(); // mi prendo la porzione di area della finestra che mi serve
        boardLayout.setLayout(new BorderLayout()); // imposto il layout come BorderLayout
        JPanel pane = new JPanel(); // creo il panel per la grid
        scoring = new ScoringBoard(players); // creo la scoring board ( è un extend di JPanel)
        scoring.buildGridForScore();
        boardLayout.add(scoring, BorderLayout.LINE_START);


        cards = new ArrayList<CardGraphic>();  // utilizzo un lista di card per aggiugere le card che verranno contrassegnate

        int i = 0; 
        for (int val : cardVals) { 
            final CardGraphic c = new CardGraphic();
            c.setValue(val); // aggiungo il valore della carta
            c.setId(i); 	// aggiungo la posizione della carta

            // setto la gestione degli eventi per ogni carta
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCard = c; // questo implica che ad ogni click c sarà la selectedCard che andrà a svolgere il matching
                    doTurn(); // spiegato più avanti
                }
            });

            cards.add(c); 
            i++; 
        }

        /*
        * Il timer mi permette di avere un margine di secondi per vedere le carte, di default l'ho settato a 750 ma si può variare
        */
        t = new Timer(750, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCards(true); // questa è la funzione che controlla il matching delle carte
            }
        });

        t.setRepeats(false);

        /*--- posiziono le carte nella board----*/
        //Container pane = this.getContentPane(); // mi prendo l'area del Jframe dove dovrò far visualizzare le carte
        pane.setLayout(new GridLayout(4, 5)); // creo un grid layout
        for (CardGraphic c : cards) { // posiziono le carte (per ID crescenti) all'interno della grid
            c.setImageLogo(); // in fase di inizializzazione della board vogliamo che tutte le carte sia coperte quindi fingo il retro della carta mettendo in tette lo stesso logo
            pane.add(c); // inserisco le card all'interno della gridlayout
        }
        boardLayout.add(pane,BorderLayout.LINE_END);

        /*
        * visualizzo la finestra grafica inserendo tutti i parametri che mi servono
        */
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // questo metodo setta l'impostazione di default alla chiusura della finestra. impostando la flag DO_NOTHING_ON_CLOSE, non si aggiuge nessun comportamento di default, ma lo gestiamo noi con il metodo setExitControl
        setSize(new Dimension(700,675)); // setta la dimensione della finestra (possiamo anche cambiarla)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); // queste due righe mi permettono di centrare la finestra rispetto allao schermo in modo assoluto
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true); // ovviamente rendo visibile la finestra

        lockBoard();
        doClientThread();
    } //---- Fine del costruttore


    //creo Thread Client, durerà fino alla fine della partita.
    public void doClientThread() {
        Thread t2 = new Thread() {
            public synchronized void run() {
                cl.gameStart();
            }
        };
        t2.start();
    }

    // Metodo utilizzato per aggiornare la ui.Il metodo viene chiamato
    // dal client quando riceve nuovi messaggi.
    public void updateInterface(OnesMove move) {

        this.move = move;

        c1 = cards.get(move.getCard1Index());
        c2 = cards.get(move.getCard2Index());
        c1.removeImage();
        c1.setImage();
        c2.removeImage();
        c2.setImage();

        // Utilizzato per rallentare l'animazione
        //Si potrebbe mettere un timer ?
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        checkCards(false);
    }

    /*
    * checkCard() è il metodo che controlla il match delle carte
    */
    private void checkCards(boolean send){
        if(c1.getValue() == c2.getValue()){ // se i valori sono uguali
            c1.setEnabled(false); 
            c2.setEnabled(false); 
            c1.setMatched(true); 
            c2.setMatched(true); 
            
            remainedCards = remainedCards - 2;

            if(remainedCards==0){
                Player playerWin = this.getPlayerWins(players);
                JOptionPane.showMessageDialog(null, "Game Ended -> Your Score is " + String.valueOf(cl.getOwnScore())+
                                                    ". "+playerWin.getUsername() + "wins.");

            }

            if(send){
                pair = true;
                sendMove();
            } 
            
        }else{ // nel caso in cui il matching non ha esito positivo
            c1.setText(""); // non faccio visualizzare nulla alla prima carta (metodo ereditato da JButton)
            c2.setText(""); // non faccio visualizzare nulla alla prima carta (metodo ereditato da JButton)
            c1.setImageLogo(); // reimposto l'immagine del logo
            c2.setImageLogo(); // reimposto l'immagine del logo

            if(send){
                pair = false;
                sendMove();
            } 
        }

        c1 = null; 
        c2 = null; 

    } //--- fine checkCards()


	/*
    * sendMove() notifica la mossa al client
    */
    public void sendMove(){
    	move = new OnesMove(c1.getId(),c2.getId(),pair);
        cl.notifyMove(move);
        lockBoard();
    }


    /*
    * doTurn() è il metodo che mi permette di scoprire le carte infatti ha due condizioni: una per scoprire la prima carta, una per scoprire
    * la seconda carta
    */
    public void doTurn(){

    	// se nessuna delle carte è scoperta
        if(c1 == null  && c2 == null){ 
            c1 = selectedCard; 
            c1.removeImage(); // rimuovo l'immagine del logo
            c1.setImage(); 	// imposto l'immagine riferita alla carta
            //System.out.println(c1.getId()); // TEMPORANEO: stampo l' ID della carta

        }

		// se viene selezionata la seconda carta
        if(c1 != null && c1 != selectedCard && c2 == null){ 
            c2 = selectedCard; 
            c2.removeImage(); // rimuovo il logo
            c2.setImage(); // imposto l'immagine riferita alla carta
            //System.out.println(c2.getId()); // TEMPORANEO: stampo l' ID della carta
            t.start(); // faccio avviare il timer per la visualizzazione della carta
        }
    } //---- fine doTurn()
        


    /* ----metodi per la gestione dell'intefaccia-------*/
    private void setExitControl(){ // imposta l'uscita dalla finestra visualizzator un alert
        int input = JOptionPane.showOptionDialog(null, // root che apre l'alert (in questo caso non c'è bisogno di specificarne uno)
                "Are you sure you want to exit the game?", // il messaggio
                "Exit", // titolo della finestra alert
                JOptionPane.YES_NO_OPTION, // tipo di bottoni dell'alert
                JOptionPane.INFORMATION_MESSAGE, // tipo di messaggio
                null,null,null); // altri parametri che non servono a nulla
        if(input == JOptionPane.YES_OPTION) { // se viene clickato il bottone SI faccio un exit(0) brutale!
            System.exit(0);
        }

    }

    private void setAboutControl(){ // mi visualizza l'alert per le info sull'about
        JOptionPane.showOptionDialog(null,
                "Memory Game was realized by: Salvatore Alescio,\n" +
                        "Alice Valentini, Andrea Zuccarini. For \n" +
                        "Distributed Systems' project.",
                "Exit",
                JOptionPane.CLOSED_OPTION, // il bottone è solo per il chiudi
                JOptionPane.INFORMATION_MESSAGE, // tipo di messaggio
                null,null,null);
    }

    private void setRegoleControl(){ // mi visualizza l'alert per le info sulle regole
        JOptionPane.showOptionDialog(null,
                "Memory, noto anche come coppie, è un popolare gioco di carte che richiede concentrazione e memoria.\n" +
                        "Nel gioco, le carte sono inizialmente mescolate e disposte coperte sul tavolo. I giocatori, a turno,\n"+
                        "scoprono due carte;  se queste formano una \"coppia\", vengono incassate dal giocatore di turno, che può scoprirne altre due;\n" +
                        "altrimenti, vengono nuovamente coperte e rimesse nella loro posizione originale sul tavolo, e il turno passa al prossimo giocatore.\n"+
                        "Vince il giocatore che riesce a scoprire più coppie.",
                "Exit",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);

    }

    /*----metodo che blocca le carte----*/
    public void lockBoard(){
        for (CardGraphic c : cards) {
            c.setEnabled(false); // disabilita tutti bottoni delle carte
        }
    }

    /*----metodo che sblocca le carte----*/
    public void unlockBoard(){
        for (CardGraphic c : cards) {
            if (c.isMatched()==false) c.setEnabled(true); // abilita tutti i bottoni delle carte
        }
    }

    public void incPointPlayer(int nodeId, int score) {
        scoring.setPlayerScore(nodeId, score);
    }

    public void setCurrentPlayer(int id){
        scoring.setCurrentPlayer(id);
    }

    public int getRemainedCards(){ return remainedCards; }

    //metodo che mi permette di sapere chi ha vinto (funziona solo con i player che hanno perso, non capisco perchè)
    public Player getPlayerWins(Player[] players){
        List<Integer> tmp = new ArrayList<Integer>();
        for(int i = 0; i<players.length; i++)
            tmp.add(players[i].getPoints());
        System.out.println(tmp);
        int idx = tmp.indexOf(Collections.max(tmp));
        System.out.println(idx);
        return players[idx];
    }

}


