import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;

class Node {
    int index;
    boolean visited;
    ArrayList<Integer> children;

    public Node(int index){
        this.index = index;
        this.children = new ArrayList<>();
    }

    public void deletechild (int index){
        this.children.set(this.children.indexOf((Integer)index), -1);
    }
}

/**
 * Naloga7
 */
public class Naloga7 {
    static int nodesCount, edgesCount;
    static int pathStart, pathEnd;
    static Node[] nodes;

    public static void main(String[] args) throws IOException{
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader bufferedReader = new BufferedReader(fileIn);
        // read first time
        String[] line = bufferedReader.readLine().split(" ");
        nodesCount = Integer.parseInt(line[0]);
        edgesCount = Integer.parseInt(line[1]);

        // read second line
        line = bufferedReader.readLine().split(" ");
        pathStart = Integer.parseInt(line[0]);
        pathEnd = Integer.parseInt(line[1]);

        // create an array of nodes

        // build graph nodes
        nodes = new Node[nodesCount];
        for (int i = 0; i < nodesCount; i++) {
            nodes[i] = new Node(i);
            //System.out.println("adding: "+i);
        }

        // connect nodes
        for (int i = 0; i < edgesCount; i++) {
            line = bufferedReader.readLine().split(" ");
            int A = Integer.parseInt(line[0]);
            int B = Integer.parseInt(line[1]);
            //System.out.printf("from: %d to: %d \n",A,B);
            nodes[A].children.add(B);
            nodes[B].children.add(A);
        }

        //over line reading
        bufferedReader.close();
        //start solving
        //System.out.printf("starting with %d connections\n", getConnections(nodes));

        ArrayList<Integer> pathNodes = rek(nodes[pathStart], pathEnd);
        for (Integer integer : pathNodes) {
            System.out.println(integer);
        }
        ArrayList<int[]> pairs = new ArrayList<>();

        System.out.printf("starting with %d connections\n", getConnections(nodes));
        
        // get only needed connections

        // 1. get connections from last (-1. )
        int startIndex = pathNodes.get(0);
        int endIndex = pathNodes.get(pathNodes.size() - 1);
        System.out.println("endindex:" + endIndex);
        while (startIndex != endIndex){
            
            int a = rek2(nodes[startIndex], pathNodes, startIndex);
            System.out.printf("searching from %d ret: %d\n", startIndex, a);
            if(a == -1){ // if the function fails to find a path
                removeTill(startIndex, pathNodes,true);
                pairs.add(new int[]{startIndex,pathNodes.get(0)});
                //System.out.printf("%d : %d\n",startIndex,pathNodes.get(0));
            }else{
                // remove all connection that it had found. if it found 3;  we can remove 1, 2, 3
                removeTill(a, pathNodes, false);
                // if(a == endIndex){
                //     break;
                // }
            }
            startIndex = pathNodes.get(0);
        
        }

        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);
        for (int i = pairs.size() - 1; i >= 0; i--) {
            if(pairs.get(i)[0] < pairs.get(i)[1]){
                bufferedWriter.append(""+pairs.get(i)[0]+" "+pairs.get(i)[1]+"\n");
            }else{
                bufferedWriter.append(""+pairs.get(i)[1]+" "+pairs.get(i)[0]+"\n");
            }
            
        }
        bufferedWriter.close();
    }

    /*
    * REK
    * Recursivly run through the graph and delete connections after
    */
    static ArrayList<Integer> rek(Node start, int stopIndex){
        // we mustn't start on a visited node
        if (start.visited){
            return null;
            //start.children.clear();
        }

        if(start.index == stopIndex){
            ArrayList<Integer> n = new ArrayList<Integer>();
            n.add(stopIndex);
            //start.visited = -2;
            return n;
        }

        // tag it so we dont get cycles
        start.visited = true;
        //System.out.println(start.index);

        // visit all the children
        ArrayList<Integer> a;
        for (Integer childIndex : start.children){
            if (childIndex != -1){
                a = rek(nodes[childIndex], stopIndex);
                if(a != null){
                    nodes[childIndex].deletechild(start.index);
                    start.deletechild(childIndex);
                    a.add(start.index);
                    return a;
                }
            }
        }
        start.visited = false;
        return null;
        
    }

    // REK2
    static int rek2(Node start, ArrayList<Integer> legal, int ignore){
        // iterate over children and try to find the match in array
        for (int i = 0; i < start.children.size(); i++) {
            int lol = start.children.get(i);
            for (Integer legalEntry : legal) {
                if (lol != -1 && lol == (int)legalEntry && lol != ignore){
                    return lol;
                }
            }
        }
        return -1;
    }

    static int getConnections(Node[] a){
        int out = 0;
        for (Node node : a) {
            for (Integer i : node.children) {
                if(i != -1){
                    out++;
                    //if(out%2 ==0) System.out.printf("%d : %d\n",node.index, i);
                }
                 
            }
        }
        return out/2;
    }

    static void removeTill(int stopInclusive, ArrayList<Integer> arr, boolean inclusive){
        ArrayList<Integer> temp = new ArrayList<>();
        for (Integer integer : arr ) {
            if(integer == stopInclusive){  // if we find it
                if(inclusive) temp.add(integer);
                break;
            }else{
                temp.add(integer);
            }
        }
        arr.removeAll(temp);
    }
}