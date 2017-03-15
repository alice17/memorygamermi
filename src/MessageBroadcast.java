

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

	public MessageBroadcast(BlockingQueue<GameMessage> buffer) throws RemoteException {
		this.buffer = buffer;
		this.messageCounter = 0;
		pendingMessage = new TreeMap<Integer,GameMessage>();
		msgCounterLock = new ReentrantLock();

	}

	public void configure(Link link, RouterFactory rmaker, MessageFactory mmaker) {
		
			this.link = link;
			this.rmaker = rmaker;
			this.mmaker = mmaker;
	}

	public void send(GameMessage msg) {
		Router r = rmaker.newRouter(msg);
		new Thread(r).start();
	}

	public synchronized void forward(GameMessage msg) throws RemoteException {
		
		if (enqueue(msg)) {
			Router router = rmaker.newRouter(msg);
			router.run();
		} else {
			System.out.println("Message discarded. " + msg.toString());
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