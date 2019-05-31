import java.util.*;

/**
 * This class is used for modeling a Hand of cards that will be played in Card Game 
 * 
 * @author AliSanzhar
 *
 */

public abstract class Hand extends CardList{
	private static final long serialVersionUID = -1061882053803389213L;
	private static final String[] HANDS = { "Straight", "Flush", "FullHouse", "Quad", "StraightFlush"}; //array of 5 card combinations in order low to high
	
	/**
	 * Creates and returns an instance of the Hand class
	 * 
	 * @param player
	 * 			player that will play Hand
	 * @param cards
	 * 			cards that compose Hand
	 */
	
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i=0; i<cards.size(); ++i) {
			addCard(cards.getCard(i));
		}
	}
	
	private CardGamePlayer player; //player of this hand
	
	/**
	 * getter for player of Hand
	 * 
	 * @return player
	 */
	
	public CardGamePlayer getPlayer() {
		return player;
	} 
	
	/**
	 * returns top card that will be compared to top card of another class
	 * 
	 * @return top card of this hand
	 */
	
	public Card getTopCard() {
		sort();
		return getCard(size()-1);
	}
	
	/**
	 * a method to check if this hand beats another one
	 * 
	 * @param hand
	 * 			hand to be compared to this
	 * @return true if this beats another hand and false if it is the same or hand beats this 
	 */
	
	public boolean beats(Hand hand) {
		if (this.size()!=hand.size()) return false;
		if (this.size()<5) {
			if (this.getTopCard().compareTo(hand.getTopCard())==1) return true;
			else return false;
		}
		else {
			if (this.getType()!=hand.getType()) {
				int a = Arrays.binarySearch(HANDS, this.getType());
				int b = Arrays.binarySearch(HANDS, hand.getType());
				if (a>b) return true;
				else return false;
			}
			else {
				if (this.getTopCard().compareTo(hand.getTopCard())==1) return true;
				else return false;
			}
		}
	}
	
	/**
	 * method to check validity of hand
	 * 
	 * @return true if a hand is valid for specified class and false if not
	 */
	
	public abstract boolean isValid();
	
	/**
	 * method that returns name of a hand
	 * 
	 * @return string containing name of a hand
	 */
	
	public abstract String getType();
}
