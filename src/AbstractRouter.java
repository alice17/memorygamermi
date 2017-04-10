


package src;

/*
Classe astratta che recupera il riferimento del vicino destro e definisce il metodo performCallHook
che viene utilizzato all'interno della classe Router.
*/

public abstract class AbstractRouter implements Runnable {

	protected Link link;
	protected Message msg;

	public AbstractRouter(Link link, Message msg) {
		this.link = link;
		this.msg = msg;
		
	}

	public void run()  {
		//viene lanciato con un thread a parte da messageBroadcast
		// da catchare l'errore

		ServiceBulk right = null;
		boolean success = true;

		right = link.getRight(); //si recupera il riferimento del vicino destro
		System.out.println("I got right reference");

		try{
			performCallHook(right);	// fuzione di router
		}catch (NullPointerException np) {
		// destinatario non raggiungibile

			System.out.println("Can't forward the message.");
			success = false;
		}
	}

	protected abstract void performCallHook(ServiceBulk to); 
		 
}