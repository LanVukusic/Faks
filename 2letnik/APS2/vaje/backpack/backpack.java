import java.util.LinkedList;

public class backpack {
    public static void main(String[] args) {
        int Volume = 8;
        int[] v = new int[] { 2, 2, 3, 2, 4 };
        int[] c = new int[] { 2, 2, 2, 3, 4 };

        LinkedList<Integer>[] memo = new LinkedList[Volume];
        for (int i = 0; i < memo.length; i++) {
            memo[i] = new LinkedList<>();
        }
    }
}
