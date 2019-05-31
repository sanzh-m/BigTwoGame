import java.util.*;

import javax.swing.*;

import java.io.*;
import java.net.*;

/**
 * This class creates an instance of Big Two Game Client and also starts it
 * 
 * @author AliSanzhar
 *
 */

public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	private boolean fullServer, gameInProcess = false;
	
	/**
	 * Creates and returns instance of BigTwoClient class
	 */
	
	public BigTwoClient() {
		
		playerList = new ArrayList<CardGamePlayer>();
		playerList.add(new CardGamePlayer(""));
		playerList.add(new CardGamePlayer(""));
		playerList.add(new CardGamePlayer(""));
		playerList.add(new CardGamePlayer(""));
		
		String s = JOptionPane.showInputDialog("Type in your name");
		setPlayerName(s);
		
		table = new BigTwoTable(this);
		
		makeConnection();
	}
	
	/**
	 * Starts the Big Two Game Client
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BigTwoClient();
	}

	/**
	 * 
	 * An inner class that is used to handle server incoming messages
	 * 
	 * @author AliSanzhar
	 *
	 */
	
	private class ServerHandler implements Runnable {
		
		private ObjectInputStream ois;
		
		/**
		 * Creates and return an instance of ServerHandler
		 */
		
		public ServerHandler() {
			try {
				ois = new ObjectInputStream(sock.getInputStream());
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			CardGameMessage message;
			
			try {

				while((message = (CardGameMessage) ois.readObject()) != null) {

					parseMessage(message);
					
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				
				if(fullServer) {
					fullServer = false;
				} else {
					
					if(gameIsOn()) {
						
						currentIdx = -1;
						prepareGame();
						table.setActivePlayer(currentIdx);
						table.disable();
					}				
					
					
					for(int i = 0; i < numOfPlayers; i++) {
						playerList.get(i).setName("");
					}
					table.clearMsgArea();
					table.clearNetMsgArea();
					
					
					table.repaint();
					
					table.chatEnable(false);
					
					table.connectEnable(true);
					
					JOptionPane.showMessageDialog(null, "Lost connection to the server.");					
				}
			}
		}
		
	}
	
	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID = playerID;
	}

	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return playerName;
	}

	@Override
	public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName = playerName;
	}

	@Override
	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP;
	}

	@Override
	public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP = serverIP;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return serverPort;
	}

	@Override
	public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort = serverPort;
	}

	@Override
	public void makeConnection(){
		// TODO Auto-generated method stub
		JOptionPaneMultiInput connectionDetails = new JOptionPaneMultiInput("Please enter IP and TCP port", "IP:", "TCP port:", "127.0.0.1", "2396");
		connectionDetails.getInput();
		setServerIP(connectionDetails.getFirstInput());
		try {
			setServerPort(Integer.parseInt(connectionDetails.getSecondInput()));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			sock = new Socket(serverIP, serverPort);
			
			oos = new ObjectOutputStream(sock.getOutputStream());
			
			Runnable serverHandlerJob = new ServerHandler();
			Thread serverHandler = new Thread(serverHandlerJob);
			serverHandler.start();
			
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
		}
		catch (Exception ex) {
			table.printMsg("Error in establishing a connection with the server! Please try again!");
			
			table.chatEnable(false);
			table.connectEnable(true);
		}
	}

	@Override
	public void parseMessage(GameMessage message) {
		// TODO Auto-generated method stub
		switch(message.getType()) {
			case CardGameMessage.PLAYER_LIST:{
				setPlayerID(message.getPlayerID());
				
				String[] arrNames = (String[]) message.getData();
				for (int i=0; i<4; ++i) {
					if (arrNames[i]!=null) {
						playerList.get(i).setName(arrNames[i]);
					}
				}
				
				table.connectEnable(false);
				table.chatEnable(true);
				
				break;
			}
			case CardGameMessage.JOIN:{
				playerList.get(message.getPlayerID()).setName((String)message.getData());
				
				table.repaint();
				
				if(message.getPlayerID() == playerID) {
					
					table.printNetMsg("Connected to server at/" + serverIP + ":" + serverPort + "\n");
					
					
					sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				}
				
				break;
			}
			case CardGameMessage.FULL:{
				fullServer = true;
				
				table.printNetMsg("Unfortunately, the server is full already. Please try to join later :(");
				
				table.chatEnable(false);
				table.connectEnable(true);
				
				break;
			}
			case CardGameMessage.QUIT:{				
				table.printNetMsg(playerList.get(message.getPlayerID()).getName()+" leaves the game :(\n");
				playerList.get(message.getPlayerID()).setName("");
				
				if (gameIsOn()) {
					currentIdx = -1;
					prepareGame();
					table.setActivePlayer(currentIdx);
					table.disable();
				}
				
				table.repaint();
				
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				
				break;
			}
			case CardGameMessage.READY:{
				
				table.printNetMsg(playerList.get(message.getPlayerID()).getName()+" is ready :)\n");
				
				break;
			}
			case CardGameMessage.START:{
				start((BigTwoDeck)message.getData());
				gameInProcess = true;
				
				break;
			}
			case CardGameMessage.MOVE:{
				checkMove(message.getPlayerID(), (int[])message.getData());
				
				break;
			}
			case CardGameMessage.MSG:{
				table.printNetMsg((String) message.getData()+"\n");
				
				break;
			}
			default:{
				table.printNetMsg("Wrong message type: " + message.getType());
				// invalid message
				break;
			}
		}
	}

	@Override
	public void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
		try {
			oos.writeObject(message);
			
			oos.flush();
		}
		catch(Exception ex) {
			table.printMsg("Error sending message, Please try again!");
			ex.printStackTrace();
		}
	}

	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return playerList.size();
	}

	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return deck;
	}

	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return playerList;
	}

	@Override
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return handsOnTable;
	}

	@Override
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return currentIdx;
	}

	@Override
	public void start(Deck deck) {
		// TODO Auto-generated method stub
		prepareGame();
		table.reset();
		
		this.deck = deck;
		
		for (int i=0; i<52; ++i) {
			Card card = deck.getCard(i);
			if (card.getRank()==2 && card.getSuit()==0) currentIdx = i/13;
			playerList.get(i/13).addCard(card);
		}
		
		playerList.get(0).sortCardsInHand();
		playerList.get(1).sortCardsInHand();
		playerList.get(2).sortCardsInHand();
		playerList.get(3).sortCardsInHand();
		
		table.setActivePlayer(currentIdx);
		table.repaint();
	}

	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx));
	}

	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		table.repaint();
		Hand newHand = null;
		CardList cards = null;
		if (cardIdx == null) {
			table.printMsg("{Pass}");
			if (handsOnTable.isEmpty()) {
				if (this.playerID == playerID) {
					JOptionPane.showMessageDialog(null, "Choose wisely", "You shall not pass with your first move", JOptionPane.ERROR_MESSAGE);
				}
				table.printMsg(" <== Not a legal move!!!\n");
				} 
			else if (playerList.get(currentIdx).getName()!=handsOnTable.get(handsOnTable.size()-1).getPlayer().getName()) {
				table.printMsg("\n");
				currentIdx++;
				currentIdx%=4;
				table.setActivePlayer(currentIdx);
			}
			else if (playerList.get(currentIdx).getName()==handsOnTable.get(handsOnTable.size()-1).getPlayer().getName()) {
				if (this.playerID == playerID) {
					JOptionPane.showMessageDialog(null, "Choose wisely", "You shall not pass, as you were the last one to play the hand", JOptionPane.ERROR_MESSAGE);
				}
				table.printMsg(" <== Not a legal move!!!\n");
			}
		}
		else {
			cards = playerList.get(currentIdx).play(cardIdx);
			if (cards.size()!=cardIdx.length) {
				if (this.playerID == playerID) {
					JOptionPane.showMessageDialog(null, "Choose wisely", "You shall not play cards that you don't have", JOptionPane.ERROR_MESSAGE);
				}
				table.printMsg("Player cannot play cards that they don't have");
			}
			else {
				newHand = composeHand(playerList.get(currentIdx), cards);
				if (newHand == null) {
					if (this.playerID == playerID) {
						JOptionPane.showMessageDialog(null, "Choose wisely", "You shall not play cards that do not compose a valid hand", JOptionPane.ERROR_MESSAGE);
					}
					table.printMsg("Selected cards do not compose a valid Hand");
				}
				else {
					table.printMsg("{"+newHand.getType()+"}");
					if (!handsOnTable.isEmpty()) {
						if (newHand.beats(handsOnTable.get(handsOnTable.size()-1)) || handsOnTable.get(handsOnTable.size()-1).getPlayer().getName()==playerList.get(currentIdx).getName()) {
							table.printMsg("\n");
							playerList.get(currentIdx).removeCards(cards);
							handsOnTable.add(newHand);
							if (!endOfGame()) {
								currentIdx++;
								currentIdx%=4;
								table.setActivePlayer(currentIdx);
								table.repaint();
							}
							else {
								table.disable();
								table.repaint();
								String results = "";
								if (currentIdx == playerID) {
									results += "You win the game. \n";
								}
								else {
									results += playerList.get(currentIdx).getName()+" wins the game. \n";
								}
								for (CardGamePlayer player: playerList) {
									if (player.getNumOfCards() != 0) {
										if (player == playerList.get(playerID)) {
											results += "You have "+player.getNumOfCards()+" cards in hand. \n";
										}
										else {
											results += player.getName()+" has "+player.getNumOfCards()+" cards in hand. \n";
										}
									}
								}
								JOptionPane.showMessageDialog(null, "The game has ended!\n" + results, "End game", JOptionPane.INFORMATION_MESSAGE);
								sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
							}
							
						}
						else {
							if (this.playerID == playerID) {
								JOptionPane.showMessageDialog(null, "Choose wisely", "You shall not play a hand that cannot beat previously played hand, unless you were the last one to play it", JOptionPane.ERROR_MESSAGE);
							}
							table.printMsg(" <== Not a legal move!!!\n");
						}
					}
					else {
						if (newHand.contains(new Card(0, 2))) {
							table.printMsg("\n");
							playerList.get(currentIdx).removeCards(cards);
							handsOnTable.add(newHand);
							currentIdx++;
							currentIdx%=4;
							table.setActivePlayer(currentIdx);
							table.repaint();
							
						}
						else {
							if (this.playerID == playerID) {
								JOptionPane.showMessageDialog(null, "Choose wisely", "You shall not start the game with Hand that doesn't contain \\u26663", JOptionPane.ERROR_MESSAGE);
							}
							table.printMsg(" <== Not a legal move!!!\n");
						}
					}
				}
			}
		}
	}

	@Override
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		for (int i=0; i<4; ++i) {
			if (playerList.get(i).getNumOfCards()==0) {
				gameInProcess = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Method that prepares the game by resetting played hands and cards that players have
	 */
	
	public void prepareGame() {
		for (int i=0; i<playerList.size(); ++i) {
			if (playerList.get(i).getNumOfCards()!=0) playerList.get(i).removeAllCards();
		}
		
		handsOnTable = new ArrayList<Hand>();
	}
	
	/**
	 * Method that checks if a game is in process
	 * 
	 * @return bool - true if is process, false if not
	 */
	
	public boolean gameIsOn() {
		if (endOfGame()) {
			return false;
		}
		else if (!gameInProcess) {
			return false;
		}
		else {
			for(int i = 0; i < numOfPlayers; i++) {
				if(playerList.get(i).getName() == "") {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Static method that returns a hand if it is possible to construct a hand from cards provided 
	 * @param player
	 * 			player who tries to build a hand
	 * @param cards
	 * 			cards for hand creation
	 * @return Hand if it is possible to create any, null if not
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		switch (cards.size()) {
		case 1:
		{
			return new Single(player, cards);
		} 
		case 2:
		{
			Hand hand = new Pair(player, cards);
			if (hand.isValid()) return hand;
			else return null;
		}
		case 3:
		{
			Hand hand = new Triple(player, cards);
			if (hand.isValid()) return hand;
			else return null;
		}
		case 5:
		{
			cards.sort();
			Hand hand = new StraightFlush(player, cards);
			if (hand.isValid()) return hand;
			hand = new Straight(player, cards);
			if (hand.isValid()) return hand;
			hand = new Flush(player, cards);
			if (hand.isValid()) return hand;
			hand = new FullHouse(player, cards);
			if (hand.isValid()) return hand;
			hand = new Quad(player, cards);
			if (hand.isValid()) return hand;
		}
		default: return null;
		}
	}
	
	/**
	 * An inner class to handle double input
	 * 
	 * @author AliSanzhar
	 *
	 */
	
	private class JOptionPaneMultiInput {

		private String nameOfPanel;
		private JTextField firstField;
	    private JTextField secondField;
	    private JPanel myPanel;
	    private String firstInput;
	    private String secondInput;
	    
	    /**
	     * Creates and return an instance of JOptionPaneMultiInput
	     * 
	     * @param nameOfPanel name of the panel
	     * @param nameFirstField name of the first field
	     * @param nameSecondField name of the second field
	     */
		
		public JOptionPaneMultiInput(String nameOfPanel, String nameFirstField, String nameSecondField, String firstFieldInitialInput, String secondFieldInitialInput) {
			this.nameOfPanel = nameOfPanel;
			firstField = new JTextField(5);
			firstField.setText(firstFieldInitialInput);
			secondField = new JTextField(5);
			secondField.setText(secondFieldInitialInput);
			myPanel = new JPanel();
			
			myPanel.add(new JLabel(nameFirstField));
		    myPanel.add(firstField);
		    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		    myPanel.add(new JLabel(nameSecondField));
		    myPanel.add(secondField);
		 
		}
		
		/**
		 * Method to launch the input window and type the information
		 */
		
		public void getInput() {
			int result = JOptionPane.showConfirmDialog(null, myPanel, nameOfPanel, JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					firstInput = firstField.getText();
					secondInput = secondField.getText();
				}
		}
		
		/**
		 * Method to retrieve gathered information
		 * @return input of the first field
		 */
		
		public String getFirstInput() {
			return firstInput;
		}
		
		/**
		 * Method to retrieve gathered information
		 * @return input of the second field
		 */
		
		public String getSecondInput() {
			return secondInput;
		}
	}
	
}
