/**
 * This class is used to create instances of BigTwoDeck 
 * 
 * @author AliSanzhar
 *
 */
public class BigTwoDeck extends Deck {
	private static final long serialVersionUID = 8254481884293570270L;
	/**
	 * Creates and returns an instance of BigTwoDeck 
	 */
	public BigTwoDeck() {
		// TODO Auto-generated constructor stub
		super();
	}
	/**
	 * Override a method as this deck uses BigTwoCard instead or regular Card
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}

}
