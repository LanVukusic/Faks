import java.util.Scanner;
public class Prime {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int possiblePrime = sc.nextInt();
        sc.close();
        if (possiblePrime == 1)
            System.out.println("is NOT prime");
        if(possiblePrime > 1 && possiblePrime <= 3){
            System.out.println("is prime");
        }

        // 2 or 3 are special so check that

        if(possiblePrime % 2 == 0 || possiblePrime % 3 == 0) {
            System.out.println("is Not prime");
        }else{
            for (int i = 5; i*i <= possiblePrime; i+= 6){
                if(possiblePrime % i == 0){
                    System.err.println("is Not prime");
                    break;
                }
            }
        }
        System.out.println("is Primee");
    }
}