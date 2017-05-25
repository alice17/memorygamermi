package src;

import java.io.Serializable;

/* Classe OneMove che rappresenta la mossa effettuata, le due carte scelte e se queste
formano una coppia */

public class OnesMove implements Cloneable, Serializable {

	public int card1Index;
	public int card2Index;
	public boolean pair;

	public OnesMove() {}

	public OnesMove(int card1Index,int card2Index,boolean pair) {
		this.card1Index = card1Index;
		this.card2Index = card2Index;
		this.pair = pair;
	}

	public boolean getPair() {
		return pair;
	}
	public int getCard1Index() {
		return this.card1Index;
	}
	public int getCard2Index() {
		return this.card2Index;
	}
}
