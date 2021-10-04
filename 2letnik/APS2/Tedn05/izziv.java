/**
 * izziv
 */
import java.util.Scanner;

public class izziv {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String[] ln = sc.nextLine().split(" ");
        int[] ar = new int[ln.length];
        for (int i = 0; i < ar.length; i++) {
            ar[i] = Integer.parseInt(ln[i]);
        }

        rek(ar,0,ar.length-1);
    }

    


    public static int rek(int[] in, int start, int end){
        if(end - start == 0){ // length of 1
            System.out.printf("[%d]: %d\n", in[start], in[start]);
            return in[start];
        }
        int mid = ((end+start)/2);
        //get best option of subarrays
        int out = Math.max(rek(in, start, mid), rek(in, mid+1, end));

        //get best option from cross arrays
        int max = in[mid] + in[mid+1];
        //left expansion
        int expand = 0;
        int running = max;
        while (true){
            expand++;
            if((mid)-expand < start){
                break;
            }
            running += in[(mid)-expand];
            max = Math.max(max, running);
        }

        //right expansion
        expand = 0;
        running = max;
        while (true){
            expand++;
            if((mid+1)+expand > end){
                break;
            }
            running += in[(mid+1)+expand];
            max = Math.max(max, running);
        }
        pyJoin(" ,", in, start, end);
        System.out.println(Math.max(max, out));
        return Math.max(max, out);

    }

    public static void pyJoin(String delimiter, int[] arr, int a, int b){
        System.out.print("[");
        System.out.print(arr[a]);
        for (int i = a+1; i <= b; i++) {
            System.out.print(", ");
            System.out.print(arr[i]);
        }
        System.out.print("]: ");

    }
}