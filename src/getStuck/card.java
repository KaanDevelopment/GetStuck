package getStuck;
import java.awt.image.BufferedImage;
public class card{
	/**
	 * This class contains attributes for a card object.
	 */
	int val; // This value acts as the face value of the card.
	String clas; // The type of the card (Hearts, Diamonds, Spades and Clubs).
	BufferedImage img; // The sprite of the card.
	boolean isRed = false; // Boolean whether the card is red in color or not.
	boolean flipped = false; // Boolean whether the card is flipped on the board or not.
	/**
	 * Main card class constructor.
	 * @param v This parameter represents the value of the card. 
	 * @param t This parameter represents the type of the card.
	 */
	public card(int v, String t) {
		val = v;
		clas = t;
		// Setting the isRed boolean based on the type of card.
		if(clas.equals("diamonds") || clas.equals("hearts")) isRed = true;
	}
	/**
	 * This method sets the sprite of the card.
	 * @param i The image with which the sprite of the card is to be set.
	 */
	public void setImage(BufferedImage i) {
		img = i;
	}
}