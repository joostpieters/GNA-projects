package gna;

import static org.junit.Assert.*;
import java.util.Random;
import libpract.SortingAlgorithm;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for SortingAlgorithms.
 */
public class SortingAlgorithmsTest {
	
	private Double[] array;
	private final static int SIZE = 10;

	InsertionSort insertionSorter = new InsertionSort();
	QuickSort quickSorter = new QuickSort();
	SelectionSort selectionSorter = new SelectionSort();

	@Before
	public void setUp() throws Exception {
		array = new Double[SIZE];
		Random generator = new Random();
		for (int i = 0; i < array.length; i++) {
			array[i] = generator.nextDouble();
		}
	}

	@Test
	public void testNull() {
		insertionSorter.sort(null);
		quickSorter.sort(null);
		selectionSorter.sort(null);
	}

	@Test
	public void testEmpty() {
		insertionSorter.sort(new Integer[0]);
		quickSorter.sort(new Integer[0]);
		selectionSorter.sort(new Integer[0]);
	}

	@Test
	public void testSimpleElement() {
		Integer[] test = new Integer[1];
		test[0] = 5;
		insertionSorter.sort(test);
		quickSorter.sort(test);
		selectionSorter.sort(test);
	}

	@Test
	public void testSpecial() {
		Integer[] test1 = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
		Integer[] test2 = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
		Integer[] test3 = { 5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5 };
		
		insertionSorter.sort(test1);
		assertTrue(checkValidity(test1));
		
		quickSorter.sort(test2);
		assertTrue(checkValidity(test2));
		
		selectionSorter.sort(test3);
		assertTrue(checkValidity(test3));
	}

	@Test
	public void testRandom1() {
		insertionSorter.sort(array);
		assertTrue(checkValidity(array));
	}
	
	@Test
	public void testRandom2() {
		quickSorter.sort(array);
		assertTrue(checkValidity(array));
	}
	
	@Test
	public void testRandom3() {		
		selectionSorter.sort(array);
		assertTrue(checkValidity(array));
	}

	private boolean checkValidity(Double[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i + 1].compareTo(array[i]) < 0)
				return false;
		}
		return true;
	}
	
	private boolean checkValidity(Integer[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i + 1].compareTo(array[i]) < 0)
				return false;
		}
		return true;
	}

}
