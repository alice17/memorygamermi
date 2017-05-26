package src;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/*
Classe utilizzata per le chiamate remote RMi, implementa la classe remota RemoteBroadcast,
per questo possono essere chiamati dei metodi in remoto di questa classe.
Gestisce l'arrivo dei messaggi, li riordina, li può scartare o inserire nel buffer.
*/

public class MessageBroadcast extends UnicastRemoteObject implements RemoteBroadcast {

	private Link link = null;
	private RouterFactory rmaker;
	private MessageFactory mmaker;
	private BlockingQueue<GameMessage> buffer;
	private int messageCounter;
	private TreeMap<Integer, GameMessage> pendingMessage;
	private ReentrantLock msgCounterLock;
	public Client clientBoard;

	public MessageBroadcast(BlockingQueue<GameMessage> buffer,final Client clientBoard) throws RemoteException {
		this.buffer = buffer;
		this.messageCounter = 0;
		this.clientBoard = clientBoard;
		pendingMessage = new TreeMap<Integer,GameMessage>();
		msgCounterLock = new ReentrantLock();

	}

	public void configure(Link link, RouterFactory rmaker, MessageFactory mmaker) {
		
			this.link = link;
			this.rmaker = rmaker;
			this.mmaker = mmaker;
	}

	//Invio di un messaggio sulla rete
	public synchronized void send(GameMessage msg) {
		
			//quando r.run termina ho il link.Node[] aggiornato
			Router r = rmaker.newRouter(msg);
			new Thread(r).start();
		
	}
	// Avvio controllo AYA
	public synchronized void sendAYA() {

		AYARouter r = rmaker.newAYARouter();
		new Thread(r).start();
	}

	// Inoltro del messaggio al vicino destro se questo è necessario
	public synchronized void forward(GameMessage msg) throws  RemoteException {
		
		if (enqueue(msg)) {
			
			boolean anyCrash = false;
			boolean[] nodesCrashed = new boolean[link.getNodes().length];
            Arrays.fill(nodesCrashed, false);
			int initialMsgCrash = msg.getHowManyCrash();


			while(link.checkAliveNode() == false) {

                msg.incCrash();
                anyCrash = true;
                nodesCrashed[link.getRightId()] = true;
                System.out.println("Finding a new neighbour");
                link.incRightId();
                if (link.getRightId() == link.getNodeId()) {
                    System.out.println("Unico giocatore, partita conclusa");
                    System.exit(0);
                    
                }
            }

            
  
            //spedisco il messaggio arrivato dal nodo precedente
            send(msg);

            if (anyCrash) {

           		// 1 per il gamemessage del nodo
            	int nextIdMsg = initialMsgCrash + messageCounter + 1 ;
            	

                for(int i=0;i<nodesCrashed.length;i++) {

                    if (nodesCrashed[i] == true) {

                        
                        System.out.println("Sending a CrashMessage id "+ nextIdMsg +" for node " + i);
            
                        //Invio msg di crash senza gestione dell'errore
                        GameMessage msgProv = mmaker.newCrashMessage(i,nextIdMsg,0);

                        if (initialMsgCrash == 0) {
							incMessageCounter();

						} else {
							pendingMessage.put(nextIdMsg,(GameMessage)msgProv.clone());
						}

                     
            
                        send(msgProv);
                        nextIdMsg = nextIdMsg + 1;
                        System.out.println("Update Board crash");
                        clientBoard.board.updateCrash(i);
                    }
                }
            }
		} else {
			System.out.println("Message discarded. " + msg.toString());
			}	
	}

	// Metodo che inserisce i messaggi nella coda se devono essere processati
	private synchronized boolean enqueue(GameMessage msg) {

		boolean doForward = false;
		System.out.println("initialMsgCrash -> " + msg.getHowManyCrash());
        System.out.println("messagecounter-> " + messageCounter);
        System.out.println("MsgId -> " + msg.getId());
		
		if (msg.getOrig() != link.getNodeId()) {
				if((msg.getId() > messageCounter) && (pendingMessage.containsKey(msg.getId()) == false)) {
					if(msg.getId() == messageCounter + 1) {
						try {
							buffer.put(msg);
							System.out.println("msg put into the queue");
						} catch (InterruptedException e) {
							System.out.println("Error! Can't put message in the queue.");
						}

						incMessageCounter();

						while(pendingMessage.containsKey(messageCounter + 1)) {
							GameMessage pendMessage = pendingMessage.remove(messageCounter + 1);
							try {
								buffer.put(pendMessage);
							} catch (InterruptedException e) {
								System.out.println("error!");
							}

							incMessageCounter();
						}
					} else {
						pendingMessage.put(msg.getId(),(GameMessage)msg.clone());
					}
					doForward = true;
				} 
		}
		return doForward;
	}

	/*
	Metodo che incrementa il msgcounter,viene utilizzato un lock per accedere alla variabile
	in mutua esclusione
	*/

	public void incMessageCounter() {
		msgCounterLock.lock();
		try {
			messageCounter++;
		} finally {
			msgCounterLock.unlock();
		}
	}


	public int retrieveMsgCounter() {
		return messageCounter;
	}

	//Metodo utilizzato dal controllo AYA sul vicino
	public synchronized void checkNode() {

		System.out.println("My neighbor is alive");
	}
}