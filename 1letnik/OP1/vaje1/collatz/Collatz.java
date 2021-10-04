//https://ucilnica.fri.uni-lj.si/pluginfile.php/128125/mod_resource/content/3/vaje01.pdf

import java.util.Scanner;

public class Collatz {
    public static void main (String args[]){
        // create scanner
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();

        int ourNum = input;

        while (ourNum != 1){
            if(ourNum % 2 == 0){ // number is even
                ourNum /= 2;
            }else{
                ourNum = ourNum * 3 + 1;
            }
            System.out.println(ourNum);
        }
    }
}