import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

public class Naloga2v2 {
    static String[][] grid;
    static Cell[][] visited;
    static int xLen, yLen, xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE, lenMax = Integer.MIN_VALUE;
    static String pathMax = "";

    static class Cell{
        public int status = 0; // 0-unvisited, 1-processing, 2-completed
        public int up, down, left, right;
        public String upPath, downPath, leftPath, rightPath;
        public Cell(){}
    }

    public static void main(String[] args) throws IOException{
         // read the input data
         FileReader fileIn = new FileReader(args[0]);
         BufferedReader reader = new BufferedReader(fileIn);
         String[] line = reader.readLine().split(",");
         xLen = Integer.parseInt(line[0]); // road length
         yLen = Integer.parseInt(line[1]); // max tank
         grid = new String[xLen][yLen];
 
         for (int x = 0; x < xLen; x++) {
             String[] lineIn = reader.readLine().split(",");
             for (int y = 0; y < yLen; y++) {
                 grid[x][y] = lineIn[y];
             }
         }
         reader.close();
         // solve the problem 
         for(int x = 0; x < xLen; x++){
            for( int y = 0; y < yLen; y++){
                // do shit
                rek(x, y, grid, grid[x][y], 0, 5);
            }
        }
        System.out.println(pathMax);
    }

    public static int max(int[] arr, int skip){
        int highest = -1;
        for (int i = 0; i > arr.length; i++){
            if(i == skip){
                continue;
            }
            if(arr[i] > highest){
                highest = arr[i];
            }
        }

        return highest;
    }

    public static int rek(int x, int y, String[][] grid, String type, int len, int entrySide){
        // do we already know the answer?
        if (visited[x][y] != null){
            // check what happens if we approach from another side
            if(visited[x][y].status == 1){
                return 0;
            }

            // from right
            if(entrySide == 0 && visited[x][y].left < len)
        }

        // tag it as visited but not processed
        Cell temp = new Cell();
        temp.status = 1;
        visited[x][y] = temp;


        // check all four sides to get the longest path

        int left, up, right, down;

        // left
        // type is the path color we are following
        // entry side tells us where we are comming from
        if((x-1) >= 0 && grid[x-1][y] == type && entrySide != 2){
            left = rek(x-1, y, grid, type, len + 1, 0);
        }else{
            left = 0;
            if(entrySide == 2){
                left = len;
            }
        }
        // up
        if((y-1) >= 0 && grid[x][y-1] == type && entrySide != 3){
            up = rek(x, y-1, grid, type, len + 1, 1);
        }else{
            up = 0;
            if(entrySide == 3){
                up = len;
            }
        }
        // right
        if((x+1) < xLen && grid[x+1][y] == type && entrySide != 0){
            right = rek(x+1, y, grid, type, len + 1, 2);
        }else{
            if(entrySide == 0){
                right = len;
            }
            right = 0;
        }
        // down
        if((y+1) < yLen && grid[x][y+1] == type && entrySide != 1){
            down = rek(x, y+1, grid, type, len + 1, 3);
        }else{
            if(entrySide == 1){
                down = len;
            }
            down = 0;
        }
        Cell cell = new Cell();
        cell.left = left;
        cell.right = right;
        cell.up = up;
        cell.down = down;
        visited[x][y] = cell;

        return max(new int[]{right, down, left, up}, entrySide);
    }
}
