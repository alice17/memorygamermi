


package src;

/* 

Guardare routerfactory, funzionano uguali ma questa si occupa dei messaggi.

*/

public class MessageFactory {

	private int myId;



	
	public MessageFactory(int myId) {
		this.myId = myId;
		
	}

	public GameMessage newGameMessage(OnesMove move,int messageCounter,int howManyCrash) {
		
		
		return new GameMessage(myId,messageCounter,move,howManyCrash);
	}
	public GameMessage newCrashMessage(int nodeCrashedId,int messageCounter,int howManyCrash) {
		
		return new GameMessage(myId,messageCounter,nodeCrashedId,howManyCrash);
	}
}