
public class GreedyPlayer implements Player{
	
	private static java.util.Random rand = new java.util.Random();
	int id;
	int cols;
	int rows;
	
    @Override
    public String name() {
        return "Greedy Boi";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id; // id is your player's id, opponents id is 3-id 
    	this.cols = cols;
    	this.rows = rows;
    }

    @Override
    public void calcMove(
    		
        Connect4Board board, int oppMoveCol, Arbitrator arb) 
    
        throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
        // array that holds the calculated scores
        int[] scoreHolder = new int[cols];
        
        // holder for max score
        int maxScore = -100;
        // holder for max score column, will give the first column by default
        int maxScoreCol = 1;
        
        // find maximum score from all possible moves 
        for (int i = 1; i < cols; i++) {
        	
        	if (board.isValidMove(i) == true) {
        		board.move(i, id); // temporarily moves greedy player
        	}
        	else {
        		scoreHolder[i] = -101;
        	}
        	
        	// scores for greedy player and opponent
        	int playerScore = (int) calcScore(board, id);
        	int opponentScore = (int) calcScore(board, 3-id);
        	
        	// calculated score for opponent
        	int finalScore = opponentScore - playerScore;
        	
        	scoreHolder[i] = finalScore;
        	
        	board.unmove(i, id);
        }
        
        // looks for the biggest number in the array 
        for (int i = 0; i < scoreHolder.length; i++) {
        	if (scoreHolder[i] > maxScore) {
        		maxScore = scoreHolder[i];
        		maxScoreCol = i;
        	}
        }
        
//        do { col = rand.nextInt(board.numCols());
//        } while (!board.isValidMove(col));
        
        arb.setMove(maxScoreCol);
    }
    
    public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}
}
