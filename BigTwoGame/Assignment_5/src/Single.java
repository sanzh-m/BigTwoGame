/**
 * This class is used to create instances of Single class
 * 
 * @author AliSanzhar
 *
 */
public class Single extends Hand {
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * Creates and returns an instance of Single class
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isValid() {
		if (size()==1) return true;
		return false;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Single";
	}

}
