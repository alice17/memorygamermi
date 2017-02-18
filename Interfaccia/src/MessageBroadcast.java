

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

	public MessageBroadcast(BlockingQueue<GameMessage> buffer) throws RemoteException {
		this.buffer = buffer;

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

	public void forward(GameMessage msg) throws RemoteException {
		
		if (enqueue(msg)) {
			Router router = rmaker.newRouter(msg);
			router.run();
		} else {
			System.out.println("message discarded");
		}	
	}

	private boolean enqueue(GameMessage msg) {
		boolean doForward = false;
		if (msg.getOrig() != link.getNodeId()) {
			try {
				System.out.println("msg punt into the queue");
				buffer.put(msg);
			} catch (InterruptedException e) {}
			doForward = true;
		}
		return doForward;
	}

	
	

}