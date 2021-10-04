import java.util.Scanner;
import java.util.TreeSet;

class Stanje implements Comparable<Stanje> {
    int volumn;
    int cena;

    public Stanje(int w, int v) {
        this.volumn = w;
        this.cena = v;
    }

    public int compareTo(Stanje other) {
        if (this.volumn == other.volumn) return other.cena - this.cena;
        return this.volumn - other.volumn;
    }

    public Stanje add(Stanje p) {
        return new Stanje(this.volumn + p.volumn, this.cena + p.cena);
    }

    public String toString() {
        return String.format("(%d, %d)", this.volumn, this.cena);
    }
}

public class Izziv11 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int prostornina = sc.nextInt();

        int n = sc.nextInt();
        TreeSet<Stanje> drevo = new TreeSet<Stanje>();
        drevo.add(new Stanje(0,0));
        System.out.println("0: (0, 0)");
        for (int i = 0; i < n; i++) {
            Stanje prebrano = new Stanje(sc.nextInt(), sc.nextInt());

            TreeSet<Stanje> toAdd = new TreeSet<Stanje>();
            for (Stanje p : drevo) {
                Stanje novo = p.add(prebrano);

                if (novo.volumn <= prostornina) toAdd.add(novo);
            }
            drevo.addAll(toAdd);

            // rezanje
            TreeSet<Stanje> toRemove = new TreeSet<Stanje>();
            int meja = -1;
            int maxCena = -1;
            for (Stanje p : drevo) {
                if (p.volumn != meja) {
                    meja = p.volumn;
                    if (p.cena <= maxCena) toRemove.add(p);
                    else maxCena = p.cena;
                } else {
                    toRemove.add(p);
                }
            }
            drevo.removeAll(toRemove);

            
            System.out.print((i + 1) + ": ");
            boolean first = true;
            for (Stanje p : drevo) {
                if (first) System.out.print(p);
                else System.out.print(" " + p);
                first = false;
            }
            System.out.println();
        }


        sc.close();
    }
}
