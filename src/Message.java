


package src;


import java.io.Serializable;



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