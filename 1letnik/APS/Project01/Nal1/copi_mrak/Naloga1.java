import java.io.*;

class Najpot {
    int cena;
    int[] array;
    int stPostank;

    public Najpot(int length) {
        this.array = new int[length];
    }
}

public class Naloga1 {
    private static int minCena;

    public static Najpot init(int cena, int postanki, int stCrpalk) { // inicializiramo objekt ki ga bomo polnili
        Najpot brt = new Najpot(stCrpalk);
        brt.stPostank = postanki;
        brt.cena = cena;
        minCena = cena;  // saj je trenutna cena ze izpolnila pogoj cena > minCena
        return brt;
    }

    public static int event(Najpot brez, Najpot z) {
        if (brez != null && z != null) {
            if (brez.cena > z.cena) {
                return 0;
            }
            if (brez.cena < z.cena) {
                return 1;
            }
            // cene sta ceni enaki in moramo preveriti postanke
            if (brez.stPostank > z.stPostank) {
                return 0;
            }
            return 1;
        } else if (brez != null && z == null) {
            return 1;
        } else if (z != null && brez == null) {
            return 0;
        }
        return -1;
    }

    public static Najpot opcije(int indeks, int gorivo, int[][] crpalke, int stCrpalk, int razdalja,
            int trenutnaRazdalja, int polnTank, int poraba, int cena, int postanki) {

        if (indeks != stCrpalk) {
            gorivo -= crpalke[indeks][0];
        } else {
            gorivo = gorivo - (razdalja - trenutnaRazdalja);
        }
        if (gorivo < 0 || cena > minCena) { 
            return null;
        }
        if (indeks == stCrpalk) {
            return init(cena, postanki, stCrpalk);
        }
        poraba += crpalke[indeks][0];

        Najpot opcijeB = opcije(indeks + 1, gorivo, crpalke, stCrpalk, razdalja, trenutnaRazdalja + crpalke[indeks][0],
                polnTank, poraba, cena, postanki);
        Najpot opcijeZ = opcije(indeks + 1, polnTank, crpalke, stCrpalk, razdalja,
                trenutnaRazdalja + crpalke[indeks][0], polnTank, 0, cena += poraba * crpalke[indeks][1], postanki + 1);

        switch (event(opcijeB, opcijeZ)) {
            case 0: // odkrili smo, da je v tem primeru boljsa izbira opcijeZ
                opcijeZ.array[indeks] = 1;
                return opcijeZ;
            case 1: // drugace je boljsa opcijeB
                return opcijeB;
        } // obe opciji nista veljavni, vrnemo null
        return null;
    }

    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);

        int dolzina, gorivo, n;
        int[][] crpalke;

        FileReader vhodna = new FileReader(args[0]);
        BufferedReader reader = new BufferedReader(vhodna);

        String[] line1 = reader.readLine().split(",");
        dolzina = Integer.parseInt(line1[0]);
        gorivo = Integer.parseInt(line1[1]);
        n = Integer.parseInt(line1[2]);
        crpalke = new int[n][2];

        for (int i = 0; i < n; i++) {
            String[] vrstica = reader.readLine().split(":");
            String[] vrstica2 = vrstica[1].split(",");
            crpalke[i][0] = Integer.parseInt(vrstica2[0]);
            crpalke[i][1] = Integer.parseInt(vrstica2[1]);
        }
        minCena = 9999999;

        Najpot solution = opcije(0, gorivo, crpalke, n, dolzina, 0, gorivo, 0, 0, 0);

        File file = new File(args[1]);
        file.delete();
        File fileNew = new File(args[1]);
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        PrintWriter pr = new PrintWriter(br);

        int vsota = 0;
        int first = 0;

        for (int i = 0; i < n; i++) {
            vsota += 1;
            if (solution != null) {
                if (solution.array[i] == 1) {
                    if (first != 0) {
                        pr.print(",");
                    }
                    first = 1;
                    pr.print(vsota);
                }
            }
        }
        pr.close();
        br.close();
        fr.close();
    }
}