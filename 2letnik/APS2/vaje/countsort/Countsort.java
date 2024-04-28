
public class Countsort {

    public static void main(String[] args) {
        int arr[] = { 3, 1, 4, 1, 5, 9, 2, 6 };
        int out[] = countsort(arr, 9);
        // print result
        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }
    }

    private static int[] countsort(int[] arr, int max) {
        max = max + 1;
        int[] sum = new int[max];

        for (int i = 0; i < arr.length; i++) {
            sum[arr[i]]++;
        }

        for (int i = 1; i < max; i++) {
            sum[i] = sum[i] + sum[i - 1];
        }

        int out[] = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            int num = arr[i];
            out[(--sum[num])] = num;
        }
        return out;
    }

}
