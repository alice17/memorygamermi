package src;

/*
CLasse che contiene un oggetto MessageBroadcast per chiamare metodi remoti e l'id del nodo.

*/

public class ServiceBulk {

		public RemoteBroadcast messageBroadcast;
		public int id;


		public ServiceBulk(RemoteBroadcast messageBroadcast, int id) {
			this.messageBroadcast = messageBroadcast;
			this.id = id;
		}
}