import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList; 

public class Compress {
    static int[][] matrix;
    static int matrixLength;
    static int smallest;
    static int largest;
    static int representBitLen;
    static String output;
    static Queue<Node> fifoStore;

    public static void main(String[] args) {
        // create new scanner and read matrix 0101 | 001|0000 | 001|1000 | 001|0001 | 001|0100 | 010|0000
        Scanner sc = new Scanner(System.in);
        matrixLength = sc.nextInt();

        fifoStore = new LinkedList<>();

        // fill the matrix and search for the smallest and largest int
        smallest = 266;
        largest = 0;
        output = "";
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

        print(dec2bin(smallest, 8));  // PROTO 1
        representBitLen = getBitSize(largest - smallest);
        print(dec2bin(representBitLen, 4));  // PROTO 2
        print(dec2bin(matrixLength, 8));  // PROTO 3

        for (int x = 0; x < matrixLength; x++) {
            for (int y = 0; y < matrixLength; y++) {
                if(matrix[x][y] != -1){
                    
                    print(dec2bin(shorten( matrix[x][y] ), representBitLen));
                    print((nodeWiter(rekursiveLoger(1, x, y, matrix[x][y],(short)0))));
                }
            }
        }
        System.out.println(output);

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
        //System.out.println(a);
        output += a.toString();
    }

    // rekursiveLogger cuts the tree of instructions in smaller subproblems
    public static Node rekursiveLoger (int streak, int x, int y, int number, short direction){
        matrix[x][y] = -1;  // we mark the current place
        Node temp = new Node(direction, (short)0, streak, number);  // we create a temporery node

        // UP
        if((y - 1 >= 0 && matrix[x][y-1] == number)){  // if we an go up
            if(direction == (short)1){  // and are already heading up
                // it is  only a continuation
                return rekursiveLoger(streak + 1, x, y-1, number, direction);
            }else{  // we an go up but we are not heading there, so its an edge
                
                temp.children ++;
                temp.childrenArr[0] = rekursiveLoger(1, x, y-1, number, (short)1);
            }
        }

        // DOWN
        if((y + 1 < matrixLength && matrix[x][y+1] == number)){  // if we an go down
            if(direction == (short)2){  // and are already heading down
                // it is  only a continuation
                return rekursiveLoger(streak + 1, x, y+1, number, direction);
            }else{  // we an go down but we are not heading there, so its an edge
                
                temp.children ++;
                temp.childrenArr[1] = rekursiveLoger(1, x, y+1, number, (short)2);
            }
        }
        
        // LEFT
        if((x - 1 >= 0 && matrix[x-1][y] == number)){  // if we an go left
            if(direction == (short)3){  // and are already heading left
                // it is  only a continuation
                return rekursiveLoger(streak + 1, x-1, y, number, direction);
            }else{  // we an go left but we are not heading there, so its an edge
                
                temp.children ++;
                temp.childrenArr[2] = rekursiveLoger(1, x-1, y, number, (short)3);
            }
        }

        // RIGHT
        if((x + 1 < matrixLength && matrix[x+1][y] == number)){  // if we an go right
            if(direction == (short)4){  // and are already heading right
                // it is  only a continuation
                return rekursiveLoger(streak + 1, x+1, y, number, direction);
            }else{  // we an go right but we are not heading there, so its an edge
                
                temp.children ++;
                temp.childrenArr[3] = rekursiveLoger(1, x+1, y, number, (short)4);
            }
        }
        // if we are not located on the edge, our journey has ended
        return temp;  // we return the path we made
        
    }

    static String nodeWiter(Node in){
        String out = "";

        // put children of the root node in a stack, and mark first 4 bits (0010) for directionss
        for(Node n : in.childrenArr){
            // UP DOWN RIGHT LEFT
            if(n == null){
                out += "0";
            }else{
                fifoStore.add(n);
                out += "1";
            }
        }

        while(!fifoStore.isEmpty()){
            //out += " | ";
            
            Node temp = fifoStore.poll();
            out += dec2bin(temp.streak, getBitSize(matrixLength));
            //out += "|";
            for(Node n : temp.childrenArr){
                if(n == null){
                    out += "0";
                }else{
                    fifoStore.add(n);
                    out += "1";
                }
            }
        }
        return out;
    }

    static void mark(Vector2 point){
        matrix[point.x][point.y] = -1;
    }

    static int shorten (int num){
        return num - smallest;
    }

    static class Node {
        short direction;
        int number;
        int streak;
        short children;
        Node[] childrenArr;
    
    
        Node(short direction, short children, int streak, int number){
            this.direction = direction;
            this.number = number;
            this.streak = streak;
            this.children = children;
            this.childrenArr = new Node[4];
        }

    }

    static class Vector2 {
        short x;
        short y;

        public Vector2(short x, short y){
            this.x = x;
            this.y = y;
        }
    }
    
}










