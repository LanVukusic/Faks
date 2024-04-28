import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class NALOGAcop{
    private static int[][] benc;
    private static int min;
    private static int GAS;
    private static int n;
    
    public static void main(String[] args) throws IOException{
        String in_path = args[0];
        String out_path = args[1];

        FileReader in = new FileReader(in_path);
        BufferedReader bf = new BufferedReader(in);
        FileOutputStream out = new FileOutputStream(out_path);
        OutputStreamWriter sw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(sw);

        String[] line = bf.readLine().split(",");

        int d = Integer.parseInt(line[0]); //dolzina poti
        GAS = Integer.parseInt(line[1]); //kapaciteta rezervarja
        n = Integer.parseInt(line[2]); //st. bencinskih

        int length = 0;

        benc = new int[n+1][2]; //an array of stations, each with distance and price data
        min = Integer.MAX_VALUE;

        //reading input:
        for(int i = 0; i < n; i++){
            line = bf.readLine().split(":")[1].split(",");
            benc[i][0] = Integer.parseInt(line[0]); // razdalja do benza
            benc[i][1] = Integer.parseInt(line[1]); // prodajna cena goriva
            length = length + benc[i][0];
        }

        //adding last stop to the array of stations
        benc[n][0] = d - length;
        benc[n][1] = 0;

        Path p = rek(0, GAS, 0, 0);
        int[] odg = p.path;

        bw.append(String.valueOf((odg[0]+1)));
        for(int i = 1; i < odg.length; i++){
            bw.append(",");
            bw.append(String.valueOf(String.valueOf((odg[i])+1)));
        }
        bw.newLine();

        bw.close();
        bf.close();
    }

    private static Path rek(int i, int g, int c, int filled){
        int old_c = c;

        //see how much tank we have left, if under 0 return
        g = g - benc[i][0];
        if(g < 0){
            return null;
        }

        //if price until now exceeds the min price, it's pointless to continue
        if(c > min){
            return null;
        }

        //the new price if we fill here (used when called rek wihth "fill" parameters)
        c = c + (GAS-g)*benc[i][1];

        //if we are in the last node, we can return an empty array, which will get filled on the way back
        if(i == n){
            min = c;
            Path p = new Path(filled);
            p.cost = c;
            return p;
        }

        Path no_fill = rek(i+1, g, old_c, filled); //g gets passed on, price is the old one

        Path fill = rek(i+1, GAS, c, filled+1); //g gets full tank and new price is passed on


        if(fill != null && no_fill == null){
            //if only fill is an array pass it on, with this layer (station) inclouded as it was filled here
            fill.path[fill.i--] = i;
            return fill;
        }
        if(fill == null && no_fill != null){
            //if only no_fill is an array pass it on without adding this layer (station)
            return no_fill;
        }
        if(fill != null && no_fill != null){
            if(fill.cost > no_fill.cost){
                return no_fill;
            }
            if(fill.cost < no_fill.cost){
                fill.path[fill.i--] = i;
                return fill;
            }
            //if above two conditions were not met, costs are equal -> comparing lengths
            if(fill.path.length < no_fill.path.length){
                fill.path[fill.i--] = i;
                return fill;
            } else{
                return no_fill;
            }
        }

        //if both, fill and no_fill, are null, we return null
        return null;
    }
}

class Path{
    public int cost;
    public int[] path;
    public int i;
    public Path(int n){
        this.path = new int[n];
        this.i = n-1;
    }
}