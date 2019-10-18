import java.util.Scanner;
public class PrimeList {

    public static boolean[] allPrimes (int n){
        //false =  primer
        //true == not prime
        boolean[] allCandidates = new boolean[n];
        allCandidates[0] = true;
        allCandidates[1] = true;
        for (int i = 2; i< n; i++) {
            if (!allCandidates[i]){ // finds first prime candidate

                for(int j = i*2; j<n; j+= i){
                    allCandidates[j] = true;
                     
                }
            }
        }
        return(allCandidates);
    }

    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        boolean[] myPrimes = allPrimes(sc.nextInt());
        /* for (int i = 0; i < myPrimes.length; i++) {
            if(!myPrimes[i])
                System.out.println(i);
        } */
        System.out.println(myPrimes.length);
    }
} 