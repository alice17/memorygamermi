


package src;

/* 

Guardare routerfactory, funzionano uguali ma questa si occupa dei messaggi.

*/

public class MessageFactory {

	private int myId;



	
	public MessageFactory(int myId) {
		this.myId = myId;
		
	}

	public GameMessage newGameMessage(OnesMove move,int messageCounter) {
		
		
		return new GameMessage(myId,messageCounter,move);
	}
	public GameMessage newCrashMessage(int nodeCrashedId,int messageCounter) {
		
		return new GameMessage(myId,messageCounter,nodeCrashedId);
	}
}