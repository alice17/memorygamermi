

package src;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
classe utilizzata per le chiamate remote RMi, implementa la classe remota RemoteBroadcast,
per questo possono essere chiamati dei metodi in remoto di questa classe.
Gestisce l'arrivo dei messaggi, li riordina, li può scartare o inserire nel buffer.

*/

public class MessageBroadcast extends UnicastRemoteObject implements RemoteBroadcast {

	private Link link = null;
	private RouterFactory rmaker;
	private MessageFactory mmaker;

	private Lock viewsLock;
	private Lock configLock;
	private Lock msgLock;

	private BlockingQueue<GameMessage> buffer;

	private int messageCounter;
	private int[] processedMessage;
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


	public void send(GameMessage msg) {
		// serve un modo per sapere se il messaggio viene inviato o no
		
		//quando r.run termina ho sia il processedMsg che i Nodes aggiornati
		Router r = rmaker.newRouter(msg);
		r.run();
		Node[] provNodes = link.getNodes();
            for (int i=0;i<provNodes.length;i++) {
                System.out.println("Node " + i + " " + provNodes[i].getActive());
            }
	}

	public synchronized void forward(GameMessage msg) throws RemoteException {
		
		if (enqueue(msg)) {
			
			Router routerForward = rmaker.newRouter(msg);
			routerForward.runForward();
			int nextActivePlayer;
			nextActivePlayer = clientBoard.board.updateAnyCrash(link.getNodes(),link.getNodeId());
		} else {
			System.out.println("Message discarded. " + msg.toString());
			System.out.println("Checking new nodes crashed");
			int[] processedMessageUpdate = msg.getProcessedMessage();
			for(int i=0;i<processedMessageUpdate.length;i++) {

				if (processedMessageUpdate[i] == -1) {
					if (link.nodes[i].getActive()) {

						link.nodes[i].setNodeCrashed();
                        clientBoard.board.updateCrash(i);
                        clientBoard.processedMsg[i] = -1;
                    }
                }
            }
		}	
	}

	private synchronized boolean enqueue(GameMessage msg) {
		boolean doForward = false;
		
		System.out.println("Msg broadcast counter before -> " + messageCounter);
		if (msg.getOrig() != link.getNodeId()) {
			//try {
				if((msg.getId() > messageCounter) && (pendingMessage.containsKey(msg.getId()) == false)) {
					if(msg.getId() == messageCounter + 1) {
						try {
							buffer.put(msg);
							System.out.println("msg put into the queue");
						} catch (InterruptedException e) {
							System.out.println("Error! Can't put message in the queue.");
						}

						msgCounterLock.lock();

						try {
							messageCounter++;
						} finally {
							msgCounterLock.unlock();
						}

						System.out.println("Message counter -> " + messageCounter);
						while(pendingMessage.containsKey(messageCounter + 1)) {
							GameMessage pendMessage = pendingMessage.remove(messageCounter + 1);
							try {
								buffer.put(pendMessage);
							} catch (InterruptedException e) {
								System.out.println("error!");
							}

							msgCounterLock.lock();
							try {
								messageCounter++;
							} finally {
								msgCounterLock.unlock();
							}
						}
					} else {
						pendingMessage.put(msg.getId(),(GameMessage)msg.clone());
					}
					doForward = true;
				} 
			//} catch (InterruptedException ie) {}
		}
		return doForward;
	}
	public void incMessageCounter() {
		msgCounterLock.lock();
		try {
			messageCounter++;
		} finally {
			msgCounterLock.unlock();
		}
	}
}