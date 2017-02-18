/* 	Classe Card:
	rappresenta una carta sul tavolo (Deck)

*/


package src;

import java.io.Serializable;


public class Card implements Serializable{

	//private int value;
	private int id;
	private boolean covered;

	public Card(int id) {
		this.id = id;
		this.covered = true;
	}

	public int getCardId(){ return this.id; }

}

