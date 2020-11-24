import java.io.*;
import java.nio.charset.StandardCharsets;


class Option {
    public int price = 0;
    public int stopsCount = 0;
    public String path;

    public Option(int price, int stopsCount, String path) {
        this.price = price;
        this.stopsCount = stopsCount;
        this.path = path;
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
        Option a = rek(0, 0, 0, 0, maxTank);
        // System.out.println(a.path.substring(1));

        // output file
        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);
        bufferedWriter.append(a.path.substring(1));
        bufferedWriter.append("\n");
        bufferedWriter.close();

    }

    public static Option rek (int priceByNow, int stopIndex, int StopsCount, int dist, int gasLeft){
        if(stopIndex != numPetrols){
            gasLeft -= petrolStops[stopIndex][0];
            dist += petrolStops[stopIndex][0];
        }

        // we can imediatly exit if we run out of gas or exceed previous best
        if(gasLeft < 0 || priceByNow > allTimeMin){  
            return null;
        }

        // if we have reached the end, we can start building path
        // have we reached the final stop?
        if (stopIndex == numPetrols){
            // check if we can do the last few miles
            if(roadLen-dist > gasLeft){
                return null;
            }
            allTimeMin = priceByNow;
            return new Option(priceByNow,StopsCount,"");
        }


        // don't refill
        Option withNoRefill = rek(  priceByNow,           stopIndex+1, StopsCount,   dist, gasLeft);
        

        // refill
        //               liters to refill     price/liter
        int price2pay = (maxTank - gasLeft) * petrolStops[stopIndex][1];
        Option withRefill = rek(priceByNow+price2pay, stopIndex+1, StopsCount+1, dist, maxTank);
        if(withRefill != null){
            withRefill.path = ","+(stopIndex+1)+withRefill.path;
        }


        if(withRefill == null){
            return withNoRefill;
        }
        if(withNoRefill == null){
            return withRefill;
        }

        // else, we see that both options are viable, so we take the cheaper / better version
        if (withRefill.price > withNoRefill.price){
            return withNoRefill;
        }
        if (withRefill.price < withNoRefill.price){
            return withRefill;
        }

        if(withNoRefill.stopsCount > withRefill.stopsCount){
            return withRefill;
        }
        return withNoRefill;
    }
}
