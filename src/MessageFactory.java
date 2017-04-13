


package src;

/* 
Guardare routerfactory, funzionano uguali ma questa si occupa dei messaggi.
Anche in questa classe andrebbero aggiunti dei nuovi metodi per creare i messaggi di errore e di ACK.

*/

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
		processedMessage[myId] = ++messageCounter;
		for (int i=0;i< processedMessage.length;i++) {
			System.out.println("Node " + i + " " + processedMessage[i]);
		}
		return new GameMessage(myId, messageCounter, processedMessage,move);
	}
	public int getMessageCounter() {
		return messageCounter;
	}

	public void incMessageCounter() {
		messageCounter++;
	}
}