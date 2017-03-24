


package src;


import java.io.Serializable;

/*
Classe che crea un oggetto generale di tipo messagge, la classe GameMessage eredita da questa classe.
Le classi che si andranno a creare, ErrorMessage e ACKMessage devono ereditare anch'esse da questa.
*/

public class Message implements Serializable, Cloneable {
		private int origId;
		private int fromId;

		public Message(int origId) {
			this.origId = origId;
			this.fromId = origId;
		}

		public int getOrig() {
			return origId;
		}

		public void setFrom(int fromId) {
			this.fromId = fromId;
		}

		public int getFrom() {
			return fromId;
		}
		public Object clone() {
			Message m = new Message(origId);
			m.setFrom(fromId);
			return m;
		}
		public String toString() {
			return "received from " + fromId + ", created by " + origId;
		}
}