package gna;

/**
 * Performs sort by using the Selection Sort algorithm.
 * 
 * @author Mathias Van Herreweghe
 */
public class QuickSort implements libpract.SortingAlgorithm {
	/**
	 * Sorts the given array using quick sort.
	 * 
	 * @return The number of comparisons (i.e. calls to compareTo) performed by
	 *         the algorithm.
	 */
	public <T extends Comparable<T>> int sort(T[] array) {
		counter = 0;
		if (array == null)
			return counter;
		return sort(array, 0, array.length - 1);
	}

	/**
	 * Sorts the given array using selection sort.
	 * 
	 * @return The number of comparisons (i.e. calls to compareTo) performed by
	 *         the algorithm.
	 */
	public <T extends Comparable<T>> int sort(T[] array, int low, int high) {
		if (low < high) {
			int pivot = partition(array, low, high);
			sort(array, low, pivot - 1);
			sort(array, pivot + 1, high);
		}
		return counter;
	}

	/**
	 * partition the subarray array[low...high] so that 
	 * array[low...j-1] <= a[j] <= a[j+1..high] and return the index j.
	 */
	public static <T extends Comparable<T>> int partition(T[] array, int low,
			int high) {

		T pivot = array[low];
		int i = low, j = high + 1;

		while (true) {
			while (less(array[++i], pivot) && !(i == high));
			while (less(pivot, array[--j]) && !(j == low));

			if (j <= i)
				break;
			swap(array, i, j);
		}
		swap(array, low, j);
		return j;
	}

	/**
	 * Compares two given objects whether the first parameter is less than the
	 * second parameter.
	 * 
	 * @param v
	 *            The first object to compare.
	 * @param w
	 *            The second object to compare.
	 * @return True if and only if v is strict less than w.
	 */
	private static <T extends Comparable<T>> boolean less(T v, T w) {
		counter++;
		return v.compareTo(w) < 0;
	}

	/**
	 * Swaps two elements in the given array.
	 * 
	 * @param array
	 *            The array to perform the swap in.
	 * @param i
	 *            The index of the first element to be swapped.
	 * @param j
	 *            The index of the second element to be swapped.
	 */
	private static <T extends Comparable<T>> void swap(T[] array, int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	/**
	 * Constructor.
	 */
	public QuickSort() {
	}

	/**
	 * Variable registering the amount of compares.
	 */
	private static int counter;
}
