package getStuck;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
@SuppressWarnings("serial")
public class GUIHandler extends JPanel{
	/**
	 * This class manages and draws graphics in the main game window.
	 */
	Dimension size; // This represents the size of the play area.
	int maxLookAhead = 10; // This value represents the deafault max look ahead.
	boolean aiVsAi = true; // Boolean that acts as storing whether the ai is playing against another ai or not.
	int sTurn = 0; // The turn of the user in a single player game.
	boolean twoP = true; // Boolean that acts as storing whether two players are playing the game.
	boolean pruning = true; // Boolean that acts as storing whether alpha beta pruning is to be used in the minimax or not.
	ArrayList<card> spades = new ArrayList<>(); //	// These array lists help in initializing the board, this is the array list of all the spades cards.
	ArrayList<card> clubs = new ArrayList<>(); // The array list of all the club cards.
	ArrayList<card> diamonds = new ArrayList<>(); // The array list of all the diamond cards.
	ArrayList<card> hearts = new ArrayList<>(); // The array list of all the heart cards.
	BufferedImage flipped; // This represents the sprite for a flipped card.
	card[][] board = new card[7][7]; // This represents the board of the cards.
	int turn = 1; // 1 is red and 2 is black
	Point emptySlot; // The point on the board where the empty point is present.
	int computed = 0; // The number of board states computed by the algorithm
	scoreFunctions sf = new scoreFunctions(); // The scoreFunctions instance to access its methods.
	MLAlgorithm ml = new MLAlgorithm(); // The MLAlgorithm instance to access its methods.
	moveFunctions mf = new moveFunctions(); // The moveFunctions instance to access its methods.
	Timer nextMove = new Timer(100, new ActionListener() {	// The timer for the next move of the ai.
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean play = true;			// This boolean stores whether it is the ai's turn to play or not.
			if(!aiVsAi && turn == sTurn) play = false;
			if(play) {
				long start = System.nanoTime(); // time before starting the minimax algorithm
				computed = 0; // The computed states is set to 0;
				Point minimax = ml.mm(turn, board, emptySlot); // Minimax is run
				String player = "| Red |";				// Displaying data in the console.
					if(turn == 2) player = "| Black |";
					String time = ((System.nanoTime() - start))+"";
					time = refine(time);
				System.out.println(player+" Number of board states = "+computed+" | Computed in "+time+" seconds.");
				System.out.println();
					turn = mf.makeMove(minimax.x, minimax.y, board, emptySlot, turn); // Moving the move by the minimax.
			}
			if(!aiVsAi) nextMove.stop(); // Stopping the loop for the user to play in a single player game.
		}
	});
	/**
	 * The panel class constructor.
	 * @param ai Whether the game is zero player or not.
	 */
	public GUIHandler(boolean ai) {
		setLayout(null);
		aiVsAi = ai; // Initializing the variables.
		size = new Dimension(getStuck.size.width-20, getStuck.size.height-20);
		for(int i = 0; i<13; i++) {		// Adding blank cards in the array lists.
			spades.add(new card(i+1, "spades"));
			clubs.add(new card(i+1, "clubs"));
			diamonds.add(new card(i+1, "diamonds"));
			hearts.add(new card(i+1, "hearts"));
		}
		try {		// Loading the card sprites from the sprite sheet.
			BufferedImage ss = ImageIO.read(new File("cards.png")); // Loading the sprite sheet.
			int iw = ss.getWidth()/13; // The height of a single card sprite.
			int ih = ss.getHeight()/5; // The width of a single card sprite.
			for(int j = 0; j<4; j++) {	// Setting the sprite of a card.
				for(int i = 0; i<12; i++) {
					if(i!=8) {
						if(i==0) {
							BufferedImage ta = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB); // The frame of the animation to be added.
							Graphics2D g = ta.createGraphics();// To get the frame we draw the sprite sheet on the frame and everything except the desired frame is cut off.		
							g.drawImage(ss, (int)(-12*(iw)), (int)(-j*(ih)),ss.getWidth(),ss.getHeight(), null); //As we have the Ace card in the 13th slot of the sprite sheet, but it has the value of 1, we add the ace card at the end first.
							if(j==0)hearts.get(0).setImage(ta);// Adding the sprite to the specific card.
							if(j==1)diamonds.get(0).setImage(ta);
							if(j==2)clubs.get(0).setImage(ta);
							if(j==3)spades.get(0).setImage(ta);
						}
						BufferedImage ta = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB);// Then we go back and add the cards at the start.
						Graphics2D g = ta.createGraphics();
						g.drawImage(ss, (int)(-i*(iw)), (int)(-j*(ih)),ss.getWidth(),ss.getHeight(), null);
						if(j==0)hearts.get(i+1).setImage(ta);
						if(j==1)diamonds.get(i+1).setImage(ta);
						if(j==2)clubs.get(i+1).setImage(ta);
						if(j==3)spades.get(i+1).setImage(ta);
					}
				}
			}
			BufferedImage ta = new BufferedImage((int)iw,(int)ih,BufferedImage.TYPE_INT_ARGB); 	//Setting the sprite for a flipped card in the same way.
			Graphics2D g = ta.createGraphics();
			g.drawImage(ss, (int)(0), (int)(-4*(ih)),ss.getWidth(),ss.getHeight(), null);
			flipped = ta;
			hearts.remove(9); //Removing the card with value 10 from each class.
			spades.remove(9);
			clubs.remove(9);
			diamonds.remove(9);
		}catch(Exception e) {e.printStackTrace();}
		ArrayList<card> cards = new ArrayList<>();	//now we will get a full deck of cards from the arraylist
		for(card c : hearts) cards.add(c);
		for(card c : diamonds) cards.add(c);
		for(card c : spades) cards.add(c);
		for(card c : clubs) cards.add(c);
		emptySlot = new Point(board[0].length/2, board.length/2); // Initialising the empty slot of the board.
		for(int x = 0; x<board[0].length; x++) { // Randomizing the board of cards.
			for(int y = 0; y<board.length; y++) {
				if(!(x==emptySlot.x && y==emptySlot.y)) {
					int ri = (int)(Math.random()*cards.size()); // A random card is chosen from the deck.
					board[y][x] = cards.get(ri); // The card is placed on the board.
					cards.remove(ri); // The card is removed from the deck.
				}
			}
		}
		if(sTurn == 2 && !aiVsAi) nextMove.start();	// If the first turn is of the ai, then next move is started.
		addMouseListener(new MouseAdapter() { // Adding the mouse listener for the player to make move.
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX() - 10; // Getting the point where the mouse is clicked.
				int y = e.getY() - 10;
				boolean yourTurn = (turn == sTurn) && (!aiVsAi); // If it is the turn of the user, then move is played.
				if(twoP) yourTurn = true;
				if(yourTurn) {
					ArrayList<Point> avMov = mf.getAvailableMoves(turn, emptySlot, board); // Getting a list of all the valid moves.
					if(avMov.size()==0) mf.addDiag(avMov, turn, emptySlot, board); // If there are no valid moves then valid diagonal moves are added.
					if(x>0 && y>0) {					// Playing a move.
						int idx = (int)(x/((double)size.width/7)); // Getting the x of the click on the board.
						int idy = (int)(y/((double)size.height/7)); // Getting the y of the click on the board.
						if(avMov.contains(new Point(idx,idy))) {// If the move is valid, then a move is made.
							System.out.println(emptySlot.x+", "+emptySlot.y);
							turn = mf.makeMove(idx,idy, board, emptySlot, turn);
							System.out.println(emptySlot.x+", "+emptySlot.y);
						}
					}
					if(!twoP) nextMove.start(); // If the next move is of the ai, then the next move is started.
				}
				if(aiVsAi) {// If it is ai vs ai, and the mouse is clicked the simulation is stopped or resumed.
					if(nextMove.isRunning()) {
						nextMove.stop();
					}
					else {
						nextMove.start();
					}
				}
				repaint();
			}
		});
	}
	/**
	 * This method displays all the graphics on the screen.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Displaying all the components.
		Graphics2D g2d = (Graphics2D) g; // Initializing the graphics object used to draw the graphics.
		for(int x = 0; x<board[0].length; x++) { // Drawing the board.
			for(int y = 0; y<board.length; y++) {
				if(board[y][x]!=null) {	// If there is a card on the spot, it is extracted and then drawn.
					card i = board[y][x];
					BufferedImage td = i.img;
					if(i.flipped) td = flipped;
					int w = size.width/7; // The optimal width is set.
					int h = (int)((double)td.getHeight()/td.getWidth() * w); // Height is set accordingly.
					g2d.drawImage(td, x*w + 10, y*h + 40, w-3, h-3, null); // Image is drawn.
				}
			}
		}
		g2d.setColor(Color.white);	// Displaying which player's turn it is.
		g2d.setFont(new Font("Arial", Font.BOLD, 20));
		String td = "Red's turn"; // Red player's turn is set by default.
		if(turn==2) td = "Black's turn"; // If it is not the red player's turn then the black player's turn is set.
		g2d.drawString(td, 10, size.height - 10 + 40);
		g2d.drawString("Red : "+sf.getScore(1, board), 20,30); // Displaying the score of both the players.
		g2d.drawString("Black : "+sf.getScore(2, board), getStuck.size.width - 140, 30);
		if(!nextMove.isRunning() && aiVsAi) { // Displaying a pause screen when the simulation is paused.
			g2d.setColor(new Color(0,0,0,150));	// Darkening the background.
			g2d.fillRect(0, 0, getStuck.f.getWidth(), getStuck.f.getHeight());
			if(mf.checkGameOver(emptySlot, board)==0) {			// If the game is not over, then two rectangles are drawn to display the pause icon.
				g2d.setColor(new Color(255,255,255,150));
				g2d.fillRect(getStuck.size.width/2 - 25 - 20, getStuck.size.height/2 - 10, 30, 100);
				g2d.setColor(new Color(255,255,255,150));
				g2d.fillRect(getStuck.size.width/2 + 5, getStuck.size.height/2 - 10, 30, 100);
			}
		}
		if((sTurn == 0) && !aiVsAi && !twoP) {	// If single player is selected, then a menu asking for which player you want to play is displayed.
			g2d.setColor(new Color(0,0,0,150));	// Darkening the background.
			g2d.fillRect(0, 0, getStuck.f.getWidth(), getStuck.f.getHeight());
			g2d.setColor(new Color(255,255,255,200));			// Displaying the "Choose your side" text.
			g2d.setFont(new Font("Arial black", Font.BOLD, 25));
			drawString(new Rectangle2D.Double(0, 0,getStuck.f.getWidth(),getStuck.f.getHeight()), "Choose your side", getStuck.f.getHeight()/2 - 100, g2d);
			if(getComponents().length==0) {	// Adding the buttons asking for the player's choice.
				JButton red = new JButton(); // The red button.
				JButton black = new JButton(); // The black button.
				red.setSize(getStuck.f.getWidth()/2 - 50, 100);	// Initializing the red button.
				red.setLocation(25, getStuck.f.getHeight()/2 - 40);
				red.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sTurn = 1; // The turn is set and the game begins.
						removeAll();
						repaint();
					}
				});
				initializeButton(red, new Color(255,0,0,180)); // The icon of the button is initialized.
				add(red);
				black.setSize(getStuck.f.getWidth()/2 - 50, 100);	// Initializing the black button.
				black.setLocation(getStuck.f.getWidth() - (black.getWidth() + 35), getStuck.f.getHeight()/2 - 40);
				black.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sTurn = 2; // The turn is set and the game begins.
						removeAll();
						repaint();
						nextMove.start();
					}
				});
				initializeButton(black, new Color(0,0,0,180)); // Initializing the icons.
				add(black);
			}
		}
	}
	/**
	 * This method is used to initialize the icons of the buttons for the player input.
	 * @param ti The button to edit. 
	 * @param c The color of the button icon.
	 */
	public void initializeButton(JButton ti, Color c) {
		BufferedImage i = new BufferedImage(ti.getWidth(), ti.getHeight(), BufferedImage.TYPE_INT_ARGB); // The icon image.
		Graphics2D g = i.createGraphics(); // The graphics of the icon.
		g.setColor(c);		// Drawing the icon image.
		g.fillRect(0,0,i.getWidth(),i.getHeight());
		ti.setContentAreaFilled(false);	// Setting the icon of the button as the icon image.
		ti.setBorderPainted(false);
		ti.setIcon(new ImageIcon(i));
	}
	/**
	 * This method draws a string in the middle of a bounding rectangle.
	 * @param b The bounding rectangle.
	 * @param a The string to draw.
	 * @param y The y position of the string.
	 * @param g The Graphics2D object to draw the string.
	 */
	public void drawString(Rectangle2D b, String a, double y, Graphics2D g) {
		JLabel test = new JLabel(a); // A place holder jlabel to get the preferred size.
		test.setFont(g.getFont());
		double ytd = y;
		if(ytd==-1) {
			ytd = b.getY()+b.getHeight()/2 + g.getFont().getSize()/3;	// If the y is given as -1, then it is also calculated in the middle of the bounding box.

		}
		g.drawString(a, (int)(b.getX()+b.getWidth()/2-test.getPreferredSize().width/2), (int)(ytd));	// Drawing the string with the graphics object.
	}
	public String refine(String tr) {
		String[] p = tr.split("");
		int ci = p.length-1;
		ArrayList<String> output = new ArrayList<>();
		while(ci>=0) {
			output.add(p[ci]);
			if(output.size()==9)output.add(".");
			ci--;
		}
		if(!output.contains(".")) {
			for(int i = 0; i<9-output.size(); i++) {
				output.add("0");
			}
			output.add(".");
		}
		String tro = "";
		for(int i = output.size()-1; i>=0; i--) {
			tro+=output.get(i);
		}
		return tro;
	}
}
