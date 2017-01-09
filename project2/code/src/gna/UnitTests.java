package gna;

import java.util.Random;

import libpract.PriorityFunc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A number of JUnit tests for Solver.
 * 
 * Feel free to modify these to automatically test puzzles or other
 * functionality
 */
public class UnitTests {

	Board goalBoard, unsolvableBoard, pdfExampleBoard, pdfExampleBoardCopy,
			oneMoveBoard;

	@Before
	public void setUp() throws Exception {
		int[][] goal = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 },
				{ 13, 14, 15, 0 } };
		goalBoard = new Board(goal);
		int[][] unsolvable = { { 1, 2, 3 }, { 4, 5, 6 },
				{ 8, 7, 0 } };
		unsolvableBoard = new Board(unsolvable);
		int[][] pdfExample = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
		pdfExampleBoard = new Board(pdfExample);
		pdfExampleBoardCopy = new Board(pdfExample);
		int[][] oneMove = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 },
				{ 13, 14, 0, 15 } };
		oneMoveBoard = new Board(oneMove);
	}

	@Test
	public void testIsSolution() {
		assertTrue(goalBoard.isSolution());
	}

	@Test
	public void testIsNotSolution() {
		assertFalse(pdfExampleBoard.isSolution());
	}

	@Test
	public void testIsGoal() {
		assertTrue(goalBoard.isSolution());
	}

	@Test
	public void testIsNotSolvable() {
		assertFalse(unsolvableBoard.isSolvable());
	}

	@Test
	public void testIsGoalSolvable() {
		assertTrue(goalBoard.isSolvable());
	}

	@Test
	public void testIsSolvable() {
		assertTrue(pdfExampleBoard.isSolvable());
	}

	@Test
	public void testHamming() {
		assertEquals(pdfExampleBoard.hamming(), 5);
	}

	@Test
	public void testManhattan() {
		assertEquals(pdfExampleBoard.manhattan(), 10);
	}

	@Test
	public void testEqualsNull() {
		assertFalse(pdfExampleBoard.equals(null));
	}

	@Test
	public void testEquals() {
		assertTrue(pdfExampleBoard.equals(pdfExampleBoardCopy));
		assertTrue(pdfExampleBoardCopy.equals(pdfExampleBoard));
	}

	@Test
	public void testEqualsDifferent() {
		assertFalse(goalBoard.equals(pdfExampleBoardCopy));
	}

	@Test
	public void testNeighbors() {
		boolean neighbor1 = false, neighbor2 = false, neighbor3 = false, neighbor4 = false;

		int[][] neighbor1tiles = { { 8, 1, 3 }, { 0, 4, 2 }, { 7, 6, 5 } };
		int[][] neighbor2tiles = { { 8, 1, 3 }, { 4, 2, 0 }, { 7, 6, 5 } };
		int[][] neighbor3tiles = { { 8, 0, 3 }, { 4, 1, 2 }, { 7, 6, 5 } };
		int[][] neighbor4tiles = { { 8, 1, 3 }, { 4, 6, 2 }, { 7, 0, 5 } };
		Board neighbor1board = new Board(neighbor1tiles);
		Board neighbor2board = new Board(neighbor2tiles);
		Board neighbor3board = new Board(neighbor3tiles);
		Board neighbor4board = new Board(neighbor4tiles);

		for (Board found : pdfExampleBoard.neighbors()) {
			if (found.equals(neighbor1board))
				neighbor1 = true;
			else if (found.equals(neighbor2board))
				neighbor2 = true;
			else if (found.equals(neighbor3board))
				neighbor3 = true;
			else if (found.equals(neighbor4board))
				neighbor4 = true;
		}
		assertTrue(neighbor1 && neighbor2 && neighbor3 && neighbor4);
	}

	@Test
	public void testToString() {
		assertTrue("8 1 3 \n4 0 2 \n7 6 5 \n"
				.equals(pdfExampleBoard.toString()));
	}

	@Test
	public void testSolverHamming() {
		Solver solver = new Solver(pdfExampleBoard, PriorityFunc.HAMMING);
		for (Board currBoard : solver.solution()) {
			assertTrue(currBoard.isSolution());
			break; // only tests first element of list as this should be the
					// solution.
		}
		assertEquals(solver.moves(), 14);
	}

	@Test
	public void testSolverManhattan() {
		Solver solver = new Solver(pdfExampleBoard, PriorityFunc.MANHATTAN);
		for (Board currBoard : solver.solution()) {
			assertTrue(currBoard.isSolution());
			break; // only tests first element of list as this should be the
					// solution.
		}
		assertEquals(solver.moves(), 14);
	}

	@Test
	public void testSolverHammingOneMove() {
		Solver solver = new Solver(oneMoveBoard, PriorityFunc.HAMMING);
		for (Board currBoard : solver.solution()) {
			assertTrue(currBoard.isSolution());
			break; // only tests first element of list as this should be the
					// solution.
		}
		assertEquals(solver.moves(), 1);
	}

	@Test
	public void testSolverManhattanOneMove() {
		Solver solver = new Solver(oneMoveBoard, PriorityFunc.MANHATTAN);
		for (Board currBoard : solver.solution()) {
			assertTrue(currBoard.isSolution());
			break; // only tests first element of list as this should be the
					// solution.
		}
		assertEquals(solver.moves(), 1);
	}

}
