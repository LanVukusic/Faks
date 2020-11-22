import java.io.*;
//import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

class Option {
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

class LinkNode {
    Option data;
    LinkNode next;
    LinkNode prev;

    public LinkNode(Option item) {
        data = item;
    }
}

class LinkedList {
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


public class Naloga1 {
    static int allTimeMin = Integer.MAX_VALUE;
    static int allTimeMinStops = Integer.MAX_VALUE;

    static int numPetrols, roadLen, maxTank;
    static int[][] petrolStops;
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

        // solve problems
        rek(0,0,0,maxTank);



        // output file
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

    public static void rek (int priceByNow,int stopIndex, int StopsCount, int length, int gasLeft){
        if(priceByNow > allTimeMin){
            return;
        }

        // have we reached the final stop?
        if (stopIndex == numPetrols){
            if( gasLeft > 0){  // we had enough gas to reach the end
                if(priceByNow < allTimeMin){
                    allTimeMin = priceByNow;
                    allTimeMinStops = StopsCount;
                }
                if(priceByNow == allTimeMin){
                    if(StopsCount < allTimeMinStops){
                        allTimeMinStops = StopsCount;
                    }
                }
            }
            return;
        }


        // refill
        rek()

        // dont refill

    }
}
