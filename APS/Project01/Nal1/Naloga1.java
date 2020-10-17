import java.io.*;
import java.util.Scanner;

// create an object with possible options
class Road {
    public int price = 0;
    public int distance = 0;
    public int tank = 0;
    public String stations = "";
    public int numStations = 0;

    Road(int tank) {
        this.tank = tank;
    }
}

public class Naloga1 {
    // here we do a sad compensation for an ArrayList
    Road[] addListLike(Road[] coll, Road entry, int index)  {
        if (index >= coll.length) { // cool we can ust add it.
            Road[] tempColl = new Road[coll.length * 2];
            for (int i = 0; i < coll.length; i++) {
                tempColl[i] = coll[i];
            }
            tempColl[index] = entry;
            return tempColl;
        } else {
            coll[index] = entry;
            return null;
        }
    }

    public static void main(String[] args) {
        int roadLen, maxTank, numPetrols, index;
        int[][] petrolStops;
        Road[] options;

        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        roadLen = Integer.parseInt(line[0]);  // road length
        maxTank = Integer.parseInt(line[1]);  // max tank
        numPetrols = Integer.parseInt(line[2]);  // number of stations

        for (int i = 0; i < numPetrols; i++) {
            String lineIn = reader.readLine().split(":")[1];
            petrolStops[i][0] = Integer.parseInt(lineIn.split(",")[0]);
            petrolStops[i][1] = Integer.parseInt(lineIn.split(",")[1]);
        }

        int currRoadLen = 0;
        options[0] = new Road(maxTank);
        index = 1;

        // loop over stations
        for (int i = 0; i < numPetrols; i++) {
            int currStopPrice = petrolStops[i][1]; // price per unit of current stop
            currRoadLen += petrolStops[i][0]; // distance to this station
            System.out.println();
            System.out.printf("station : %d\n", i + 1);

            // loop over options
            int tempindex = index;
            // loop over all paths
            for (int j = 0; j < tempindex; j++) {
                // check for every valid path by now
                if (options[j].distance + options[j].tank >= currRoadLen) { // this is a valid path taken, since it has
                                                                            // enough to reach next stop
                    // since we have enough gas to make a move we do it
                    // we make a move so we burn gass
                    options[j].tank -= petrolStops[i][0];
                    options[j].distance += petrolStops[i][0];

                    // we branch
                    options[index] = new Road(maxTank);
                    // fill the tank
                    options[index].price += currStopPrice * (maxTank - options[j].tank);
                    options[index].numStations = options[j].numStations + 1;
                    options[index].stations = options[j].stations.concat(String.valueOf(i + 1));

                    // move the car to this stop
                    options[index].distance = options[j].distance;

                    // increase index
                    index++;
                } else {
                    System.out.printf("option %d is dead\n", j);
                }
            }

        }

        int minPrice = Integer.MAX_VALUE;
        int minStops = Integer.MAX_VALUE;
        int win = 0;

        // check what could reach the end
        for (int j = 0; j < index; j++) {
            if (options[j].distance + options[j].tank >= roadLen) { // that path could reach the end
                System.out.println(j);
                System.out.println(options[j].stations);
                System.out.println(options[j].price);
                System.out.println("------");
                if (options[j].price < minPrice) {
                    minPrice = options[j].price;
                    minStops = options[j].numStations;
                    win = j;
                } else {
                    if (options[j].price == minPrice) {
                        if (options[j].numStations < minStops) {
                            minPrice = options[j].price;
                            minStops = options[j].numStations;
                            win = j;
                        }
                    }
                }
            }
        }

        // write out
        // System.out.println("returninn");
        // System.out.println(options[win].stations);
        return;

    }
}
