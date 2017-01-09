package gna;

import static org.junit.Assert.*;
import libpract.Stitch;
import libpract.Position;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.HashMap;
import java.util.Stack;

public class StitcherTest {
	
	// 5x5 with a diagonal black line, image a with a red background, image b with a blue background
	private static int[][] img1a, img1b; 
	// 5x5 with a fixed black path, image a with a red background, image b with a blue background
	private static int[][] img2a, img2b;
	// 5x5 with a diagonal seam. Except for the seam; all other values are equal to Stitch.EMPTY.
	private static Stitch[][] mask;
	private static Stitcher stitcher = new Stitcher();
	
	@Before
	public void setupMutableFixture() {
		mask = new Stitch[][]
				{{Stitch.SEAM, Stitch.EMPTY,  Stitch.EMPTY, Stitch.EMPTY, Stitch.EMPTY},
				{Stitch.EMPTY, Stitch.SEAM, Stitch.EMPTY, Stitch.EMPTY, Stitch.EMPTY},
				{Stitch.EMPTY, Stitch.EMPTY, Stitch.SEAM, Stitch.EMPTY, Stitch.EMPTY},
				{Stitch.EMPTY, Stitch.EMPTY, Stitch.EMPTY, Stitch.SEAM, Stitch.EMPTY},
				{Stitch.EMPTY, Stitch.EMPTY, Stitch.EMPTY, Stitch.EMPTY, Stitch.SEAM}};
	}
	
	@BeforeClass
	public static void setupImmutableFixture() {
		img1a = new int[][]
		{{-16776704, -196602, -196606, -392448, -262142},
		{-131070, -16776185, -196608, -65528, -65526},
		{-131065, -458240, -16383487, -196352, -65024},
		{-457472, -65020, -196096, -16776704, -391672},
		{-458494, -65532, -65277, -391672, -16776704}};		
		img1b = new int[][]
		{{-16776448, -16645890, -16776964, -16383239, -16645895},
		{-16645890, -16775936, -16580102, -16645633, -16776963},
		{-16776966, -16580353, -16776956, -16775940, -16383745},
		{-16383493, -16645633, -16775938, -16580608, -16121610},
		{-16645892, -16776963, -16383745, -16121610, -16580608}};		
		img2a = new int[][]
		{{-16777206, -327680, -65532, -196608, -325888, -327678, -64512, -65528, -131072, -131072},
		{-196608, -16579576, -64512, -720637, -262144, -65524, -131063, -524288, -131072, -131072},
		{-588543, -196604, -16777212, -16775672, -16776704, -16776178, -16515072, -65530, -131072, -131072},
		{-131070, -130304, -326400, -196608, -130304, -130816, -130816, -16776960, -131072, -131072},
		{-65024, -65525, -196595, -391934, -65024, -130816, -326908, -16711161, -131072, -131072},
		{-196607, -130816, -261888, -64249, -524288, -327421, -16449527, -524288, -131072, -131072},
		{-458752, -65525, -196607, -262144, -63232, -16711678, -64768, -65024, -131072, -131072},
		{-457472, -65024, -65024, -196607, -916730, -16318203, -196608, -196608, -131072, -131072},
		{-196352, -65024, -196606, -65024, -131072, -131072, -16776704, -16776960, -16776960, -131070},
		{-327675, -196607, -65022, -327680, -65277, -196352, -196606, -65280, -131070, -16776960}};
		img2b = new int[][]
		{{-16776448, -16514821, -16383745, -16776966, -16776457, -16776450, -16775681, -16776964, -16776962, -16776962},
		{-16711169, -16776960, -16775939, -16317961, -16383745, -16776963, -16121604, -16645123, -16776962, -16776962},
		{-16710668, -16383747, -16777216, -16580608, -16515072, -16776192, -16711674, -16449285, -16776962, -16776962},
		{-16776964, -16775425, -16775942, -16776963, -16776964, -16317953, -16449284, -16775936, -16776962, -16776962},
		{-16513793, -16776965, -16776963, -16645384, -16776964, -16121601, -16776969, -16775936, -16776962, -16776962},
		{-16776966, -16711169, -16579329, -16776968, -16317698, -16776967, -16775931, -16776968, -16776962, -16776962},
		{-16448514, -16645633, -16776962, -16776965, -16645890, -16776188, -16776964, -16580355, -16776962, -16776962},
		{-15990537, -16775425, -16776707, -16579585, -16187157, -16775417, -16383745, -16711426, -16776962, -16776962},
		{-16776451, -16318211, -16776193, -16776964, -16579841, -16776963, -16711424, -16515072, -16777216, -16711169},
		{-16710916, -16711169, -16776963, -16645122, -16776963, -16776708, -16776962, -16776450, -16711169, -16777216}};
	}
	
	/**
	 * Tests if the positions corresponds with the positions marked as a black diagonal seam from img1a and img1b.
	 * These images were made in Photoshop, then converted into an int[][] by Util.readImage().
	 */
	@Test
	public void diagonal_seam(){
		HashMap<Integer, Position> values = new HashMap<Integer, Position>();
		Position pos;
		for (int i = 0; i < img1a.length; i++) {
			pos = new Position(i, i);
			values.put(pos.hashCode(), pos);
		}
		
		for (Position tmpPos : stitcher.seam(img1a, img1b)) {
			if (values.containsKey(tmpPos.hashCode()) && values.get(tmpPos.hashCode()).equals(tmpPos))
				values.remove(tmpPos.hashCode());
		}
		assertTrue(values.size() == 0);
	}
	
