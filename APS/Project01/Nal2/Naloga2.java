import java.io.*;
import java.security.Guard;
import java.util.concurrent.CountDownLatch;
public class Naloga2 {
    static String[][] grid;
    static int xLen, yLen, xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE,       lenMax = Integer.MIN_VALUE;
    static String pathMax = "";
    static hashTable used;

    enum Dir {
        Up, Down, Left, Right, Omni
    }

    static class LinkNode {
        Cache data;
        LinkNode next;
        LinkNode prev;

        public LinkNode(Cache item) {
            this.data = item;
        }
    }

    static class LinkedList {
        LinkNode head;

        public LinkedList(Cache item) {
            this.head = new LinkNode(item);
            this.head.prev = null;
            this.head.next = null;
        }

        public void add(Cache item) {
            // lets just add it at the beginning
            LinkNode temp = new LinkNode(item);
            temp.next = this.head;
            temp.prev = null;
            this.head.prev = temp;
            this.head = temp;
        }

        public void remove(LinkNode node) {
            // if its first
            if (node.prev == null) {
                this.head = node.next;
                return;
            }

            // else
            node.prev.next = node.next;
            node = null;
            return;
        }
    }

    static class hashTable {
        LinkedList[] db;

        public hashTable(int len) {
            this.db = new LinkedList[len];
        }

        protected int hash (CacheKey key){
            return this.fancyMod(37*key.pos[0] + 59*key.pos[1] + 61*key.direction.hashCode());
        }

        public void push(CacheKey key, Cache val){
            val.key = key;
            if(db[this.hash(key)] == null){
                // frst entry in this cell
                db[this.hash(key)] = new LinkedList(val);
            }else{
                db[this.hash(key)].add(val);
            }
        }

        protected int fancyMod(int i){
            return ((this.db.length + i%this.db.length)%this.db.length);
        }

        public Cache get (CacheKey key){
            if(this.db[this.hash(key)] == null){
                return null;
            }
            LinkNode item = this.db[this.hash(key)].head;
            do {
                if(item.data.key.equals(key)){
                    return item.data;
                }
                item = item.next;
            } while (item != null);
            return null;
        }
    }
    static class CacheKey {
        protected Dir direction = Dir.Up;
        protected int[] pos = new int[2];

        public CacheKey(Dir dir, int[] pos){
            this.direction = dir;
            this.pos = pos;

        }

        public boolean equals(CacheKey obj) {
            if(obj.direction == this.direction){
                if(obj.pos[0] == this.pos[0] && obj.pos[1] == this.pos[1]){
                    return true;
                }
            }
            return false;
        }
    }

    static class Cache {
        public CacheKey key;
        int length = 0;
        String path = "";

        public Cache(){}
        public Cache(String path, int length){
            this.path = path;
            this.length = length;
        }
    }

    public static String[][] Copy(String[][] in){
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
        FileReader fileIn = new FileReader(args[0]);
        //FileReader fileIn = new FileReader("in.txt");

        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        xLen = Integer.parseInt(line[0]); // road length
        yLen = Integer.parseInt(line[1]); // max tank
        grid = new String[xLen][yLen];

        for (int y = 0; y < yLen; y++) {
            String[] lineIn = reader.readLine().split(",");
            for (int x = 0; x < xLen; x++) {
                grid[x][y] = lineIn[x];
            }
        }
        reader.close();

        used = new hashTable(xLen*yLen);

        //debugMeDaddy(grid);

        Cache vn;
        for(int y = 0; y < yLen; y++){
            for( int x = 0; x < xLen; x++){
                // do stuff 

                vn = rek(x, y, Copy(grid), Dir.Omni, 1, "");
                if(vn.length > lenMax){
                    lenMax = vn.length;
                    pathMax = vn.path;
                }
            }
            //System.out.println(". ");
        }

        System.out.println("Max len: "+lenMax);
        System.out.println(pathMax);
    }

    public static void debugMeDaddy(String[][] grid){
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

    public static Cache rek (int x, int y, String[][] visited, Dir direction, int len, String path) {
        CacheKey key = new CacheKey(direction,new int[]{x,y});

        Cache out = used.get(key);
        if(out != null){
            //System.out.println("cached");
            return out;
        }
        //System.out.println("nocache");

        Cache u=null, d=null, l=null, r=null;
        String[][] mask = Copy(visited);
        mask[x][y] = "";

        // up
        if((y-1)>=0 && grid[x][y].equals(visited[x][y-1])){
            u = rek(x, y-1, mask, Dir.Up, len + 1, path+",GOR");
        }

        // down
        if((y+1)<yLen && grid[x][y].equals(visited[x][y+1])){
            d = rek(x, y+1, mask, Dir.Down, len + 1, path+",DOL");
        }

        // left
        if((x-1)>=0 && grid[x][y].equals(visited[x-1][y])){
            l = rek(x-1, y, mask, Dir.Left, len + 1, path+",LEVO");
        }
        // right
        if((x+1)<xLen && grid[x][y].equals(visited[x+1][y])){
            r = rek(x+1, y, mask, Dir.Right, len + 1, path+",DESNO");
        }
        // cache our thingey
        Cache best = maxCache(new Cache[]{u,d,l,r, new Cache(path, len)});
        used.push(key, best);
        return best;
    }

}
