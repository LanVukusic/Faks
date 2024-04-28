import java.io.*;
import java.security.Guard;
import java.util.concurrent.CountDownLatch;
public class Naloga2_bcupp {
    static char[][] grid;
    static int xLen, yLen, xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE,       lenMax = Integer.MIN_VALUE;
    static String pathMax = "";

    enum Dir {
        Up, Down, Left, Right, Omni
    }

    static class Cache {
        int length = 0;
        String path = "";

        public Cache(){}
        public Cache(String path, int length){
            this.path = path;
            this.length = length;
        }
    }

    public static void main2(String[] args) {
        System.out.println(maxCache(new Cache[]{new Cache("123", 5),new Cache("", 4),new Cache("", 2),new Cache("", 2)}).path);
    }

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);

        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        xLen = Integer.parseInt(line[1]); // road length
        yLen = Integer.parseInt(line[0]); // max tank
        grid = new char[yLen][xLen];

        for (int y = 0; y < yLen; y++) {
            String[] lineIn = reader.readLine().split(",");
            for (int x = 0; x < xLen; x++) {
                grid[y][x] = lineIn[x].charAt(0);
            }
        }
        reader.close();

        debugMe(grid);

        Cache vn;
        int mojx = 0, mojy = 0;
        for(int y = 0; y < yLen; y++){
            for( int x = 0; x < xLen; x++){
                // do stuff 

                vn = rek(x, y, new char[yLen][xLen], 0, grid[y][x], "");
                if(vn.length > lenMax){
                    lenMax = vn.length;
                    pathMax = vn.path;
                    mojx = x;
                    mojy = y;
                }
            }
            //System.out.println(". ");
        }
        System.out.println(mojx);
        System.out.println(mojy);
        System.out.println("Max len: "+lenMax);
        System.out.println(pathMax);
    }

    public static void debugMe(char[][] grid){
        System.out.println();
        for (int y = 0; y < yLen; y++) {
            for (int x = 0; x < xLen; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    public static Cache maxCache(Cache[] in){
        Cache big = new Cache();
        big.length = -1;
        for (Cache cache : in) {
            if(cache == null){
                continue;
            }
            if(cache.length>big.length){
                big = cache;
            }
        }
        return big;
    }

    public static Cache rek (int x, int y, char[][] mask, int len, char izbrana, String path) {
        Cache u=null, d=null, l=null, r=null;
        mask[y][x] = 1;
        // up
        if((y-1)>=0){
            if(grid[y-1][x] == izbrana && (mask[y-1][x]) != 1){
                u = rek(x, y-1, mask, len + 1, izbrana,path+",GOR");
            }
        }

        // down
        if((y+1)<yLen){
            if(grid[y+1][x] == izbrana && (mask[y+1][x] ) != 1){
                u = rek(x, y+1, mask, len + 1, izbrana,path+",DOL");
            }
        }

        // left
        if((x-1)>=0){
            if(grid[y][x-1] == izbrana && (mask[y][x-1]) != 1){
                u = rek(x-1, y, mask, len + 1, izbrana,path+",LEVO");
            }
        }

        // right
        if((x+1)<xLen){
            if(grid[y][x+1] == izbrana && (mask[y][x+1]) != 1){
                u = rek(x+1, y, mask, len + 1, izbrana,path+",DESNO");
            }
        }

        mask[y][x] = 0;

        return maxCache(new Cache[]{u,d,l,r, new Cache(path, len)});
    }

    // public static Cache rek2 (int x, int y, String[][] visited, Dir direction, int len, String path) {
    //     /*
    //     CacheKey key = new CacheKey(direction,new int[]{x,y});
    //     Cache out = used.get(key);
    //     if(out != null){
    //         //System.out.println("cached");
    //         return out;
    //     }
    //     //System.out.println("nocache");
    //     */

    //     Cache u=null, d=null, l=null, r=null;
    //     String[][] mask = Copy(visited);
    //     mask[x][y] = "";

    //     // up
    //     if((y-1)>=0 && grid[x][y].equals(visited[x][y-1])){
    //         u = rek(x, y-1, mask, Dir.Up, len + 1, path+",GOR");
    //     }

    //     // down
    //     if((y+1)<yLen && grid[x][y].equals(visited[x][y+1])){
    //         d = rek(x, y+1, mask, Dir.Down, len + 1, path+",DOL");
    //     }

    //     // left
    //     if((x-1)>=0 && grid[x][y].equals(visited[x-1][y])){
    //         l = rek(x-1, y, mask, Dir.Left, len + 1, path+",LEVO");
    //     }
    //     // right
    //     if((x+1)<xLen && grid[x][y].equals(visited[x+1][y])){
    //         r = rek(x+1, y, mask, Dir.Right, len + 1, path+",DESNO");
    //     }
    //     // cache our thingey
    //     Cache best = maxCache(new Cache[]{u,d,l,r, new Cache(path, len)});
    //     //used.push(key, best);
    //     return best;
    // }


}
