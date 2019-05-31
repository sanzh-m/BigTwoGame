import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class for GUI of Big Two game 
 * 
 * @author AliSanzhar
 *
 */

public class BigTwoTable implements CardGameTable  {
	
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JMenu menu;
	private JMenuBar menuBar;
	private JMenuItem connect, quit;
	private JPanel buttons;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea netMsgArea;
	private JTextArea msgInputArea;
	private JLabel msgInputAreaLabel;
	private JPanel textArea;
	private Dimension screenSize;
	private double widthOfScreen;
	private double heightOfScreen;
	private Image[][] cardImages;
	private Image cardBackImage, backGround;
	private Image[] avatars = new Image[4];
	private int[] cardsX;
	private int[] cardsY;
	
	/**
	 * Creates and returns the instance of BigTwoTable class, which is a GUI for Big Two game
	 * 
	 * @param game - the game, which needs the GUI 
	 */
	
	public BigTwoTable(BigTwoClient game) {
		this.game = game;
		activePlayer = -1;
		
		selected = new boolean[52];
		cardsX = new int[52];
		cardsY = new int[52];
		
		frame = new JFrame(game.getPlayerName());
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		widthOfScreen = screenSize.getWidth();
		heightOfScreen = screenSize.getHeight();
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setEnabled(true);
		
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		buttons = new JPanel();
		buttons.setBounds(0, (int)Math.round(heightOfScreen*0.95), (int)Math.round(widthOfScreen), (int)Math.round(heightOfScreen*0.05));
		buttons.add(playButton, BorderLayout.WEST);
		buttons.add(passButton, BorderLayout.WEST);
		
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		msgArea.setRows((int)Math.round(heightOfScreen/(msgArea.getFont().getSize()*3.2)));
		msgArea.setColumns((int)Math.round(widthOfScreen/35));
		msgArea.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		textArea = new JPanel();
		textArea.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		textArea.add(scroller, gbc);
		
		netMsgArea = new JTextArea();
		netMsgArea.setEditable(false);
		netMsgArea.setRows((int)Math.round(heightOfScreen/(netMsgArea.getFont().getSize()*3.2)));
		netMsgArea.setColumns((int)Math.round(widthOfScreen/35));
		netMsgArea.setLineWrap(true);
		JScrollPane scroller1 = new JScrollPane(netMsgArea);
		scroller1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		textArea.add(scroller1, gbc);
		
		msgInputArea = new JTextArea();
		msgInputArea.setEditable(true);
		msgInputArea.setRows(1);
		msgInputArea.setColumns((int)Math.round(widthOfScreen/40));
		msgInputArea.setLineWrap(false);
		msgInputAreaLabel = new JLabel("Message:");
		msgInputArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				
			}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					String s = msgInputArea.getText();
					game.sendMessage(new CardGameMessage(CardGameMessage.MSG, game.getPlayerID(), s));
					msgInputArea.setText("");
				}
			}
		});
		
		buttons.add(msgInputAreaLabel, BorderLayout.EAST);
		buttons.add(msgInputArea, BorderLayout.EAST);
		
		menuBar = new JMenuBar();
		menu = new JMenu("Actions");
		connect = new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(connect);
		menu.add(quit);
		menuBar.add(menu);
		
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		frame.add(buttons, BorderLayout.SOUTH);
		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(textArea, BorderLayout.EAST);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		widthOfScreen = screenSize.getWidth();
		heightOfScreen = screenSize.getHeight();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		cardImages = new Image[4][13];
		for (int i=0; i<4; ++i) {
			for (int j=0; j<13; ++j) {
				String s;
				switch (i) {
					case 0: {
						s = "diamonds_";
						break;
					}
					case 1: {
						s = "clubs_";
						break;
					}
					case 2: {
						s = "hearts_";
						break;
					}
					case 3: {
						s = "spades_";
						break;
					}
					default: s="";
				}
				cardImages[i][j] = new ImageIcon("cards/"+s+(j+1)+".png").getImage();
			}
		}
		cardBackImage = new ImageIcon("cards/Back.png").getImage();
		backGround = new ImageIcon("cards/backGround.png").getImage();
		avatars[0] = new ImageIcon("avatars/Luke.png").getImage();
		avatars[1] = new ImageIcon("avatars/Yoda.png").getImage();
		avatars[2] = new ImageIcon("avatars/Darth_Vader.png").getImage();
		avatars[3] = new ImageIcon("avatars/Emperor_Palpatine.png").getImage();
		repaint();	
		
		
	}
	
	private class BigTwoPanel extends JPanel implements MouseListener{

		private static final long serialVersionUID = -7875355567977595879L;
		private int cardWidth;
		private int cardHeight;		
		
		public BigTwoPanel() {
			super();
			addMouseListener(this);
			cardWidth = (int)Math.round(widthOfScreen/25);
			cardHeight = (int)Math.round(heightOfScreen/10);
			resetCardsCoordinates();
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
			if (bigTwoPanel.isEnabled()) {
				int X = arg0.getX();
				int Y = arg0.getY();
				ArrayList<Integer> selectedCards = new ArrayList<Integer>();
				
				for (int i = game.getPlayerID()*13; i<game.getPlayerID()*13+game.getPlayerList().get(game.getPlayerID()).getCardsInHand().size(); ++i) {
					if ( (X>=cardsX[i] && X<=cardsX[i]+cardWidth) && (Y>=cardsY[i] && Y<=cardsY[i]+cardHeight)) {
						selectedCards.add(i);
					}
				}
				
				if (selectedCards.size()!= 0) {
					int topSelectedCard = selectedCards.get(selectedCards.size()-1);
					if (!selected[topSelectedCard]) cardsY[topSelectedCard] -= (int)Math.round(heightOfScreen/50);
					else cardsY[topSelectedCard] += (int)Math.round(heightOfScreen/50);
					selected[topSelectedCard] = !selected[topSelectedCard];
					this.repaint();
				}				
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//nothing to do
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//nothing to do
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//nothing to do
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//nothing to do
		}
		
		protected void paintComponent (Graphics g) {
			
			
			Graphics2D G2D = (Graphics2D) g;
			super.paintComponent(G2D);
			
			//G2D.drawImage(new ImageIcon("cards/back.png").getImage(), 10, 10, this);
			
			int penX = (int) Math.round(widthOfScreen/200);
			int penY = (int) Math.round(heightOfScreen/20);
			
			G2D.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), this);
			for (int i=0; i<4; ++i) {
				if (game.getPlayerList().get(i).getName()!="") {
					if (i==activePlayer && i!=game.getPlayerID()) {
						G2D.setColor(Color.RED);
					}
					else if (i!=activePlayer && i!=game.getPlayerID()){
						G2D.setColor(Color.BLACK);
					}
					else {
						G2D.setColor(Color.BLUE);
					}
					
					G2D.drawImage(avatars[i], penX, penY, (int)Math.round(widthOfScreen/12), (int)Math.round(heightOfScreen/7), this);
					G2D.drawString(game.getPlayerList().get(i).getName(), penX+(int)Math.round(widthOfScreen/100), penY+(int)Math.round(heightOfScreen/7.5));
					
					penY += (int) Math.round(heightOfScreen/5);
					
				}	
			}
			if (game.gameIsOn()) {
				System.out.println("I'm here as well");
				for (int i=0; i<game.getNumOfPlayers(); ++i) {
					int numOfCards = game.getPlayerList().get(i).getNumOfCards();
					for (int j=0; j<numOfCards; j++) {
						Image image;
						if (i==game.getPlayerID()) {
							int rank = game.getPlayerList().get(i).getCardsInHand().getCard(j).rank;
							int suit = game.getPlayerList().get(i).getCardsInHand().getCard(j).suit;
							image = cardImages[suit][rank];
						}
						else image = cardBackImage;
						
						G2D.drawImage(image, cardsX[13*i+j], cardsY[13*i+j], cardWidth, cardHeight, this);
					}
				}
				if (!game.getHandsOnTable().isEmpty()) {
					Hand lastHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
					int handX = (int) Math.round(widthOfScreen/2.5);
					int handY = (int) Math.round(heightOfScreen/15);
					
					G2D.setColor(Color.LIGHT_GRAY);
					G2D.fillRect(handX, handY-(int) Math.round(heightOfScreen/30), (int) Math.round(widthOfScreen/7), (int) Math.round(heightOfScreen/40));
					G2D.setColor(Color.BLACK);
					G2D.drawString("Last hand was played by " + lastHand.getPlayer().getName(), handX, handY-(int) Math.round(heightOfScreen/80));
					for (int i=0; i<lastHand.size(); ++i) {
						int rank = lastHand.getCard(i).rank;
						int suit = lastHand.getCard(i).suit;
						Image image = cardImages[suit][rank];
						G2D.drawImage(image, handX, handY, cardWidth, cardHeight, this);
						handX += (int) Math.round(widthOfScreen/35);
					}
					
					handX = (int) Math.round(widthOfScreen/2.5);
					handY += (int) Math.round(widthOfScreen/15);
					
					for (int i=0; i<game.getHandsOnTable().size()-1; ++i) {
						Hand playedHand = game.getHandsOnTable().get(i);
						for (int j=0; j<playedHand.size(); ++j) {
							G2D.drawImage(cardBackImage, handX, handY, cardWidth, cardHeight, this);
							handX += (int) Math.round(heightOfScreen/100);
						}
					}
				}
			}
		}
		
	}
	
	private class PlayButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			if (bigTwoPanel.isEnabled() && activePlayer==game.getPlayerID()) {
				if (getSelected() != null) game.makeMove(activePlayer, getSelected());
				else printMsg("Cards are not selected, please select or press Pass\n");
			}
			repaint();
			
		}
		
	}
	
	private class PassButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if (bigTwoPanel.isEnabled() && activePlayer==game.getPlayerID()) {
				game.makeMove(activePlayer, null);
				resetCardsCoordinates();
				repaint();
			}
			
		}
		
	}
	
	private class ConnectMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			game.makeConnection();
			
		}
		
	}
	
	private class QuitMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			System.exit(0);
		}
		
	}
	
	@Override
	public void setActivePlayer(int activePlayer) {
		// TODO Auto-generated method stub
		if (activePlayer<0 || activePlayer>game.getNumOfPlayers()) this.activePlayer = -1;
		else this.activePlayer=activePlayer;
		if (activePlayer != -1) {
			this.printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn:\n");
		}
	}

	@Override
	public int[] getSelected() {
		// TODO Auto-generated method stub
		ArrayList<Integer> selectedCards = new ArrayList<Integer>();
		
		for (int i = activePlayer*13; i < activePlayer*13+game.getPlayerList().get(activePlayer).getCardsInHand().size(); ++i) {
			if (selected[i]) {
				selectedCards.add(i-activePlayer*13);
			}
		}
		
		if (selectedCards.size()==0) return null;
		else {
			int [] returnSelected = new int[selectedCards.size()];
			for (int i=0; i<selectedCards.size(); ++i) {
				returnSelected[i] = selectedCards.get(i);
			}
			return returnSelected;
		}
	}

	@Override
	public void resetSelected() {
		// TODO Auto-generated method stub
		selected = new boolean[52];
	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		resetCardsCoordinates();
		frame.repaint();
	}

	@Override
	public void printMsg(String msg) {
		// TODO Auto-generated method stub
		msgArea.append(msg);
	}
	
	/**
	 * Method to add text to network text area
	 * @param msg the message to add
	 */
	
	public void printNetMsg(String msg) {
		// TODO Auto-generated method stub
		netMsgArea.append(msg);
	}

	@Override
	public void clearMsgArea() {
		// TODO Auto-generated method stub
		repaint();
		msgArea.setText(null);
	}
	
	/**
	 * Method to clear network text area
	 */
	
	public void clearNetMsgArea() {
		repaint();
		netMsgArea.setText(null);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		resetSelected();
		clearMsgArea();
		enable();
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		bigTwoPanel.setEnabled(true);
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * Make the network chat enabled or disabled
	 * @param condition true - enables, false - disabled
	 */
	
	public void chatEnable(boolean condition) {
		netMsgArea.setEnabled(condition);
	}
	
	/**
	 * Make the connect option enabled or disabled
	 * @param condition true - enables, false - disables
	 */
	
	public void connectEnable(boolean condition) {
		connect.setEnabled(condition);
	}
	
	/**
	 * This method assigns coordinates to cards, usually this method is called after playing cards, because remain cards needs to be rearranged
	 */
	
	public void resetCardsCoordinates() {
		int x = (int)Math.round(widthOfScreen/12);
		int y = (int)Math.round(heightOfScreen/14);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				x += (int)Math.round(widthOfScreen/60);
				cardsX[(i * 13) + j] = x;	
				cardsY[(i * 13) + j] = y;	
			}
			x = (int)Math.round(widthOfScreen/12);
			y += (int)Math.round(heightOfScreen/5);
		}
		resetSelected();
	}

}
