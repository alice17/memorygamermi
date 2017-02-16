

package src;



public class GameMessage extends Message implements Cloneable {


	private int id;
	private String test;

	public GameMessage(int origId, int id,String test) {

		super(origId);
		this.id = id;
		this.test = test;
	}

	public int getId() {
		return id;
	}

	public String getTest() {
		return test;
	}
	/*public String getOrig() {
		return super.getOrig(); 
	}*/

	public String toString() {
		return "#" + id + ", created by " + getOrig() + ", received from "
				+ getFrom();
	}

	public Object clone() {
		GameMessage m = new GameMessage(getOrig(),id,test);
		m.setFrom(getFrom());
		return m;
	}




}