import java.io.IOException;
import java.util.Scanner;

public class izziv {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int[] arr = new int[n];
        int[] out = new int[n];
        int[] count = new int[33];

        // read data
        for (int i = 0; i < arr.length; i++) {
            arr[i] = scanner.nextInt();
            
        }
        scanner.close();
        
        // count stuff
        for (int i = 0; i < arr.length; i++) {
            count[dec2binlen(arr[i])]++; // index i in count array coresponds with ammount of nubmers with i binary digits
        }
        int a = 0;

        int sum = 0;
        for (int i = 0; i < count.length; i++) {
            sum += count[i];
            count[i] = sum;
        }

        for (int i = arr.length-1; i >= 0; i--) {
            int ll = dec2binlen(arr[i]);
            int lol = count[ll]- 1;
            out[lol] = arr[i];
            System.out.printf("(%d,%d)\n", arr[i], lol);
            count[dec2binlen(arr[i])] --;
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(""+out[i]+" ");
        }
    }

    public static int dec2binlen(int i){
        return Integer.toBinaryString(i).replace("0", "").length();
    }
}
