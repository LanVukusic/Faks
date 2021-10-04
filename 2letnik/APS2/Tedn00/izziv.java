import java.util.Random;
import java.lang.System;
import java.util.concurrent.ThreadLocalRandom;

public class izziv {
    public static void main(String[] args) {
        System.out.printf("%-15s %-15s %-15s\n", "n", "linearno", "dvojisko");
        System.out.println("---------------------------------------------");

        for (int i = 1; i < 100; i++) {
            int n = 1000 * i;
            System.out.printf("%-15d %-15d %-15d\n", n, timeLinear(n), timeBinary(n));
        }

    }

    public static int[] generateTable(int n) {
        int[] out = new int[n - 1];
        for (int i = 0; i < out.length; i++) {
            out[i] = i;
        }
        return out;
    }

    public static int findLinear(int[] a, int v) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == v) {
                return i;
            }
        }

        return -1;
    }

    // public static int findBinary(int[] a, int l, int r, int v) {
    //     if (l == r) {
    //         return -1;
    //     }

    //     if (a[(l + r) / 2] == v) {
    //         return (l + r) / 2;
    //     } else {
    //         if (a[(l + r) / 2] > v) { // too big, lowering
    //             return (findBinary(a, l, (l + r) / 2, v));
    //         } else { // too small increasing
    //             return (findBinary(a, (l + r) / 2, r, v));
    //         }
    //     }
    // }

    public static int findBinary(int arr[], int l, int r, int x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the
            // middle itself
            if (arr[mid] == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr[mid] > x)
                return findBinary(arr, l, mid - 1, x);

            // Else the element can only be present
            // in right subarray
            return findBinary(arr, mid + 1, r, x);
        }

        // We reach here when element is not present
        // in array
        return -1;
    }

    public static long timeLinear(int n) {
        int[] table = generateTable(n);
        long columTime = 0;
        long start;

        for (int i = 0; i < 1000; i++) {
            start = System.nanoTime();

            int randomNum = ThreadLocalRandom.current().nextInt(1, n);
            findLinear(table, randomNum);

            long time = System.nanoTime() - start;
            columTime += time;
        }
        return columTime / 1000;
    }

    public static long timeBinary(int n) {
        int[] table = generateTable(n);
        long columTime = 0;
        long start;

        for (int i = 0; i < 1000; i++) {
            start = System.nanoTime();

            int randomNum = ThreadLocalRandom.current().nextInt(1, n);
            findBinary(table, 0, table.length-1, randomNum);

            long time = System.nanoTime() - start;
            columTime += time;
        }
        return columTime / 1000;
    }

}
