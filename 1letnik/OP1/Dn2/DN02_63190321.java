import java.util.Scanner;

/**
 * DN02_63190321
 * https://ucilnica.fri.uni-lj.si/pluginfile.php/130234/mod_resource/content/1/stevila.pdf
 */
public class DN02_63190321 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int command = sc.nextInt();
        int a = sc.nextInt();
        int b = sc.nextInt();
        int k = sc.nextInt();

        switch(command){
            case 1:  // number of instances between a and b devisible by k
                int devisible = 0;
                for (int i = a; i < b+1; i++) {
                    if(i % k == 0){
                        devisible ++;
                    }
                }
                //System.out.println(devisible);
                break;

            case 2:  // number of instances from a to b which include "k"
                int hasK = 0;
                // String temp;
                for (int i = a; i < b+1; i++) {
                    // temp = ""+i;
                    if((""+i).contains(""+k)){
                        hasK ++;
                    }
                }
                System.out.println(hasK);
                break;

            case 3:  // number of instances from a to b where all decimals are devisible by "k"
                int divByK = 0;
                for (int i = a; i < b+1; i++) {
                    if (decimalsDevisibleBy(i, k)){
                        divByK ++;
                    }
                }
                System.out.println(divByK);
                break;
                
            case 4:  // number of ints with k consequative digits
                int conseqDec = 0, currD, numD;
                for (int i = a; i < b+1; i++) {
                    currD = 10;  // something that no single digit could be
                    numD = 0;
                    for (int j = 1; j <= numDecimals(i); j++) {
                        if(decimalAtPlace(i, j) == currD){
                            numD += 1;
                            if(numD == k){
                            //System.out.println(i);
                                conseqDec ++;
                                break;
                            }
                        }else{
                            currD = (decimalAtPlace(i, j));
                            numD = 1;
                        }
                    }
                }
                System.out.println(conseqDec);
                break;
            
            case 5:  // number of ints with decreasing/increasing subsequent digitssingle
                int numberUsefull = 0, nm1, recurred, modifier = 1, currDec;
                // modifier; used as either +1 or -1 when checking subsequences
                for (int i = (int)Math.max(10*k, a); i < b+1; i++) {  // start withsinglelong subsequence
                    recurred = 1;  // reset the reocurring
                    nm1 = 99; // larger than any single digit number

                    //check for each number
                    for (int j2 = 1; j2 <= numDecimals(i); j2++) {
                        currDec = decimalAtPlace(i, j2);
                        if(nm1 + modifier == currDec){  // it's a previous subsequence
                            recurred ++;
                            
                        }else{
                            if(nm1 + modifier*-1 == currDec){  //it's a new subsequence
                                recurred = 2;
                                modifier *= -1;
                            }else{
                                recurred = 1;
                                // reset the streak
                            }
                        }
                        nm1 = currDec;  // set the n-1 mark to the current number
                        if(recurred >= k){  // check if reaccourance is long enough
                            numberUsefull ++;
                            //System.out.println(i);
                            break;
                        }
                    }
                }
                System.out.println(numberUsefull);
        }
    }

    // returns true if all decimals are divisible by divider
    public static boolean decimalsDevisibleBy (int num, int divider){
        for(int i = 1; i <= numDecimals(num); i++){
            if(decimalAtPlace(num, i) % divider != 0){
                return false;
            }
        }
        return true;
    }
    
    //returns decimal at certain place: 54321 NOT 12345
    public static int decimalAtPlace (int num, int place){
        num = num % (int)Math.pow(10, place); // strips leading numbers
        num = num - num % (int)Math.pow(10, place-1);
        return num / (int)Math.pow(10, place-1);
    }

    //returns the number of decimals that a number has
    public static int numDecimals (int num){
        int places = 1;
        while (num > 10){
            num = num / 10;
            places ++;
        }  
        return places;
    }
    
}