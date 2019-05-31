/**
 * This class creates an instance of Flush class
 * 
 * @author AliSanzhar
 *
 */
public class Flush extends Hand {
	private static final long serialVersionUID = -1176872676059568463L;

	/**
	 * Creates and returns instance of Flush class
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size()!=5) return false;
		else {
			int [] a = {0, 0, 0, 0, 0};
			for (int i=0; i<5; ++i) {
				a[i] = getCard(i).getSuit();
				if (i!=0 && a[i]!=a[i-1]) return false;
			}
			return true;
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Flush";
	}

}
