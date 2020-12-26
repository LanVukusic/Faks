import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class Edge {
    public double price;
    public int endIndex;

    public Edge(double price, int nextNode) {
        this.price = price;
        this.endIndex = nextNode;
    }
}

class GraphNode {
    public ArrayList<Edge> children;
    public int index;
    public double v;
    public double h; // nek heuristic garbage karkoli to je
    public int visitedInIteration;

    public GraphNode(int index) {
        this.index = index;
        this.children = new ArrayList<>();
        this.h = 0; // okay...to je ocena dolzine poti do cilja. Konvergira proti legit ceni
        this.v = 0;
        this.visitedInIteration = -1;
    }
}


/**
 * Naloga10
 */
public class Naloga10 {
    static int P;
    static int pathStart, pathEnd;
    static ArrayList<GraphNode> cities;

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader bufferedReader = new BufferedReader(fileIn);
        // read first time
        String[] line;
        P = Integer.parseInt(bufferedReader.readLine());

        // create an array of nodes
        cities = new ArrayList<>(P * 2);
        cities.add(new GraphNode(0));
        int highestId = 0;

        // link cities with edges
        for (int i = 0; i < P; i++) {
            line = bufferedReader.readLine().split(",");
            int cityFrom = Integer.parseInt(line[0]);
            int cityyTo = Integer.parseInt(line[1]);
            int price = Integer.parseInt(line[2]);

            // we add this many new cities
            while (cityFrom > highestId) {
                highestId++;
                cities.add(new GraphNode(highestId));
                
            }
            while (cityyTo > highestId) {
                highestId++;
                cities.add(new GraphNode(highestId));
            }

            cities.get(cityFrom).children.add(new Edge(price, cityyTo)); // add one direction
        }

        // read second lineln
        line = bufferedReader.readLine().split(",");
        pathStart = Integer.parseInt(line[0]);
        pathEnd = Integer.parseInt(line[1]);
        bufferedReader.close();
        // end file reading

        // START SOLVING

        // ta buraz iz 6. naloge bo tle kr jak
        int iter = 0;
        boolean updated = true;
        GraphNode currentOrigin;

        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);
       
        ArrayList<Integer> out = new ArrayList<>();
        while (updated) {
            updated = false;
            currentOrigin = cities.get(pathStart);
            out.clear();
            while (true) {
                //System.out.printf("%d,", currentOrigin.index);
                out.add(currentOrigin.index);
                currentOrigin.visitedInIteration = iter;  // sets a flag so we dont revisit this city in this iteration
                Edge next = bestConn(currentOrigin.children, iter); // gets best city to visit next
                
                if(currentOrigin.index == pathEnd){  // we have reached the end
                    break;
                }
                if(next != null){  // check if we can move
                    System.out.printf("Best option is %d\n", next.endIndex);
                    double v_b = next.price + cities.get(next.endIndex).h;
                    if(cities.get(next.endIndex).h > v_b){
                        System.out.println("bigoofff");
                        return;
                    }
                    if (currentOrigin.h < v_b) {  // check if we have to update the cost of a node we are standing on
                        System.out.printf("updatamo ceno iz %f v %f\n", currentOrigin.h, v_b);
                        currentOrigin.h = v_b; // we update the heuristic
                        updated = true;
                    }
                    // move to the next node
                    System.out.printf("gremo iz %d v %d\n", currentOrigin.index, next.endIndex);
                    if(next.endIndex != cities.get(next.endIndex).index){
                        System.out.println("big oof");
                        return;
                    }
                    currentOrigin = cities.get(next.endIndex);
                    
                }else{  //we can't move
                    currentOrigin.h = Double.POSITIVE_INFINITY;
                    break;
                }
            }

            // printing to file section
            for (int i = 0; i < out.size() - 1; i++) {
                bufferedWriter.append(String.valueOf(out.get(i))+",");
            }
            bufferedWriter.append(""+out.get(out.size() - 1)+"\n");

            // set new iteration flag
            iter++;
        }
        bufferedWriter.close();
    }

    static Edge bestConn(ArrayList<Edge> arr, int forbiddenIter) {
        Edge out = null;
        double outprice = Double.POSITIVE_INFINITY;
        double index = Double.POSITIVE_INFINITY;

        for (Edge edge : arr) {

            if (cities.get(edge.endIndex).visitedInIteration == forbiddenIter){
                continue;
            }

            // check for price
            double price0 = edge.price + cities.get(edge.endIndex).h;
            if (price0 == outprice) {
                // account for same price case
                if (edge.endIndex < index) {
                    out = edge;
                    outprice = price0;
                    index = edge.endIndex;
                    continue;
                }
            }

            if (price0 < outprice) {
                out = edge;
                outprice = price0;
                index = edge.endIndex;
                continue;
            }
        }

        return out;
    }

}