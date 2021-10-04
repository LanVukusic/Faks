
/**
 * naloga
 */

import java.io.IOException;
import java.util.Scanner;


public class naloga {

    static void pogrezni(int arr[], int i, int dolzinaKopice) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < dolzinaKopice && arr[l] > arr[largest])
            largest = l;

        // If right child is larger than largest so far
        if (r < dolzinaKopice && arr[r] > arr[largest])
            largest = r;

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            pogrezni(arr, largest, dolzinaKopice);
        }
    }

    static void buildHeap(int arr[]) {
        // Index of last non-leaf node
        int startIdx = (arr.length / 2) - 1;

        for (int i = startIdx; i >= 0; i--) {
            pogrezni(arr, i, arr.length);
        }
    }

    public static void prints(int[] arr, int j){
        int left = 1;
        int left_mod = 1;
        for (int i = 0; i < arr.length - j; i++) {
            if(left == 0){
                System.out.print("| ");
                left_mod = left_mod * 2;
                left = left_mod;
            }
            System.out.print(""+arr[i]+" ");
            left --;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] tab = new int[n];

        for (int i = 0; i < tab.length; i++) {
            tab[i] = scanner.nextInt();
        }
        buildHeap(tab);
        for(int i = tab.length -1; i>=0; i--){
            int j = tab.length -i -1 ;
            //printHeap(tab, j-1);
            prints(tab, j);
            int temp = tab[0];
            tab[0] = tab[tab.length - 1 -j];
            tab[tab.length - 1 -j] = temp;
            pogrezni(tab, 0, i);
            
            System.out.println();
        }
        
        scanner.close();
    }
}