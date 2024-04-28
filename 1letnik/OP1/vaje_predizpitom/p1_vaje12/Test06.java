
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Test06 {

    public static void main(String[] args) {
        izpisi(Deljivost.stevilo2delitelji(1, 10));
        izpisi(Deljivost.stevilo2delitelji(20, 50));
        izpisi(Deljivost.stevilo2delitelji(70, 69));
    }

    private static void izpisi(Map<Integer, List<Integer>> st2del) {
        for (int stevilo: new TreeSet<Integer>(st2del.keySet())) {
            System.out.printf("%d -> %s%n", stevilo, st2del.get(stevilo));
        }
        System.out.println();
    }
}
