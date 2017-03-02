


package src;



public class MessageFactory {

	private int myId;
	private int messageCounter;
	private int[] processedMessage;


	public MessageFactory(int myId,int[] processedMessage) {
		this.myId = myId;
		this.messageCounter = 0;
		this.processedMessage = processedMessage;
	}

	public Message newMessage() {
		return new Message(myId);
	}

	public GameMessage newGameMessage(OnesMove move) {
		processedMessage[myId] = messageCounter++;
		return new GameMessage(myId, messageCounter, processedMessage,move);
	}
	public int getMessageCounter() {
		return messageCounter;
	}

	public void incMessageCounter() {
		messageCounter++;
	}
}