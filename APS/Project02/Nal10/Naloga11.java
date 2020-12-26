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
public class Naloga11 {
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
        boolean noChange;
        while(true){
            int indeks = pathStart;

            if (noChange == true){  // smo koncali
                break;
            }
            
            naKoncu = 0;
            // System.out.print(start + ",");
            pr.print(start);
            int[] arrObiskanih = new int[finnish];
            

            while(naKoncu == 0){
                int imaIzhode = 0;
                Edge min = null;
                s = 0;
                for (Edge i : g.adjList[indeks]) {
                    arrObiskanih[indeks - 1] = 1;  // do tle lahko ne pride ce je vozlisce brez izhodov
                    imaIzhode = 1;
                    if (s == 0 && arrObiskanih[i.key -1] != 1){
                        min = i;
                        s = 1;
                    }
                    else if (s == 0 && arrObiskanih[i.key -1] == 1){
                        continue;
                    }
                    else if ((i.price + ocene[i.key - 1]) < (min.price + ocene[min.key - 1]) && arrObiskanih[i.key - 1] != 1){  // če ima trenutno vozlisce bol poceni pot in ga nismo se obiskali postane ta min
                        min = i;
                    }
                    else if (i.price == min.price && arrObiskanih[i.key - 1] != 1){  // če IMASTA LOL enako ceno ju primerjamo po id-ju
                        if (i.key < min.key){
                            min = i;
                        }
                    }
                }
                

                if (s != 0 && min.key == finnish){
                    System.out.printf("Best option is %d\n", min.key);
                    // System.out.print(min.key);
                    pr.print("," + min.key);
                    naKoncu = 1;
                    s = 1;
                }
                else if (s != 0){
                    // System.out.print(min.key + ",");
                    pr.print("," + min.key);
                }

                if (imaIzhode != 1 && s != 1){  // če iz vozlisca ni nobene povezave in ni koncno
                    noChange = 0;
                    ocene[indeks - 1] = Integer.MAX_VALUE;
                    break;
                }
                else if (s != 1){  // če ima najmanj en izhod a so ze vsa obiskana
                    break;
                }
                else if (ocene[indeks - 1] < (min.price + ocene[min.key - 1])){  // ce je potrebno posodobiti hevristicno oceno  *** spremenili iz != na <
                    noChange = 0;       // naredili smo spremembo tako da ni to zadnja iteracija
                    System.out.printf("updatamo ceno iz %d v %d\n", ocene[indeks - 1], min.price + ocene[min.key - 1]);
                    ocene[indeks - 1] =  min.price + ocene[min.key - 1];
                }
                System.out.printf("gremo iz %d v %d\n", indeks,  min.key);
                indeks = min.key;
                
            }
            // System.out.println();
            pr.println();
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