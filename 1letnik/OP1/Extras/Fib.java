import java.util.Scanner;
public class Fib {
   static int fib (int input){
        if(input <1 ){
            return 1;
        }
        else{
            return fib(input-1)+fib(input-2);
        }

    }
    public static void main (String[] args){
        Scanner myScanner = new Scanner(System.in);
        System.out.println(fib(myScanner.nextInt()));
        myScanner.close();
    }
}