/**
 * naloga
 */
import java.io.IOException;
import java.util.Scanner;

public class izziv {

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        

        int[] A = new int[n];
        int[] B = new int[m];
        int[] C = new int[m+n];

        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }

        for (int i = 0; i < m; i++) {
            B[i] = scanner.nextInt();
        }

        int indexA=0, indexB=0, indexC=0;

        int out = 0;

        while (true){
            if(indexA>=n && indexB >= m){
                break;
            }
            out ++;

            // iz A
            if(indexA<n && indexB >= m){
                C[indexC] = A[indexA];
                indexC++;
                indexA++;
                System.out.print("a");
                continue;
            }

            // iz B
            if(indexA>=n && indexB < m){
                C[indexC] = B[indexB];
                indexC++;
                indexB++;
                System.out.print("b");
                continue;
            }

            // uzamemo iz A
            if(A[indexA] < B[indexB]){
                C[indexC] = A[indexA];
                indexC++;
                indexA++;
                System.out.print("a");
                continue;
            }

            // uzamemo iz B
            if(A[indexA] > B[indexB]){
                C[indexC] = B[indexB];
                indexC++;
                indexB++;
                System.out.print("b");
                continue;
            }

            // tud iz A ampak premaknemo oba indexa
            if(A[indexA] == B[indexB]){
                C[indexC] = B[indexB];
                indexC++;
                indexA++;
                System.out.print("a");
                continue;
            }

        }
        System.out.println("");
        scanner.close();
        for (int i = 0; i < out; i++) {
            System.out.print(""+C[i]+" ");
        }
    }
}