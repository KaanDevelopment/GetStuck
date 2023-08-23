package getStuck;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
public class getStuck {
	/**
	 * This class manages displaying and initializing the main game frame and panel. 
	 */
	static GUIHandler p; // This panel (p) of the window used for drawing the graphics.
	static JFrame f; // The JFframe (f) acts as the main frame.
	static JFrame o; // The Jframe (o) acts as the settings window frame.
	static Dimension size = new Dimension(350,(int)(1.4444444444444444 * 350)+20); // Initializing the window size.
	static int total = 1; // The default total number of games that'll be played in AI vs AI mode.
	static int played = 0; // The deafult number of games played.
	static int rwins = 0; // The number of times the red player wins.
	static int bwins = 0; // The number of times the black player wins.
	static boolean ai = false; // Boolean whether the game is played between two ai players.
	static boolean pruned = false; // Boolean whether alpha beta pruning is enabled or not.
	static boolean twoPlayer = false; // Boolean whether there are two users playing the game.
	/** 
	 * The main method.
	 * @param args These are the arguments of the main method.
	 */
	public static void main(String[] args) {
		// Initializing the settings window.
		o = new JFrame("Settings");
		o.setSize(300,300); // Setting the size.
		o.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - 150, Toolkit.getDefaultToolkit().getScreenSize().height/2 - 150); // Setting the location.
		o.setResizable(false); // Setting the window unresizable.
		o.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o.setContentPane(new options()); // Setting the panel of the frame.
		o.setVisible(true);
		// Initializing the default panel to ensure not having any null pointer exceptions if it is used.
		p = new GUIHandler(true);
		p.aiVsAi = ai;
		p.pruning = pruned;
		// Initializing the main game frame.
		f = new JFrame("Get Stuck!");
		f.setSize(size.width+10, size.height+55);
		f.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - size.width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - size.height/2);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * This method restarts the game by reinitializing the main panel.
	 */
	public static void restart() {
		p.nextMove.stop(); // Stopping the main game loop.
		boolean ai = p.aiVsAi; // Storing the ai boolean of the panel.
		int mla = p.maxLookAhead; // Storing the max look ahead of the panel.
		boolean twoP = p.twoP; // Storing the two player boolean of the panel.
		p = new GUIHandler(p.aiVsAi); // Initializing the panel object.
		p.maxLookAhead = mla; 	// Setting some parameters.
		p.twoP = twoP;
		if(ai) p.nextMove.start();	// Resetting the content pane of the main frame as the panel is reinitialised.
		f.setContentPane(p);
		f.getContentPane().setBackground(Color.green.darker().darker());
		f.setVisible(true);
	}
}
