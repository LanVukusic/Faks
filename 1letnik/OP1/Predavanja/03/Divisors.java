import java.util.Scanner;

public class Divisors {
    public static int numOfDivisors(int n){
        int num = 0;
        for(int i = 1; i<= n/2; i++){
            if (n%i == 0){
                System.out.println(i);
                num++;
            }
        }
        System.out.println(n);
        return(num+1);
    }

    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println(numOfDivisors(sc.nextInt()));
        //sc.Close();
    }
}