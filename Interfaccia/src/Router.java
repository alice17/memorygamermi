

package src;


import java.rmi.RemoteException;


public class Router extends AbstractRouter {

	private GameMessage gameMsg;
	private RouterFactory rmaker;


	public Router(Link link, GameMessage gameMsg, RouterFactory rmaker) {
		super(link,gameMsg);
		this.gameMsg = gameMsg;
		this.rmaker = rmaker;

	}

	@Override
	public void run() {
		super.run();
	}

	@Override
	protected void performCallHook(ServiceBulk to) {
		GameMessage cloneMsg = (GameMessage)gameMsg.clone();
		cloneMsg.setFrom(link.getNodeId());

		try {
			to.messageBroadcast.forward(cloneMsg);
		} catch (RemoteException rE) {
			rE.printStackTrace();
			System.out.println("RemoteException");
		}
	}
}