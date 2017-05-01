


package src;

/*
Questa classe è utilizzata per creare (crea solo, non si occupa dell'invio dei msg)vari tipi di Router incaricati di spedire messaggi specifici.
Per ora viene creato solo il router incaricato di spedire i messaggi
di gioco.Con la gestione dei crash questa classe deve creare anche il router specifico per l'invio
dei messaggi di errore e il router per l'invio dei messaggi per controllare se il vicino è andato in crash.
Con l'aggiunta dei crash questa classe potrebbe avere i seguenti metodi:

public Router newRouter() presente
public ErrorRouter newErrorRouter() da aggiungere, per spedire messaggi di errore
public ACKRouter newACKRouter() da aggiungere, per sapere se i vicini sono andati in crash

*/

public class RouterFactory {

	private Link link;



	public RouterFactory(Link link) {
		this.link = link;

	}

	public Router newRouter(GameMessage gameMsg) {
		return new Router(link, gameMsg, this);
	}

	public AYARouter newAYARouter() {
		return new AYARouter(link,this);
	}
}