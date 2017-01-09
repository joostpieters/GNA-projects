package gna;

import libpract.StdIn;
import libpract.PriorityFunc;

class Main
{
	public static void main( String[] args )
	{
		int N = StdIn.readInt();
		int[][] tiles = new int[N][N];
		
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				tiles[i][j] = StdIn.readInt();
		
		Board initial = new Board(tiles);
		if (!initial.isSolvable())
		{
			System.out.println("No solution possible");
		}
		else
		{
			Solver solver = new Solver(initial, PriorityFunc.MANHATTAN);
	
			for (Board board : solver.solution())
				System.out.println(board);

			System.out.println("Minimum number of moves = " + solver.moves());
		}
	}
}


