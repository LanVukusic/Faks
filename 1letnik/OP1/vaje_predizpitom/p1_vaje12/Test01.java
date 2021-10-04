
import java.util.List;

public class Test01 {

    public static void main(String[] args) {
        List<Integer> stevila = List.of(10, 7, 5, 21, 26, 14, 17, 9, 3, 15);
        System.out.println(Deljivost.filtriraj(stevila, k -> k >= 10));
        System.out.println(Deljivost.filtriraj(stevila, k -> k % 2 == 0));

        List<String> imena = List.of("Ana", "Bojan", "Cvetka", "Denis", "Eva", "Franci",
                    "Gregor", "Hilda", "Ivan", "Jasna", "Karel", "Lea",
                    "Mojca", "Nina", "Olga", "Peter", "Roman", "Sonja",
                    "Tone", "Urban", "Vinko", "Zoran");

        System.out.println(Deljivost.filtriraj(imena, ime -> ime.length() == 5));
        System.out.println(Deljivost.filtriraj(imena, 
                    ime -> ime.compareTo("D") >= 0 && ime.compareTo("N") <= 0));
    }
}
