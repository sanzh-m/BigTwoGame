/**
 * This class creates instances of Straight class
 * 
 * @author AliSanzhar
 *
 */
public class Straight extends Hand {
	private static final long serialVersionUID = 4735239255262917363L;

	/**
	 * Creates and returns an instance of Straight class
	 * 
	 * @param player
	 * 			player who will play this hand
	 * @param cards
	 * 			cards that compose this hand
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size()!=5) return false;
		else {
			sort();
			int[] a = {0, 0, 0, 0, 0};
			for (int i=0; i<5; ++i) {
				a[i] = getCard(i).getRank();
				if (a[i]<2) a[i]+=11;
				else a[i]-=2;
				a[i] -= i;
				if (i!=0 && a[i]!=a[i-1]) return false;
			}
			return true;
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Straight";
	}

}
