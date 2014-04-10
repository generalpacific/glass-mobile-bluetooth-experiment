package com.example.bluetoothexperiment.nqueens;

/**
 * Util class for solving the N Queens problem.
 * @author prashant
 *
 */
public final class NQueensUtil {
	
	public static class Solution {
		public int solution = 0;
	}
	
	private NQueensUtil() {
		throw new IllegalStateException(
				"Cannot instantiate NQueensUtil as it is a util class");
	}

	public static int solve(int N) {
		Solution solObj = new Solution();
		int board[] = new int[N + 1]; // N+1 since need board[1]...board[N]
		place(1, board, N, solObj);
		return solObj.solution;
	}

	private static boolean safe(int row, int column, int board[]) {
		// Check whether it is safe to place a queen at row, column;
		// i.e., is board[column]=row a safe configuration?
		for (int j = 1; j < column; j++) {
			if (board[column - j] == row || board[column - j] == row - j
					|| board[column - j] == row + j) {
				return false;
			}
		}
		return true;
	}

	private static int place(int column, int board[], int N, Solution solution) {
		// Place a queen in all safe positions of column c,
		// then try placing a queen in the next column.
		// If a position in column N is safe, print the board.
		for (int row = 1; row <= N; row++) {
			board[column] = row;
			if (safe(row, column, board)) {
				if (column == N) {
					solution.solution++; // we have a solution
				}
				else {
					place(column + 1, board, N,solution); // try next column
				}
			}
			board[column] = 0; // unrecord that a queen was placed
		}
		return solution.solution;
	}
}
