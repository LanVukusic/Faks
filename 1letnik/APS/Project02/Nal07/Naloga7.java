import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.TreeSet;
import java.io.*;

// NEDELA: 
// 2, 5
class Edge implements Comparable<Edge> {
    public int from;
    public int to;

    /*
     * Creates an undirected edge between vertices
     */
    public Edge(int node_1, int node_2) {
        this.from = Math.min(node_1, node_2);
        this.to = Math.max(node_1, node_2);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge otherNode = (Edge) other;
        return (this.from == otherNode.from && this.to == otherNode.to);
    }

    @Override
    public int hashCode() {
        return this.from * 6451 + this.to * 7907;
    }

    @Override
    public int compareTo(Edge other) {
        if (this.from != other.from) {
            return this.from - other.from;
        } else {
            return this.to - other.to;
        }
    }
}

class Node {
    int index;
    boolean visited;
    ArrayList<Integer> children;
    public Node path_prev;

    public Node(int index) {
        this.index = index;
        this.children = new ArrayList<>();
    }

    public void deleteConn(int index, Node[] a) {
        // delete one way
        this.children.set(this.children.indexOf((Integer) index), -1);
        // delete other way
        a[index].children.set(a[index].children.indexOf((Integer) this.index), -1);
    }
}

/**
 * Naloga7
 */
public class Naloga7 {

    static int nodesCount, edgesCount;
    static int pathStart, pathEnd;
    static Node[] nodes;
    static int time = 0;
    static TreeSet<Edge> rek2Out;

    public static void main(String[] args) throws IOException {
        // init
        rek2Out = new TreeSet<Edge>();

        // red file
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
            // System.out.println("adding: "+i);
        }

        // connect nodes
        for (int i = 0; i < edgesCount; i++) {
            line = bufferedReader.readLine().split(" ");
            int A = Integer.parseInt(line[0]);
            int B = Integer.parseInt(line[1]);
            // System.out.printf("from: %d to: %d \n",A,B);
            nodes[A].children.add(B);
            nodes[B].children.add(A);
        }

        // over line reading
        bufferedReader.close();

        // get all the nodes in a path
        TreeSet<Edge> out = null;
        ArrayDeque<Integer> q = new ArrayDeque<Integer>();
        q.add(pathStart);
        boolean[] visited = new boolean[nodes.length];
        visited[pathStart] = true;
        // BFS
        while (true) {
            Node n = nodes[q.pollLast()]; // poll one element from db
            visited[n.index] = true;
            if (n.index == pathEnd) {
                break;
            }
            for (Integer c : n.children) {
                if (!visited[c]) {
                    nodes[c].path_prev = n;
                    q.add(c);
                    visited[c] = true;
                }
            }
        }

        // create a unique edge set from path
        Node n = nodes[pathEnd];
        out = new TreeSet<>();
        while (n != null) {
            if (n.index == pathStart) {
                break;
            }
            out.add(new Edge(n.index, n.path_prev.index));
            n = n.path_prev;
        }

        // // print path
        // for (Edge e : out) {
        // System.out.printf("%d %d\n", e.from, e.to);
        // }

        // Mark all the vertices as not visited
        int V = nodes.length;
        int NIL = -1;

        visited = new boolean[V];
        int disc[] = new int[V];
        int low[] = new int[V];
        int parent[] = new int[V];

        // Initialize parent and visited, and ap(articulation point)
        // arrays
        for (int i = 0; i < V; i++) {
            parent[i] = NIL;
            visited[i] = false;
        }

        // Call the recursive helper function to find Bridges
        // in DFS tree rooted with vertex 'i'

        for (int i = 0; i < V; i++) {
            if (visited[i] == false) {
                bridgeUtil(i, visited, disc, low, parent);
            }
        }

        rek2Out.retainAll(out);
        // nej zracuna se enkrt tele večje majnše ka je shitshow

        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);

        for (Edge e : rek2Out) {
            // System.out.println(""+e.from+" - "+e.to);
            bufferedWriter.append("" + e.from + " " + e.to + "\n");
        }
        bufferedWriter.close();
    }

    public static void bridgeUtil(int u, boolean visited[], int disc[], int low[], int parent[]) {

        // Mark the current node as visited
        visited[u] = true;

        // Initialize discovery time and low value
        disc[u] = low[u] = ++time;

        // Go through all vertices aadjacent to this
        Iterator<Integer> i = nodes[u].children.iterator();
        while (i.hasNext()) {
            int v = i.next(); // v is current adjacent of u

            // If v is not visited yet, then make it a child
            // of u in DFS tree and recur for it.
            // If v is not visited yet, then recur for it
            if (!visited[v]) {
                parent[v] = u;
                bridgeUtil(v, visited, disc, low, parent);

                // Check if the subtree rooted with v has a
                // connection to one of the ancestors of u
                low[u] = Math.min(low[u], low[v]);

                // If the lowest vertex reachable from subtree
                // under v is below u in DFS tree, then u-v is
                // a bridge
                if (low[v] > disc[u]) {
                    // System.out.println(""+u+" "+v);
                    rek2Out.add(new Edge(u, v));
                }

            }

            // Update low value of u for parent function calls.
            else if (v != parent[u])
                low[u] = Math.min(low[u], disc[v]);
        }
    }
}