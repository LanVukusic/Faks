import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class copi {
    public static void main(String[] args) throws Exception {
        String in_path = args[0];
        String out_path = args[1];
        // String in_path = "in.txt";
        // String out_path = "out.txt";

        FileReader in = new FileReader(in_path);
        BufferedReader br = new BufferedReader(in);
        FileOutputStream out = new FileOutputStream(out_path);
        OutputStreamWriter sw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(sw);

        int M = Integer.parseInt(br.readLine());

        /**
         * SETUP:
         */

        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Integer>curr_path = new ArrayList<Integer>();
        ArrayList<Integer> prev_path = new ArrayList<Integer>();
        int START;
        int END;

        /**
         * Input:
         */

        String[] line;
        int from;
        int to;
        int cost;

        Node a;
        Node b;

        for (int i = 0; i < M; i++) {
            line = br.readLine().split(",");

            from = Integer.parseInt(line[0]);
            to = Integer.parseInt(line[1]);
            cost = Integer.parseInt(line[2]);

            if (!nodes.containsKey(from)) {
                a = new Node(from);
                nodes.put(from, a);
            } else {
                a = nodes.get(from);
            }
            if (!nodes.containsKey(to)) {
                b = new Node(to);
                nodes.put(to, b);
            } else {
                b = nodes.get(to);
            }

            a.conns.add(new Povezava(to, b, cost));
        }

        line = br.readLine().split(",");
        START = Integer.parseInt(line[0]);
        END = Integer.parseInt(line[1]);

        br.close();

        /**
         * Execution iterations:
         */

        Node start_node = nodes.get(START);
        Node end_node = nodes.get(END);

        boolean equals = false;
        boolean checkEquals = false;
        int depth;

        int iteration = -1;
        boolean iterationDone;

        Node curr;
        Povezava next = null;

        int best_cost;

        String outStr;

        while(!equals){
            iteration++;
            depth = 0;
            //System.out.println("iteration: " + iteration);
            start_node.visited_on = iteration;
            curr = start_node;

            iterationDone = false;
            while(!iterationDone){
                outStr = String.valueOf(curr.id);
                bw.write(outStr);

                curr.visited_on = iteration;
                ////System.out.println("curr: " + curr.id);

                if(checkEquals){
                    //System.out.println("checking equals:");
                    if(depth >= prev_path.size()){
                        //System.out.println("dpeth overloaded");
                        checkEquals = false;
                    } else if(curr.id != prev_path.get(depth++)){
                        //System.out.println("not equal");
                        checkEquals = false;
                    }
                }

                curr_path.add(curr.id);

                if(curr.id == END){
                    iterationDone = true;
                    ////System.out.println("->on END");
                    break;
                }

                if(curr.conns.isEmpty()){
                    curr.h = Integer.MAX_VALUE;
                    iterationDone = true;
                    ////System.out.println("->on EMPTY");
                    break;
                }

                next = bestNext(iteration, curr.conns.iterator());

                if(next == null){ //every adjecent node has been visited in this iteration
                    iterationDone = true;
                    ////System.out.println("->on USED");
                    break;
                }

                best_cost = next.cost + next.node.h;

                if(curr.h < best_cost){
                    checkEquals = false;
                    curr.h = best_cost;
                }

                curr = next.node;
                bw.write(',');

                ////System.out.println("-> NEXT");
            }

            for(Integer pn : curr_path){
                //System.out.printf("%d\n", pn);
            }
            //System.out.println();

            if(checkEquals){
                equals = true;
            } else{
                prev_path = new ArrayList<Integer>(curr_path);
                curr_path.clear();
                checkEquals = true;
            }

            bw.write('\n');
        }
        bw.close();
    }

    private static Povezava bestNext(final int iteration, final Iterator<Povezava> iter){
        Povezava best = null;
        int best_cost = 0;

        Povezava curr;
        int cost;


        while(iter.hasNext()){ //find first viable connection
            curr = iter.next();
            if(curr.node.visited_on < iteration){ //we find a viable node and set it to best
                if(curr.node.h == Integer.MAX_VALUE){
                    best_cost = Integer.MAX_VALUE;
                } else{
                    best_cost = curr.cost + curr.node.h;
                }
                best = curr;
                break;
            }
        }

        while(iter.hasNext()){ //searches other viable connections
            curr = iter.next();
            if(curr.node.visited_on == iteration){
                continue;
            }

            if(curr.node.h == Integer.MAX_VALUE){
                cost = Integer.MAX_VALUE;
            } else{
                cost = curr.cost + curr.node.h;
            }

            if(cost < best_cost){
                best_cost = cost;
                best = curr;
            } else if(cost == best_cost){
                if(curr.node_id < best.node_id){
                    best = curr;
                }
            }
        }
        return best;
    }
}

class Povezava {
    public int node_id;
    public Node node;
    public int cost;

    public Povezava(int node_id, Node node, int cost) {
        this.node_id = node_id;
        this.node = node;
        this.cost = cost;
    }
}

class Node {
    public int id;
    public int h;
    public int visited_on;
    public ArrayList<Povezava> conns;

    public Node(int id) {
        this.id = id;
        this.h = 0;
        this.visited_on = -1;
        this.conns = new ArrayList<Povezava>();
    }
}