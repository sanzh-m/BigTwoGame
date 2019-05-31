/**
 * This class is used to create instances of BigTwoCard
 * 
 * @author AliSanzhar
 *
 */
public class BigTwoCard extends Card {
	private static final long serialVersionUID = -5822018182891811204L;
	/**
	 * Creates and returns an instance of BigTwoCard
	 * 
	 * @param suit
	 * 			suit of the card
	 * @param rank
	 * 			rank of the card
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Method compares this card to with a card provided according to the rules of Big Two
	 * 
	 * @return 1 if this if greater, -1 is another one is grater, 0 if equal
	 */
	public int compareTo(Card card) {
		int a = this.rank;
		int b = card.rank;
		if (a<2) a+=11;
		else a-=2;
		if (b<2) b+=11;
		else b-=2;
		if (a>b) return 1;
		else if (a<b) return -1;
		else if (this.suit>card.suit) return 1;
		else if (this.suit<card.suit) return -1;
		else return 0;
	}

}
