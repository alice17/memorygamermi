package src;

import java.rmi.RemoteException;

/*
Classe AYARouter utilizzata per creare il router che gestisce la 
*/


public class AYARouter extends AbstractRouter {

	//private RouterFactory rmaker;

	// Metodo per la creazione di un istanza AYARouter
	public AYARouter(Link link,RouterFactory rmaker) {

		super(link);
		//this.rmaker = rmaker;
	}

	@Override
	public void run() {
		super.run();
	}

	//Metodo che esegue una chiamata remota RMI sul nodo vicino
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
