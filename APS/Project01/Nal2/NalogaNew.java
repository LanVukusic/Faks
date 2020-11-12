import java.io.*;
import java.nio.channels.SelectableChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.stream.StreamSource;

class Out {
    public int len = 0;
    public String path = "";

    public Out(int l, String p) {
        this.len = l;
        this.path = p;
    }
}

public class NalogaNew {
    static String[][] grid;
    static int xLen, yLen;
    static String pathMax = "";

    enum Dir {
        Up, Down, Left, Right, Omni;
    }

    public static Out getBest(Out[] in){
        Out b = new Out(0, "");
        for (Out out : in) {
            if(out.len > b.len){
                b = out;
            }
        }
        return b;
    }

    public static String[][] Copy(String[][] in) {
        String[][] out = new String[in.length][in[0].length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                out[i][j] = ""+in[i][j];
            }
        }
        return out;
    }

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader("in.txt");//args[0]);
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        xLen = Integer.parseInt(line[0]); // road length
        yLen = Integer.parseInt(line[1]); // max tank
        grid = new String[xLen][yLen];

        for (int y = 0; y < yLen; y++) {
            String[] lineIn = reader.readLine().split(",");
            for (int x = 0; x < xLen; x++) {
                grid[x][y] = lineIn[y];
            }
        }
        reader.close();

        int a = Integer.MIN_VALUE;
        String pathinjo = "";

        for (int y = 0; y < yLen; y++) {
            for (int x = 0; x < xLen; x++) {
                Out vn = rek(x, y, 0, "", Copy(grid));
                if (vn.len > a) {
                    a = vn.len;
                    pathinjo = vn.path;
                }
            }
        }
        System.out.println(a);
        System.out.println(pathinjo);
    }

    public static Out rek(int x, int y, int len, String path, String[][] visited) {
        if(visited[x][y].equals("")){
            return new Out(0,"");
        }
        String[][] map = Copy(visited);
        map[x][y] = "";
        Out up=new Out(0,""), down=new Out(0,""), right=new Out(0,""), left=new Out(0,"");
        
        // up
        if (y + 1 < yLen && grid[x][y].equals(visited[x][y + 1])) {
            up = rek(x, y + 1, len + 1, path + "GOR ",  Copy(map));
            System.out.println("GOR");
        }

        // down
        if (y - 1 >= 0 && grid[x][y].equals(visited[x][y - 1])) {
            down = rek(x, y - 1, len + 1, path + "DOL ", Copy(map));
            System.out.println("DOWKA");
        }

        // right
        if (x + 1 < xLen && grid[x][y].equals(visited[x+1][y])) {
            right = rek(x + 1, y, len + 1, path + "DESNO ", Copy(map));
            System.out.println("DESNO");
        }

        // left
        if (x - 1 >= 0 && grid[x][y].equals(visited[x-1][y])) {
            left = rek(x - 1, y, len + 1, path + "LEVO ", Copy(map));
            System.out.println("LEVO");
        }

        Out a = getBest(new Out[]{up, down, right, left});
        a.len += len;
        a.path =  a.path;
        return a;
    }

}
