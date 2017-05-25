package src;

/*
Classe astratta che recupera il riferimento del vicino destro e definisce il metodo performCallHook
che viene utilizzato all'interno della classe Router.
*/

public abstract class AbstractRouter implements Runnable {

	protected Link link;
	protected GameMessage gameMsg;
	protected Message crashMsg;


	// Metodo utilizzato per creare un AbstractRouter che gestisce un GameMessage
	public AbstractRouter(Link link, GameMessage gameMsg) {

		this.link = link;
		this.gameMsg = gameMsg;
		this.crashMsg = null;
		
	}

	// Metodo utilizzato per creare un AbstractRouter che gestisce un CrashMessage
	public AbstractRouter(Link link, Message crashMsg) {

		this.link = link;
		this.crashMsg = crashMsg;
		this.gameMsg = null;
	}

	
	//Metodo utilizzato per creare un Abstract Router che gestisce un AYA request
	public AbstractRouter(Link link) {

		this.link = link;
	}

	//Metodo Runnable
	public void run() {


			try{
				ServiceBulk right = null;
				// Se non viene trovato il riferimento si imposta active = false nel node
				right = link.getRight(); //si recupera il riferimento del vicino destro
				performCallHook(right);	// funzione di router
				System.out.println("I got right reference");

			}catch (NullPointerException np) {
			
				// destinatario non raggiungibile
				System.out.println("Can't forward the message to neighbour.");
				
			}
	}

	protected abstract void performCallHook(ServiceBulk to); 
	
}