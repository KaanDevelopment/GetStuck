package getStuck;
import java.awt.Point;
import java.util.ArrayList;
public class MLAlgorithm {
	/**
	 *  This method implements the minimax algorithm to play the game.
	 */
	moveFunctions mf = new moveFunctions(); // The moveFunctions instance to access its methods.
	/**
	 * This method gives the first call to minimax and returns the result.
	 * @param turn The turn of the ai, or the player to maximise.
	 * @return The result of the minimax.
	 */
	public Point mm(int turn, card[][] board, Point emptySlot) {
		card[][] b = new card[board.length][board[0].length];// Initializing a copy of the board to be used in computation.
		Point mptslt = new Point(emptySlot.x, emptySlot.y);
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board[0].length; j++) {
				b[i][j] = board[i][j];
			}
		}		
		ArrayList<Point> avMov = mf.getAvailableMoves(turn, emptySlot, board);	// Getting the available moves of the current player.
		if(avMov.size()==0) mf.addDiag(avMov, turn, emptySlot, board);
		ArrayList<Double> scores = new ArrayList<Double>(); // ArrayList storing the scores of all the possible moves.
		double max = -99999999; // The max score.
		for(Point p : avMov) {	// Making the move to check.
			getStuck.p.computed ++;
			b[mptslt.y][mptslt.x] = b[p.y][p.x];
			b[p.y][p.x] = null;
			b[mptslt.y][mptslt.x].flipped = true;			
			double score = minimax(b, p, false, 1, -999999, 999999, turn, b[mptslt.y][mptslt.x]); 	// Getting the score from minimax.
			scores.add(score);
			if(max<score) max = score;	// The move made is reversed.
			b[p.y][p.x] = b[mptslt.y][mptslt.x];
			b[mptslt.y][mptslt.x] = null;
			b[p.y][p.x].flipped = false;
		}
		return avMov.get(scores.indexOf(max));	// The move with the highest score is returned.
	}
	/**
	 * This method performs the minimax algorithm and returns a score, with alpha-beta pruning.
	 * @param board The board
	 * @param empty The empty slot.
	 * @param maximizing Boolean whether the player is maximizing or not.
	 * @param depth The depth of the graph.
	 * @param alpha Alpha for alpha beta pruning.
	 * @param beta Beta for alpha beta pruning.
	 * @param t The ai player.
	 * @param cardTurned The card turned in the previous turn (used in computing the score).
	 * @return
	 */
	public double minimax(card[][] board, Point empty, boolean maximizing, int depth, double alpha, double beta, int t, card cardTurned) {	
		int result = 0; // The result of whether the game is over r not, if it is who won.
		ArrayList<Point> rmoves = mf.getAvailableMoves(1, empty, board);// Getting the moves of the red player.
		if(rmoves.size() == 0) mf.addDiag(rmoves, 1, empty, board);
		ArrayList<Point> bmoves = mf.getAvailableMoves(2, empty, board);// Getting the moves of the black player.
		if(bmoves.size() == 0) mf.addDiag(bmoves, 2, empty, board);
		if(rmoves.size() == 0) result =  -1;	// Setting the result.
		else if(bmoves.size() == 0) result =  1;
		if(result==-1) {
			if(t == 2) return 9999;	// Returning the score of a winning or losing situation.
			if(t == 1) return -9999;
		}else if(result==1) {
			if(t == 2) return -9999;// Returning the score of a winning or losing situation.
			if(t == 1) return 9999;
		}
		if(depth>=getStuck.p.maxLookAhead) {
			if(t == 2) return bmoves.size() - rmoves.size() - cardTurned.val/2;		// If the depth is equal to the max depth, the score is calculated and returned.
			else return rmoves.size() - bmoves.size() - cardTurned.val/2;
		}else if(result==0) {
			if(maximizing) {	// If the game is still going on, then the graph continues.
				int turn = t; // The turn of the player.
				ArrayList<Point> avMov = rmoves;	// Setting the available moves of the player.
				if(turn == 2) avMov = bmoves;
				if(avMov.size() == 0) mf.addDiag(avMov,turn, empty, board);
				double maxscore = -999999; // The max score.
				for(Point p : avMov) {
					getStuck.p.computed ++;
					board[empty.y][empty.x] = board[p.y][p.x]; // Making the move to check.
					board[p.y][p.x] = null;
					board[empty.y][empty.x].flipped = true;
					double score = minimax(board, p, false, depth+1, alpha, beta, t, board[empty.y][empty.x]); // Getting the score, if the score is greater than the max score, it is updated.
					if(maxscore<score) maxscore = score;
					if(getStuck.p.pruning) {
						if(alpha<maxscore) alpha = maxscore;// If alpha beta pruning is enabled, it will be initiated.
						if(beta<=alpha) break;
					}
					board[p.y][p.x] = board[empty.y][empty.x];	// The made move for checking is reversed.
					board[empty.y][empty.x] = null;
					board[p.y][p.x].flipped = false;
				}
				return maxscore; // The max score is returned.
			}else {
				int turn = 1;	// Setting the turn of the current player.
				if(t == 1) turn = 2; 
				ArrayList<Point> avMov = rmoves;// getting the available moves of the current player.
				if(turn == 2) avMov = bmoves;
				if(avMov.size()==0) mf.addDiag(avMov, turn, empty, board);
				double minscore = 999999; // The minimum score.
				for(Point p : avMov) {
					getStuck.p.computed ++;
					board[empty.y][empty.x] = board[p.y][p.x];// Making the move to check.
					board[p.y][p.x] = null;
					board[empty.y][empty.x].flipped = true;
					double score = minimax(board, p, false, depth+1, alpha, beta, t, board[empty.y][empty.x]);	// Getting the score
					if(minscore>score) minscore = score;
					if(getStuck.p.pruning) {
						if(beta>minscore) beta = minscore;// If alpha beta pruning is enabled, it is intiated.
						if(beta<=alpha) break;
					}
					board[p.y][p.x] = board[empty.y][empty.x];	// The move made for checking is reversed.
					board[empty.y][empty.x] = null;
					board[p.y][p.x].flipped = false;
				}
				return minscore; // The minimum score is returned.
			}
		}
		return 0;
	}
}