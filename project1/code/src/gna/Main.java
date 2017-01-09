package gna;

import java.util.Random;

public class Main {
	// TODO documenteren

	public static <T> void main(String[] args) {
		InsertionSort insertionSorter = new InsertionSort();
		QuickSort quickSorter = new QuickSort();
		SelectionSort selectionSorter = new SelectionSort();
		Double test[] = createRandomArray(100);

		int insertionSortCounts = insertionSorter.sort(test);
		int quickSortCounts = quickSorter.sort(test);
		int selectionSortCounts = selectionSorter.sort(test);
		
		System.out.println(quickSortCounts);
	}

	/**
	 * Creates and returns a array with random Doubles of size N.
	 * @param	N
	 * 			The size the array should be.
	 * @return	a array of size N, with each element being a Double
	 * 			between 0 and 1.
	 */
	protected static Double[] createRandomArray(int N) {
		Random random = new Random();
		Double[] test = new Double[N];
		for (int i = 0; i < test.length; i++) {
			test[i] = random.nextDouble();
		}
		return test;
	}

}
