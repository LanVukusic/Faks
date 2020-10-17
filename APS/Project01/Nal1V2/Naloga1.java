//package Nal1V2;
import java.io.*;

public class Naloga1 {
    static int allTimeMin = Integer.MAX_VALUE;
    static int numPetrols, roadLen, maxTank, index;
    static int[][] petrolStops;

    public static void main(String[] args) throws IOException{
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        roadLen = Integer.parseInt(line[0]);  // road length
        maxTank = Integer.parseInt(line[1]);  // max tank
        numPetrols = Integer.parseInt(line[2]);  // number of stations
        petrolStops = new int[numPetrols][2];

        for (int i = 0; i < numPetrols; i++) {
            String lineIn = reader.readLine().split(":")[1];
            petrolStops[i][0] = Integer.parseInt(lineIn.split(",")[0]);
            petrolStops[i][1] = Integer.parseInt(lineIn.split(",")[1]);
        }

        int[] res = rek(0,0, new int[0], maxTank-petrolStops[0][0], petrolStops[0][0]);
        for (int i : res) {
            System.out.println(i + 1);
        }
    }

    public static int[] rek (int station, int price, int[] stops, int gas, int distance){
        // can i even be here with my gas?
        if (gas < 0){
            return null;
        }

        // if we already miss the lowest limit, we can return null
        if(price > allTimeMin){
            return null;
        }

        System.out.printf("checking station %d \n",station + 1);

        // if we reach last stop
        if (station == numPetrols - 1){  // minus 1 cuz arrays start at 0
            // check if we can go for the finish line
            if(distance + gas > roadLen){
                // great we can go there without refill
                allTimeMin = price;
                return stops;
            }else{
                // can we go there with a refill
                if(distance + maxTank > roadLen){
                    int[] a = new int[stops.length + 1];
                    for (int i = 0; i < stops.length; i++) {
                        a[i] = stops[i];
                    }
                    a[stops.length] = station;
                    price += (maxTank - gas)* petrolStops[station][1];
                    if(price <= allTimeMin){
                        allTimeMin = price;
                        return a;
                    }else{
                        return null;    
                    }
                    
                }
            }
            return null;  // we cant reach it even with a refill
        }else{
            // we have to go further with either refilling or not
            // dont refill
            int[] a = rek(station + 1, price, stops, (gas - petrolStops[station+1][0]), (distance+petrolStops[station+1][0]));
            
            // refill
            int priceOfGas = (maxTank - gas) * petrolStops[station][1];
            int[] newStops = new int[stops.length + 1];
            for (int i = 0; i < stops.length; i++) {
                newStops[i] = stops[i];
            }
            newStops[stops.length] = station;
            int[] b = rek(station + 1, price + priceOfGas, newStops, maxTank, (distance+petrolStops[station+1][0]));

            // we return either null or some option if any of them are null
            if (a == null){
                return b;
            }
            if(b == null){
                return a;
            }

            // if not, we return the cheapest version
            int priceA = Integer.MIN_VALUE, priceB = Integer.MIN_VALUE;
            for (int i = 0; i < a.length; i++){
                priceA += petrolStops[a[i]][1];
            }
            for (int i = 0; i < b.length; i++){
                priceB += petrolStops[b[i]][1];
            }
            if(priceA > priceB){
                return b;
            }else{
                if (priceA == priceB){
                    if(a.length > b.length){
                        return b;
                    }else{
                        return a;
                    }
                }
                return a;
            }
        }
    }
}