	/**
	 * Tests if the positions corresponds with the positions marked as a black seam from img2a and img2b.
	 * These images were made in Photoshop, then converted into an int[][] by Util.readImage().
	 */
	@Test
	public void fixedPath_seam(){
		HashMap<Integer, Position> values = new HashMap<Integer, Position>();
		
		Stack<Position> positions = new Stack<Position>();
		positions.add(new Position(0,0));
		positions.add(new Position(1,1));
		positions.add(new Position(2,2));
		positions.add(new Position(3,2));
		positions.add(new Position(4,2));
		positions.add(new Position(5,2));
		positions.add(new Position(6,2));
		positions.add(new Position(7,3));
		positions.add(new Position(7,4));
		positions.add(new Position(6,5));
		positions.add(new Position(5,6));
		positions.add(new Position(5,7));
		positions.add(new Position(6,8));
		positions.add(new Position(7,8));
		positions.add(new Position(8,8));
		positions.add(new Position(9,9));
		
		for (Position pos : positions)
			values.put(pos.hashCode(), pos);		
		
		for (Position tmpPos : stitcher.seam(img2a, img2b)) {
			if (values.containsKey(tmpPos.hashCode()) && values.get(tmpPos.hashCode()).equals(tmpPos))
				values.remove(tmpPos.hashCode());
		}
		assertTrue(values.size() == 0);
	}
	
	@Test
	public void stitch_diagonalSeam(){
		Stitch[][] result = stitcher.stitch(img1a, img1b);
		
		int y = img1a.length - 2;
		int x = img1a[0].length - 2;
		
		for (int i = 0; i < img1a.length; i++)
			assertTrue(result[i][i] == Stitch.SEAM);
		
		for (int i = 0; i < y--; i++)
			for (int j = 0; j < x--; j++)
				assertTrue(result[4 - i][0 + j] == Stitch.IMAGE1);
		
		y = img1a.length - 2;
		x = img1a[0].length - 2;
		
		for (int i = 0; i < y--; i++)
			for (int j = 0; j < x--; j++)
				assertTrue(result[0 + i][4 - j] == Stitch.IMAGE2);
	}
	
	@Test
	public void testFloodfill_diagonalSeam(){
		stitcher.floodfill(mask);
		
		int y = img1a.length - 2;
		int x = img1a[0].length - 2;
		
		for (int i = 0; i < img1a.length; i++)
			assertTrue(mask[i][i] == Stitch.SEAM);
		
		for (int i = 0; i < y--; i++)
			for (int j = 0; j < x--; j++)
				assertTrue(mask[4 - i][0 + j] == Stitch.IMAGE1);
		
		y = img1a.length - 2;
		x = img1a[0].length - 2;
		
		for (int i = 0; i < y--; i++)
			for (int j = 0; j < x--; j++)
				assertTrue(mask[0 + i][4 - j] == Stitch.IMAGE2);
	}	
	
	@Test
	public void makesDiagonalNeighbor(){
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++) {
				if (i == 0 || j == 0)
					assertFalse(stitcher.makesDiagonalNeighbor(i, j));
				else
					assertTrue(stitcher.makesDiagonalNeighbor(i, j));
			}
	}
	
	@Test
	public void linearizedValue_setSize(){
		stitcher.setSize(img1a);
		assertTrue(stitcher.getLinearizedValue(0, 0) == 0);
		assertTrue(stitcher.getLinearizedValue(2, 3) == 17);
		stitcher.setSize(mask);
		assertTrue(stitcher.getLinearizedValue(0, 0) == 0);
		assertTrue(stitcher.getLinearizedValue(2, 3) == 17);
	}
	
	@Test
	public void isValidPosition() {
		stitcher.setSize(img1a);
		assertTrue(stitcher.isValidPosition(new Position(0,0)));
		assertTrue(stitcher.isValidPosition(new Position(4,4)));
		assertFalse(stitcher.isValidPosition(new Position(5,5)));
		assertFalse(stitcher.isValidPosition(new Position(-1,3)));
	}
	
	@Test
	public void getNeighbors() {
		stitcher.setSize(img1a);
		HashMap<Integer, Position> withDiagonals = new HashMap<Integer, Position>();
		Position pos;
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				pos = new Position(2 + j, 2 + i);
				withDiagonals.put(pos.hashCode(), pos);
			}
				
		HashMap<Integer, Position>  withoutDiagonals = new HashMap<Integer, Position>();
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
				if (!stitcher.makesDiagonalNeighbor(i, j) && !(i == 0 && j == 0)) {
					pos = new Position(2 + j, 2 + i);
					withoutDiagonals.put(pos.hashCode(), pos);
				}

		for (Position tmpPos : stitcher.getNeighbors(new Position(2, 2), true))
			if (withDiagonals.containsKey(tmpPos.hashCode()) && withDiagonals.get(tmpPos.hashCode()).equals(tmpPos))
				withDiagonals.remove(tmpPos.hashCode());
		for (Position tmpPos : stitcher.getNeighbors(new Position(2, 2), false))
			if (withoutDiagonals.containsKey(tmpPos.hashCode()) && withoutDiagonals.get(tmpPos.hashCode()).equals(tmpPos))
				withoutDiagonals.remove(tmpPos.hashCode());

		assertEquals(withDiagonals.size(), 0, 0);
		assertEquals(withoutDiagonals.size(), 0, 0);
		
	}
}
