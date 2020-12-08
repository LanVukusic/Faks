import java.io.*;
import java.nio.charset.StandardCharsets;

public class Naloga2 {
    public static int visina;
    public static int dolzina;
    public static String naj_potka = ""; 

    public static void main(String[] args) throws IOException{
        long startTime = System.currentTimeMillis(); // Time measuring
        // read the input data
        FileReader fileIn = new FileReader(args[0]);
        //FileReader fileIn = new FileReader("in.txt");
        BufferedReader reader = new BufferedReader(fileIn);
        String[] line = reader.readLine().split(",");
        // yes we insecure af
        visina = Integer.parseInt(line[0]); //visina to je -- Y --
        dolzina = Integer.parseInt(line[1]); //sirina to je -- X --
        char[][] grid = new char[visina][dolzina];

        for (int y = 0; y < visina; y++) {
            String[] lineIn = reader.readLine().split(",");
            for (int x = 0; x < dolzina; x++) {
                grid[y][x] = lineIn[x].charAt(0);
            }
        }
        reader.close();


        // actually solving shit
        // obiskana polja lolz
        boolean[][] obiskana = new boolean[visina][dolzina];

        int naj_x = 0, naj_y =  0;
        int naj_len = 0;
        
        for (int y = 0; y < visina; y++) {
            for (int x = 0; x < dolzina; x++) {
                if(mybemybe(x,y,grid)){  // check if worth a shot
                    int a = rek(grid, x, y, 0, obiskana, grid[y][x]);
                    
                    if (a > naj_len){
                        naj_x = x;
                        naj_y = y;
                        naj_len = a;
                    }
                }
            }
        }

        rek_path(grid, naj_x, naj_y, 0, obiskana, grid[naj_y][naj_x], "", naj_len);

        // System.out.printf("%d,%d\n",naj_y, naj_x);
        // System.out.printf("%s\n", naj_potka.substring(1));
        // System.out.println("pedr");

        // long stopTime = System.currentTimeMillis();             // Time measuring
        // long elapsedTime = stopTime - startTime;                // Time measuring
        // System.out.println("> Ms elapsed: "  + elapsedTime);              // Time measuring

        // output file
        FileOutputStream out_f = new FileOutputStream(args[1]);
        OutputStreamWriter ssreamWriter = new OutputStreamWriter(out_f, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(ssreamWriter);

        bufferedWriter.append(String.format("%d,%d\n",naj_y, naj_x));
        bufferedWriter.append(String.format("%s\n", naj_potka.substring(1)));
        //bufferedWriter.append("\n");
        bufferedWriter.close();
        
    }

    public static boolean mybemybe(int x, int y, char[][] polje){
        if(x == 0 || y == 0 || x ==  dolzina - 1|| y == visina - 1) 
            return true;
        
        if((polje[y-1][x] == polje[y][x] && polje[y+1][x] == polje[y][x]) || (polje[y][x-1] == polje[y][x+1] && polje[y][x-1] == polje[y][x])){
            return false;
        }
        
        return true;
    }

    public static int rek(char[][] polje, int x, int y, int dolzina_poti, boolean[][] obiskana, char izbrana){
        // a smo legal?
        if(x<0 || y<0 || x>= dolzina || y >= visina) {
            return 0;
        }
        if(obiskana[y][x] || polje[y][x] != izbrana){
            return 0;
        }

        obiskana[y][x] = true;

        // do them checks
        int[] lolziballzy = {rek(polje, x+1, y, ++dolzina_poti, obiskana, izbrana),
        rek(polje, x-1, y, dolzina_poti, obiskana, izbrana),
        rek(polje, x, y+1, dolzina_poti, obiskana, izbrana),
        rek(polje, x, y-1, dolzina_poti, obiskana, izbrana)};
        
        

        int najdalsi = 0;
        for (int i : lolziballzy) {
            if(i > najdalsi){
                najdalsi = i;
            }
        }

        obiskana[y][x] = false;
        return najdalsi + 1;   
    }

    public static int rek_path(char[][] polje, int x, int y, int dolzina_poti, boolean[][] obiskana, char izbrana, String potka, int max_dolzina){
        
        /*
        if(naj_potka.length() != 0){
            return 0;   
        }
        */
        
        // a smo legal?
        if(x<0 || y<0 || x>= dolzina || y >= visina) {
            return 0;
        }
        if(obiskana[y][x] || polje[y][x] != izbrana){
            return 0;
        }

        if (dolzina_poti >= max_dolzina - 1) {
            naj_potka = potka;
            // System.out.println(naj_potka);
            return 0;
        }


        obiskana[y][x] = true;

        // do them checks
        int[] lolziballzy = {rek_path(polje, x+1, y, ++dolzina_poti, obiskana, izbrana, potka+",DESNO", max_dolzina),
        rek_path(polje, x-1, y, dolzina_poti, obiskana, izbrana, potka+",LEVO", max_dolzina),
        rek_path(polje, x, y+1, dolzina_poti, obiskana, izbrana, potka+",DOL", max_dolzina),
        rek_path(polje, x, y-1, dolzina_poti, obiskana, izbrana, potka+",GOR", max_dolzina)};

        int najdalsi = 0;
        for (int i : lolziballzy) {
            if(i > najdalsi){
                najdalsi = i;
            }
        }

        obiskana[y][x] = false;
        return najdalsi + 1;
    } 
}
