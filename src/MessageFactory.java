


package src;



public class MessageFactory {

	private int myId;


	public MessageFactory(int myId) {
		this.myId = myId;
	}

	public Message newMessage() {
		return new Message(myId);
	}

	public GameMessage newGameMessage(String test) {
		return new GameMessage(myId, myId, test);
	}
}