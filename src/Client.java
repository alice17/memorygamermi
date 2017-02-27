
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
import java.util.ArrayList;
import java.util.List;




public class Client {

    public   final int PORT = 1099;
    private  Game game;
    private  Player[] players;
    private  int nodeId;
    private  Link link;
    private  int playersNo;
    private  MessageBroadcast messageBroadcast;
    private  MessageFactory mmaker;
    private  RouterFactory rmaker;
    private  BlockingQueue<GameMessage> buffer;
    private  int[] processedMsg;
    private  String playerName;
    private  Deck deck;
    private  boolean result;
    private  Partecipant partecipant;
    private  List<String> namePlayers;
    private  Player me;

    /*
    * Per una corretta interazione per i feedback in WindowRegistration,
    * ho spacchettato il client in più metodi, in modo tale che gli avvisi
    * più importanti venissero gestiti dalla classe WindowRegistration*/

    public Client(String username){
         this.playerName = username;

    }
    // tale metodo setta le connessioni e in nome del client
    public boolean setClientGame() {

        InetAddress localHost = null;

        try{
            localHost = InetAddress.getLocalHost();
            System.out.println("Local host is " + localHost);
        } catch (UnknownHostException uh){
            System.exit(1);
        }


        String server = "localhost";


        Random random = new Random();
        int port = random.nextInt(100)+2001;

        /*if (System.getSecurityManager() == null)
            System.getSecurityManager(new RMISecurityManager());
        else
            System.out.println("Security Manager not starts.");*/

         me = new Player(playerName, localHost, port);

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


        partecipant = null;
        result = false;

        /* establish connection with server */
        try{
            partecipant = new Partecipant();
            System.out.println("Looking up subscribe service...");
            String url = "rmi://" + server +":" + PORT + "/Subscribe";
            SubscribeInterface subscribe = (SubscribeInterface) Naming.lookup(url);
            System.out.println("Subscribe service found at address " + url);

            result = subscribe.subscribeAccepted(partecipant, me);
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
        
        
        return result;
      }

      public void configureDeckPlayers(){
            players = partecipant.getPlayers();
            playersNo = players.length;
            deck = partecipant.getDeck();
      }
      
        // tale metodo inizializza il gioco
      public void inizializeGame(){
            //if( playersNo > 1 ){
              //  dialog.setVisible(false); // chiudo l'info di attesa
                //System.out.println("Players subscribed:");

              /*  for (int i=0; i < playersNo;i++){
                    namePlayers.add(players[i].getUsername());
                }*/

                //System.out.println("Deck obtained. Number of cards: " + deck.getnCards());

				/* stampa valori del mazzo di carte
				for(int i=0; i < deck.getnCards(); i++){
					System.out.println(deck.getCard(i).getCardId()); } */

                link = new Link(me, players);
                nodeId = link.getNodeId();
                processedMsg = new int[players.length];
                Arrays.fill(processedMsg, 0);
                rmaker = new RouterFactory(link);
                mmaker = new MessageFactory(nodeId);
                messageBroadcast.configure(link,rmaker,mmaker);

                System.out.println("My id is " + nodeId + " and my name is " + players[nodeId].getUsername());
                System.out.println("My left neighbour is " + players[link.getLeftId()].getUsername());
                System.out.println("My right neighbour is " + players[link.getRightId()].getUsername());

                game = new Game(playersNo);

                // start the game
                gameStart(deck);

        }



    private void gameStart(Deck deck) {
        Board board = new Board(deck);

        tryToMyturn();

        while(!game.isGameEnded()) {
            try {
                //board.lockBoard();
                System.out.println("Waiting up to " + getWaitSeconds() + " seconds for a message..");
                GameMessage m = buffer.poll(getWaitSeconds(), TimeUnit.SECONDS);
                tryToMyturn();

                if(m != null) {
                    System.out.println("Processing message " + m);
                    game.setCurrentPlayer((game.getCurrentPlayer()+1) % players.length);
                    tryToMyturn();
                } else {
                    System.out.println("Timeout");
                }
            } catch (InterruptedException e) {}
           game.setGameEnded(true);
        }

    }

    private void tryToMyturn() {

        while (game.getCurrentPlayer() == nodeId) {

            System.out.println("I'm trying to send a test message to my right neighbour");
            String test = "Origin nodeId message is " + nodeId;
            game.setCurrentPlayer((game.getCurrentPlayer()+1) % players.length);
            messageBroadcast.send(mmaker.newGameMessage(test));
            System.out.println("Next Player is " + players[game.getCurrentPlayer()].getUsername() + " id " + game.getCurrentPlayer());

            // sceglie la prima carta
            // manda il broadcast per la mossa
            // sceglie la seconda carta
            // determina se vince
            // broadcast e turno successivo
        }
    }

    private long getWaitSeconds() {
        return 10L + nodeId * 2;
    }


    public List<String> PlayerForUI(){
      return namePlayers;
    }
    
    public int getPlayersNo(){
    	return playersNo;
    }
}
