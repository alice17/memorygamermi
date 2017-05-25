package src;

/*
Questa classe è utilizzata per creare (crea solo, non si occupa dell'invio dei msg) vari tipi di Router incaricati di 
funzioni differenti.
*/

public class RouterFactory {

	private Link link;



	public RouterFactory(Link link) {
		this.link = link;

	}

	// crea un newRouter che può essere utilizzato per gestire msg di gioco oppure di crash (GameMessage)
	public Router newRouter(GameMessage gameMsg) {
		return new Router(link, gameMsg, this);
	}

	// crea un AYARouter per gestire il controllo AYA sui vicini
	public AYARouter newAYARouter() {
		return new AYARouter(link,this);
	}
}
