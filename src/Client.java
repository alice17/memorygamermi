
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
    private int currentPlayer = 0;
    private Player[] players;
    private Node me;
    private int nodeId;
    private Link link;
    private int playersNo;
    private int activePlayersNo;    // numero di giocatori attivi
    public static MessageBroadcast messageBroadcast;
    private MessageFactory mmaker;
    private RouterFactory rmaker;
    private BlockingQueue<GameMessage> buffer;
    public int[] processedMsg;
    private String playerName;
    private List<Integer> cardVals;
    public final Board board;
    private OnesMove move;
    private final WindowRegistration initialWindow;
    private int rightId;

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
            messageBroadcast = new MessageBroadcast (buffer,this);
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
            activePlayersNo = playersNo;

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
                rmaker = new RouterFactory(link);
                mmaker = new MessageFactory(nodeId);
                messageBroadcast.configure(link,rmaker,mmaker);

                System.out.println("My id is " + nodeId + " and my name is " + players[nodeId].getUsername());
                System.out.println("My left neighbour is " + players[link.getLeftId()].getUsername());
                System.out.println("My right neighbour is " + players[link.getRightId()].getUsername());
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
                //board.setCurrentPlayer(game.getCurrentPlayer());
                board.setCurrentPlayer(currentPlayer);
                boolean repeat = true;
                int nextPlayer = 0;
                System.out.println("Waiting up to " + getWaitSeconds() + " seconds for a message..");
                GameMessage m = buffer.poll(getWaitSeconds(), TimeUnit.SECONDS);

                if(m != null) {

                    System.out.println("Processing message " + m);
                    // recupero la mossa dal messaggio che mi è arrivato
                    move = m.getMove();

                    System.out.println("Message from Node " + m.getFrom());

                    // Controlla se è un messaggio di crash oppure di gioco
                    System.out.println(m.getNodeCrashed());
                    if(m.getNodeCrashed() != -1) {

                        System.out.println("Crash Message");
                        link.nodes[m.getNodeCrashed()].setNodeCrashed();
                        board.updateCrash(m.getNodeCrashed());
                        retrieveNextPlayerCrash();

                    } else {

                        if(m.getPair() == false) {
                                // se il giocatore ha effettuato la mossa
                                if(move.getCard1Index()>0 && move.getCard2Index()>0)
                                        board.updateInterface(move);
                                retrieveNextPlayer();
                        } else {

                            board.updateInterface(move);
                            players[m.getOrig()].incPoints();
                            board.incPointPlayer(m.getOrig(),players[m.getOrig()].getPoints());
                        }
                    }
                    //System.out.println("The next player is " + game.getCurrentPlayer());
                    System.out.println("The next player is " + currentPlayer);
                    tryToMyturn();
                } else {
                    System.out.println("Timeout");
                    //int playeId = game.getCurrentPlayer();
                    int playeId = currentPlayer;
                    rightId = link.getRightId();
                    while(!link.checkAYANode(rightId,playeId)) {
                        if (rightId == playeId) {

                            System.out.println("Current Player has crashed.Sending crash Msg");
                            link.nodes[rightId].setNodeCrashed();
                            link.setRightId((rightId + 1) % players.length);
                            boolean[] nodesCrashed = new boolean[players.length];
                            Arrays.fill(nodesCrashed, false);
                            boolean anyCrash = false;
                            messageBroadcast.incMessageCounter();
                            int messageCounter = messageBroadcast.retrieveMsgCounter();
                            boolean sendOk = false;
                            int howManyCrash = 0;

                            checkLastNode();

                            while(link.checkAliveNode() == false) {

                                anyCrash = true;
                                howManyCrash = howManyCrash + 1;
                                nodesCrashed[link.getRightId()] = true;
                                System.out.println("Finding a new neighbour");
                                link.incRightId();
                                checkLastNode();
                               
                            }
                            while (sendOk == false) {

                                //non fà il controllo sul send ma prima
                                System.out.println("Im sending a crash message with id " + messageCounter );
                                board.updateCrash(rightId);
                                /*
                                board.clearOldPlayer(game.getCurrentPlayer());
                                game.setCurrentPlayer(link.getRightId());
                                board.setCurrentPlayer(game.getCurrentPlayer());
                                */
                                board.clearOldPlayer(currentPlayer);
                                currentPlayer = link.getRightId();
                                board.setCurrentPlayer(currentPlayer);

                                messageBroadcast.send(mmaker.newCrashMessage(rightId,messageCounter,howManyCrash));
                                sendOk = true; 
                            }
                            System.out.println("Next Player is " + players[currentPlayer].getUsername() + " id " + currentPlayer);

                            //Spedisce CrashMessage se sono stati rilevati crash

                            if (anyCrash) {

                                howManyCrash = howManyCrash + 1;
                                for(int i=0;i<nodesCrashed.length;i++) {
                                    if (nodesCrashed[i] == true) {

                                        messageBroadcast.incMessageCounter();
                                        int messageCounterCrash = messageBroadcast.retrieveMsgCounter();
                                        System.out.println("Sending a CrashMessage id " + messageCounterCrash);
                                        //Invio msg di crash senza gestione dell'errore
                                        board.updateCrash(i);
                                        //board.clearOldPlayer(game.getCurrentPlayer());
                                        //game.setCurrentPlayer((game.getCurrentPlayer()+1) % pla);
                                        //board.setCurrentPlayer( game.getCurrentPlayer() );
                                        messageBroadcast.send(mmaker.newCrashMessage(i,messageCounterCrash,howManyCrash));
                                    }
                                }
                            }
                            break;
                        }
                     	rightId = (rightId + 1) % players.length;
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void tryToMyturn() {

        while (currentPlayer == nodeId) {

            //Quando è il mio turno sblocco la board e rimango in attesa della mossa
            //Direi che questo wait() sia quasi obbligatorio se lo vogliamo strutturare così.
            //L oggetto Client si blocca un attimo ma la classe remota RMI MessageBroadcast può ancora
            // ricevere messaggi, appena il client si riattiva può ritornare in ascolto sul buffer per vedere
            // se ci sono messaggi.Se ce ne sono va ad aggiornare l interfaccia locale.

            board.setCurrentPlayer(currentPlayer);
            System.out.println("Unlock board.");
            board.unlockBoard();
            System.out.println("I'm trying to do a move");
            int nextPlayer = 0;

            try{
                System.out.println("Wait move");
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            //spedisco il messaggio sulla classe remota del mio vicino destro tramite RMI.
            
            boolean[] nodesCrashed = new boolean[players.length];
            Arrays.fill(nodesCrashed, false);
            boolean anyCrash = false;
            messageBroadcast.incMessageCounter();
            int messageCounter = messageBroadcast.retrieveMsgCounter();
            boolean sendOk = false;
            int howManyCrash = 0;

            while(link.checkAliveNode() == false) {

                anyCrash = true;
                howManyCrash = howManyCrash + 1;
                nodesCrashed[link.getRightId()] = true;
                System.out.println("Finding a new neighbour");
                link.incRightId();
                if (link.getRightId() == link.getNodeId()) {
                    System.out.println("Unico giocatore, partita conclusa");
                    System.exit(0);
                    //si deve sostituire con una chiamada gameEnd alla board.
                }
            }

            while (sendOk == false) {

                //non fà il controllo sul send ma prima
                System.out.println("Im sending a message with id " + messageCounter );
                messageBroadcast.send(mmaker.newGameMessage(move,messageCounter,howManyCrash));
                sendOk = true; 
            }


            //mi calcola il prox giocatore anche senza crash
            nextPlayer = board.updateAnyCrash(link.getNodes(),link.getNodeId());


            if (move.getPair() == false) {

                //Incremento il prossimo giocatore che deve giocare.
                board.clearOldPlayer(currentPlayer);
                currentPlayer = nextPlayer;
                board.setCurrentPlayer(currentPlayer);

            } else {
                players[nodeId].incPoints();
                board.incPointPlayer(nodeId, players[nodeId].getPoints());
            }
            board.lockBoard();
            
            
            System.out.println("Next Player is " + players[currentPlayer].getUsername() + " id " + currentPlayer);

            //Spedisce CrashMessage se sono stati rilevati crash

            if (anyCrash) {

                howManyCrash = howManyCrash + 1;
                for(int i=0;i<nodesCrashed.length;i++) {
                    if (nodesCrashed[i] == true) {

                        messageBroadcast.incMessageCounter();
                        int messageCounterCrash = messageBroadcast.retrieveMsgCounter();
                        System.out.println("Sending a CrashMessage id " + messageCounterCrash);
                        //Invio msg di crash senza gestione dell'errore
                        messageBroadcast.send(mmaker.newCrashMessage(i,messageCounterCrash,howManyCrash));
                    }
                }
            }

        }

    }

    private long getWaitSeconds() {
        return 10L + nodeId * 2;
    }

    public synchronized Player[] getPlayers() {
    // restituisce la lista di player appena scaduto il timeout
        if (players == null) {
            try{
                System.out.println("Waiting for other players...");
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

    public void retrieveNextPlayer() {

        //va avanti fino a quando trova il primo nodo attivo
        while(!link.nodes[((currentPlayer+1) % players.length)].getActive()) {
                                        board.clearOldPlayer(currentPlayer);
                                        currentPlayer = (currentPlayer+1) % players.length;
                                }
        board.clearOldPlayer(currentPlayer);
        currentPlayer = (currentPlayer+1) % players.length;
        board.setCurrentPlayer( currentPlayer );

    }
    public void retrieveNextPlayerCrash() {

        if(link.nodes[currentPlayer].getActive()) {
        	System.out.println("Player active");
        } else {
        	retrieveNextPlayer();
        }
        

    }
    public int getNodeId() {
        return nodeId;
    }
    private void checkLastNode() {

    	if (link.getRightId() == link.getNodeId()) {

    		board.updateCrash(rightId);
    		board.clearOldPlayer(currentPlayer);
    		board.setCurrentPlayer(nodeId);
    		System.out.println("Unico giocatore, partita conclusa");
    		board.alertLastPlayer();
        }
    }
}
