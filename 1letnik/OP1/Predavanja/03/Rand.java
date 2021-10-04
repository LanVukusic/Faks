import java.util.Random;
import java.util.Scanner;

public class Rand {
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int secret = random.nextInt(50)+1;
        int input = 0;
        
        while (input != secret){
            //System.out.println("Guess the numberrrr!!");
            input = sc.nextInt();
            if(input > secret){
                System.out.println("too much");
            }else if ( input < secret){
                System.out.println("too little");
            }

        }

        System.out.println("Goood fkn job!!!");
    }
}