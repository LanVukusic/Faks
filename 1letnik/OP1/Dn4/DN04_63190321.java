import java.util.Scanner;

/**
 * DN04_63190321
 */
public class DN04_63190321 {

    public static void main(final String[] args) {
        final Scanner sc = new Scanner(System.in);
         final int baseIn = sc.nextInt();
         final int baseOut = sc.nextInt();
         final int inLen = sc.nextInt();
         final String[] number = new String[inLen];
         final int[] number2 = new int[inLen];

         for (int i = 0; i < inLen; i++) {
             number[i] = sc.next();
         }
         for (int i = 0; i < inLen; i++) {
             number2[i] = (char)number[i] - "0";
         }
         System.out.println(number[2]);
    }
}