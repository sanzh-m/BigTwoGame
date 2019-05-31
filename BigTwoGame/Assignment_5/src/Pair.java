/**
 * This class is used to create instances of Pair class
 * 
 * @author AliSanzhar
 *
 */
public class Pair extends Hand {
	private static final long serialVersionUID = -7735787877198652522L;
	
	/**
	 * Creates and returns instance of Pair class
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size()==2) {
			if (this.getCard(0).rank == this.getCard(1).rank) return true;
		}
		return false;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Pair";
	}

}
