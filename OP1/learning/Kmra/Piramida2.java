import java.util.Scanner;

public class Piramida2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int stVrstic= sc.nextInt();
        int stevilo=1;
        int presledki=4;
        for (int vr=1; vr<=stVrstic; vr++){
            for (int x=1; x<=presledki; x++){
                System.out.print(" ");
            }
            for (int i=3; i<=vr*2+1; i++)
            if (stevilo<10){
                System.out.print(stevilo);
                stevilo++;
            }
            else {
                System.out.print(stevilo%10);
                stevilo++;
            }
            presledki=presledki-1;
            stevilo=vr+1;
            System.out.println();

            
        }         
        }
    
}
