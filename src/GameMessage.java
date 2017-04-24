package src;

/*
classe che gestisce i messaggi di gioco, si dovrebbero creare altre due classi simili
per la gestione dei messaggi di errore (ErrorMessage) e per la gestione degli ACK (ACKMessage)
*/

public class GameMessage extends Message implements Cloneable {

	private int id;
	private OnesMove move;
	private int nodeCrashedId;

	
	// Metodo che inizializza un GameMessage classico

	public GameMessage(int origId, int id,OnesMove move) {
		super(origId,id);
		this.id = id;
		this.move = move;
		this.nodeCrashedId = -1;

	}


	//Metodo che inizializza un GameMessage utilizzato per informare
	//la rete del crash di un dato nodo.

	public GameMessage(int origId, int id,int nodeCrashedId) {
		super(origId,id);
		this.id = id;
		this.nodeCrashedId = nodeCrashedId;
		this.move = null;
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
			m = new GameMessage(getOrig(),id,move);
		} else {
			m = new GameMessage(getOrig(),id,nodeCrashedId);
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





}
