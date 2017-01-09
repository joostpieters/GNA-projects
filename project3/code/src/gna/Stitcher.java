package gna;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import libpract.*;

/**
 * Implement the methods stitch, seam and floodfill.
 */
public class Stitcher {

	private int IMAGE_WIDTH, IMAGE_HEIGHT;

	/**
	 * Return the sequence of positions on the seam. The first position in the
	 * sequence is (0, 0) and the last is (width - 1, height - 1). Each position
	 * on the seam must be adjacent to its predecessor and successor (if any).
	 * Positions that are diagonally adjacent are considered adjacent.
	 * 
	 * Remark: Position (x, y) corresponds to the pixels image1[x][y] and
	 * image2[x][y].
	 * 
	 * image1 and image2 are both non-null and have equal dimensions.
	 */
	public List<Position> seam(int[][] image1, int[][] image2) {
		setSize(image1);
		DirectedEdge edge;
		EdgeWeightedDigraph diGraph = new EdgeWeightedDigraph(IMAGE_WIDTH
				* IMAGE_HEIGHT);
		Stack<Position> positions = new Stack<Position>();

		for (int i = 0; i < IMAGE_HEIGHT; i++)
			for (int j = 0; j < IMAGE_WIDTH; j++)
				for (Position pos : getNeighbors(new Position(j, i), true)) {
					edge = new DirectedEdge(getLinearizedValue(j, i),
											getLinearizedValue(pos.getX(), pos.getY()),
											ImageCompositor.pixelSqDistance(
												image1[pos.getY()][pos.getX()],
												image2[pos.getY()][pos.getX()]));
					diGraph.addEdge(edge);
				}
		DijkstraSP shortestPath = new DijkstraSP(diGraph, 0);
		Iterable<DirectedEdge> path = shortestPath.pathTo(IMAGE_WIDTH
				* IMAGE_HEIGHT - 1);
		positions.add(new Position(0, 0));
		for (DirectedEdge e : path)
			positions.add(new Position(e.to() % IMAGE_WIDTH, e.to()
					/ IMAGE_WIDTH));
		return positions;
	}

	/**
	 * Apply the floodfill algorithm described in the assignment to mask. You
	 * can assume the mask contains a seam from the upper left corner to the
	 * bottom right corner.
	 * 
	 * The seam is represented using Stitch.SEAM and all other positions contain
	 * the default value Stitch.EMPTY. So your algorithm must replace all
	 * Stitch.EMPTY values with either Stitch.IMAGE1 or Stitch.IMAGE2.
	 * 
	 * Positions left to the seam should contain Stitch.IMAGE1, and those right
	 * to the seam should contain Stitch.IMAGE2. Run `ant test` to test if your
	 * implementation does this correctly!
	 */
	/* NOTE: I had to check whether or not mask[y][x] was null or Stitch.EMPTY because 
	 * the main method uses the former and provided tests the latter.
	 * A coherent use of this matter would need less compares.
	 */
	public void floodfill(Stitch[][] mask) {
		setSize(mask);
		Stack<Position> stack = new Stack<Position>();
		stack.push(new Position(0, IMAGE_HEIGHT - 1));
		Position pos;

		while (!stack.empty()) {
			pos = stack.pop();

			if ((mask[pos.getY()][pos.getX()] != null && 
					mask[pos.getY()][pos.getX()] != Stitch.EMPTY)  ||
					!isValidPosition(pos))
				continue;

			mask[pos.getY()][pos.getX()] = Stitch.IMAGE1;
			for (Position newPos : getNeighbors(pos, false))
				if ((mask[newPos.getY()][newPos.getX()] == null ||
						mask[newPos.getY()][newPos.getX()] == Stitch.EMPTY)) {
					stack.push(newPos);
				}
		}
		
		stack.clear();
		stack.push(new Position(IMAGE_WIDTH - 1, 0));

		while (!stack.empty()) {
			pos = stack.pop();

			if ((mask[pos.getY()][pos.getX()] != null && 
					mask[pos.getY()][pos.getX()] != Stitch.EMPTY)  ||
					!isValidPosition(pos))
				continue;

			mask[pos.getY()][pos.getX()] = Stitch.IMAGE2;
			for (Position newPos : getNeighbors(pos, false))
				if (mask[newPos.getY()][newPos.getX()] == null ||
						mask[newPos.getY()][newPos.getX()] == Stitch.EMPTY)
					stack.push(newPos);						
		}
	}

	/**
	 * Return the mask to stitch two images together. The seam runs from the
	 * upper left to the lower right corner, where in general the rightmost part
	 * comes from the first image (but remember that the seam can be complex,
	 * see the spiral example in the assignment). A pixel in the mask is
	 * Stitch.IMAGE1 on the places where image1 should be used, and
	 * Stitch.IMAGE2 where image2 should be used. On the seam record a value of
	 * Stitch.SEAM.
	 * 
	 * ImageCompositor will only call this method (not seam and floodfill) to
	 * stitch two images.
	 * 
	 * image1 and image2 are both non-null and have equal dimensions.
	 */
	public Stitch[][] stitch(int[][] image1, int[][] image2) {
		Iterable<Position> seam = seam(image1, image2);
		Stitch[][] result = new Stitch[image1.length][image1[0].length];
		for (Position pos : seam)
			result[pos.getY()][pos.getX()] = Stitch.SEAM;
		floodfill(result);
		return result;
	}

	/**
	 * Auxiliary method which returns the neighbor positions of the given
	 * position.
	 */	
	public List<Position> getNeighbors(Position p, boolean inclDiagonal) {
		List<Position> neighbors = new ArrayList<Position>();
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
				if (isValidPosition(new Position(p.getX() + j, p.getY() + i))
						&& !(j == 0 && i == 0) && 
						(inclDiagonal || !makesDiagonalNeighbor(i, j)))
					neighbors.add(new Position(p.getX() + j, p.getY() + i));
		return neighbors;
	}
	
	/**
	 * Auxiliary method which checks whether or not the given indexes give a 
	 * diagonal neighbor if added to a position.
	 */
	public boolean makesDiagonalNeighbor(int i, int j) {
		return i != 0 && j != 0;
	}

	/**
	 * Auxiliary method which checks whether or not the given position is a
	 * valid position for the image parameters in seam().
	 */
	public boolean isValidPosition(Position p) {
		if (p.getX() >= 0 && p.getY() >= 0 && p.getX() < IMAGE_WIDTH
				&& p.getY() < IMAGE_HEIGHT)
			return true;
		return false;
	}

	/**
	 * Auxiliary method which returns the linearized value of given x and y
	 * value in the image matrix given in seam().
	 */
	public int getLinearizedValue(int x, int y) {
		return y * IMAGE_WIDTH + x;
	}

	/**
	 * Auxiliary method to set the size for this stitcher to work with.
	 */
	public void setSize(Stitch[][] mask) {
		if (mask.length != IMAGE_HEIGHT || mask[0].length != IMAGE_WIDTH) {
			IMAGE_HEIGHT = mask.length;
			IMAGE_WIDTH = mask[0].length;
		}
	}

	/**
	 * Auxiliary method to set the size for this stitcher to work with.
	 */
	public void setSize(int[][] mask) {
		if (mask.length != IMAGE_HEIGHT || mask[0].length != IMAGE_WIDTH) {
			IMAGE_HEIGHT = mask.length;
			IMAGE_WIDTH = mask[0].length;
		}
	}
}
