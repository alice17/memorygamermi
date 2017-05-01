
package src;


import java.rmi.RemoteException;



public class AYARouter extends AbstractRouter {

	private RouterFactory rmaker;

	public AYARouter(Link link,RouterFactory rmaker) {
		super(link);
		this.rmaker = rmaker;
	}

	@Override
	public void run() {
		super.run();
	}

	@Override
	protected synchronized void performCallHook(ServiceBulk to) {

		try {
			to.messageBroadcast.checkNode();
		} catch (RemoteException re) {
			re.printStackTrace();
			System.out.println("Remote Exception");
		}
	}
}