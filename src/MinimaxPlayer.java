/* *****************************************************************************
 * Title:            MinimaxPlayer
 * Files:            MinimaxPlayer.java
 * 					 
 * Semester:         Spring 2022
 * 
 * Author:           Lily Battin
 * 					 liba8321@colorado.edu
 * 
 * Description:		 A Connect-4 player that makes moves based on a minimax 
 * 					 algorithm 
 * 
 * Written:       	 4-22-22
 * 
 * Credits:          Roujia Sun
 **************************************************************************** */

/**
 * A mimimax algorithm that has its depth limited by a time-limiting arbitrator
 * 
 * @author Lily Battin
 *
 */
public class MinimaxPlayer implements Player {

	int id; // player id
	int cols; // number of columns in board
	int opponent_id; // opponent id

	@Override
	public String name() {
		return "Dialga";
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
			// run the first level of the minimax search and set move to the column
			// corresponding to the best score
			for (int i = 0; i < cols; i++) {

				if (board.isValidMove(i) == true) {
					board.move(i, id); // temporarily moves minimax player

					// calculated score for opponent
					int minimaxScore = minimax(board, maxDepth, false, arb);

					scoreHolder[i] = minimaxScore;

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

	public int minimax(Connect4Board board, int depth, boolean isMaximizing, Arbitrator arb) {

		// if the depth = 0 or no more moves or time is up
		if (depth == 0 || board.isFull() || arb.isTimeUp()) {
			return calcScore(board, id) - calcScore(board, opponent_id);
		}

		// returns best max score
		if (isMaximizing == true) {
			int bestScore = -1000;

			for (int i = 0; i < cols; i++) {
				if (board.isValidMove(i) == true) {
					board.move(i, id);
					bestScore = Math.max(bestScore, minimax(board, depth - 1, false, arb));
					board.unmove(i, id);
				}
			}
			return bestScore;
		}
		// returns best min score
		else {
			int bestScore = 1000;
			for (int i = 0; i < cols; i++) {
				if (board.isValidMove(i) == true) {
					board.move(i, opponent_id);
					bestScore = Math.min(bestScore, minimax(board, depth - 1, true, arb));
					board.unmove(i, opponent_id);
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
