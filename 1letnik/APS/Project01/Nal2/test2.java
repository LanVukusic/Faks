import java.io.*;

class Poskus {
    int koraki = 0;
    String path = "";

    public Poskus(int koraki, String path) {
        this.koraki = koraki;
        this.path = path;
    }
}

public class test2 {

    public static Poskus stejKorake(char[][] crke, char izbrana, int i, int j, int koraki, int[][] path, String potka) {
        int stuck = 1;
        Poskus desno = new Poskus(0, "");
        Poskus levo = new Poskus(0, "");
        Poskus gor = new Poskus(0, "");
        Poskus dol = new Poskus(0, "");
        if (koraki == 0) {
            path[i][j] = 1;
        }
        // desno
        if (j + 1 != crke[0].length) {
            if (crke[i][j + 1] == izbrana && path[i][j + 1] != 1) {
                path[i][j + 1] = 1;
                stuck = 0;
                desno = stejKorake(crke, izbrana, i, j + 1, koraki + 1, path,
                        potka == "" ? potka + "DESNO" : potka + "," + "DESNO");
            }
        }
        // levo
        if (j - 1 >= 0) {
            if (crke[i][j - 1] == izbrana && path[i][j - 1] != 1) {
                path[i][j - 1] = 1;
                stuck = 0;
                levo = stejKorake(crke, izbrana, i, j - 1, koraki + 1, path,
                        potka == "" ? potka + "LEVO" : potka + "," + "LEVO");
            }
        }
        // gor
        if (i - 1 >= 0) {
            if (crke[i - 1][j] == izbrana && path[i - 1][j] != 1) {
                path[i - 1][j] = 1;
                stuck = 0;
                gor = stejKorake(crke, izbrana, i - 1, j, koraki + 1, path,
                        potka == "" ? potka + "GOR" : potka + "," + "GOR");
            }
        }
        // dol
        if (i + 1 != crke.length) {
            if (crke[i + 1][j] == izbrana && path[i + 1][j] != 1) {
                path[i + 1][j] = 1;
                stuck = 0;
                dol = stejKorake(crke, izbrana, i + 1, j, koraki + 1, path,
                        potka == "" ? potka + "DOL" : potka + "," + "DOL");
            }
        }
        Poskus obj = new Poskus(koraki, potka);
        if (stuck == 1) {
            path[i][j] = 0;
            return obj;
        }
        path[i][j] = 0;
        if (desno.koraki >= levo.koraki) {
            if (desno.koraki >= gor.koraki) {
                if (desno.koraki >= dol.koraki) {
                    return desno;
                }
                return dol;
            } else if (gor.koraki >= dol.koraki) {
                return gor;
            }
            return dol;
        } else if (levo.koraki >= gor.koraki) {
            if (levo.koraki >= dol.koraki) {
                return levo;
            }
            return dol;
        }
        return gor;
    }

    public static void main(String[] args) throws IOException {
       
        FileReader vhodna = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(vhodna);

        String[] line1 = reader.readLine().split(",");
        int x = Integer.parseInt(line1[0]);
        int y = Integer.parseInt(line1[1]);
        // Poskus[x][y][0] = desno
        // Poskus[x][y][1] = levo
        // Poskus[x][y][2] = gor
        // Poskus[x][y][3] = dol

        char[][] crke = new char[x][y];
        int special = 0;
        for (int i = 0; i < x; i++) { // vstavimo crke iz datotek v char array
            String line = reader.readLine();
            for (int j = 0; j < (y + y - 1); j++) {
                char ch = line.charAt(j);
                if (ch != ',') {
                    crke[i][special] = ch;
                    special++;
                }
            }
            special = 0;
        }

        // int[][] array = new int[x][y];
        Poskus max = new Poskus(0, "");
        int indeks1 = 0;
        int indeks2 = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                char izbrana = crke[i][j];
                int[][] arrayPath = new int[x][y];
                Poskus stKorakov = stejKorake(crke, izbrana, i, j, 0, arrayPath, "");
                // System.out.println(stKorakov);
                if (stKorakov.koraki > max.koraki) {
                    max = stKorakov;
                    indeks1 = i;
                    indeks2 = j;
                }

            }
        }
        // int maks = 0;
        File file = new File(args[1]);
        file.delete();
        File fileNew = new File(args[1]);
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        PrintWriter pr = new PrintWriter(br);
        pr.println(indeks1 + "," + indeks2);
        pr.println(max.path);
        pr.close();
        br.close();
        fr.close();
        // System.out.println("(" + indeks1 + ", " + indeks2 + ") " + max.path);

    }
}