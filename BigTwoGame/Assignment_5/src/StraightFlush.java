/**
 * This class creates an instance of StraightFlush class
 *  
 * @author AliSanzhar
 *
 */
public class StraightFlush extends Hand {
	private static final long serialVersionUID = -7319311705716526303L;

	/**
	 * Creates and returns an instance of StraightFlush
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size()!=5) return false;
		else {
			sort();
			int [] a = {0, 0, 0, 0, 0};
			int [] b = {0, 0, 0, 0, 0};
			for (int i=0; i<5; ++i) {
				a[i] = getCard(i).getRank();
				b[i] = getCard(i).getSuit();
				a[i] -= i;
				if (i!=0 && (a[i]!=a[i-1] || b[i]!=b[i-1])) return false;
			}
			return true;
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "StraightFlush";
	}

}
