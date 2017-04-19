


package src;

/*
Classe astratta che recupera il riferimento del vicino destro e definisce il metodo performCallHook
che viene utilizzato all'interno della classe Router.
*/

public abstract class AbstractRouter implements Runnable {

	protected Link link;
	protected GameMessage msg;

	public AbstractRouter(Link link, GameMessage msg) {
		this.link = link;
		this.msg = msg;
		
	}

	public void run()  {

		boolean success = false;

		while (success == false) {

			
			try{
				ServiceBulk right = null;
				// Se non viene trovato il riferimento si imposta active false nel node
				right = link.getRight(); //si recupera il riferimento del vicino destro
				performCallHook(right);	// fuzione di router
				System.out.println("I got right reference");
				success = true;
			}catch (NullPointerException np) {
			
				// destinatario non raggiungibile

				System.out.println("Can't forward the message to neighbour.");
				System.out.println("Finding a new neighbour");
				msg.setProcessedMsgElement(link.getRightId(),-1);
				link.incRightId();
				if (link.getRightId() == link.getNodeId()) {
					System.out.println("Unico giocatore, partita conclusa");
					System.exit(0);
				} 
			}
		}
	}

	public void runForward()  {

		boolean success = false;

		while (success == false) {

			
			try{
				ServiceBulk right = null;
				right = link.getRight(); //si recupera il riferimento del vicino destro
				performCallHook(right);	// fuzione di router
				System.out.println("I got right reference");
				success = true;
			}catch (NullPointerException np) {
			
				// destinatario non raggiungibile

				System.out.println("Can't forward the message to neighbour.");
				System.out.println("Finding a new neighbour");
				msg.setProcessedMsgElement(link.getRightId(),-1);
				//client.updateCrashBoard(link.getRightId());
				link.incRightId();
				if (link.getRightId() == link.getNodeId()) {
					System.out.println("Unico giocatore, partita conclusa");
					System.exit(0);
				} 
			}
		}
	}

	protected abstract void performCallHook(ServiceBulk to); 

	protected abstract void updateCrashBoard(int nodeCrashed);
		 
}