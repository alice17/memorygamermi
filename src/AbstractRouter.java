


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

		ServiceBulk right = null;
		boolean success = false;


		right = link.getRight(); //si recupera il riferimento del vicino destro
		System.out.println("I got right reference");
		performCallHook(right);
		success = true;
		
	}

	protected abstract void performCallHook(ServiceBulk to); 
		 
}