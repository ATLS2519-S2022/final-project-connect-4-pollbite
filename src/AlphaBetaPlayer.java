/* *****************************************************************************
 *              ALL STUDENTS COMPLETE THESE SECTIONS
 * Title:            AlphaBetaPlayer
 * Files:            AlphaBetaPlayer.java
 * 					 
 * Semester:         Spring 2022
 * 
 * Author:           Lily Battin
 * 					 liba8321@colorado.edu
 * 
 * Description:		 A Connect-4 player that uses a minimax algorithm with 
 * 					 alpha beta pruning in order to make its next move
 * 
 * Written:       	 4-22-22
 * 
 * Credits:          Roujia Sun
 **************************************************************************** */

/**
 * A minimax algorithm with alpha beta pruning that has its depth limited by a
 * time limiting arbitrator
 * 
 * @author Lily Battin
 *
 */
public class AlphaBetaPlayer implements Player {

	int id;
	int cols;
	int opponent_id;

	@Override
	public String name() {
		return "Giratina";
	}

	@Override
	public void init(int id, int msecPerMove, int rows, int cols) {
		this.id = id; // id is your player's id, opponents id is 3-id
		this.cols = cols;
		this.opponent_id = 3 - id;
	}

	@Override
	public void calcMove(

			Connect4Board board, int oppMoveCol, Arbitrator arb)

			throws TimeUpException {
		// Make sure there is room to make a move.
		if (board.isFull()) {
			throw new Error("Complaint: The board is full!");
		}

		// the maximum depth of the search
		int maxDepth = 1;

		// array that holds the calculated scores
		int[] scoreHolder = new int[cols];

		// holder for max score
		int maxScore = -100;

		// holder for max score column, will give the first column by default
		int maxScoreCol = 1;

		while (!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
			// run the first level of the alphabeta search and set move to the column
			// corresponding to the best score
			for (int i = 0; i < cols; i++) {

				if (board.isValidMove(i) == true) {
					board.move(i, id); // temporarily moves alphabeta player

					// calculated score for opponent
					int alphabetaScore = alphabeta(board, maxDepth, -1000, 1000, false, arb);

					scoreHolder[i] = alphabetaScore;

					board.unmove(i, id);
				} else {
					scoreHolder[i] = -101;
				}
			}

			// looks for the biggest number in the array
			for (int i = 0; i < scoreHolder.length; i++) {
				if (scoreHolder[i] > maxScore) {
					maxScore = scoreHolder[i];
					maxScoreCol = i;
				}
			}
			arb.setMove(maxScoreCol);
		}
	}

	public int alphabeta(Connect4Board board, int depth, int a, int b, boolean isMaximizing, Arbitrator arb) {

		if (depth == 0 || board.isFull() || arb.isTimeUp()) {
			return calcScore(board, id) - calcScore(board, opponent_id);
		}

		if (isMaximizing == true) {
			int bestScore = -1000;

			for (int i = 0; i < cols; i++) {
				bestScore = Math.max(bestScore, alphabeta(board, depth - 1, a, b, false, arb));
				a = Math.max(a, bestScore);

				// prunes the branch if value a is greater than b
				if (a >= b) {
					break;
				}
			}
			return bestScore;
		}

		else {
			int bestScore = 1000;
			for (int i = 0; i < cols; i++) {
				bestScore = Math.min(bestScore, alphabeta(board, depth - 1, a, b, true, arb));
				b = Math.min(b, bestScore);

				// prunes the branch is a is greater than b
				if (a >= b) {
					break;
				}
			}
			return bestScore;
		}
	}

	public int calcScore(Connect4Board board, int id) {
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id)
					continue;
				if (board.get(r, c + 1) != id)
					continue;
				if (board.get(r, c + 2) != id)
					continue;
				if (board.get(r, c + 3) != id)
					continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id)
					continue;
				if (board.get(r + 1, c) != id)
					continue;
				if (board.get(r + 2, c) != id)
					continue;
				if (board.get(r + 3, c) != id)
					continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id)
					continue;
				if (board.get(r + 1, c + 1) != id)
					continue;
				if (board.get(r + 2, c + 2) != id)
					continue;
				if (board.get(r + 3, c + 3) != id)
					continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id)
					continue;
				if (board.get(r - 1, c + 1) != id)
					continue;
				if (board.get(r - 2, c + 2) != id)
					continue;
				if (board.get(r - 3, c + 3) != id)
					continue;
				score++;
			}
		}
		return score;
	}
}
