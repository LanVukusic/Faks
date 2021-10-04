import java.util.Iterator;

/**
 * mmergesort
 */
public class Mergesort {
    public static void main(String[] args) {
        // set data
        int arr[] = { 3, 1, 4, 1, 5, 9, 2, 6 };

        // sort
        int out[] = mergesort(0, arr.length - 1, arr);

        // print result
        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }
    }

    public static int[] mergesort(int left, int right, int[] table) {
        // 1 big arrays are sorted
        if (right - left == 0) {
            return new int[] { table[left] };
        }

        // bigger arrays need a bit more love

        // first we find pivot point, where we divide arrays in two "same" sized
        // segments
        int pivotIndex = (left + right) / 2;

        int[] out = new int[right - left + 1];
        // we recurse over two segments
        int[] leftArr = mergesort(left, pivotIndex, table);
        int[] rightArr = mergesort(pivotIndex + 1, right, table);

        int i = 0, j = 0, o = 0;
        // j- RIGHT, i- LEFT
        while (o < out.length) {
            // check that we dont run out of either array
            if (i < leftArr.length && (j == rightArr.length || rightArr[j] > leftArr[i])) {
                out[o++] = leftArr[i++];
            } else {
                out[o++] = rightArr[j++];
            }
        }

        return out;

    }
}
