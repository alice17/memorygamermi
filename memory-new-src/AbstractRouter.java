


package src;



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

		right = link.getRight();
		System.out.println("I got right reference");
		performCallHook(right);
		success = true;
		
	}

	protected abstract void performCallHook(ServiceBulk to); 
		 
}