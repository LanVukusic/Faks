import java.util.Scanner;

/**
 * Compress
 */
public class Compress {
    static int[][] matrix;
    static int matrixLength;
    static int smallest;
    static int largest;
    static int representBitLen;

    public static void main(String[] args) {
        // create new scanner and read matrix
        Scanner sc = new Scanner(System.in);
        matrixLength = sc.nextInt();

        // fill the matrix and search for the smallest and largest int
        smallest = 266;
        largest = 0;
        matrix = new int[matrixLength][matrixLength];

        for(int x = 0; x < matrixLength; x++){
            for(int y = 0; y < matrixLength; y++){
                int a = sc.nextInt();
                if (a >= largest){
                    largest = a;  // finding largest
                }
                if(a <= smallest){
                    smallest = a;  // finding smallest
                }
                matrix[x][y] = a;
            }
        }

        print(dec2bin(smallest, 8));
        print(dec2bin(getBitSize(largest - smallest), 4));


    }

    // returns the number of bits needed to represent the number.
    // input is base10
    public static int getBitSize (int number){
        for (int i = 1; i < 8; i++){
            if(number - Math.pow(2, i) < 0){
                return i;
            }
        }
        return 8;
    }

    // returns the string representation of the number.
    //input is base10,      output is base2
    public static String dec2bin (int number, int padding){
        String a = Integer.toBinaryString(number);
        int b = (padding - a.length());
        for (int i = 0; i < b; i++) {
            a = "0" + a;
        }
        return a;
    }


    // for debuging purpouses that was made in to a function.
    public static void print (Object a){
        System.out.println(a);
    }

    public static void vectorFromEdge (int[][] matrix, int x, int y){
        
    }
}





