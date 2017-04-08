

package src;


import java.rmi.RemoteException;

/*
classe che estende abstarctrouter, incaricata dell'invio dei messaggi di gioco.
Si dovrebbero creare altre due classi simili, una per l'invio dei mess di errore e una
per l'invio degli ACK ai vicini per le info sui crash.
*/

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

	/* Metodo che utilizza una chiamata rmi, come parametro di ingresso
	è presente un riferimento al vicino destro di tipo ServiceBulk */
	@Override
	protected void performCallHook(ServiceBulk to) {
		GameMessage cloneMsg = (GameMessage)gameMsg.clone();
		cloneMsg.setFrom(link.getNodeId());

		try {
			System.out.println("Forward");
			to.messageBroadcast.forward(cloneMsg); //chiamata rmi
		} catch (RemoteException rE) {
			rE.printStackTrace();
			System.out.println("RemoteException");
		}catch (NullPointerException np){
			// destinatario irraggiungibile
			
			System.out.println("Can't forward the message.");
		}
	}
}