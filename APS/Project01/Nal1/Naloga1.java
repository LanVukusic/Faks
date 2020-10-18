import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

public class Naloga1 {
    static int allTimeMin = Integer.MAX_VALUE;
    static int numPetrols, roadLen, maxTank, index;
    static int[][] petrolStops;

    static class Option {
        public int price = 0;
        public int dist = 0;
        public int gas = 0;
        public int[] stops;

        public Option (int price, int dist, int gas, int[] stops){
            this.price = price;
            this.dist = dist;
            this.gas = gas;
            this.stops = stops;
        }
    }

    static class LinkNode {
        Option data;
        LinkNode next;
        LinkNode prev;

        public LinkNode(Option item) {
            data = item;
        }
    }

    static class LinkedList {
        LinkNode head;
        public LinkedList(Option item) {
            this.head = new LinkNode(item);
            this.head.prev = null;
            this.head.next = null;
        }

        public void add(Option item) {
            // lets just add it at the beginning
            LinkNode temp = new LinkNode(item);
            temp.next = this.head;
            temp.prev = null;
            this.head.prev = temp;
            this.head = temp;
        }

        public void remove(LinkNode node){
            // if its first
            if(node.prev == null){
                this.head = node.next;
                return;
            }
            
            // else
            node.prev.next = node.next;
            node = null;
            return;
        }
    }

    public static void main(String[] args) throws IOException {
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        roadLen = Integer.parseInt(line[0]); // road length
        maxTank = Integer.parseInt(line[1]); // max tank
        numPetrols = Integer.parseInt(line[2]); // number of stations
        petrolStops = new int[numPetrols][2];

        for (int i = 0; i < numPetrols; i++) {
            String lineIn = reader.readLine().split(":")[1];
            petrolStops[i][0] = Integer.parseInt(lineIn.split(",")[0]);
            petrolStops[i][1] = Integer.parseInt(lineIn.split(",")[1]);
        }
        reader.close();


        int currDist = 0;
        LinkedList options = new LinkedList(new Option(0, 0, maxTank, new int[0]));
        for (int stopIndex = 0; stopIndex < numPetrols; stopIndex ++){
            currDist += petrolStops[stopIndex][0];
            LinkNode item = options.head;
            do {
                if(item.data.dist + item.data.gas >= currDist){
                    // we either continue without refill
                    item.data.dist += petrolStops[stopIndex][0];
                    item.data.gas -= petrolStops[stopIndex][0];

                    // we branch and refill
                    int newPrice = item.data.price + petrolStops[stopIndex][1] * (maxTank - item.data.gas);
                    int[] my_stops = new int[ item.data.stops.length + 1];
                    for (int i = 0; i < item.data.stops.length; i++) {
                        my_stops[i] = item.data.stops[i];
                    }
                    my_stops[item.data.stops.length] = stopIndex;
                    options.add(new Option(newPrice, item.data.dist, maxTank, my_stops.clone()));
                    item = item.next;
                }else{
                    LinkNode temp = item.next;
                    options.remove(item);
                    item = temp;
                }
            } while (item != null);
        }

        LinkNode item = options.head;
        int minPrice = Integer.MAX_VALUE, minLen = Integer.MAX_VALUE;
        int[] out = new int[0];
        do {
            if(item.data.dist + item.data.gas >= roadLen){
                if(item.data.price <= minPrice){
                    // compare length
                    if(item.data.price == minPrice){
                        if(item.data.stops.length < minLen){
                            minLen = item.data.stops.length;
                            out = item.data.stops;
                        }
                    }else{
                        minPrice = item.data.price;
                        minLen = item.data.stops.length;
                        out = item.data.stops;
                    }
                }    
            }
            item = item.next;
        }while (item != null);

        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);

        bufferedWriter.append(String.valueOf((out[0]+1)));
        for(int i = 1; i < out.length; i++){
            bufferedWriter.append(",");
            bufferedWriter.append(String.valueOf((out[i])+1));
        }
        bufferedWriter.close();
    } 
}
