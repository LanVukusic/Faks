/**
 * quicksort
 * 
 */

public class Quicksort {

    public static void main(String[] args) {
        // set data
        int arr[] = { 3, 1, 4, 1, 5, 9, 2, 6 };

        // sort
        qsort(0, arr.length - 1, arr);

        // print result
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void qsort(int start, int end, int[] table) {
        if (start < end) {
            // get the pivot element and its index;
            int pivotIndex = end;

            // swap elements s left side is smaller than pivot and right is greater.
            int i = start, j = end;
            while (i <= j) {
                // pass all "ok" indexes where everything is in order
                while (table[i] < table[pivotIndex]) {
                    i++;
                }
                while (table[j] > table[pivotIndex]) {
                    j++;
                }

                // do the swapping
                if (i <= j) {
                    int tmp = table[i];
                    table[i] = table[j];
                    table[j] = tmp;

                    i++;
                    j--;
                }

            }

            // recurse on left and right side seperately
            qsort(start, pivotIndex - 1, table);
            qsort(pivotIndex + 1, end, table);
        }
    }
}