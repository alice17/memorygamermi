
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

public class Client {

    public static final int PORT = 1099;
    private static Game game;
    private static Player[] players;
    private static int nodeId;
    private static Link link;
    private static int playersNo;
    private static MessageBroadcast messageBroadcast;
    private static MessageFactory mmaker;
    private static RouterFactory rmaker;
    private static BlockingQueue<GameMessage> buffer;
    private static int[] processedMsg;
    private static Deck deck;

    public static void main(String[] args) {

        InetAddress localHost = null;
        
        try{
            localHost = InetAddress.getLocalHost();
            System.out.println("Local host is " + localHost);
        } catch (UnknownHostException uh){
            System.exit(1);
        }
        
        String playerName = args[0];
        String server = "localhost";
        int port = PORT;
        
        if (args.length > 1) 
        	port = Integer.parseInt(args[1]);

        /*if (System.getSecurityManager() == null)
            System.getSecurityManager(new RMISecurityManager());
        else
            System.out.println("Security Manager not starts.");*/

        Player me = new Player(playerName, localHost, port);
        
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
        
        if (result) {
        // subscribe accepted
			System.out.println("You have been added to player list.");
			players = partecipant.getPlayers();
			playersNo = players.length;
			deck = partecipant.getDeck();

			if( playersNo > 1 ){
				System.out.println("Players subscribed:");
				
				for (int i=0; i < playersNo;i++){
					System.out.println(players[i].getUsername());
				}
				
				System.out.println("Deck obtained. Number of cards: " + deck.getnCards());
				
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
				gameStart();
			}else{
				System.out.println("Not enough players to start the game. :(");
				System.exit(0);
			}
        }   
    }
 
	private static void gameStart() {
        
        tryToMyturn();

        while(!game.isGameEnded()) { 
            try {
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
    
    private static void tryToMyturn() {

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

    private static long getWaitSeconds() {
        return 10L + nodeId * 2;
    }
    
}
