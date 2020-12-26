import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

       
        line = bufferedReader.readLine();
        t_length = Integer.parseInt(line);
        T = new Graph(t_length);
        // read second line and further
        for (int i = 0; i < t_length; i++) {
            String[] lineM = bufferedReader.readLine().split(",");

            Node a = new Node(lineM[1].charAt(0));
            T.addNode(Integer.parseInt(lineM[0]), a);

            // add children
            for (int j = 2; j < lineM.length; j++) {
                a.addChild(Integer.parseInt(lineM[j]));
            }
        }
        bufferedReader.close();

        int pattrens = 0;
        for(Node n : T.nodes){
            // za vsak node v velikem grafu
            if(rekTree(P.nodes[0], n, P, T) ){
                pattrens ++;
            }
        }

        System.out.println(pattrens);
    }

    static boolean rekTree(Node n1, Node n2, Graph P, Graph T){
        if(n1.name == n2.name && n1.children.size() == 0){
            return true;
        }
        if(n1.name == n2.name && n1.children.size() == n2.children.size()){
            Iterator<Integer> n1_iter = n1.children.iterator();
            Iterator<Integer> n2_iter = n2.children.iterator();

            for (int i = 0; i < n1.children.size(); i++) {
                if(! rekTree(P.getNode(n1_iter.next()), T.getNode(n2_iter.next()), P, T)){
                    return false;
                }
            }

        }else{
            return false;
        }
        return true;
    }
}
