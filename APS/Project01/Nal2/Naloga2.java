import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

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
            return (37*key.pos[0] + 59*key.pos[1] + 61*key.direction.hashCode()) % this.db.length;
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

        public Cache get (CacheKey key){
            LinkNode item = this.db[this.hash(key)].head;
            do {
                if(item.data.key == key){
                    return item.data;
                }
                item = item.next;
            } while (item != null);
            return null;
        }
   
    }

    static class CacheKey {
        Dir direction = Dir.Up;
        int[] pos = new int[2];

        public CacheKey(Dir dir, int[] pos){
            this.direction = dir;
            this.pos = pos;

        }
    }

    static class Cache {
        public CacheKey key;
        int length = 0;
        String path = "";
    }

    public static String[][] Copy(String[][] in){
        String[][] out = new String[in.length][in[0].length];

        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                out[i] = in[i];
            }
        }
        return out;
    }

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
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

        //used = new hashTable(xLen*yLen);

        for(int y = 0; y < yLen; y++){
            for( int x = 0; x < xLen; x++){
                // do shit
                System.out.println(""+x+y);
                rek(x, y, Copy(grid), Dir.Omni, 0, "");
            }
        }
        //System.out.println(grid[1][0])
        System.out.println("Max len: "+lenMax);
        System.out.println(pathMax);
    }

    public static void rek(int x, int y, String[][] visited, Dir direction, int len, String path) {
        if(len > lenMax){
            lenMax = len;
            pathMax = path;
        }

        //System.out.println(grid[x][y]);

        //System.out.println(visited[x][y]);
        String[][] mask = Copy(visited);
        mask[x][y] = "";

        // up
        if((y-1)>=0 && grid[x][y] == visited[x][y-1]){
            rek(x, y-1, mask, Dir.Up, len + 1, path+",GOR");
            //System.out.println(len);
        }
        
        // down
        if((y+1)<yLen && grid[x][y] == visited[x][y+1]){
            rek(x, y+1, mask, Dir.Down, len + 1, path+",DOL");
            //System.out.println(len);
        }

        // left
        if((x-1)>=0 && grid[x][y] == visited[x-1][y]){
            rek(x-1, y, mask, Dir.Left, len + 1, path+",LEVO");
            //System.out.println(len);
        }

        // right
        if((x+1)<xLen && grid[x][y] == visited[x+1][y]){
            rek(x+1, y, mask, Dir.Right, len + 1, path+",DESNO");
            //System.out.println(len);
        }
        
        /*
        // if it's not calculated yet
        if(used.get(new CacheKey(direction, new int[]{x,y})) != null){
            String[][] mask = visited.clone();
            mask[x][y] = "";

            // up
            if(direction != Dir.Down && (y-1)>=0 && grid[x][y-1] == visited[x][y]){
                rek(x, y-1, mask, Dir.Up, len + 1, path+", UP");
            }
            
            // down
            if(direction != Dir.Up && (y+1)<yLen && grid[x][+1] == visited[x][y]){
                rek(x, y+1, mask, Dir.Down, len + 1, path+", DOWN");
            }

            // left
            if(direction != Dir.Right && (x-1)>=0 && grid[x-1][y] == visited[x][y]){
                rek(x-1, y, mask, Dir.Left, len + 1, path+", LEFT");
            }

            // right
            if(direction != Dir.Down && (x+1)<xLen && grid[x+1][y] == visited[x][y]){
                rek(x, y-1, mask, Dir.Right, len + 1, path+", RIGHT");
            }
        }else{
            int pLen = used.get(new CacheKey(direction, new int[]{x,y})).length;
            if(pLen + len > lenMax){
                lenMax = pLen + len;
                pathMax = path+used.get(new CacheKey(direction, new int[]{x,y})).path;
            }
            return;
        }

        */
    }

}
