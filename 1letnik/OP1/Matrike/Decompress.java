import java.util.Scanner;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Decompress {
    static Scanner sc;
    static int[][] matrix;
    static int matrixLength;
    static int smallest;
    static int largest;
    static int representBitLen;
    static String input;
    static int readerHeadPos;
    static Queue<Vector2> roots;
    static Queue<Integer> directions;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        input = sc.nextLine();

        smallest = bin2dec(readChunk(8));
        representBitLen = bin2dec(readChunk(4));
        matrixLength = bin2dec(readChunk(8));
        // create a matrix
        matrix = new int[matrixLength][matrixLength];
        for (int[] row: matrix){  // fill the matrix with -1
            Arrays.fill(row, -1);
        }

        // read the buffer and populate matrix
        for (int x = 0; x < matrixLength; x++) {
            for (int y = 0; y < matrixLength; y++) {
                
                if(matrix[x][y] == -1){
                    // fill the island of numbers
                    //System.out.println(""+x+" "+y);
                    tableFill(new Vector2((short)x, (short)(y)));
                    //printTable();
                    break;
                }
            }
        }

        // print out the table
        printTable();

    }

    static void printTable(){
        for (int y = 0; y < matrixLength; y++) {
            for (int x = 0; x < matrixLength; x++) {
                System.out.print(matrix[y][x]);
            }
            System.out.println();
        }
    }

    // populates the matrix in a direction <dir> from <root> of len <len> with number <num>
    static void writeLine (int dir, Vector2 root, int len, int num){
        Vector2 newRoot = new Vector2();
        //System.out.println("number: "+ num+" of len " + len + " in dir "+ dir + " root " + root.x+ " " + root.y);
        // left right up down
        // 0    1    2    3

        for (int i = 0; i <= len; i++) {
            //System.out.println(num);
            //System.out.println(dir);
            if(dir == 0){  // LEFT
                if(i == len){
                    matrix[root.y][root.x - i] = num;
                    newRoot = new Vector2 ((short)(root.x-i), (short)root.y );
                }
            }
            if(dir == 1){  // RIGHT
                matrix[root.y][root.x + i] = num;
                if(i == len){
                    newRoot = new Vector2 ((short)(root.x+i), (short)root.y );
                }
            }
            if(dir == 3){  // DOWN
                matrix[root.y + i][root.x] = num;
                if(i == len){
                    newRoot = new Vector2 ((short)root.x, (short)(root.y+i) );
                }
            }
            if(dir == 2){  // UP
                matrix[root.y - i][root.y] = num;
                if(i == len){
                    newRoot = new Vector2 ((short)root.x, (short)(root.y-i) );
                }
            }
        }
        // System.out.println(newRoot.x);
        // System.out.println(newRoot.y);
        for (int j = 0; j < 4; j++) {
            if(! readChunk(1).equals("0")){
                // it has that position
                roots.add(newRoot);
                directions.add(j);
            }
        }
    }

    static void tableFill (Vector2 pos){
        int num = longen(bin2dec(readChunk(representBitLen)));
        //System.out.println("doing for number "+num);
        matrix[pos.x][pos.y] = num;
        roots = new LinkedList<>();
        directions = new LinkedList<>(); // left right up down
                                    // 0    1    2    3
         
        for (int i = 0; i < 4; i++) {
            if(! readChunk(1).equals("0")){
                //System.out.println("detected dir "+i);
                // it has that position
                roots.add(new Vector2(pos.x, pos.y));
                directions.add(i);
            }
        }

        while (! roots.isEmpty()){
            writeLine(directions.remove(), roots.remove(), bin2dec(readChunk(getBitSize(matrixLength))), num);
        }
    }

    static int bin2dec (String num){
        return Integer.parseInt(num, 2);
    }

    // the oposite of shorten lol
    static int longen (int num){
        return num + smallest;
    }

    // returns the next substring from input, which length is <chunkLen>
    static String readChunk (int chunkLen){
        String out = "";
        out = input.substring(readerHeadPos, readerHeadPos + chunkLen);
        readerHeadPos += chunkLen;
        return out;
    }

    // returns the number of bits needed to represent the number.
    // input is base10
    static int getBitSize (int number){
        for (int i = 1; i < 8; i++){
            if(number - Math.pow(2, i) < 0){
                return i;
            }
        }
        return 8;
    }

    static class Vector2 {

        short x;
        short y;

        public Vector2(short x, short y){
            this.x = x;
            this.y = y;
        }

        public Vector2(){
        }

    }


}
