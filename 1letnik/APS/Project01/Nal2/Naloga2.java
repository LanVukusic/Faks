import java.io.*;


class Cache {
    int length = 0;
    String path = "";

    public Cache(){}
    public Cache(String path, int length){
        this.path = path;
        this.length = length;
    }
}

class Poskus {
    int koraki = 0;
    String path = "";

    public Poskus(int koraki, String path) {
        this.koraki = koraki;
        this.path = path;
    }
}

public class Naloga2{
    static char[][] grid;
    static int xLen, yLen, xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE,       lenMax = Integer.MIN_VALUE;
    static String pathMax = "";

    public static char[][] Copy(char[][] in){
        char[][] out = new char[in.length][in[0].length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                out[i][j] = in[i][j];
            }
        }
        return out;
    }

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        xLen = Integer.parseInt(line[0]); //visina
        yLen = Integer.parseInt(line[1]); //sirina
        grid = new char[xLen][yLen];

        for (int y = 0; y < yLen; y++) {
            String[] lineIn = reader.readLine().split(",");
            for (int x = 0; x < xLen; x++) {
                grid[x][y] = lineIn[x].charAt(0);
            }
        }
        reader.close();

        debugMe(grid);

        Cache vn;
        int mojx = 0, mojy = 0;
        String potka = "";
        for(int y = 0; y < yLen; y++){
            for( int x = 0; x < xLen; x++){
                
                Poskus a =stejKorake(grid, grid[x][y], x, y, 0, new int[xLen][yLen], "");
                if(a.koraki > lenMax){
                    lenMax = a.koraki;
                    mojx = x; mojy = y;
                    potka = a.path;
                }
            }
        }

        
        //vn = rek_path(mojx, mojy, Copy(grid), 1, "");
        System.out.print(mojy);
        System.out.println(","+mojx);
        System.out.println(potka);
        System.out.println("Max len: "+lenMax);
    }

    public static Poskus stejKorake(char[][] crke, char izbrana, int i, int j, int koraki, int[][] path, String potka) {
        int stuck = 1;
        Poskus desno = new Poskus(0, "");
        Poskus levo = new Poskus(0, "");
        Poskus gor = new Poskus(0, "");
        Poskus dol = new Poskus(0, "");
        if (koraki == 0) {
            path[i][j] = 1;
        }
        // desno
        if (j + 1 != crke[0].length) {
            if (crke[i][j + 1] == izbrana && path[i][j + 1] != 1) {
                path[i][j + 1] = 1;
                stuck = 0;
                desno = stejKorake(crke, izbrana, i, j + 1, koraki + 1, path,
                        potka == "" ? potka + "DESNO" : potka + "," + "DESNO");
            }
        }
        // levo
        if (j - 1 >= 0) {
            if (crke[i][j - 1] == izbrana && path[i][j - 1] != 1) {
                path[i][j - 1] = 1;
                stuck = 0;
                levo = stejKorake(crke, izbrana, i, j - 1, koraki + 1, path,
                        potka == "" ? potka + "LEVO" : potka + "," + "LEVO");
            }
        }
        // gor
        if (i - 1 >= 0) {
            if (crke[i - 1][j] == izbrana && path[i - 1][j] != 1) {
                path[i - 1][j] = 1;
                stuck = 0;
                gor = stejKorake(crke, izbrana, i - 1, j, koraki + 1, path,
                        potka == "" ? potka + "GOR" : potka + "," + "GOR");
            }
        }
        // dol
        if (i + 1 != crke.length) {
            if (crke[i + 1][j] == izbrana && path[i + 1][j] != 1) {
                path[i + 1][j] = 1;
                stuck = 0;
                dol = stejKorake(crke, izbrana, i + 1, j, koraki + 1, path,
                        potka == "" ? potka + "DOL" : potka + "," + "DOL");
            }
        }
        Poskus obj = new Poskus(koraki, potka);
        if (stuck == 1) {
            path[i][j] = 0;
            return obj;
        }
        path[i][j] = 0;
        if (desno.koraki >= levo.koraki) {
            if (desno.koraki >= gor.koraki) {
                if (desno.koraki >= dol.koraki) {
                    return desno;
                }
                return dol;
            } else if (gor.koraki >= dol.koraki) {
                return gor;
            }
            return dol;
        } else if (levo.koraki >= gor.koraki) {
            if (levo.koraki >= dol.koraki) {
                return levo;
            }
            return dol;
        }
        return gor;
    }

    public static Cache rek (int x, int y, char[][] visited, int len) {

        Cache u=null, d=null, l=null, r=null;
        char[][] mask = new char[visited.length][visited[0].length];
        System.arraycopy(visited, 0, mask, 0, mask.length);
        mask[x][y] = ' ';

        // up
        if((y-1)>=0 && grid[x][y] == (visited[x][y-1])){
            u = rek(x, y-1, mask, len + 1);
        }

        // down
        if((y+1)<yLen && grid[x][y]==(visited[x][y+1])){
            d = rek(x, y+1, mask, len + 1);
        }

        // left
        if((x-1)>=0 && grid[x][y]==(visited[x-1][y])){
            l = rek(x-1, y, mask, len + 1);
        }

        // right
        if((x+1)<xLen && grid[x][y]==(visited[x+1][y])){
            r = rek(x+1, y, mask, len + 1);
        }

        // cache our thingey
        Cache best = maxCache(new Cache[]{u,d,l,r, new Cache("", len)});
        //used.push(key, best);
        return best;
    }

    public static Cache rek_path (int x, int y, char[][] visited, int len, String path){

        Cache u=null, d=null, l=null, r=null;
        char[][] mask = new char[visited.length][visited[0].length];
        System.arraycopy(visited, 0, mask, 0, mask.length);
        mask[x][y] = ' ';

        // up
        if((y-1)>=0 && grid[x][y] == (visited[x][y-1])){
            u = rek_path(x, y-1, mask, len + 1, path+" GOR");
        }

        // down
        if((y+1)<yLen && grid[x][y]==(visited[x][y+1])){
            d = rek_path(x, y+1, mask, len + 1, path+" DOL");
        }

        // left
        if((x-1)>=0 && grid[x][y]==(visited[x-1][y])){
            l = rek_path(x-1, y, mask, len + 1, path+" LEVO");
        }
        // right
        if((x+1)<xLen && grid[x][y]==(visited[x+1][y])){
            r = rek_path(x+1, y, mask, len + 1, path+" DESNO");
        }
        // cache our thingey
        Cache best = maxCache(new Cache[]{u,d,l,r, new Cache(path, len)});
        //used.push(key, best);
        return best;
    }

    public static void debugMe(char[][] grid){
        System.out.println();
        for (int y = 0; y < yLen; y++) {
            for (int x = 0; x < xLen; x++) {
                System.out.print(grid[x][y]);
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
}
