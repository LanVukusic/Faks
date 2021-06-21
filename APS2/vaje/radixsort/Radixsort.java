
public class Radixsort {
    public static void main(String[] args) {
        int arr[] = { 3, 1, 4, 1, 54, 62, 712 };
        int out[] = radixSort(arr, 10);
        // print result
        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }
    }

    private static int[] radixSort(int[] arr, int base) { // based on count
        int max = base;
        boolean empty = false;

        int dec = 1;
        while (!empty) {
            empty = true;
            int[] sum = new int[max];
            int[] out = new int[arr.length];

            for (int i = 0; i < arr.length; i++) {
                int num = arr[i] % (dec * 10);
                num = num / dec;
                if (empty && num != 0) {
                    empty = false;
                }
                sum[num]++;
            }

            for (int i = 1; i < max; i++) {
                sum[i] = sum[i] + sum[i - 1];
            }

            for (int i = arr.length - 1; i >= 0; i--) {
                int num = arr[i] % (dec * 10);
                num = num / dec;
                out[(--sum[num])] = arr[i];
            }

            dec = dec * 10;
            arr = out;
        }
        return arr;
    }
}
