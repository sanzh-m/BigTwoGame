/**
 * This class creates instances of Quad class
 * 
 * @author AliSanzhar
 *
 */
public class Quad extends Hand {
	private static final long serialVersionUID = -6513678899946621301L;

	/**
	 * Creates and returns instance of Quad class
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	/**
	 * This method override getTopCard as it has to return highest card in quadruplet
	 * 
	 * @return top card
	 */
	public Card getTopCard() {
		sort();
		int [] a = {0, 0, 0, 0, 0};
		for (int i=0; i<5; ++i) {
			a[i] = getCard(i).getRank();
		}
		if (a[0]==a[1]) return getCard(3);
		else return getCard(4);
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
			if ((a[0]==a[1] && a[1]==a[2] && a[2]==a[3]) || (a[1]==a[2] && a[2]==a[3] && a[3]==a[4])) return true;
			else return false;
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Quad";
	}

}
