import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

class Edge {

    int key;
    int price;

    public Edge(int key, int price) {
        this.key = key;
        this.price = price;
    }
}

class Graph {
    int stVozlisc;
    LinkedList<Edge> adjList[];

    public Graph(int stVozlisc) {
        this.stVozlisc = stVozlisc;
        this.adjList = new LinkedList[stVozlisc];

        for (int i = 0; i < stVozlisc; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int source, Edge v) {
        adjList[source].add(v);
    }
}

public class mrak {
    public static void main(String[] args) throws IOException {

        FileReader vhodna = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(vhodna);
        // double cas = System.currentTimeMillis();

        String line1 = reader.readLine();
        int numOfconnections = Integer.parseInt(line1);

        int max = 6000;
        // tole bo treba z grafi rešit brt
        Graph g = new Graph(max);

        for (int i = 0; i < numOfconnections; i++) {
            String[] lineC = reader.readLine().split(",");
            int vozlisce1 = Integer.parseInt(lineC[0]);
            int vozlisce2 = Integer.parseInt(lineC[1]);
            int price = Integer.parseInt(lineC[2]);

            Edge v = new Edge(vozlisce2, price);

            g.addEdge(vozlisce1, v);
        }
        String[] lineL = reader.readLine().split(",");
        int start = Integer.parseInt(lineL[0]);
        int finnish = Integer.parseInt(lineL[1]);

        int[] ocene = new int[finnish];

        boolean noChange = false;  // tle bomo gledal ce so se ocene v eni iteraciji ohranile ali spremenile
        int naKoncu = 0;  // tle gledamo ce smo ze prsli na konec grafa
        int s = 0;

        // print file
        FileOutputStream file = new FileOutputStream(args[1]);
        OutputStreamWriter fr = new OutputStreamWriter(file, StandardCharsets.UTF_8);
        BufferedWriter br = new BufferedWriter(fr);
        PrintWriter pr = new PrintWriter(br);

        while(true){
            int indeks = start;

            if (noChange){  // smo koncali
                break;
            }
            noChange = true;
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
                    noChange = false;
                    ocene[indeks - 1] = Integer.MAX_VALUE;
                    break;
                }
                else if (s != 1){  // če ima najmanj en izhod a so ze vsa obiskana
                    break;
                }
                else if (ocene[indeks - 1] < (min.price + ocene[min.key - 1])){  // ce je potrebno posodobiti hevristicno oceno  *** spremenili iz != na <
                    noChange = false;       // naredili smo spremembo tako da ni to zadnja iteracija
                    System.out.printf("updatamo ceno iz %d v %d\n", ocene[indeks - 1], min.price + ocene[min.key - 1]);
                    ocene[indeks - 1] =  min.price + ocene[min.key - 1];
                }
                System.out.printf("gremo iz %d v %d\n", indeks,  min.key);
                indeks = min.key;
                
            }
            // System.out.println();
            pr.println();
        }
        pr.close();
        br.close();
        fr.close();
    }
}