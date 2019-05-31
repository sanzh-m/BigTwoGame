/**
 * This class creates instance of FullHouse class
 * 
 * @author AliSanzhar
 *
 */
public class FullHouse extends Hand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4622772461907486829L;

	/**
	 * Creates and returns instance of FullHouse class
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Overrides getTopCard method as it has to return highest card in triple inside a fullHouse
	 * 
	 * @return top card of triple
	 */
	public Card getTopCard() {
		sort();
		int [] a = {0, 0, 0, 0, 0};
		for (int i=0; i<5; ++i) {
			a[i] = getCard(i).getRank();
		}
		if (a[0] == a[1] && a[1] == a[2]) return getCard(2);
		else return super.getTopCard();
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size()!=5) return false;
		else {
			sort();
			int [] a = {0, 0, 0, 0, 0};
			for (int i=0; i<5; ++i) {
				a[i] = getCard(i).getRank();
			}
			if ((a[0]==a[1] && a[2]==a[3] && a[3]==a[4]) || (a[0]==a[1] && a[1]==a[2] && a[3]==a[4])) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "FullHouse";
	}

}
