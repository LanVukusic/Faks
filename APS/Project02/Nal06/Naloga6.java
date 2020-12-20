import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;


class Edge{
    public int pathLenToNext;
    public int endIndex;

    public Edge(int price, int nextNode){
        this.pathLenToNext = price;
        this.endIndex = nextNode;
    }
}

class GraphNode{
    public int color;
    public ArrayList<Edge> children;
    public int[] visitedPerColor;

    public GraphNode(int colorcount){
        this.visitedPerColor = new int[colorcount];
        this.children = new ArrayList<>();
        for (int i = 0; i < visitedPerColor.length; i++) {
            this.visitedPerColor[i] = Integer.MAX_VALUE;
        }
    }
}

class PriqObject implements Comparable{
    public int index;
    public int pathLength;
    public int color;

    public PriqObject(int index, int cost, int color){
        this.index = index;
        this.color = color;
        this.pathLength = cost;
    }

    @Override
    public int compareTo(Object o){
        return this.pathLength - ((PriqObject) o).pathLength;
    }
}

/**
 * Naloga6
 */
public class Naloga6 {
    static int N, P, S;
    static int pathStart, pathEnd;
    static GraphNode[] cities;
    public static void main(String[] args) throws IOException{
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader bufferedReader = new BufferedReader(fileIn);
        // read first time
        String[] line = bufferedReader.readLine().split(" ");
        N = Integer.parseInt(line[0]);
        P = Integer.parseInt(line[1]);
        S = Integer.parseInt(line[2]);

        // read second lineln
        line = bufferedReader.readLine().split(" ");
        pathStart = Integer.parseInt(line[0]);
        pathEnd = Integer.parseInt(line[1]);
        
        // create an array of nodes
        cities = new GraphNode[N];
        for (int i = 0; i < N; i++) {
            cities[i] = new GraphNode(S+1);
        }
        
        // link cities with edges
        for (int i = 0; i < P; i++) {
            line = bufferedReader.readLine().split(" ");
            int cityFrom = Integer.parseInt(line[0]);
            int cityyTo  = Integer.parseInt(line[1]);
            int price = Integer.parseInt(line[2]);
            cities[cityFrom].children.add(new Edge(price, cityyTo)); // add one direction
            cities[cityyTo].children.add(new Edge(price, cityFrom)); // add anothe direction
        }

        // set group colors. Color 0 is unique all others are legit colors
        for (int i = 0; i < S; i++) {
            line = bufferedReader.readLine().split(" ");
            for (String s : line) {
                int index = Integer.parseInt(s);
                cities[index].color = i+1;
            }
        }
        bufferedReader.close();
        // end file reading

        // start problem solving
        // priority queue
        PriorityQueue<PriqObject> priQ = new PriorityQueue<>();
        priQ.add(new PriqObject(pathStart, 0, 0));

        for (int i = 0; i < cities[pathStart].visitedPerColor.length; i++) {
            cities[pathStart].visitedPerColor[i] = 0;
        }

        PriqObject currentObj = priQ.poll();

        // while the last node doesnt bubble up to the top;
        while (currentObj.index != pathEnd){

            // iterate over children of current object
            // children being connections to other nodes
            for (Edge currEdge : cities[currentObj.index].children) {
                GraphNode nextCity = cities[currEdge.endIndex];

                if( nextCity.color == 0 || currentObj.color == 0 || nextCity.color != currentObj.color ){
                    int lastLength = nextCity.visitedPerColor[currentObj.color];
                    int newLength = currentObj.pathLength + currEdge.pathLenToNext;

                    if( newLength < lastLength){
                        priQ.add(new PriqObject(currEdge.endIndex, newLength, currentObj.color));
                        nextCity.visitedPerColor[currentObj.color] = newLength;
                    }
                }
                
            }

            currentObj = priQ.poll();
        }
        System.out.println(currentObj.pathLength);
    }
}