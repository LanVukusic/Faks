import java.util.Scanner;

public class Romanje2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int path = sc.nextInt();
        int speed = sc.nextInt();
        
        int dayCounter = 1;

        for (int i = path; i > 0; i-=speed) {
            if(i-speed <= 0){
                System.out.printf("%d. dan: %d -> %d (prehodil %d)\n",dayCounter, i, 0, i);    
            }else{
                System.out.printf("%d. dan: %d -> %d (prehodil %d)\n",dayCounter, i, i-speed, speed);
                // %d is used for printing ints, %i returns an error....faulty web sources..damn
            }
            
            dayCounter++;
        }
    }
}