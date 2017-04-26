package src;

/*
classe che gestisce i messaggi di gioco, si dovrebbero creare altre due classi simili
per la gestione dei messaggi di errore (ErrorMessage) e per la gestione degli ACK (ACKMessage)
*/

public class GameMessage extends Message implements Cloneable {

	private int id;
	private OnesMove move;
	private int nodeCrashedId;
	private int howManyCrash;

	
	// Metodo che inizializza un GameMessage classico

	public GameMessage(int origId, int id,OnesMove move,int howManyCrash) {
		super(origId,id);
		this.id = id;
		this.move = move;
		this.nodeCrashedId = -1;
		this.howManyCrash = howManyCrash;

	}


	//Metodo che inizializza un GameMessage utilizzato per informare
	//la rete del crash di un dato nodo.

	public GameMessage(int origId, int id,int nodeCrashedId,int howManyCrash) {
		super(origId,id);
		this.id = id;
		this.nodeCrashedId = nodeCrashedId;
		this.move = null;
		this.howManyCrash = howManyCrash;
	} 

	public int getId() {
		return super.getId();
	}

	

	public String toString() {
		return "#" + id + ", created by " + getOrig() + ", received from "
				+ getFrom();
	}

	public Object clone() {

		GameMessage m;
		if (nodeCrashedId == -1) {
			m = new GameMessage(getOrig(),id,move,howManyCrash);
		} else {
			m = new GameMessage(getOrig(),id,nodeCrashedId,howManyCrash);
		}
		m.setFrom(getFrom());
		return m;
	}

	public boolean getPair() {
		return this.move.getPair();
	}

	public OnesMove getMove() {
		return this.move;
	}

	public int getNodeCrashed() {
		return nodeCrashedId;
	}

	public void incCrash() {
		howManyCrash = howManyCrash + 1;
	}
	public int getHowManyCrash() {
		return howManyCrash;
	}





}
