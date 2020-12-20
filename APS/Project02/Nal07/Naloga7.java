/**
 * Naloga7
 */
public class Naloga7 {
    static int nodesCount, edgesCount;
    static int pathStart, pathEnd;

    public static void main(String[] args) {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader bufferedReader = new BufferedReader(fileIn);
        // read first time
        String[] line = bufferedReader.readLine().split(" ");
        nodesCount = Integer.parseInt(line[0]);
        edgesCount = Integer.parseInt(line[1]);

        // read second lineln
        line = bufferedReader.readLine().split(" ");
        pathStart = Integer.parseInt(line[0]);
        pathEnd = Integer.parseInt(line[1]);
        
        // create an array of nodes
        cities = new GraphNode[nodesCount];
        for (int i = 0; i < N; i++) {
            cities[i] = new GraphNode(S+1);
        }
        
        bufferedReader.close();
        // end file reading
    }

}