package getStuck;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class moveFunctions {
	/**
	 *  This class contains methods to make moves on the board and contains all the logic.
	 */	
	scoreFunctions sf = new scoreFunctions(); // The instance of the scoreFunctions class to access its methods.
	/**
	 * Makes a move of a given point.
	 * @param x The x co-ordinate of the move.
	 * @param y The y co-ordinate of the move.
	 * @return The turn code of the next player.
	 */
	public int makeMove(int x, int y, card[][] board, Point emptySlot, int turn) {		
		board[emptySlot.y][emptySlot.x] = board[y][x]; 	// Move is made.
		board[emptySlot.y][emptySlot.x].flipped = true;// The card is placed and flipped.
		board[y][x] = null;		// The card at the point is removed.
		emptySlot.x = x;
		emptySlot.y = y;
		int retturn = turn+1;// The turn is changed.
		if(retturn>2) retturn = 1;
		getStuck.p.repaint();
		ArrayList<Point> future = getAvailableMoves(retturn, emptySlot, board); // Checking if the game is finished in the next turn.
		if(future.size()==0) addDiag(future, retturn, emptySlot, board); // Getting the available moves of the next player.
		if(future.size()==0) {		// If the player is stuck.
			getStuck.p.nextMove.stop(); // The loop is stopped.
			int winningScore = 0; // The score of the winning score.
			int losingScore = 0; // The score of the losing score.
			boolean rWin = true; // Boolean whether the red player has won.
			if(retturn == 1) rWin = false;
			for(int idx = 0; idx<board[0].length; idx++) {	// Calculating the scores.
				for(int idy = 0; idy<board.length; idy++) {
					if(board[idy][idx]!=null && !board[idy][idx].flipped) {	// If there is a card on the slot and it is not flipped, then its value is considered.
						int fval = board[idy][idx].val; // The face value of the card
						if(fval<10) { // Adding the face value of the card to the winning score or the losing score accordingly.
							if(board[idy][idx].isRed==rWin) winningScore+=fval;
							else losingScore+=fval;
						}else {
							if(board[idy][idx].isRed == rWin) winningScore+=10;
							else losingScore-=10;
						}
					}
					
				}
			}
			int rscore = winningScore;// Setting the red player score and the black player score based on who has won.
			int bscore = losingScore;
			if(!rWin) {
				rscore = losingScore;
				bscore = winningScore;
			}
			String td = "Red : "+rscore+"\nBlack : "+bscore+"\n";// Setting the text to be displayed in the dialog box.
			String ta = "Black lost by getting stuck";	// The text indicating which player has lost.
			if(retturn == 1) ta = "Red lost by getting stuck";
			
			if(winningScore<losingScore) {
				if(rWin) ta = "Red lost due to less score";
				else ta = "Black lost due to less score";
			}
			td+=ta;	
			if(!getStuck.p.aiVsAi) {// If a game is finished then the score and the results are displayed, if not the end result of the simulation is displayed.
				Object[] options = {"Ok", "Restart"};
				int result = JOptionPane.showOptionDialog(getStuck.f, td, "", JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, options, options[options.length-1]); //Displaying the dialog box and getting the button pressed.
				if(result == 1) {				// If the restart button is pressed then the game restarts.
            		getStuck.restart();
            	}else System.exit(0); //If any other button is pressed then the game ends.
			}else {
				getStuck.played++;// If a game in a simulation is finished, then the number of played games is incremented, and data is updates, if the simulation is going on then the data is saved and the game restarts.
				if(rWin) getStuck.rwins++;
				else getStuck.bwins++;
				if(getStuck.played>=getStuck.total) {				// If the played games is equal to the total number of games to be played.
					sf.save(rscore, bscore, rWin); // The data is saved.
					Object[] options = {"Ok"}; // Setting the options to be displayed in the dialog box. 
					int result = JOptionPane.showOptionDialog(getStuck.f, "Games played ; "+getStuck.played+"\nRed : "+getStuck.rwins+" Black : "+getStuck.bwins, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if(result!=999) System.exit(0);
				}else {
					sf.save(rscore, bscore, rWin);	// The data is saved and the game restarts.
					getStuck.restart();
				}
			}
		}
		
		return retturn;
	}
	
	/**
	 * This method returns an array list of all the valid moves that can be made by a player.
	 * @param p The player
	 * @param emptySlot The empty slot in the board
	 * @param board The board
	 * @return An array list of all the valid moves the given player can make.
	 */
	public ArrayList<Point> getAvailableMoves(int p, Point emptySlot, card[][] board){
		boolean red = false; // Boolean whether the player is red or not.
		if(p == 1) red = true;
		ArrayList<Point> ret = new ArrayList<>(); // The array list to return.
		// Checking any moves to the left of the player.
		int cx = emptySlot.x+1;
		while(cx>=0 && cx<board[0].length) {
			// If the slot has a card, and belongs to the player and is not flipped and is a valid move, then it is added.
			if(board[emptySlot.y][cx]!=null && board[emptySlot.y][cx].isRed==red && !board[emptySlot.y][cx].flipped && isValid(new Point(cx, emptySlot.y), emptySlot, board)) 
				ret.add(new Point(cx, emptySlot.y));
			cx++;
		}
		// Same thing is done for the other 3 directions.
		int cx2 = emptySlot.x-1;
		while(cx2>=0 && cx2<board[0].length) {
			if(board[emptySlot.y][cx2]!=null && board[emptySlot.y][cx2].isRed==red && !board[emptySlot.y][cx2].flipped && isValid(new Point(cx2, emptySlot.y), emptySlot, board)) ret.add(new Point(cx2, emptySlot.y));
			cx2--;
		}
		int cy = emptySlot.y+1;
		while(cy>=0 && cy<board.length) {
			if(board[cy][emptySlot.x]!=null && board[cy][emptySlot.x].isRed==red && !board[cy][emptySlot.x].flipped && isValid(new Point(emptySlot.x, cy), emptySlot, board)) ret.add(new Point(emptySlot.x, cy));
			cy++;
		}
		int cy2 = emptySlot.y-1;
		while(cy2>=0 && cy2<board.length) {
			if(board[cy2][emptySlot.x]!=null && board[cy2][emptySlot.x].isRed==red && !board[cy2][emptySlot.x].flipped && isValid(new Point(emptySlot.x, cy2), emptySlot, board)) ret.add(new Point(emptySlot.x, cy2));
			cy2--;
		}
		return ret;
	}
	/**
	 * This method returns a boolean storing whether a move is valid to be played on the board or not.
	 * @param m The move to be played.
	 * @param emptySlot The empty slot on the board.
	 * @param board The board.
	 * @return Boolean whether the move is valid.
	 */
	public boolean isValid(Point m, Point emptySlot, card[][] board) {
		int diffx = emptySlot.x - m.x; // The difference in the x coordinate of the empty slot and the move to check.
		int diffy = emptySlot.y - m.y; // The difference in the y coordinate of the empty slot and the move to check.
		if(m.x==emptySlot.x && m.y==emptySlot.y) return false; // The move isnt valid if the point is the empty slot.
		if(diffx==0 || diffy==0 || Math.abs(diffx)==Math.abs(diffy)) {	// If the point is vertically upward or downward or horizontally right or left to the empty slot
			if(board[m.y][m.x].val>10) return true; // If the face value of the card at that point is greater than 10, then it can jump above the flipped cards, so returns true.
			else {	// If the value of the card is less than 10, then the cards between the card to be moved and the empty slot are checked, if any of them are flipped, the method returns false
				if(diffy==0) {	// Checking horizontally.
					int cx = m.x;
					if(diffx>0)cx++;
					if(diffx<0)cx--;
					while(cx>=0 && cx<board[0].length) {
						if(cx==emptySlot.x) break;
						else if(board[m.y][cx].flipped) {
							return false;
						}
						if(diffx>0)cx++;
						if(diffx<0)cx--;
					}
					return true;
				}
				if(diffx==0) {		// Checking vertically.
					int cy = m.y;
					if(diffy>0)cy++;
					if(diffy<0)cy--;
					while(cy>=0 && cy<board.length) {
						if(cy==emptySlot.y) break;
						else if(board[cy][m.x].flipped) return false;
						if(diffy>0)cy++;
						if(diffy<0)cy--;
					}
					return true;
				}
				if(Math.abs(diffx)==Math.abs(diffy)) {	// Checking if the point is diagonal to the empty slot, if it is and if there is no flipped card between, then the method returns true..
					int y = m.y;
					int x = m.x;
					y+=Math.abs(diffy)/diffy;
					x+=Math.abs(diffx)/diffx;
					while(x>=0 && y>=0 && x<board[0].length && y<board.length) {
						if(x==emptySlot.x && y==emptySlot.y) break;
						else if(board[y][x].flipped) return false;
						y+=Math.abs(diffy)/diffy;
						x+=Math.abs(diffx)/diffx;
					}
					return true;
				}
			}
		}
		return false;	// Else the method returns false.
	}
	/**
	 * This method adds diagonal moves to a given array list of moves.
	 * @param a The array list of moves.
	 * @param t The player.
	 * @param emptySlot The empty slot.
	 * @param board The board.
	 */
	public void addDiag(ArrayList<Point> a, int t, Point emptySlot, card[][] board) {	// This method works exactly the same way as the getAvailableMoves method, just instead of checking left or right or up or down, it checks diagonally.
		boolean red = false;
		if(t == 1) red = true;
		ArrayList<Point> ret = new ArrayList<>();
		int cx = emptySlot.x+1;
		int y = emptySlot.y+1;
		while(cx>=0 && cx<board[0].length && y>=0 && y<board.length) {
			if(board[y][cx].isRed==red && !board[y][cx].flipped && isValid(new Point(cx, y), emptySlot, board)) ret.add(new Point(cx, y));
			cx++;
			y++;
		}
		int cx2 = emptySlot.x-1;
		int y2 = emptySlot.y+1;
		while(cx2>=0 && cx2<board[0].length && y2>=0 && y2<board.length) {
			if(board[y2][cx2].isRed==red && !board[y2][cx2].flipped && isValid(new Point(cx2, y2), emptySlot, board)) ret.add(new Point(cx2, y2));
			cx2--;
			y2++;
		}
		int cy = emptySlot.y-1;
		int x = emptySlot.x+1;
		while(cy>=0 && cy<board.length && x>=0 && x<board[0].length) {
			if(board[cy][x].isRed==red && !board[cy][x].flipped && isValid(new Point(x, cy), emptySlot, board)) ret.add(new Point(x, cy));
			cy--;
			x++;
		}		
		int cy2 = emptySlot.y-1;
		int x2 = emptySlot.x-1;
		while(cy2>=0 && cy2<board.length && x2>=0 && x2<board[0].length) {
			if(board[cy2][x2].isRed==red && !board[cy2][x2].flipped && isValid(new Point(x2, cy2), emptySlot, board)) ret.add(new Point(x2, cy2));
			cy2--;
			x2--;
		}	
		a.addAll(ret);
	}
	/**
	 * This method checks if the game is over and which player has won.
	 * @return The player which has won or no one has won. (1 is returned if red player has won, -1 is returned if black player has won, 0 is returned if no player has won.)
	 */
	public int checkGameOver(Point emptySlot, card[][] board) {
		ArrayList<Point> moves = getAvailableMoves(1, emptySlot, board); // getting the available moves for the red player.
		if(moves.size()==0) addDiag(moves, 1, emptySlot, board); // If there are no vertical moves for the player, the diagonal ones are added.	
		ArrayList<Point> bmoves = getAvailableMoves(2, emptySlot, board); // Getting the available moves for the black player.
		if(bmoves.size()==0) addDiag(bmoves, 2, emptySlot, board); // If there are no vertical moves for the player, the diagonal ones are added.	
		if(moves.size()==0) return -1; // If the red player cannot make any move, -1 is returned.
		else if(bmoves.size()==0) return 1; // If the black player cannot make any move, 1 is returned.
		else return 0; // If both the players can make a move, then the game is still going, so 0 is returned.
	}
	
}
