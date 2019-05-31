/**
 * This class creates instances of Triple class
 * 
 * @author AliSanzhar
 *
 */
public class Triple extends Hand {
	private static final long serialVersionUID = 2109395258622002051L;
	/**
	 * Creates and returns instance of Triple class variable
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size()==3) {
			if (this.getCard(0).rank==this.getCard(1).rank && this.getCard(1).rank==this.getCard(2).rank) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Triple";
	}

}
