/* 	La classe Deck rappresenta il "tavolo" del gioco
	L'array cards contiene gli indici delle carte mischiati ed è univoco ad ogni Client
*/

package src;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.io.Serializable;

public class Deck implements Serializable{

	//private List<Card> cards;
	private int nCards;				// number of total cards on the deck
	private int remainedCards;
	private List<Integer> cardVals;


	public Deck(int nCards){
		//this.cards = cards;
		this.nCards = nCards;
		this.remainedCards = nCards;
	}
	
	public void generateCards(){
	// genera e mescola il mazzo di carte
		int i;
		cardVals = new ArrayList<Integer>();
		
		for(i=0; i < (nCards/2) ; i++){
			cardVals.add(i);
			cardVals.add(i);
		}
		
		 Collections.shuffle(cardVals);
	}

	public List<Integer> getCardVals(){
		return cardVals;
	}
	
	/*public Card getCard(int id){ return cards.get(id); }*/
	
	public int getnCards(){ return nCards; }
}
