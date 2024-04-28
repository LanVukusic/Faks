import java.util.Scanner;
import java.lang.Math;

public class Ledenesvece {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int height = sc.nextInt();
        int length = (int)Math.pow(2, height-1);

        for (int i = 1; i<=height; i++){
            for (int j = 0; j<=length; j++){
                if(j%(length / Math.pow(2, height-i)) == 0){
                    System.out.print("*");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }


    }
}