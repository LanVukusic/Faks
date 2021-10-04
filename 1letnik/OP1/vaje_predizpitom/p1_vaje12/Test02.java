
import java.util.List;

public class Test02 {

    public static void main(String[] args) {
        List<Integer> stevila = List.of(10, 7, 5, 21, 26, 14, 17, 9, 3, 15);
        System.out.println(Deljivost.pretvori(stevila, k -> k * k));
        System.out.println(Deljivost.pretvori(stevila, k -> (char) (k + 'A' - 1)));

        List<String> imena = List.of("Ana", "Bojan", "Cvetka", "Denis", "Eva", "Franci",
                    "Gregor", "Hilda", "Ivan", "Jasna", "Karel", "Lea",
                    "Mojca", "Nina", "Olga", "Peter", "Roman", "Sonja",
                    "Tone", "Urban", "Vinko", "Zoran");

        System.out.println(Deljivost.pretvori(imena, ime -> ime.toUpperCase()));
        System.out.println(Deljivost.pretvori(imena, ime -> ime.length()));
    }
}
