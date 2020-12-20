import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;


class Edge{
    public int pathLenToNext;
    public int endIndex;

    public Edge(int price, int node){
        this.pathLenToNext = price;
        this.endIndex = node;
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
        priQ.add(new PriqObject(pathStart, 0, cities[pathStart].color));
        int currBest = Integer.MAX_VALUE;

        // infinte while loop. check every top element of priQ
        while (true){
            // get current node
            PriqObject currentNode = priQ.poll();
            //System.out.println(currentNode.pathLength);

            // check the exiting condition
            if (currentNode.pathLength > currBest){
                break;
            }

            // get all of its children indexes
            childrenLoop:
            for (Edge priqObject : cities[currentNode.index].children) {
                // the actual node is "cities[priqObject.endIndex]"
                

                // if any current or child are of color 0; or they have the same color, we are cool
                if ( currentNode.color == 0 || cities[priqObject.endIndex].color == 0 || currentNode.color !=  cities[priqObject.endIndex].color ){
                    // compute new path length
                    int newLength = currentNode.pathLength + priqObject.pathLenToNext;
                    System.out.println("" + priqObject.endIndex + " OK");

                    // check if you can find the end node
                    if (priqObject.endIndex == pathEnd){
                        currBest = newLength;
                        // System.out.println("najdu brttt");
                        // System.out.println(currBest);
                        return;
                    }

                    // if we already have a better price for this particular color
                    if(cities[priqObject.endIndex].visitedPerColor[currentNode.color] < newLength){
                        continue;
                    }else{
                        cities[priqObject.endIndex].visitedPerColor[currentNode.color] = newLength;
                        priQ.add(new PriqObject(priqObject.pathLenToNext, newLength, currentNode.color));
                    }
                    
                    // find the possibly better options
                    // for (PriqObject iterObject : priQ) {
                    //     if ( iterObject.index == priqObject.endIndex && iterObject.color == cities[priqObject.endIndex].color){
                    //         if (newLength < iterObject.pathLength) {
                    //             priQ.remove();
                    //         }
                    //         continue childrenLoop;
                    //     }
                    // }

                    priQ.add(new PriqObject(priqObject.pathLenToNext, newLength, currentNode.color));
                }
            }
        }

        // check the priority queue

        // while (true) {
        //     PriqObject curr = priQ.poll();

        //     // break will get triggered when we find the best path
        //     // we than only go through ne next few that might get us better off
        //     if (curr.pathLength > currBest){
        //         break;
        //     }

        //     //check the children and see what's going on
        //     for (Edge viewedChildEdge : cities[curr.index].children) {
             
        //         // check if we are color blocked
        //         // if we are okay, continue exploration
        //         if ( cities[viewedChildEdge.endIndex].color == 0 || curr.color == 0 ||  cities[viewedChildEdge.endIndex].color != curr.color){
        //             int newLength = curr.pathLength + viewedChildEdge.pathLenToNext;

        //             //check if we found the finish
        //             if (viewedChildEdge.endIndex == pathEnd){
        //                 System.out.println("found");
        //                 currBest = newLength;
        //                 System.out.println(newLength);
        //                 return;
        //                 //continue;
        //             }  

        //             // check if we have the occurance in priorityQ
        //             // that potentialy has a shorter route
        //             // possible speedup: priQ is sorted so we could serch much faseter... will we? nope ..no time
        //             for (PriqObject priqObject : priQ) {
        //                 if(priqObject.index == viewedChildEdge.endIndex && priqObject.color == cities[viewedChildEdge.endIndex].color && priqObject.pathLength> newLength){
        //                     priqObject.pathLength = newLength;
        //                     continue;
        //                 }
        //             }
        //             // if not, we just add it to the priority queue
        //             //System.out.println(newLength);
        //             priQ.add(new PriqObject(viewedChildEdge.endIndex, newLength, cities[curr.index].color));
        //         }
        //     }
        // }

        System.out.println(currBest);

    }
}