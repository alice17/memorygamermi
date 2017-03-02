

package src;



public class GameMessage extends Message implements Cloneable {

	private int id;
	private OnesMove move;
	private int[] processedMessage;

	public GameMessage(int origId, int id,int[] processedMessage,OnesMove move) {
		super(origId);
		this.id = id;
		this.processedMessage = processedMessage;
		this.move = move;
	}

	public int getId() {
		return id;
	}

	
	/*public String getOrig() {
		return super.getOrig(); 
	}*/

	public String toString() {
		return "#" + id + ", created by " + getOrig() + ", received from "
				+ getFrom();
	}

	public Object clone() {
		GameMessage m = new GameMessage(getOrig(),id,processedMessage.clone(),move);
		m.setFrom(getFrom());
		return m;
	}

	public boolean getPair() {
		return this.move.getPair();
	}

	public OnesMove getMove() {
		return this.move;
	}




}
