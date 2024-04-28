import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

class Node{
    public char name;
    public PriorityQueue<Integer> children;
    int index;

    public Node(char name){
        this.children = new PriorityQueue<>();
        this.name = name;
    }

    public void addChild(int child){
        this.children.add((Integer)child);
    }
}
class Graph{
    public Node[] nodes;

    public Graph(int nodeCount){
        nodes = new Node[nodeCount];
    }

    public void addNode(int index, Node n){
        this.nodes[index - 1] = n;
        n.index = index;
    }

    public Node getNode(int index){
        return this.nodes[index - 1];
    }
}

public class Naloga8 {
    static Graph T; // drevo
    static Graph P; // vzorec

    static int t_length;
    static int p_length;

    public static void main(String[] args) throws IOException{
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        //FileReader fileIn = new FileReader("t6.txt");
        BufferedReader bufferedReader = new BufferedReader(fileIn);


        String line = bufferedReader.readLine();
        p_length = Integer.parseInt(line);
        P = new Graph(p_length);
        // further
        for (int i = 0; i < p_length; i++) {
            String[] lineM = bufferedReader.readLine().split(",");

            Node a = new Node(lineM[1].charAt(0)); 
            P.addNode(Integer.parseInt(lineM[0]), a);

            // add children
            for (int j = 2; j < lineM.length; j++) {
                a.addChild(Integer.parseInt(lineM[j]));
            }
        }

        
        // do stuff for graph T
        line = bufferedReader.readLine();
        t_length = Integer.parseInt(line);
        T = new Graph(t_length);
        // read lines for graph T
        for (int i = 0; i < t_length; i++) {
            String[] lineM = bufferedReader.readLine().split(",");

            // add the node in the array
            Node a = new Node(lineM[1].charAt(0));
            T.addNode(Integer.parseInt(lineM[0]), a);

            // add children
            for (int j = 2; j < lineM.length; j++) {
                a.addChild(Integer.parseInt(lineM[j]));
            }
        }
        bufferedReader.close();
        // END FILE READING


        // START SOLVING PROBLEM
        int pattrens = 0;  // number of found pattrens ; gets returned at the end
        for(Node n : T.nodes){  // start in every possible position in big graph

            // for every node in BIG graph
            if(rekTree(P.nodes[0], n, P, T) ){  // check if pattren exists, originating from this position
                pattrens ++;  // if it does, we increment the counter by 1
            }
        }

        //Output to file / stdout - uncomment next line
        //System.out.println(pattrens);
        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);
        bufferedWriter.append(String.valueOf(pattrens));
        bufferedWriter.append("\n");
        bufferedWriter.close();

    }

    /*
    * rekTree returns a boolean value, describing wether a pattren starting in n1 is same as in n2
    */
    static boolean rekTree(Node n1, Node n2, Graph P, Graph T){
        // if pattren has 0 children its okay, since we find that the name matches; we return true
        if(n1.name == n2.name && n1.children.size() == 0){
            return true;
        }

        // if the child has more children we have to recursivly check the child trees for a match
        if(n1.name == n2.name && n1.children.size() == n2.children.size()){
            // we get two children lists and check every pair against another
            Iterator<Integer> n1_iter = n1.children.iterator();
            Iterator<Integer> n2_iter = n2.children.iterator();

            // we do that by **recursion** :O 
            for (int i = 0; i < n1.children.size(); i++) {
                if(! rekTree(P.getNode(n1_iter.next()), T.getNode(n2_iter.next()), P, T)){
                    return false;  // we only check if the process fails anyhow
                }
            }

        }else{
            return false;  // process fails if the names are not same or the child count is different, 
                           // while child count of n1 is not 0
        }
        return true; // nothing has failed, meaning it passed all completion tests; returning true, the pattren is a match
    }
}
