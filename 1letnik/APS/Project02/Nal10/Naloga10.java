import java.io.IOException;
import java.io.*;
import java.util.LinkedList;
import java.nio.charset.StandardCharsets;

class Edge {
    public int price;
    public Node end;

    public Edge(int price, Node end_node) {
        this.price = price;
        this.end = end_node;
    }
}

class Node {
    public int id = 0;
    public LinkedList<Edge> children;
    public double heuristic = 0;
    public int visited_iter;

    public Node() {
        this.children = new LinkedList<>();
    }
}

public class Naloga10 {
    static int start_place = 0;
    static int stop_place = 0;

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        // FileReader fileIn = new FileReader("t6.txt");
        BufferedReader bufferedReader = new BufferedReader(fileIn);
        // read first time
        int num_reads = Integer.parseInt(bufferedReader.readLine());

        Node[] places = new Node[num_reads];

        for (int i = 0; i < num_reads; i++) {
            String[] a = bufferedReader.readLine().split(",");
            int from = Integer.parseInt(a[0]);
            int to = Integer.parseInt(a[1]);
            int price = Integer.parseInt(a[2]);

            if (places[from] == null) {
                places[from] = new Node();
                places[from].id = from;
            }
            if (places[to] == null) {
                places[to] = new Node();
                places[to].id = to;
            }
            places[from].children.add(new Edge(price, places[to]));
        }

        String[] line2 = bufferedReader.readLine().split(",");
        start_place = Integer.parseInt(line2[0]);
        stop_place = Integer.parseInt(line2[1]);

        // solve
        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter streamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);

        Node current_place;
        boolean tobreak = false;
        int iter = 0;
        String out = "";

        boolean eq2 = false;
        String before = "";

        while (true) {
            iter++;
            current_place = places[start_place];
            if (tobreak) {
                break;
            }
            tobreak = true;
            
            out = "";

            while (true) {
                current_place.visited_iter = iter;
                out += current_place.id+",";
                if (current_place.id == stop_place) {
                    break;
                }

                // get best option
                if(current_place.children.isEmpty()){
                    current_place.heuristic = Double.POSITIVE_INFINITY;
                    break;
                }

                Edge best = null;
                double vbb = 0;
                for (Edge n : current_place.children) {
                    if (n.end.visited_iter == iter) {
                        continue;
                    }
                    // v(b) = c(a,b) + h(b)
                    double v_b = n.price + n.end.heuristic;

                    if (best == null) {
                        best = n;
                        vbb = v_b;
                        continue;
                    }
                    
                    if (v_b < vbb) {
                        best = n;
                        vbb = v_b;
                        continue;
                    }
                    if (vbb == v_b) {
                        if (best.end.id > n.end.id) {
                            best = n;
                            vbb = v_b;
                            continue;
                        }
                    }
                }

                // check if there is a best option
                if (best == null) {
                    // update to inf
                    //current_place.heuristic = Double.POSITIVE_INFINITY;
                    break;
                }

                // we have a next node
                if(current_place.heuristic < vbb){
                    current_place.heuristic = vbb;
                    tobreak = false;
                }
                current_place = best.end;
            }
            if(out.equals(before)){
                eq2 = true;
            }
            before = out;
            bufferedWriter.append(out.substring(0, out.length()-1));
            bufferedWriter.append("\n");
        }

        // add doubled last line mybe
        if(!eq2){
            bufferedWriter.append(out.substring(0, out.length()-1));
            bufferedWriter.append("\n");
        }

        bufferedWriter.close();

    }
}
