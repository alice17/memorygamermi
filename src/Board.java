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
 * La classe Board permette di istanziare la board con le carte per eseguire il gioco del memory
 */

public class Board extends JFrame { //l'estensione a JFrame mi permette di creare direttamente una finestra grafica
    private List<CardGraphic> cards; // è la lista di carta che verrà rappresentata nella board
    private CardGraphic selectedCard; // è un oggetto tmp che mi tiene memorizzato la prima carta quando devo ricercare la seconda
    private CardGraphic c1; 
    private CardGraphic c2; 
    private Timer t; // è un timer che mi rende visibile la coppia di carte matchate (vale nel sia caso in cui il match abbia esito positivo che negativo
    private Score myScore = new Score(); // è l'oggetto che mi tiene aggiornato lo score del player


    public Board(Deck deck) {
        /*----creo la struttura della board------*/
        setTitle("Memory"); 

        //gestisco l'evento alla chiusura della finstra board (simbolo in alto a sinistra)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExitControl(); //funzione che mi gestisce la chiusura della finestra (spiegata di seguito)
            }
        });

        //creo la menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuImpostazioni = new JMenu("Impostazioni"); // creo il primo menù delle impostazioni
        JMenuItem esci = new JMenuItem("Esci", KeyEvent.VK_Q); // creo l'item di chiusura (VK_Q è il simbolo che mi permette di vedere la shortcut della chiusura)
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
        JMenuItem regole = new JMenuItem("Regole"); // creo l'item delle regole
        JMenuItem about = new JMenuItem("about"); // creo l'item riguardante il nostro gruppo
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
        List<CardGraphic> cardLists = new ArrayList<CardGraphic>();  // utilizzo un lista di card per aggiugere le card che verranno contrassegnate
        List<Integer> myDeck = deck.getDeck(); // istanzio un lista per recuperare i valori delle carte dalla classe  Deck

        int i = 0; // questo variabile contatore mi permette di aggiugere gli ID univoci alla carte
        for (int val : myDeck) { // eseguo il foreach
            final CardGraphic c = new CardGraphic();
            c.setValue(val); // aggiungo il valore della carta
            c.setId(i); // aggiungo la posizione della carta
            // setto la gestione degli eventi per ogni carta
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCard = c; // questo implica che ad ogni click c sarà la selectedCard che andrà a svolgere il matching
                    doTurn(); // spiegato più avanti
                }
            });
            cardLists.add(c); // aggiungo la carta alla lista
            i++; // incremento l'ID per la carta successiva
        }

        this.cards = cardLists; // una volta finito referenzio l'oggetto cardList  alla lista globale cards

        /*
        * Il timer mi permette di avere un margine di secondi per vedere le carte, di default l'ho settato a 750 ma si può variare
        */
        t = new Timer(750, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCards(); // questa è la funzione che controlla il matching delle carte
            }
        });

        t.setRepeats(false); // con questo metodo non si vuole far ripetere il timer (di default è settato a true)

        /*--- posiziono le carte nella board----*/
        Container pane = this.getContentPane(); // mi prendo l'area del Jframe dove dovrò far visualizzare le carte
        pane.setLayout(new GridLayout(4, 5)); // creo un grid layout
        
        for (CardGraphic c : cards) { 	// posiziono le carte (per ID crescenti) all'interno della grid
            c.setImageLogo(); 			
            pane.add(c); 
        }

        /*
        * visualizzo la finestra grafica inserendo tutti i parametri che mi servono
        */
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setSize(new Dimension(700,675)); 
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); // queste due righe mi permettono di centrare la finestra rispetto allao schermo in modo assoluto
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true); // ovviamente rendo visibile la finestra
    } 


    /*
    * doTurn() è il metodo che mi permette di scoprire le carte infatti ha due condizioni: una per scoprire la prima carta, una per scoprire
    * la seconda carta
    */
        public void doTurn(){
            if(c1 == null  && c2 == null){ // se nessuna delle carte è scoperta
                c1 = selectedCard; 
                c1.removeImage(); 
                c1.setImage(); 
                System.out.println(c1.getId()); // TEMPORANEO: stampo l' ID della carta

            }
            if(c1 != null && c1 != selectedCard && c2 == null){ // se viene selezionata la seconda carta
                c2 = selectedCard; 
                c2.removeImage(); 
                c2.setImage(); 
                System.out.println(c2.getId()); // TEMPORANEO: stampo l' ID della carta
                t.start(); // faccio avviare il timer per la visualizzazione della carta
            }

        }

        /*
        * checkCard() è il metodo che controlla il match delle carte
        */
        public void checkCards(){
            if(c1.getValue() == c2.getValue()){ 
                c1.setEnabled(false); 
                c2.setEnabled(false); 
                c1.setMatched(true); 
                c2.setMatched(true); /
                myScore.updateScore(); // vado ad eseguire l'update dello score riferito al player

                if(this.isGameWon()){ // metodo che mi verifica se tutte le carte sono state effettivamente matchate
                    JOptionPane.showMessageDialog(this, "Hai vinto!!! " + String.valueOf(myScore.getScore()) +" punti");
                }
            }
            else{ // nel caso in cui il matching non ha esito positivo
                c1.setText(""); 
                c2.setText(""); 
                c1.setImageLogo(); 
                c2.setImageLogo(); 
            }
            c1 = null; 
            c2 = null; 

        } 

        /*
         * isGameWon() è un metodo che verifica se il gioco è realmente finito (sicuramente migliorabile in modo distribuito).
         * In poche parole, controlla il valore booleano di ogni carta: se almeno uno è false allora ritorna false, altrimenti il gioco è finito
         */
        public boolean isGameWon(){
            for(CardGraphic c : this.cards){
                if(c.getMatched() == false){
                    return false;
                }
            }
            return true;
        } 

    /* ----metodi per la gestione dell'intefaccia-------*/
    private static void setExitControl(){ // imposta l'uscita dalla finestra visualizzator un alert
        int input = JOptionPane.showOptionDialog(null, // root che apre l'alert (in questo caso non c'è bisogno di specificarne uno)
                "Sicuro di voler uscire del gioco?", // il messaggio
                "Esci", // titolo della finestra alert
                JOptionPane.YES_NO_OPTION, // tipo di bottoni dell'alert
                JOptionPane.INFORMATION_MESSAGE, // tipo di messaggio
                null,null,null); // altri parametri che non servono a nulla
        if(input == JOptionPane.YES_OPTION) { // se viene clickato il bottone SI faccio un exit(0) brutale!
            System.exit(0);
        }

    }


    private static void setAboutControl(){ // mi visualizza l'alert per le info sull'about
        JOptionPane.showOptionDialog(null,
                "Il gioco di memory è stato realizzato da: Salvatore Alescio,\n" +
                        "Alice Valentini, Andrea Zuccarini. Per il progetto di \n" +
                        "Sistemi Distribuiti",
                "Esci",
                JOptionPane.CLOSED_OPTION, // il bottone è solo per il chiudi
                JOptionPane.INFORMATION_MESSAGE, // tipo di messaggio
                null,null,null);
    }

    private static void setRegoleControl(){ // mi visualizza l'alert per le info sulle regole
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
    /*----metodo che blocca le carte----*/
    public void lockBoard(){
        for (CardGraphic c : cards) {
            c.setEnabled(false); // disabilita tutti bottoni delle carte
        }
    }
    /*----metodo che sblocca le carte----*/
    public void unlockBoard(){
        for (CardGraphic c : cards) {
            c.setEnabled(true); // abilita tutti i bottoni delle carte
        }
    }



}


