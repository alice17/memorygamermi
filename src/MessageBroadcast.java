

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


	public synchronized void send(GameMessage msg) {
		
			//quando r.run termina ho il link.Node[] aggiornato
			Router r = rmaker.newRouter(msg);
			new Thread(r).start();
		
	}

	public synchronized void forward(GameMessage msg) throws  RemoteException {
		
		if (enqueue(msg)) {
			
			boolean anyCrash = false;
			boolean[] nodesCrashed = new boolean[link.getNodes().length];
            Arrays.fill(nodesCrashed, false);
			
			while(link.checkAliveNode() == false) {

                anyCrash = true;
                nodesCrashed[link.getRightId()] = true;
                System.out.println("Finding a new neighbour");
                link.incRightId();
                if (link.getRightId() == link.getNodeId()) {
                    System.out.println("Unico giocatore, partita conclusa");
                    System.exit(0);
                    //si deve sostituire con una chiamada gameEnd alla board.
                }
            }

            
            //Router routerForward = rmaker.newRouter(msg);
            //routerForward.run();
            send(msg);

            if (anyCrash) {

                for(int i=0;i<nodesCrashed.length;i++) {
                    if (nodesCrashed[i] == true) {

                        System.out.println("Sending a CrashMessage within the network for node " + i);
                        incMessageCounter();
                        //int messageCounterCrash = retrieveMsgCounter();

                        //Invio msg di crash senza gestione dell'errore
                        send(mmaker.newCrashMessage(i,messageCounter));
                        System.out.println("Update Board crash");
                        clientBoard.board.updateCrash(i);
                    }
                }
            }
		} else {
			System.out.println("Message discarded. " + msg.toString());
			}	
	}

	private synchronized boolean enqueue(GameMessage msg) {
		boolean doForward = false;
		
		if (msg.getOrig() != link.getNodeId()) {
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

	/*public boolean sendError(GameMessage msg) {

		CrashRouter rError = rmaker.newCrashRouter(msg);
		return rError.routerRun();
	}*/


	/*public synchronized void forwardError(GameMessage msg) throws RemoteException {

		if (enqueue(msg)) {
			
			CrashRouter routerForwardError = rmaker.newCrashRouter(msg);
			boolean[] nodesCrashed = new boolean[link.getNodes().length];
            Arrays.fill(nodesCrashed, false);
            boolean anyCrash = false;
			while(routerForwardError.routerRun() == false) {
				anyCrash = true;
                nodesCrashed[link.getRightId()] = true;
                System.out.println("Finding a new neighbour");
                link.incRightId();
                if (link.getRightId() == link.getNodeId()) {
                    System.out.println("Unico giocatore, partita conclusa");
                    System.exit(0);
                    
                }

			}
			//int nextActivePlayer;
			//nextActivePlayer = clientBoard.board.updateAnyCrash(link.getNodes(),link.getNodeId());
			} else {
			System.out.println("Message discarded. " + msg.toString());
		}
	}*/

	public int retrieveMsgCounter() {
		return messageCounter;
	}

	public synchronized void sendAYA() {


			System.out.println("I'm alive");
		/*} catch(RemoteException re) {
			re.printStackTrace();
		}*/
	}
}