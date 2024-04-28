import java.util.Scanner;

public class Romanje1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int path = sc.nextInt();
        int speed = sc.nextInt();

        for (int i = path; i > 0; i-=speed) {
            System.out.println(i);
            
        }
    }
}