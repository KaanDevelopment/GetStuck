package getStuck;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
public class scoreFunctions {
	/**
	 * This class conatains manages dealing with saving the data and getting the score of a particular player.
	 */
	/**
	 * This method saves the data of a simulation in the data.txt file.
	 * @param the parameter rscore represents the red player score.
	 * @param the parameter bscore represents the black player score.
	 * @param rWins is a boolean whether the red player has won or not.
	 */
	public void save(int rscore, int bscore, boolean rWins) {
		try {
			File file = new File("data.txt"); // Loading the data.txt file.
			Scanner s = new Scanner(file); // Scanner for reading the already present data in the file.
			String content = ""; // The content to write in the file.
			while(true) {
				try { 
					content+=s.nextLine()+"\n"; // Loading the already present data in the file.
				}catch(Exception e) {break;}
			}
			if(getStuck.played == 1) {	// Adding the new data to save.
				content+="______________Another session______________\n";
			}
			String wonText = "Red wins.";
			if(!rWins) wonText = "Black wins.";
			content+="Game "+getStuck.played+" : Red = "+rscore +" | Black = "+bscore+"\n     "+wonText+"\n\n";
			FileWriter fw = new FileWriter("data.txt");	// Writing the data and then closing the filewriter.
			fw.write(content);
			fw.flush();
			fw.close();
			s.close();
		}catch(Exception e) {}
	}
	/**
	 * This method returns the score of a given player.
	 * @param turn The player.
	 * @return
	 */
	public int getScore(int turn, card[][] board) {
		int ret = 0; // The returning score.
		boolean isRed = false;		// If the player is red or not.
		if(turn == 1) isRed = true;
		for(int x = 0; x<board[0].length; x++) {
			for(int y = 0; y<board.length; y++) {
				if(board[y][x]!=null && !board[y][x].flipped && board[y][x].isRed == isRed) {// If there is a card on the board and the card is of the same player, then its value is added to the returning value.
					if(board[y][x].val<10) ret+=board[y][x].val;
					else ret += 10;
				}
			}
		}
		return ret;
	}
}