//https://ucilnica.fri.uni-lj.si/pluginfile.php/128125/mod_resource/content/3/vaje01.pdf

import java.util.Scanner;

public class Kvadrati {
    public static void main (String args[]){
        // create scanner
        Scanner sc = new Scanner(System.in);
        int start = sc.nextInt();
        int end = sc.nextInt();

        // loop between start and inclusivly end
        for(int i = start; i <= end; i++){
            // print out the squared counter
            System.out.println(i*i);
        }

        // close reader
        sc.close();
    }
}