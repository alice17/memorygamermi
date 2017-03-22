
package src;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;


public class Client  {

    public final int PORT = 1099;
    private Game game;
    private Player[] players;
    private Node me;
    private int nodeId;
    private Link link;
    private int playersNo;
    private MessageBroadcast messageBroadcast;
    private MessageFactory mmaker;
    private RouterFactory rmaker;
    private BlockingQueue<GameMessage> buffer;
    private int[] processedMsg;
    private String playerName;
    private List<Integer> cardVals;
    private final Board board;
    private OnesMove move;
    private final WindowRegistration initialWindow;

    public Client (String username, final Board board, final WindowRegistration initialWindow){
        this.board = board;
        this.playerName = username;
        this.initialWindow = initialWindow;
        inizializeGame();
    }

    private void inizializeGame() {

        InetAddress localHost = null;

        try{
            localHost = InetAddress.getLocalHost();
            System.out.println("Local host is " + localHost);
        } catch (UnknownHostException uh){
            uh.printStackTrace();
            System.exit(1);
        }

        String server = "localhost";
        Random random = new Random();
        int port = random.nextInt(100)+2001;

        /*if (System.getSecurityManager() == null)
            System.getSecurityManager(new RMISecurityManager());
        else
            System.out.println("Security Manager not starts.");*/

        messageBroadcast = null;
        buffer = new LinkedBlockingQueue<GameMessage>();


        try {
            LocateRegistry.createRegistry(port);
        /*
        	try{
            	LocateRegistry.createRegistry(port);
            } catch (RemoteException ee) {
            	LocateRegistry.getRegistry(port);
            }
        */
            messageBroadcast = new MessageBroadcast (buffer);
            String serviceURL = "rmi://" + localHost.getCanonicalHostName() + ":" + port + "/Broadcast";
            System.out.println("Registering message broadcast service at " + serviceURL);
            Naming.rebind(serviceURL,messageBroadcast);
        } catch (RemoteException rE) {
            rE.printStackTrace();
        } catch (MalformedURLException murlE) {
            murlE.printStackTrace();
        }


        Partecipant partecipant = null;
        boolean result = false;

        /* establish connection with server */
        try{
            partecipant = new Partecipant();
            System.out.println("Looking up subscribe service...");
            String url = "rmi://" + server +":" + PORT + "/Subscribe";
            SubscribeInterface subscribe = (SubscribeInterface) Naming.lookup(url);
            System.out.println("Subscribe service found at address " + url);


            result = subscribe.subscribeAccepted(partecipant, playerName, localHost, port);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (result) {

            // subscribe accepted
            initialWindow.notifySubscribe();
            System.out.println("You have been added to player list.");
            players = partecipant.getPlayers();
            playersNo = players.length;

            if( playersNo > 1 ){

                cardVals = partecipant.getCardVals();
                System.out.println("Card list acquired");
                initialWindow.notifyGameStart();
                System.out.println("Players subscribed:");

                for (int i=0; i < playersNo;i++){
                    System.out.println(players[i].getUsername());
                }

                me = new Node(localHost, port);
                link = new Link(me, players);
                nodeId = link.getNodeId();
                processedMsg = new int[players.length];
                Arrays.fill(processedMsg, 0);
                rmaker = new RouterFactory(link);
                mmaker = new MessageFactory(nodeId,processedMsg);
                messageBroadcast.configure(link,rmaker,mmaker);

                System.out.println("My id is " + nodeId + " and my name is " + players[nodeId].getUsername());
                System.out.println("My left neighbour is " + players[link.getLeftId()].getUsername());
                System.out.println("My right neighbour is " + players[link.getRightId()].getUsername());

                game = new Game(playersNo);

            }else{
                initialWindow.notifyErrorGameStart();
                System.out.println("Not enough players to start the game. :(");
                System.exit(0);
            }
        } else {
            initialWindow.notifyErrorSubscribe();
            System.out.println("Game subscribe unsuccessful. Exit the game.");
            System.exit(0);
        }
    }

    public synchronized void gameStart() {
        //Inizio gioco
        //Il thread looperà dentro a gameStart fino alla fine del gioco.
        tryToMyturn();

        while(board.getRemainedCards() > 0) {
            try {
                //Eseguo quando non è il mio turno,sto in ascolto di messaggi sul buffer. 
                
                boolean repeat = true;
                System.out.println("Waiting up to " + getWaitSeconds() + " seconds for a message..");
                GameMessage m = buffer.poll(getWaitSeconds(), TimeUnit.SECONDS);

                if(m != null) {

                    System.out.println("Processing message " + m);
                    //Incremento messageCounter
                    // Per ora ho creato due messagecounter, uno in Messagefactory(questo) e uno in MessageBroadcast.
                    // Sincronizzati alla grezza.
                    // In Uno vecchio usa un oggetto condiviso tra il client e l'interfaccia remota
                    // più o meno come la callback usando i lock, sarebbe utilissimo.mistero.Da provare.
                    mmaker.incMessageCounter();
                    System.out.println("Message counter factory " + mmaker.getMessageCounter());
                    // recupero la mossa dal messaggio che mi è arrivato
                    move = m.getMove();
                    //Questo array tiene conto dell'ultimo messaggio processato per ogni nodo
                    //Per ora non serve a niente, sarò utile coi crash per vedere qual'è il nodo
                    // con la vista dei messaggi spediti più recente.
                    processedMsg[m.getOrig()] = m.getId();

                    // analizza la mossa contenuta nel messaggio
                    if(m.getPair() == false) {
                        // Incremento l'id del giocatore attuale.
                        game.setCurrentPlayer((game.getCurrentPlayer()+1) % players.length);
                        board.setCurrentPlayer( game.getCurrentPlayer() );
                    } else {
                        players[m.getOrig()].incPoints();
                        board.incPointPlayer(m.getOrig(),players[m.getOrig()].getPoints());
                    }
                    
                    //Passo alla board la mossa per aggiornare la ui.
                    board.updateInterface(move);

                    System.out.println("The next player is " + game.getCurrentPlayer());

                    tryToMyturn();
                } else {
                     System.out.println("Timeout");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void tryToMyturn() {

        while (game.getCurrentPlayer() == nodeId) {

            //Quando è il mio turno sblocco la board e rimango in attesa della mossa
            //Direi che questo wait() sia quasi obbligatorio se lo vogliamo strutturare così.
            //L oggetto Client si blocca un attimo ma la classe remota RMI MessageBroadcast può ancora
            // ricevere messaggi, appena il client si riattiva può ritornare in ascolto sul buffer per vedere
            // se ci sono messaggi.Se ce ne sono va ad aggiornare l interfaccia locale.

            System.out.println("Unlock board.");
            board.unlockBoard();
            System.out.println("I'm trying to do a move");

            try{
                System.out.println("Wait move");
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            if (move.getPair() == false) {
                //Quando viene notificata la mossa viene ribloccata la board.
                //board.lockBoard();
                //Incremento il prossimo giocatore che deve giocare.In locale lo faccio qua.
                game.setCurrentPlayer((game.getCurrentPlayer()+1) % players.length);
                board.setCurrentPlayer( game.getCurrentPlayer() );
            } else {
                players[nodeId].incPoints();
                board.incPointPlayer(nodeId, players[nodeId].getPoints());
            }
            board.lockBoard();
            // Aumento il message counter, questo bisognerebbe cambiarlo per usare un 
            // oggetto condiviso tra la classe client e il MessageBroadcast per tenere
            // sincronizzati i messaggi che arrivano e quelli che vengono spediti.
            messageBroadcast.incMessageCounter();

            //spedisco il messaggio sulla classe remota del mio vicino destro tramite RMI.
            messageBroadcast.send(mmaker.newGameMessage(move));
            System.out.println("Message counter factory " + mmaker.getMessageCounter());
            System.out.println("Next Player is " + players[game.getCurrentPlayer()].getUsername() + " id " + game.getCurrentPlayer());

        }

    }

    private long getWaitSeconds() {
        return 10L + nodeId * 2;
    }

    public synchronized Player[] getPlayers() {
    // restituisce la lista di player appena scaduto il timeout
        if (players == null) {
            try{
                System.out.println("Waiting other players");
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        return players;
    }

    //Quando il giocatore ha fatto la sua mossa, la board lo notifica al client
    //che la deve impacchettare in un messaggio da spedire.
    public synchronized void notifyMove(OnesMove move) {
            this.move = move;
            System.out.println("Notify move");
            notifyAll();
    }

    public List<Integer> getCardVals(){ return cardVals; }

    public int getOwnScore() { 
        if(players!=null){
            return players[nodeId].getPoints(); 
        }else{
            return 0;
        }
    }
}
