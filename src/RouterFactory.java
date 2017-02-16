


package src;


public class RouterFactory {

	private Link link;



	public RouterFactory(Link link) {
		this.link = link;

	}

	public Router newRouter(GameMessage gameMsg) {
		return new Router(link, gameMsg, this);
	}
}