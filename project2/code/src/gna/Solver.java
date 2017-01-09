package gna;

import libpract.PriorityFunc;

import java.util.Comparator;
import java.util.Stack;

public class Solver {

	private class State {
		private Board board;
		private int moves;
		private State prev;

		private State(State another) {
			this.board = another.board;
			this.moves = another.moves;
			this.prev = another.prev;
		}

		private State() {
			this.board = null;
			this.moves = 0;
			this.prev = null;
		}
	}

	class StateComparator implements Comparator<State> {
		private PriorityFunc priority;

		public StateComparator(PriorityFunc priority) {
			super();
			this.priority = priority;
		}

		public int compare(State o1, State o2) {
			if (this.priority == PriorityFunc.HAMMING)
				return ((o1.board.hamming() + o1.moves) - (o2.board.hamming() + o2.moves));
			else if (this.priority == PriorityFunc.MANHATTAN)
				return ((o1.board.manhattan() + o1.moves) - (o2.board.manhattan() + o2.moves));
			else
				throw new IllegalArgumentException(
						"Priority function not supported");
		}
	}

	State solution = new State();
	Stack<Board> solutionStack = new Stack<Board>();

	/**
	 * Finds a solution to the initial board.
	 * 
	 * @param priority
	 *            is either PriorityFunc.HAMMING or PriorityFunc.MANHATTAN
	 */
	public Solver(Board initial, PriorityFunc priority) {
		boolean found = false;
		State state = new State();
		state.board = initial;
		state.moves = 0;
		state.prev = null;
		State tmpMin;
		
		if (state.board.isSolution()) {
			solution = state;
			found = true;
		}

		// Use the given priority function (either PriorityFunc.HAMMING
		// or PriorityFunc.MANHATTAN) to solve the puzzle.
		// -->	unnecessary to use an if-else condition because of the StateComparator but I tought
		// 		I'd better leave things as they were in the original file.
		if (priority == PriorityFunc.HAMMING) {
			Comparator<State> comparator = new StateComparator(priority);
			MinPQ<State> pq = new MinPQ<State>(10, comparator);
			pq.insert(state);
			while (!found) {
				tmpMin = pq.delMin();
				if (tmpMin.board.isSolution()){
					solution = tmpMin;
					found = true;
					break;
				}
				final Iterable<Board> neighbors = tmpMin.board.neighbors();
				for (Board neighbor : neighbors) {
					if (tmpMin.prev == null
							|| !neighbor.equals(tmpMin.prev.board)) {
						state = new State();
						state.board = neighbor;
						state.moves = tmpMin.moves + 1;
						state.prev = tmpMin;

						pq.insert(state);
					}
				}
			}
		} else if (priority == PriorityFunc.MANHATTAN) {
			Comparator<State> comparator = new StateComparator(priority);
			MinPQ<State> pq = new MinPQ<State>(10, comparator);
			pq.insert(state);
			while (!found) {
				tmpMin = pq.delMin();
				if (tmpMin.board.isSolution()){
					solution = tmpMin;
					found = true;
					break;
				}
				final Iterable<Board> neighbors = tmpMin.board.neighbors();
				for (Board neighbor : neighbors) {
					if (tmpMin.prev == null
							|| !neighbor.equals(tmpMin.prev.board)) {
						state = new State();
						state.board = neighbor;
						state.moves = tmpMin.moves + 1;
						state.prev = tmpMin;

						pq.insert(state);
					}
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Priority function not supported");
		}

		State temp = new State(solution); // using the copy constructor
		while (temp.prev != null) {
			solutionStack.add(temp.board);
			temp = temp.prev;
		}
		solutionStack.add(temp.board);
	}

	/**
	 * Returns min number of moves to solve initial board. -1 if no solution.
	 */
	public int moves() {
		return solution.moves;
	}

	/**
	 * Returns an Iterable of board positions in solution.
	 */
	public Iterable<Board> solution() {
		return solutionStack;
	}
}
