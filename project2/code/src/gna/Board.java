package gna;

import java.util.Stack;

import libpract.PriorityFunc;

public class Board {

	private final int[][] tiles;
	private final int N;

	// construct a board from an N-by-N array of tiles
	public Board(int[][] tiles) {
		this.tiles = tiles;
		this.N = tiles.length;
	}

	// return number of blocks out of place
	public int hamming() {
		int sum = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (tiles[i][j] != i * N + j + 1 && tiles[i][j] != 0)
					sum++;
		return sum;
	}

	// return sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int sum = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (tiles[i][j] != 0) {
					int targetX = (tiles[i][j] - 1) / N;
					int targetY = (tiles[i][j] - 1) % N;
					sum += Math.abs(i - targetX) + Math.abs(j - targetY);
				}
		return sum;
	}

	// does this board position equal y
	public boolean equals(Object y) {
		if (this == y)
			return true;
		if (y == null)
			return false;
		if (this.getClass() != y.getClass())
			return false;

		Board object = (Board) y;
		if (N != object.tiles.length)
			return false;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (this.tiles[i][j] != object.tiles[i][j])
					return false;
		return true;
	}

	// return an Iterable of all neighboring board positions
	public Iterable<Board> neighbors() {
		Stack<Board> stack = new Stack<Board>();
		int row = 0, col = 0;
		zerofinder: for (row = 0; row < N; row++)
			for (col = 0; col < N; col++)
				if (tiles[row][col] == 0)
					break zerofinder;

		for (int j = -1; j < 2; j += 2) {
			if (col == 0 && j == -1)
				continue;
			if ((col + 1) >= N && j == +1)
				continue;
			int[][] newTiles = new int[N][N];
			for (int i = 0; i < N; i++)
				newTiles[i] = tiles[i].clone();
			newTiles[row][col] = newTiles[row][col + j];
			newTiles[row][col + j] = 0;
			Board newBoard = new Board(newTiles);
			stack.push(newBoard);
		}
		for (int i = -1; i < 2; i += 2) {
			if (row == 0 && i == -1)
				continue;
			if ((row + 1) >= N && i == +1)
				continue;
			int[][] newTiles = new int[N][N];
			for (int j = 0; j < N; j++)
				newTiles[j] = tiles[j].clone();
			newTiles[row][col] = newTiles[row + i][col];
			newTiles[row + i][col] = 0;
			Board newBoard = new Board(newTiles);
			stack.push(newBoard);
		}
		return stack;
	}

	// return a string representation of the board
	public String toString() {
		String boardRepresentation = "";
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				boardRepresentation += tiles[i][j] + " ";
			boardRepresentation += "\n";
		}
		return boardRepresentation;
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		int[] tempTiles = new int[N * N - 1];
		int index = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (tiles[i][j] != 0) {
					tempTiles[index] = tiles[i][j];
					index++;
				}

		int inversions = 0;
		for (int i = 0; i < tempTiles.length; i++)
			for (int j = i; j < tempTiles.length; j++)
				if (tempTiles[i] > tempTiles[j])
					inversions++;

		return (inversions % 2 == 0);
	}

	// is the given board solved?
	public boolean isSolution() {
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (tiles[i][j] != 0 && tiles[i][j] != (i * N + j + 1))
					return false;
		return true;
	}
}
